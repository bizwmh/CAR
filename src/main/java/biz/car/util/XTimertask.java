/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.util;

import java.util.Timer;
import java.util.TimerTask;

import com.typesafe.config.Config;

import biz.car.SYS;
import biz.car.XRunnable;
import biz.car.config.ConfigObject;

/**
 * A Java <code>TimerTask</code> controlled by an external configuration file
 * for the timer task parameters. <code>Timer</code> and <code>TimerTask</code>
 * are a 1:1 relationship.
 * <p>
 * The configuration parameters for the <code>XTimerTask</code> are
 * <ul>
 * <li><b>name</b>
 * <dl>
 * the name for the associated timer thread.<br>
 * Default is the class name of the timer task.
 * </dl>
 * <li><b>period</b>
 * <dl>
 * determines the time between successive task executions.<br>
 * The period is a number which may be followed by a single character (s, m or
 * h) indicating that the time is given as seconds, minutes or hours. Without a
 * suffix the number are milliseconds.<br>
 * Default is 0s which will execute the timer task just once.
 * </dl>
 * </ul>
 *
 * @version 1.0.0 10.02.2025 12:28:29
 */
public abstract class XTimertask extends ConfigObject implements XRunnable {

	private Timer myTimer;

	/**
	 * Creates a default <code>XTimertask</code> instance.
	 */
	public XTimertask() {
		super();
	}

	@Override
	public void accept(Config aConfig) {
		super.accept(aConfig);

		if (myTimer != null) {
			myTimer.cancel();
		}
		myTimer = new Timer(getName());
<<<<<<< HEAD
		TimerTask l_task = createTask(this);
		long l_period = getPeriod();

		myTimer.schedule(l_task, 0, l_period);
	}

	@Override
	public void dispose() {
		if (myTimer != null) {
			myTimer.cancel();

			myTimer = null;
		}
	}

	private TimerTask createTask(Runnable aTask) {
		return new TimerTask() {

			@Override
			public void run() {
				aTask.run();
=======
		TimerTask l_task = createTask();
		long l_period = getPeriod();

		myTimer.schedule(l_task, 0, l_period);
	}

	@Override
	public void dispose() {
		if (myTimer != null) {
			myTimer.cancel();

			myTimer = null;
		}
	}

	private TimerTask createTask() {
		return new TimerTask() {

			@Override
			public void run() {
				this.run();
>>>>>>> branch 'master' of https://github.com/bizwmh/CAR
			}
		};
	}

	/**
	 * @return the time between successive task executions
	 */
	private long getPeriod() {
		String l_period = getString(PERIOD, "0"); //$NON-NLS-1$
		int l_len = l_period.length();
		long l_ret = 0;

		if (l_len > 0) {
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
	}
}
