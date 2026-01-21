/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.csv;

import java.io.File;
import java.io.IOException;
import java.util.List;

import biz.car.io.XFileWriter;

/**
 * A file writer for a file in CSV format.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class CSVWriter extends XFileWriter {

	private boolean bAppend;
	private CSVHeader hdr;

	/**
	 * Creates a default <code>CSVWriter</code> instance.
	 */
	public CSVWriter() {
		super();
	}

	/**
	 * @return the CSV header for this reader
	 */
	public CSVHeader header() {
		return hdr;
	}

	/**
	 * Opens this writer using the specified <code>File</code>. If the second
	 * argument is <code>true</code>, then CSV records will be written to the end of
	 * the file rather than to the beginning.
	 * 
	 * @param aFile   the file to use as the output medium.
	 * @param bAppend if <code>true</code>, then bytes will be written to the end of
	 *                the file rather than to the beginning
	 * @throws IOException if the file could not be opened
	 */
	public void open(File aFile, boolean anAppend) throws IOException {
		bAppend = aFile.exists() && anAppend;

		super.open(aFile, anAppend);
	}

	/**
	 * Opens the file given by the specified filename for output.
	 * 
	 * @param aName the name of the file
	 * @throws IOException if open fails
	 */
	public void open(String aName) throws IOException {
		File l_file = new File(aName);

		open(l_file);
	}

	/**
	 * Writes the specified record string to the file.
	 * 
	 * @param aRecord the text string to be written as a CSV record.
	 * @throws IOException if an error occurred when writing to the file
	 */
	public void write(CSVRecord aRecord) throws IOException {
		if (hdr == null) {
			hdr = aRecord.hdr;

			if (!bAppend) {
				write(hdr.toString());
				resetRecordCount();
			}
		}
		String l_line = aRecord.toString();

		write(l_line);
	}

	/**
	 * Writes the header for this CSV file.
	 * 
	 * @param aList the list of header field names
	 * @throws IOException           if an error occurred when writing to the file
	 * @throws IllegalStateException if this method is not called as first write
	 *                               operation after opening the file
	 */
	public void writeHeader(List<String> aList) throws IOException {
		if (getRecordCount() == 0) {
			hdr = CSVRecord.Header(aList);
			String l_line = hdr.toString();

			write(l_line);
			resetRecordCount();
		} else {
			throw new IllegalStateException();
		}
	}
}
