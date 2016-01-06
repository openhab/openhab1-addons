/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.primare.internal;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Interface for receiving status updates from Primare
 * 
 * @author Pauli Anttila, Veli-Pekka Juslin
 * @since 1.7.0
 */
public interface PrimareEventListener extends EventListener {

	/**
	 * Receive status update from Primare device
	 * 
	 * @param data
	 *            Received data.
	 */
	void statusUpdateReceived(EventObject event, String deviceId, byte[] data);
	
}
