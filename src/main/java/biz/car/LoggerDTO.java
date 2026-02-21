/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car;

import com.typesafe.config.Config;

import biz.car.config.CConfig;

/**
 * The configuration for the creation of a logger at runtime.
 *
 * @version 1.0.0 21.02.2026 10:31:40
 */
public class LoggerDTO extends CConfig {

	/**
	 * if <code>true</code> log messages are passed to the root logger
	 * Default is <code>false</code>.
	 */
	public boolean additive;
	/**
	 * The name of the appender
	 */
	public String appender;
	/**
	 * the name of the log file
	 */
	public String file;
	/**
	 * The name of the logger
	 */
	public String logger;
	/**
	 * the path to the log file folder
	 */
	public String path;
	/**
	 * The pattern for the log messages
	 */
	public String pattern;
	/**
	 * if <code>true</code> the file given in path will be prefixed with a timestamp<br>
	 * Default is <code>true</code>.
	 */
	public boolean timestamp;
	
	/**
	 * Creates a default <code>LoggerDTO</code> instance.
	 */
	public LoggerDTO() {
		super();
	}

	/**
	 * Creates a default <code>LoggerDTO</code> instance.
	 * 
	 * @param aConfig the underlying logger configuration
	 */
	public LoggerDTO(Config aConfig) {
		super(aConfig);
	}
}
