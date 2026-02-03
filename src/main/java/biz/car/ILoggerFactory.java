/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car;

import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import biz.car.config.ACS;
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
public class ILoggerFactory {

	public static ILogger getLogger(Config aConfig) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		ILoggerFactory l_lf = new ILoggerFactory();

		// load logger configuration
		ACS.initialize(l_lf, aConfig);

		// 1. Encoder für das Format erstellen
		PatternLayoutEncoder ple = new PatternLayoutEncoder();
//      ple.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
		ple.setPattern(l_lf.pattern);
		ple.setContext(lc);
		ple.start();

		// 2. FileAppender erstellen
		FileAppender<ILoggingEvent> l_fileAppender = new FileAppender<>();
		l_fileAppender.setContext(lc);
		l_fileAppender.setName(l_lf.appenderName);
		l_fileAppender.setFile(l_lf.fileName);
		l_fileAppender.setEncoder(ple);
		l_fileAppender.start();

		// 3. Logger holen und Appender zuweisen
		Logger l_logger = (Logger) LoggerFactory.getLogger(l_lf.loggerName);
		l_logger.addAppender(l_fileAppender);

		// Festlegen, ob Logs an den Root-Logger (Konsole) weitergereicht werden
		l_logger.setAdditive(l_lf.additive);

		return new ILogger(l_logger);
	}

	public boolean additive;
	public String appenderName;
	public String fileName;
	public String loggerName;
	public String pattern;

	/**
	 * Creates a default <code>ILoggerFactory</code> instance.
	 */
	private ILoggerFactory() {
		super();
	}
}
