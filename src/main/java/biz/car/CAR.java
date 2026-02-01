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
	 * TODO String _conf
	 */
	String _conf = VAL._conf;
	/**
	 * TODO String _csv
	 */
	String _csv = VAL._csv;
	/**
	 * TODO String _default
	 */
	String _default = VAL._default;
	/**
	 * TODO String _jar
	 */
	String _jar = VAL._jar;
	/**
	 * TODO String _properties
	 */
	String _properties = VAL._properties;
	/**
	 * TODO String _sql
	 */
	String _sql = VAL._sql;
	/**
	 * The default date format
	 */
	String DF_DATE = ACS.APP.getString(VAL.dateFormat);
	/**
	 * TODO String DUMMY
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
	 * The key for the LOGGER runtime option.<br>
	 */
	String LOGGER = VAR.LOGGER;
	/**
	 * The key for the NAME runtime option.<br>
	 */
	String NAME = VAR.NAME;
	/**
	 * Key for the output runtime option
	 */
	String OUTPUT = VAR.OUTPUT;
	/**
	 * The key for the PATH runtime option.<br>
	 */
	String PATH = VAR.PATH;
	/**
	 * The key for the PERIOD parameter of a timer task.<br>
	 */
	String PERIOD = VAR.PERIOD;
}
