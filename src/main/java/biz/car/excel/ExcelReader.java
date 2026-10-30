/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved.
 * -------------------------------------------------------------------------- */

package biz.car.excel;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * An <code>ExcelReader</code> reads the records of a single sheet from an
 * Excel workbook (<code>.xls</code> or <code>.xlsx</code>) on the file
 * system.<br>
 * The first row of the sheet is treated as the header row. Cell values are
 * rendered to their displayed text (as Excel would show them), including
 * evaluated formula results.
 *
 * @version 1.0.0 18.07.2026
 */
public class ExcelReader implements Closeable {

	private int count;
	private final DataFormatter fmt = new DataFormatter();
	private ExcelHeader hdr;
	private FormulaEvaluator myEvaluator;
	private int rowIndex;
	private Sheet sheet;
	private int sheetIndex;
	private String sheetName;
	private Workbook workbook;

	/**
	 * Creates a default <code>ExcelReader</code> instance.
	 */
	public ExcelReader() {
		super();
	}

	/**
	 * Closes the underlying workbook and releases all system resources allocated
	 * to it.
	 *
	 * @throws IOException if an error occurred during the close operation
	 */
	@Override
	public void close() throws IOException {
		if (workbook != null) {
			workbook.close();
			workbook = null;
		}
	}

	/**
	 * Returns the current value of the internal record counter. A
	 * <code>readRecord</code> operation increases this counter by 1 if the sheet
	 * end has not yet been reached.
	 *
	 * @return the number of records read from the sheet
	 */
	public int getRecordCount() {
		return count;
	}

	/**
	 * @return the header for the sheet opened by this reader
	 */
	public ExcelHeader header() {
		return hdr;
	}

	/**
	 * Opens the given workbook file for input and selects the sheet configured via
	 * {@link #setSheetName(String)} or {@link #setSheetIndex(int)} (the first
	 * sheet, by default).
	 *
	 * @param aFile the workbook file to use as the input medium.
	 * @throws IOException              if the given file could not be found or
	 *                                  read
	 * @throws IllegalStateException    if this reader has already been opened
	 * @throws IllegalArgumentException if the configured sheet does not exist in
	 *                                  the workbook
	 */
	public void open(File aFile) throws IOException {
		Objects.requireNonNull(aFile);

		if (workbook != null) {
			throw new IllegalStateException();
		}
		workbook = WorkbookFactory.create(aFile, null, true);
		myEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
		sheet = sheetName != null ? workbook.getSheet(sheetName)
				: workbook.getSheetAt(sheetIndex);

		if (sheet == null) {
			throw new IllegalArgumentException(sheetName);
		}
		List<String> l_hdr = readHeaderRow();
		hdr = ExcelRecord.Header(l_hdr);
		resetRecordCount();
	}

	/**
	 * Opens the workbook source specified by the given runtime parameters.
	 *
	 * @param aFirst the initial part of the path to the input file
	 * @param aMore  additional parameters to join the file path
	 * @throws IOException if the workbook source could not be opened
	 */
	public void open(String aFirst, String... aMore) throws IOException {
		Path l_path = Paths.get(aFirst, aMore);
		File l_file = l_path.toFile();

		open(l_file);
	}

	/**
	 * Reads the next row from the sheet.
	 *
	 * @return the record read from the sheet or <code>null</code> if the end of
	 *         the sheet has been reached.
	 */
	public ExcelRecord readRecord() {
		List<String> l_fields = readFields();
		ExcelRecord l_ret = null;

		if (l_fields != null) {
			l_ret = hdr.Record(l_fields);
		}
		return l_ret;
	}

	/**
	 * Resets the internal record counter back to zero.
	 */
	public void resetRecordCount() {
		count = 0;
	}

	/**
	 * Selects the sheet to read by its zero-based index.
	 *
	 * @param anIndex the index of the sheet within the workbook
	 * @throws IllegalStateException if this reader has already been opened.
	 */
	public void setSheetIndex(int anIndex) {
		if (workbook != null) {
			throw new IllegalStateException();
		}
		sheetIndex = anIndex;
		sheetName = null;
	}

	/**
	 * Selects the sheet to read by its name.
	 *
	 * @param aName the name of the sheet within the workbook
	 * @throws IllegalStateException if this reader has already been opened.
	 */
	public void setSheetName(String aName) {
		if (workbook != null) {
			throw new IllegalStateException();
		}
		sheetName = Objects.requireNonNull(aName);
	}

	/**
	 * Renders the display value of the cell at the given column in the given row.
	 *
	 * @param aRow the row (may be <code>null</code> for a fully blank row)
	 * @param aCol the zero-based column index
	 * @return the cell text or the <i>NullString</i> if the cell is empty
	 */
	private String cellValue(Row aRow, int aCol) {
		Cell l_cell = aRow == null ? null : aRow.getCell(aCol);
		String l_ret = l_cell == null ? "" : fmt.formatCellValue(l_cell, myEvaluator); //$NON-NLS-1$

		return l_ret;
	}

	/**
	 * Reads the next non-blank row of the sheet, skipping fully empty rows.
	 *
	 * @return the field values of the row or <code>null</code> if the end of the
	 *         sheet has been reached.
	 */
	private List<String> readFields() {
		List<String> l_ret = null;

		while (l_ret == null && rowIndex <= sheet.getLastRowNum()) {
			Row l_row = sheet.getRow(rowIndex++);

			if (l_row != null) {
				l_ret = new ArrayList<>();

				for (int i = 0; i < hdr.columns().size(); i++) {
					l_ret.add(cellValue(l_row, i));
				}
			}
		}
		if (l_ret != null) {
			count++;
		}
		return l_ret;
	}

	/**
	 * Reads row 0 of the selected sheet as the header row.
	 *
	 * @return the list of trimmed header column names
	 */
	private List<String> readHeaderRow() {
		Row l_row = sheet.getRow(0);
		List<String> l_ret = new ArrayList<>();

		if (l_row != null) {
			int l_last = l_row.getLastCellNum();

			for (int i = 0; i < l_last; i++) {
				l_ret.add(cellValue(l_row, i).trim());
			}
		}
		rowIndex = 1;

		return l_ret;
	}
}
