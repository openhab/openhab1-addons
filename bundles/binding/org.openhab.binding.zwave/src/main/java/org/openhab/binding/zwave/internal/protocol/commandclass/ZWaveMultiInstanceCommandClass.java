/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Basic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Generic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Specific;
import org.openhab.binding.zwave.internal.protocol.NodeStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Handles the Multi Instance / Multi Channel command class. The
 * Multi Instance command class is used to control multiple instances
 * of the same device class on the node. Multi Channel support (version 2)
 * of the command class can also handle multiple instances of different
 * command classes. The instances are called endpoints in this version.
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
@XStreamAlias("multiInstanceCommandClass")
public class ZWaveMultiInstanceCommandClass extends ZWaveCommandClass {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveMultiInstanceCommandClass.class);
	private static final int MAX_SUPPORTED_VERSION = 2;
	
    // Version 1
	private static final int MULTI_INSTANCE_GET = 0x04;
	private static final int MULTI_INSTANCE_REPORT = 0x05;
	private static final int MULTI_INSTANCE_ENCAP = 0x06;

    // Version 2
	private static final int MULTI_CHANNEL_ENDPOINT_GET = 0x07;
	private static final int MULTI_CHANNEL_ENDPOINT_REPORT = 0x08;
	private static final int MULTI_CHANNEL_CAPABILITY_GET = 0x09;
	private static final int MULTI_CHANNEL_CAPABILITY_REPORT = 0x0a;
	private static final int MULTI_CHANNEL_ENDPOINT_FIND = 0x0b;
	private static final int MULTI_CHANNEL_ENDPOINT_FIND_REPORT = 0x0c;
	private static final int MULTI_CHANNEL_ENCAP = 0x0d;
	
	private final Map<Integer, ZWaveEndpoint> endpoints = new HashMap<Integer, ZWaveEndpoint>();
	
	private boolean endpointsAreTheSameDeviceClass;
	
	/**
	 * Creates a new instance of the ZWaveMultiInstanceCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveMultiInstanceCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.MULTI_INSTANCE;
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	public int getMaxVersion() {
		return MAX_SUPPORTED_VERSION;
	};

	/**
	 * Gets the endpoint object using it's endpoint ID as key.
	 * Returns null if the endpoint is not found.
	 * @param endpointId the endpoint ID of the endpoint to get.
	 * @return Endpoint object
	 * @throws IllegalArgumentException thrown when the endpoint is not found.
	 */
	public ZWaveEndpoint getEndpoint(int endpointId){
		
		return this.endpoints.get(endpointId);
	}
	
	/**
	 * Gets the collection of endpoints attached to this node.
	 * @return the collection of endpoints.
	 */
	public Collection<ZWaveEndpoint> getEndpoints() {
		return this.endpoints.values();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpointId) {
		logger.trace("Handle Message Multi-instance/Multi-channel Request");
		logger.debug(String.format("Received Multi-instance/Multi-channel Request for Node ID = %d", this.getNode().getNodeId()));
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
			case MULTI_INSTANCE_GET:
			case MULTI_CHANNEL_ENDPOINT_GET:
			case MULTI_CHANNEL_CAPABILITY_GET:
			case MULTI_CHANNEL_ENDPOINT_FIND:
			case MULTI_CHANNEL_ENDPOINT_FIND_REPORT:
				logger.warn(String.format("Command 0x%02X not implemented.", command));
				return;
			case MULTI_INSTANCE_REPORT:
				handleMultiInstanceReportResponse(serialMessage, offset + 1);
				break;
			case MULTI_INSTANCE_ENCAP:
				handleMultiInstanceEncapResponse(serialMessage, offset + 1);
				break;
			case MULTI_CHANNEL_ENDPOINT_REPORT:
				handleMultiChannelEndpointReportResponse(serialMessage, offset + 1);
				break;
			case MULTI_CHANNEL_CAPABILITY_REPORT:
				handleMultiChannelCapabilityReportResponse(serialMessage, offset + 1);
				break;
			case MULTI_CHANNEL_ENCAP:				
				handleMultiChannelEncapResponse(serialMessage, offset + 1);
				break;
			default:
			logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", 
					command, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey()));
		}
	}

	/**
	 * Handles Multi Instance Report message. Handles Report on
	 * the number of instances for the command class.
	 * @param serialMessage the serial message to process.
	 * @param offset the offset at which to start procesing.
	 */
	private void handleMultiInstanceReportResponse(SerialMessage serialMessage,
			int offset) {
		logger.trace("Process Multi-instance Report");
		int commandClassCode = serialMessage.getMessagePayloadByte(offset);
		int instances = serialMessage.getMessagePayloadByte(offset + 1);

		if (instances == 0) {
			setInstances(1);
		} else 
		{
			CommandClass commandClass = CommandClass.getCommandClass(commandClassCode);
			
			if (commandClass == null) {
				logger.error(String.format("Unsupported command class 0x%02x", commandClassCode));
				return;
			}
			
			logger.debug(String.format("Node %d Requested Command Class = %s (0x%02x)", this.getNode().getNodeId(), commandClass.getLabel() , commandClassCode));
			ZWaveCommandClass zwaveCommandClass = this.getNode().getCommandClass(commandClass);
			
			if (zwaveCommandClass == null) {
				logger.error(String.format("Unsupported command class %s (0x%02x)", commandClass.getLabel(), commandClassCode));
				return;
			}
			
			zwaveCommandClass.setInstances(instances);
			logger.debug(String.format("Node %d Instances = %d, number of instances set.", this.getNode().getNodeId(), instances));
		}
		
		for (ZWaveCommandClass zwaveCommandClass : this.getNode().getCommandClasses())
			if (zwaveCommandClass.getInstances() == 0) // still waiting for an instance report of another command class. 
				return;
		
		// advance node stage.
		this.getNode().advanceNodeStage(NodeStage.STATIC_VALUES);
	}
	
	/**
	 * Handles Multi Instance Encapsulation message. Decapsulates 
	 * an Application Command message and handles it using the right
	 * instance.
	 * @param serialMessage the serial message to process.
	 * @param offset the offset at which to start procesing.
	 */
	private void handleMultiInstanceEncapResponse(
			SerialMessage serialMessage, int offset) {
		logger.trace("Process Multi-instance Encapsulation");
		int instance = serialMessage.getMessagePayloadByte(offset);
		int commandClassCode = serialMessage.getMessagePayloadByte(offset + 1);
		CommandClass commandClass = CommandClass.getCommandClass(commandClassCode);

		if (commandClass == null) {
			logger.error(String.format("Unsupported command class 0x%02x", commandClassCode));
			return;
		}
		
		logger.debug(String.format("Node %d Requested Command Class = %s (0x%02x)", this.getNode().getNodeId(), commandClass.getLabel() , commandClassCode));
		ZWaveCommandClass zwaveCommandClass = this.getNode().getCommandClass(commandClass);
		
		if (zwaveCommandClass == null) {
			logger.error(String.format("Unsupported command class %s (0x%02x)", commandClass.getLabel(), commandClassCode));
			return;
		}
		
		logger.debug(String.format("Node %d, Instance = %d, calling handleApplicationCommandRequest.", this.getNode().getNodeId(), instance));
		zwaveCommandClass.handleApplicationCommandRequest(serialMessage, offset+ 3, instance);
	}
	
	/**
	 * Handles Multi Channel Endpoint Report message. Handles Report on
	 * the number of endpoints and whether they are dynamic and/or have the
	 * same command classes.
	 * @param serialMessage the serial message to process.
	 * @param offset the offset at which to start processing.
	 */
	private void handleMultiChannelEndpointReportResponse(
			SerialMessage serialMessage, int offset) {
		logger.debug("Process Multi-channel endpoint Report");
		
		boolean changingNumberOfEndpoints = (serialMessage.getMessagePayloadByte(offset) & 0x80) != 0;
		endpointsAreTheSameDeviceClass = (serialMessage.getMessagePayloadByte(offset) & 0x40) != 0;
		int endpoints = serialMessage.getMessagePayloadByte(offset + 1) & 0x7F;
		
		logger.debug("Changing number of endpoints = {}", changingNumberOfEndpoints ? "true" : false);
		logger.debug("Endpoints are the same device class = {}", endpointsAreTheSameDeviceClass ? "true" : false);
		logger.debug("Number of endpoints = {}", endpoints);

		// TODO: handle dynamically added endpoints. Have never seen such a device.
		if (changingNumberOfEndpoints)
			logger.warn("Changing number of endpoints, expect some weird behavior during multi channel handling.");
		
		for (int i=1; i <= endpoints; i++) {
			ZWaveEndpoint endpoint = new ZWaveEndpoint(i);
			this.endpoints.put(i, endpoint);
			if (!endpointsAreTheSameDeviceClass || i == 1)
				this.getController().sendData(this.getMultiChannelCapabilityGetMessage(endpoint));
		}
	}
	
	/**
	 * Handles Multi Channel Capability Report message. Handles Report on
	 * an endpoint and adds command classes to the endpoint.
	 * @param serialMessage the serial message to process.
	 * @param offset the offset at which to start processing.
	 */
	private void handleMultiChannelCapabilityReportResponse(SerialMessage serialMessage, int offset) {
		logger.debug("Process Multi-channel capability Report");
		int receivedEndpointId = serialMessage.getMessagePayloadByte(offset) & 0x7F;
		boolean dynamic = ((serialMessage.getMessagePayloadByte(offset) & 0x80) != 0);
		int genericDeviceClass = serialMessage.getMessagePayloadByte(offset + 1);
		int specificDeviceClass = serialMessage.getMessagePayloadByte(offset + 2);
		
		logger.debug("Endpoints are the same device class = {}", endpointsAreTheSameDeviceClass ? "true" : false);

		// Loop either all endpoints, or just set command classes on one, depending on whether
		// all endpoints have the same device class.
		int startId = this.endpointsAreTheSameDeviceClass ? 1 : receivedEndpointId;
		int endId = this.endpointsAreTheSameDeviceClass ? this.endpoints.size() : receivedEndpointId;
		
		boolean supportsBasicCommandClass = this.getNode().supportsCommandClass(CommandClass.BASIC);
		
		for (int endpointId = startId; endpointId <= endId; endpointId++) {
			ZWaveEndpoint endpoint = this.endpoints.get(endpointId);
			
			if (endpoint == null){
				logger.error("Endpoint {} not found on node {}. Cannot set command classes.", endpointId, this.getNode().getNodeId());
				continue;
			}
	
			Basic basic = this.getNode().getDeviceClass().getBasicDeviceClass();
			Generic generic = Generic.getGeneric(genericDeviceClass);
			if (generic == null) {
				logger.error(String.format("Endpoint %d has invalid device class. generic = 0x%02x, specific = 0x%02x.", 
						endpoint, genericDeviceClass, specificDeviceClass));
				continue;
			}
			Specific specific = Specific.getSpecific(generic, specificDeviceClass);
			if (specific == null) {
				logger.error(String.format("Endpoint %d has invalid device class. generic = 0x%02x, specific = 0x%02x.", 
						endpoint, genericDeviceClass, specificDeviceClass));
				continue;
			}
			
			logger.debug("Endpoint Id = {}", endpointId);
			logger.debug("Endpoints is dynamic = {}", dynamic ? "true" : false);
			logger.debug(String.format("Basic = %s 0x%02x", basic.getLabel(), basic.getKey()));
			logger.debug(String.format("Generic = %s 0x%02x", generic.getLabel(), generic.getKey()));
			logger.debug(String.format("Specific = %s 0x%02x", specific.getLabel(), specific.getKey()));
			
			ZWaveDeviceClass deviceClass = endpoint.getDeviceClass();
			deviceClass.setBasicDeviceClass(basic);
			deviceClass.setGenericDeviceClass(generic);
			deviceClass.setSpecificDeviceClass(specific);
			
			// add basic command class, if it's also supported by the parent node.
			if (supportsBasicCommandClass) {
				ZWaveCommandClass commandClass = new ZWaveBasicCommandClass(this.getNode(), this.getController(), endpoint);
				endpoint.addCommandClass(commandClass);
			}
			
			for (int i = 0; i < serialMessage.getMessagePayload().length - offset - 3; i++) {
				int data = serialMessage.getMessagePayloadByte(offset + 3 + i);
				if(data == 0xef )  {
					// TODO: Implement control command classes
					break;
				}
				logger.debug(String.format("Adding command class 0x%02X to the list of supported command classes.", data));
				ZWaveCommandClass commandClass = ZWaveCommandClass.getInstance(data, this.getNode(), this.getController(), endpoint);
				
				if (commandClass == null)
					continue;
				
				endpoint.addCommandClass(commandClass);
				
				ZWaveCommandClass parentClass = this.getNode().getCommandClass(commandClass.getCommandClass());
				
				// copy version info to endpoint classes.
				if (parentClass != null)
					commandClass.setVersion(parentClass.getVersion());
			}
		}
		
		if (this.endpointsAreTheSameDeviceClass)
			// advance node stage.
			this.getNode().advanceNodeStage(NodeStage.STATIC_VALUES);
		else {
			for (ZWaveEndpoint ep : this.endpoints.values())
			{
				if (ep.getDeviceClass().getBasicDeviceClass() == Basic.NOT_KNOWN) // only advance node stage when all endpoints are known.
					return;
			}
			// advance node stage.
			this.getNode().advanceNodeStage(NodeStage.STATIC_VALUES);
		}
	}
	
	/**
	 * Handles Multi Channel Encapsulation message. Decapsulates 
	 * an Application Command message and handles it using the right
	 * endpoint.
	 * @param serialMessage the serial message to process.
	 * @param offset the offset at which to start procesing.
	 */
	private void handleMultiChannelEncapResponse(
			SerialMessage serialMessage, int offset) {
		logger.trace("Process Multi-channel Encapsulation");
		CommandClass commandClass;
		ZWaveCommandClass zwaveCommandClass;
		int endpointId = serialMessage.getMessagePayloadByte(offset);
		int commandClassCode = serialMessage.getMessagePayloadByte(offset + 2);
		commandClass = CommandClass.getCommandClass(commandClassCode);
		
		if (commandClass == null) {
			logger.error(String.format("Unsupported command class 0x%02x", commandClassCode));
			return;
		}
		
		logger.debug(String.format("Node %d Requested Command Class = %s (0x%02x)", this.getNode().getNodeId(), commandClass.getLabel() , commandClassCode));
		ZWaveEndpoint endpoint = this.endpoints.get(endpointId);
		
		if (endpoint == null){
			logger.error("Endpoint {} not found on node {}. Cannot set command classes.", endpointId, this.getNode().getNodeId());
			return;
		}
		
		zwaveCommandClass = endpoint.getCommandClass(commandClass);
		
		if (zwaveCommandClass == null) {
			logger.warn(String.format("CommandClass %s (0x%02x) not implemented by endpoint %d, fallback to main node.", commandClass.getLabel(), commandClassCode, endpointId));
			zwaveCommandClass = this.getNode().getCommandClass(commandClass);
		}
		
		if (zwaveCommandClass == null) {
			logger.error(String.format("CommandClass %s (0x%02x) not implemented by node %d.", commandClass.getLabel(), commandClassCode, this.getNode().getNodeId()));
			return;
		}
		
		logger.debug(String.format("Node %d, Endpoint = %d, calling handleApplicationCommandRequest.", this.getNode().getNodeId(), endpointId));
		zwaveCommandClass.handleApplicationCommandRequest(serialMessage, offset + 3, endpointId);
	}
	
	/**
	 * Gets a SerialMessage with the MULTI INSTANCE GET command.
	 * Returns the number of instances for this command class.
	 * @param the command class to return the number of instances for.
	 * @return the serial message.
	 */
	public SerialMessage getMultiInstanceGetMessage(CommandClass commandClass) {
		logger.debug("Creating new message for application command MULTI_INSTANCE_GET for node {} and command class {}", this.getNode().getNodeId(), commandClass.getLabel());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							3, 
								(byte) getCommandClass().getKey(), 
								(byte) MULTI_INSTANCE_GET,
								(byte) commandClass.getKey()
								};
    	result.setMessagePayload(newPayload);
    	return result;		
	}
	
	/**
	 * Gets a SerialMessage with the MULTI INSTANCE ENCAP command.
	 * Encapsulates a message for a specific instance.
	 * @param serialMessage the serial message to encapsulate
	 * @param instance the number of the instance to encapsulate the message for.
	 * @return the encapsulated serial message.
	 */
	public SerialMessage getMultiInstanceEncapMessage(SerialMessage serialMessage, int instance) {
		logger.debug("Creating new message for application command MULTI_INSTANCE_ENCAP for node {} and instance {}", this.getNode().getNodeId(), instance);
		
		byte[] payload = serialMessage.getMessagePayload();
		byte[] newPayload = new byte[payload.length + 3];
		System.arraycopy(payload, 0, newPayload, 0, 2);
		System.arraycopy(payload, 0, newPayload, 3, payload.length);
		newPayload[1] += 3;
		newPayload[2] = (byte) this.getCommandClass().getKey();
		newPayload[3] = MULTI_INSTANCE_ENCAP;
		newPayload[4] = (byte)(instance);
		
		serialMessage.setMessagePayload(newPayload);
    	return serialMessage;		
	}
	
	/**
	 * Gets a SerialMessage with the MULTI CHANNEL ENDPOINT GET command.
	 * Returns the endpoints for this node.
	 * @return the serial message.
	 */
	public SerialMessage getMultiChannelEndpointGetMessage() {
		logger.debug("Creating new message for application command MULTI_CHANNEL_ENDPOINT_GET for node {}", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) MULTI_CHANNEL_ENDPOINT_GET };
    	result.setMessagePayload(newPayload);
    	return result;		
	}
	
	/**
	 * Gets a SerialMessage with the MULTI CHANNEL CAPABILITY GET command.
	 * Gets the capabilities for a specific endpoint.
	 * @param the number of the endpoint to get the 
	 * @return the serial message.
	 */
	public SerialMessage getMultiChannelCapabilityGetMessage(ZWaveEndpoint endpoint) {
		logger.debug("Creating new message for application command MULTI_CHANNEL_CAPABILITY_GET for node {} and endpoint {}", this.getNode().getNodeId(), endpoint.getEndpointId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							3, 
								(byte) getCommandClass().getKey(), 
								(byte) MULTI_CHANNEL_CAPABILITY_GET,
								(byte) endpoint.getEndpointId() };
    	result.setMessagePayload(newPayload);
    	return result;
	}
	
	/**
	 * Gets a SerialMessage with the MULTI INSTANCE ENCAP command.
	 * Encapsulates a message for a specific instance.
	 * @param serialMessage the serial message to encapsulate
	 * @param endpoint the endpoint to encapsulate the message for.
	 * @return the encapsulated serial message.
	 */
	public SerialMessage getMultiChannelEncapMessage(SerialMessage serialMessage, ZWaveEndpoint endpoint) {
		logger.debug("Creating new message for application command MULTI_CHANNEL_ENCAP for node {} and endpoint {}", this.getNode().getNodeId(), endpoint.getEndpointId());
		
		byte[] payload = serialMessage.getMessagePayload();
		byte[] newPayload = new byte[payload.length + 4];
		System.arraycopy(payload, 0, newPayload, 0, 2);
		System.arraycopy(payload, 0, newPayload, 4, payload.length);
		newPayload[1] += 4;
		newPayload[2] = (byte) this.getCommandClass().getKey();
		newPayload[3] = MULTI_CHANNEL_ENCAP;
		newPayload[4] = 0x01;
		newPayload[5] = (byte) endpoint.getEndpointId();
		
		serialMessage.setMessagePayload(newPayload);
    	return serialMessage;		
	}
	
	/**
	 * Initializes the Multi instance / endpoint command class by setting the number of instances
	 * or getting the endpoints.
	 */
	public void initEndpoints() {
		switch (this.getVersion()) {
			case 1:
				// get number of instances for all command classes on this node.
				for (ZWaveCommandClass commandClass : this.getNode().getCommandClasses()) {
					if (commandClass.getCommandClass() == CommandClass.NO_OPERATION)
						continue;
					this.getController().sendData(this.getMultiInstanceGetMessage(commandClass.getCommandClass()));
				}
				break;
			case 2:
				this.getController().sendData(this.getMultiChannelEndpointGetMessage());
				break;
			default:
				logger.warn(String.format("Unknown version %d for command class %s (0x%02x)", 
						this.getVersion(), this.getCommandClass().getLabel(), this.getCommandClass().getKey()));
		}
	};
	
}
