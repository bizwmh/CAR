/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.io;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import biz.car.SYS;
import biz.car.bundle.MSG;

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

	private final String name;
	private Map<WatchKey, PathListenerPair> registrations;
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
		running = new AtomicBoolean(false);
		name = aName;
	}

	/**
	 * Registers a directory path with a listener for monitoring file system events.
	 * The listener will be called whenever CREATE, DELETE, or MODIFY events occur
	 * in the specified directory.
	 * 
	 * @param aPath     the directory path to monitor
	 * @param aListener the listener to notify when events occur
	 * @throws IOException              if the path cannot be registered with the
	 *                                  WatchService
	 * @throws IllegalArgumentException if the path is not a directory
	 */
	public void register(Path aPath, DirectoryListener aListener) {
		if (!Files.isDirectory(aPath)) {
			throw SYS.LOG.exception(MSG.PATH_NOT_DIRECTORY, aPath);
		}

		try {
			WatchKey l_key = aPath.register(
			      watchService,
			      StandardWatchEventKinds.ENTRY_CREATE,
			      StandardWatchEventKinds.ENTRY_DELETE,
			      StandardWatchEventKinds.ENTRY_MODIFY);

			synchronized (registrations) {
				registrations.put(l_key, new PathListenerPair(aPath, aListener));
			}
		} catch (IOException anEx) {
			throw SYS.LOG.exception(anEx);
		}
	}

	/**
	 * Starts the watch service in a background thread. This method will begin
	 * monitoring all registered directories.
	 */
	public void start() {
		if (running.compareAndSet(false, true)) {
			try {
				watchService = FileSystems.getDefault().newWatchService();
				registrations = new HashMap<>();
				watchThread = new Thread(this::watch, name);
				watchThread.setDaemon(true);
				watchThread.start();
			} catch (IOException anEx) {
				SYS.LOG.error(anEx);
				;
			}
			SYS.LOG.info(MSG.WATCHER_STARTED, name);
		}
	}

	/**
	 * Stops the watch service and releases all resources.
	 * 
	 * @throws IOException if an error occurs while closing the WatchService
	 */
	public void stop() {
		if (running.compareAndSet(true, false)) {
			try {
				if (watchThread != null) {
					watchThread.interrupt();
				}
				watchService.close();
				SYS.LOG.info(MSG.WATCHER_STOPPED, name);
			} catch (IOException anEx) {
			}
		}
	}

	/**
	 * Main watch loop that processes events from the WatchService.
	 */
	private void watch() {
		while (running.get()) {
			WatchKey l_key;
			try {
				l_key = watchService.take();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			} catch (ClosedWatchServiceException e) {
				break;
			}

			PathListenerPair l_pair;
			synchronized (registrations) {
				l_pair = registrations.get(l_key);
			}

			if (l_pair == null) {
				l_key.reset();
				continue;
			}

			for (WatchEvent<?> l_event : l_key.pollEvents()) {
				try {
					l_pair.listener.onEvent(l_pair.path, l_event);
				} catch (Exception anEx) {
					SYS.LOG.error(anEx);
				}
			}

			boolean l_valid = l_key.reset();
			if (!l_valid) {
				synchronized (registrations) {
					registrations.remove(l_key);
				}
			}
		}
	}
}
