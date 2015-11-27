/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.config;

import java.util.Map;

import org.openhab.binding.satel.SatelBindingConfig;
import org.openhab.binding.satel.internal.event.IntegraStatusEvent;
import org.openhab.binding.satel.internal.event.SatelEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.openhab.binding.satel.internal.protocol.command.ClearTroublesCommand;
import org.openhab.binding.satel.internal.protocol.command.IntegraStatusCommand;
import org.openhab.binding.satel.internal.protocol.command.SetClockCommand;
import org.openhab.binding.satel.internal.types.IntegraType;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class implements binding configuration for all items that represent
 * Integra zones/partitions/outputs state.
 * 
 * Supported options:
 * <ul>
 * <li>commands_only - binding does not update state of the item, but accepts
 * commands</li>
 * </ul>
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class IntegraStatusBindingConfig extends SatelBindingConfig {

	enum StatusType {
		DATE_TIME, SERVICE_MODE, TROUBLES, ACU100_PRESENT, INTRX_PRESENT, TROUBLES_MEMORY, GRADE23_SET
	}

	private StatusType statusType;

	private IntegraStatusBindingConfig(StatusType statusType, Map<String, String> options) {
		super(options);
		this.statusType = statusType;
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
	public static IntegraStatusBindingConfig parseConfig(String bindingConfig) throws BindingConfigParseException {
		ConfigIterator iterator = new ConfigIterator(bindingConfig);

		// check if a status item
		if (!"status".equalsIgnoreCase(iterator.next()))
			return null;

		return new IntegraStatusBindingConfig(iterator.nextOfType(StatusType.class, "status type"),
				iterator.parseOptions());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public State convertEventToState(Item item, SatelEvent event) {
		if (!(event instanceof IntegraStatusEvent) || hasOptionEnabled(Options.COMMANDS_ONLY)) {
			return null;
		}

		IntegraStatusEvent statusEvent = (IntegraStatusEvent) event;

		switch (this.statusType) {
		case DATE_TIME:
			if (item.getAcceptedDataTypes().contains(DateTimeType.class)) {
				return new DateTimeType(statusEvent.getIntegraTime());
			} else {
				return null;
			}
		case SERVICE_MODE:
			return booleanToState(item, statusEvent.inServiceMode());
		case TROUBLES:
			return booleanToState(item, statusEvent.troublesPresent());
		case TROUBLES_MEMORY:
			return booleanToState(item, statusEvent.troublesMemory());
		case ACU100_PRESENT:
			return booleanToState(item, statusEvent.isAcu100Present());
		case INTRX_PRESENT:
			return booleanToState(item, statusEvent.isIntRxPresent());
		case GRADE23_SET:
			return booleanToState(item, statusEvent.isGrade23Set());
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SatelMessage convertCommandToMessage(Command command, IntegraType integraType, String userCode) {
		if (command instanceof OnOffType) {
			boolean switchOn = ((OnOffType) command == OnOffType.ON);

			switch (this.statusType) {
			case TROUBLES:
			case TROUBLES_MEMORY:
				if (switchOn) {
					return null;
				} else {
					return ClearTroublesCommand.buildMessage(userCode);
				}

				// do nothing for other types of status
			default:
				break;
			}

		} else if (this.statusType == StatusType.DATE_TIME) {
			DateTimeType dateTime = null;
			if (command instanceof StringType) {
				dateTime = DateTimeType.valueOf(command.toString());
			} else if (command instanceof DateTimeType) {
				dateTime = (DateTimeType) command;
			}
			if (dateTime != null) {
				return SetClockCommand.buildMessage(dateTime.getCalendar(), userCode);
			}
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SatelMessage buildRefreshMessage(IntegraType integraType) {
		return IntegraStatusCommand.buildMessage();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("IntegraStatusBindingConfig: status = %s, options = %s", this.statusType,
				this.optionsAsString());
	}
}
