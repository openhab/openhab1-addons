/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
 * Circle Information response message
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class InformationResponseMessage extends Message {	
	
	private int year;
	private int month;
	private int minutes;
	private int logAddress;
	private boolean powerState;
	private int hertz;
	private String hardwareVersion;
	private int firmwareVersion;
	@SuppressWarnings("unused")
	private int unknown;
	
	public InformationResponseMessage(int sequenceNumber, String payLoad) {
		super(sequenceNumber, payLoad);
		type = MessageType.DEVICE_INFORMATION_RESPONSE;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getLogAddress() {
		return logAddress;
	}

	public boolean getPowerState() {
		return powerState;
	}

	public int getHertz() {
		return (hertz == 133) ? 50 : 60 ;
	}

	public String getHardwareVersion() {
		return hardwareVersion;
	}

	public int getFirmwareVersion() {
		return firmwareVersion;
	}

	@Override
	protected String payLoadToHexString() {
		return payLoad;
	}

	@Override
	protected void parsePayLoad() {
		Pattern RESPONSE_PATTERN = Pattern.compile("(\\w{16})(\\w{2})(\\w{2})(\\w{4})(\\w{8})(\\w{2})(\\w{2})(\\w{12})(\\w{8})(\\w{2})");

		Matcher matcher = RESPONSE_PATTERN.matcher(payLoad);
		if(matcher.matches()) {
			MAC = matcher.group(1);
			year = Integer.parseInt(matcher.group(2), 16) + 2000;	
			month = Integer.parseInt(matcher.group(3), 16);	
			minutes = Integer.parseInt(matcher.group(4), 16);	
			logAddress = (Integer.parseInt(matcher.group(5), 16) - 278528) / 8;	
			powerState = ( matcher.group(6).equals("01"));
			hertz = Integer.parseInt(matcher.group(7), 16);	
			hardwareVersion = StringUtils.left(matcher.group(8), 4) + "-" + StringUtils.mid(matcher.group(8), 4, 4) + "-" + StringUtils.right(matcher.group(8), 4);
			firmwareVersion = Integer.parseInt(matcher.group(9), 16);
			unknown = Integer.parseInt(matcher.group(10), 16);
		} else {
			logger.debug("Plugwise protocol RoleCallResponseMessage error: {} does not match", payLoad);
		}
	}

}
