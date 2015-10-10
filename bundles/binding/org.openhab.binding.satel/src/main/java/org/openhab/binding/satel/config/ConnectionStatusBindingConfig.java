/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.config;

import java.util.Calendar;
import java.util.Map;

import org.openhab.binding.satel.SatelBindingConfig;
import org.openhab.binding.satel.internal.event.ConnectionStatusEvent;
import org.openhab.binding.satel.internal.event.NewStatesEvent;
import org.openhab.binding.satel.internal.event.SatelEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.openhab.binding.satel.internal.types.IntegraType;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class implements binding configuration for various connection status
 * values.
 * 
 * Supported options:
 * <ul>
 * <li>invert_state - for "connected" status, active state is <code>false</code>
 * </li>
 * </ul>
 * 
 * @author Krzysztof Goworek
 * @since 1.8.0
 */
public class ConnectionStatusBindingConfig extends SatelBindingConfig {

	enum StatusType {
		CONNECTED, CONNECTED_SINCE, CONNECTION_ERRORS
	}

	private StatusType statusType;
	private Calendar connectedSince;
	private int connectionFailures;

	private ConnectionStatusBindingConfig(StatusType statusType, Map<String, String> options) {
		super(options);
		this.statusType = statusType;
		this.connectedSince = null;
		this.connectionFailures = 0;
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
	public static ConnectionStatusBindingConfig parseConfig(String bindingConfig) throws BindingConfigParseException {
		ConfigIterator iterator = new ConfigIterator(bindingConfig);

		// check if a status item
		if (!"module".equalsIgnoreCase(iterator.next()))
			return null;

		return new ConnectionStatusBindingConfig(iterator.nextOfType(StatusType.class, "status type"),
				iterator.parseOptions());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public State convertEventToState(Item item, SatelEvent event) {
		if (!(event instanceof ConnectionStatusEvent)) {
			// since we get event about changes, we assume connection has been established.
			// if current items state is uninitialized, "generate" fake event to update its state
			if (event instanceof NewStatesEvent && this.connectedSince == null) {
				event = new ConnectionStatusEvent(true);
			} else {
				return null;
			}
		}

		ConnectionStatusEvent statusEvent = (ConnectionStatusEvent) event;
		boolean invertState = hasOptionEnabled(Options.INVERT_STATE);

		// update internal values
		if (statusEvent.isConnected()) {
			this.connectionFailures = 0;
			if (this.connectedSince == null) {
				this.connectedSince = Calendar.getInstance();
			}
		} else {
			this.connectionFailures += 1;
			this.connectedSince = null;
		}

		switch (this.statusType) {
		case CONNECTED:
			return booleanToState(item, statusEvent.isConnected() ^ invertState);

		case CONNECTED_SINCE:
			if (this.connectedSince == null) {
				return UnDefType.NULL;
			} else if (item.getAcceptedDataTypes().contains(DateTimeType.class)) {
				return new DateTimeType(this.connectedSince);
			}
			break;

		case CONNECTION_ERRORS:
			if (item.getAcceptedDataTypes().contains(DecimalType.class)) {
				return new DecimalType(this.connectionFailures);
			}
			break;
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SatelMessage convertCommandToMessage(Command command, IntegraType integraType, String userCode) {
		// this configuration does not accept commands
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SatelMessage buildRefreshMessage(IntegraType integraType) {
		// this is configuration for internal state - does not need refresh
		// command
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("%s: status = %s, options = %s", this.getClass().getName(), this.statusType,
				this.optionsAsString());
	}
}
