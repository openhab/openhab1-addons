/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import org.openhab.binding.zwave.internal.protocol.ZWaveNodeState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Handles the Hail command class. Some devices handle state changes by
 * 'hailing' the controller and asking it to request the device values
 * @author Ben Jones
 * @since 1.4.0
 */
@XStreamAlias("hailCommandClass")
public class ZWaveHailCommandClass extends ZWaveCommandClass {

	@XStreamOmitField
	private static final Logger logger = LoggerFactory.getLogger(ZWaveHailCommandClass.class);
	
	private static final int HAIL = 1;
	
	/**
	 * Creates a new instance of the ZWaveHailCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveHailCommandClass(ZWaveNode node, 
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.HAIL;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage, 
			int offset, int endpoint) {
		logger.trace("Handle Message Manufacture Specific Request");
		logger.debug("NODE {}: Received Hail command (v{})", this.getNode().getNodeId(), this.getVersion());
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case HAIL:
				logger.trace("Process Hail command");
				
				logger.debug("NODE {}: Request an update of the dynamic values", this.getNode().getNodeId());
				
				// We only re-request dynamic values for nodes that are completely initialized.
				if (this.getNode().getNodeState() != ZWaveNodeState.ALIVE || this.getNode().isInitializationComplete() == false) {
					return;
				}
				
				getController().pollNode(getNode());
				
				break;
			default:
			logger.warn(String.format("NODE %d: Unsupported Command 0x%02X for command class %s (0x%02X).",
					this.getNode().getNodeId(),
					command, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey()));
		}
	}
}

