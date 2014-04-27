/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.openhab.binding.maxcube.internal.MaxTokenizer;
import org.slf4j.Logger;

/**
 * The L message contains real time information about all MAX! devices.
 * 
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public final class L_Message extends Message {

	public L_Message(String raw) {
		super(raw);
	}

	public Collection<? extends Device> getDevices(List<Configuration> configurations) {

		List<Device> devices = new ArrayList<Device>();

		byte[] decodedRawMessage = Base64.decodeBase64(getPayload().getBytes());

		MaxTokenizer tokenizer = new MaxTokenizer(decodedRawMessage);

		while (tokenizer.hasMoreElements()) {
			byte[] token = tokenizer.nextElement();
			Device tempDevice = Device.create(token, configurations);
			if (tempDevice != null) {
				devices.add(tempDevice);
			}
		}

		return devices;
	}

	@Override
	public void debug(Logger logger) {
		logger.debug("=== L_Message === ");
		logger.debug("\tRAW:" + this.getPayload());
	}

	@Override
	public MessageType getType() {
		return MessageType.L;
	}
}