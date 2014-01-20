/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.heatmiser.internal.thermostat;

import java.util.Calendar;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for the Heatmiser thermostats. This provides the core
 * functionality - other thermostat classes extend this to provide or update the
 * specific functionality of that thermostat.
 * 
 * @author Chris Jackson
 * @since 1.4.0
 * 
 */
public abstract class HeatmiserThermostat {
	private static Logger logger = LoggerFactory.getLogger(HeatmiserThermostat.class);

	private String connector;

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
	protected int dcbHolidayTime;
	protected int dcbHoldTime;

	protected byte DCB_READ_ADDRESS				= 3;
	protected byte DCB_READ_DATA_START			= 9;

	protected byte DCB_READ_MODEL				= 4;
	protected byte DCB_READ_FROST_TEMPERATURE	= 17;
	protected byte DCB_READ_SET_TEMPERATURE		= 18;
	protected byte DCB_READ_ON_OFF_STATE		= 21;
	protected byte DCB_READ_HOLIDAY_TIME		= 24;
	protected byte DCB_READ_HOLD_TIME			= 26;
	protected byte DCB_READ_FLOOR_TEMPERATURE	= 30;
	protected byte DCB_READ_ROOM_TEMPERATURE	= 32;
	protected byte DCB_READ_HEAT_STATE			= 35;
	protected byte DCB_READ_WATER_STATE			= 36;


	/**
	 * Set the connector to be used with this thermostat
	 * @param newConnector
	 */
	public void setConnector(String newConnector) {
		connector = newConnector;
	}

	/**
	 * Get the connector to be used with this thermostat
	 * @return
	 */
	public String getConnector() {
		return connector;
	}

	protected int getInt(int pos) {
		int val;
		val = (data[pos] & 0xFF) + ((data[pos + 1] & 0xFF) * 256);
		return val;
	}

	protected double getTemp(int pos) {
		double val;
		val = (double) ((data[pos + 1] & 0xFF) + ((data[pos] & 0xFF) * 256)) / 10;
		return val;
	}

	/**
	 * 
	 */
	void readDCB() {
		dcbState = data[DCB_READ_DATA_START + DCB_READ_ON_OFF_STATE];
		dcbHeatState = data[DCB_READ_DATA_START + DCB_READ_HEAT_STATE];
		dcbFrostTemperature = data[DCB_READ_DATA_START + DCB_READ_FROST_TEMPERATURE];
		dcbRoomTemperature = getTemp(DCB_READ_DATA_START + DCB_READ_ROOM_TEMPERATURE);
		dcbSetTemperature = data[DCB_READ_DATA_START + DCB_READ_SET_TEMPERATURE];
		dcbHolidayTime = getTime(DCB_READ_DATA_START + DCB_READ_HOLIDAY_TIME);
		dcbHoldTime = getTime(DCB_READ_DATA_START + DCB_READ_HOLD_TIME);

		// The following are not supported in all thermostats
		if(DCB_READ_FLOOR_TEMPERATURE != 0)
			dcbFloorTemperature = getTemp(DCB_READ_DATA_START + DCB_READ_FLOOR_TEMPERATURE);

		if(DCB_READ_WATER_STATE != 0)
			dcbWaterState = data[DCB_READ_DATA_START + DCB_READ_WATER_STATE];
	}

	/*
	 * Sets the new packet data for the thermostat This function processes the
	 * basic data and checks the validity of the incoming data
	 * 
	 * @param in Input data byte array
	 */
	public boolean setData(byte in[]) {
		if (in.length < DCB_READ_DATA_START)
			return false;

		data = in;
		frameLength = getInt(1);
		if (in.length != frameLength)
			return false;

		int crc = getInt(frameLength - 2);
		if (crc != checkCRC(data))
			return false;

		address = data[3];
		function = data[4];
		if (function == 1)
			return false;

		// Check that the whole DCB is returned
		// While this isn't 100% necessary as per the protocol, it's what the binding does to make
		// things easier!
		dcbStart = getInt(5);
		if (dcbStart != 0)
			return false;

		readDCB();
		return true;
	}

	protected int checkCRC(byte[] packet) {
		int crc = 0xFFFF; // initial value
		int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)

		for (int cnt = 0; cnt < packet.length - 2; cnt++) {
			byte b = packet[cnt];
			for (int i = 0; i < 8; i++) {
				boolean bit = ((b >> (7 - i) & 1) == 1);
				boolean c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				if (c15 ^ bit)
					crc ^= polynomial;
			}
		}

		crc &= 0xffff;

