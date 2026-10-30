/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved.
 * -------------------------------------------------------------------------- */

package biz.car.excel;

import biz.car.CAR;
import biz.car.config.CConfig;

/**
 * The default {@link ExcelHandler} used by an {@link ExcelFeeder} when no
 * other handler is given.<br>
 * Writes every incoming Excel record to the application log.
 *
 * @version 1.0.0 18.07.2026
 */
public class ExcelPrinter extends CConfig implements CAR, ExcelHandler {

	/**
	 * Creates a default <code>ExcelPrinter</code> instance.
	 */
	public ExcelPrinter() {
		super();
	}

	@Override
	public void dispose() {
		// nothing to do
	}

	/**
	 * Logs the given record.
	 *
	 * @param aRecord the record to log
	 */
	@Override
	public void handle(ExcelRecord aRecord) {
		info(aRecord.toString());
	}

	@Override
	public void onExit() {
		// nothing to do
	}

	@Override
	public void onInit() {
		// nothing to do
	}
}
