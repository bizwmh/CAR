/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.config;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import biz.car.SYS;
import biz.car.util.ClassUtil;

/**
 * Application configuration support.<br>
 * Resources are assumed to be on the class path of the application.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface ACS {

	/**
	 * The runtime options of the application.
	 */
	Config APP = ConfigFactory.load();

	/**
	 * The JVM environment variables
	 */
	Config ENV = ConfigFactory.systemEnvironment();

	/**
	 * Static class fields will be initialized by the values found in the
	 * configuration file. This is done for fields where the field name matches the
	 * entry in the configuration file.<br>
	 * If the field name contains the '_' character (not as first) and this field
	 * name is not a key value then the '_' is replaced by '.' and then used again
	 * as a key value.
	 * 
	 * @param aClass the class where to initialize the static field members
	 * @param aConfig the configuration file providing the values
	 * @return the configuration used to initialize the class
	 */
	static Config initialize(Class<?> aClass, Config aConfig) {
		Config l_ret = Objects.requireNonNull(aConfig);

		setFields(aClass, l_ret, null);

		return l_ret;
	}

	/**
	 * Static class fields will be initialized by the values found in the
	 * configuration file. This is done for fields where the field name matches the
	 * entry in the configuration file.<br>
	 * If the field name contains the '_' character (not as first) and this field
	 * name is not a key value then the '_' is replaced by '.' and then used again
	 * as a key value.
	 * 
	 * @param aClass the class where to initialize the static field members
	 * @param aName  the name of the configuration file
	 * @return the configuration used to initialize the class
	 */
	static Config initialize(Class<?> aClass, String aName) {
		ClassLoader l_cl = aClass.getClassLoader();
		Config l_ret = ConfigFactory.load(l_cl, aName);

		setFields(aClass, l_ret, null);

		return l_ret;
	}

	/**
	 * Instance fields will be initialized by the values found in the given
	 * configuration file. This is done for fields where the field name matches the
	 * entry in the configuration file.<br>
	 * If the field name contains the '_' character (not as first) and this field
	 * name is not a key value then the '_' is replaced by '.' and then used again
	 * as a key value.
	 * 
	 * @param anObject the class instance whose fields to initialize
	 * @param aConfig  the configuration to use for the initialization
	 * @return the configuration used to initialize the class
	 */
	static Config initialize(Object anObject, Config aConfig) {
		Config l_ret = Objects.requireNonNull(aConfig);

		setFields(anObject.getClass(), l_ret, anObject);

		return l_ret;
	}

	/**
	 * Instance fields will be initialized by the values found in the given
	 * configuration file. This is done for fields where the field name matches the
	 * entry in the configuration file.<br>
	 * If the field name contains the '_' character (not as first) and this field
	 * name is not a key value then the '_' is replaced by '.' and then used again
	 * as a key value.
	 * 
	 * @param anObject the class instance whose fields to initialize
	 * @param aName    the name of the configuration file
	 * @return the configuration used to initialize the class
	 */
	static Config initialize(Object anObject, String aName) {
		ClassLoader l_cl = anObject.getClass().getClassLoader();
		Config l_ret = ConfigFactory.load(l_cl, aName);

		setFields(anObject.getClass(), l_ret, anObject);

		return l_ret;
	}

	/**
	 * Loads a configuration from a resource on the class path.<br>
	 * The resulting configuration will only contain the configuration entries from
	 * the given resource.
	 *
	 * @param aName the name of the configuration resource
	 * @return the optional configuration used to initialize the class
	 */
	static Optional<Config> parseResource(String aName) {
		Config l_ret = null;
		Optional<URL> l_url = ClassUtil.getResource(aName);

		if (l_url.isPresent()) {
			l_ret = ConfigFactory.parseURL(l_url.get());
		}
		return Optional.ofNullable(l_ret);
	}

	/**
	 * Loads a configuration from a resource on the class path.<br>
	 * The resulting configuration will only contain the configuration entries from
	 * the given resource.
	 *
	 * @param aClass the class loader of the given class will be used to find the
	 *               resource
	 * @param aName  the name of the configuration resource
	 * @return the optional configuration used to initialize the class
	 */
	static Optional<Config> parseResource(Class<?> aClass, String aName) {
		Config l_ret = null;
		Optional<URL> l_url = ClassUtil.getResource(aClass, aName);

		if (l_url.isPresent()) {
			l_ret = ConfigFactory.parseURL(l_url.get());
		}
		return Optional.ofNullable(l_ret);
	}

	/**
	 * Assigns values to the fields of a class.
	 * 
	 * @param aClass the class where to look up the declared fields
	 * @param aConf  the configuration. Field names are used as keys, the values are
	 *               assigned to the class fields
	 * @param anObj  the object where to set the fields. If <code>null</code> the
	 *               static fields of the class are only initialized.
	 */
	static void setFields(Class<?> aClass, Config aConf, Object anObj) {
		Field[] l_fields = aClass.getDeclaredFields();
		XConfig l_xc = () -> aConf;

		Stream.of(l_fields).filter(f -> !f.isSynthetic()).forEach(field -> {
			try {
				String l_fname = field.getName();
				Optional<String> l_path = l_xc.searchPath(l_fname);

				if (l_path.isPresent()) {
					field.setAccessible(true);
					Object l_value = aConf.getValue(l_path.get()).unwrapped();

					field.set(anObj, l_value);
				}
			} catch (IllegalArgumentException | IllegalAccessException anEx) {
				throw SYS.LOG.exception(anEx);
			}
		});
	}

	/**
	 * Creates a new configuration from the given configuration backed up by the
	 * application runtime properties.
	 * 
	 * @param aConfig the configuration to be backed up
	 * @return this configuration backed up with the application runtime options.
	 */
	static Config withFallbackApp(Config aConfig) {
		return aConfig.withFallback(APP);
	}
}
