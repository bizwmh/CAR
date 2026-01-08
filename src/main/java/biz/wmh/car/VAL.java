/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.wmh.car;

import biz.wmh.car.util.SFI;

/**
 * Application wide constants.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class VAL {

	public static String _conf;
	public static String _csv;
	public static String _jar;
	public static String _properties;
	public static String _sql;

	// -------------------------------------------------------------------------
	// Initialize the static fields
	// -------------------------------------------------------------------------
	static {
		SFI.initialize(VAL.class);
	}

	/**
	 * Creates a default VAR instance.
	 */
	private VAL() {
		super();
	}
}
