/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved.
 * -------------------------------------------------------------------------- */

package biz.car.excel;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.UnaryOperator;

import com.typesafe.config.Config;

import biz.car.XRunnable;
import biz.car.XRuntimeException;
import biz.car.config.CConfig;
import biz.car.util.ClassUtil;
import biz.car.util.KeyValuePairs;

/**
 * Processes the records of a sheet in an Excel workbook.<br>
 * This class loops over the input records and delegates the processing of the
 * individual records to an <code>ExcelHandler</code>.
 *
 * @version 1.0.0 18.07.2026
 */
public class ExcelFeeder extends CConfig implements XRunnable {

	private static final String SHEET = "sheet"; //$NON-NLS-1$

	private UnaryOperator<ExcelRecord> mapper = r -> r;
	private ExcelHandler myHandler;

	/**
	 * Creates a default <code>ExcelFeeder</code> instance.
	 */
	public ExcelFeeder() {
		super();

		myHandler = new ExcelPrinter();
	}

	/**
	 * Creates a default <code>ExcelFeeder</code> instance.
	 *
	 * @param aHandler the associated consumer of Excel records
	 */
	public ExcelFeeder(ExcelHandler aHandler) {
		super();

		myHandler = aHandler;
	}

	@Override
	public void accept(Config aConfig) {
		super.accept(aConfig);

		myHandler.accept(aConfig);

		if (hasProperty(KV)) {
			String l_kv = getString(KV);
			Optional<URL> l_url = ClassUtil.getResource(l_kv);
			KeyValuePairs l_kvPairs = null;

			if (l_url.isPresent()) {
				l_kvPairs = KeyValuePairs.parseResource(l_kv);
				mapper = new ExcelMapper(l_kvPairs);
			} else {
				Class<?> l_class = getClass();
				l_url = ClassUtil.getResource(l_class, l_kv);

				if (l_url.isPresent()) {
					l_kvPairs = KeyValuePairs.parseResource(l_class, l_kv);
					mapper = new ExcelMapper(l_kvPairs);
				} else {
					if (Paths.get(l_kv).toFile().isFile()) {
						mapper = ExcelMapper.parseFile(l_kv);
					}
				}
			}
		}
	}

	@Override
	public void dispose() {
		if (myHandler != null) {
			myHandler.dispose();

			myHandler = null;
		}
	}

	@Override
	public void exec() {
		try (ExcelReader l_rdr = openReader()) {
			myHandler.onInit();

			ExcelRecord l_rec = l_rdr.readRecord();

			while (l_rec != null) {
				feed(l_rec);

				l_rec = l_rdr.readRecord();
			}
			myHandler.onExit();
		} catch (IOException anEx) {
			myHandler.onError(anEx);
			rethrow(new XRuntimeException(anEx));
		} catch (XRuntimeException anEx) {
			myHandler.onError(anEx);
			rethrow(anEx);
		}
	}

	/**
	 * Optionally maps the given record via the {@link ExcelMapper} (if a
	 * {@code KV} file was configured) and delegates it to the associated
	 * {@link ExcelHandler}.
	 *
	 * @param aRecord the record to process
	 */
	protected void feed(ExcelRecord aRecord) {
		ExcelRecord l_rec = mapper.apply(aRecord);

		myHandler.handle(l_rec);
	}

	/**
	 * Opens the configured workbook file and selects the configured sheet (via
	 * the {@code sheet} property, interpreted as a zero-based index if numeric,
	 * otherwise as a sheet name; defaults to the first sheet).
	 *
	 * @return the opened reader
	 * @throws IOException if the workbook could not be opened
	 */
	private ExcelReader openReader() throws IOException {
		String l_path = getString(INPUT);

		if (hasConfig(l_path)) {
			l_path = config().getConfig(l_path).getString(PATH);
		}
		ExcelReader l_ret = new ExcelReader();

		if (hasProperty(SHEET)) {
			String l_sheet = getString(SHEET);

			try {
				l_ret.setSheetIndex(Integer.parseInt(l_sheet));
			} catch (NumberFormatException anEx) {
				l_ret.setSheetName(l_sheet);
			}
		}
		l_ret.open(l_path);

		return l_ret;
	}
}
