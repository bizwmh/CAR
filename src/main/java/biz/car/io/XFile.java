/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.io;

import java.io.File;

/**
 * A <code>XFile</code> wraps a file of the local file system that is not a
 * directory.
 *
 * @version 1.0.0 16.10.2024 15:55:35
 */
public class XFile implements FSObject {

	private File myFile;

	/**
	 * Creates a default <code>XFile</code> instance.
	 * 
	 * @param aFile
	 *            the file to be associated to this <code>XFile</code> instance.
	 */
	public XFile(File aFile) {
		super();
		
		myFile = aFile;
	}

	/**
	 * Creates a default <code>XFile</code> instance.
	 * 
	 * @param aPath
	 *            the path to the file to be associated to this <code>XFile</code> instance.
	 */
	public XFile(String aPath) {
		super();
		
		myFile = new File(aPath);
	}

	@Override
	public File get() {
		return myFile;
	}
}
