/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import biz.car.CAR;
import biz.car.SYS;

/**
 * A base implementation of the <code>XConfig</code> interface.<br>
 * Fields are initialized from the configuration. Field names are used as the
 * keys for the configuration entries.
 *
 * @version 1.0.0 01.11.2024 10:38:01
 */
public class ConfigObject implements CAR, Configurable {

	private Config conf;
	private String label;
	private Logger logger;
	private String name;

	/**
	 * Creates a default <code>ConfigObject</code> instance with an empty
	 * configuration.
	 */
	public ConfigObject() {
		this(EMPTY);
	}

	/**
	 * Creates a <code>ConfigObject</code> instance.<br>
	 * The fields of this object are initialized with the values from the associated
	 * configuration. The field name is used as the key for a configuration entry.
	 * 
	 * @param aConfig the underlying configuration object
	 */
	public ConfigObject(Config aConfig) {
		super();

		conf = aConfig;

		if (conf != EMPTY) {
			ACS.initialize(this, conf);
		}
	}

	/**
	 * Creates a default <code>ConfigObject</code> instance.
	 * 
	 * @param aName the name of the configuration object
	 */
	public ConfigObject(String aName) {
		this(EMPTY);

		name = aName;
	}

	/**
	 * Assigns a new configuration.<br>
	 * The new underlying configuration is the given configuration backed up by the
	 * currently assigned configuration.
	 * 
	 * @param aConfig the new configuration
	 */
	@Override
	public void accept(Config aConfig) {
		conf = aConfig.withFallback(conf);
		String l_name = name;

		ACS.initialize(this, conf);

		if (l_name != null) {
			name = l_name; // name is immutable
		} else if (conf.hasPath(NAME)) {
			name = getString(NAME);
		}
		if (aConfig.hasPath(LOGGER)) {
			String l_logger = aConfig.getString(LOGGER);
			logger = LoggerFactory.getLogger(l_logger);
		}
	}

	@Override
	public Config config() {
		return conf;
	}

	@Override
	public String getLabel() {
		if (label == null) {
			label = getString(LABEL,getClass().getSimpleName());
		}
		return label;
	}

	@Override
	public String getName() {
		return name == null ? getClass().getSimpleName() : name;
	}

	@Override
	public Logger logger() {
		if (logger == null) {
			if (hasPath(LOGGER)) {
				logger = LoggerFactory.getLogger(getString(LOGGER));
			} else {
				logger = SYS.LOG.logger();
			}
		}
		return logger;
	}
}
