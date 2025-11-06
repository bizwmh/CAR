/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car;

import biz.car.bundle.MSG;

/**
 * General purpose interface for executing a set of operations:<br>
 * <ul>
 * <li>the <code>exec</code> method provides the functionality of the runnable
 * <li>the <code>run</code> method invokes <code>exec</code> but surrounds
 * <code>exec</code> by a try-catch block
 * </ul>
 * The <code>dispose</code> method performs the final clean up.
 *
 * @version 2.0.0 06.10.2025 18:13:05
 */
public interface XRunnable extends Runnable, XLogger {

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
	 * Notification about an abnormal termination of the exec.<br>
	 * Only called in case of an uncaught exception.
	 * 
	 * @param anEx the exception cause
	 */
	default void onAbend(Throwable anEx) {
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

			throw anEx;
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
	 * Sends a start message to the exec log.
	 */
	default void startMessage() {
		info(MSG.EXEC_STARTED, getName());
	}
}
