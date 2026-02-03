/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.io;

import java.io.File;
import java.util.Date;
import java.util.function.Predicate;

/**
 * Represents a file store in the local file system.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
@FunctionalInterface
public interface FSObject extends PrefixedFile {

	/**
	 * Returns <code>true</code> if the file is a directory.
	 */
	static final Predicate<File> IsDirectory = file -> file.isDirectory();

	/**
	 * Returns <code>true</code> if the file is a file.
	 */
	static final Predicate<File> IsFile = file -> file.isFile();

	/**
	 * Returns <code>true</code> if the file is not hidden.
	 */
	static final Predicate<File> NotHidden = file -> !file.isHidden();

	/**
	 * @return the name of the file without extension.<br>
	 *         If this object is a directory the null-string is returned.
	 */
	default String getBaseName() {
		File l_file = get();
		String l_ret = getName();

		if (l_file.isFile()) {
			int l_dot = l_ret.lastIndexOf('.');
			l_ret = l_dot > 0 ? l_ret.substring(0, l_dot) : l_ret;
		}
		return l_ret;
	}

	/**
	 * @return the name of the file or directory denoted by this abstract pathname.
	 *         This is just the last name in the pathname's name sequence.
	 */
	default String getName() {
		return get().getName();
	}

	/**
	 * @return the name of the parent file system object.
	 */
	default String getParent() {
		return get().getParent();
	}

	/**
	 * @return the path name to this directory. The resulting string uses the
	 *         default name-separator character to separate the names in the name
	 *         sequence.
	 */
	default String getPath() {
		return get().getPath();
	}

	/**
	 * @return The length, in bytes, of the file denoted by this FS object, or
	 *         <code>0L</code> if the file does not exist or is a directory.
	 */
	default long getSize() {
		long l_ret = get().length();

		if (isDirectory()) {
			l_ret = 0;
		}
		return l_ret;
	}

	/**
	 * @return The file extension or the Null-String if the file does not have a
	 *         file extension.
	 */
	default String getType() {
		String l_ret = ""; //$NON-NLS-1$
		String l_name = getName();
		int l_ind = l_name.lastIndexOf('.');

		if (l_ind > 0) {
			if (l_ind < l_name.length() - 1) {
				l_ret = l_name.substring(l_ind + 1);
			}
		}
		return l_ret;
	}

	/**
	 * @return <code>true</code> if the underlying <code>File</code> instance exists
	 *         and is a directory.
	 */
	default boolean isDirectory() {
		return get().isDirectory();
	}

	/**
	 * @return <code>true</code> if the underlying <code>File</code> instance exists
	 *         and is a file.
	 */
	default boolean isFile() {
		return get().isFile();
	}

	/**
	 * @return <code>true</code> if the underlying <code>File</code> instance is
	 *         hidden.
	 */
	default boolean isHidden() {
		return get().isHidden();
	}

	/**
	 * @return the last modified date
	 */
	default Date lastModifiedDate() {
		Date l_ret = new Date(get().lastModified());

		return l_ret;
	}
}
