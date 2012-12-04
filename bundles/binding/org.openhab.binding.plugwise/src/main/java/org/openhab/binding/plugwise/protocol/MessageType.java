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
