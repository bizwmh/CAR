/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved.
 * -------------------------------------------------------------------------- */

package biz.car.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

import biz.car.util.KeyValuePairs;

/**
 * Converts a source Excel record to a new Excel record with a different
 * header.
 *
 * @version 1.0.0 18.07.2026
 */
public class ExcelMapper implements UnaryOperator<ExcelRecord> {

	/**
	 * Factory method for an <code>ExcelMapper</code>.<br>
	 * Key-value pairs are loaded from the given file. The keys defines the header
	 * columns of the new Excel record created by the <code>apply</code> method.<br>
	 * The values define the header columns of the source record given as the
	 * argument of the <code>apply</code> method.
	 *
	 * @param aFile the name of the file holding the key-value pairs.
	 * @return the new <code>ExcelMapper</code> instance
	 */
	public static ExcelMapper parseFile(String aFile) {
		KeyValuePairs l_pairs = KeyValuePairs.parseFile(aFile);
		ExcelMapper l_ret = new ExcelMapper(l_pairs);

		return l_ret;
	}

	private ExcelHeader hdr;
	private KeyValuePairs kv;

	/**
	 * Creates a default <code>ExcelMapper</code> instance.
	 */
	public ExcelMapper() {
		super();
	}

	/**
	 * Creates a default <code>ExcelMapper</code> instance.
	 *
	 * @param aKV the associated key-value pairs
	 */
	public ExcelMapper(KeyValuePairs aKV) {
		super();

		kv = aKV;
		hdr = ExcelRecord.Header(kv.keys());
	}

	@Override
	public ExcelRecord apply(ExcelRecord aRecord) {
		ExcelRecord l_rec = Objects.requireNonNull(aRecord);
		List<String> l_list = new ArrayList<String>();

		for (String l_field : hdr.columns()) {
			String l_key = kv.valueOf(l_field);
			String l_value = valueOf(l_rec, l_key);

			l_list.add(l_value);
		}
		ExcelRecord l_ret = hdr.Record(l_list);

		return l_ret;
	}

	/**
	 * Looks up a value in the source record.
	 *
	 * @param aRecord the record to inspect
	 * @param aKey    the key for the field
	 * @return the value found or the <code>NullString</code>
	 */
	protected String valueOf(ExcelRecord aRecord, String aKey) {
		String l_ret = aRecord.optionalValue(aKey).orElse(""); //$NON-NLS-1$

		return l_ret;
	}
}
