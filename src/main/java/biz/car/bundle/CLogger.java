/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.car.XLogger;

/**
 * Base implementation for the <code>XLogger</code> interface.
 *
 * @version 2.0.0 16.01.2026 14:58:31
 */
public class CLogger implements XLogger {

	private Logger logger;

	/**
	 * Creates a default <code>CLogger</code> instance.
	 * 
	 * @param aLogger the underlying logger instance
	 */
	public CLogger(Logger aLogger) {
		super();

		logger = aLogger;
	}

	/**
	 * Creates a default <code>CLogger</code> instance.
	 * 
	 * @param aLogger the name of the logger
	 */
	public CLogger(String aLogger) {
		super();

		logger = LoggerFactory.getLogger(aLogger);
	}

	@Override
	public Logger logger() {
		return logger;
	}
}
