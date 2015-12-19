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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Handles the Multi Command command class.
 * @author Chris Jackson
 * @since 1.6.0
 */
@XStreamAlias("multiCommandCommandClass")
public class ZWaveMultiCommandCommandClass extends ZWaveCommandClass {

	@XStreamOmitField
	private static final Logger logger = LoggerFactory.getLogger(ZWaveMultiCommandCommandClass.class);

	private static final int MULTI_COMMMAND_ENCAP = 0x01;

	/**
	 * Creates a new instance of the ZWaveMultiCommandCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveMultiCommandCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.MULTI_CMD;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpointId) {
		logger.debug("NODE {}: Received Multi-Command Request", this.getNode().getNodeId());
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
		case MULTI_COMMMAND_ENCAP:			// Multi Cmd Encapsulation
			handleMultiCommandEncapResponse(serialMessage, offset+1);
			break;
		}
	}
	
	/**
	 * Handle the multi command message. This processes the received frame, processing each
	 * command class in turn.
	 * 
	 * @param serialMessage The received message
	 * @param offset The starting offset into the payload
	 */
	private void handleMultiCommandEncapResponse(
			SerialMessage serialMessage, int offset) {
		logger.trace("Process Multi-command Encapsulation");
		
		int classCnt = serialMessage.getMessagePayloadByte(offset++);

		// Iterate over all commands
		for(int c = 0; c < classCnt; c++) {
			CommandClass commandClass;
			ZWaveCommandClass zwaveCommandClass;
			int commandClassCode = serialMessage.getMessagePayloadByte(offset + 1);
			commandClass = CommandClass.getCommandClass(commandClassCode);			
			if (commandClass == null) {
				logger.error(String.format("NODE %d: Unknown command class 0x%02x", getNode().getNodeId(), commandClassCode));
			}
			else {
				logger.debug("NODE {}: Incoming command class {}", getNode().getNodeId(), commandClass.getLabel());
				zwaveCommandClass  = getNode().getCommandClass(commandClass);
			
				// Apparently, this node supports a command class that we did not get (yet) during initialization.
				// Let's add it now then to support handling this message.
				if (zwaveCommandClass == null) {
					logger.debug("NODE {}: Command class {} not found, trying to add it.", 
							getNode().getNodeId(), commandClass.getLabel(), commandClass.getKey());
					
					zwaveCommandClass = ZWaveCommandClass.getInstance(commandClass.getKey(), getNode(), this.getController());
	
					if (zwaveCommandClass != null) {
						logger.debug("NODE {}: Adding command class {}", getNode().getNodeId(), commandClass.getLabel());
						getNode().addCommandClass(zwaveCommandClass);
					}
				}
	
				if (zwaveCommandClass == null) {
					logger.error("NODE {}: CommandClass %s not implemented.", this.getNode().getNodeId(), commandClass.getLabel());
				}
				else {
					logger.debug("NODE {}: Calling handleApplicationCommandRequest.", this.getNode().getNodeId());
					zwaveCommandClass.handleApplicationCommandRequest(serialMessage, offset + 2, 0);
				}
			}

			// Step over this class
			offset += serialMessage.getMessagePayloadByte(offset) + 1;
		}
	}
}
