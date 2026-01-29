/* --------------------------------------------------------------------------
 * Project: CAR - Common Application Runtime
 * --------------------------------------------------------------------------
 * Use of this software is subject to license terms. All Rights Reserved. 
 * -------------------------------------------------------------------------- */

package biz.wmh.car;

/**
 * An unchecked exception indicating an error during an application
 * operation.<br>
 * As a convention the class throwing the exception should also write the error
 * message to the application log.
 *
 * @version 2.0.0 08.01.2026 08:32:08
 */
public class XRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a default <code>XException</code> instance without detail message.
	 */
	public XRuntimeException() {
		super();
	}

	/**
	 * Constructs a new exception with the given detail message.
	 * 
	 * @param aMessage  the detail message of the exception
	 * @param anArgList the message variables
	 */
	public XRuntimeException(String aMessage, Object... anArgList) {
		super(XLogger.format(aMessage, anArgList));
	}

	/**
	 * Constructs a new exception with the given cause.
	 * 
	 * @param aCause the cause of the exception
	 */
	public XRuntimeException(Throwable aCause) {
		super(aCause);
	}

	/**
	 * Constructs a new exception with the given cause and detail message.
	 * 
	 * @param aCause    the cause of the exception
	 * @param aMessage  the detail message of the exception
	 * @param anArgList the message variables
	 */
	public XRuntimeException(Throwable aCause, String aMessage,
			Object... anArgList) {
		super(XLogger.format(aMessage, anArgList), aCause);
	}

	/**
	 * This implementation returns a non-null message text.<br>
	 * If the message of this exception is <code>null</code> then the message from a
	 * cause is looked up. If no message can be found at all the simple class name
	 * of the exception is returned.
	 * 
	 * @return the message text for this exception
	 */
	@Override
	public String getMessage() {
		Throwable l_cause = this;
		String l_ret = super.getMessage();

		while (l_cause != null && l_ret == null) {
			l_cause = l_cause.getCause();
			l_ret = l_cause.getMessage();
		}
		if (l_ret == null) {
			l_ret = getClass().getSimpleName();
		}
		return l_ret;
	}

	/**
	 * Builds an error message from the given exception.<br>
	 * If the exception contains a cause then the message will be built from the
	 * cause. The message will be prefixed with class and method name from the stack
	 * trace.
	 * 
	 * @return the formatted message
	 */
	public String traceMessage() {
		Throwable l_cause = getCause();

		if (l_cause == null) {
			l_cause = this;
		}
		return traceMessage(l_cause);
	}

	private String traceMessage(Throwable aCause) {
		StackTraceElement l_ste = aCause.getStackTrace()[0];
		String l_class = l_ste.getClassName();
		String l_method = l_ste.getMethodName();
		String l_prefix = l_class + "::" + l_method; //$NON-NLS-1$
		String l_msg = getMessage();

		return XLogger.format("{} >>> {}", l_prefix, l_msg); //$NON-NLS-1$
	}
}
