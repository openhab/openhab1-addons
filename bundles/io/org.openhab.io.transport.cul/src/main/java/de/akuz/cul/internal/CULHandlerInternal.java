package de.akuz.cul.internal;

import de.akuz.cul.CULCommunicationException;
import de.akuz.cul.CULDeviceException;

/**
 * Internal interface for the CULManager. CULHandler should always implement the
 * external and internal interface.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public interface CULHandlerInternal {

	public void open() throws CULDeviceException;

	public void close();

	public boolean hasListeners();

	public void sendWithoutCheck(String message) throws CULCommunicationException;

}
