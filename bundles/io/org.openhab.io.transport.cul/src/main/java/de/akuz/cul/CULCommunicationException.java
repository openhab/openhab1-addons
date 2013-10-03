package de.akuz.cul;

/**
 * An exception which is thrown if communication with a culfw based device
 * causes an error.
 * 
 * @author Till Klocke
 * 
 */
public class CULCommunicationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1861588016496497682L;

	public CULCommunicationException() {
		super();
	}

	public CULCommunicationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CULCommunicationException(String arg0) {
		super(arg0);
	}

	public CULCommunicationException(Throwable arg0) {
		super(arg0);
	}

}
