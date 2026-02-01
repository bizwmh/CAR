/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.config;

import static com.typesafe.config.ConfigValueType.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

import biz.car.util.Underscore;

/**
 * Provides access to the values of a configuration.
 * 
 * @version 2.0.0 08.01.2026 08:32:08
 */
@FunctionalInterface
public interface XConfig {

	/**
	 * The empty <code>config</code> instance.
	 */
	static Config EMPTY = ConfigFactory.empty();

	/**
	 * Converts a <code>Dictionary</code> to a <code>Config</code> instance.
	 * 
	 * @return the config instance
	 */
	static Config fromDictionary(Dictionary<String, ?> aDict) {
		Map<String, Object> l_map = new HashMap<String, Object>();

		if (!aDict.isEmpty()) {
			Enumeration<String> l_enum = aDict.keys();

			while (l_enum.hasMoreElements()) {
				String l_key = l_enum.nextElement();
				Object l_value = aDict.get(l_key);

				l_map.put(l_key, l_value);
			}
		}
		Config l_ret = ConfigFactory.parseMap(l_map);

		return l_ret;
	}

	/**
	 * Converts a <code>Config</code> to a <code>Map</code> with values of type
	 * <code>String</code>.<br>
	 * The keys are built from the keys of the complete configuration hierarchy.
	 * 
	 * @param aConfig the config to convert
	 * @return the resulting <code>Map</code> object
	 */
	static Map<String, String> toStringMap(Config aConfig) {
		Map<String, String> l_ret = new HashMap<String, String>();

		aConfig.entrySet()
				.forEach(entry -> {
					String l_key = entry.getKey();
					String l_val = aConfig.getString(l_key);

					l_ret.put(l_key, l_val);
				});
		return l_ret;
	}

	/**
	 * Looks up a String value list.<br>
	 * If the value is a single string then an array of size 1 is returned.<br>
	 * If the key does not exist then an array of size 0 is returned.
	 * 
	 * @param aKey the key for the configuration entry
	 * @return the String value list
	 */
	default List<String> asStringList(String aKey) {
		List<String> l_ret = new ArrayList<String>();
		Config l_conf = config();

		if (l_conf.hasPath(aKey)) {
			ConfigValue l_value = l_conf.getValue(aKey);

			switch (l_value.valueType()) {
			case LIST:
				l_ret.addAll(l_conf.getStringList(aKey));
				break;

			case STRING:
				String l_string = l_conf.getString(aKey).trim();
				String[] l_array = l_string.split(","); //$NON-NLS-1$
				List<String> l_list = Arrays.asList(l_array);

				l_list.forEach(s -> l_ret.add(s.trim()));

				if (l_string.endsWith(",")) { //$NON-NLS-1$
					l_ret.add(""); //$NON-NLS-1$
				}
				break;

			default:
			}
		}
		return l_ret;
	}

	/**
	 * @return a reference to the underlying <code>Config</code> object
	 */
	Config config();

	/**
	 * Retrieves a Configuration value.<br>
	 * If the value was not found, then this method retries the lookup with the
	 * path, where underscores have been converted according to the definition of
	 * the <code>Underscore</code> interface.
	 * 
	 * @param aPath the path to the runtime option
	 * @return the optional value found
	 */
	default Optional<String> find(String aPath) {
		Optional<String> l_path = searchPath(aPath);
		String l_ret = null;

		if (l_path.isPresent()) {
			l_ret = getString(l_path.get());
		}
		return Optional.ofNullable(l_ret);
	}

	/**
	 * Provides a boolean configuration value.
	 * 
	 * @param aKey the key for the searched value
	 * @return the value found for the given key
	 * @throws ConfigException.Missing   if the value for the key does not exist
	 * @throws ConfigException.WrongType if the value cannot be parsed as a boolean
	 */
	default boolean getBool(String aKey) {
		return config().getBoolean(aKey);
	}

