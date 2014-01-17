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
 * Message to be sent to a Circle when its Node Available message is "accepted"
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class NodeAvailableResponseMessage extends Message {

	boolean acceptanceCode;
	String destinationMAC;

	public NodeAvailableResponseMessage(boolean code, String destination) {
		super("", "");
		acceptanceCode = code;
		destinationMAC = destination;
		MAC="";
		type = MessageType.NODE_AVAILABLE_RESPONSE;

	}

	@Override
	protected String payLoadToHexString() {
		return String.format("%02X",acceptanceCode ? "01" : "00") + destinationMAC;
	}

	@Override
	protected void parsePayLoad() {
	}
	
	protected String sequenceNumberToHexString() {
		return "";
	}

}
