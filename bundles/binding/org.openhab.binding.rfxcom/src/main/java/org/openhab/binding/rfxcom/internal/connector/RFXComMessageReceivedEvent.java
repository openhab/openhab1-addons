/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.connector;

import java.util.EventObject;

/**
 * The listener interface for receiving data from RFXCOM controller.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public class RFXComMessageReceivedEvent extends EventObject {

	private static final long serialVersionUID = 3821740012020068392L;

	public RFXComMessageReceivedEvent(Object source) {
		super(source);
	}

	/**
	 * Invoked when data message is received from RFXCOM controller.
	 * 
	 * @param packet
	 *            Data from controller.

	 */
	public void MessageReceivedEvent(byte[] packet) {
	}

}
