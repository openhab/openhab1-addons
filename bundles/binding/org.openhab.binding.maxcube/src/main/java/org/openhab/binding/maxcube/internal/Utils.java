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
package org.openhab.binding.maxcube.internal;

import java.util.Calendar;
import java.util.Date;

/**
* Utility class for common tasks. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public final class Utils {
	
	public static int fromHex(String hex) {
		return Integer.parseInt(hex, 16);
	}
	
	public static final String toHex(int... values) {
		String returnValue = "";
		for (int v : values) {
			returnValue += v < 16 ? "0" + Integer.toHexString(v).toUpperCase() : Integer.toHexString(v).toUpperCase();
		}	
		return returnValue;
	}

	public static int fromByte(byte b) {
		return b & 0xFF;
	}
	
	public static Date resolveDateTime(int date, int time) {
		
		int month = ((date & 0xE000) >> 12)+((date & 0x80) >> 7);
		int day = (date & 0x1F00) >> 8;
		int year = (date & 0x0F) + 2000;
	
		int hours = (int)(time * 0.5);
		int minutes = (int)(60 * ((time * 0.5)-hours));
		
		return new Date(year, month, day, hours, minutes);
	}
}