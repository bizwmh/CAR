/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.util;

import com.typesafe.config.Config;

import biz.car.CAR;
import biz.car.XRunnable;
import biz.car.config.ConfigAdapter;

/**
 * A runnable, but dummy configuration object.<br>
 * The usage is for testing or monitoring purposes.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class Dummy extends ConfigAdapter implements CAR, XRunnable {

	private Long period;

	/**
	 * Creates a default <code>Dummy</code> instance with an empty configuration.
	 */
	public Dummy() {
		super();
	}

	/**
	 * Creates a <code>Dummy</code> instance.<br>
	 * The fields of this object are initialized with the values from the associated
	 * configuration. The field name is used as the key for a configuration entry.
	 * 
	 * @param aConfig the underlying configuration object
	 */
	public Dummy(Config aConfig) {
		super(aConfig);
	}

	/**
	 * Creates a default <code>Dummy</code> instance with an empty configuration.
	 * 
	 * @param aName the name of the configuration object
	 */
	public Dummy(String aName) {
		super(aName);
	}

	@Override
	public void dispose() {
		// nothing to do
	}

	@Override
	public void exec() {
		Delay.millis(getPeriod());
	}

	/**
	 * @return the configured period of time used 
	 */
	private long getPeriod() {
		if (period == null) {
			String l_period = getString(PERIOD, "3s"); //$NON-NLS-1$
			period = Delay.Period.apply(l_period);
		}
		return period;
	}
}
