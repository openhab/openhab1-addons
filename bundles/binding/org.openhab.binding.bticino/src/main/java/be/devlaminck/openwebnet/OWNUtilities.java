package be.devlaminck.openwebnet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Utilities for OpenWebNet - OpenHab binding Based on code from Mauro Cicolella
 * (as part of the FREEDOMOTIC framework)
 * (https://github.com/freedomotic/freedomotic/tree/master/plugins/devices/openwebnet) 
 * and on code of Flavio Fcrisciani release as EPL
 * 
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
 * 
 * (https://github.com/fcrisciani/java-myhome-library)
 * 
 * @author Tom De Vlaminck
 * @serial 1.0
 * @since 1.5.0
 * 
 */
public class OWNUtilities
{

	/*
	 * 
	 * OWN Control Messages
	 */
	public final static String MSG_OPEN_ACK = "*#*1##";
	public final static String MSG_OPEN_NACK = "*#*0##";

	// create the frame to send to the own gateway
	public static String createFrame(ProtocolRead c)
	{
		String frame = null;
		String address[] = null;
		String who = null;
		String what = null;
		String where = null;

		who = c.getProperty("who");
		what = c.getProperty("what");
		address = c.getProperty("address").split("\\*");
		where = address[0];
		frame = "*" + who + "*" + what + "*" + where + "##";
		return (frame);
	}

	public static String getDateTime()
	{
		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return (sdf.format(calendar.getTime()));
	}

	public static String convertTemperature(String temperature)
	{
		String temp = "";
		if (!temperature.substring(0, 1).equalsIgnoreCase("0"))
		{
			temp += "-";
		}
		temp += temperature.substring(1, 3);
		temp += ".";
		temp += temperature.substring(3);
		return (temp);
	}

	public static String dayName(String dayNumber)
	{
		String dayName = null;
		switch (Integer.parseInt(dayNumber))
		{
		case (0):
			dayName = "Sunday";
			break;
		case (1):
			dayName = "Monday";
			break;
		case (2):
			dayName = "Tuesday";
			break;
		case (3):
			dayName = "Wednesday";
			break;
		case (4):
			dayName = "Thursday";
			break;
		case (5):
			dayName = "Friday";
			break;
		case (6):
			dayName = "Saturday";
			break;
		default:
			dayName = "Invalid day number [" + dayNumber + "]";
		}
		return (dayName);
	}

	public static String gatewayModel(String modelNumber)
	{
		String model = null;
		switch (new Integer(Integer.parseInt(modelNumber)))
		{
		case (2):
			model = "MHServer";
		case (4):
			model = "MH20F0";
		case (6):
			model = "F452V";
		case (11):
			model = "MHServer2";
		case (12):
			model = "F453AV";
		case (13):
			model = "H4684";
		default:
			model = "Unknown";
		}
		return (model);
	}
}
