/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

import biz.car.util.KeyValuePairs;

/**
 * Converts a source CSV record to a new CSV record with a different header.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class CSVMapper implements UnaryOperator<CSVRecord> {

	/**
	 * Factory method for a <code>CSVMapper</code>.<br>
	 * Key-value pairs are loaded from the given file. The keys defines the header
	 * columns of the new CSV record created by the <code>apply</code> method.<br>
	 * The values define the header columns of the source record given as the
	 * argument of the <code>apply</code> method.
	 * 
	 * @param aFile the name of the file holding the key-value pairs.
	 * @return the new <code>CSVMapper</code> instance
	 */
	public static CSVMapper parseFile(String aFile) {
		CSVMapper l_ret = new CSVMapper();
		KeyValuePairs l_pairs = KeyValuePairs.parseFile(aFile);
		l_ret.kv = l_pairs;
		l_ret.hdr = CSVRecord.Header(l_pairs.keys());

		return l_ret;
	}

	private CSVHeader hdr;
	private KeyValuePairs kv;

	/**
	 * Creates a default <code>CSVMapper</code> instance.
	 */
	public CSVMapper() {
		super();
	}

	@Override
	public CSVRecord apply(CSVRecord aRecord) {
		CSVRecord l_ret = Objects.requireNonNull(aRecord);
		List<String> l_list = new ArrayList<String>();

		for (String l_field : hdr.columns()) {
			String l_value = kv.valueOf(l_field);
			l_value = l_ret.optionalValue(l_value).orElse(""); //$NON-NLS-1$

			l_list.add(l_value);
		}
		l_ret = hdr.Record(l_list);

		return l_ret;
	}
}
