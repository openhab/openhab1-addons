/**
 * Copyright (c) 2010-2013, openHAB.org and others.
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
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveConfigurationParameterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Handles the Configuration command class. This allows reading
 * and writing of node configuration parameters
 * @author Chris Jackson
 * @since 1.4.0
 */
@XStreamAlias("configurationCommandClass")
public class ZWaveAssociationCommandClass extends ZWaveCommandClass {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveAssociationCommandClass.class);
	
	private static final int ASSOCIATIONCMD_SET = 0x01;
	private static final int ASSOCIATIONCMD_GET = 0x02;
	private static final int ASSOCIATIONCMD_REPORT = 0x03;
	private static final int ASSOCIATIONCMD_REMOVE = 0x04;
	private static final int ASSOCIATIONCMD_GROUPINGSGET = 0x05;
	private static final int ASSOCIATIONCMD_GROUPINGSREPORT = 0x06;

	/**
	 * Creates a new instance of the ZWaveConfigurationCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveAssociationCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.ASSOCIATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {
		logger.debug(String.format("Received Configuration Request for Node ID = %d", this.getNode().getNodeId()));
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case ASSOCIATIONCMD_SET:
				logger.trace("Process Association Set");
				processConfigurationReport(serialMessage, offset);
				break;
			case ASSOCIATIONCMD_GET:
				logger.trace("Process Association Get");
				return;
			case ASSOCIATIONCMD_REPORT:
				logger.trace("Process Association Report");
				processConfigurationReport(serialMessage, offset);
				break;
			case ASSOCIATIONCMD_REMOVE:
				logger.trace("Process Association Remove");
				return;
			case ASSOCIATIONCMD_GROUPINGSGET:
				logger.trace("Process Association GroupingsGet");
				return;
			case ASSOCIATIONCMD_GROUPINGSREPORT:
				logger.trace("Process Association GroupingsReport");
				return;
			default:
				logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", 
					command, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey()));
		}
	}

	/**
	 * Processes a CONFIGURATIONCMD_REPORT / CONFIGURATIONCMD_SET message.
	 * @param serialMessage the incoming message to process.
	 * @param offset the offset position from which to start message processing.
	 * @param endpoint the endpoint or instance number this message is meant for.
	 */
	protected void processConfigurationReport(SerialMessage serialMessage, int offset) {
        // Extract the parameter index and value
        int parameter = serialMessage.getMessagePayloadByte(offset+1); 
        int size = serialMessage.getMessagePayloadByte(offset+2); 

        // Recover the data
        int value = 0;
        for( int i=0; i<size; ++i ) {
            value <<= 8;
            value |=  serialMessage.getMessagePayloadByte(offset+3+i);
        }

        logger.debug(String.format("Node configuration report from nodeId = %d, parammeter = %d, value = 0x%02X", this.getNode().getNodeId(), parameter, value));

		ZWaveConfigurationParameterEvent zEvent = new ZWaveConfigurationParameterEvent(this.getNode().getNodeId(), parameter, value, size);
		this.getController().notifyEventListeners(zEvent);
	}

	/**
	 * Gets a SerialMessage with the CONFIGURATIONCMD_GET command 
	 * @return the serial message
	 */
	public SerialMessage getConfigMessage(int parameter) {
		logger.debug("Creating new message for application command CONFIGURATIONCMD_GET for node {}", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							3, 
								(byte) getCommandClass().getKey(), 
								(byte) ASSOCIATIONCMD_GET,
								(byte) (parameter & 0xff)};
    	result.setMessagePayload(newPayload);
    	return result;		
	}

	/**
	 * Gets a SerialMessage with the CONFIGURATIONCMD_SET command 
	 * @param the level to set. 0 is mapped to off, > 0 is mapped to on.
	 * @return the serial message
	 */
	public SerialMessage setConfigMessage(int parameter, int value, int size) {
		logger.debug("Creating new message for application command CONFIGURATIONCMD_SET for node {}", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Set);
    	byte[] newPayload = new byte[size + 6];
    	newPayload[0] = (byte) this.getNode().getNodeId();
    	newPayload[1] = (byte) (4 + size);
    	newPayload[2] = (byte) getCommandClass().getKey(); 
    	newPayload[3] =	(byte) ASSOCIATIONCMD_GET;
    	newPayload[4] = (byte) (parameter & 0xFF);
    	newPayload[5] = (byte) (size & 0xFF);

        if( size > 2 )
        {
        	newPayload[6] = (byte) ((value>>24 ) & 0xff );
        	newPayload[7] = (byte) ((value>>16 ) & 0xff );
        	newPayload[8] = (byte) ((value>> 8 ) & 0xff );
        	newPayload[9] = (byte) ((value     ) & 0xff );
        }
        else if( size > 1 ) 
        {
        	newPayload[6] = (byte) ((value>> 8 ) & 0xff );
        	newPayload[7] = (byte) ((value     ) & 0xff );
        }
        else {
        	newPayload[6] = (byte) ((value     ) & 0xff );
        }

    	result.setMessagePayload(newPayload);
    	return result;		
	}
}
