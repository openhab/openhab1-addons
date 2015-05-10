/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.radius.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class defines RADIUS packet type codes
 * 
 * @author Jan N. Klug
 * @since 1.8.0
 */

class RadiusTypeCode {
	public static final byte ACCESS_REQUEST = 1;
	public static final byte ACCESS_ACCEPT = 2;
	public static final byte ACCESS_REJECT = 3;
	public static final byte ACCOUNTING_REQUEST = 4;
	public static final byte ACCOUNTING_RESPONSE = 5;
	public static final byte ACCESS_CHALLENGE = 11;
	public static final byte STATUS_SERVER = 12;
	public static final byte STATUS_CLIENT = 13;

	private static final Map<String, Byte> codeMap = Collections.unmodifiableMap(new HashMap<String, Byte>() {
		private static final long serialVersionUID = -3311245286019033513L;
		{
			put("ACCESS_REQUEST", ACCESS_REQUEST);
			put("ACCESS_ACCEPT", ACCESS_ACCEPT);
			put("ACCESS_REJECT", ACCESS_REJECT);
			put("ACCOUNTING_REQUEST", ACCOUNTING_REQUEST);
			put("ACCOUNTING_RESPONSE", ACCOUNTING_RESPONSE);
			put("ACCESS_CHALLENGE", ACCESS_CHALLENGE);
			put("STATUS_SERVER", STATUS_SERVER);
			put("STATUS_CLIENT", STATUS_CLIENT);
		}
	});

	/**
	 * get RADIUS type code value from string
	 * 
	 * @param code
	 *            string representation of type code
	 * @return numeric type code value or 0 if invalid
	 */
	public static byte getCode(final String code) {
		if (codeMap.containsKey(code)) {
			return codeMap.get(code);
		} else {
			return 0;
		}
	}

}

/**
 * This class defines RADIUS packet type attributes.
 * 
 * @author Jan N. Klug
 * @since 1.8.0
 */

class RadiusAttribute {
	public static final byte USER_NAME = 1;
	public static final byte USER_PASSWORD = 2;
	public static final byte CHAP_PASSWORD = 3;
	public static final byte NAS_IP_ADDRESS = 3;
	public static final byte NAS_PORT = 5;
	public static final byte SERVICE_TYPE = 6;
	public static final byte FRAMED_PROTOCOL = 7;
	public static final byte FRAMED_IP_ADDRESS = 8;
	public static final byte FRAMED_IP_NETMASK = 9;
	public static final byte FRAMED_ROUTING = 10;
	public static final byte FILTER_ID = 11;
	public static final byte FRAMED_MTU = 12;
	public static final byte FRAMED_COMPRESSION = 13;
	public static final byte LOGIN_IP_HOST = 14;
	public static final byte LOGIN_SERVICE = 15;
	public static final byte LOGIN_TCP_PORT = 16;
	public static final byte REPLY_MESSAGE = 18;
	public static final byte CALLBACK_NUMBER = 19;
	public static final byte CALLBACK_ID = 20;
	public static final byte FRAMED_ROUTE = 22;
	public static final byte FRAMED_IPX_NETWORK = 23;
	public static final byte STATE = 24;
	public static final byte CLASS = 25;
	public static final byte VENDOR_SPECIFIC = 26;
	public static final byte SESSION_TIMEOUT = 27;
	public static final byte IDLE_TIMEOUT = 28;
	public static final byte TERMINATION_ACTION = 29;
	public static final byte CALLED_STATION_ID = 30;
	public static final byte CALLING_STATION_ID = 31;
	public static final byte NAS_IDENTIFIER = 32;
	public static final byte PROXY_STATE = 33;
	public static final byte LOGIN_LAT_SERVICE = 34;
	public static final byte LOGIN_LAT_NODE = 35;
	public static final byte LOGIN_LAT_GROUP = 36;
	public static final byte FRAMED_APPLETALK_LINK = 37;
	public static final byte FRAMED_APPLETALK_NETWORK = 38;
	public static final byte FRAMED_APPLETALK_ZONE = 39;
	public static final byte CHAP_CHALLENGE = 60;
	public static final byte NAS_PORT_TYPE = 61;
	public static final byte PORT_LIMIT = 62;
	public static final byte LOGIN_LAT_PORT = 63;

