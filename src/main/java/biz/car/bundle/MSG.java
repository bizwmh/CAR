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
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class MSG {

	public static String EXEC_ABENDED;
	public static String EXEC_ENDED;
	public static String EXEC_STARTED;
	public static String OBJECT_NOT_REGISTERED;
	public static String OBJECT_UNREGISTERED;
	public static String PATH_NOT_DIRECTORY;
	public static String RESOURCE_NOT_FOUND;
	public static String TIMER_CANCELLED;
	public static String TIMER_STARTED;
	public static String TIMER_STOPPED;
	public static String WATCHER_STARTED;
	public static String WATCHER_STOPPED;

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