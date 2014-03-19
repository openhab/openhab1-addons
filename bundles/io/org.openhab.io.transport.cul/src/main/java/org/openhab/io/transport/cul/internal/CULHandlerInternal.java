/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul.internal;

import org.openhab.io.transport.cul.CULCommunicationException;
import org.openhab.io.transport.cul.CULDeviceException;

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
