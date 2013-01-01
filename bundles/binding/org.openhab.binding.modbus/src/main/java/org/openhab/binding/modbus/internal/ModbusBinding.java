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
package org.openhab.binding.modbus.internal;

import java.util.Collection;

import net.wimpi.modbus.procimg.InputRegister;
import net.wimpi.modbus.util.BitVector;

import org.openhab.binding.modbus.ModbusBindingProvider;
import org.openhab.binding.modbus.internal.ModbusGenericBindingProvider.ModbusBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.AbstractEventSubscriberBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;


/**
 * Modbus binding allows to connect to multiple Modbus slaves as TCP master.
 * This implementation works with coils (boolean values) only.
 * 
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public class ModbusBinding extends AbstractEventSubscriberBinding<ModbusBindingProvider> {
	
	private static EventPublisher eventPublisher = null;

	
	public void activate() {
	}

	public void deactivate() {
	}


	public void setEventPublisher(EventPublisher eventPublisher) {
		ModbusBinding.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		ModbusBinding.eventPublisher = null;
	}

	/**
	 * Parses configuration creating Modbus slave instances defined in cfg file
	 * {@inheritDoc}
	 */
	protected void internalReceiveCommand(String itemName, Command command) {
		for (ModbusBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				ModbusBindingConfig config = provider.getConfig(itemName);
				ModbusSlave slave = ModbusConfiguration.getSlave(config.slaveName);
				slave.executeCommand(command, config.readRegister, config.writeRegister);
			}
		}

	}

	/**
	 * Posts update event to OpenHAB bus for "holding" type slaves
	 * @param binding ModbusBinding to get item configuration from BindingProviding
	 * @param registers data received from slave device in the last poll
	 * @param itemName item to update
	 */
	protected void internalUpdateItem(String slaveName, InputRegister[] registers,
			String itemName) {
		for (ModbusBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				ModbusBindingConfig config = provider.getConfig(itemName);
				if (config.slaveName.equals(slaveName)) {
					InputRegister value = registers[config.readRegister];
					DecimalType newState = new DecimalType(value.getValue());
					if (!newState.equals(provider.getConfig(itemName).getItemState()))
						eventPublisher.postUpdate(itemName, newState);
				}
			}
		}
	}

	/**
	 * Posts update event to OpenHAB bus for "coil" type slaves
	 * @param binding ModbusBinding to get item configuration from BindingProviding
	 * @param registers data received from slave device in the last poll
	 * @param item item to update
	 */
	protected void internalUpdateItem(String slaveName, BitVector coils,
			String itemName) {
		for (ModbusBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				ModbusBindingConfig config = provider.getConfig(itemName);
				if (config.slaveName.equals(slaveName)) {
					boolean state = coils.getBit(config.readRegister);
					State currentState = provider.getConfig(itemName).getItemState();
					State newState = provider.getConfig(itemName).translateBoolean2State(state);
					if (!newState.equals(currentState)) {
						eventPublisher.postUpdate(itemName, newState);
					}
				}
			}
		}
	}
	

	public Collection<String> getItemNames() {
		Collection<String> items = null;
		for (BindingProvider provider : providers) {
			if (items == null)
				items = provider.getItemNames();
			else
				items.addAll(provider.getItemNames());
		}
		return items;
	}


}