		return crc;
	}

	/**
	 * Build a packet to send to the thermostat
	 * 
	 * @param write
	 * @param start
	 * @param length
	 * @param data
	 * @return
	 */
	protected abstract byte[] makePacket(boolean write, int start, int length, byte[] data);

	/**
	 * Produces a packet to poll this thermostat
	 * 
	 * @return byte array with the packet
	 */
	public abstract byte[] pollThermostat();

	/**
	 * Formats a command to the thermostat
	 * 
	 * @param function
	 *            The command function
	 * @param command
	 *            The openHAB command parameter
	 * @return byte array with the command packet
	 */
	public byte[] formatCommand(Functions function, Command command) {
		switch (function) {
		case SETTEMP:
			return setRoomTemperature(command);
		case ONOFF:
			return setOnOff(command);
		case RUNMODE:
			return setRunMode(command);
		case FROSTTEMP:
			return setFrostTemperature(command);
		case HOLIDAYSET:
			return setHolidayTime(command);
		default:
			return null;
		}
	}

	public boolean setTime() {
		return true;
	}

	/**
	 * Extracts a time from the data buffer
	 * @param i
	 * @return
	 */
	protected int getTime(int i) {
		return (data[36] & 0xFF) + ((data[35] & 0xFF) * 256);
	}

	/**
	 * Command to set the room temperature
	 * 
	 * @param command
	 * @return byte array with the command data
	 */
	public byte[] setRoomTemperature(Command command) {
		byte[] cmdByte = new byte[1];

		if (!(command instanceof DecimalType))
			return null;

		byte temperature = ((DecimalType) command).byteValue();

		if (temperature < 5)
			return null;
		if (temperature > 35)
			return null;

		cmdByte[0] = temperature;
		return makePacket(true, 18, 1, cmdByte);
	}

	/**
	 * Sets the frost temperature
	 * 
	 * @param command
	 * @return byte array with the command packet
	 */
	public byte[] setFrostTemperature(Command command) {
		byte[] cmdByte = new byte[1];

		byte temperature = ((DecimalType) command).byteValue();
		if (temperature < 5)
			temperature = 5;
		if (temperature > 18)
			temperature = 18;

		cmdByte[0] = (byte) temperature;
		return makePacket(true, 17, 1, cmdByte);
	}

	/**
	 * Sets the holiday time
	 * 
	 * @param command
	 *            time to set holiday mode - specified in days
	 * @return command string to send to thermostat
	 */
	public byte[] setHolidayTime(Command command) {
		byte[] cmdBytes = new byte[2];

		// Convert the time into an integer - 99 days is the maximum
		int time = Integer.parseInt(command.toString());
		if (time < 0)
			time = 0;
		if (time > 99)
			time = 0;

		// Convert time to hours
		time = time * 24;

		// Subtract the hours gone today
		Calendar now = Calendar.getInstance();
		time -= now.get(Calendar.HOUR_OF_DAY);

		// Sanity check
		if (time < 0)
			time = 0;
		if (time > (99 * 24))
			time = 0;
		logger.debug("Setting holiday time {} days = {} hours.", command.toString(), time);

		cmdBytes[0] = (byte) (time & 0xff);
		cmdBytes[1] = (byte) ((time >> 8) & 0xff);

		return makePacket(true, 24, 2, cmdBytes);
	}

	/**
	 * Sets the current time for the thermostat
	 * 
	 * @param command
	 * @return command string to send to thermostat
	 */
	public byte[] setTime(Command command) {
		byte[] cmdBytes = new byte[4];

		// Now.wday = Now.wday - 1
		// if(Now.wday == 0) then
		// Now.wday = Now.wday + 7
		// end
		// cmdFrame =
		// Dec2Hex(Now.wday)..Dec2Hex(Now.hour)..Dec2Hex(Now.min)..Dec2Hex(Now.sec)
		return makePacket(true, 43, 4, cmdBytes);
	}

	/**
	 * Enables or disables the thermostat
	 * 
	 * @param command
	 * @return
	 */
	public byte[] setOnOff(Command command) {
		byte[] cmdByte = new byte[1];

		if (command.toString().contentEquals("ON"))
			cmdByte[0] = 1;
		else
			cmdByte[0] = 0;
		return makePacket(true, 21, 1, cmdByte);
	}

	public byte[] setRunMode(Command command) {
		byte[] cmdByte = new byte[1];

		if (command.toString().contentEquals("ON"))
			cmdByte[0] = 1;
		else
			cmdByte[0] = 0;
		return makePacket(true, 23, 1, cmdByte);
	}

	/**
	 * Returns the current room temperature
	 * 
	 * @param itemType
	 * @return
	 */
	public State getTemperature(Class<? extends Item> itemType) {
		if(DCB_READ_ROOM_TEMPERATURE == 0)
			return null;

		if (itemType == StringItem.class)
			return StringType.valueOf(Double.toString(dcbRoomTemperature));

		// Default to DecimalType
		return DecimalType.valueOf(Double.toString(dcbRoomTemperature));
	}

	/**
	 * Returns the current frost temperature
	 * 
	 * @param itemType
	 * @return
	 */
	public State getFrostTemperature(Class<? extends Item> itemType) {
		if (itemType == StringItem.class)
			return StringType.valueOf(Double.toString(dcbFrostTemperature));

		// Default to DecimalType
		return DecimalType.valueOf(Double.toString(dcbFrostTemperature));
	}

	/**
	 * Returns the current floor temperature
	 * 
	 * @param itemType
	 * @return
	 */
	public State getFloorTemperature(Class<? extends Item> itemType) {
		if (itemType == StringItem.class)
			return StringType.valueOf(Double.toString(dcbFloorTemperature));

		// Default to DecimalType
		return DecimalType.valueOf(Double.toString(dcbFloorTemperature));
	}

	/**
	 * Returns the current state of the thermostat This is a consolidated status
	 * that brings together a number of status registers within the thermostat.
	 * 
	 * @param itemType
	 * @return
	 */
	public State getState(Class<? extends Item> itemType) {
		// If this is a switch, then just treat this like the getOnOffState()
		// function
		if (itemType == SwitchItem.class)
			return dcbState == 1 ? OnOffType.ON : OnOffType.OFF;

		// Default to a string
		if (dcbState == 0)
			return StringType.valueOf(States.OFF.toString());
		if (dcbHolidayTime != 0)
			return StringType.valueOf(States.HOLIDAY.toString());
		if (dcbHoldTime != 0)
			return StringType.valueOf(States.HOLD.toString());

		return StringType.valueOf(States.ON.toString());
	}

	/**
	 * Returns the current heating state
	 * 
	 * @param itemType
	 * @return
	 */
	public State getOnOffState(Class<? extends Item> itemType) {
		if (itemType == StringItem.class)
			return dcbState == 1 ? StringType.valueOf("ON") : StringType.valueOf("OFF");
		if (itemType == SwitchItem.class)
			return dcbState == 1 ? OnOffType.ON : OnOffType.OFF;

		// Default to DecimalType
		return DecimalType.valueOf(Integer.toString(dcbState));
	}

	public State getWaterState(Class<? extends Item> itemType) {
		if(DCB_READ_WATER_STATE == 0)
			return null;

		if (itemType == StringItem.class)
			return dcbWaterState == 1 ? StringType.valueOf("ON") : StringType.valueOf("OFF");
		if (itemType == SwitchItem.class)
			return dcbWaterState == 1 ? OnOffType.ON : OnOffType.OFF;

		// Default to DecimalType
		return DecimalType.valueOf(Integer.toString(dcbWaterState));
	}

	public State getSetTemperature(Class<? extends Item> itemType) {
		if (itemType == StringItem.class)
			return StringType.valueOf(Double.toString(dcbSetTemperature));

		// Default to DecimalType
		return DecimalType.valueOf(Double.toString(dcbSetTemperature));
	}

	public State getHeatState(Class<? extends Item> itemType) {
		if (itemType == StringItem.class)
			return dcbHeatState == 1 ? StringType.valueOf("ON") : StringType.valueOf("OFF");
		if (itemType == SwitchItem.class)
			return dcbHeatState == 1 ? OnOffType.ON : OnOffType.OFF;

		// Default to DecimalType
		return DecimalType.valueOf(Integer.toString(dcbHeatState));
	}

	public State getHolidayMode(Class<? extends Item> itemType) {
		return dcbHolidayTime > 0 ? OnOffType.ON : OnOffType.OFF;
	}

	public State getHolidayTime(Class<? extends Item> itemType) {
		if (itemType == SwitchItem.class)
			return dcbHolidayTime > 0 ? OnOffType.ON : OnOffType.OFF;

		// Return a date with the end time
		Calendar now = Calendar.getInstance();
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		now.add(Calendar.HOUR, dcbHolidayTime);
		return new DateTimeType(now);
	}

	public State getHolidaySet(Class<? extends Item> itemType) {
		if (itemType == SwitchItem.class)
			return dcbHolidayTime > 0 ? OnOffType.ON : OnOffType.OFF;

		// Return the number of days (midnights) remaining
		// ie midnight tonight == 1
		int days = 0;
		if (dcbHolidayTime > 0)
			days = dcbHolidayTime / 24 + 1;

		return DecimalType.valueOf(Integer.toString(days));
	}

	public State getHoldMode(Class<? extends Item> itemType) {
		return dcbHoldTime > 0 ? OnOffType.ON : OnOffType.OFF;
	}

	public State getHoldTime(Class<? extends Item> itemType) {
		if (itemType == SwitchItem.class)
			return dcbHoldTime > 0 ? OnOffType.ON : OnOffType.OFF;

		// Return a date with the end time
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MINUTE, dcbHoldTime);
		return new DateTimeType(now);
	}

	public Models getModel() {
		return dcbModel;
	}

	public enum Functions {
		UNKNOWN, ROOMTEMP, FLOORTEMP, ONOFF, RUNMODE, SETTEMP, FROSTTEMP, HOLIDAYTIME, HOLIDAYMODE, HOLIDAYSET, HEATSTATE, WATERSTATE, HOLDTIME, HOLDMODE, STATE;
	}

	public enum States {
		OFF, ON, HOLD, HOLIDAY;
	}

	public enum Models {
		UNKNOWN, PRT, PRTHW, DT, DTE, PRTE, PRT_WIFI, PRTHW_WIFI, DT_WIFI, DTE_WIFI, PRTE_WIFI
	}
}
