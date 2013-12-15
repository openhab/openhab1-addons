/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.util.Calendar;

import org.openhab.binding.maxcube.internal.Utils;
import org.slf4j.Logger;


/**
* The H message contains information about the MAX!Cube. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public final class H_Message extends Message {
	
	private Calendar cal = Calendar.getInstance();
	
	private String rawSerialNumber = null;
	private String rawRfHexAddress = null;
	private String rawFirmwareVersion = null;
	private String rawConnectionId = null;
	private String rawSystemDate = null;
	private String rawSystemTime = null;
	
	// yet unknown fields
	private String field4 = null;
	private String field6 = null;
	private String field7 = null;
	private String field8 = null;
	private String field10 = null;
	private String field11 = null;

	public H_Message(String raw) {
		super(raw);

		String[] tokens = this.getPayload().split(Message.DELIMETER);

		if (tokens.length < 11)
		{
			throw new ArrayIndexOutOfBoundsException("MAX!Cube raw H_Message corrupt");
		}
		
		rawSerialNumber = tokens[0];
		rawRfHexAddress = tokens[1];
		rawFirmwareVersion = tokens[2];
		rawConnectionId = tokens[4];
		
		setDateTime(tokens[7], tokens[8]);
	}

	private final void setDateTime(String hexDate, String hexTime) {
		
		int year = Utils.fromHex(hexDate.substring(0,2));
		int month = Utils.fromHex(hexDate.substring(2, 4));
		int date = Utils.fromHex(hexDate.substring(4, 6));
		
		int hours = Utils.fromHex(hexTime.substring(0, 2));
		int minutes = Utils.fromHex(hexTime.substring(2, 4));

		cal.set(year, month, date, hours, minutes, 0);
	}
	
	@Override
	public void debug(Logger logger) {
		logger.debug("=== H_Message === ");
		logger.debug("\tRAW:             " + this.getPayload());
		logger.debug("\tReading Time:    " + cal.getTime());
		logger.debug("\tSerial number:   " + rawSerialNumber);
		logger.debug("\tRF address (HEX):" + rawRfHexAddress);
		logger.debug("\tFirmware version:" + rawFirmwareVersion);
		logger.debug("\tConnection ID:   " + rawConnectionId);
	}

	@Override
	public MessageType getType() {
		return MessageType.H;
	}
}