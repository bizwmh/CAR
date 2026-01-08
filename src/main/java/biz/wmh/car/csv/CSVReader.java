/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.wmh.car.csv;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import biz.wmh.car.io.XFileReader;

/**
 * A <code>CSVReader</code> reads text input from a CSV file on the file system.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class CSVReader extends XFileReader {

	private String delim;
	private CSVHeader hdr;
	private CSVParser myParser;

	/**
	 * Creates a default <code>CSVReader</code> instance.
	 */
	public CSVReader() {
		super();
	}

	/**
	 * @return the CSV header for this reader
	 */
	public CSVHeader header() {
		return hdr;
	}

	/**
	 * Opens this file for input using the specified <code>File</code>.
	 * 
	 * @param aFile the file to use as the input medium.
	 * @throws IOFoundException if the given file could not be found on the file
	 *                          system
	 */
	public void open(File aFile) throws IOException {
		Objects.requireNonNull(aFile);
		super.open(aFile);

		if (delim == null) {
			delim = CSV.DELIMITER;
		}
		myParser = new CSVParser(delim);
		List<String> l_hdr = readFields();
		hdr = CSVRecord.Header(l_hdr).Delimiter(delim);
		
		resetRecordCount();
	}

	/**
	 * Opens the CSV source specified by the given runtime parameters.
	 * 
	 * @param aFrist the initial part of the path to the input file
	 * @param aMore  additional parameters to join the file path
	 * @throws IOException if the CSV source could not be opened
	 */
	public void open(String aFrist, String... aMore) throws IOException {
		Path l_path = Paths.get(aFrist, aMore);
		File l_file = l_path.toFile();

		open(l_file);
	}

	/**
	 * Reads the next line from the CSV input file.
	 * 
	 * @return the text line read from the file or <code>null</code> if end of file
	 *         has been reached.
	 * @throws IOException if an error occurred during the close operation
	 */
	public CSVRecord readRecord() throws IOException {
		List<String> l_fields = readFields();
		CSVRecord l_ret = null;

		if (l_fields != null) {
			l_ret = hdr.Record(l_fields);
		}
		return l_ret;
	}

	/**
	 * Assigns a CSV field delimiter.
	 * 
	 * @param the CSV delimiter
	 * @throws IllegalStateException if the delimiter has already been set.
	 */
	public void setDelimiter(String aDelim) {
		if (myParser == null) {
			delim = Objects.requireNonNull(aDelim);
			
			return;
		}
		throw new IllegalStateException();
	}

	private List<String> readFields() throws IOException {
		String l_line = read();

		while (l_line != null && myParser.parse(l_line) != true) {
			l_line = read();
		}
		return myParser.fields();
	}
}
