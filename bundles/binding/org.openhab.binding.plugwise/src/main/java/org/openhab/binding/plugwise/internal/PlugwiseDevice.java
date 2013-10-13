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
package org.openhab.binding.plugwise.internal;

import org.openhab.binding.plugwise.PlugwiseCommandType;
import org.openhab.binding.plugwise.protocol.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class to represent Plugwise devices
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
@SuppressWarnings("rawtypes")
public class PlugwiseDevice implements Comparable {
	
	private static final Logger logger = LoggerFactory.getLogger(PlugwiseDevice.class);

	public enum DeviceType { Stick, Circle, CirclePlus };
	
	protected String MAC;
	protected DeviceType type;
	protected String friendlyName;
	
	
	public PlugwiseDevice(String mac,DeviceType typed,String friendly) {
		MAC = mac;
		type = typed;
		friendlyName = friendly;
	}
	
	
	public String getMAC() {
		return MAC;
	}

	public DeviceType getType() {
		return type;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public int compareTo(Object arg0) {
		  return getMAC().compareTo(((PlugwiseDevice)arg0).getMAC());
	}

	/**
	 * 
	 * Each Plugwise device needs to know how process the message that are meant for it. Extending classes therefore have to Override this
	 * method and extend it to include new message types that are relevant for that device class
	 * 
	 * @param message to process
	 * @return
	 */
	public boolean processMessage(Message message) {
		if(message!=null) {
			logger.debug("Received unrecognized Plugwise protocol data unit: MAC:{} command:{} sequence:{} payload:{}", new Object[] { message.getMAC(),message.getType().toString(), Integer.toString(message.getSequenceNumber()), message.getPayLoad()});
			return true;	
		} else {
			return false;
		}
	}
	
	/**
	 * Each Plugwise device should be to post updates to the OH runtime. However, devices can choose to defer or delegate this posting to another more superior class
	 * For example, Circle(+) will pass this on to the Stick, and the Stick will pass it on to the Plugwise Binding
	 * Each device class has to override this method to make it specific for that device class
	 * 
	 * 
	 * @param MAC of the Plugwise device
	 * @param type of Plugwise Command 
	 * @param value to be posted
	 * @return
	 */
	public boolean postUpdate(String MAC, PlugwiseCommandType type, Object value) {
		if(MAC != null && type != null && value != null) {
			logger.debug("Passing on an update to the openHAB bus: MAC: {} Type:{} value:{}", new Object[] { MAC,type.toString(), value.toString()});
			return true;
		} else {
			return false;
		}
	}
	

}
