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

public class RFXComCurrentMessage  extends RFXComBaseMessage {

	/*	Current packet layout (length 13) - ELEC1 - OWL CM113, Electrisave, cent-a-meter

        packetlength = 0
        packettype = 1
        subtype = 2
        seqnbr = 3
        id_1 = 4
        id_2 = 5
        count = 6
        channel1_1 = 7
        channel1_2 = 8
        channel2_1 = 9
        channel2_2 = 10
        channel3_1 = 11
        channel3_2 = 12	
        battery_level = 13	//bits 3-0
        signal_level = 13	//bits 7-4
	 */

	public enum SubType {
		ELEC1(1),

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
					RFXComValueSelector.CHANNEL1_AMPS,
					RFXComValueSelector.CHANNEL2_AMPS,
					RFXComValueSelector.CHANNEL3_AMPS);

	public SubType subType = SubType.ELEC1;
	public int sensorId = 0;
	public byte count = 0;
	public double channel1Amps = 0;
	public double channel2Amps = 0;
	public double channel3Amps = 0;
	public byte signalLevel = 0;
	public byte batteryLevel = 0;

	public RFXComCurrentMessage() {
		packetType = PacketType.CURRENT;
	}

	public RFXComCurrentMessage(byte[] data) {
		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + sensorId;
		str += "\n - Count = " + count;
		str += "\n - Channel1 Amps = " + channel1Amps;
		str += "\n - Channel2 Amps = " + channel2Amps;
		str += "\n - Channel3 Amps = " + channel3Amps;
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
		count = data[6];
		
		//Current = Field / 10
		channel1Amps = ((data[7] & 0xFF) << 8 | (data[8] & 0xFF)) / 10.0;
		channel2Amps = ((data[9] & 0xFF) << 8 | (data[10] & 0xFF)) / 10.0;
		channel3Amps = ((data[11] & 0xFF) << 8 | (data[12] & 0xFF)) / 10.0;
		
		signalLevel = (byte) ((data[13] & 0xF0) >> 4);
		batteryLevel = (byte) (data[13] & 0x0F);
	}

	@Override
	public byte[] decodeMessage() {
		byte[] data = new byte[13];

		data[0] = 0x0D;
		data[1] = RFXComBaseMessage.PacketType.CURRENT.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		
		data[4] = (byte) ((sensorId & 0xFF00) >> 8);
		data[5] = (byte) (sensorId & 0x00FF);
		data[6] = count;

		data[7] = (byte) (((int)(channel1Amps * 10.0) >> 8) & 0xFF);
		data[8] = (byte) ((int)(channel1Amps * 10.0) & 0xFF);
		data[9] = (byte) (((int)(channel2Amps * 10.0) >> 8) & 0xFF);
		data[10] = (byte) ((int)(channel2Amps * 10.0) & 0xFF);
		data[11] = (byte) (((int)(channel3Amps * 10.0) >> 8) & 0xFF);
		data[12] = (byte) ((int)(channel3Amps * 10.0) & 0xFF);

		data[13] = (byte) (((signalLevel & 0x0F) << 4) | (batteryLevel & 0x0F));

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

			} else if (valueSelector == RFXComValueSelector.CHANNEL1_AMPS) {

				state = new DecimalType(channel1Amps);

			} else if (valueSelector == RFXComValueSelector.CHANNEL2_AMPS) {

				state = new DecimalType(channel2Amps);

			} else if (valueSelector == RFXComValueSelector.CHANNEL3_AMPS) {

				state = new DecimalType(channel3Amps);

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
