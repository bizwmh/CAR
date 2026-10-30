/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved.
 * -------------------------------------------------------------------------- */

package biz.car.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Substitutes placeholders of the form <code>${aName}</code> in a string by
 * the value of the corresponding entry in a <code>Map</code>.<br>
 * If a placeholder's name has no entry in the map, the placeholder is left
 * unchanged in the result.
 *
 * @version 2.0.0 21.07.2026 00:00:00
 */
public interface Vars {

	/**
	 * Pattern matching a placeholder <code>${aName}</code>. Group 1 holds the
	 * name.
	 */
	Pattern PLACEHOLDER = Pattern.compile("\\$\\{([^}]+)\\}"); //$NON-NLS-1$

	/**
	 * Replaces all placeholders <code>${aName}</code> found in
	 * <code>aString</code> by the value of the entry <code>aName</code> in
	 * <code>aMap</code>.<br>
	 * A placeholder whose name is not found in <code>aMap</code> is left
	 * unchanged.
	 *
	 * @param aString the string to parse, may be <code>null</code>
	 * @param aMap    the map providing the substitution values
	 * @return the string with all resolvable placeholders substituted
	 */
	static String apply(String aString, Map<String, ?> aMap) {
		if (aString == null) {
			return null;
		}

		Matcher l_matcher = PLACEHOLDER.matcher(aString);
		StringBuilder l_sb = new StringBuilder();

		while (l_matcher.find()) {
			String l_name = l_matcher.group(1);
			Object l_value = aMap.get(l_name);
			String l_replacement = l_value != null ? l_value.toString()
					: l_matcher.group();

			l_matcher.appendReplacement(l_sb,
					Matcher.quoteReplacement(l_replacement));
		}
		l_matcher.appendTail(l_sb);

		return l_sb.toString();
	}
}
