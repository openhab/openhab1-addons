package org.openhab.binding.pixtend.internal;

/**
 * Exception to be thrown if the command is not compatible with the type
 * required by the port
 * 
 * @author Michael Kolb
 * @since 1.7.1
 *
 */
public class IncompatibleCommandException extends Exception {

	public IncompatibleCommandException(String string) {
		super(string);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4113267045266176384L;

}
