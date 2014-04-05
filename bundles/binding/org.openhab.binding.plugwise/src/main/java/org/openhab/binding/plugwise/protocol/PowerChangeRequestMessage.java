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
 * Message that is used to switch On or Off a Circle relais
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class PowerChangeRequestMessage extends Message {

	public PowerChangeRequestMessage(String MAC, boolean powerState) {
		super(MAC, powerState ? "01" : "00");
		type = MessageType.POWER_CHANGE_REQUEST;	
	}

	@Override
	protected String payLoadToHexString() {
		return payLoad;
	}

	@Override
	protected void parsePayLoad() {
	}

	protected String sequenceNumberToHexString() {
		return "";
	}
	
}
