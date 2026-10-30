/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved.
 * -------------------------------------------------------------------------- */

package biz.car.excel;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The header of a sheet in an Excel workbook.
 *
 * @version 1.0.0 18.07.2026
 */
public interface ExcelHeader {

	/**
	 * Converts an ExcelRecord to a Map.<br>
	 * The header fields are used as the keys.
	 */
	Function<ExcelRecord, Map<String, String>> ToMap = rec -> {
		Map<String, String> l_ret = rec.fieldNames().stream()
			.collect(Collectors.toMap(
				field -> field,
				field -> rec.optionalValue(field).orElse(""))); //$NON-NLS-1$

		return l_ret;
	};

	/**
	 * @return the list of sheet column names
	 */
	List<String> columns();

	/**
	 * Constructor method for an empty Excel record.<br>
	 * The number of null-string fields corresponds to the header size.
	 *
	 * @return the new ExcelRecord instance
	 */
	ExcelRecord Record();

	/**
	 * Constructor method for a new Excel record with this header.
	 *
	 * @param aList the list of record fields
	 * @return the new ExcelRecord instance
	 */
	ExcelRecord Record(List<String> aList);

	/**
	 * Constructor method for a new Excel record with this header.
	 *
	 * @param aMap a map of record field values
	 * @return the new ExcelRecord instance
	 */
	ExcelRecord Record(Map<String, String> aMap);
}
