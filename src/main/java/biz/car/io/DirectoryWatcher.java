/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.io;

import java.io.IOException;
import java.nio.file.ClosedWatchServiceException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import biz.car.SYS;
import biz.car.XRuntimeException;
import biz.car.bundle.MSG;
import biz.car.util.Delay;

/**
 * Utility class for monitoring directory changes using the WatchService API.
 * This class allows registering directories with listeners that will be
 * notified when file system events occur.
 */
public class DirectoryWatcher {

	/**
	 * Internal class to pair a path with its listener.
	 */
	private static class PathListenerPair {
		final DirectoryListener listener;
		final Path path;

		PathListenerPair(Path aPath, DirectoryListener aListener) {
			this.path = aPath;
			this.listener = aListener;
		}
	}

	private static Object lock = new Object();

	private Map<WatchKey, PathListenerPair> keyToPath;
	private final String name;
	private AtomicBoolean running;
	private WatchService watchService;
	private Thread watchThread;

	/**
	 * Creates a new DirectoryWatcher instance.
	 * 
	 * @param aName the name for the watcher thread
	 */
	public DirectoryWatcher(String aName) {
		super();

		name = aName;
		keyToPath = new HashMap<WatchKey, PathListenerPair>();
		running = new AtomicBoolean(false);
	}

	/**
	 * Registers a directory path with a listener for monitoring file system events.
	 * The listener will be called whenever CREATE, DELETE, or MODIFY events occur
	 * in the specified directory.
	 * 
	 * @param aPath     the directory path to monitor
	 * @param aListener the listener to notify when events occur
	 * @throws XRuntimeException if the path cannot be registered with the
	 *                           WatchService
	 */
	public void register(Path aPath, DirectoryListener aListener) {
		if (!Files.isDirectory(aPath)) {
			throw SYS.LOG.exception(MSG.WATCHER_PATH_NOT_DIRECTORY, aPath);
		}

		try {
			WatchKey l_key = aPath.register(
			      getService(),
			      StandardWatchEventKinds.ENTRY_CREATE,
			      StandardWatchEventKinds.ENTRY_DELETE,
			      StandardWatchEventKinds.ENTRY_MODIFY);

			synchronized (lock) {
				if (!keyToPath.containsKey(l_key)) {
					keyToPath.put(l_key, new PathListenerPair(aPath, aListener));
					SYS.LOG.info(MSG.WATCHER_PATH_REGISTERED, aPath, name);
				}
			}
		} catch (Exception anEx) {
			throw SYS.LOG.exception(anEx);
		}
	}

	/**
	 * Registers a directory path and all subdirectories with a listener for
	 * monitoring file system events. The listener will be called whenever CREATE,
	 * DELETE, or MODIFY events occur in the specified directory.
	 * 
	 * @param aPath     the directory path to monitor
	 * @param aListener the listener to notify when events occur
	 * @throws XRuntimeException if the path cannot be registered with the
	 *                           WatchService
	 */
	public void registerAll(Path aPath, DirectoryListener aListener) {
		try {
			Files.walkFileTree(aPath, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs)
				      throws IOException {
					register(aPath, aListener);

					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException anEx) {
			throw SYS.LOG.exception(anEx);
		} catch (XRuntimeException anEx) {
			throw anEx;
		}
	}

	/**
	 * Starts the watch service in a background thread. This method will begin
	 * monitoring all registered directories.
	 */
	public void start() {
		if (running.compareAndSet(false, true)) {
			watchThread = new Thread(this::watch, name);
			watchThread.setDaemon(true);
			watchThread.start();
			SYS.LOG.info(MSG.WATCHER_STARTED, name);
		}
	}

	/**
	 * Stops the watch service and releases all resources.
	 */
	public void stop() {
		if (running.compareAndSet(true, false)) {
			try {
				if (watchThread != null) {
					if (watchService != null) {
						watchService.close();
					}
				}
			} catch (IOException anEx) {
				watchThread.interrupt();
			}
		}
	}

	private void cleanup() {
			try {
				keyToPath.clear();
				watchService.close();
			} catch (IOException anEx) {
			}
	}

	/**
	 * Main watch loop that processes events from the WatchService.
	 */
	private void watch() {
		WatchKey l_key;
		PathListenerPair l_pair;
		WatchService l_svc = getService();

		try {
			while (running.get()) {
				l_key = l_svc.take();
				
				// wait some miliiseconds to see if more events occurred
				Delay.millis(500);

				// pick up all events
				List<WatchEvent<?>> l_events = l_key.pollEvents();

				synchronized (lock) {
					l_pair = keyToPath.get(l_key);
				}
				if (l_pair == null) {
					l_key.reset();
					continue;
				}
				l_pair.listener.onEvent(l_pair.path, l_events);
				l_key.reset();
			}
		} catch (InterruptedException anEx) {
			Thread.currentThread().interrupt();
		} catch (ClosedWatchServiceException anEx) {
		} catch (XRuntimeException anEx) {
		} catch (Exception anEx) {
			SYS.LOG.exception(anEx);
		} finally {
			SYS.LOG.info(MSG.WATCHER_STOPPED, name);
			cleanup();
		}
	}

	private WatchService getService() {
		if (watchService == null) {
			try {
				watchService = FileSystems.getDefault().newWatchService();
			} catch (IOException anEx) {
				throw SYS.LOG.exception(anEx);
			}
		}
		return watchService;
	}
}
