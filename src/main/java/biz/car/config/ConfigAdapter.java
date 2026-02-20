/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.config;

import com.typesafe.config.Config;

import biz.car.CAR;

/**
 * A base implementation of the <code>Configurable</code> interface.<br>
 * Fields are initialized from the configuration. Field names are used as the
 * keys for the configuration entries.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class ConfigAdapter extends CConfig implements CAR, Configurable {

	private String name;

	/**
	 * Creates a default <code>ConfigAdapter</code> instance with an empty
	 * configuration.
	 */
	public ConfigAdapter() {
		super();
	}

	/**
	 * Creates a <code>ConfigObject</code> instance.<br>
	 * The fields of this object are initialized with the values from the associated
	 * configuration. The field name is used as the key for a configuration entry.
	 * 
	 * @param aConfig the underlying configuration object
	 */
	public ConfigAdapter(Config aConfig) {
		super(aConfig);

		name = getString(NAME, null);
	}

	/**
	 * Creates a default <code>ConfigObject</code> instance.
	 * 
	 * @param aName the name of the configuration object
	 */
	public ConfigAdapter(String aName) {
		this();

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
		logger = loggerFromConfig();
		String l_name = getString(NAME, null);
		
		if (name == null) {
			name = l_name;
		}
	}

	@Override
	public String getName() {
		return name == null ? Configurable.super.getName() : name;
	}
}
