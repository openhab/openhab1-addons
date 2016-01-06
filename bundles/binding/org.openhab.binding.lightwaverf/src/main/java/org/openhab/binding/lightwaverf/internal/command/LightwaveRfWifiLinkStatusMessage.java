/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.command;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;


/**
 * A message received from the Wifi Link with Status Info.
 *  On the LAN the messages look like:  
 * *!{"trans":452,"mac":"ab:cd:ef","time":1447712274,"type":"hub","prod":"wfl","fw":"U2.91Y",
 * "uptime":1386309,"timeZone":0,"lat":52.48,"long":-87.89,"duskTime":1447690400,
 * "dawnTime":1447659083,"tmrs":0,"evns":1,"run":0,"macs":8,"ip":"192.168.0.1","devs":0}
 * 
 * @author Neil Renaud
 * @since 1.8.0
 */
public class LightwaveRfWifiLinkStatusMessage extends AbstractLightwaveRfJsonMessage implements LightwaveRfSerialMessage {

	private static final String WIFI_LINK_SERIAL = "wifilink";
	
	private static final Pattern MAC_ID_REG_EXP = Pattern
			.compile(".*\"mac\":\"([^\"}]*)\".*");
	private static final Pattern TIME_ID_REG_EXP = Pattern
			.compile(".*\"time\":([^,}]*).*");
	private static final Pattern TYPE_REG_EXP = Pattern
			.compile(".*\"type\":\"([^\"}]*)\".*");
	private static final Pattern PROD_REG_EXP = Pattern
			.compile(".*\"prod\":\"([^\"}]*)\".*");
	private static final Pattern FIRMWARE_REG_EXP = Pattern
			.compile(".*\"fw\":\"([^\"}]*)\".*");
	private static final Pattern UPTIME_REG_EXP = Pattern
			.compile(".*\"uptime\":([^,}]*).*");
	private static final Pattern TIMEZONE_REG_EXP = Pattern
			.compile(".*\"timeZone\":([^,}]*).*");
	private static final Pattern LATITUDE_REG_EXP = Pattern
			.compile(".*\"lat\":([^,}]*).*");
	private static final Pattern LONGITUDE_REG_EXP = Pattern
			.compile(".*\"long\":([^,}]*).*");
	private static final Pattern DUSK_TIME_REG_EXP = Pattern
			.compile(".*\"duskTime\":([^,}]*).*");
	private static final Pattern DAWN_TIME_REG_EXP = Pattern
			.compile(".*\"dawnTime\":([^,}]*).*");
	private static final Pattern TMRS_REG_EXP = Pattern
			.compile(".*\"tmrs\":([^,}]*).*");
	private static final Pattern ENVS_REG_EXP = Pattern
			.compile(".*\"evns\":([^,}]*).*");
	private static final Pattern RUN_REG_EXP = Pattern
			.compile(".*\"run\":([^,}]*).*");
	private static final Pattern MACS_REG_EXP = Pattern
			.compile(".*\"macs\":([^,}]*).*");
	private static final Pattern IP_REG_EXP = Pattern
			.compile(".*\"ip\":\"([^\"}]*)\".*");
	private static final Pattern DEVS_REG_EXP = Pattern
			.compile(".*\"devs\":([^,}]*).*");

	
	private final String mac;
	private final Date time;
	private final String type;
	private final String prod;
	private final String firmware;
	private final long uptime;
	private final String timeZone;
	private final String latitude;
	private final String longitude;
	private final Date duskTime;
	private final Date dawnTime;
	private final String tmrs;
	private final String envs;
	private final String run;
	private final String macs;
	private final String ip;
	private final String devs;
	
