/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.wmh.car;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import biz.wmh.car.bundle.MSG;
import biz.wmh.car.config.Configurable;

/**
 * General purpose interface for executing a set of operations:<br>
 * <ul>
 * <li>the <code>exec</code> method provides the functionality of the runnable
 * <li>the <code>run</code> method invokes <code>exec</code> but surrounds
 * <code>exec</code> by a try-catch block
 * <li>the <code>start</code> methods invoke <code>run</code> but started in its
 * own thread
 * </ul>
 * The <code>accept</code> method shall be called before the execution is
 * started. The <code>accept</code> method provides a configuration to further
 * initialize the runnable.<br>
 * The <code>dispose</code> method performs the final clean up.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface XRunnable extends Configurable, Runnable {

	/**
	 * Releases all allocated resources.<br>
	 * When this method has finished this <code>Runnable</code> instance shall no
	 * longer be usable.
	 */
	void dispose();

	/**
	 * Sends an end message to the exec log.
	 */
	default void endMessage() {
		info(MSG.EXEC_ENDED, getName());
	}

	/**
	 * Sends an error message to the exec log.
	 */
	default void errorMessage() {
		info(MSG.EXEC_ABENDED, getName());
	}

	/**
	 * Performs the operations of this <code>Exec</code>.
	 */
	void exec();

	/**
	 * @return the name for this <code>XConfig</code>
	 */
	default String getName() {
		return getClass().getSimpleName();
	}

	/**
	 * Waits for the completion of a <code>Future</code><br>
	 * Exceptions are caught, logged and do not terminate the scenario set.
	 * 
	 * @param aThread the <code>Future</code> to wait for
	 */
	default void join(Future<?> aThread) {
		try {
			// wait for the thread to terminate
			aThread.get();
		} catch (InterruptedException anEx) {
			// just terminate
		} catch (ExecutionException anEx) {
			Throwable l_ex = anEx.getCause();

			if (!(l_ex instanceof XRuntimeException)) {
				// log the exception
				l_ex = exception(l_ex);
			}
			throw (XRuntimeException) l_ex;
		}
	}

	/**
	 * Notification about an abnormal termination of the exec.<br>
	 * Only called in case of an uncaught exception.
	 * 
	 * @param anEx the exception cause
	 */
	default void onAbend(Throwable anEx) {
	}

	/**
	 * Rethrows an exception.
	 * 
	 * @param anEx the exception to rethrow
	 */
	default void rethrow(XRuntimeException anEx) {
		throw anEx;
	}

	/**
	 * Executes the individual operations of this <code>Runnable</code>.<br>
	 * This method just invokes the <code>exec</code> method, but wraps it into a
	 * try-catch block:
	 * <ul>
	 * <li><code>XRuntimeException</code>s are re-thrown
	 * <li><code>Throwable</code>s are written to the system log and a
	 * <code>XRuntimeException</code> is thrown
	 * </ul>
	 * <code>dispose</code> is called in a <code>finally</code> block.
	 * <p>
	 * 
	 * @throws XRuntimeException if an error occurs
	 */
	default void run() {
		Runnable OnEnd = () -> endMessage();
		Runnable OnError = () -> errorMessage();
		Runnable OnExit = OnEnd;

		try {
			startMessage();
			exec();
		} catch (XRuntimeException anEx) {
			OnExit = OnError;

			rethrow(anEx);
		} catch (Throwable anEx) {
			OnExit = OnError;

			onAbend(anEx);
			throw exception(anEx);
		} finally {
			OnExit.run();
			dispose();
		}
	}

	/**
	 * Starts the execution of this runnable as a completable future.<br>
	 * The name of this runnable is used as the name of the resulting thread.
	 * 
	 * @return the new <code>CompletableFuture</code>
	 */
	default CompletableFuture<Void> start() {
		CompletableFuture<Void> l_ret = start(getName());

		return l_ret;
	}

	/**
	 * Starts the execution of this runnable as a completable future.
	 * 
	 * @param anExecutor the executor to use for asynchronous execution
	 * @return the new <code>CompletableFuture</code>
	 */
	default CompletableFuture<Void> start(Executor anExecutor) {
		CompletableFuture<Void> l_ret = CompletableFuture.runAsync(this, anExecutor);

		return l_ret;
	}

	/**
	 * Starts the execution of this runnable as a completable future.
	 * 
	 * @param aName the name for the thread
	 * @return the new <code>CompletableFuture</code>
	 */
	default CompletableFuture<Void> start(String aName) {
		ThreadFactory l_tf = r -> {
			return new Thread(r, aName);
		};
		ExecutorService l_xs = Executors.newCachedThreadPool(l_tf);
		CompletableFuture<Void> l_ret = start(l_xs);

		return l_ret;
	}

	/**
	 * Sends a start message to the exec log.
	 */
	default void startMessage() {
		info(MSG.EXEC_STARTED, getName());
	}
}
