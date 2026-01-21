/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Provides directory related functions.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class XDirectory implements FSObject {

	private File myFile;

	/**
	 * Creates a default <code>XDirectory</code> instance.
	 * 
	 * @param aFile the reference to the file system folder
	 */
	public XDirectory(File aFile) {
		super();

		myFile = aFile;
	}

	/**
	 * Creates a default <code>XDirectory</code> instance.
	 * 
	 * @param aPath the path to the file system folder
	 */
	public XDirectory(String aPath) {
		super();

		myFile = new File(aPath);
	}

	/**
	 * Creates a list of all files contained in this directory and all sub
	 * directories.
	 * 
	 * @return the list of all non-hidden files
	 */
	public List<File> allFiles() {
		List<File> l_ret = allFiles(NotHidden);

		return l_ret;
	}

	/**
	 * Creates a filtered selection of files contained in this directory and all sub
	 * directories.
	 * 
	 * @param aFilter the filter to be applied for the selection
	 * @return the list of files
	 */
	public List<File> allFiles(Predicate<File> aFilter) {
		List<File> l_ret = new ArrayList<>();
		File[] l_flist = get().listFiles();

		if (l_flist != null) {
			Arrays.asList(l_flist)
				.forEach(file -> {
					if (file.isFile()) {
						if (aFilter.test(file)) {
							l_ret.add(file);
						}
					} else if (file.isDirectory()) {
						XDirectory l_dir = new XDirectory(file);

						l_ret.addAll(l_dir.allFiles(aFilter));
					}
				});
		}
		return l_ret;
	}

	/**
	 * Creates a list of all folders contained in this directory and all sub
	 * directories.
	 * 
	 * @return the list of all not hidden folders
	 */
	public List<XDirectory> allFolders() {
		List<XDirectory> l_ret = allFolders(NotHidden);

		return l_ret;
	}

	/**
	 * Creates a filtered selection of folders contained in this directory and all
	 * sub directories.
	 * 
	 * @param aFilter the filter to be applied for the selection
	 * @return the list of folders
	 */
	public List<XDirectory> allFolders(Predicate<File> aFilter) {
		List<XDirectory> l_ret = new ArrayList<XDirectory>();
		List<XDirectory> l_list = folderList(aFilter);

		l_ret.addAll(l_list);
		l_list.forEach(dir -> l_ret.addAll(dir.allFolders(aFilter)));

		return l_ret;
	}

	/**
	 * @return a list of non-hidden files contained in this directory
	 */
	public List<File> fileList() {
		return fileList(NotHidden);
	}

	/**
	 * Creates a filtered selection of files contained in this directory.
	 * 
	 * @param aFilter the filter to be applied for the selection
	 * @return the list of files
	 */
	public List<File> fileList(Predicate<File> aFilter) {
		List<File> l_ret = new ArrayList<>();
		Predicate<File> l_filter = IsFile.and(aFilter);
		File[] l_flist = get().listFiles();

		if (l_flist != null) {
			List<File> l_list = Arrays.asList(l_flist).stream()
				.filter(l_filter)
				.collect(Collectors.toList());
			l_ret.addAll(l_list);
		}
		return l_ret;
	}

	/**
	 * @return a list of non-hidden sub folders
	 */
	public List<XDirectory> folderList() {
		return folderList(NotHidden);
	}

	/**
	 * Creates a filtered selection of folders contained in this directory.
	 * 
	 * @param aFilter the filter to be applied for the selection
	 * @return the list of folders
	 */
	public List<XDirectory> folderList(Predicate<File> aFilter) {
		List<XDirectory> l_ret = new ArrayList<>();
		Predicate<File> l_filter = IsDirectory.and(aFilter);
		File[] l_flist = get().listFiles();

		if (l_flist != null) {
			List<XDirectory> l_list = Arrays.asList(l_flist).stream()
				.filter(l_filter)
				.map(file -> new XDirectory(file))
				.collect(Collectors.toList());
			l_ret.addAll(l_list);
		}
		return l_ret;
	}

	@Override
	public File get() {
		return myFile;
	};

	/**
	 * Performs a file tree walk via the invited file visitor.
	 * 
	 * @param aVisitor the file visitor to be invited
	 * @throws IOException if an I/O error is thrown by a visitor method
	 */
	public void invite(FileVisitor<Path> aVisitor) throws IOException {
		Files.walkFileTree(get().toPath(), aVisitor);
	}

	@Override
	public String toString() {
		return myFile.getPath();
	}
}
