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
 * @version 1.0.0 15.10.2024 10:29:12
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
}