/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.wmh.car.csv;

import java.util.regex.Pattern;

/**
 * CSV constants.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class CSV {

	public static final String DELIMITER;
	public static final String DQ;
	public static final Pattern DQ_PATTERN;
	public static final String DQ2;
	public static final String DQ2_END;
	public static final Pattern DQ2_PATTERN;
	public static final String NEWLINE;
	public static final String QUOTE;

	// -------------------------------------------------------------------------
	// Initialize the static fields
	// -------------------------------------------------------------------------
	static {
		DELIMITER = ";"; //$NON-NLS-1$
		DQ = "\""; //$NON-NLS-1$
		DQ2 = "\"\""; //$NON-NLS-1$
		DQ2_END = "\"+$"; //$NON-NLS-1$
		NEWLINE = "\n"; //$NON-NLS-1$
		String l_pattern = "[" + DELIMITER + DQ + NEWLINE + "]"; //$NON-NLS-1$ //$NON-NLS-2$
		DQ_PATTERN = Pattern.compile(l_pattern);
		DQ2_PATTERN = Pattern.compile(DQ2_END);
		QUOTE = ""; //$NON-NLS-1$
	}

	private CSV() {
		super();
	}
}
