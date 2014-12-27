/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.protocol.command;

import java.util.BitSet;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.satel.internal.event.EventDispatcher;
import org.openhab.binding.satel.internal.event.NewStatesEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.openhab.binding.satel.internal.types.ControlType;
import org.openhab.binding.satel.internal.types.OutputControl;
import org.openhab.binding.satel.internal.types.OutputState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command class for commands that control (change) state of Integra objects,
 * like zones (arm, disarm), inputs (bypass, unbypass) outputs (on, off,
 * switch), etc.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class ControlObjectCommand extends SatelCommand {
	private static final Logger logger = LoggerFactory.getLogger(ControlObjectCommand.class);

	private static final byte RESPONSE_CODE = (byte) 0xef;

	private ControlType controlType;

	/**
	 * Creates new command class instance for specified type of control.
	 * 
	 * @param controlType
	 *            type of control
	 * @param eventDispatcher
	 *            event dispatcher for event distribution
	 */
	public ControlObjectCommand(ControlType controlType, EventDispatcher eventDispatcher) {
		super(eventDispatcher);
		this.controlType = controlType;
	}

	/**
	 * Builds message to control objects of specified type.
	 * 
	 * @param controlType
	 *            type of controlled objects
	 * @param objects
	 *            bits that represents objects to control
	 * @param userCode
	 *            code of the user on behalf the control is made
	 * @return built message object
	 */
	public static SatelMessage buildMessage(ControlType controlType, byte[] objects, String userCode) {
		return new SatelMessage(controlType.getControlCommand(), ArrayUtils.addAll(userCodeToBytes(userCode), objects));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleResponse(SatelMessage response) {
		// validate response
		if (response.getCommand() != RESPONSE_CODE) {
			logger.error("Invalid response code: {}", response.getCommand());
			return;
		}
		if (response.getPayload().length != 1) {
			logger.error("Invalid payload length: {}", response.getPayload().length);
			return;
		}
		if (commandSucceeded(response)) {
			// force outputs refresh
			BitSet newStates = new BitSet(48);
			// TODO generalize for all kinds of control
			if (this.controlType instanceof OutputControl) {
				newStates.set(OutputState.output.getRefreshCommand());
				this.getEventDispatcher().dispatchEvent(new NewStatesEvent(newStates));
			}
		}
	}
}
