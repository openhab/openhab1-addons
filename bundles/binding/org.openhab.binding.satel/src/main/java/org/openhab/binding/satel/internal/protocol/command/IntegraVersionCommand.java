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
import org.openhab.binding.satel.internal.event.IntegraVersionEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command class for command that returns Integra version and type.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class IntegraVersionCommand extends SatelCommand {
	private static final Logger logger = LoggerFactory.getLogger(IntegraVersionCommand.class);

	public static final byte COMMAND_CODE = 0x7e;

	/**
	 * Creates new command class instance.
	 * 
	 * @param eventDispatcher
	 *            event dispatcher for event distribution
	 */
	public IntegraVersionCommand(EventDispatcher eventDispatcher) {
		super(eventDispatcher);
	}

	/**
	 * Builds message to get Integra version and type.
	 * 
	 * @return built message object
	 */
	public static SatelMessage buildMessage() {
		return new SatelMessage(COMMAND_CODE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleResponse(SatelMessage response) {
		if (response.getCommand() != COMMAND_CODE) {
			logger.error("Invalid response code: {}", response.getCommand());
			return;
		}
		if (response.getPayload().length != 14) {
			logger.error("Invalid payload length: {}", response.getPayload().length);
			return;
		}
		// build version string
		String verStr = new String(response.getPayload(), 1, 1) + "." + new String(response.getPayload(), 2, 2) + " "
				+ new String(response.getPayload(), 4, 4) + "-" + new String(response.getPayload(), 8, 2) + "-"
				+ new String(response.getPayload(), 10, 2);
		// dispatch version event
		this.getEventDispatcher().dispatchEvent(
				new IntegraVersionEvent(response.getPayload()[0], verStr, response.getPayload()[12], response
						.getPayload()[13] == (byte) 0xFF));
	}
}
