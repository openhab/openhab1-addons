/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.knx.core.config;

import org.openhab.core.types.Type;

import tuwien.auto.calimero.datapoint.Datapoint;

/**
 * This interface must be implemented by classes that provide a type mapping
 * between openHAB and KNX.
 * When a command or status update is sent to an item on the openHAB event bus,
 * it must be clear, in which format it must be sent to KNX and vice versa.
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public interface KNXTypeMapper {

	/**
	 * maps an openHAB command/state to a string value which correspond to its datapoint in KNX
	 *  
	 * @param type a command or state
	 * @return datapoint value as a string
	 */
	public String toDPValue(Type type);

	/**
	 * maps a datapoint value to an openHAB command or state
	 * 
	 * @param datapoint the source datapoint 
	 * @param data the datapoint value as an ASDU byte array (see <code>{@link ProcessEvent}.getASDU()</code>)
	 * @return a command or state of openHAB
	 */
	public Type toType(Datapoint datapoint, byte[] data);

}
