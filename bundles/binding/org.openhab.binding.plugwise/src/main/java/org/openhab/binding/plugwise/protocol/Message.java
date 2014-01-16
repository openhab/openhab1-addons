/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.protocol;

import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openhab.binding.plugwise.protocol.MessageType;

/**
 * Base class to represent Plugwise protocol data units
 * 
 * In general a message consists of a hex string containing the following parts:
 * a type indicator - many types are yet to be reverse engineered
 * a sequence number - messages in PW are numbered so that we can keep track of them in an application
 * a PW MAC address - basically, the destination of the message
 * a payload
 * a CRC checksum that is calculated using the previously mentioned segments of the message
 * 
 * Before sending off a message in the PW network they are prepended with a protocol header and trailer is
 * added at the end
 * 
 * 
 * @author Karel Goderis
 * @since 1.1.0
 */
public abstract class Message {
	
	protected static final Logger logger = LoggerFactory.getLogger(Message.class);

	protected MessageType type;
	protected int sequenceNumber = 0;
	protected String payLoad;
	protected String MAC;
	
	// to keep track how many attempts we have made to send this message
	protected int retryCount = 0;
	
	// for messages where the MAC address is part of the payload
	public Message( int sequenceNumber,  String payLoad) {
		this.sequenceNumber = sequenceNumber;
		this.payLoad = payLoad;
		
		parsePayLoad();
	}
	
	// for messages where we do not care about the MAC address
	public Message(String payLoad) {
		this.payLoad = payLoad;

		if(payLoad != null) {
			parsePayLoad();
		}	
	}
	
	public Message(int sequenceNumber, String MAC,String payLoad) {
		this.payLoad = payLoad;
		this.sequenceNumber = sequenceNumber;
		this.MAC = MAC;
		
		if(payLoad != null) {
			parsePayLoad();
		}
	}
	
	// for messages where we do not care about the sequence number
	public Message(String MAC,String payLoad) {
		this.payLoad = payLoad;
		this.MAC = MAC;
		
		if(payLoad != null) {
			parsePayLoad();
		}
	}
	
	public void increaseAttempts() {
		retryCount++;
	}
	
	public int getAttempts() {
		return retryCount;
	}

	public MessageType getType() {
		return type;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getPayLoad() {
		return payLoad;
	}
	
	public String getMAC() {
		return MAC;
	}

	protected String sequenceNumberToHexString() {
		return String.format("%04X", sequenceNumber);
	}
	
	abstract protected String payLoadToHexString();
	
	
	private String MACToHexString() {
		return getMAC();
	}
	
	private String typeToHexString() {
		return String.format("%04X", type.toInt());
	}
	
	// Method that implementation classes have to override, and that is responsible for parsing the payload into meaningful fields
	abstract protected void parsePayLoad();
	
	public String toHexString() {
		String result = null ;
		result =  typeToHexString() + sequenceNumberToHexString() + MACToHexString() + payLoadToHexString();
		String CRC = getCRC(result);
		result = result + CRC;
		
		return result;
	}
	
	protected String getCRC(String buffer) {
		
		int crc = 0x0000;
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12) 

        byte[] bytes = new byte[0];
		try {
			bytes = buffer.getBytes("ASCII");
		} catch (UnsupportedEncodingException e) {
			return "";
		}

        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15    & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
             }
        }

        crc &= 0xffff;
		
		return(String.format("%04X", crc).toUpperCase());
	}
	
}
