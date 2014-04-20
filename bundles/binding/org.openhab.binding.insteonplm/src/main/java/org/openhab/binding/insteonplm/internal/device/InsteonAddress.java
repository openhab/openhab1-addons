/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import org.openhab.binding.insteonplm.internal.utils.Utils;

/**
 * This class wraps an Insteon Address 'xx.xx.xx'
 * 
 * @author Daniel Pfrommer
 * @since 1.5.0
 */

public class InsteonAddress {
	private byte highByte;
	private byte middleByte;
	private byte lowByte;
	
	public InsteonAddress() {
		highByte	= 0x00;
		middleByte	= 0x00;
		lowByte		= 0x00;
	}
	
	public InsteonAddress(InsteonAddress a) {
		highByte	= a.highByte;
		middleByte	= a.middleByte;
		lowByte		= a.lowByte;
	}
	
	public InsteonAddress(byte high, byte middle, byte low) {
		highByte	= high;
		middleByte	= middle;
		lowByte		= low;
	}
	
	public InsteonAddress(int high, int middle, int low) {
		this((byte) high, (byte) middle, (byte) low);
	}
	
	public InsteonAddress(byte[] address) {
		if (address.length != 3) {
			throw new IllegalArgumentException("address must be 3 bytes!");
		}
		highByte	= address[0];
		middleByte	= address[1];
		lowByte		= address[2];
	}
	/**
	 * Constructor
	 * @param address string must have format of e.g. '2a.3c.40'
	 */
	public InsteonAddress(String address) {
		String[] parts = address.split("\\.");
		if (parts.length != 3) 
			throw new IllegalArgumentException("Address string must have 3 bytes, has: " + parts.length);
		highByte	= (byte) Utils.fromHexString(parts[0]);
		middleByte	= (byte) Utils.fromHexString(parts[1]);
		lowByte		= (byte) Utils.fromHexString(parts[2]);
	}
	
	public void setHighByte(byte h)		{highByte	= h;}
	public void setMiddleByte(byte m)	{middleByte = m;}
	public void setLowByte(byte l)		{lowByte	= l;}

	public byte getHighByte()	{return highByte;}
	public byte getMiddleByte()	{return middleByte;}
	public byte getLowByte()	{return lowByte;}

	public String toString() {
		String s = Utils.getHexString(highByte) + "." + 
			Utils.getHexString(middleByte) + "." + 
			Utils.getHexString(lowByte);
		return s;
	}
	public void storeBytes(byte[] bytes, int offset) {
		bytes[offset] 		= getHighByte();
		bytes[offset + 1] 	= getMiddleByte();
		bytes[offset + 2]	= getLowByte();
	}
	public void loadBytes(byte[] bytes, int offset) {
		setHighByte(bytes[offset]);
		setMiddleByte(bytes[offset + 1]);
		setLowByte(bytes[offset + 2]);
	}
	@Override
	public boolean equals(Object a) {
		if (!(a instanceof InsteonAddress)) return false;
		InsteonAddress address = (InsteonAddress) a;
		boolean low = address.getLowByte() == getLowByte();
		boolean middle = address.getMiddleByte() == getMiddleByte();
		boolean high = address.getHighByte() == getHighByte();
		return low && middle && high;
	}
	@Override
	public int hashCode() {
		int hashCode = lowByte + 256 * middleByte + 65536 * highByte;
		return hashCode;
	}

	/**
	 * Test if Insteon address is valid
	 * @return true if address is in valid AB.CD.EF format
	 */
	public static boolean s_isValid(String addr) {
		String[] fields = addr.split("\\.");
		if (fields.length != 3) return false;
		try{
			// convert the insteon xx.xx.xx address to integer to test
			@SuppressWarnings("unused")
			int test = Integer.parseInt(fields[2], 16) * 65536
					+ Integer.parseInt(fields[1], 16) * 256 +
					+ Integer.parseInt(fields[0], 16);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Turn string into address
	 * @param val the string to convert
	 * @return the corresponding insteon address
	 */
	public static InsteonAddress s_parseAddress(String val) {
		return new InsteonAddress(val);
	}
	
	public static void main(String[] args) {
		// debug/test code
		InsteonAddress a1 = new InsteonAddress();
		InsteonAddress a2 = new InsteonAddress();
		System.out.println(a1.equals(a2));
		System.out.println(new InsteonAddress("0f.0f.0a"));
	}
}