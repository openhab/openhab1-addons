package org.openhab.binding.lightwaverf.internal.exception;

public class LightwaveRfMessageException extends Exception {

	private static final long serialVersionUID = -2131620053984993990L;
	
	public LightwaveRfMessageException(String message) {
		super(message);
	}
	
	
	public LightwaveRfMessageException(String message, Throwable cause) {
		super(message, cause);
	}

}
