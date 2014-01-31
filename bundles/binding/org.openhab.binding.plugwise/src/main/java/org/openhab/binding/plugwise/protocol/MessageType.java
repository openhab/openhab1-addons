/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * Plugwise message types - many are still missing, and require further protocol analysis
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public enum MessageType {

	ACKNOWLEDGEMENT(0), 
	NODE_AVAILABLE (6), 
	NODE_AVAILABLE_RESPONSE(7), 
	NETWORK_RESET_REQUEST(8),
	INITIALISE_REQUEST(10), 
	INITIALISE_RESPONSE(17),
	POWER_INFORMATION_REQUEST(18),
	POWER_INFORMATION_RESPONSE(19),
	CLOCK_SET_REQUEST(22), 
	POWER_CHANGE_REQUEST(23),
	DEVICE_ROLECALL_REQUEST(24), 
	DEVICE_ROLECALL_RESPONSE(25),
	DEVICE_INFORMATION_REQUEST(35),
	DEVICE_INFORMATION_RESPONSE(36),
	DEVICE_CALIBRATION_REQUEST(38),
	DEVICE_CALIBRATION_RESPONSE(39),
	REALTIMECLOCK_GET_REQUEST(41),
	REALTIMECLOCK_GET_RESPONSE(58),
	CLOCK_GET_REQUEST(62), 
	CLOCK_GET_RESPONSE(63),
	POWER_BUFFER_REQUEST(72),
	POWER_BUFFER_RESPONSE(73);
	
	private int identifier;

	private MessageType(int value) {
		identifier = value;
	}
	
    private static final Map<Integer, MessageType> typesByValue = new HashMap<Integer, MessageType>();

    static {
        for (MessageType type : MessageType.values()) {
            typesByValue.put(type.identifier, type);
        }
    }
	
    public static MessageType forValue(int value) {
        return typesByValue.get(value);
    }
    
    public int toInt() {
    	return identifier;
    }
};
