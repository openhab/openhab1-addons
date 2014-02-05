/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.heatmiser.internal.thermostat;

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
public class HeatmiserWifiThermostat extends HeatmiserThermostat {
	private static Logger logger = LoggerFactory.getLogger(HeatmiserWifiThermostat.class);

	private Integer PIN = null;

	public HeatmiserWifiThermostat() {
		DCB_READ_DATA_START = 7;
	}

	public void setPIN(Integer pin) {
		PIN = new Integer(pin);
	}

	public int getAddress() {
		// Wifi thermostats must have address set to 0
		return 0;
	}

	/*
	 * Sets the new packet data for the thermostat This function processes the
	 * basic data and checks the validity of the incoming data
	 * 
	 * @param in Input data byte array
	 */
	public boolean setData(byte in[]) {
		if (in.length < 9)
			return false;

		dcbData = in;

		// First byte is 0x94
		if (dcbData[0] != 0x94)
			return false;

		// Get and check the length
		frameLength = getInt(1);
		if (in.length != frameLength)
			return false;

		// Check the CRC
		int crc = getInt(frameLength - 2);
		if (crc != checkCRC(in))
			return false;

		// Check that the whole DCB is returned
		// While this isn't 100% necessary, it's what the binding does to make
		// things easier!
		dcbStart = getInt(3);
		if (dcbStart != 0)
			return false;

		switch (dcbData[DCB_READ_DATA_START + DCB_READ_MODEL]) {
		case 0:
			dcbModel = Models.DT_WIFI;
			break;
		case 1:
			dcbModel = Models.DTE_WIFI;
			break;
		case 2:
			dcbModel = Models.PRT_WIFI;
			break;
		case 3:
			dcbModel = Models.PRTE_WIFI;
			break;
		case 4:
			dcbModel = Models.PRTHW_WIFI;
			break;
		}

		readDCB();
		return true;
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
	protected byte[] makePacket(int start, int length, byte[] data) {
/*		byte[] outPacket;

		// If PINset is true, then this is a WiFi thermostat
		if (PIN == null)
			return null;

		outPacket[0] = (byte) 0x93;
		if (write) {
			outPacket[1] = (byte) (length + 11);
			outPacket[3] = 1;
		} else {
			outPacket[1] = 10;
			outPacket[3] = 0;
		}
		outPacket[2] = (byte) 0x81;
		outPacket[4] = (byte) (start & 0xff);
		outPacket[5] = (byte) ((start >> 8) & 0xff);
		outPacket[6] = (byte) (length & 0xff);
		outPacket[7] = (byte) ((length >> 8) & 0xff);

		if (write == true) {
			for (byte cnt = 0; cnt < length; cnt++)
				outPacket[8 + cnt] = data[cnt];
		} else
			length = 0;

		int crc = checkCRC(outPacket);
		outPacket[length + 8] = (byte) (crc & 0xff);
		outPacket[length + 9] = (byte) ((crc >> 8) & 0xff);

		return outPacket;*/
		return null;
	}

	/**
	 * Produces a packet to poll this thermostat
	 * 
	 * @return byte array with the packet
	 */
	public byte[] pollThermostat() {
		byte[] outPacket = new byte[11];
		
		// If PINset is true, then this is a WiFi thermostat
		if (PIN == null)
			return null;

		outPacket[0] = (byte) 0x93;

		outPacket[1] = 11;
		outPacket[2] = 0;

		outPacket[3] = (byte) (PIN & 0xff);
		outPacket[4] = (byte) ((PIN >> 8) & 0xff);
		outPacket[5] = 0;
		outPacket[6] = 0;
		outPacket[7] = (byte)0xff;
		outPacket[8] = (byte)0xff;

		int crc = checkCRC(outPacket);
		outPacket[9] = (byte) (crc & 0xff);
		outPacket[10] = (byte) ((crc >> 8) & 0xff);

		return outPacket;
	}

	public boolean setTime() {
		return true;
	}

	@Override
	protected byte[] makePacket(boolean write, int start, int length, byte[] data) {
		// TODO Auto-generated method stub
		return null;
	}
}
