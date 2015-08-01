/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.protocol.command;

import org.openhab.binding.satel.internal.event.EventDispatcher;
import org.openhab.binding.satel.internal.event.IntegraStateEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.openhab.binding.satel.internal.types.StateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command class for commands that return state of Integra objects, like partitions
 * (armed, alarm, entry time), zones (violation, tamper, alarm), outputs and
 * doors (opened, opened long).
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class IntegraStateCommand extends SatelCommand {
	private static final Logger logger = LoggerFactory.getLogger(IntegraStateCommand.class);

	private StateType stateType;

	/**
	 * Creates new command class instance for specified type of state.
	 * 
	 * @param stateType
	 *            type of state
	 * @param eventDispatcher
	 *            event dispatcher for event distribution
	 */
	public IntegraStateCommand(StateType stateType, EventDispatcher eventDispatcher) {
		super(eventDispatcher);
		this.stateType = stateType;
	}

	/**
	 * Builds message to get stae of objects for specified type.
	 * 
	 * @param stateType
	 *            tpye of state to get
	 * @param extended
	 *            if <code>true</code> command will be sent as extended (256
	 *            zones or outputs)
	 * @return built message object
	 */
	public static SatelMessage buildMessage(StateType stateType, boolean extended) {
		return SatelCommand.buildMessage(stateType.getRefreshCommand(), extended);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleResponse(SatelMessage response) {
		// validate response
		if (response.getCommand() != this.stateType.getRefreshCommand()) {
			logger.error("Invalid response code: {}", response.getCommand());
			return;
		}
		if (!isPayloadLengthValid(response.getPayload().length)) {
			logger.error("Invalid payload length for this object type {}: {}", this.stateType.getObjectType(),
					response.getPayload().length);
			return;
		}
		// dispatch event
		this.getEventDispatcher().dispatchEvent(new IntegraStateEvent(this.stateType, response.getPayload()));
	}

	private boolean isPayloadLengthValid(int length) {
		switch (this.stateType.getObjectType()) {
		case PARTITION:
			return length == 4;
		case ZONE:
		case OUTPUT:
			return length == 16 || length == 32;
		case DOORS:
			return length == 8;
		}
		return false;
	}
}
