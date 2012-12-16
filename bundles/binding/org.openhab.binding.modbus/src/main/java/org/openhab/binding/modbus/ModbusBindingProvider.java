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
package org.openhab.binding.modbus;

import org.openhab.binding.modbus.internal.ModbusGenericBindingProvider.ModbusBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can map openHAB items to Modbus
 * binding types.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public interface ModbusBindingProvider extends BindingProvider {

	/**
	 * Allowed slave device types "coil" corresponds to discrete output Coils
	 * (read/write) "discrete" corresponds to discrete input contacts (read
	 * only) "holding" corresponds to analog output holding registers
	 * (read/write) "input" corresponds to analog input registers (read only)
	 */
	static final public String TYPE_COIL = "coil";
	static final public String TYPE_DISCRETE = "discrete";
	static final public String TYPE_HOLDING = "holding";
	static final public String TYPE_INPUT = "input";

	static final String[] SLAVE_DATA_TYPES = { TYPE_COIL, TYPE_DISCRETE, TYPE_HOLDING, TYPE_INPUT };

	/**
	 * Returns Modbus item configuration
	 * 
	 * @param itemName item name
	 * @return Modbus item configuration
	 */
	ModbusBindingConfig getConfig(String itemName);

}
