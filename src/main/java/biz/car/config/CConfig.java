/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.config;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;

import biz.car.CAR;
import biz.car.SYS;
import biz.car.XLogger;

/**
 * Base implementation of the XLogger interface.<br>
 * This class also provides logging based on the logger key in the
 * configuration. Default is the system logger.
 *
 * @version 1.0.0 20.02.2026 07:56:11
 */
public class CConfig implements XConfig, XLogger {

	protected Config conf;
	protected Logger logger;

	/**
	 * Creates a default <code>CConfig</code> instance with an empty configuration
	 * and the system logger.
	 */
	public CConfig() {
		super();

		conf = EMPTY;
		logger = SYS.LOG.logger();
	}

	/**
	 * Creates a <code>CConfig</code> instance based on the given configuration.
	 * 
	 * @param aConfig the configuration to use
	 */
	public CConfig(Config aConfig) {
		super();

		conf = Objects.requireNonNull(aConfig);

		if (hasPath(CAR.LOGGER)) {
			logger = LoggerFactory.getLogger(getString(CAR.LOGGER));
		} else {
			logger = SYS.LOG.logger();
		}
	}

	@Override
	public Config config() {
		return conf;
	}

	@Override
	public Logger logger() {
		return logger;
	}

	/**
	 * Sets the logger to the logger in the configuration, if the configuration
	 * contains a logger entry. Otherwise the current logger is not changed or set
	 * to the system logger, if the current logger is <code>null</code>.
	 * 
	 * @return
	 */
	protected Logger loggerFromConfig() {
		Logger l_ret = logger;

		if (hasPath(CAR.LOGGER)) {
			l_ret = LoggerFactory.getLogger(getString(CAR.LOGGER));
		} else if (logger == null) {
			l_ret = SYS.LOG.logger();
		}
		return l_ret;
	}

	/**
    * Returns a {@code CConfig} based on this one, but with the given path set
    * to the given value. Does not modify this instance (since it's immutable).
    * If the path already has a value, that value is replaced.
    * 
    * @param aPath
    *            path expression for the value's new location
    * @param aValue
    *            value at the new path
    * @return the new instance with the new map entry
    */
   CConfig withValue(String aPath, String aValue) {
		ConfigValue l_value = ConfigValueFactory.fromAnyRef(aValue);
		Config l_conf = conf.withValue(aPath, l_value);
		
		return new CConfig(l_conf);
   }
}
