/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.io;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import biz.car.util.SFI;

/**
 * Utility class providing cached access to {@link Charset} instances.
 * <p>
 * Three standard charsets are pre-registered under symbolic keys that match
 * their field names (resolved by {@link biz.car.util.SFI}):
 * <ul>
 *   <li>{@code "ASCII"} — {@link StandardCharsets#US_ASCII}</li>
 *   <li>{@code "LATIN"} — {@link StandardCharsets#ISO_8859_1}</li>
 *   <li>{@code "UTF8"}  — {@link StandardCharsets#UTF_8}</li>
 * </ul>
 * Any other key is resolved on first use via {@link Charset#forName(String)}
 * and cached for subsequent lookups.
 *
 * @version 2.0.0 22.05.2026 14:12:05
 */
public class XCharset {

	private static String ASCII;
	private static Map<String, Charset> csMap;
	private static String LATIN;
	private static String UTF8;

	static {
		csMap = new HashMap<String, Charset>();

		SFI.initialize(XCharset.class);
		csMap.put(LATIN, StandardCharsets.ISO_8859_1);
		csMap.put(ASCII, StandardCharsets.US_ASCII);
		csMap.put(UTF8, StandardCharsets.UTF_8);
	}

	/**
	 * Returns the {@link Charset} registered under the given key.
	 * <p>
	 * The pre-defined keys {@code "ASCII"}, {@code "LATIN"}, and {@code "UTF8"}
	 * map to {@link StandardCharsets#US_ASCII}, {@link StandardCharsets#ISO_8859_1},
	 * and {@link StandardCharsets#UTF_8} respectively.  Any other key is treated
	 * as a canonical charset name and resolved via {@link Charset#forName(String)};
	 * the result is cached for subsequent calls.
	 *
	 * @param aKey a pre-defined symbolic key or a canonical charset name
	 * @return the corresponding {@link Charset}; never {@code null}
	 * @throws java.nio.charset.UnsupportedCharsetException if {@code aKey} is
	 *         neither a pre-defined key nor a supported charset name
	 */
	public static Charset get(String aKey) {
		Charset l_ret = csMap.get(aKey);

		if (l_ret == null) {
			l_ret = Charset.forName(aKey);

			csMap.put(aKey, l_ret);
		}
		return l_ret;
	}

	/**
	 * Creates a default <code>XCharacterSet</code> instance.
	 */
	private XCharset() {
		super();
	}
}
