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
import org.openhab.binding.satel.internal.protocol.SatelMessage;

/**
 * Command class for command that clear troubles memory.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class ClearTroublesCommand extends SatelCommand {

	public static final byte COMMAND_CODE = (byte) 0x8b;

	/**
	 * Creates new command class instance.
	 * 
	 * @param eventDispatcher
	 *            event dispatcher for event distribution
	 */
	public ClearTroublesCommand(EventDispatcher eventDispatcher) {
		super(eventDispatcher);
	}

	/**
	 * Builds message to clear trouble memory.
	 * 
	 * @param userCode
	 *            code of the user on behalf the control is made
	 * @return built message object
	 */
	public static SatelMessage buildMessage(String userCode) {
		return new SatelMessage(COMMAND_CODE, userCodeToBytes(userCode));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleResponse(SatelMessage response) {
		if (commandSucceeded(response)) {
			// TODO force refresh
		}
	}
}
