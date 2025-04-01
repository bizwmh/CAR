/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car;

import biz.car.bundle.VAR;
import biz.car.config.ACS;

/**
 * CAR constants.
 *
 * @version 1.0.2 03.03.2025 12:43:04
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
	 * The key for the LABEL runtime option.<br>
	 */
	String LABEL = VAR.LABEL;
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
