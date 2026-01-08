/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.wmh.car.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * If the string contains underscores:
 * <ul>
 * <li>a single underscore is replaced by a dot '.'
 * <li>a double underscore is replaced by a dash '-'
 * <li>a triple underscore is replaced by an underscore '_'
 * </ul>
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface Underscore {

	/**
	 * Constant for a dash '-'.
	 */
	String DASH = "-"; //$NON-NLS-1$

	/**
	 * Constant for a dot '.'.
	 */
	String DOT = "."; //$NON-NLS-1$

	/**
	 * Constant for an underscore '_'.
	 */
	String US = "_"; //$NON-NLS-1$

	/**
	 * Constant for a sequence of 1, 2 or 3 underscores.
	 */
	Pattern US3 = Pattern.compile("_{1,3}"); //$NON-NLS-1$

	static String apply(String aString) {
		StringBuilder l_sb = new StringBuilder();
		Matcher l_matcher = US3.matcher(aString);

		while (l_matcher.find()) {
			int len = l_matcher.end() - l_matcher.start();

			switch (len) {
			case 1:
				l_matcher.appendReplacement(l_sb, DOT);
				break;
			case 2:
				l_matcher.appendReplacement(l_sb, DASH);
				break;
			case 3:
				l_matcher.appendReplacement(l_sb, US);
				break;
			default:
				l_matcher.appendReplacement(l_sb, l_matcher.group());
			}
		}
		l_matcher.appendTail(l_sb);

		return l_sb.toString();
	}
}