	public LightwaveRfWifiLinkStatusMessage(String message) throws LightwaveRfMessageException {
		super(message);
		mac = getStringFromText(MAC_ID_REG_EXP, message);
		time = getDateFromText(TIME_ID_REG_EXP, message);
		prod = getStringFromText(PROD_REG_EXP, message);
		type = getStringFromText(TYPE_REG_EXP, message);
		firmware = getStringFromText(FIRMWARE_REG_EXP, message);
		uptime = getLongFromText(UPTIME_REG_EXP, message);
		timeZone = getStringFromText(TIMEZONE_REG_EXP, message);
		latitude = getStringFromText(LATITUDE_REG_EXP, message);
		longitude = getStringFromText(LONGITUDE_REG_EXP, message);
		duskTime = getDateFromText(DUSK_TIME_REG_EXP, message);
		dawnTime = getDateFromText(DAWN_TIME_REG_EXP, message);
		tmrs = getStringFromText(TMRS_REG_EXP, message);
		envs = getStringFromText(ENVS_REG_EXP, message);
		run = getStringFromText(RUN_REG_EXP, message);
		macs = getStringFromText(MACS_REG_EXP, message);
		ip = getStringFromText(IP_REG_EXP, message);
		devs = getStringFromText(DEVS_REG_EXP, message);
	}
	
	@Override
	public String getLightwaveRfCommandString() {
		return new StringBuilder("*!{")
				.append("\"trans\":").append(getMessageId().getMessageIdString())
				.append(",\"mac\":\"").append(mac)
				.append("\",\"time\":").append(getLightwaveDateFromJavaDate(time))
				.append(",\"type\":\"").append(type)
				.append(",\"prod\":\"").append(prod)
				.append(",\"fw\":\"").append(firmware)
				.append(",\"uptime\":\"").append(uptime)
				.append(",\"timeZone\":\"").append(timeZone)
				.append(",\"lat\":\"").append(latitude)
				.append(",\"long\":\"").append(longitude)
				.append(",\"duskTime\":\"").append(getLightwaveDateFromJavaDate(duskTime))
				.append(",\"dawnTime\":\"").append(getLightwaveDateFromJavaDate(dawnTime))
				.append(",\"tmrs\":\"").append(tmrs)
				.append(",\"evns\":\"").append(envs)
				.append(",\"run\":\"").append(run)
				.append(",\"macs\":\"").append(macs)
				.append(",\"ip\":\"").append(ip)
				.append(",\"devs\":\"").append(devs)
				.append("}").toString();
	}

	@Override
	public State getState(LightwaveRfType type) {
		switch (type) {
		case WIFILINK_IP:
			return new StringType(ip);
		case WIFILINK_FIRMWARE:
			return new StringType(firmware);
		case WIFILINK_DUSK_TIME:
			Calendar calDusk = Calendar.getInstance();
			calDusk.setTime(duskTime);
			return new DateTimeType(calDusk);
		case WIFILINK_DAWN_TIME:
			Calendar calDawn = Calendar.getInstance();
			calDawn.setTime(dawnTime);
			return new DateTimeType(calDawn);
		case WIFILINK_UPTIME:
			return new DecimalType(uptime);
		case WIFILINK_LONGITUDE:
			return new StringType(longitude);
		case WIFILINK_LATITUDE:
			return new StringType(latitude);
		case UPDATETIME:
			Calendar cal = Calendar.getInstance();
			cal.setTime(getTime());
			return new DateTimeType(cal);
		default:
			return null;
		}
	}
	
	public static boolean matches(String message) {
		if (message.contains("\"type\":\"hub\"") && message.contains("\"prod\":\"wfl\"") && message.contains("lat") && message.contains("long")) {
			return true;
		}
		return false;
	}

	@Override
	public LightwaveRfMessageType getMessageType() {
		return LightwaveRfMessageType.SERIAL;
	}

	public String getMac() {
		return mac;
	}
	
	public String getProd() {
		return prod;
	}
	
	public Date getTime() {
		return time;
	}
	
	public String getType() {
		return type;
	}

	public String getFirmware() {
		return firmware;
	}

	public long getUptime() {
		return uptime;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public Date getDuskTime() {
		return duskTime;
	}

	public Date getDawnTime() {
		return dawnTime;
	}

	public String getTmrs() {
		return tmrs;
	}

	public String getEnvs() {
		return envs;
	}

	public String getRun() {
		return run;
	}

	public String getMacs() {
		return macs;
	}

	public String getIp() {
		return ip;
	}

	public String getDevs() {
		return devs;
	}

	@Override
	public String getSerial() {
		return WIFI_LINK_SERIAL;
	}
}
