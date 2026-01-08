/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.wmh.car.io;

import java.io.File;
import java.util.Date;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import biz.wmh.car.util.XTimestamp;

/**
 * Timestamp related functions for the last modified date of the file and a file
 * name prefix.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface PrefixedFile extends Supplier<File> {

	/**
	 * The string pattern for the last modified date.
	 */
	String MODIFIED = "yyyy-MM-dd HH:mm:ss:SSS"; //$NON-NLS-1$

	/**
	 * The regular expression for the format of the file timestamp prefix
	 */
	Pattern prefix = Pattern.compile("^\\d{8}-\\d{6}-\\d{3}_"); //$NON-NLS-1$

	/**
	 * The string pattern for a timestamp prefix.
	 */
	String PREFIXED = "yyyyMMdd-HHmmss-SSS_"; //$NON-NLS-1$

	/**
	 * Checks a file name if it has a timestamp prefix.
	 * 
	 * @param aName the file name to check
	 * @return <code>true</code> if the file name starts with a timestamp prefix
	 */
	static boolean isPrefixed(String aName) {
		Matcher l_matcher = prefix.matcher(aName);

		return l_matcher.find();
	}

	/**
	 * Removes the timestamp prefix from the file name.
	 * 
	 * @param aName the name of the file
	 * @return the trimmed name
	 */
	static String trimPrefix(String aName) {
		Matcher l_matcher = prefix.matcher(aName);
		String l_ret = l_matcher.replaceFirst(""); //$NON-NLS-1$

		return l_ret;
	}

	/**
	 * Extract the base name from a file.<br>
	 * The base name is the file name of the file without the file extension and
	 * without the timestamp prefix.
	 */
	default String getBaseName() {
		FSObject l_fo = () -> get();
		String l_ret = l_fo.getBaseName();
		l_ret = trimPrefix(l_ret);

		return l_ret;
	}

	/**
	 * @return the last modified date in it's string representation
	 */
	default String lastModifiedString() {
		Date l_last = new Date(get().lastModified());
		String l_ret = new XTimestamp(l_last).format(MODIFIED);

		return l_ret.toString();
	};

	/**
	 * Creates a new File instance.<br>
	 * The file name will be prefixed with the current time.<br>
	 * If the file name is already prefixed, that prefix will be replaced by the
	 * current time.
	 * 
	 * @return the new file with a name prefixed with the current time
	 */
	default File prefix() {
		String l_path = get().getParent();
		String l_name = get().getName();
		Matcher l_matcher = prefix.matcher(l_name);
		l_name = l_matcher.replaceFirst(""); //$NON-NLS-1$
		l_name = XTimestamp.asString(PREFIXED) + l_name;
		File l_ret = new File(l_path, l_name);

		return l_ret;
	}
}
