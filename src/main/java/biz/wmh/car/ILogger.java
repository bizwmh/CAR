/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.wmh.car;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.wmh.car.bundle.VAL;
import biz.wmh.car.config.ACS;

/**
 * Base implementation for the <code>XLogger</code> interface.
 *
 * @version 2.0.0 16.01.2026 14:58:31
 */
public class ILogger implements XLogger {

	private Logger logger;

	/**
	 * Creates a system logger instance.
	 */
	public ILogger() {
		super();

		String l_name = ACS.APP.getString(VAL.systemLogger);
		logger = LoggerFactory.getLogger(l_name);
	}

	/**
	 * Creates a default <code>ILogger</code> instance.
	 * 
	 * @param aLogger the underlying logger instance
	 */
	public ILogger(Logger aLogger) {
		super();

		logger = aLogger;
	}

	/**
	 * Creates a default <code>ILogger</code> instance.
	 * 
	 * @param aLogger the name of the logger
	 */
	public ILogger(String aLogger) {
		super();

		logger = LoggerFactory.getLogger(aLogger);
	}

	@Override
	public Logger logger() {
		return logger;
	}
}
