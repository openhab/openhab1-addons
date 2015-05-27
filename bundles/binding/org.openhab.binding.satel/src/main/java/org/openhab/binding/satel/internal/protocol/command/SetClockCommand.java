/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.protocol.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.satel.internal.event.EventDispatcher;
import org.openhab.binding.satel.internal.protocol.SatelMessage;

/**
 * Command class for command to set RTC clock.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class SetClockCommand extends SatelCommand {

	public static final byte COMMAND_CODE = (byte) 0x8e;

	private static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * Creates new command class instance.
	 * 
	 * @param eventDispatcher
	 *            event dispatcher for event distribution
	 */
	public SetClockCommand(EventDispatcher eventDispatcher) {
		super(eventDispatcher);
	}

	/**
	 * Builds message to set RTC clock.
	 * 
	 * @param dateTime
	 *            date and time to set
	 * @param userCode
	 *            code of the user on behalf the control is made
	 * @return built message object
	 */
	public static SatelMessage buildMessage(Calendar dateTime, String userCode) {
		byte[] dateTimeBytes = DATETIME_FORMAT.format(dateTime.getTime()).getBytes();
		return new SatelMessage(COMMAND_CODE, ArrayUtils.addAll(userCodeToBytes(userCode), dateTimeBytes));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleResponse(SatelMessage response) {
		if (commandSucceeded(response)) {
			// TODO force refresh?
		}
	}
}
