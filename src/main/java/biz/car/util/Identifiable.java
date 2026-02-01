/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.util;

/**
 * Provides an identifier that is unique within the context of the implementing
 * class.
 * 
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface Identifiable {

	/**
	 * @return the unique identifier
	 */
	public String getId();
}
