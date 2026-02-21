/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car;

import java.io.File;

import org.slf4j.LoggerFactory;

import biz.car.bundle.CLogger;
import biz.car.io.PrefixedFile;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

/**
 * Factory for creating an <code>CLogger</code> instance based on a logger
 * configuration.
 *
 * @version 2.0.0 03.02.2026 07:18:21
 */
public interface XLoggerFactory {

	/**
	 * Creates a XLogger instance based on the given configruation.
	 * 
	 * @param aDTO the logger configuration
	 * @return the <code>XLogger</code> instance
	 */
	public static XLogger getLogger(LoggerDTO aDTO) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

		// 1. Create Encoder for the Pattern Layout
		PatternLayoutEncoder ple = new PatternLayoutEncoder();
		ple.setPattern(aDTO.pattern);
		ple.setContext(lc);
		ple.start();
		// Create the path to the log file
		File l_file = new File(aDTO.path, aDTO.file);
		
		if (aDTO.timestamp) {
			l_file = prefixedPath(l_file);
		}
		// 2. Create the FileAppender
		FileAppender<ILoggingEvent> l_fileAppender = new FileAppender<>();
		l_fileAppender.setContext(lc);
		l_fileAppender.setName(aDTO.appender);
		l_fileAppender.setFile(l_file.getPath());
		l_fileAppender.setEncoder(ple);
		l_fileAppender.start();

		// 3. Assign Appender to Logger
		Logger l_logger = (Logger) LoggerFactory.getLogger(aDTO.logger);
		l_logger.addAppender(l_fileAppender);

		// Determine, if log entries are passed to the root logger
		l_logger.setAdditive(aDTO.additive);

		return new CLogger(l_logger);
	}

	/**
	 * Creates a XLogger instance based on the given name.
	 * 
	 * @param aName the name of the logger
	 * @return the <code>XLogger</code> instance
	 */
	static XLogger getLogger(String aName) {
		return new CLogger(aName);
	}

	private static File prefixedPath(File aFile) {
		PrefixedFile l_pf = () -> aFile;
		File l_ret = l_pf.prefix();

		return l_ret;
	}
}
