/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.io;

import java.util.List;
import java.util.Optional;

/**
 * A general purpose interface for retrieving field values from any data source.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface FieldSource {

	/**
	 * @return the ordered list of field names in the underlying data source.
	 */
	public List<String> fieldNames();

	/**
	 * Provides access to field values of the underlying data source.
	 * 
	 * @param aName the name of the field
	 * @return the field value found
	 * @throws IndexOutOfBoundsException if the field name is not defined
	 */
	public String getValue(String aName);

	/**
	 * Checks if this record has a field with the given name.
	 * 
	 * @param aName the name of field
	 * @return <code>true</code> if the field exists
	 */
	public boolean hasField(String aName);

	/**
	 * Provides access to field values of the underlying data source.
	 * 
	 * @param aName the name of the field
	 * @return the optional field value
	 */
	public Optional<String> optionalValue(String aName);
}
