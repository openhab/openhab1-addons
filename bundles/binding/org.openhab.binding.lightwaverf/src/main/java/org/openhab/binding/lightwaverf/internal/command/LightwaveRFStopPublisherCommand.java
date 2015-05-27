/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.command;

import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.core.types.State;

/**
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRFStopPublisherCommand implements LightwaveRFCommand {

	public String getLightwaveRfCommandString() {
		return null;
	}

	public String getRoomId() {
		return null;
	}

	public String getDeviceId() {
		return null;
	}

	@Override
	public State getState(LightwaveRfType type) {
		return null;
	}

	public LightwaveRfMessageId getMessageId() {
		return null;
	}

	@Override
	public LightwaveRfMessageType getMessageType() {
		return null;
	}

}
