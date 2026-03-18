/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.config;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;

import biz.car.CAR;
import biz.car.util.ClassUtil;

/**
 * Base implementation of the <code>Configurable</code> interface.<br>
 * The fields of this object are initialized with the values from the associated
 * configuration. Field names are used as the keys for a configuration entries.
 *
 * @version 1.0.0 20.02.2026 07:56:11
 */
public class CConfig implements CAR, Configurable {

	private Config conf;
	private Logger myLogger;
	private String name;

	/**
	 * Creates a default <code>CConfig</code> instance.<br>
	 * This instance is initialized with the default configuration.
	 */
	public CConfig() {
		super();

		conf = defaultConfig();
		myLogger = loggerFromConfig();

		initialize();
	}

	/**
	 * Creates a <code>CConfig</code> instance based on the given configuration.
	 * 
	 * @param aConfig the configuration to use
	 */
	public CConfig(Config aConfig) {
		super();

		conf = Objects.requireNonNull(aConfig);
		myLogger = loggerFromConfig();

		initialize();
	}

	/**
	 * Creates a default <code>CConfig</code> instance.
	 * 
	 * @param aName the name for the configuration object
	 */
	public CConfig(String aName) {
		this();

		name = aName;
	}

	/**
	 * Merges the current configuration with the key-value pairs of the given
	 * configuration.
	 * 
	 * @param aConfig a configuration whose keys should be used as fallback, if the
	 *                keys are not present in this one
	 */
	@Override
	public void accept(Config aConfig) {
		String l_name = name;
		conf = aConfig.withFallback(config());
		myLogger = loggerFromConfig();

		initialize();

		// the name is immutable
		if (l_name != null) {
			name = l_name;
		}
	}

	@Override
	public Config config() {
		return conf;
	}

	/**
	 * Returns the name of this configuration object.<br>
	 * If a value for the name is not set the simple class name is returned.
	 */
	public String getName() {
		return name == null ? getClass().getSimpleName() : name;
	}

	@Override
	public Logger logger() {
		return myLogger;
	}

	/**
	 * Returns a {@code CConfig} based on this one, but with the given path set to
	 * the given value. Does not modify this instance (since it's immutable). If the
	 * path already has a value, that value is replaced.
	 * 
	 * @param aPath  path expression for the value's new location
	 * @param aValue value at the new path
	 * @return the new instance with the new map entry
	 */
	public CConfig withValue(String aPath, String aValue) {
		ConfigValue l_value = ConfigValueFactory.fromAnyRef(aValue);
		Config l_conf = conf.withValue(aPath, l_value);

		return new CConfig(l_conf);
	}

	/**
	 * Assigns a default configuration to this object.<br>
	 * The default implementation looks up a resource on the classpath with the
	 * simple class name (followed by '.conf').
	 * 
	 * @return the default configuration or <code>XConfig.EMPTY</code>.
	 */
	protected Config defaultConfig() {
		Class<?> l_class = getClass();
		String l_res = l_class.getSimpleName() + CAR._conf;
		Optional<URL> l_url = ClassUtil.getResource(l_class, l_res);

		if (l_url.isPresent()) {
			return ConfigFactory.parseURL(l_url.get());
		}
		return XConfig.EMPTY;
	}

	/**
	 * Initializes this <code>CConfig</code> with the loaded configuration.<br>
	 * Called by a constructor.
	 */
	protected void initialize() {
		initialize(this);
	}

	/**
	 * Sets the logger to the logger in the configuration, if the configuration
	 * contains a logger entry. Otherwise the current logger is not changed or set
	 * to the system logger, if the current logger is <code>null</code>.
	 * 
	 * @return
	 */
	protected Logger loggerFromConfig() {
		Logger l_ret = myLogger;

		if (hasPath(LOGGER)) {
			l_ret = LoggerFactory.getLogger(getString(LOGGER));
		} else if (l_ret == null) {
			l_ret = LoggerFactory.getLogger(getClass().getSimpleName());
		}
		return l_ret;
	}
}
