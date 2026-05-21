/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.csv;

import biz.car.config.Configurable;

/**
 * Processor of a <code>CSVRecord</code>.<br>
 * The CSVFeeder uses a reference to a CSVHandler instance for processing the
 * input CSV records.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public interface CSVHandler extends Configurable {

	/**
	 * Releases all allocated resources.<br>
	 * When this method has finished this <code>CSVHandler</code> instance shall no
	 * longer be usable.
	 */
	void dispose();

	/**
	 * Processes a CSV record
	 * 
	 * @param aRecord the record to process
	 */
	void handle(CSVRecord aRecord);

	/**
	 * Called when an exception occurred during the input process.
	 * By default the exception message is logged.
	 * 
	 * @param anEx the exception thrown by the <code>CSVFeeder</code>.
	 */
	default void onError(Exception anEx) {
		error(anEx);
	}

	/**
	 * Called once after the CSVFeeder has finished the processing the input
	 * records.
	 */
	void onExit();

	/**
	 * Called once before the CSVFeeder starts processing the input records.
	 */
	void onInit();
}
