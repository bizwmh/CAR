/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved.
 * -------------------------------------------------------------------------- */

package biz.car.excel;

import biz.car.config.Configurable;

/**
 * Processor of an <code>ExcelRecord</code>.<br>
 * The ExcelFeeder uses a reference to an ExcelHandler instance for processing
 * the input Excel records.
 *
 * @version 1.0.0 18.07.2026
 */
public interface ExcelHandler extends Configurable {

	/**
	 * Releases all allocated resources.<br>
	 * When this method has finished this <code>ExcelHandler</code> instance shall
	 * no longer be usable.
	 */
	void dispose();

	/**
	 * Processes an Excel record
	 *
	 * @param aRecord the record to process
	 */
	void handle(ExcelRecord aRecord);

	/**
	 * Called when an exception occurred during the input process.
	 * By default the exception message is logged.
	 *
	 * @param anEx the exception thrown by the <code>ExcelFeeder</code>.
	 */
	default void onError(Exception anEx) {
		error(anEx);
	}

	/**
	 * Called once after the ExcelFeeder has finished processing the input
	 * records.
	 */
	void onExit();

	/**
	 * Called once before the ExcelFeeder starts processing the input records.
	 */
	void onInit();
}
