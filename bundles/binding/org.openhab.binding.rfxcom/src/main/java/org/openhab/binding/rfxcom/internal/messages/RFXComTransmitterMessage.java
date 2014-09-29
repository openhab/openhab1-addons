/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import java.util.List;

import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;

/**
 * RFXCOM data class for transmitter message.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public class RFXComTransmitterMessage extends RFXComBaseMessage {

	public enum SubType {
		TRANSMITTER_MESSAGE(1),

		UNKNOWN(255);

		private final int subType;

		SubType(int subType) {
			this.subType = subType;
		}

		SubType(byte subType) {
			this.subType = subType;
		}

		public byte toByte() {
			return (byte) subType;
		}
	}

	public enum Response {
		ACK(0), 					// ACK, transmit OK
		ACK_DELAYED(1), 			// ACK, but transmit started after 3 seconds delay
									// anyway with RF receive data
		NAK(2), 					// NAK, transmitter did not lock on the requested transmit
									// frequency
		NAK_INVALID_AC_ADDRESS(3), // NAK, AC address zero in id1-id4 not
									// allowed

		UNKNOWN(255);

		private final int response;

		Response(int response) {
			this.response = response;
		}

		Response(byte response) {
			this.response = response;
		}

		public byte toByte() {
			return (byte) response;
		}
	}

	public SubType subType = SubType.TRANSMITTER_MESSAGE;
	public Response response = Response.ACK;

	public RFXComTransmitterMessage() {
		packetType = PacketType.TRANSMITTER_MESSAGE;
	}

	public RFXComTransmitterMessage(byte[] data) {
		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Response = " + response;

		return str;
	}

	@Override
	public void encodeMessage(byte[] data) {

		super.encodeMessage(data);

		try {
			subType = SubType.values()[super.subType];
		} catch (Exception e) {
			subType = SubType.UNKNOWN;
		}
		
		try {
			response = Response.values()[data[4]];
		} catch (Exception e) {
			response = Response.UNKNOWN;
		}
	}

	@Override
	public byte[] decodeMessage() {

		byte[] data = new byte[5];

		data[0] = 0x04;
		data[1] = RFXComBaseMessage.PacketType.TRANSMITTER_MESSAGE.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		data[4] = response.toByte();

		return data;
	}

	@Override
	public State convertToState(RFXComValueSelector valueSelector)
			throws RFXComException {
		
		throw new RFXComException("Not supported");
	}

	@Override
	public void convertFromState(RFXComValueSelector valueSelector, String id,
			Object subType, Type type, byte seqNumber) throws RFXComException {
		
		throw new RFXComException("Not supported");
	}

	@Override
	public Object convertSubType(String subType) throws RFXComException {
		
		for (SubType s : SubType.values()) {
			if (s.toString().equals(subType)) {
				return s;
			}
		}
		
		throw new RFXComException("Unknown sub type " + subType);
	}
	
	@Override
	public List<RFXComValueSelector> getSupportedValueSelectors() throws RFXComException {
		return null;
	}

}
