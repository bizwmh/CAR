/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.util;

import java.util.function.Function;

import biz.car.SYS;

/**
 * Creates a wait in current thread.
 * 
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface Delay {

	/**
	 * @return the period of time
	 */
	Function<String, Long> Period = aPeriod -> {
		String l_period = aPeriod;
		int l_len = l_period.length();
		long l_ret = 0;

		if (l_len > 1) {
			try {
				String l_last = l_period.substring(l_len - 1, l_len);
				long l_factor = 1;
				int l_ind = "smhd".indexOf(l_last); //$NON-NLS-1$

				switch (l_ind) {
				case -1:
					break;
				case 0:
					l_factor = 1000;
					l_period = l_period.substring(0, l_len - 1);
					break;
				case 1:
					l_factor = 60 * 1000;
					l_period = l_period.substring(0, l_len - 1);
					break;
				case 2:
					l_factor = 60 * 60 * 1000;
					l_period = l_period.substring(0, l_len - 1);
					break;
				case 3:
					l_factor = 24 * 60 * 60 * 1000;
					l_period = l_period.substring(0, l_len - 1);
					break;
				default:
					l_factor = 1;
				}
				l_ret = Long.valueOf(l_period);
				l_ret = l_ret * l_factor;
			} catch (Exception anEx) {
				throw SYS.LOG.exception(anEx);
			}
		}
		return l_ret;
	};

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
