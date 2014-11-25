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
 * TODO document me!
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class ControlObjectCommand extends SatelCommand {
	private static final Logger logger = LoggerFactory.getLogger(ControlObjectCommand.class);
	
	public static final byte RESPONSE_CODE = (byte) 0xef;

	private ControlType controlType;

	public ControlObjectCommand(ControlType controlType, EventDispatcher eventDispatcher) {
		super(eventDispatcher);
		this.controlType = controlType;
	}

	public static SatelMessage buildMessage(ControlType controlType, byte[] objects, String userCode) {
		return new SatelMessage(controlType.getControlCommand(), ArrayUtils.addAll(userCodeToBytes(userCode), objects));
	}

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
