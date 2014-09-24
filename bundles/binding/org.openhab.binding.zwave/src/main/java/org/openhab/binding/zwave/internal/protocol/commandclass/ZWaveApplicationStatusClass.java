/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Handles the Application Status command class.
 * @author Dan Cunningham
 * @since 1.6.0
 */
@XStreamAlias("ZWaveApplicationStatusClass")
public class ZWaveApplicationStatusClass extends ZWaveCommandClass {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveApplicationStatusClass.class);
	
	private static final int ApplicationStatusBusy = 0x1;
	private static final int ApplicationStatusRejected = 0x2;
	
	private static final int StatusBusyTryAgainLater = 0x1;
	private static final int StatusBusyTryAgainLaterInSeconds = 0x2;
	private static final int StatusBusyQueued= 0x3;
	
	
	
	/**
	 * Creates a new instance of the ZWaveApplicationStatusClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveApplicationStatusClass(ZWaveNode node, 
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.APPLICATION_STATUS;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage, 
			int offset, int endpoint) {
		logger.debug("NODE {} Application Status message", getNode());
		int status = serialMessage.getMessagePayloadByte(offset++);
		switch (status) {
			case ApplicationStatusBusy:
				logger.trace("NODE {} Process ApplicationStatusBusy status", getNode());
				int busyStatus = serialMessage.getMessagePayloadByte(offset++);
				switch(busyStatus){
					case StatusBusyTryAgainLater:
						logger.warn("NODE {} is busy and wants us to try again later", getNode());
						break;
					case StatusBusyTryAgainLaterInSeconds:
						int seconds = serialMessage.getMessagePayloadByte(offset++);
						logger.warn("NODE {} is busy and wants us to try again in {} seconds",getNode(), seconds);
						break;
					case StatusBusyQueued:
						logger.warn("NODE {} is busy and has queued the request", getNode());
						break;
				}
				break;
			case ApplicationStatusRejected:
				logger.warn("NODE {} has rejected the request", getNode());
				break;
			default:
			logger.warn(String.format("Unsupported status 0x%02X for command class %s (0x%02X).", 
					status, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey()));
		}
	}
}

