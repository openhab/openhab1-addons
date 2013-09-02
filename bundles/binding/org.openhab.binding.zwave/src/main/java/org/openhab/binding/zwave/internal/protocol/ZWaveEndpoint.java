/**
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
package org.openhab.binding.zwave.internal.protocol;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.zwave.internal.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Basic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Generic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Specific;

/**
 * ZWaveEndpoint class. Represents an endpoint in case of a Multi-channel node.
 * 
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
public class ZWaveEndpoint {

	private final ZWaveDeviceClass deviceClass;
	private final int endpointId;

	private Map<CommandClass, ZWaveCommandClass> supportedCommandClasses = new HashMap<CommandClass, ZWaveCommandClass>();

	/**
	 * Constructor. Creates a new instance of the ZWaveEndpoint class.
	 * @param node the parent node of this endpoint.
	 * @param endpointId the endpoint ID.
	 */
	public ZWaveEndpoint(int endpointId) {
		this.endpointId = endpointId;
		this.deviceClass = 
			new ZWaveDeviceClass(Basic.NOT_KNOWN, Generic.NOT_KNOWN, Specific.NOT_USED);
	}

	/**
	 * Gets the endpoint ID
	 * @return endpointId the endpointId
	 */
	public int getEndpointId() {
		return endpointId;
	}

	/**
	 * Gets the Command classes this endpoint implements.
	 * @return the command classes.
	 */
	public Collection<ZWaveCommandClass> getCommandClasses() {
		return supportedCommandClasses.values();
	}

	/**
	 * Gets a commandClass object this endpoint implements. Returns null if
	 * this endpoint does not support this command class.
	 * 
	 * @param commandClass
	 *            The command class to get.
	 * @return the command class.
	 */
	public ZWaveCommandClass getCommandClass(CommandClass commandClass) {
		return supportedCommandClasses.get(commandClass);
	}

	/**
	 * Adds a command class to the list of supported command classes by this
	 * endpoint. Does nothing if command class is already added.
	 * @param commandClass the command class instance to add.
	 */
	public void addCommandClass(ZWaveCommandClass commandClass) {
		CommandClass key = commandClass.getCommandClass();
		if (!supportedCommandClasses.containsKey(key)) {
			supportedCommandClasses.put(key, commandClass);
		}
	}

	/**
	 * Returns the device class for this endpoint.
	 * @return the deviceClass
	 */
	public ZWaveDeviceClass getDeviceClass() {
		return deviceClass;
	}
	
}
