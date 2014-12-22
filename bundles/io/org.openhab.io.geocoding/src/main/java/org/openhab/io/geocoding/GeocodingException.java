package org.openhab.io.geocoding;

public class GeocodingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GeocodingException() {
		super();
	}

	public GeocodingException(String message, Throwable cause) {
		super(message, cause);
	}

	public GeocodingException(String message) {
		super(message);
	}

	public GeocodingException(Throwable cause) {
		super(cause);
	}

}
