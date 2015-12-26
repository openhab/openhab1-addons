package org.openhab.binding.myq.internal;

import java.io.IOException;

/**
 * Throw if the data we are parsing in not what we are expecting for input.
 * 
 * @author Dan Cunningham
 *
 */
public class InvalidDataException extends IOException {

	private static final long serialVersionUID = 1L;

	public InvalidDataException(String message) {
		super(message);
	}
}
