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
 * @version 1.0.0 15.10.2024 10:29:50
 */
public class VAR {

	public static String EXEC;
	public static String LABEL;
	public static String LOGGER;
	public static String NAME;

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