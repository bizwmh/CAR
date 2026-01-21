/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.util;

/**
 * Provides an integer sequence number. The display length is fixed, i.e. the
 * number of digits is fixed and leading zeroes will be added.
 * 
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class SequenceNumber {

	/**
	 * The maximum number of digits for a <code>SequenceNumber</code>.
	 */
	public static final int maxDigits = 9;
	private static final Object lock = new Object();
	private static final String nulls = "000000000"; //$NON-NLS-1$

	private int maxValue;
	private int sn;
	private int snDigits;

	/**
	 * Creates a default <code>SequenceNumber</code> instance with 5 digits.
	 */
	public SequenceNumber() {
		super();

		setDigits(5);
	}

	/**
	 * @return the next sequence number as an integer
	 */
	public int getInt() {
		synchronized (lock) {
			sn = sn + 1;

			if (sn == maxValue) {
				sn = 1;
			}
			return sn;
		}
	}

	/**
	 * @return the next sequence number as a string
	 */
	public String getString() {
		synchronized (lock) {
			getInt();

			return toString();
		}
	}

	/**
	 * Sets the number of digits for this sequence number.
	 * 
	 * @param aNumber
	 *            the number to set
	 * @throws IllegalArgumentException
	 *             if the number is greater than <code>maxDigits</code>.
	 */
	public void setDigits(int aNumber) {
		if (aNumber > maxDigits) {
			throw new IllegalArgumentException();
		}
		snDigits = aNumber;
		maxValue = 1;

		for (int i = 0; i < aNumber; i++) {
			maxValue = maxValue * 10;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String l_ret = nulls + sn;
		l_ret = l_ret.substring(l_ret.length() - snDigits);

		return l_ret;
	}
}
