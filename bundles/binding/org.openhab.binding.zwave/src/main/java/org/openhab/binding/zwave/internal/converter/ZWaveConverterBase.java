/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.zwave.internal.converter.command.ZWaveCommandConverter;
import org.openhab.binding.zwave.internal.converter.state.StateComparator;
import org.openhab.binding.zwave.internal.converter.state.ZWaveStateConverter;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * ZWaveConverterBase class. Base class for all converters
 * that convert between the Z-Wave API and openHAB
 * items.
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public abstract class ZWaveConverterBase {

	private final ZWaveController controller;
	private final EventPublisher eventPublisher;
	private final Map<Class<? extends State>,ZWaveStateConverter<?,?>> stateConverters = new HashMap<Class<? extends State>, ZWaveStateConverter<?,?>>();
	private final Map<Class<? extends Command>, ZWaveCommandConverter<?,?>> commandConverters = new HashMap<Class<? extends Command>, ZWaveCommandConverter<?,?>>();
	
	/**
	 * Constructor. Creates a new instance of the {@link ZWaveConverterBase}
	 * class.
	 * @param controller the {@link ZWaveController} to use to send messages.
	 * @param eventPublisher the {@link EventPublisher} that can be used to send updates.
	 */
	public ZWaveConverterBase(ZWaveController controller, EventPublisher eventPublisher) {
		this.controller = controller;
		this.eventPublisher = eventPublisher;
	}

	/**
	 * Returns the {@link EventPublisher} that can be used
	 * inside the converter to publish event updates.
	 * @return the eventPublisher
	 */
	protected EventPublisher getEventPublisher() {
		return this.eventPublisher;
	}

	/**
	 * Returns the {@link ZWaveController} that is used to send messages.
	 * @return the controller to use to send messages.
	 */
	protected ZWaveController getController() {
		return this.controller;
	}

	/**
	 * Add a {@link ZWaveStateConverter} to the map of converters for fast lookup.
	 * @param converter the {@link ZWaveStateConverter} to add.
	 */
	protected void addStateConverter(ZWaveStateConverter<?,?> converter) {
		this.stateConverters.put(converter.getState(), converter);
	}
	
	/**
	 * Add a {@link ZWaveStateConverter} to the map of converters for fast lookup.
	 * @param converter the {@link ZWaveStateConverter} to add.
	 */
	protected void addCommandConverter(ZWaveCommandConverter<?,?> converter) {
		this.commandConverters.put(converter.getCommand(), converter);
	}
	
	/**
	 * Returns the default Refresh interval for the converter to use.
	 * 0 (zero) indicates that the converter does not refresh by default or does not support it.
	 * @return the refresh interval for this converter.
	 */
	abstract int getRefreshInterval();
	
	/**
	 * Gets a {@link ZWaveStateConverter} that is suitable for this {@link CommandClass} and the data types supported by
	 * the {@link Item}
	 * @param commandClass the {@link CommandClass} that sent the value.
	 * @param item the {@link Item} that has to receive the State;
	 * @return a converter object that converts between the value and the state;
	 */
	protected ZWaveStateConverter<?,?> getStateConverter(Item item, Object value) {
		
		List<Class<? extends State>> list = new ArrayList<Class<? extends State>>(item.getAcceptedDataTypes());
		Collections.sort(list, new StateComparator());

		for (Class<? extends State> stateClass : list) {
			ZWaveStateConverter<?,?> result = stateConverters.get(stateClass);

			if (result == null || !result.getType().isInstance(value))
				continue;
			
			return result;
		}
		
		return null;
	}

	/**
	 * Gets a {@link ZWaveCommandConverter} that is suitable for this {@link CommandClass}
	 * @param commandClass the {@link CommandClass} that sent the value.
	 * @return a converter object that converts between the value and the state;
	 */
	protected ZWaveCommandConverter<?,?> getCommandConverter(Class<? extends Command> commandClass) {
		return this.commandConverters.get(commandClass);
	}	
}
