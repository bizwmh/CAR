/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.config;

import java.util.function.Consumer;

import com.typesafe.config.Config;

import biz.car.XLogger;

/**
 * An application object that can be configured. <br>
 * When a new configuration is consumed the new configuration is backed up by
 * the currently assigned configuration. Instance fields are initialized with
 * the values from the configuration.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface Configurable extends
		Consumer<Config>,
		XConfig,
		XLogger {
}