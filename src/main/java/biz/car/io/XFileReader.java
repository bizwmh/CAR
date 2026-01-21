/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * A <code>XFileReader</code> can be used to read text input from a file on the
 * file system.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class XFileReader implements Closeable {

	private int count;
	private String name;
	private BufferedReader rdr;

	/**
	 * Creates a default <code>XFileReader</code> instance.
	 */
	public XFileReader() {
		super();
	}

	/**
	 * Closes this file and releases all system resources allocated to this file.
	 * 
	 * @throws IOException if an error occurred during the close operation
	 */
	public void close() throws IOException {
		if (rdr != null) {
			rdr.close();
			rdr = null;
		}
	}

	/**
	 * @return the path name of this file
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the current value of the internal record counter. A <code>read</code>
	 * operation increases this counter by 1 if end of file is not yet reached.
	 * After end of file this method returns the total number of records.
	 * 
	 * @return the number of records read from to the file
	 */
	public int getRecordCount() {
		return count;
	}

	/**
	 * Opens this file for input using the specified <code>File</code>.
	 * 
	 * @param aFile the file to use as the input medium.
	 * @throws IOFoundException if the given file could not be found on the file
	 *                          system
	 */
	public void open(File aFile) throws IOException {
		if (rdr == null) {
			FileReader l_reader = new FileReader(aFile, StandardCharsets.UTF_8);
			rdr = new BufferedReader(l_reader);
			name = aFile.getPath();
			count = 0;
		}
	}

	/**
	 * Opens the file given by the specified filename for input.
	 * 
	 * @param aName the name of the file
	 * @throws FileNotFoundException if the given file could not be found on the
	 *                               file system
	 */
	public void open(String aName) throws IOException {
		File l_file = new File(aName);
		open(l_file);
	}

	/**
	 * Opens the file given by the specified directory and filename for input.
	 * 
	 * @param aDir  the directory path for the filename
	 * @param aName the name of the file
	 * @throws FileNotFoundException if the given file could not be found on the
	 *                               file system
	 */
	public void open(String aDir, String aName) throws IOException {
		File l_file = new File(aDir, aName);
		open(l_file);
	}

	/**
	 * Opens this file for input using the specified <code>URL</code>.
	 * 
	 * @param aURL the URL to use as the input medium.
	 * @throws IOException          if the given URL could not be resolved to an
	 *                              input stream
	 * @throws NullPointerException if the given URL is <code>null</code>
	 */
	public void open(URL aURL) throws IOException {
		File l_file = new File(aURL.getFile());
		open(l_file);
	}

	/**
	 * Reads the next line from the text input file.
	 * 
	 * @return aLine the text line read from the file or <code>null</code> if end of
	 *         file has been reached.
	 * @throws IOException if an error occurred during the close operation
	 */
	public String read() throws IOException {
		String l_line = rdr.readLine();

		if (l_line != null) {
			count++;
		}
		return l_line;
	}

	/**
	 * Resets the internal record counter back to zero.
	 */
	public void resetRecordCount() {
		count = 0;
	}
}