	/**
	 * Provides a boolean configuration value.
	 * 
	 * @param aKey     the key for the searched value
	 * @param aDefault the boolean value to return if key for the searched value
	 *                 does not exist
	 * @return the value found for the given key
	 * @throws ConfigException.Missing   if the value for the key does not exist
	 * @throws ConfigException.WrongType if the value cannot be parsed as a boolean
	 */
	default boolean getBool(String aKey, boolean aDefault) {
		boolean l_ret = aDefault;
		Config l_conf = config();

		if (l_conf.hasPath(aKey)) {
			l_ret = l_conf.getBoolean(aKey);
		}
		return l_ret;
	}

	/**
	 * Provides an integer configuration value.
	 * 
	 * @param aKey the key for the searched value
	 * @return the value found for the given key
	 * @throws ConfigException.Missing   if the value for the key does not exist
	 * @throws ConfigException.WrongType if the value cannot be parsed as an integer
	 */
	default int getInt(String aKey) {
		return config().getInt(aKey);
	}

	/**
	 * @return the name for this <code>XConfig</code>
	 */
	default String getName() {
		return getClass().getSimpleName();
	}

	/**
	 * Retrieves the value of a runtime option.
	 * 
	 * @param aKey the name of the runtime option
	 * @return the value found
	 * @throws ConfigException.Missing if aKey is not existing
	 */
	default String getString(String aKey) {
		return config().getString(aKey);
	}

	/**
	 * Looks up a string configuration parameter.
	 * 
	 * @param aKey     the key for the searched value
	 * @param aDefault the default value
	 * @return the value found for the given key or the default value
	 */
	default String getString(String aKey, String aDefault) {
		String l_ret = aDefault;
		Config l_conf = config();

		if (l_conf.hasPath(aKey)) {
			l_ret = l_conf.getString(aKey);
		}
		return l_ret;
	}

	/**
	 * Checks whether a value is present and non-null at the given path.
	 *
	 * @param aPath the path to check
	 * @return <code>true</code> if the path is present
	 */
	default boolean hasPath(String aPath) {
		return config().hasPath(aPath);
	}

	/**
	 * Checks whether a configuration object is present.
	 * 
	 * @param akey the key to a config object
	 * @return <code>true</code> if <code>aKey</code> exists and the value is an
	 *         object, otherwise <code>false</code>
	 */
	default boolean hasConfig(String aKey) {
		ConfigValue l_cv = config().root().get(aKey);
		boolean l_ret = false;

		if (l_cv != null) {
			ConfigValueType l_vt = l_cv.valueType();
			l_ret = l_vt == OBJECT;
		}
		return l_ret;
	}

	/**
	 * Checks whether a property parameter is present.
	 * 
	 * @param akey the key to a property value
	 * @return <code>true</code> if <code>aKey</code> exists and the value is not an
	 *         object and not a list, otherwise <code>false</code>
	 */
	default boolean hasProperty(String aKey) {
		ConfigValue l_cv = config().root().get(aKey);
		boolean l_ret = false;

		if (l_cv != null) {
			ConfigValueType l_vt = l_cv.valueType();
			l_ret = l_vt != OBJECT && l_vt != LIST;
		}
		return l_ret;
	}

	/**
	 * @return the list of all top level keys in this configuration where
	 *         <code>hasProperty</code> would return true
	 */
	default List<String> propertyKeys() {
		return config().root().keySet().stream()
				.filter(key -> hasProperty(key))
				.collect(Collectors.toList());
	}

	/**
	 * Perform a 2-phase check on the existence of the given path to a configuration
	 * entry. If the path could not be found as it is, then the path is converted
	 * according the rules of the Underscore interface and the search is repeated
	 * with that path.
	 * 
	 * @param aPath the path to look up
	 * @return the optional path as it exists in the configuration
	 */
	default Optional<String> searchPath(String aPath) {
		String l_ret = aPath;

		if (!hasPath(aPath)) {
			l_ret = Underscore.apply(l_ret);

			if (!hasPath(l_ret)) {
				l_ret = null;
			}
		}
		return Optional.ofNullable(l_ret);
	}
}
