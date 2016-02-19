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

/**
 * RFXCOM data class for RFXSensor message.
 * 
 * @author Damien Servant
 * @since 1.8.0
 */
public class RFXComRfxSensorMessage extends RFXComBaseMessage {
	/*	RFXSensor packet layout (length 7)
		packetlength = 0
		packettype = 1
		subtype = 2
		seqnbr = 3
		id = 4
		msg1 = 5
		msg2 = 6
		battery_level = 7 //bits 3-0
		signal_level = 7 //bits 7-4
	 */	 

	public enum SubType {
		RFXSENSORTEMP(0),
		RFXSENSORAD(1),
		RFXSENSORVOLT(2),
		RFXSENSORMSG(3),
		
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
					RFXComValueSelector.TEMPERATURE,
					RFXComValueSelector.VOLTAGE,		
					RFXComValueSelector.STATUS,		
					RFXComValueSelector.SIGNAL_LEVEL,
					RFXComValueSelector.BATTERY_LEVEL
					);

	public SubType subType = SubType.RFXSENSORTEMP;
	public int sensorId = 0;
	public double temperature = 0;
	public double voltage = 0;
	public String message = "";
	public int typeMessage = 0;
	public byte signalLevel = 0;
	public byte batteryLevel = 0;
	
	public RFXComRfxSensorMessage() {
		packetType = PacketType.RFXSENSOR;
	}

	public RFXComRfxSensorMessage(byte[] data) {
		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + sensorId;
		
		switch (subType) {
		case RFXSENSORTEMP:
			str += "\n - Temperature = " + temperature;
		case RFXSENSORAD:
		case RFXSENSORVOLT:
			str += "\n - Voltage (mV) = " + voltage;
		case RFXSENSORMSG:
			str += "\n - Message = " + message;
		default:	
		}
		
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
		sensorId = (data[4] & 0xFF);

		temperature = 0;
		voltage = 0;
		message = "";

		switch (subType) {
		
		case RFXSENSORTEMP:
			temperature = (short) ((data[5] & 0x7F) << 8 | (data[6] & 0xFF)) / 100.0;
			if ((data[5] & 0x80) != 0)
				temperature = -temperature;		
				
		case RFXSENSORAD:
		case RFXSENSORVOLT:
			voltage = (short) ((data[5] & 0x7F) << 8 | (data[6] & 0xFF));
			
		case RFXSENSORMSG:
			typeMessage = (data[6] & 0xFF);
		
			switch (typeMessage) {
			case 0x1:
				message = "Sensor addresses incremented";
			case 0x2:
				message = "Battery low detected";
			case 0x81:
				message = "No 1-wire device connected";
			case 0x82:
				message = "1-Wire ROM CRC error";
			case 0x83:
				message = "1-Wire device connected is not a DS18B20 or DS2438";
			case 0x84:
				message = "No end of read signal received from 1-Wire device";
			case 0x85:
				message = "1-Wire scratchpad CRC error";
			default:	
				message = "ERROR: Unknown message";
			}
		
		default:	
		}

		signalLevel = (byte) ((data[7] & 0xF0) >> 4);
		batteryLevel = (byte) (data[7] & 0x0F);
	}

	@Override
	public byte[] decodeMessage() {
		byte[] data = new byte[8];
		short temp;

		data[0] = 0x07;
		data[1] = RFXComBaseMessage.PacketType.RFXSENSOR.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		data[4] = (byte) (sensorId & 0xFF);
		
		switch (subType) {
		
		case RFXSENSORTEMP:
			temp = (short) Math.abs(temperature * 100.0);
			data[5] = (byte) ((temp >> 8) & 0xFF);
			data[6] = (byte) (temp & 0xFF);	
			if (temp < 0)
				data[5] |= 0x80;
				
		case RFXSENSORAD:
		case RFXSENSORVOLT:
			temp = (short) (voltage);
			data[5] = (byte) ((temp >> 8) & 0xFF);
			data[6] = (byte) (temp  & 0xFF);
		
		case RFXSENSORMSG:
			data[6] = (byte) (typeMessage  & 0xFF);
		default:	
		}

		data[7] = (byte) (((signalLevel & 0x0F) << 4) | (batteryLevel & 0x0F));

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
			} else if (valueSelector == RFXComValueSelector.TEMPERATURE) {
				state = new DecimalType(temperature);
			} else if (valueSelector == RFXComValueSelector.VOLTAGE) {
				state = new DecimalType(voltage);
			} else {
				throw new RFXComException("Can't convert "
						+ valueSelector + " to NumberItem");
			}

		} else if (valueSelector.getItemClass() == StringItem.class) {

			if (valueSelector == RFXComValueSelector.RAW_DATA) {

				state = new StringType(
						DatatypeConverter.printHexBinary(rawMessage));

			} else if (valueSelector == RFXComValueSelector.STATUS) {

				state = new StringType(message.toString());

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
