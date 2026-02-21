/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.config;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigOrigin;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;

import biz.car.LoggerDTO;
import biz.car.XLogger;
import biz.car.XLoggerFactory;

/**
 * Analyzes the content of a configuration and writes all found entries to a log
 * file.
 *
 * @version 1.0.0 15.02.2026 05:43:28
 */
public class ConfigHandler implements XLogger {

	private static final String TAB = "\t"; //$NON-NLS-1$

	private Logger logger;
	private String tabs = ""; //$NON-NLS-1$

	/**
	 * Creates a default <code>ConfigHandler</code> instance.
	 */
	public ConfigHandler() {
		super();
	}

	@Override
	public void info(String aMessage, Object... anArgList) {
		String l_msg = tabs + aMessage;

		XLogger.super.info(l_msg, anArgList);
	}

	/**
	 * Logs the content of the given configuration to a log file.
	 * 
	 * @param aName   the name of the configuration
	 * @param aConfig the configuration to inspect
	 */
	public void log(String aName, Config aConfig) {
		setLogger(aName);
		origin(aName, aConfig);
		log(aConfig);
	}

	@Override
	public Logger logger() {
		return logger;
	}

	private void log(Config aConfig) {
		ConfigObject l_co = aConfig.root();
		Set<Map.Entry<String, ConfigValue>> l_set = l_co.entrySet();

		for (Map.Entry<String, ConfigValue> l_entry : l_set) {
			String l_key = l_entry.getKey();
			ConfigValue l_value = l_entry.getValue();
			ConfigValueType l_type = l_value.valueType();

			switch (l_type) {
			case OBJECT: {
				info(l_key);

				String l_tabs = tabs;
				tabs = tabs + TAB;
				Config l_conf = aConfig.getConfig(l_key);

				log(l_conf);

				tabs = l_tabs;

				break;
			}
			case LIST: {
				List<ConfigValue> l_list = aConfig.getList(l_key);

				if (l_list.size() == 0) {
					info("{} = []", l_key); //$NON-NLS-1$
				} else {
					ConfigValue l_cv = l_list.get(0);
					ConfigValueType l_cvType = l_cv.valueType();

					if (l_cvType == ConfigValueType.OBJECT) {
						List<? extends Config> l_confList = aConfig.getConfigList(l_key);
						int l_ind = 0;

						for (Config l_conf : l_confList) {
							info("{}[{}]", l_key, l_ind++); //$NON-NLS-1$

							String l_tabs = tabs;
							tabs = tabs + TAB;

							log(l_conf);

							tabs = l_tabs;
						}
					} else {
						List<String> l_stringList = aConfig.getStringList(l_key);

						info("{} = [{}]", l_key, l_stringList.toString()); //$NON-NLS-1$
					}
				}
				break;
			}
			case NULL: {
				info("{} = '{}'", l_key, ""); //$NON-NLS-1$//$NON-NLS-2$

				break;
			}
			default:
				info("{} = '{}'", l_key, l_value.unwrapped().toString()); //$NON-NLS-1$
			}
		}
	}

	private void origin(String aName, Config aConfig) {
		ConfigOrigin l_origin = aConfig.origin();
		String l_fname = l_origin.filename();
		String l_res = l_origin.resource();
		URL l_url = l_origin.url();

		info("name = {}", aName); //$NON-NLS-1$

		if (l_fname != null) {
			info("file = {}", l_fname); //$NON-NLS-1$
		}
		if (l_res != null) {
			info("resource = {}", l_res); //$NON-NLS-1$
		}
		if (l_url != null) {
			info("urlname = {}", l_url.toString()); //$NON-NLS-1$
		}
		info("=".repeat(40)); //$NON-NLS-1$
	}

	private void setLogger(String aName) {
		LoggerDTO l_dto = new LoggerDTO();
		l_dto.pattern = "%msg%n"; //$NON-NLS-1$
		l_dto.file = aName;
		logger = XLoggerFactory.getLogger(l_dto).logger();
	}
}
