/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.wmh.car.csv;

import java.util.List;
import java.util.Map;

/**
 * The header of a CSV file.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface CSVHeader {

	/**
	 * @return the list of CSV column names
	 */
	List<String> columns();

	/**
	 * Assigns a CSV field delimiter to this header.
	 * 
	 * @param aDelim the delimiter character sequence
	 * @return this header
	 */
	CSVHeader Delimiter(String aDelim);

	/**
	 * Assigns the quote character to this header.
	 * 
	 * @param aQuote the quote character sequence
	 * @return this header
	 */
	CSVHeader Quote(String aQuote);

	/**
	 * Constructor method for an empty CSV record.<br>
	 * The number of null-string fields corresponds to the header size.
	 * 
	 * @return the new CSVRecord instance
	 */
	CSVRecord Record();

	/**
	 * Constructor method for a new CSV record with this header.
	 * 
	 * @param aList the list of record fields
	 * @return the new CSV record instance
	 */
	CSVRecord Record(List<String> aList);

	/**
	 * Constructor method for a new CSV record with this header.
	 * 
	 * @param aMap a map of record field values
	 * @return the new CSV record instance
	 */
	CSVRecord Record(Map<String, String> aMap);
}
