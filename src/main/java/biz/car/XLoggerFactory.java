/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car;

import java.io.File;

import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import biz.car.bundle.ILogger;
import biz.car.config.ACS;
import biz.car.config.XConfig;
import biz.car.io.PrefixedFile;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;

/**
 * Factory for creating an <code>ILogger</code> instance based on a logger
 * configuration.
 *
 * @version 2.0.0 03.02.2026 07:18:21
 */
public interface XLoggerFactory {

	/**
	 * Creates a XLogger instance based on the given configruation.
	 * 
	 * @param aConfig the logger configuration
	 * @return the <code>XLogger</code> instance
	 */
	public static XLogger getLogger(Config aConfig) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

		// load logger configuration
		XConfig l_xc = () -> aConfig;
		String l_pattern = l_xc.getString(CAR.PATTERN, ACS.APP.getString(CAR.PATTERN));
		String l_appenderName = l_xc.getString(CAR.APPENDER, ILogger.class.getName());
		String l_fileName = l_xc.getString(CAR.PATH, defaultFileName());
		String l_loggerName = l_xc.getString(CAR.LOGGER, ILogger.class.getName());
		boolean l_additive = l_xc.getBool(CAR.ADDITIVE, false);

		// 1. Create Encoder for the Pattern Layout
		PatternLayoutEncoder ple = new PatternLayoutEncoder();
		ple.setPattern(l_pattern);
		ple.setContext(lc);
		ple.start();

		// 2. Create the FileAppender
		FileAppender<ILoggingEvent> l_fileAppender = new FileAppender<>();
		l_fileAppender.setContext(lc);
		l_fileAppender.setName(l_appenderName);
		l_fileAppender.setFile(l_fileName);
		l_fileAppender.setEncoder(ple);
		l_fileAppender.start();

		// 3. Assign Appender to Logger
		Logger l_logger = (Logger) LoggerFactory.getLogger(l_loggerName);
		l_logger.addAppender(l_fileAppender);

		// Determine, if log entries are passed to the root logger
		l_logger.setAdditive(l_additive);

		return new ILogger(l_logger);
	}

	/**
	 * Creates a XLogger instance based on the given name.
	 * 
//	 * @param aName the name of the logger
	 * @return the <code>XLogger</code> instance
	 */
	static XLogger getLogger(String aName) {
		return new ILogger(aName);
	}

	private static String defaultFileName() {
		File l_file = new File("log", ILogger.class.getSimpleName() + ".log"); //$NON-NLS-1$//$NON-NLS-2$
		PrefixedFile l_pf = () -> l_file;
		File l_prefixed = l_pf.prefix();
		String l_ret = l_prefixed.getPath();

		return l_ret;
	}
}
