/**
 * 
 */
package org.openhab.binding.hue.internal;

/**
 * @author Gernot Eger
 *
 */
@SuppressWarnings("serial")
public class HueSettingsParseException extends Exception {

	/**
	 * 
	 */
	public HueSettingsParseException() {
	}

	/**
	 * @param message
	 */
	public HueSettingsParseException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public HueSettingsParseException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public HueSettingsParseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public HueSettingsParseException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
