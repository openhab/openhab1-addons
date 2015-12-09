/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

public class RFXComUVMessage  extends RFXComBaseMessage {

	/*	UV packet layout (length 9) - UV - UVN128,UVR128,UV138,UVN800,TFA

        packetlength = 0
        packettype = 1
        subtype = 2
        seqnbr = 3
        id1 = 4
        id2 = 5
        uv = 6
        temperatureh = 7    'bits 6-0
        tempsign = 7        'bit 7
        temperaturel = 8
        battery_level = 9	//bits 3-0
        signal_level = 9	//bits 7-4
	 */

	public enum SubType {
		UV1(1), // UVN128,UVR128,UV138
		UV2(2), // UVN800
		UV3(3), // TFA

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

	private final static List<RFXComValueSelector> supportedValueSelectors = Arrays
			.asList(RFXComValueSelector.RAW_DATA,
					RFXComValueSelector.SIGNAL_LEVEL,
					RFXComValueSelector.BATTERY_LEVEL,
					RFXComValueSelector.UV,
					RFXComValueSelector.TEMPERATURE);

	public SubType subType = SubType.UV1;
	public int sensorId = 0;
	public int uv = 0;
	public double temperature = 0;
	public byte signalLevel = 0;
	public byte batteryLevel = 0;

	public RFXComUVMessage() {
		packetType = PacketType.UV;
	}

	public RFXComUVMessage(byte[] data) {
		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + sensorId;
		str += "\n - UV = " + uv;
		str += "\n - Temperature = " + temperature;
		str += "\n - Signal level = " + signalLevel;
		str += "\n - Battery level = " + batteryLevel;

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
		
		sensorId = (data[4] & 0xFF) << 8 | (data[5] & 0xFF);
		
		uv = (data[6] & 0xFF);
		
		if(subType == SubType.UV3) {
			temperature = (short) ((data[7] & 0x7F) << 8 | (data[8] & 0xFF)) * 0.1;
			if ((data[7] & 0x80) != 0)
				temperature = -temperature;
		}
		
		signalLevel = (byte) ((data[9] & 0xF0) >> 4);
		batteryLevel = (byte) (data[9] & 0x0F);
	}

	@Override
	public byte[] decodeMessage() {
		byte[] data = new byte[9];

		data[0] = 0x09;
		data[1] = RFXComBaseMessage.PacketType.UV.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		
		data[4] = (byte) ((sensorId & 0xFF00) >> 8);
		data[5] = (byte) (sensorId & 0x00FF);
		data[6] = (byte) (uv & 0xFF);;

		short temp = (short) Math.abs(temperature * 10);
		data[7] = (byte) ((temp >> 8) & 0xFF);
		data[8] = (byte) (temp & 0xFF);
		if (temperature < 0)
			data[7] |= 0x80;

		data[9] = (byte) (((signalLevel & 0x0F) << 4) | (batteryLevel & 0x0F));

		return data;
	}
	
	@Override
	public String generateDeviceId() {
		 return String.valueOf(sensorId);
	}

	@Override
	public State convertToState(RFXComValueSelector valueSelector)
			throws RFXComException {
		
		org.openhab.core.types.State state = UnDefType.UNDEF;

		if (valueSelector.getItemClass() == NumberItem.class) {

			if (valueSelector == RFXComValueSelector.SIGNAL_LEVEL) {

				state = new DecimalType(signalLevel);

			} else if (valueSelector == RFXComValueSelector.BATTERY_LEVEL) {

				state = new DecimalType(batteryLevel);

			} else if (valueSelector == RFXComValueSelector.UV) {

				state = new DecimalType(uv);

			} else if (valueSelector == RFXComValueSelector.TEMPERATURE) {

				state = new DecimalType(temperature);

			} else {
				
				throw new RFXComException("Can't convert " + valueSelector + " to NumberItem");
				
			}

		} else if (valueSelector.getItemClass() == StringItem.class) {

			if (valueSelector == RFXComValueSelector.RAW_DATA) {

				state = new StringType(
						DatatypeConverter.printHexBinary(rawMessage));
			} else {
				throw new RFXComException("Can't convert "
						+ valueSelector + " to StringItem");
			}
		} else {

			throw new RFXComException("Can't convert " + valueSelector
					+ " to " + valueSelector.getItemClass());

		}

		return state;
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
		return supportedValueSelectors;
	}

}
