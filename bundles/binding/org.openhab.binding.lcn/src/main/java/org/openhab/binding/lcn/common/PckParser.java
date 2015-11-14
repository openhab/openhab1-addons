/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.common;

import java.util.regex.Pattern;

/**
 * Helpers to parse LCN-PCK commands.
 * <p>
 * LCN-PCK is the command-syntax used by LCN-PCHK to send and receive LCN commands.
 * 
 * @author Tobias Jüttner
 */
public final class PckParser {
	
	/** Authentication at LCN-PCHK: Request user name. */
	public static final String AUTH_USERNAME = "Username:";
	
	/** Authentication at LCN-PCHK: Request password. */
	public static final String AUTH_PASSWORD = "Password:";
	
	/** Authentication at LCN-PCHK succeeded. */ 
	public static final String AUTH_OK = "OK";
	
	/** LCN-PK/PKU is connected. */
	public static final String LCNCONNSTATE_CONNECTED = "$io:#LCN:connected";
	
	/** LCN-PK/PKU is disconnected. */
	public static final String LCNCONNSTATE_DISCONNECTED = "$io:#LCN:disconnected";
	
	/** Pattern to parse positive acknowledges. */
	public static final Pattern PATTERN_ACK_POS =
		Pattern.compile("-M(?<segId>\\d{3})(?<modId>\\d{3})!");
	
	/** Pattern to parse negative acknowledges. */
	public static final Pattern PATTERN_ACK_NEG =
		Pattern.compile("-M(?<segId>\\d{3})(?<modId>\\d{3})(?<code>\\d+)");
	
	/** Pattern to parse segment coupler responses. */
	public static final Pattern PATTERN_SK_RESPONSE =
		Pattern.compile("=M(?<segId>\\d{3})(?<modId>\\d{3})\\.SK(?<id>\\d+)");
	
	/** Pattern to parse serial number and firmware date responses. */
	public static final Pattern PATTERN_SN =
		Pattern.compile("=M(?<segId>\\d{3})(?<modId>\\d{3}).SN(?<sn>[0-9|A-F]{10})(?<manu>[0-9|A-F]{2})FW(?<swAge>[0-9|A-F]{6})HW(?<hwType>\\d+)");
	
	/** Pattern to parse output-port status responses in percent. */
	public static final Pattern PATTERN_STATUS_OUTPUT_PERCENT =
		Pattern.compile(":M(?<segId>\\d{3})(?<modId>\\d{3})A(?<outputId>\\d)(?<percent>\\d+)");		
	
	/** Pattern to parse output-port status responses in native format (0..200). */
	public static final Pattern PATTERN_STATUS_OUTPUT_NATIVE =
		Pattern.compile(":M(?<segId>\\d{3})(?<modId>\\d{3})O(?<outputId>\\d)(?<value>\\d+)");
	
	/** Pattern to parse relays status responses. */
	public static final Pattern PATTERN_STATUS_RELAYS =
		Pattern.compile(":M(?<segId>\\d{3})(?<modId>\\d{3})Rx(?<byteValue>\\d+)");
	
	/** Pattern to parse binary-sensors status responses. */
	public static final Pattern PATTERN_STATUS_BINSENSORS =
		Pattern.compile(":M(?<segId>\\d{3})(?<modId>\\d{3})Bx(?<byteValue>\\d+)");
	
	/** Pattern to parse variable 1-12 status responses (since 170206). */
	public static final Pattern PATTERN_STATUS_VAR =
		Pattern.compile("%M(?<segId>\\d{3})(?<modId>\\d{3})\\.A(?<id>\\d{3})(?<value>\\d+)");
	
	/** Pattern to parse set-point variable status responses (since 170206). */
	public static final Pattern PATTERN_STATUS_SETVAR =
		Pattern.compile("%M(?<segId>\\d{3})(?<modId>\\d{3})\\.S(?<id>\\d)(?<value>\\d+)");
	
	/** Pattern to parse threshold status responses (since 170206). */
	public static final Pattern PATTERN_STATUS_THRS =
		Pattern.compile("%M(?<segId>\\d{3})(?<modId>\\d{3})\\.T(?<registerId>\\d)(?<thrsId>\\d)(?<value>\\d+)");
	
	/** Pattern to parse S0-input status responses (LCN-BU4L). */
	public static final Pattern PATTERN_STATUS_S0INPUT =
		Pattern.compile("%M(?<segId>\\d{3})(?<modId>\\d{3})\\.C(?<id>\\d)(?<value>\\d+)");
	
	/** Pattern to parse generic variable status responses (concrete type unknown, before 170206). */
	public static final Pattern PATTERN_VAR_GENERIC =
		Pattern.compile("%M(?<segId>\\d{3})(?<modId>\\d{3})\\.(?<value>\\d+)");
	
	/** Pattern to parse threshold register 1 status responses (5 values, before 170206). */
	public static final Pattern PATTERN_THRS5 =
		Pattern.compile("=M(?<segId>\\d{3})(?<modId>\\d{3})\\.S1(?<value1>\\d{5})(?<value2>\\d{5})(?<value3>\\d{5})(?<value4>\\d{5})(?<value5>\\d{5})(?<hyst>\\d{5})");
	
	/** Pattern to parse status of LEDs and logic-operations responses. */
	public static final Pattern PATTERN_STATUS_LEDSANDLOGICOPS =
		Pattern.compile("=M(?<segId>\\d{3})(?<modId>\\d{3})\\.TL(?<ledStates>[AEBF]{12})(?<logicOpStates>[NTV]{4})");
	
	/** Pattern to parse key-locks status responses. */
	public static final Pattern PATTERN_STATUS_KEYLOCKS =
		Pattern.compile("=M(?<segId>\\d{3})(?<modId>\\d{3})\\.TX(?<table0>\\d{3})(?<table1>\\d{3})(?<table2>\\d{3})((?<table3>\\d{3}))?");
	
	/**
	 * Generates an array of booleans from an input integer (actually a byte).
	 * 
	 * @param input the input byte (0..255)
	 * @return the array of 8 booleans
	 * @throws IllegalArgumentException if input is out of range (not a byte)
	 */
	public static boolean[] getBooleanValue(int inputByte) throws IllegalArgumentException {
		if (inputByte < 0 || inputByte > 255) {
			throw new IllegalArgumentException();
		}
		boolean[] result = new boolean[8];
		for (int i = 0; i < 8; ++i) {
			result[i] = (inputByte & (1 << i)) != 0;
		}
		return result;
	}
	
}
