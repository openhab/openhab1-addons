/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

/**
 * RFXCOM data class for control message.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public class RFXComControlMessage extends RFXComBaseMessage {

	public RFXComControlMessage() {

	}

	public RFXComControlMessage(byte[] data) {
		encodeMessage(data);
	}

	@Override
	public byte[] decodeMessage() {
		return null;
	}

	@Override
	public void encodeMessage(byte[] data) {
		super.encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();

		return str;
	}
}
