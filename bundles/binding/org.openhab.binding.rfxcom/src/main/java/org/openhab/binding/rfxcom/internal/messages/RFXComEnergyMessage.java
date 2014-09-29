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

public class RFXComEnergyMessage  extends RFXComBaseMessage {

	/*	Energy packet layout (length 17)

        packetlength = 0
        packettype = 1
        subtype = 2
        seqnbr = 3
        id1 = 4
        id2 = 5
        count = 6
        instant1 = 7
        instant2 = 8
        instant3 = 9
        instant4 = 10
        total1 = 11
        total2 = 12
        total3 = 13
        total4 = 14
        total5 = 15
        total6 = 16
        battery_level = 17	//bits 3-0
        signal_level = 17 	//bits 7-4
	 */
	
	private static float TOTAL_USAGE_CONVERSION_FACTOR = 223.666F;
	private static float WATTS_TO_AMPS_CONVERSION_FACTOR = 230F;
	
	public enum SubType {
		ELEC1(0),
		ELEC2(1),

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
					RFXComValueSelector.COMMAND,
					RFXComValueSelector.INSTANT_AMPS,
					RFXComValueSelector.TOTAL_AMP_HOURS);

	public SubType subType = SubType.ELEC1;
	public int sensorId = 0;
	public byte count = 0;
	public double instantAmps = 0;
	public double totalAmpHours = 0;
	public byte signalLevel = 0;
	public byte batteryLevel = 0;

	public RFXComEnergyMessage() {
		packetType = PacketType.ENERGY;
	}

	public RFXComEnergyMessage(byte[] data) {
		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + sensorId;
		str += "\n - Count = " + count;
		str += "\n - Instant Amps = " + instantAmps;
		str += "\n - Total Amp Hours = " + totalAmpHours;
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
		
		// all usage is reported in Watts based on 230V
		double instantUsage = ((data[7] & 0xFF) << 24 | (data[8] & 0xFF) << 16 | (data[9] & 0xFF) << 8 | (data[10] & 0xFF));
		double totalUsage = ((data[11] & 0xFF) << 40 | (data[12] & 0xFF) << 32 | (data[13] & 0xFF) << 24 | (data[14] & 0xFF) << 16 | (data[15] & 0xFF) << 8 | (data[16] & 0xFF));
		
		// convert to amps so external code can determine the watts based on local voltage
		instantAmps = instantUsage / WATTS_TO_AMPS_CONVERSION_FACTOR;
		totalAmpHours = totalUsage / WATTS_TO_AMPS_CONVERSION_FACTOR / TOTAL_USAGE_CONVERSION_FACTOR;
		
		signalLevel = (byte) ((data[17] & 0xF0) >> 4);
		batteryLevel = (byte) (data[17] & 0x0F);
	}

	@Override
	public byte[] decodeMessage() {
		byte[] data = new byte[17];

		data[0] = 0x11;
		data[1] = RFXComBaseMessage.PacketType.ENERGY.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		
		data[4] = (byte) ((sensorId & 0xFF00) >> 8);
		data[5] = (byte) (sensorId & 0x00FF);
		data[6] = count;

		// convert our 'amp' values back into Watts since this is what comes back
		long instantUsage = Double.doubleToRawLongBits( instantAmps * WATTS_TO_AMPS_CONVERSION_FACTOR );
		long totalUsage = Double.doubleToRawLongBits( totalAmpHours * WATTS_TO_AMPS_CONVERSION_FACTOR * TOTAL_USAGE_CONVERSION_FACTOR );
		
		data[7] = (byte) ((instantUsage >> 24) & 0xFF);
		data[8] = (byte) ((instantUsage >> 16) & 0xFF);
		data[9] = (byte) ((instantUsage >> 8) & 0xFF);
		data[10] = (byte) (instantUsage & 0xFF);

		data[11] = (byte) ((totalUsage >> 40) & 0xFF);
		data[12] = (byte) ((totalUsage >> 32) & 0xFF);
		data[13] = (byte) ((totalUsage >> 24) & 0xFF);
		data[14] = (byte) ((totalUsage >> 16) & 0xFF);
		data[15] = (byte) ((totalUsage >> 8) & 0xFF);
		data[16] = (byte) (totalUsage & 0xFF);

		data[17] = (byte) (((signalLevel & 0x0F) << 4) | (batteryLevel & 0x0F));

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

			} else if (valueSelector == RFXComValueSelector.INSTANT_AMPS) {

				state = new DecimalType(instantAmps);

			} else if (valueSelector == RFXComValueSelector.TOTAL_AMP_HOURS) {

				state = new DecimalType(totalAmpHours);

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