	private static final Map<String, Byte> attributeMap = Collections.unmodifiableMap(new HashMap<String, Byte>() {
		private static final long serialVersionUID = -7167689293970183229L;
		{
			// TODO: add ACCOUNTING attributes
			put("USER_NAME", USER_NAME);
			put("USER_PASSWORD", USER_PASSWORD);
			put("CHAP_PASSWORD", CHAP_PASSWORD);
			put("NAS_IP_ADDRESS", NAS_IP_ADDRESS);
			put("NAS_PORT", NAS_PORT);
			put("SERVICE_TYPE", SERVICE_TYPE);
			put("FRAMED_PROTOCOL", FRAMED_PROTOCOL);
			put("FRAMED_IP_ADDRESS", FRAMED_IP_ADDRESS);
			put("FRAMED_IP_NETMASK", FRAMED_IP_NETMASK);
			put("FRAMED_ROUTING", FRAMED_ROUTING);
			put("FILTER_ID", FILTER_ID);
			put("FRAMED_MTU", FRAMED_MTU);
			put("FRAMED_COMPRESSION", FRAMED_COMPRESSION);
			put("LOGIN_IP_HOST", LOGIN_IP_HOST);
			put("LOGIN_SERVICE", LOGIN_SERVICE);
			put("LOGIN_TCP_PORT", LOGIN_TCP_PORT);
			put("REPLY_MESSAGE", REPLY_MESSAGE);
			put("CALLBACK_NUMBER", CALLBACK_NUMBER);
			put("CALLBACK_ID", CALLBACK_ID);
			put("FRAMED_ROUTE", FRAMED_ROUTE);
			put("FRAMED_IPX_NETWORK", FRAMED_IPX_NETWORK);
			put("STATE", STATE);
			put("CLASS", CLASS);
			put("VENDOR_SPECIFIC", VENDOR_SPECIFIC);
			put("SESSION_TIMEOUT", SESSION_TIMEOUT);
			put("IDLE_TIMEOUT", IDLE_TIMEOUT);
			put("TERMINATION_ACTION", TERMINATION_ACTION);
			put("CALLED_STATION_ID", CALLED_STATION_ID);
			put("CALLING_STATION_ID", CALLING_STATION_ID);
			put("NAS_IDENTIFIER", NAS_IDENTIFIER);
			put("PROXY_STATE", PROXY_STATE);
			put("LOGIN_LAT_SERVICE", LOGIN_LAT_SERVICE);
			put("LOGIN_LAT_NODE", LOGIN_LAT_NODE);
			put("LOGIN_LAT_GROUP", LOGIN_LAT_GROUP);
			put("FRAMED_APPLETALK_LINK", FRAMED_APPLETALK_LINK);
			put("FRAMED_APPLETALK_NETWORK", FRAMED_APPLETALK_NETWORK);
			put("FRAMED_APPLETALK_ZONE", FRAMED_APPLETALK_ZONE);
			put("CHAP_CHALLENGE", CHAP_CHALLENGE);
			put("NAS_PORT_TYPE", NAS_PORT_TYPE);
			put("PORT_LIMIT", PORT_LIMIT);
			put("LOGIN_LAT_PORT", LOGIN_LAT_PORT);
		}
	});

	/**
	 * get attribute value from string
	 * 
	 * @param attribute
	 *            string representation of attribute
	 * @return numeric attribute value or 0 if invalid
	 */
	public static byte getAttribute(final String attribute) {
		if (attributeMap.containsKey(attribute)) {
			return attributeMap.get(attribute);
		} else {
			return 0;
		}
	}

}

/**
 * This class defines an attribute value pair for RADIUS
 * 
 * @author Jan N. Klug
 * @since 1.8.0
 */

class RadiusAVP {
	private byte attribute;
	private byte[] value;

	public RadiusAVP(byte attribute, String value) {
		this.attribute = attribute;
		this.value = value.getBytes();
	}

	public RadiusAVP(byte attribute, byte[] value) {
		this.attribute = attribute;
		this.value = value;
	}

	public byte length() {
		return (byte) (2 + value.length);
	}

	public byte getType() {
		return attribute;
	}

	public byte[] getRawValue() {
		return value;
	}

	public String getValueString() {
		return new String(value);
	}

	public long getValueLong() {
		long ret = 0;
		for (int i = 0; i < value.length; i++) {
			ret = (ret << 8) + (value[i] & 0xff);
		}
		return ret;
	}

	public byte[] getAVPBytes() {
		byte length = length();
		byte[] avpBytes = new byte[length];
		avpBytes[0] = attribute;
		avpBytes[1] = length;
		System.arraycopy(value, 0, avpBytes, 2, value.length);
		return avpBytes;
	}

	public String toString() {
		String s = "";
		for (byte b : getAVPBytes()) {
			s += String.format("%02X ", b);
		}
		return s;
	}
}

/**
 * This class defines a RADIUS packet
 * 
 * @author Jan N. Klug
 * @since 1.8.0
 */

class RadiusPacket {
	private byte typeCode;
	private byte identifier;
	private byte[] authenticator;
	private ArrayList<RadiusAVP> avps;

	private static byte nextIdentifier = 0;

	/**
	 * initialize new packet of type {@code typeCode}
	 * 
	 * @param typeCode
	 *            a valid RADIUS packet type code
	 */
	public RadiusPacket(byte typeCode) {
		authenticator = new byte[16];
		System.arraycopy(String.format("%16d", System.currentTimeMillis() / 1000L).getBytes(), 0, authenticator, 0, 16);
		this.typeCode = typeCode;
		identifier = nextIdentifier++;
		avps = new ArrayList<RadiusAVP>();
	}

