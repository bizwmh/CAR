/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.csv;

/**
 * Processor of a <code>CSVRecord</code>.<br>
 * The CSVFeeder uses a reference to a CSVHandler instance for processing the
 * input CSV records.
 *
 * @version 1.0.0 03.03.2025 12:10:17
 */
public interface CSVHandler {

	/**
	 * Processes a CSV record
	 * 
	 * @param aRecord the record to process
	 */
	void handle(CSVRecord aRecord);

	/**
	 * Called when an exception occurred during the input process.
	 * 
	 * @param anEx the exception thrown by the <code>CSVFeeder</code>.
	 */
	void onError(Exception anEx);

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
