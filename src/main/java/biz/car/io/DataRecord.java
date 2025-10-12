/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.io;

/**
 * A general purpose interface for a modifiable data source.
 *
 * @version 2.0.0 09.10.2025 12:48:13
 */
public interface DataRecord extends FieldSource {

	/**
	 * Updates the data record with a new field value.
	 * 
	 * @param aName the name of the field
	 * @param aValue the new field value
	 * @return the current <code>DataRecord</code> instance
	 * @throws IndexOutOfBoundsException if the field name is not defined
	 */
	public DataRecord setValue(String aName, String aValue);
}
