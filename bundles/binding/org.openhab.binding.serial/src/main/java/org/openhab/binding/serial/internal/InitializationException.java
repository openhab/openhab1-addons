package org.openhab.binding.serial.internal;

public class InitializationException extends Exception {

	private static final long serialVersionUID = -5106059856757667266L;

	public InitializationException(String msg) {
		super(msg);
	}

	public InitializationException(Throwable cause) {
		super(cause);
	}

	public InitializationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
