/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.csv;

import java.util.function.Consumer;

/**
 * Processor of a <code>CSVRecord</code>.<br>
 * The CSVFeeder uses a reference to a CSVConsumer instance for processing the
 * input CSV records.
 *
 * @version 1.0.0 03.03.2025 12:10:17
 */
public interface CSVConsumer extends Consumer<CSVRecord> {

	/**
	 * Called once before the CSVFeeder starts processing the input records.
	 */
	void onInit();

	/**
	 * Called once after the CSVFeeder has finished the processing the input
	 * records.
	 */
	void onExit();

	/**
	 * Called when an exception occurred during the input process.
	 * 
	 * @param anEx the exception thrown by the <code>CSVFeeder</code>.
	 */
	void onError(Exception anEx);
}
