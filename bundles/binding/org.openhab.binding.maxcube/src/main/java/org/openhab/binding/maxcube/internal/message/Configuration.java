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
* Base class for configuration provided by the MAX!Cube C_Message. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public final class Configuration {
	
	private DeviceType deviceType = null;
	private String rfAddress = null;
	private String serialNumber = null;
	
	private Configuration(String rfAddress, DeviceType deviceType, String serialNumber) {
		this.rfAddress = rfAddress;
		this.deviceType = deviceType;
		this.serialNumber = serialNumber;
	}
	
	public static Configuration create(Message message) {
		C_Message c_message = (C_Message) message;
		
		Configuration configuration = new Configuration(c_message.getRFAddress(), c_message.getDeviceType(), c_message.getSerialNumber());
	
		return configuration;
	}
	
	public String getRFAddress() {
		return rfAddress;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public String getSerialNumber() {
		return serialNumber;
	}	
}