/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.csv;

import java.io.File;
import java.io.IOException;

import biz.car.XRunnable;
import biz.car.config.ConfigObject;

/**
 * Processes the records of a CSV file.<br>
 * This class loops over the input records and delegates the processing of the
 * individual records to a <code>CSVHandler</code>.
 *
 * @version 1.0.0 03.03.2025 12:18:00
 */
public class CSVFeeder extends ConfigObject implements XRunnable {

	private CSVHandler myConsumer;
	private CSVReader rdr;

	/**
	 * Creates a default <code>CSVFeeder</code> instance.
	 */
	public CSVFeeder(CSVHandler aConsumer) {
		super();

		rdr = new CSVReader();
		myConsumer = aConsumer;
	}

	@Override
	public void dispose() {
		if (rdr != null) {
			try {
				rdr.close();
			} catch (IOException anEx) {
				myConsumer.onError(anEx);
			}
			rdr = null;
		}
	}

	@Override
	public void exec() {
		try {
			File l_in = inputFile();

			myConsumer.onInit();
			rdr.open(l_in);

			CSVRecord l_rec = rdr.readRecord();

			while (l_rec != null) {
				myConsumer.handle(l_rec);

				l_rec = rdr.readRecord();
			}
			myConsumer.onExit();
		} catch (IOException anEx) {
			myConsumer.onError(anEx);
			;
		}
	}

	/**
	 * The value of the PATH parameter in the config file is taken as the file name.
	 * 
	 * @return a reference to the input CSV file
	 */
	protected File inputFile() {
		String l_path = getString(PATH);
		File l_ret = new File(l_path);

		return l_ret;
	}
}
