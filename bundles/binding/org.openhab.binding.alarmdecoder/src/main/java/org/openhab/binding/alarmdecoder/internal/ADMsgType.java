/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.alarmdecoder.internal;

import java.util.HashMap;

/**
 * The various message types that come from the ad2usb/ad2pi interface
 * @author Bernd Pfrommer
 * @since 1.6.0
 */
public enum ADMsgType {
	KPM(0),			// keypad message
	RFX(1),			// wireless message
	EXP(2),			// zone expander message
	REL(3),			// relay message
	INVALID(4),		// invalid message
	NUMTYPES(5);	// total number of types
	
	private final int m_type; 

	ADMsgType(int i) {
		m_type = i;
	}
	/**
	 * 
	 * @return corresponding integer value
	 */
	public int getValue() {
		return m_type;
	}
	/**
	 * 
	 * @return true if it is a valid message type
	 */
	public boolean isValid() {
		return (this != INVALID && this != NUMTYPES) ;
	}
	
	/**
	 * Determine the message type from a 3-letter string
	 * @param s the 3-letter string
	 * @return the message type (potentially INVALID)
	 */
	public static ADMsgType s_fromString(String s) {
		ADMsgType mt = s_strToType.get(s);
		if (mt == null) return s_strToType.get("INVALID");
		return mt;
	}
	/**
	 * Test if 3-letter string is a message type
	 * @param s string to test
	 * @return true if string is a valid message type
	 */
	public static boolean s_isValid(String s) {
		return (s_fromString(s).isValid());
	}
	
	/** hash map from string to type */
	private static HashMap<String, ADMsgType> s_strToType;
	
	static {
		s_strToType = new HashMap<String, ADMsgType>();
		s_strToType.put("KPM",  KPM);
		s_strToType.put("RFX",  RFX);
		s_strToType.put("EXP",  EXP);
		s_strToType.put("REL",  REL);
		s_strToType.put("INVALID",  INVALID);
	}
}
