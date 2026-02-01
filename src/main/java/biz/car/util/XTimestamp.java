/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import biz.car.bundle.VAL;
import biz.car.config.ACS;

/**
 * Represents a point in time.
 *
 * @version 2.0.0 18.01.2026 12:23:05
 */
public class XTimestamp {

	private static String pattern;
	private static SimpleDateFormat tsf;

	static {
		pattern = ACS.APP.getString(VAL.timestampFormat);
		tsf = new SimpleDateFormat(pattern);
	}

	/**
	 * Gets the current day and time in the format 'YYYY-MM-DD HH:MM:SS:MM'
	 * 
	 * @return the string representing current day and time.
	 */
	public static String asString() {
		return tsf.format(new Date());
	}

	/**
	 * Gets the current day and time in the format given by the format pattern.
	 * 
	 * @param aFormat the format string to be used to build the timestamp format
	 * @return the string representing current day and time.
	 */
	public static String asString(String aFormat) {
		SimpleDateFormat l_format = new SimpleDateFormat(aFormat);

		return l_format.format(new Date());
	}

	/**
	 * Assigns a display format to be used as a string representation for a
	 * <code>XTimestamp</code> instance.
	 * 
	 * @param aPattern the pattern to be used as the display format
	 */
	public static void setFormat(String aPattern) {
		tsf = new SimpleDateFormat(aPattern);
	}

	private Date ts;

	/**
	 * Creates a <code>Timestamp</code> representing the current date and time.
	 */
	public XTimestamp() {
		super();
		ts = new Date();
	}

	/**
	 * Creates a <code>Timestamp</code> from the given date.
	 * 
	 * @param aDate the date to be used as timestamp
	 */
	public XTimestamp(Date aDate) {
		super();
		ts = aDate;
	}

	/**
	 * Assigns a display format to be used as a string representation for this
	 * <code>XTimestamp</code> instance.
	 * 
	 * @param aPattern the pattern to be used as the display format
	 * @return the new <code>XTimestamp</code> instance with the given format
	 */
	public String format(String aPattern) {
		SimpleDateFormat l_tsf = new SimpleDateFormat(aPattern);
		String l_ret = l_tsf.format(ts);

		return l_ret;
	}

	/**
	 * Gets the day and time in the format 'YYYY-MM-DD HH:MM:SS:MM'
	 * 
	 * @return the string representing the date and time.
	 */
	public String toString() {
		return tsf.format(ts);
	}
}
