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
package org.openhab.binding.maxcube.internal.message;

import java.util.Calendar;

import org.openhab.binding.maxcube.internal.Utils;


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
	public String debug() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("=== H_Message === ");
		sb.append("\tRAW:" + this.getPayload());
		sb.append("\tReading Time: " + cal.getTime());
		sb.append("\tSerial number:  " + rawSerialNumber);
		sb.append("\tRF address (HEX):" + rawRfHexAddress);
		sb.append("\tFirmware version:" + rawFirmwareVersion);
		sb.append("\tConnection ID: " + rawConnectionId);
		
		return sb.toString();
	}

	@Override
	public MessageType getType() {
		return MessageType.H;
	}
}