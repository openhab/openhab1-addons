/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekey.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

import at.fhooe.mc.schlgtwt.parser.HomePacket;
import at.fhooe.mc.schlgtwt.parser.MultiPacket;
import at.fhooe.mc.schlgtwt.parser.RarePacket;
import at.fhooe.mc.schlgtwt.parser.UniformPacket;

/**
 * This class stores binding configuration information containing
 * the itemtype (Number- or StringItem) and the field that this item
 * is interested in. See enum InterestType 
 * @author Paul Schlagitweit
 * @since 1.5.0
 */
public class EKeyBindingConfig implements BindingConfig {
	public enum InterestType {
		// int str int str str int int int int int
		ACTION, USERNAME, USERID, USERSTATUS, TERMINALID, TERMINALNAME, FINGERID, KEYID, INPUTID, RELAYID, MODE;

		/**
		 * Gets the enum-type for string (the string "ACTION" will
		 * return InterestType.ACTION)
		 * @param s
		 * @return
		 * @throws Exception
		 */
		public static InterestType getType(String s) throws Exception {
			return Enum.valueOf(InterestType.class, s);
		}

	}

	/**
	 * Stores the interest type (see enum values)
	 */
	public InterestType interestedIn;

	/**
	 * Stores the type of the item (Number- or StringItem)
	 */
	Class<? extends Item> itemType;
	
	

	/**
	 * This gets the interesting data of an ekey packet.
	 * 
	 * @param packet
	 *            can be of type rare, multi or home
	 * @param interest
	 *            see the interesttypes in the enum definition
	 * @return returns the corresponding value or null if the packet doesn't
	 *         provide this value
	 */
	public static Object getValueOfInterest(UniformPacket packet,
			InterestType interest) {

		// uniform data fields

		if (interest == InterestType.USERID) {
			return packet.getUserID();
		} else if (interest == InterestType.ACTION) {
			return packet.getAction();
		} else if (interest == InterestType.TERMINALID) {
			return packet.getFsSerial();
		} else if (interest == InterestType.FINGERID) {
			return packet.getFingerID();
		} else if (interest == InterestType.MODE) {
			return packet.getProtocolMode();
		} else

		// multi data fields

		if (packet instanceof MultiPacket) {

			if (interest == InterestType.KEYID)
				return ((MultiPacket) packet).getKeyID();
			else if (interest == InterestType.INPUTID)
				return ((MultiPacket) packet).getInputID();
			else if (interest == InterestType.USERNAME)
				return ((MultiPacket) packet).getUsername();
			else if (interest == InterestType.TERMINALNAME)
				return ((MultiPacket) packet).getFsName();
			else if (interest == InterestType.USERSTATUS)
				return ((MultiPacket) packet).getUserstatus();

		} else

		// home data fields

		if (packet instanceof HomePacket) {
			if (interest == InterestType.RELAYID)
				return ((HomePacket) packet).getRelayID();
		} else
		// rare data fields
		if (packet instanceof RarePacket) {

			if (interest == InterestType.RELAYID)
				return ((RarePacket) packet).getRelayID();
		}

		return null;
	}



}