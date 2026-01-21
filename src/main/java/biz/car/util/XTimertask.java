/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.util;

import java.util.Timer;
import java.util.TimerTask;

import biz.car.bundle.MSG;
import biz.car.config.ConfigAdapter;

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
 * @version 2.0.0 08.01.2026 08:32:08
 */
public abstract class XTimertask extends ConfigAdapter implements Runnable {

	private Timer myTimer;

	/**
	 * Creates a default <code>XTimertask</code> instance.
	 */
	public XTimertask() {
		super();
	}

	/**
	 * Starts the execution of this timer task.
	 */
	public void start() {
		if (myTimer != null) {
			myTimer.cancel();
		}
		myTimer = new Timer(getName());
		TimerTask l_task = createTask(this);
		long l_period = getPeriod();

		myTimer.schedule(l_task, 0, l_period);
		info(MSG.TIMER_STARTED, getName());
	}
	
	/**
	 * Cancels this timer task.
	 */
	public void stop() {
		if (myTimer != null) {
			myTimer.cancel();
			
			myTimer = null;
		}
		info(MSG.TIMER_STOPPED, getName());
	}

	private TimerTask createTask(Runnable aTask) {
		return new TimerTask() {

			@Override
			public void run() {
				try {
					aTask.run();
				} catch (Exception anEx) {
					myTimer.cancel();
					error(MSG.TIMER_CANCELLED, getName());
				}
			}
		};
	}

	/**
	 * @return the time between successive task executions
	 */
	private long getPeriod() {
		String l_period = getString(PERIOD, "0"); //$NON-NLS-1$
		long l_ret = Delay.Period.apply(l_period);

		return l_ret;
	}
}
