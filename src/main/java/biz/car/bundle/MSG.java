/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.bundle;

import biz.car.config.ACS;

/**
 * Bundle messages.
 *
 * @version 1.0.0 11.10.2024 06:19:13
 */
public class MSG {

	public static String EXEC_ABENDED;
	public static String EXEC_ENDED;
	public static String EXEC_STARTED;
	public static String RESOURCE_NOT_FOUND;

	// -------------------------------------------------------------------------
	// Initialize the static fields
	// -------------------------------------------------------------------------
	static {
		ACS.initialize(MSG.class, "MSG.properties"); //$NON-NLS-1$
	}

	/**
	 * Creates a default <code>VAR</code> instance.
	 */
	private MSG() {
		super();
	}
}