	/**
	 * parse an existing raw packet
	 * 
	 * @param rawData
	 *            a valid full RADIUS packet
	 */
	public RadiusPacket(byte[] rawData) {
		int len = rawData[2] * 256 + rawData[3];
		int cnt = 20;
		typeCode = rawData[0];
		identifier = rawData[1];
		authenticator = new byte[16];
		System.arraycopy(rawData, 4, authenticator, 0, 16);
		avps = new ArrayList<RadiusAVP>();
		while (cnt < len) {
			byte alen = rawData[cnt + 1];
			byte attribute = rawData[cnt];
			byte[] value = new byte[alen - 2];
			System.arraycopy(rawData, cnt + 2, value, 0, alen - 2);
			addAttribute(new RadiusAVP(attribute, value));
			cnt += alen;
		}
	}

	/**
	 * get the RADIUS type code of this packet
	 * 
	 * @return the RADIUS type code
	 */
	public byte getTypeCode() {
		return typeCode;
	}

	/**
	 * add a new attribute to this RADIUS packet
	 * 
	 * @param avp
	 *            a valid RADIUS attribute-value pair
	 */
	public void addAttribute(RadiusAVP avp) {
		avps.add(avp);
	}

	/**
	 * add special attribute PAP password
	 * 
	 * @param password
	 *            clear-text password
	 * @param secret
	 *            shared secret with the server
	 */
	public void encodePapPassword(String password, String secret) {
		int length;
		byte[] bn = new byte[16];
		MessageDigest md5;

		// get length as multiple of 16, maximum 128
		if (password.length() % 16 == 0) {// already multiple of 16
			length = password.length();
		} else {
			length = ((int) (password.length() / 16) + 1) * 16;
		}
		if (length > 128) {
			length = 128;
		}
		// copy old value and pad
		byte[] value = new byte[length];
		System.arraycopy(password.getBytes(), 0, value, 0, password.length());
		for (int i = password.length(); i < length; i++) {
			value[i] = 0;
		}
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(secret.getBytes());
			md5.update(authenticator);
			for (int i = 0; i < length; i += 16) {
				bn = md5.digest();
				md5.reset();
				md5.update(secret.getBytes());
				for (int j = 0; j < 16; j++) {
					value[i + j] = (byte) (value[i + j] ^ bn[j]);
					md5.update(value[i + j]);
				}
			}
			addAttribute(new RadiusAVP(RadiusAttribute.USER_PASSWORD, value));
		} catch (NoSuchAlgorithmException e) {
		}
	}

	/**
	 * show RADIUS attributes of this package
	 * 
	 * @return hex string of all attributes (one per line)
	 */
	public String showAttributes() {
		String s = "";
		for (RadiusAVP avp : avps) {
			s += avp.toString() + "\n";
		}
		return s;
	}

	/**
	 * get total length of the full RADIUS packet
	 * 
	 * @return packet length
	 */
	public int getRawPacketLength() {
		int length = 20; // 1 byte type, 1 byte identifier, 2 bytes length, 16 bytes authentictor
		for (RadiusAVP avp : avps) {
			length += avp.length();
		}
		return length;
	}

	/**
	 * get the full RADIUS packet as byte-array
	 * 
	 * @return full packet data
	 */
	public byte[] getRawPacket() {
		int next = 20;
		int length = getRawPacketLength();

		byte[] packet = new byte[length];

		packet[0] = typeCode;
		packet[1] = identifier;
		packet[2] = (byte) (length / 256);
		packet[3] = (byte) (length % 256);
		System.arraycopy(authenticator, 0, packet, 4, 16);

		for (RadiusAVP avp : avps) {
			int len = avp.length();
			System.arraycopy(avp.getAVPBytes(), 0, packet, next, len);
			next += len;
		}

		return packet;
	}

	/**
	 * get a attribute value pair from this packet
	 * 
	 * @param attributeCode
	 *            of attribute
	 * @return attribute-value pair or null if not found
	 */
	public RadiusAVP getAttribute(byte attributeCode) {
		for (RadiusAVP avp : avps) {
			if (avp.getType() == attributeCode) {
				return avp;
			}
		}
		return null;
	}

	/**
	 * check if attribute exists in this package
	 * 
	 * @param attributeCode
	 *            attribute to check
	 * @return true if exists
	 */
	public boolean hasAttribute(byte attributeCode) {
		for (RadiusAVP avp : avps) {
			if (avp.getType() == attributeCode) {
				return true;
			}
		}
		return false;
	}

	/**
	 * convert full packet to string
	 * 
	 * @return hex string and total number of bytes
	 */
	public String toString() {
		String s = "";
		for (byte b : getRawPacket()) {
			s += String.format("%02X ", b);
		}
		s += "(" + getRawPacketLength() + " b)";
		return s;
	}

}
