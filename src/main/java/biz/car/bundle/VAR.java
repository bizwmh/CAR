/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.bundle;

import biz.car.config.ACS;

/**
 * Variable names.
 *
 * @version 1.0.2 03.03.2025 12:42:44
 */
public class VAR {

	public static String EXEC;
	public static String INPUT;
	public static String LABEL;
	public static String LOGGER;
	public static String NAME;
	public static String OUTPUT;
	public static String PATH;
	public static String PERIOD;

	// -------------------------------------------------------------------------
	// Initialize the static fields
	// -------------------------------------------------------------------------
	static {
		ACS.initialize(VAR.class, "VAR.conf"); //$NON-NLS-1$
	}

	/**
	 * Creates a default <code>VAR</code> instance.
	 */
	private VAR() {
		super();
	}
}