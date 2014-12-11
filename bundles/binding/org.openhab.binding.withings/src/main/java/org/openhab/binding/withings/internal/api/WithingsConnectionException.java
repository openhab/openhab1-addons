package org.openhab.binding.withings.internal.api;

/**
 * {@link WithingsConnectionException} is thrown for every error that occurs
 * while connecting to the Withings API. The cause exception (if there is one)
 * can be retrieved by {@link WithingsConnectionException#getCause()}. For a
 * detailed explanation of Withings API error codes please look at
 * http://www.withings.com/de/api#responsecodes.
 * 
 * @see http://www.withings.com/de/api#responsecodes
 * @author Dennis Nobel
 * @since 1.5.0
 */
public class WithingsConnectionException extends Exception {

	private static final long serialVersionUID = -5173894547371219059L;

	public WithingsConnectionException(String message) {
		super(message);
	}

	public WithingsConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

}
