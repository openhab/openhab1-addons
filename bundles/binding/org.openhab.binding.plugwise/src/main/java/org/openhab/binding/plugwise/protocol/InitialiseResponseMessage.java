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
package org.openhab.binding.plugwise.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * Once the network is initialized we will get some useful information back from the Stick, such as the MAC address of the 
 * Circle+ driving the network
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class InitialiseResponseMessage extends Message {
	
	boolean online;
	String networkID;
	String unknown1;
	String unknown2;
	String shortNetworkID;
	String circlePlusMAC;

	public InitialiseResponseMessage(int sequenceNumber, String payLoad) {
		super(sequenceNumber,payLoad);
		type = MessageType.INITIALISE_RESPONSE;
	}

	public InitialiseResponseMessage(String payLoad) {
		super(payLoad);
		type = MessageType.INITIALISE_RESPONSE;
	}

	public String getMAC() {
		return MAC;
	}

	public boolean isOnline() {
		return online;
	}

	public String getNetworkID() {
		return networkID;
	}
	
	public String getCirclePlusMAC(){
		return circlePlusMAC;
	}

	@Override
	protected void parsePayLoad() {

		Pattern RESPONSE_PATTERN = Pattern.compile("(\\w{16})(\\w{2})(\\w{2})(\\w{16})(\\w{4})(\\w{2})");

		Matcher matcher = RESPONSE_PATTERN.matcher(payLoad);
		if(matcher.matches()){
			MAC = matcher.group(1);
			unknown1 = matcher.group(2);
			online = (Integer.parseInt(matcher.group(3), 16) == 1);
			networkID = matcher.group(4);
			shortNetworkID = matcher.group(5);
			unknown2 = matcher.group(6);
			
			// now some serious protocol reverse-engineering assumption. Circle+ MAC = networkID with first two bytes replaced by 00
			circlePlusMAC = "00" + StringUtils.right(networkID, StringUtils.length(networkID)-2);					
		}
		else {
			logger.debug("Plugwise protocol InitialiseResponse error: {} does not match", payLoad);
		}
		
	}

	@Override
	protected String payLoadToHexString() {
		return unknown1 + String.format("%02X",online ? "1" : "0") + networkID + shortNetworkID + unknown2;
	}

}
