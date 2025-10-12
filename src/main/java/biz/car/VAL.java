/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car;

import biz.car.util.SFI;

/**
 * Application wide constants.
 *
 * @version 2.0.0 06.10.2025 14:01:34
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
