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
 * @version 2.0.0 09.10.2025 11:18:52
 */
public class VAR {

	public static String EXEC;
	public static String INPUT;
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