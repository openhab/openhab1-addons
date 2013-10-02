package de.akuz.cul.internal;

import de.akuz.cul.CULDeviceException;

public interface CULHandlerInternal {

	public void open() throws CULDeviceException;

	public void close();

	public boolean hasListeners();

}
