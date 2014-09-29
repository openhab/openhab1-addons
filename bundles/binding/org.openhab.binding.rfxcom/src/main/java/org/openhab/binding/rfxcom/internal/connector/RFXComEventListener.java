/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.connector;

import java.util.EventListener;
import java.util.EventObject;

/**
 * This interface defines interface to receive data from RFXCOM controller.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public interface RFXComEventListener extends EventListener {

	/**
	 * Procedure for receive raw data from RFXCOM controller.
	 * 
	 * @param data
	 *            Received raw data.
	 */
	void packetReceived(EventObject event, byte[] data);

}
