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
package org.openhab.io.net.actions;

import java.util.HashMap;
import java.util.Map;

import org.openhab.io.net.iot.IoTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * This class provides static methods that can be used in automation rules
 * for sending IoT requests
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 *
 */
public class IoT {
	
	private static final Logger logger = LoggerFactory.getLogger(IoT.class);
	
	private static Map<String, IoTService> iotServices = new HashMap<String, IoTService>();
	
	
	public IoT() {
		// default constructor, necessary for osgi-ds
	}
	
	public void addIoTService(IoTService service) {
		iotServices.put(service.getName(), service);
	}
	
	public void removeIoTService(IoTService service) {
		iotServices.remove(service.getName());
	}
	
	
	/**
	 * Sends a the given <code>value</code> to an {@link IoTService} identified
	 * by the <code>iotServiceName</code>. 
	 * 
	 * @param iotServiceName the name of the {@link IoTService} to send the given
	 * <code>value</code> to
	 * @param feedId specifies the feed to add the given <code>value</code> to
	 * @param value the value to send
	 */
	static public void sendSenseValue(String iotServiceName, String feedId, String value) {
		IoTService ioTService = iotServices.get(iotServiceName);
		if (ioTService != null) {
			ioTService.send(feedId, value);
		}
		else {
			logger.warn("There is now IoT-Service registered with the name '{}'", iotServiceName);
		}
	} 

}
