/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
