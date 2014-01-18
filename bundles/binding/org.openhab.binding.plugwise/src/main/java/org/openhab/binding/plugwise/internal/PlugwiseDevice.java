/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
