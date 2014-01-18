/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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

/**
 * RFXCOM data class for temperature and humidity message.
 * 
 * @author Marc SAUVEUR
 * @since 1.4.0
 */
public class RFXComRainMessage extends RFXComBaseMessage {

	public enum SubType {
		UNDEF(0),
		RGR126_682_918_928(1),
		PCR800(2),
		TFA(3),
		UPM_RG700(4),
		WS2300(5),
		LA_CROSSE_TX5(6),
		
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
					RFXComValueSelector.RAIN_RATE,
					RFXComValueSelector.RAIN_TOTAL);

	public SubType subType = SubType.RGR126_682_918_928;
	public int sensorId = 0;
	public double rainRate = 0;
	/*public byte rain = 0;*/
	public double rainTotal = 0;
	public byte signalLevel = 0;
	public byte batteryLevel = 0;

	public RFXComRainMessage() {
		packetType = PacketType.RAIN;
	}

	public RFXComRainMessage(byte[] data) {
		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + sensorId;
		str += "\n - Rain rate = " + rainRate;
		str += "\n - Rain total = " + rainTotal;
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

		rainRate = (short) ((data[6] & 0xFF) << 8 | (data[7] & 0xFF)) * 0.01;
		rainTotal = (short) ((data[8] & 0xFF) << 8 | (data[9] & 0xFF) << 8 | (data[10] & 0xFF) ) * 0.1;
		signalLevel = (byte) ((data[11] & 0xF0) >> 4);
		batteryLevel = (byte) (data[11] & 0x0F);
	}

	@Override
	public byte[] decodeMessage() {
		byte[] data = new byte[10];

		data[0] = 0x0B;
		data[1] = RFXComBaseMessage.PacketType.RAIN.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		data[4] = (byte) ((sensorId & 0xFF00) >> 8);
		data[5] = (byte) (sensorId & 0x00FF);

		short rainR = (short) Math.abs(rainRate * 100);
		data[6] = (byte) ((rainR >> 8) & 0xFF);
		data[7] = (byte) (rainR & 0xFF);
		
		short rainT = (short) Math.abs(rainTotal * 10);
		data[8] = (byte) ((rainT >> 8) & 0xFF);
		data[9] = (byte) ((rainT >> 8) & 0xFF);
		data[10] = (byte) (rainT & 0xFF);

		data[11] = (byte) (((signalLevel & 0x0F) << 4) | (batteryLevel & 0x0F));

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

			} else if (valueSelector == RFXComValueSelector.RAIN_RATE) {

				state = new DecimalType(rainRate);
			} else if (valueSelector == RFXComValueSelector.RAIN_TOTAL) {

				state = new DecimalType(rainTotal);
			

			} else {
				throw new RFXComException("Can't convert "
						+ valueSelector + " to NumberItem");
			}

		} else if (valueSelector.getItemClass() == StringItem.class) {

			if (valueSelector == RFXComValueSelector.RAW_DATA) {

				state = new StringType(
						DatatypeConverter.printHexBinary(rawMessage));

			
			} else {
				throw new RFXComException("Can't convert " + valueSelector + " to StringItem");
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
