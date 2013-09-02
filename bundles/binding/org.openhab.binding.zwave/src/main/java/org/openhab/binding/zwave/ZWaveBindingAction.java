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
package org.openhab.binding.zwave;

import java.util.HashMap;
import java.util.Map;

/**
 * Z-Wave binding action enumeration. It Defines the action that can be performed by the
 * binding on the item.
 * 
 * @author Brian Crosby
 * @since 1.3.0
 */
public enum ZWaveBindingAction {
	/**
	 * This binding is not reporting a value.
	 */
	NONE("NOP"),
	
	/**
	 * Reports the Home ID.
	 */
	REPORT_HOMEID("HOMEID"),
	
	/**
	 * Reports the Node ID.
	 */
	REPORT_NODEID("NODEID"),
	
	/**
	 * Reports whether the node is listening.
	 */
	REPORT_LISTENING("LISTENING"),
	
	//TODO: Make a sleeping AND a dead reporting type. Need more information from Node before this is possible.
	
	/**
	 * Reports whether the node is sleeping / dead or not.
	 */
	REPORT_SLEEPING_DEAD("SLEEPING_DEAD"),
	
	/**
	 * Reports whether the node is routing Z-Wave messages.
	 */
	REPORT_ROUTING("ROUTING"),
	
	/**
	 * Reports Node version.
	 */
	REPORT_VERSION("VERSION"),
	
	/**
	 * Reports the basic device class of the node (in hex).
	 */
	REPORT_BASIC("BASIC"),
	
	/**
	 * Reports the basic device class of the node (as text).
	 */
	REPORT_BASIC_LABEL("BASIC_LABEL"),
	
	/**
	 * Reports the Generic device class of the node (in hex).
	 */
	REPORT_GENERIC("GENERIC"),
	
	/**
	 * Reports the Generic device class of the node (as text).
	 */
	REPORT_GENERIC_LABEL("GENERIC_LABEL"),
	
	/**
	 * Reports the Specific device class of the node (in hex).
	 */
	REPORT_SPECIFIC("SPECIFIC"),
	
	/**
	 * Reports the Specific device class of the node (as text).
	 */
	REPORT_SPECIFIC_LABEL("SPECIFIC_LABEL"),
	
	/**
	 * Reports the manufacturer of the node.
	 */
	REPORT_MANUFACTURER("MANUFACTURER"),
	
	/**
	 * Reports the Device type of the node (in hex).
	 */
	REPORT_DEVICE_TYPE_ID("DEVICE_TYPE_ID"),
	
	/**
	 * Reports the Device type of the node (as text).
	 */
	REPORT_DEVICE_TYPE("DEVICE_TYPE"),
	
	/**
	 * Reports the date / time the node value was last updated.
	 */
	REPORT_LASTUPDATE("LASTUPDATE"),

	/**
	 * Reports the amount of Start of frames this node has received.
	 */
	REPORT_SOF("SOF"),
	
	/**
	 * Reports the amount of Canceled frames this node has received.
	 */
	REPORT_CAN("CAN"),
	
	/**
	 * Reports the amount of not acknowledged frames this node has received.
	 */
	REPORT_NAK("NAK"),
	
	/**
	 * Reports the amount of out of order frames this node has received.
	 */
	REPORT_OOF("OOF"),
	
	/**
	 * Reports the amount of out of acknowledged frames this node has received.
	 */
	REPORT_ACK("ACK"),
	
	/**
	 * Reports the wake up interval time in seconds.
	 */
	REPORT_WAKE_UP_INTERVAL("WAKE_UP_INTERVAL"),
	
	/**
	 * Reports the battery level for a device.
	 */
	REPORT_BATTERY_LEVEL("BATTERY_LEVEL"),
	
	/**
	 * Enables Z-Wave dimmers to restore to the last value, instead of the maximum level;
	 */
	RESTORE_LAST_VALUE("RESTORE_LAST_VALUE");
	
	private String label;
	private static Map<String, ZWaveBindingAction> labelToZWaveBindingActionMapping;

	private ZWaveBindingAction(String label) {
		this.label = label;
	}
	
	private static void initMapping() {
		labelToZWaveBindingActionMapping = new HashMap<String, ZWaveBindingAction>();
		for (ZWaveBindingAction s : values()) {
			labelToZWaveBindingActionMapping.put(s.label, s);
		}
	}

	/**
	 * Returns the label of the ReportValues enumeration
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Lookup function based on the binding action label.
	 * Returns null if the binding action is not found.
	 * @param label the label to lookup
	 * @return enumeration value of the binding action.
	 */
	public static ZWaveBindingAction getZWaveBindingAction(String label) {
		if (labelToZWaveBindingActionMapping == null) {
			initMapping();
		}
		return labelToZWaveBindingActionMapping.get(label);
	}
	
}
