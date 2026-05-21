/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car.csv;

import java.io.IOException;
import java.util.function.UnaryOperator;

import com.typesafe.config.Config;

import biz.car.XRunnable;
import biz.car.XRuntimeException;
import biz.car.config.CConfig;

/**
 * Processes the records of a CSV file.<br>
 * This class loops over the input records and delegates the processing of the
 * individual records to a <code>CSVHandler</code>.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class CSVFeeder extends CConfig implements XRunnable {

	private UnaryOperator<CSVRecord> mapper = r -> r;
	private CSVHandler myHandler;

	/**
	 * Creates a default <code>CSVFeeder</code> instance.
	 */
	public CSVFeeder() {
		super();

		myHandler = new CSVOutput();
	}

	/**
	 * Creates a default <code>CSVFeeder</code> instance.
	 * 
	 * @param aHandler the associated consumer of CSV records
	 */
	public CSVFeeder(CSVHandler aHandler) {
		super();

		myHandler = aHandler;
	}

	@Override
	public void accept(Config aConfig) {
		super.accept(aConfig);

		myHandler.accept(aConfig);

		if (hasProperty(KV)) {
			mapper = CSVMapper.parseFile(getString(KV));
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
		try (CSVReader l_rdr = openReader()) {
			myHandler.onInit();

			CSVRecord l_rec = l_rdr.readRecord();

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
	 * Optionally maps the given record via the {@link CSVMapper} (if a {@code KV}
	 * file was configured) and delegates it to the associated {@link CSVHandler}.
	 *
	 * @param aRecord the record to process
	 */
	protected void feed(CSVRecord aRecord) {
		CSVRecord l_rec = mapper.apply(aRecord);

		myHandler.handle(l_rec);
	}

	private CSVReader openReader() throws IOException {
		String l_path = getString(INPUT);

		if (hasConfig(l_path)) {
			l_path = config().getConfig(l_path).getString(PATH);
		}
		CSVReader l_ret = new CSVReader();

		l_ret.open(l_path);

		return l_ret;
	}
}
