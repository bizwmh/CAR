/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.csv;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * Functions to convert field content from CSV format to display format and vice
 * versa.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface CSVField {

	/**
	 * Converts a text value into CSV format.
	 * <p>
	 * 
	 * If the value contains at least one of the characters
	 * <ul>
	 * <li>new line
	 * <li>double quote
	 * <li>field separator (default is ;)
	 * </ul>
	 * then
	 * <ul>
	 * <li>double quote is replaced by 2 double quotes
	 * <li>the value is enclosed by quotes
	 * </ul>
	 * 
	 * @param aValue the text value to convert
	 * @return the converted value or the original value
	 */
	static String toCSVFormat(String aValue) {
		String l_ret = Objects.requireNonNull(aValue);
		Matcher l_matcher = CSV.DQ_PATTERN.matcher(aValue);

		if (l_matcher.find()) {
			l_ret = aValue.replace(CSV.DQ, CSV.DQ2);
			l_ret = CSV.DQ + l_ret + CSV.DQ;
		}
		return l_ret;
	};

	/**
	 * Converts a CSV field value into text format.
	 * <p>
	 * 
	 * If the value starts with a quote
	 * <ul>
	 * <li>enclosing quotes are removed
	 * <li>2 double quotes are replaced by a single double quote
	 * </ul>
	 * 
	 * @param aValue the CSV field value to convert
	 * @return the converted value or the original value
	 */
	static String toDisplayFormat(String aValue) {
		String l_ret = Objects.requireNonNull(aValue);

		if (aValue.startsWith(CSV.DQ)) {
			l_ret = aValue.substring(1, aValue.length() - 1);
			l_ret = l_ret.replace(CSV.DQ2, CSV.DQ);
		}
		return l_ret;
	};
}
