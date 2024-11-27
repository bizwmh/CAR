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
 * @version 1.0.0 16.10.2024 15:23:17
 */
public interface Identifiable {

	/**
	 * @return the unique identifier
	 */
	public String getId();
}
