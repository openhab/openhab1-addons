package org.openhab.binding.withings.internal.api;

public class WithingsConnectionException extends Exception {

	private static final long serialVersionUID = -5173894547371219059L;

	public WithingsConnectionException(String message) {
		super(message);
	}

	public WithingsConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

}
