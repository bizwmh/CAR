/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.wmh.car;

import biz.wmh.car.bundle.VAR;
import biz.wmh.car.config.ACS;

/**
 * CAR constants.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface CAR {

	/**
	 * The default date format
	 */
	String DF_DATE = ACS.APP.getString("dateFormat"); //$NON-NLS-1$
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
