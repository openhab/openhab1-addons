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
import org.openhab.binding.satel.internal.event.NewStatesEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command class for command that returns list of states changed since last state read.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class NewStatesCommand extends SatelCommand {
	private static final Logger logger = LoggerFactory.getLogger(NewStatesCommand.class);

	public static final byte COMMAND_CODE = 0x7f;

	/**
	 * Creates new command class instance.
	 * @param eventDispatcher event dispatcher for event distribution
	 */
	public NewStatesCommand(EventDispatcher eventDispatcher) {
		super(eventDispatcher);
	}
	
	/**
	 * Builds message to get list of new states.
	 * @param extended
	 *            if <code>true</code> command will be sent as extended (256
	 *            zones or outputs)
	 * @return built message object
	 */
	public static SatelMessage buildMessage(boolean extended) {
		return SatelCommand.buildMessage(COMMAND_CODE, extended);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleResponse(SatelMessage response) {
		// validate response
		if (response.getCommand() != COMMAND_CODE) {
			logger.error("Invalid response code: {}", response.getCommand());
			return;
		}
		if (response.getPayload().length < 5 || response.getPayload().length > 6) {
			logger.error("Invalid payload length: {}", response.getPayload().length);
			return;
		}
		// dispatch event
		this.getEventDispatcher().dispatchEvent(new NewStatesEvent(response.getPayload()));
	}
}
