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
 * Heatmiser network thermostat
 * 
 * @author Chris Jackson
 * @since 1.4.0
 * 
 */
public class HeatmiserNetworkThermostat extends HeatmiserThermostat {
	private static Logger logger = LoggerFactory.getLogger(HeatmiserNetworkThermostat.class);

	private byte address = 0;

	public HeatmiserNetworkThermostat() {
		DCB_READ_DATA_START = 9;
	}
	
	public void setAddress(byte newAddress) {
		address = newAddress;
	}

	public int getAddress() {
		return address;
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
		frameLength = getInt(1);
		if (in.length != frameLength)
			return false;

		int crc = getInt(frameLength - 2);
		if (crc != checkCRC(in))
			return false;

		address = dcbData[3];
		function = dcbData[4];
		if (function == 1)
			return false;

		// Check that the whole DCB is returned correctly
		// While this isn't 100% necessary, it's what the binding does to make
		// things easier!
		dcbStart = getInt(5);
		if (dcbStart != 0)
			return false;

		switch (dcbData[DCB_READ_DATA_START + DCB_READ_MODEL]) {
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
	protected byte[] makePacket(boolean write, int start, int length, byte[] data) {
		byte[] outPacket;

		if (write == false)
			outPacket = new byte[10];
		else
			outPacket = new byte[10 + length];

		outPacket[0] = address;
		if (write) {
			outPacket[1] = (byte) (length + 10);
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

		return outPacket;
	}

	/**
	 * Produces a packet to poll this thermostat
	 * 
	 * @return byte array with the packet
	 */
	public byte[] pollThermostat() {
		return makePacket(false, 0, 0xffff, null);
	}
}
