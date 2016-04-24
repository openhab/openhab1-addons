/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.oppoblurayplayer.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.oppoblurayplayer.connector.OppoBlurayPlayerConnector;
import org.openhab.binding.oppoblurayplayer.connector.OppoBlurayPlayerSerialConnector;
import org.openhab.binding.oppoblurayplayer.i18n.Messages;
import org.openhab.binding.oppoblurayplayer.internal.core.OppoBlurayPlayerCommandReceiver;
import org.openhab.binding.oppoblurayplayer.internal.core.OppoBlurayPlayerCommandSender;
import org.openhab.core.events.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provide high level interface to Oppo Bluray player.
 * 
 * @author netwolfuk (http://netwolfuk.wordpress.com)
 * @since 1.9.0
 */
public class OppoBlurayPlayerDevice {

	public enum Switch {
		ON, OFF;
	}

	final private int defaultTimeout = 5000;
	final private int powerStateTimeout = 40000;

	private static Logger logger = LoggerFactory
			.getLogger(OppoBlurayPlayerDevice.class);

	private OppoBlurayPlayerConnector connection = null;
	private boolean connected = false;
	private OppoBlurayPlayerCommandSender sender;
	private OppoBlurayPlayerCommandReceiver receiver;
	
	public OppoBlurayPlayerDevice(String serialPort, String deviceId, OppoBlurayPlayerBinding binding) {
		connection = (OppoBlurayPlayerConnector) new OppoBlurayPlayerSerialConnector(serialPort);
		receiver = new OppoBlurayPlayerCommandReceiver(binding, deviceId);
		sender = new OppoBlurayPlayerCommandSender(connection, defaultTimeout);
	}

	protected void sendQuery(OppoBlurayPlayerCommand command) throws OppoBlurayPlayerException {
		logger.debug("Query: '{}'", command.getQuery().toString());
		sender.sendQuery(command);
	}

	protected void sendCommand(OppoBlurayPlayerCommand command) throws OppoBlurayPlayerException {
		logger.debug("Command: '{}'", command.getCommandString().toString());
		sender.sendCommand(command);
	}

	public void connect() throws OppoBlurayPlayerException {
		connection.connect();
		Thread senderThread = new Thread(sender);
		senderThread.start();
		Thread receiverThread = new Thread(receiver);
		receiverThread.start();
		connection.registerCommandReceiver(receiver);
		connected = true;
	}

	public void disconnect() throws OppoBlurayPlayerException {
		connection.unregisterCommandReceiver(receiver);
		sender.stop();
		receiver.stop();
		connection.disconnect();
		connected = false;
	}
	
	public boolean isConnected() {
		return connected;
	}

}
