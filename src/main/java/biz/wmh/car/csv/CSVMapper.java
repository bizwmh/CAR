/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.wmh.car.csv;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import biz.wmh.car.SYS;

/**
 * Converts a source CSV record to a new CSV record with a different header.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class CSVMapper implements UnaryOperator<CSVRecord> {

	private Map<String, String> fieldMap;
	private CSVHeader hdr;

	/**
	 * Creates a default <code>CSVMapper</code> instance.
	 */
	public CSVMapper() {
		super();

		fieldMap = new HashMap<String, String>();
	}

	@Override
	public CSVRecord apply(CSVRecord aRecord) {
		CSVRecord l_ret = aRecord;

		if (l_ret != null) {
			List<String> l_list = hdr.columns().stream()
					.map(field -> fieldMap.get(field))
					.map(field -> aRecord.optionalValue(field).orElse("")) //$NON-NLS-1$
					.collect(Collectors.toList());
			l_ret = hdr.Record(l_list);
		}
		return l_ret;
	}

	/**
	 * Loads a CSV mapping from a file.<br>
	 * The mapping is a list of key-value pairs. The keys defines the header columns
	 * of the new CSV record created by the <code>apply</code> method.<br>
	 * The values define the header columns of the source record given as the
	 * argument of the <code>apply</code> method.
	 * 
	 * @param aName the name of the file to load.
	 */
	public void load(String aName) {
		try {
			List<String> l_hdr = new ArrayList<String>();
			File l_file = new File(aName);
			Path l_path = l_file.toPath();
			List<String> l_lines = Files.readAllLines(l_path, StandardCharsets.UTF_8);
			List<String> l_list = l_lines.stream()
					.map(l -> l.trim())
					.filter(l -> l.length() > 0 && !l.startsWith("#")) //$NON-NLS-1$
					.map(l -> {
						String l_ret = l;
						int l_ind = l.indexOf("#"); //$NON-NLS-1$

						if (l_ind > 0) {
							l_ret = l.substring(0, l_ind);
						}
						return l_ret;
					})
					.collect(Collectors.toList());
			l_list.forEach(l -> {
				String[] l_split = l.split("=", 2); //$NON-NLS-1$

				switch (l_split.length) {
				case 1:
					l_hdr.add(l_split[0]);
					fieldMap.put(l_split[0], l_split[0]);
					break;

				case 2:
					l_hdr.add(l_split[0]);
					fieldMap.put(l_split[0], l_split[1]);
					break;

				default:
					break;
				}
			});
			hdr = CSVRecord.Header(l_hdr);
		} catch (IOException anEx) {
			throw SYS.LOG.exception(anEx);
		}
	}

}
