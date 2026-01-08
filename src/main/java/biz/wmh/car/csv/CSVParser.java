/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.wmh.car.csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Breaks a CSV text line into fields.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class CSVParser {

	private static final boolean DONE = true;
	private static final boolean PENDING = false;

	private String delim;
	private boolean state;
	private List<String> fieldList;

	/**
	 * Creates a default <code>CSVParser</code> instance.
	 * 
	 * @param aDelim the CSV field delimiter
	 */
	public CSVParser(String aDelim) {
		super();

		delim = aDelim == null ? CSV.DELIMITER : aDelim;
		fieldList = new ArrayList<>();
		state = DONE;
	}

	/**
	 * @return a list of the parsed field values
	 */
	public List<String> fields() {
		List<String> l_ret = null;

		if (fieldList.size() > 0) {
			l_ret = new ArrayList<>(fieldList);

			fieldList.clear();
		}
		return l_ret;
	}

	/**
	 * Breaks a CSV text line into fields.
	 * 
	 * @param aLine the line to split into fields
	 * @return <code>true</code> if the CSV record is complete, <code>false</code>
	 *         if content is pending in case of a multiline record
	 */
	public boolean parse(String aLine) {
		List<String> l_tokenList = split(aLine);
		String l_token = null;

		for (int i = 0; i < l_tokenList.size(); i++) {
			l_token = l_tokenList.get(i);

			if (state == DONE) {
				state = add(l_token);
			} else {
				if (i > 0) {
					state = append(l_token, delim);
				} else {
					state = append(l_token, CSV.NEWLINE);
				}
			}
		}
		return state;
	}

	private boolean add(String aToken) {
		fieldList.add(aToken);

		return off2on(aToken);
	}

	private boolean append(String aToken, String aDelimiter) {
		int l_index = fieldList.size() - 1;
		String l_token = fieldList.get(l_index);
		l_token = l_token + aDelimiter + aToken;
		fieldList.set(l_index, l_token);

		return on2off(aToken);
	}

	/**
	 * Computes the next state for a given token of a CSV text line..
	 * <p>
	 * This method shall be invoked if the current state of the token is not
	 * pending.
	 * 
	 * @param aToken the CSV token to check
	 * @return <code>true</code> if the token starts with a double quote but does not have an end
	 *         double quote.
	 */
	private boolean off2on(String aToken) {
		boolean l_ret = DONE;

		if (aToken.startsWith(CSV.DQ)) {
			l_ret = PENDING;
			if (aToken.length() > 1) {
				l_ret = on2off(aToken.substring(1));
			}
		}
		return l_ret;
	}

	/**
	 * Computes the next state for a given token of a CSV line.
	 * <p>
	 * This method shall be invoked if the current state of the token is ON.
	 * 
	 * @param aToken the CSV token to check
	 * @return OFF if the token ends with a double quote.
	 */
	private boolean on2off(String aToken) {
		boolean l_ret = PENDING;
		Matcher l_matcher = CSV.DQ2_PATTERN.matcher(aToken);

		if (l_matcher.find()) {
			String l_dq = l_matcher.group();
			int l_rest = l_dq.length() % 2;

			if (l_rest == 1) {
				l_ret = DONE;
			}
		}
		return l_ret;
	}

	private List<String> split(String aLine) {
		String[] l_array = aLine.split(delim);
		List<String> l_ret = new ArrayList<>();

		l_ret.addAll(Arrays.asList(l_array));

		if (aLine.endsWith(delim)) {
			l_ret.add(""); //$NON-NLS-1$
		}
		return l_ret;
	}
}
