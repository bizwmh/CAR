/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.util;

/**
 * Creates a wait in current thread.
 * 
 * @version 1.0.0 16.10.2024 15:24:10
 */
public interface Delay {

	/**
	 * A wait in milliseconds.
	 * 
	 * @param aLong the number of milliseconds.
	 */
	static void millis(long aLong) {
		try {
			Thread.sleep(aLong);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * A wait in seconds.
	 * 
	 * @param aSecs the number of seconds.
	 */
	static void seconds(int aSecs) {
		try {
			Thread.sleep(aSecs * 1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
