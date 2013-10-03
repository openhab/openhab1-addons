package de.akuz.cul.internal;

import de.akuz.cul.CULDeviceException;

/**
 * Internal interface for the CULManager. CULHandler should always implement the
 * external and internal interface.
 * 
 * @author Till Klocke
 * 
 */
public interface CULHandlerInternal {

	public void open() throws CULDeviceException;

	public void close();

	public boolean hasListeners();

}
