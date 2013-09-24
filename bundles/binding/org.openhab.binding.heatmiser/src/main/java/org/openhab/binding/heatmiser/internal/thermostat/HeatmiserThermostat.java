/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.heatmiser.internal.thermostat;

import org.openhab.binding.heatmiser.internal.thermostat.HeatmiserThermostat.Functions;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

public class HeatmiserThermostat {
	private byte address;
	private int frameLength;
	private byte function;
	protected byte data[];
	private int dcbStart;
	private Models dcbModel;
	protected byte dcbState;
	protected byte dcbHeatState;
	protected byte dcbWaterState;
	protected double dcbRoomTemperature;
	protected double dcbFrostTemperature;
	protected double dcbFloorTemperature;
	protected double dcbSetTemperature;


	public void setAddress(byte newAddress) {
		address = newAddress;
	}

	public int getAddress() {
		return address;
	}

	protected int getInt(int pos) {
		int val;
		val = (data[pos] & 0xFF) + ((data[pos+1] & 0xFF) * 256);
		return val;
	}

	protected double getTemp(int pos) {
		double val;
		val = (double)((data[pos+1] & 0xFF) + ((data[pos] & 0xFF) * 256)) / 10;
		return val;
	}

	/*
	 * Sets the new packet data for the thermostat
	 * This function processes the basic data and checks the validity of the incoming data
	 * 
	 * @param in Input data byte array
	 */
	public boolean setData(byte in[]) {
		if(in.length < 9)
			return false;
		
		data = in;
		frameLength = getInt(1);
		if(in.length != frameLength)
			return false;

		int crc = getInt(frameLength-2);
		if(crc != checkCRC(data))
			return false;

		address = data[3];
		function = data[4];
		if(function == 1)
			return false;
		
		// Check that the whole DCB is returned
		// While this isn't 100% necessary, it's what the binding does to make things easier!
		dcbStart = getInt(5);
		if(dcbStart != 0)
			return false;

		switch(data[13]) {
		case 0:
			dcbModel = Models.DT;
			break;
		case 1:
			dcbModel = Models.DTE;
			break;
		case 2:
			dcbModel = Models.PRT;
			break;
		case 3:
			dcbModel = Models.PRTE;
			break;
		case 4:
			dcbModel = Models.PRTHW;
			break;
		}
		
		return true;
	}
	
	private int checkCRC(byte[] packet) {
        int crc = 0xFFFF;          // initial value
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12) 

        for (int cnt = 0; cnt < packet.length-2; cnt++) {
        	byte b = packet[cnt];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15    & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
             }
        }

        crc &= 0xffff;

		return crc;
	}

	protected byte[] makePacket(boolean write, int start, int length, byte[] data) {
		byte[] outPacket;

		if(write == false)
			outPacket = new byte[10];
		else
			outPacket = new byte[10+length];
		
		outPacket[0] = address;
		if(write) {
			outPacket[1] = (byte) (length + 10);
			outPacket[3] = 1;
		}
		else {
			outPacket[1] = 10;
			outPacket[3] = 0;
		}
		outPacket[2] = (byte) 0x81;
		outPacket[4] = (byte)(start & 0xff);
		outPacket[5] = (byte)((start >> 8) & 0xff);
		outPacket[6] = (byte)(length & 0xff);
		outPacket[7] = (byte)((length >> 8) & 0xff);

		if(write == true) {
			for(byte cnt = 0; cnt < length; cnt++)
				outPacket[8+cnt] = data[cnt];
		}
		else
			length = 0;

		int crc = checkCRC(outPacket);
		outPacket[length+8] = (byte)(crc & 0xff);
		outPacket[length+9] = (byte)((crc >> 8) & 0xff);

		return outPacket;
	}
	
	public byte[] pollThermostat() {
		return makePacket(false, 0, 0xffff, null);
	}

	public byte[] formatCommand(Functions function, Command command) {
		return null;
	}
	
	public boolean setTime() {
		return true;
	}

	public boolean setTemperature() {
		return false;
	}

	public boolean setHoliday() {
		return false;
	}

	public State getTemperature(Class<? extends Item> itemType) {
		if(itemType == StringItem.class)
			return StringType.valueOf(Double.toString(dcbRoomTemperature));

		// Default to DecimalType
		return DecimalType.valueOf(Double.toString(dcbRoomTemperature));
	}
	
	public State getFrostTemperature(Class<? extends Item> itemType) {
		if(itemType == StringItem.class)
			return StringType.valueOf(Double.toString(dcbFrostTemperature));

		// Default to DecimalType
		return DecimalType.valueOf(Double.toString(dcbFrostTemperature));
	}

	public State getFloorTemperature(Class<? extends Item> itemType) {
		if(itemType == StringItem.class)
			return StringType.valueOf(Double.toString(dcbFloorTemperature));

		// Default to DecimalType
		return DecimalType.valueOf(Double.toString(dcbFloorTemperature));
	}

	public State getState(Class<? extends Item> itemType) {
		if(itemType == StringItem.class)
			return dcbState == 1 ? StringType.valueOf("ON") : StringType.valueOf("OFF");
		if(itemType == SwitchItem.class)
			return dcbState == 1 ? OnOffType.ON : OnOffType.OFF;

		// Default to DecimalType
		return DecimalType.valueOf(Integer.toString(dcbState));
	}
	
	public State getWaterState(Class<? extends Item> itemType) {
		if(itemType == StringItem.class)
			return dcbWaterState == 1 ? StringType.valueOf("ON") : StringType.valueOf("OFF");
		if(itemType == SwitchItem.class)
			return dcbWaterState == 1 ? OnOffType.ON : OnOffType.OFF;

		// Default to DecimalType
		return DecimalType.valueOf(Integer.toString(dcbWaterState));
	}
	
	public State getSetTemperature(Class<? extends Item> itemType) {
		if(itemType == StringItem.class)
			return StringType.valueOf(Double.toString(dcbSetTemperature));

		// Default to DecimalType
		return DecimalType.valueOf(Double.toString(dcbSetTemperature));
	}

	public State getHeatState(Class<? extends Item> itemType) {
		if(itemType == StringItem.class)
			return dcbHeatState == 1 ? StringType.valueOf("ON") : StringType.valueOf("OFF");
		if(itemType == SwitchItem.class)
			return dcbHeatState == 1 ? OnOffType.ON : OnOffType.OFF;

		// Default to DecimalType
		return DecimalType.valueOf(Integer.toString(dcbHeatState));
	}

	public Models getModel() {
		return dcbModel;
	}
	
	public enum Functions {
		UNKNOWN,
		ROOMTEMP,
		FLOORTEMP,
		ONOFF,
		RUNMODE,
		SETTEMP,
		FROSTTEMP,
		HOLDTEMP,
		HOLIDAY,
		HEATSTATE,
		WATERSTATE;
	}

	public enum Models {
		PRT, PRTHW, DT, DTE, PRTE
	}
}
