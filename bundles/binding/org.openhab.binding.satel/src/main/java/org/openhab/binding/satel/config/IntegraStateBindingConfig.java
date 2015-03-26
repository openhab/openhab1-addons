/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.config;

import java.util.Map;

import org.openhab.binding.satel.SatelBindingConfig;
import org.openhab.binding.satel.internal.event.IntegraStateEvent;
import org.openhab.binding.satel.internal.event.SatelEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.openhab.binding.satel.internal.protocol.command.ControlObjectCommand;
import org.openhab.binding.satel.internal.protocol.command.IntegraStateCommand;
import org.openhab.binding.satel.internal.types.DoorsState;
import org.openhab.binding.satel.internal.types.IntegraType;
import org.openhab.binding.satel.internal.types.ObjectType;
import org.openhab.binding.satel.internal.types.OutputControl;
import org.openhab.binding.satel.internal.types.OutputState;
import org.openhab.binding.satel.internal.types.PartitionControl;
import org.openhab.binding.satel.internal.types.PartitionState;
import org.openhab.binding.satel.internal.types.StateType;
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
 * This class implements binding configuration for all items that represent
 * Integra zones/partitions/outputs state.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class IntegraStateBindingConfig extends SatelBindingConfig {

	private StateType stateType;
	private int[] objectNumbers;
	private Map<String, String> options;

	private IntegraStateBindingConfig(StateType stateType, int[] objectNumbers, Map<String, String> options) {
		this.stateType = stateType;
		this.objectNumbers = objectNumbers;
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
		ConfigIterator iterator = new ConfigIterator(bindingConfig);
		ObjectType objectType;

		// parse object type, mandatory
		try {
			objectType = ObjectType.valueOf(iterator.nextUpperCase());
		} catch (Exception e) {
			// wrong config type, skip parsing
			return null;
		}

		// parse state type, mandatory except for output
		StateType stateType = null;
		int[] objectNumbers = {};

		switch (objectType) {
		case ZONE:
			stateType = iterator.nextOfType(ZoneState.class, "zone state type");
			break;
		case PARTITION:
			stateType = iterator.nextOfType(PartitionState.class, "partition state type");
			break;
		case OUTPUT:
			stateType = OutputState.OUTPUT;
			break;
		case DOORS:
			stateType = iterator.nextOfType(DoorsState.class, "doors state type");
			break;
		}

		// parse object numbers, if provided
		if (iterator.hasNext()) {
			try {
				String[] objectNumbersStr = iterator.next().split(",");
				objectNumbers = new int[objectNumbersStr.length];
				for (int i = 0; i < objectNumbersStr.length; ++i) {
					int objectNumber = Integer.parseInt(objectNumbersStr[i]);
					if (objectNumber < 1 || objectNumber > 256) {
						throw new BindingConfigParseException(String.format("Invalid object number: %s", bindingConfig));
					}
					objectNumbers[i] = objectNumber;
				}
			} catch (NumberFormatException e) {
				throw new BindingConfigParseException(String.format("Invalid object number: %s", bindingConfig));
			}
		}

		return new IntegraStateBindingConfig(stateType, objectNumbers, parseOptions(iterator));
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
		
		if (this.objectNumbers.length == 1) {
			int bitNbr = this.objectNumbers[0] - 1;
			return booleanToState(item, stateEvent.isSet(bitNbr));
		} else if (this.objectNumbers.length == 0) {
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
	public SatelMessage convertCommandToMessage(Command command, IntegraType integraType, String userCode) {
		if (command instanceof OnOffType && this.objectNumbers.length == 1) {
			boolean switchOn = ((OnOffType) command == OnOffType.ON);
			boolean force_arm = this.options.containsKey("FORCE_ARM");

			switch (this.stateType.getObjectType()) {
			case OUTPUT:
				byte[] outputs = getObjectBitset((integraType == IntegraType.I256_PLUS) ? 32 : 16);
				return ControlObjectCommand.buildMessage(switchOn ? OutputControl.ON : OutputControl.OFF, outputs,
						userCode);

			case DOORS:
				break;

			case ZONE:
				break;

			case PARTITION:
				byte[] partitions = getObjectBitset(4);
				switch ((PartitionState) this.stateType) {
				// clear alarms on OFF command
				case ALARM:
				case ALARM_MEMORY:
				case FIRE_ALARM:
				case FIRE_ALARM_MEMORY:
				case VERIFIED_ALARMS:
				case WARNING_ALARMS:
					if (switchOn) {
						return null;
					} else {
						return ControlObjectCommand.buildMessage(PartitionControl.CLEAR_ALARM, partitions, userCode);
					}

					// arm or disarm, depending on command
				case ARMED:
				case REALLY_ARMED:
					return ControlObjectCommand.buildMessage(switchOn ? (force_arm ? PartitionControl.FORCE_ARM_MODE_0
							: PartitionControl.ARM_MODE_0) : PartitionControl.DISARM, partitions, userCode);
				case ARMED_MODE_1:
					return ControlObjectCommand.buildMessage(switchOn ? (force_arm ? PartitionControl.FORCE_ARM_MODE_1
							: PartitionControl.ARM_MODE_1) : PartitionControl.DISARM, partitions, userCode);
				case ARMED_MODE_2:
					return ControlObjectCommand.buildMessage(switchOn ? (force_arm ? PartitionControl.FORCE_ARM_MODE_2
							: PartitionControl.ARM_MODE_2) : PartitionControl.DISARM, partitions, userCode);
				case ARMED_MODE_3:
					return ControlObjectCommand.buildMessage(switchOn ? (force_arm ? PartitionControl.FORCE_ARM_MODE_3
							: PartitionControl.ARM_MODE_3) : PartitionControl.DISARM, partitions, userCode);

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
		StringBuilder sb = new StringBuilder();
		for (int i : this.objectNumbers) {
			if (sb.length() > 0)
				sb.append(",");
			sb.append(Integer.toString(i));
		}
		return String.format("IntegraStateBindingConfig: object = %s, state = %s, object nbr = %s, options = %s",
				this.stateType.getObjectType(), this.stateType, sb.toString(), this.options);
	}

	private byte[] getObjectBitset(int size) {
		byte[] bitset = new byte[size];
		for (int objectNumber : this.objectNumbers) {
			int bitNbr = objectNumber - 1;
			bitset[bitNbr / 8] |= (byte) (1 << (bitNbr % 8));
		}
		return bitset;
	}
}
