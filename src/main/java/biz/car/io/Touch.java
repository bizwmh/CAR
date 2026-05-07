/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.io;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;

import biz.car.SYS;
import biz.car.bundle.MSG;

/**
 * Sets the given date and time as the last modified timestamp of a file.
 *
 * @version 2.0.0 01.05.2026 14:00:40
 */
public class Touch implements BiConsumer<String, String> {

	private File myFile;

	/**
	 * Creates a default <code>Touch</code> instance.
	 * 
	 * @param aFileName the name of the file
	 */
	public Touch(String aFileName) {
		super();

		myFile = new File(aFileName);

		// Datei prüfen
		if (!myFile.exists()) {
			throw SYS.LOG.exception(MSG.RESOURCE_NOT_FOUND, aFileName);
		}
	}

	public static void main(String[] aArgList) {
		if (aArgList.length != 3) {
			System.err.println(
				"Usage: java Touch <dateiname> <tt.mm.jjjj> <hh:mm:ss>"); //$NON-NLS-1$
			System.exit(1);
		}
		Touch l_main = new Touch(aArgList[0]);

		l_main.accept(aArgList[1], aArgList[2]);
	}

	@Override
	public void accept(String aDate, String aTime) {
		DateTimeFormatter l_fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"); //$NON-NLS-1$
		try {
			// Datum + Uhrzeit parsen
			LocalDateTime l_ldt = LocalDateTime.parse(aDate + " " + aTime, l_fmt); //$NON-NLS-1$
			// In FileTime umwandeln und setzen
			ZonedDateTime l_zdt = l_ldt.atZone(ZoneId.systemDefault());
			FileTime l_ft = FileTime.from(l_zdt.toInstant());
			Path path = myFile.toPath();
			Files.setLastModifiedTime(path, l_ft);
		} catch (Exception anEx) {
			throw SYS.LOG.exception(anEx);
		}
	}
}
