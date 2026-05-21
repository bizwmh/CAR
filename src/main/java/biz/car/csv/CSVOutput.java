/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved.
 * -------------------------------------------------------------------------- */

package biz.car.csv;

import java.io.IOException;

import biz.car.CAR;
import biz.car.config.CConfig;

/**
 * A {@link CSVHandler} that writes incoming CSV records to an output file.<br>
 * The output file name is read from the configuration key {@code output}. User
 * of this class must call <code>dispose</code> as the last method in order to
 * release all allocated resources.
 *
 * @version 1.0.0 20.05.2026
 */
public class CSVOutput extends CConfig implements CAR, CSVHandler {

	private CSVWriter wrt;

	/**
	 * Creates a default <code>CSVOutput</code> instance.
	 */
	public CSVOutput() {
		super();
	}

	/**
	 * Closes the underlying {@link CSVWriter} and releases it.<br>
	 * Called by both {@link #onExit()} and {@link #onError(Exception)}. Safe to
	 * call more than once.
	 */
	public void dispose() {
		if (wrt != null) {
			try {
				wrt.close();
			} catch (IOException anEx) {
				error(anEx);
			}
			wrt = null;
		}
	}

	/**
	 * Writes the given record to the output file.
	 *
	 * @param aRecord the record to write
	 */
	@Override
	public void handle(CSVRecord aRecord) {
		try {
			wrt.write(aRecord);
		} catch (IOException anEx) {
			throw exception(anEx);
		}
	}

	@Override
	public void onExit() {
		// nothing to do
	}

	/**
	 * Opens the output file before processing starts.<br>
	 * The file name is obtained from the configuration key {@code output}.
	 */
	@Override
	public void onInit() {
		wrt = new CSVWriter();
		String l_out = getString(OUTPUT);

		if (hasConfig(l_out)) {
			l_out = config().getConfig(l_out).getString(PATH);
		}

		try {
			wrt.open(l_out);
		} catch (IOException anEx) {
			throw exception(anEx);
		}
	}
}
