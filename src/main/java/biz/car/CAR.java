/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car;

import biz.car.bundle.VAL;
import biz.car.bundle.VAR;
import biz.car.config.ACS;

/**
 * CAR constants.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface CAR {

	/**
	 * String constant ".conf" to be used as a file extension.
	 */
	String _conf = VAL._conf;
	/**
	 * String constant ".csv" to be used as a file extension.
	 */
	String _csv = VAL._csv;
	/**
	 * String constant ".default" to be used as a file extension.
	 */
	String _default = VAL._default;
	/**
	 * String constant ".jar" to be used as a file extension.
	 */
	String _jar = VAL._jar;
	/**
	 * String constant ".properties" to be used as a file extension.
	 */
	String _properties = VAL._properties;
	/**
	 * String constant ".sql" to be used as a file extension.
	 */
	String _sql = VAL._sql;
	/**
	 * Key for the additive parameter in logger appenders.
	 */
	String ADDITIVE = VAR.ADDITIVE;
	/**
	 * Key for the name of a logger appender.
	 */
	String APPENDER = VAR.APPENDER;
	/**
	 * The default date format
	 */
	String DF_DATE = ACS.APP.getString(VAL.dateFormat);
	/**
	 * General purpose string constant "DUMMY".
	 */
	String DUMMY = VAL.DUMMY;
	/**
	 * The key for the EXEC runtime option.<br>
	 */
	String EXEC = VAR.EXEC;
	/**
	 * Key for the input runtime option
	 */
	String INPUT = VAR.INPUT;
	/**
	 * General purpose constant indicating some kind of failure.
	 */
	String KO = VAL.KO;
	/**
	 * The key for the LOGGER runtime option.<br>
	 */
	String LOGGER = VAR.LOGGER;
	/**
	 * The key for the NAME runtime option.<br>
	 */
	String NAME = VAR.NAME;
	/**
	 * General purpose constant indicating a successful status.
	 */
	String OK = VAL.OK;
	/**
	 * Key for the output runtime option
	 */
	String OUTPUT = VAR.OUTPUT;
	/**
	 * The key for the PATH runtime option.<br>
	 */
	String PATH = VAR.PATH;
	/**
	 * Key for the pattern parameter in Logger layouts.
	 */
	String PATTERN = VAR.PATTERN;
	/**
	 * The key for the PERIOD parameter of a timer task.<br>
	 */
	String PERIOD = VAR.PERIOD;
}
