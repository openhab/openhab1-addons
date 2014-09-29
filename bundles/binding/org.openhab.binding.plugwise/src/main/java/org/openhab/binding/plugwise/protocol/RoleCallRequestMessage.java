/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.protocol;

/**
 * Role call request message, sent by Circle+ in the network in order to "scan" for available Circles
 * Upto 64 devices can be part of a plugwise network
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class RoleCallRequestMessage extends Message {

	private int nodeID;
	
	public RoleCallRequestMessage(String MAC, int nodeID) {
		super(MAC, "");
		this.nodeID = nodeID;
		type = MessageType.DEVICE_ROLECALL_REQUEST;
	}

	@Override
	protected String payLoadToHexString() {
		return String.format("%02X", nodeID);
	}

	@Override
	protected void parsePayLoad() {		
	}

	@Override
	public String getPayLoad() {	
		return String.format("%02X", nodeID);
	}
	
	protected String sequenceNumberToHexString() {
		return "";
	}
	
}
