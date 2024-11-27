/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.car;

import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

/**
 * A simple facade interface to SLF4J logging providing default implementations
 * for some logging functions as well as some new features.<br>
 * Any class implementing this interface can then use these SLF4J logging
 * functions via class methods.
 *
 * @version 1.0.0 11.10.2024 05:59:21
 */
@FunctionalInterface
public interface XLogger {

	/**
	 * Performs an array substitution for a message pattern.
	 * 
	 * @param aMessage  the pattern for the detail message
	 * @param anArgList the message variables
	 * @return the formatted message
	 */
	static String format(String aMessage, Object... anArgList) {
		return MessageFormatter.arrayFormat(aMessage, anArgList).getMessage();
	}

	/**
	 * Writes a debug message to the application log.
	 * 
	 * @param aMessage  the detail message
	 * @param anArgList the message variables
	 */
	default void debug(String aMessage, Object... anArgList) {
		logger().debug(aMessage, anArgList);
	}

	/**
	 * Writes an error message to the log.
	 * 
	 * @param aMessage  the detail message
	 * @param anArgList the message variables
	 */
	default void error(String aMessage, Object... anArgList) {
		logger().error(aMessage, anArgList);
	}

	/**
	 * Logs the error message from the given exception.
	 * 
	 * @param aCause the exception cause
	 */
	default void error(Throwable aCause) {
		XRuntimeException l_ret = new XRuntimeException(aCause);

		logger().error(l_ret.traceMessage());

	}

	/**
	 * Writes an error message to the application log.
	 * 
	 * @param aMessage  the detail message
	 * @param anArgList the message variables
	 * @return the generated runtime exception with the message
	 */
	default XRuntimeException exception(String aMessage, Object... anArgList) {
		XRuntimeException l_ret = new XRuntimeException(aMessage, anArgList);

		logger().error(aMessage, anArgList);
		return l_ret;
	}

	/**
	 * Logs the exception with the accompanying error message.
	 * 
	 * @param aCause the exception cause
	 * @return the generated runtime exception with the message
	 */
	default XRuntimeException exception(Throwable aCause) {
		XRuntimeException l_ret = new XRuntimeException(aCause);

		error(l_ret);
		
		return l_ret;
	}

	/**
	 * Logs the exception with the accompanying error message.
	 * 
	 * @param aCause    the exception cause
	 * @param aMessage  the detail message
	 * @param anArgList the message variables
	 * @return the generated runtime exception with the message
	 */
	default XRuntimeException exception(Throwable aCause, String aMessage,
		Object... anArgList) {
		XRuntimeException l_ret = new XRuntimeException(aCause, aMessage, anArgList);

		logger().error(l_ret.getMessage(), aCause);
		
		return l_ret;
	}

	/**
	 * Writes an info message to the application log.
	 * 
	 * @param aMessage  the detail message
	 * @param anArgList the message variables
	 */
	default void info(String aMessage, Object... anArgList) {
		logger().info(aMessage, anArgList);
	}

	/**
	 * Supplier function to get the logger.
	 * 
	 * @return the logger instance associated to the implementing class.
	 */
	Logger logger();

	/**
	 * Writes a trace message to the application log.
	 * 
	 * @param aMessage  the detail message
	 * @param anArgList the message variables
	 */
	default void trace(String aMessage, Object... anArgList) {
		logger().trace(aMessage, anArgList);
	}

	/**
	 * Writes a warning message to the application log.
	 * 
	 * @param aMessage  the detail message
	 * @param anArgList the message variables
	 */
	default void warn(String aMessage, Object... anArgList) {
		logger().warn(aMessage, anArgList);
	}
}
