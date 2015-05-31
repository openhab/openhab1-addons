/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.protocol.command;

import java.util.Calendar;

import org.openhab.binding.satel.internal.event.EventDispatcher;
import org.openhab.binding.satel.internal.event.IntegraStatusEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command class for command that returns Integra RTC and basic status.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class IntegraStatusCommand extends SatelCommand {
	private static final Logger logger = LoggerFactory.getLogger(IntegraStatusCommand.class);

	public static final byte COMMAND_CODE = 0x1a;

	/**
	 * Creates new command class instance.
	 * 
	 * @param eventDispatcher
	 *            event dispatcher for event distribution
	 */
	public IntegraStatusCommand(EventDispatcher eventDispatcher) {
		super(eventDispatcher);
	}

	/**
	 * Builds message to get Integra RTC and status.
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
		if (response.getPayload().length != 9) {
			logger.error("Invalid payload length: {}", response.getPayload().length);
			return;
		}
		// parse current date and time
		byte[] bytes = response.getPayload();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, bcdToInt(bytes, 0, 2));
		c.set(Calendar.MONTH, bcdToInt(bytes, 2, 1)-1);
		c.set(Calendar.DAY_OF_MONTH, bcdToInt(bytes, 3, 1));
		c.set(Calendar.HOUR_OF_DAY, bcdToInt(bytes, 4, 1));
		c.set(Calendar.MINUTE, bcdToInt(bytes, 5, 1));
		c.set(Calendar.SECOND, bcdToInt(bytes, 6, 1));
		// dispatch version event
		this.getEventDispatcher().dispatchEvent(new IntegraStatusEvent(c, bytes[7], bytes[8]));
	}
}
