/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.config;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.satel.SatelBindingConfig;
import org.openhab.binding.satel.internal.event.IntegraStateEvent;
import org.openhab.binding.satel.internal.event.SatelEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.openhab.binding.satel.internal.protocol.command.ControlObjectCommand;
import org.openhab.binding.satel.internal.protocol.command.IntegraStateCommand;
import org.openhab.binding.satel.internal.types.DoorsState;
import org.openhab.binding.satel.internal.types.InputState;
import org.openhab.binding.satel.internal.types.IntegraType;
import org.openhab.binding.satel.internal.types.ObjectType;
import org.openhab.binding.satel.internal.types.OutputControl;
import org.openhab.binding.satel.internal.types.OutputState;
import org.openhab.binding.satel.internal.types.StateType;
import org.openhab.binding.satel.internal.types.ZoneControl;
import org.openhab.binding.satel.internal.types.ZoneState;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class implements binding configuration for all items that represents
 * Integra zones/inputs/outputs state.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class IntegraStateBindingConfig implements SatelBindingConfig {

	private static final DecimalType DECIMAL_ONE = new DecimalType(1);

	private StateType stateType;
	private int objectNumber;
	private Map<String, String> options;

	private IntegraStateBindingConfig(StateType stateType, int objectNumber, Map<String, String> options) {
		this.stateType = stateType;
		this.objectNumber = objectNumber;
		this.options = options;
	}

	/**
	 * Parses given binding configuration and creates configuration object.
	 * 
	 * @param bindingConfig
	 *            config to parse
	 * @return parsed config object or <code>null</code> if config does not
	 *         match
	 * @throws BindingConfigParseException
	 *             in case of parse errors
	 */
	public static IntegraStateBindingConfig parseConfig(String bindingConfig) throws BindingConfigParseException {
		String[] configElements = bindingConfig.split(":");
		int idx = 0;
		ObjectType objectType;

		// parse object type, mandatory
		try {
			objectType = ObjectType.valueOf(configElements[idx++]);
		} catch (Exception e) {
			// wrong config type, skip parsing
			return null;
		}

		// parse state type, mandatory except for output
		StateType stateType = null;
		int objectNumber = 0;

		try {
			switch (objectType) {
			case input:
				stateType = InputState.valueOf(configElements[idx++]);
				break;
			case zone:
				stateType = ZoneState.valueOf(configElements[idx++]);
				break;
			case output:
				stateType = OutputState.output;
				break;
			case doors:
				stateType = DoorsState.valueOf(configElements[idx++]);
				break;
			}
		} catch (Exception e) {
			throw new BindingConfigParseException(String.format("Invalid state type: {}", bindingConfig));
		}

		// parse object number, if provided
		if (idx < configElements.length) {
			try {
				objectNumber = Integer.parseInt(configElements[idx++]);
			} catch (NumberFormatException e) {
				throw new BindingConfigParseException(String.format("Invalid object number: {}", bindingConfig));
			}
		}

		// parse options: comma separated pairs of <name>=<value>
		Map<String, String> options = new HashMap<String, String>();
		if (idx < configElements.length) {
			for (String option : configElements[idx++].split(",")) {
				if (option.contains("=")) {
					String[] keyVal = option.split("=", 2);
					options.put(keyVal[0], keyVal[1]);
				} else {
					options.put(option, "");
				}
			}
		}

		if (idx < configElements.length) {
			// if anything left, throw exception
			throw new BindingConfigParseException(String.format("Too many elements: {}", bindingConfig));
		}

		return new IntegraStateBindingConfig(stateType, objectNumber, options);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public State convertEventToState(Item item, SatelEvent event) {
		if (!(event instanceof IntegraStateEvent)) {
			return null;
		}

		IntegraStateEvent stateEvent = (IntegraStateEvent) event;
		if (stateEvent.getStateType() != this.stateType) {
			return null;
		}

		if (this.objectNumber > 0) {
			int bitNbr = this.objectNumber - 1;
			if (item instanceof ContactItem) {
				return stateEvent.isSet(bitNbr) ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
			} else if (item instanceof SwitchItem) {
				return stateEvent.isSet(bitNbr) ? OnOffType.ON : OnOffType.OFF;
			} else if (item instanceof NumberItem) {
				return stateEvent.isSet(bitNbr) ? DECIMAL_ONE : DecimalType.ZERO;
			}
		} else {
			if (item instanceof ContactItem) {
				return (stateEvent.statesSet() > 0) ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
			} else if (item instanceof SwitchItem) {
				return (stateEvent.statesSet() > 0) ? OnOffType.ON : OnOffType.OFF;
			} else if (item instanceof NumberItem) {
				return new DecimalType(stateEvent.statesSet());
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SatelMessage handleCommand(Command command, IntegraType integraType, String userCode) {
		if (command instanceof OnOffType && this.objectNumber > 0) {
			boolean switchOn = ((OnOffType) command == OnOffType.ON);
			boolean force_arm = this.options.containsKey("force_arm");

			switch (this.stateType.getObjectType()) {
			case output:
				byte[] outputs = getObjectBitset((integraType == IntegraType.I256_PLUS) ? 32 : 16);
				return ControlObjectCommand.buildMessage(switchOn ? OutputControl.on : OutputControl.off, outputs,
						userCode);

			case doors:
				break;

			case input:
				break;

			case zone:
				byte[] zones = getObjectBitset(4);
				switch ((ZoneState) this.stateType) {
				// clear alarms on OFF command
				case alarm:
				case alarm_memory:
				case fire_alarm:
				case fire_alarm_memory:
				case verified_alarms:
				case warning_alarms:
					if (switchOn) {
						return null;
					} else {
						return ControlObjectCommand.buildMessage(ZoneControl.clear_alarm, zones, userCode);
					}

					// arm or disarm, depending on command
				case armed:
				case really_armed:
					return ControlObjectCommand.buildMessage(switchOn ? (force_arm ? ZoneControl.force_arm_mode_0
							: ZoneControl.arm_mode_0) : ZoneControl.disarm, zones, userCode);
				case armed_mode_1:
					return ControlObjectCommand.buildMessage(switchOn ? (force_arm ? ZoneControl.force_arm_mode_1
							: ZoneControl.arm_mode_1) : ZoneControl.disarm, zones, userCode);
				case armed_mode_2:
					return ControlObjectCommand.buildMessage(switchOn ? (force_arm ? ZoneControl.force_arm_mode_2
							: ZoneControl.arm_mode_2) : ZoneControl.disarm, zones, userCode);
				case armed_mode_3:
					return ControlObjectCommand.buildMessage(switchOn ? (force_arm ? ZoneControl.force_arm_mode_3
							: ZoneControl.arm_mode_3) : ZoneControl.disarm, zones, userCode);

					// do nothing for other types of state
				default:
					break;
				}
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SatelMessage buildRefreshMessage(IntegraType integraType) {
		return IntegraStateCommand.buildMessage(this.stateType, integraType == IntegraType.I256_PLUS);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("IntegraStateBindingConfig: object = %s, state = %s, object nbr = %d, options = %s",
				this.stateType.getObjectType(), this.stateType, this.objectNumber, this.options);
	}

	private byte[] getObjectBitset(int size) {
		byte[] bitset = new byte[size];
		int bitNbr = this.objectNumber - 1;
		bitset[bitNbr / 8] = (byte) (1 << (bitNbr % 8));
		return bitset;
	}
}
