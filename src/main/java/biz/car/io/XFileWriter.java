/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.io;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * A <code>XFileWriter</code> can be used to write text output.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class XFileWriter implements Closeable {

	private int count;
	private String name;
	private BufferedWriter writer;

	/**
	 * Creates a default <code>XFileWriter</code> instance.
	 * 
	 */
	public XFileWriter() {
		super();
	}

	/**
	 * Closes this file and releases all system resources allocated to this file.
	 * 
	 * @throws IOException If an I/O error occurs
	 */
	public void close() throws IOException {
		if (writer != null) {
			writer.flush();
			writer.close();

			writer = null;
		}
	}

	/**
	 * Flushes the internal write cache.
	 * 
	 * @throws IOException if an error occurred when writing to the file
	 */
	public void flush() throws IOException {
		writer.flush();
	}

	/**
	 * @return the path name of this file
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the number of records written to the file
	 */
	public int getRecordCount() {
		return count;
	}

	/**
	 * Opens this file using the specified <code>File</code>.
	 * 
	 * @param aFile the file to use as the output medium.
	 * @throws IOException if the file could not be opened
	 */
	public void open(File aFile) throws IOException {
		open(aFile, false);
	}

	/**
	 * Opens this file using the specified <code>File</code>. If the second argument
	 * is <code>true</code>, then bytes will be written to the end of the file
	 * rather than the beginning.
	 * 
	 * @param aFile   the file to use as the output medium.
	 * @param bAppend if <code>true</code>, then bytes will be written to the end of
	 *                the file rather than the beginning
	 * @throws IOException if the file could not be opened
	 */
	public void open(File aFile, boolean bAppend) throws IOException {
		if (writer == null) {
			count = 0;
			FileWriter l_fw = new FileWriter(aFile, StandardCharsets.UTF_8, bAppend);
			writer = new BufferedWriter(l_fw);
			name = aFile.getPath();
		}
	}

	/**
	 * Opens the file given by the specified directory and filename for output.
	 * 
	 * @param aDir  the directory path for the filename
	 * @param aName the name of the file
	 * @throws IOException if the file could not be opened
	 */
	public void open(String aDir, String aName) throws IOException {
		File l_file = new File(aDir, aName);
		open(l_file);
	}

	/**
	 * Opens the file given by the specified directory and filename for output. If
	 * the third argument is <code>true</code>, then bytes will be written to the
	 * end of the file rather than the beginning.
	 * 
	 * @param aDir    the directory path for the filename
	 * @param aName   the name of the file
	 * @param bAppend if <code>true</code>, then bytes will be written to the end of
	 *                the file rather than the beginning
	 * @throws IOException if the file could not be opened
	 */
	public void open(String aDir, String aName, boolean bAppend) throws IOException {
		File l_file = new File(aDir, aName);
		open(l_file, bAppend);
	}

	/**
	 * Resets the internal record counter back to zero.
	 */
	public void resetRecordCount() {
		count = 0;
	}

	/**
	 * Writes the specified string to the file.
	 * 
	 * @param aLine the text string to be written.
	 * @throws IOException if an error occurred when writing to the file
	 */
	public void write(String aLine) throws IOException {
		writer.write(aLine, 0, aLine.length());
		writer.newLine();
		count++;
	}
}