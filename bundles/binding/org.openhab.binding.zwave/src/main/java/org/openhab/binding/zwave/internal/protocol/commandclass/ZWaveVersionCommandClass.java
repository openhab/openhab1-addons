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
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.NodeStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Handles the Version command class. The Version Command Class is 
 * used to obtain the library type, the protocol version
 * used by the node, the individual command class versions 
 * used by the node and the vendor specific application 
 * version from a device.
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
@XStreamAlias("versionCommandClass")
public class ZWaveVersionCommandClass extends ZWaveCommandClass {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveVersionCommandClass.class);

	public static final int VERSION_GET = 0x11;
	public static final int VERSION_REPORT = 0x12;
	public static final int VERSION_COMMAND_CLASS_GET = 0x13;
	public static final int VERSION_COMMAND_CLASS_REPORT = 0x14;
	
	/**
	 * Creates a new instance of the ZWaveVersionCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveVersionCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.VERSION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {
		logger.trace("Handle Message Version Request");
		logger.debug(String.format("Received Version Request for Node ID = %d", this.getNode().getNodeId()));
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case VERSION_GET:
			case VERSION_COMMAND_CLASS_GET:
				logger.warn(String.format("Command 0x%02X not implemented.", command));
				return;
			case VERSION_REPORT:
				logger.debug("Process Version Report");
				int libraryType = serialMessage.getMessagePayloadByte(offset + 1);
				int protocolVersion = serialMessage.getMessagePayloadByte(offset + 2);
				int protocolSubVersion = serialMessage.getMessagePayloadByte(offset + 3);
				int applicationVersion = serialMessage.getMessagePayloadByte(offset + 4);
				int applicationSubVersion = serialMessage.getMessagePayloadByte(offset + 5);
				
				logger.debug(String.format("Node %d Library Type = 0x%02x", this.getNode().getNodeId(), libraryType));
				logger.debug(String.format("Node %d Protocol Version = 0x%02x", this.getNode().getNodeId(), protocolVersion));
				logger.debug(String.format("Node %d Protocol Sub Version = 0x%02x", this.getNode().getNodeId(), protocolSubVersion));
				logger.debug(String.format("Node %d Application Version = 0x%02x", this.getNode().getNodeId(), applicationVersion));
				logger.debug(String.format("Node %d Application Sub Version = 0x%02x", this.getNode().getNodeId(), applicationSubVersion));
				
				// Nothing to do with this info, not exactly useful.
				break;
			case VERSION_COMMAND_CLASS_REPORT:
				logger.debug("Process Version Command Class Report");
				int commandClassCode = serialMessage.getMessagePayloadByte(offset + 1);
				int commandClassVersion = serialMessage.getMessagePayloadByte(offset + 2);
				
				CommandClass commandClass = CommandClass.getCommandClass(commandClassCode);
				if (commandClass == null) {
					logger.error(String.format("Unsupported command class 0x%02x", commandClassCode));
					return;
				}

				logger.debug(String.format("Node %d Requested Command Class = %s (0x%02x)", this.getNode().getNodeId(), commandClass.getLabel() , commandClassCode));
				logger.debug(String.format("Node %d Version = %d", this.getNode().getNodeId(), commandClassVersion));

				// The version is set on the command class for this node. By updating the version, extra functionality is unlocked in the command class.
				// The messages are backwards compatible, so it's not a problem that there is a slight delay when the command class version is queried on the
				// node.
				ZWaveCommandClass zwaveCommandClass = this.getNode().getCommandClass(commandClass);
				if (zwaveCommandClass == null) {
					logger.error(String.format("Unsupported command class %s (0x%02x)", commandClass.getLabel(), commandClassCode));
					return;
				}
				
				if (commandClassVersion > zwaveCommandClass.getMaxVersion()) {
					zwaveCommandClass.setVersion( zwaveCommandClass.getMaxVersion() );
					logger.debug(String.format("Node %d Version = %d, version set to maximum supported by the binding. Enabling extra functionality.", this.getNode().getNodeId(), zwaveCommandClass.getMaxVersion()));
				} else {
					zwaveCommandClass.setVersion( commandClassVersion );
					logger.debug(String.format("Node %d Version = %d, version set. Enabling extra functionality.", this.getNode().getNodeId(), commandClassVersion));
				}
				
				for (ZWaveCommandClass zCC : this.getNode().getCommandClasses()) {
					// wait for all nodes to get/set version information before advancing to the next stage.
					if (zCC.getVersion() == 0)
						return;
				}
				// advance node stage;
				this.getNode().advanceNodeStage(NodeStage.INSTANCES_ENDPOINTS);
					
				break;
			default:
			logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", 
					command, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey()));
		}
	}
	
	/**
	 * Gets a SerialMessage with the VERSION GET command 
	 * @return the serial message
	 */
	public SerialMessage getVersionMessage() {
		logger.debug("Creating new message for application command VERSION_GET for node {}", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) VERSION_GET };
    	result.setMessagePayload(newPayload);
    	return result;		
	}
	
	/**
	 * Gets a SerialMessage with the VERSION COMMAND CLASS GET command.
	 * This version is used to differentiate between multiple versions of a command
	 * and to enable extra functionality. 
	 * @param commandClass The command class to get the version for.
	 * @return the serial message
	 */
	public SerialMessage getCommandClassVersionMessage(CommandClass commandClass) {
	logger.debug("Creating new message for application command VERSION_COMMAND_CLASS_GET for node {} and command class {}", this.getNode().getNodeId(), commandClass.getLabel());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							3, 
								(byte) getCommandClass().getKey(), 
								(byte) VERSION_COMMAND_CLASS_GET,
								(byte) commandClass.getKey()
								};
    	result.setMessagePayload(newPayload);
    	return result;		
	}
	
	/**
	 * Check the version of a command class by sending a VERSION_COMMAND_CLASS_GET message to the node.
	 * @param commandClass the command class to check the version for.
	 */
	public void checkVersion(ZWaveCommandClass commandClass) {
		ZWaveVersionCommandClass versionCommandClass = (ZWaveVersionCommandClass)this.getNode().getCommandClass(CommandClass.VERSION);
		
		if (versionCommandClass == null) {
			logger.error(String.format("Version command class not supported on node %d," +
					"reverting to version 1 for command class %s (0x%02x)", 
					this.getNode().getNodeId(), 
					commandClass.getCommandClass().getLabel(), 
					commandClass.getCommandClass().getKey()));
			return;
		}
		
		this.getController().sendData(versionCommandClass.getCommandClassVersionMessage(commandClass.getCommandClass()));
	};
}
