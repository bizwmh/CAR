/* --------------------------------------------------------------------------
 * Project: CAR OSGi
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved.
 * -------------------------------------------------------------------------- */

package biz.car.config;

import java.net.URL;
import java.util.Optional;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import biz.car.util.ClassUtil;

/**
 * Bundle configuration support.<br>
 * Configuration resources are assumed to be located on the bundle class path.
 *
 * @version 1.0.0 03.11.2024 11:50:51
 */
public interface BCS {

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
		Config l_ret = load(aClass, aName);

		ACS.setFields(aClass, l_ret, null);

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
	 * @return the <code>Config</code> from the given <code>ACS</code>
	 */
	static Config initialize(Object anObject, String aName) {
		Class<?> l_class = anObject.getClass();
		Config l_ret = load(l_class, aName);

		ACS.setFields(l_class, l_ret, anObject);

		return l_ret;
	}

	/**
	 * Loads a configuration from a resource on the class path.<br>
	 * This method will also add the system properties to the configuration.<br>
	 * If the given resource does not exist, the resulting configuration will only
	 * contain the system properties.
	 *
	 * @param aClass the class loader of the given class will be used to find the
	 *               resource
	 * @param aName  the name of the configuration resource
	 * @return the configuration used to initialize the class
	 */
	static Config load(Class<?> aClass, String aName) {
		ClassLoader l_cl = aClass.getClassLoader();
		Config l_ret = ConfigFactory.load(l_cl, aName);

		return l_ret;
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
}
