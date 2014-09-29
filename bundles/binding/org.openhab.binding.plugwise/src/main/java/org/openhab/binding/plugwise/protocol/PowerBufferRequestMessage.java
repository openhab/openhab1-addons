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
 * Request historical energy consumption data
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class PowerBufferRequestMessage extends Message {
	
	private int logAddress;

	public PowerBufferRequestMessage(String MAC, int logAddress) {
		super(MAC, "");
		type = MessageType.POWER_BUFFER_REQUEST;
		this.logAddress = logAddress;
	}

	@Override
	protected String payLoadToHexString() {		
		return String.format("%08X", (logAddress * 8 + 278528));
	}

	@Override
	protected void parsePayLoad() {
	}
	
	protected String sequenceNumberToHexString() {
		return "";
	}

}
