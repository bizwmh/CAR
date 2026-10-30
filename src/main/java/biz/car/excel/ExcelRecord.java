/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved.
 * -------------------------------------------------------------------------- */

package biz.car.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import biz.car.io.DataRecord;
import biz.car.io.FieldSource;

/**
 * A data record read from a row of an Excel sheet.
 *
 * @version 1.0.0 18.07.2026
 */
public class ExcelRecord implements DataRecord {

	private static class Header implements ExcelHeader {

		private List<String> columns;

		/**
		 * Creates a default <code>Header</code> instance.
		 *
		 * @param aList the list of header columns
		 */
		public Header(List<String> aList) {
			super();

			List<String> l_list = aList.stream()
					.map(f -> f.trim())
					.toList();
			columns = List.copyOf(l_list);
		}

		@Override
		public List<String> columns() {
			return columns;
		}

		@Override
		public ExcelRecord Record() {
			ExcelRecord l_ret = new ExcelRecord(this);
			l_ret.fields = columns.stream()
					.map(s -> "") //$NON-NLS-1$
					.collect(Collectors.toList());

			return l_ret;
		}

		@Override
		public ExcelRecord Record(List<String> aList) {
			List<String> l_list = ensureSize(aList);
			ExcelRecord l_ret = new ExcelRecord(this);
			l_ret.fields = l_list;

			return l_ret;
		}

		@Override
		public ExcelRecord Record(Map<String, String> aMap) {
			ExcelRecord l_ret = new ExcelRecord(this);
			l_ret.fields = columns.stream()
					.map(s -> {
						if (aMap.containsKey(s)) {
							return aMap.get(s);
						}
						return ""; //$NON-NLS-1$
					})
					.collect(Collectors.toList());

			return l_ret;
		}

		@Override
		public String toString() {
			return String.join(" | ", columns); //$NON-NLS-1$
		}

		/**
		 * Makes sure that a given list of field values is adjusted to the size of this
		 * header.
		 * <p>
		 * If the size of the given list is greater than the header size fields are
		 * truncated.<br>
		 * If the size of the given list is less than the header size null-string fields
		 * are added.
		 *
		 * @param aFields the list of field values
		 * @return a field list with the size of the header
		 * @throws NullPointerException if <code>aFields</code> is <code>null</code>
		 */
		private List<String> ensureSize(List<String> aFields) {
			List<String> l_ret = Objects.requireNonNull(aFields);

			if (l_ret.size() != columns.size()) {
				List<String> l_list = l_ret;
				l_ret = new ArrayList<>();
				String l_value = null;

				for (int i = 0; i < columns.size(); i++) {
					l_value = i < l_list.size() ? l_list.get(i) : ""; //$NON-NLS-1$

					l_ret.add(l_value);
				}
			}
			return l_ret;
		}
	}

	/**
	 * Constructs a new Excel header with the given columns.
	 *
	 * @param aList the list of header columns
	 * @return the new Excel header
	 */
	public static ExcelHeader Header(List<String> aList) {
		ExcelHeader l_ret = new Header(aList);

		return l_ret;
	}

	public final Header hdr;

	private List<String> fields;

	/**
	 * Creates a default <code>ExcelRecord</code> instance.
	 *
	 * @param aHeader the header for this record
	 */
	private ExcelRecord(Header aHeader) {
		super();

		hdr = aHeader;
	}

	@Override
	public List<String> fieldNames() {
		return hdr.columns;
	}

	/**
	 * Looks up a single field value in the current record.
	 *
	 * @param aName the name of field in the header of this sheet
	 * @return the value found
	 * @throws IndexOutOfBoundsException if the field name is not defined
	 */
	@Override
	public String getValue(String aName) {
		int l_ind = hdr.columns.indexOf(aName);
		String l_ret = fields.get(l_ind);

		return l_ret;
	}

	/**
	 * Checks if this record has a field with the given name.
	 *
	 * @param aName the name of field in the header of this sheet
	 * @return <code>true</code> if the field exists
	 */
	@Override
	public boolean hasField(String aName) {
		int l_ind = hdr.columns.indexOf(aName);
		boolean l_ret = l_ind != -1;

		return l_ret;
	}

	@Override
	public Optional<String> optionalValue(String aName) {
		String l_ret = null;
		int l_ind = hdr.columns.indexOf(aName);

		if (l_ind != -1) {
			l_ret = fields.get(l_ind);
		}
		return Optional.ofNullable(l_ret);
	}

	/**
	 * Replaces the values of this record by the values from the field source.<br>
	 * If a field value is not contained in the source the field value is set to the
	 * <i>NullString</i>.
	 *
	 * @param aSource the field source
	 */
	public void put(FieldSource aSource) {
		fieldNames().forEach(name -> {
			String l_value = aSource.optionalValue(name).orElse(""); //$NON-NLS-1$
			int l_ind = hdr.columns.indexOf(name);

			fields.set(l_ind, l_value);
		});
	}

	/**
	 * Sets a single field value.
	 *
	 * @param aName  the name of the field as given in the sheet header.
	 * @param aValue the value to set
	 * @return the current Excel record instance with the given field value.
	 * @throws NullPointerException      if one of the parameters is
	 *                                   <code>null</code>
	 * @throws IndexOutOfBoundsException if the field name is not defined
	 */
	@Override
	public ExcelRecord setValue(String aName, String aValue) {
		Objects.requireNonNull(aName);
		Objects.requireNonNull(aValue);

		int l_ind = hdr.columns.indexOf(aName);

		fields.set(l_ind, aValue);

		return this;
	}

	@Override
	public String toString() {
		return String.join(" | ", fields); //$NON-NLS-1$
	}

	/**
	 * @return the list of record field values
	 */
	public List<String> values() {
		return new ArrayList<>(fields);
	}
}
