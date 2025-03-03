/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import biz.car.config.ACS;

/**
 * Runtime features of the JVM system environment.
 *
 * @version 1.0.0 11.10.2024 06:07:15
 */
public class SYS implements XLogger {

	/**
	 * The default system logger. <br>
	 * The name of the logger can be set in the application properties file.
	 */
	public static final XLogger LOG = new SYS();

	/**
	 * The JVM system properties as a <code>Config</code> object.
	 */
	public static Config PROPS = ConfigFactory.systemProperties();

	/**
	 * All properties from the given configuration file are stored in the Java
	 * system properties via the <code>System.setProperty</code> method.
	 * 
	 * @param aConfig the configuration file
	 */
	public static void addProperties(Config aConfig) {
		Config l_conf = Objects.requireNonNull(aConfig);

		l_conf.entrySet()
			.forEach(entry -> {
				String l_key = entry.getKey();
				String l_val = l_conf.getString(l_key);

				System.setProperty(l_key, l_val);
			});
		ConfigFactory.invalidateCaches();

		PROPS = ConfigFactory.systemProperties();
	}

	private final Logger logger;

	/**
	 * Creates a <code>SYS</code> instance.
	 * 
	 * @param aName the name of the system logger to be used
	 */
	public SYS(String aName) {
		super();

		String l_name = ACS.APP.getString(aName);
		logger = LoggerFactory.getLogger(l_name);
	}

	/**
	 * Creates a <code>SYS</code> instance for the default system logger.
	 */
	private SYS() {
		this("systemLogger"); //$NON-NLS-1$
	}

	@Override
	public Logger logger() {
		return logger;
	}
}
