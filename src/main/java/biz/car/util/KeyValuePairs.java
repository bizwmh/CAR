/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import biz.car.SYS;

/**
 * A collection of key-value pairs.
 *
 * @version 1.0.0 07.03.2026 16:00:09
 */
public class KeyValuePairs {

	/**
	 * Loads the key-value pairs from the given file.
	 * 
	 * @param aFile the name of the file
	 * @return the new <code>KeyValuepairs</code> instance
	 */
	public static KeyValuePairs parseFile(String aFile) {
		try {
			KeyValuePairs l_ret = new KeyValuePairs();
			File l_file = new File(aFile);
			Path l_path = l_file.toPath();
			List<String> l_lines = Files.readAllLines(l_path,
					StandardCharsets.UTF_8);

			List<String> l_list = l_lines.stream()
					.map(l -> l.strip())
					.filter(l -> l.length() > 0 && !l.startsWith("#")) //$NON-NLS-1$
					.map(l -> {
						String l_line = l;
						int l_ind = l.indexOf("#"); //$NON-NLS-1$

						if (l_ind > 0) {
							l_line = l.substring(0, l_ind);
						}
						return l_line;
					})
					.collect(Collectors.toList());
			l_list.forEach(l -> {
				String[] l_split = l.split("=", 2); //$NON-NLS-1$

				switch (l_split.length) {
				case 1:
					l_ret.k2v.put(l_split[0].strip(), l_split[0].strip());
					l_ret.v2k.put(l_split[0].strip(), l_split[0].strip());
					break;

				case 2:
					l_ret.k2v.put(l_split[0].strip(), l_split[1].strip());
					l_ret.v2k.put(l_split[1].strip(), l_split[0].strip());
					break;

				default:
					break;
				}
			});
			return l_ret;
		} catch (IOException anEx) {
			throw SYS.LOG.exception(anEx);
		}
	}

	private Map<String, String> k2v = new LinkedHashMap<String, String>();
	private Map<String, String> v2k = new LinkedHashMap<String, String>();

	/**
	 * Creates a default <code>KeyValuePairs</code> instance.
	 */
	private KeyValuePairs() {
		super();
	}

	/**
	 * Looks up a key in this pair.
	 * 
	 * @param aValue the value for the searched key
	 * @return the value found or <code>null</code>
	 */
	public String keyOf(String aValue) {
		return v2k.get(aValue);
	}

	/**
	 * @return the list of keys
	 */
	public List<String> keys() {
		return k2v.keySet().stream().toList();
	}

	/**
	 * Looks up a value in this pair.
	 * 
	 * @param aKey the key for the value
	 * @return the value found or <code>null</code>
	 */
	public String valueOf(String aKey) {
		return k2v.get(aKey);
	}

	/**
	 * @return the list of values
	 */
	public List<String> values() {
		return v2k.keySet().stream().toList();
	}
}
