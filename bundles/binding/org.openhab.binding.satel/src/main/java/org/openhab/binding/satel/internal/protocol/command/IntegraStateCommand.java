/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
 * TODO document me!
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class IntegraStateCommand extends SatelCommand {
	private static final Logger logger = LoggerFactory.getLogger(IntegraStateCommand.class);

	private StateType stateType;

	public IntegraStateCommand(StateType stateType, EventDispatcher eventDispatcher) {
		super(eventDispatcher);
		this.stateType = stateType;
	}

	public static SatelMessage buildMessage(StateType stateType, boolean extended) {
		return SatelCommand.buildMessage(stateType.getRefreshCommand(), extended);
	}

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
		case zone:
			return length == 4;
		case input:
		case output:
			return length == 16 || length == 32;
		case doors:
			return length == 8;
		}
		return false;
	}
}
