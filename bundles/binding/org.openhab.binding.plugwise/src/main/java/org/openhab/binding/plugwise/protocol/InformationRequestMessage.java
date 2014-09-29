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
 * Circle Information request message
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class InformationRequestMessage extends Message {

	public InformationRequestMessage(String MAC) {
		super(MAC, "");
		type = MessageType.DEVICE_INFORMATION_REQUEST;
		this.MAC = MAC;
	}

	@Override
	protected String payLoadToHexString() {
		return "";
	}

	@Override
	protected void parsePayLoad() {
	}
	
	protected String sequenceNumberToHexString() {
		return "";
	}

}
