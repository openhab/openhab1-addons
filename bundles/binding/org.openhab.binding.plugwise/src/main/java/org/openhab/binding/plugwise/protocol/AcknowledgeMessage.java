/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Acknowledge message class - ACKs are used in the PW protocol to serve different means, from acknowledging a message
 * sent to the stick by the host, as well as confirmation messages from Circles in the network for various purposes
 * Not all purposes are yet reverse-engineered
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class AcknowledgeMessage extends Message {
	
	public enum ExtensionCode {
		NOTEXTENDED(0),
		SUCCESS(193),
		ERROR(194),
		CIRCLEPLUS(221),
		CLOCKSET(215),
		ON(216),
		OFF(222),
		TIMEOUT(225),
		UNKNOWN(999);
		
		private int identifier;

		private ExtensionCode(int value) {
				identifier = value;
		}
		
	    private static final Map<Integer, ExtensionCode> typesByValue = new HashMap<Integer, ExtensionCode>();

	    static {
	        for (ExtensionCode type : ExtensionCode.values()) {
	            typesByValue.put(type.identifier, type);
	        }
	    }
		
	    public static ExtensionCode forValue(int value) {
	        return typesByValue.get(value);
	    }
	    
	    public int toInt() {
	    	return identifier;
	    }
	}
	
	private ExtensionCode code;
	private String extendedMAC = "";

	public AcknowledgeMessage(int sequenceNumber, String payLoad) {
		super(sequenceNumber, payLoad);
		type = MessageType.ACKNOWLEDGEMENT;
		MAC = "";
	}

	public AcknowledgeMessage(String payLoad) {
		super(payLoad);
		type = MessageType.ACKNOWLEDGEMENT;
		MAC = "";
	}

	@Override
	protected void parsePayLoad() {
		
		Pattern SHORT_RESPONSE_PATTERN = Pattern.compile("(\\w{4})");
		Pattern EXTENDED_RESPONSE_PATTERN = Pattern.compile("(\\w{4})(\\w{16})");

		Matcher shortMatcher = SHORT_RESPONSE_PATTERN.matcher(payLoad);
		Matcher extendedMatcher = EXTENDED_RESPONSE_PATTERN.matcher(payLoad);

		if (extendedMatcher.matches()) {
			code = ExtensionCode.forValue(Integer.parseInt(extendedMatcher.group(1),16));
			if(code==null) {
				code = ExtensionCode.UNKNOWN;
			}
			extendedMAC = extendedMatcher.group(2);
		} else if(shortMatcher.matches()){
			code = ExtensionCode.forValue(Integer.parseInt(shortMatcher.group(1),16));
			if(code==null) {
				code = ExtensionCode.UNKNOWN;
			}
		} 
		else {
			logger.debug("Plugwise protocol AcknowledgeMessage error: {} does not match", payLoad);
			code = ExtensionCode.UNKNOWN;
		}
	}
	
	public boolean isSuccess() {
		if(code == ExtensionCode.SUCCESS ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isError() {
		if(code == ExtensionCode.ERROR ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isTimeOut() {
		if(code == ExtensionCode.TIMEOUT ) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isExtended() {
		
		if(code!=ExtensionCode.NOTEXTENDED && code!=ExtensionCode.SUCCESS && code!=ExtensionCode.ERROR) {
			return true;
		} else {
			return false;
		}
	}
	
	public ExtensionCode getExtensionCode() {
		if(isExtended()) {
			return code;
		} else {
			return ExtensionCode.NOTEXTENDED;
		}
			
	}

	public String getExtendedMAC(){
		if(isExtended()) {
			return extendedMAC;
		} else {
			return null;
		}
	}
	
	public String getCirclePlusMAC(){
		if(isExtended() && code == ExtensionCode.CIRCLEPLUS) {
			return extendedMAC;
		} else {
			return null;
		}
	}
	
	public boolean isOn(){
		if(isExtended() && code == ExtensionCode.ON) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isOff(){
		if(isExtended() && code == ExtensionCode.OFF) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public String getPayLoad() {
		return payLoadToHexString();
	}

	@Override
	protected String payLoadToHexString() {
		
		switch(code) {
		case CIRCLEPLUS:
			return String.format("%04X",code.toInt()) + extendedMAC;
		case ON:
			return String.format("%04X",code.toInt()) + extendedMAC;
		case OFF:
			return String.format("%04X",code.toInt()) + extendedMAC;
		default:
			return String.format("%04X",code.toInt());
			
		}
		
	}

}
