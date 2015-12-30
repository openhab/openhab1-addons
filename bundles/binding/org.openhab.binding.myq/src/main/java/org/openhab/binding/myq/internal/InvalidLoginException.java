package org.openhab.binding.myq.internal;

/**
 * Exception type used when a login attempt fails against the MyQ API.
 * 
 * @author Dan Cunningham
 *
 */
public class InvalidLoginException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidLoginException(String message) {
		super(message);
	}
}
