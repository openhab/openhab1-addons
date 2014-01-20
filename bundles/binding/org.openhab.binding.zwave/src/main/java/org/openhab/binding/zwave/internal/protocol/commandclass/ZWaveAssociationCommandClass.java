/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.zwave.internal.protocol.AssociationGroup;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Handles the Association command class. This allows reading and writing of
 * node association parameters
 * 
 * @author Chris Jackson
 * @since 1.4.0
 */
@XStreamAlias("associationCommandClass")
public class ZWaveAssociationCommandClass extends ZWaveCommandClass {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveAssociationCommandClass.class);

	private static final int ASSOCIATIONCMD_SET = 0x01;
	private static final int ASSOCIATIONCMD_GET = 0x02;
	private static final int ASSOCIATIONCMD_REPORT = 0x03;
	private static final int ASSOCIATIONCMD_REMOVE = 0x04;
	private static final int ASSOCIATIONCMD_GROUPINGSGET = 0x05;
	private static final int ASSOCIATIONCMD_GROUPINGSREPORT = 0x06;

	// Stores the list of association groups
	private Map<Integer, AssociationGroup>configAssociations = new HashMap<Integer, AssociationGroup>();

	/**
	 * Creates a new instance of the ZWaveAssociationCommandClass class.
	 * 
	 * @param node
	 *            the node this command class belongs to
	 * @param controller
	 *            the controller to use
	 * @param endpoint
	 *            the endpoint this Command class belongs to
	 */
	public ZWaveAssociationCommandClass(ZWaveNode node, ZWaveController controller, ZWaveEndpoint endpoint) {
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
	public void handleApplicationCommandRequest(SerialMessage serialMessage, int offset, int endpoint) {
		logger.debug(String.format("Received Association Request for Node ID = %d", this.getNode().getNodeId()));
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
		case ASSOCIATIONCMD_SET:
			logger.trace("Process Association Set");
			processAssociationReport(serialMessage, offset);
			break;
		case ASSOCIATIONCMD_GET:
			logger.trace("Process Association Get");
			return;
		case ASSOCIATIONCMD_REPORT:
			logger.trace("Process Association Report");
			processAssociationReport(serialMessage, offset);
			break;
		case ASSOCIATIONCMD_REMOVE:
			logger.trace("Process Association Remove");
			return;
		case ASSOCIATIONCMD_GROUPINGSGET:
			logger.trace("Process Association GroupingsGet");
			return;
		case ASSOCIATIONCMD_GROUPINGSREPORT:
			logger.trace("Process Association GroupingsReport - number of groups "
					+ serialMessage.getMessagePayloadByte(offset + 1));
			return;
		default:
			logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", command, this
					.getCommandClass().getLabel(), this.getCommandClass().getKey()));
		}
	}

	/**
	 * Processes a CONFIGURATIONCMD_REPORT / CONFIGURATIONCMD_SET message.
	 * 
	 * @param serialMessage
	 *            the incoming message to process.
	 * @param offset
	 *            the offset position from which to start message processing.
	 * @param endpoint
	 *            the endpoint or instance number this message is meant for.
	 */
	protected void processAssociationReport(SerialMessage serialMessage, int offset) {
		// Extract the group index
		int group = serialMessage.getMessagePayloadByte(offset + 1);
		// The max associations supported (0 if the requested group is not
		// supported)
		int maxAssociations = serialMessage.getMessagePayloadByte(offset + 2);
		// Number of outstanding requests (if the group is large, it may come in
		// multiple frames)
		int following = serialMessage.getMessagePayloadByte(offset + 3);

		if (maxAssociations == 0) {
			// Unsupported association group. Nothing to do!
			return;
		}

		logger.debug("Node {}, association group {} has max associations " + maxAssociations, this.getNode()
				.getNodeId(), group);

		AssociationGroup association = configAssociations.get(group);
		if(association == null) {
			association = new AssociationGroup(group);
		}

		ZWaveAssociationEvent zEvent = new ZWaveAssociationEvent(this.getNode().getNodeId(), group);
		if (serialMessage.getMessagePayload().length > (offset + 4)) {
			logger.debug("Node {}, association group {} includes the following nodes:", this.getNode().getNodeId(),
					group);
			int numAssociations = serialMessage.getMessagePayload().length - (offset + 4);
			for (int cnt = 0; cnt < numAssociations; cnt++) {
				int node = serialMessage.getMessagePayloadByte(offset + 4 + cnt);
				logger.debug("Node {}", node);
				
				// Add the node to the group
				zEvent.addMember(node);
				association.addNode(node);
			}
		}

		// Update the group in the list
		configAssociations.put(group, association);
		
		// Is this the end of the list
		if (following == 0) {
		}

		this.getController().notifyEventListeners(zEvent);
	}

	/**
	 * Gets a SerialMessage with the ASSOCIATIONCMD_GET command
	 * 
	 * @param group
	 *            the association group to read
	 * @return the serial message
	 */
	public SerialMessage getAssociationMessage(int group) {
		// Clear the current information for this group
		AssociationGroup association = new AssociationGroup(group); 
		configAssociations.put(group, association);
		
		logger.debug("Creating new message for application command ASSOCIATIONCMD_GET for node {}", this.getNode()
				.getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
				SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
		byte[] newPayload = { (byte) this.getNode().getNodeId(), 3, (byte) getCommandClass().getKey(),
				(byte) ASSOCIATIONCMD_GET, (byte) (group & 0xff) };
		result.setMessagePayload(newPayload);
		return result;
	}

	/**
	 * Gets a SerialMessage with the ASSOCIATIONCMD_SET command
	 * 
	 * @param group
	 *            the association group
	 * @param node
	 *            the node to add to the specified group
	 * @return the serial message
	 */
	public SerialMessage setAssociationMessage(int group, int node) {
		logger.debug("Creating new message for application command ASSOCIATIONCMD_SET for node {}", this.getNode()
				.getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
				SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Set);

		byte[] newPayload = { (byte) this.getNode().getNodeId(), 4, (byte) getCommandClass().getKey(),
				(byte) ASSOCIATIONCMD_SET, (byte) (group & 0xff), (byte) (node & 0xff) };

		result.setMessagePayload(newPayload);
		return result;
	}

	/**
	 * Gets a SerialMessage with the ASSOCIATIONCMD_REMOVE command
	 * 
	 * @param group
	 *            the association group
	 * @param node
	 *            the node to add to the specified group
	 * @return the serial message
	 */
	public SerialMessage removeAssociationMessage(int group, int node) {
		logger.debug("Creating new message for application command ASSOCIATIONCMD_REMOVE for node {}", this.getNode()
				.getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData,
				SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Set);

		byte[] newPayload = { (byte) this.getNode().getNodeId(), 4, (byte) getCommandClass().getKey(),
				(byte) ASSOCIATIONCMD_REMOVE, (byte) (group & 0xff), (byte) (node & 0xff) };

		result.setMessagePayload(newPayload);
		return result;
	}

	/**
	 * Returns a list of nodes that are currently members of the association
	 * group. This method only returns the list that is currently in the
	 * class - it does not interact with the device.
	 * 
	 * To update the list stored in the class, call getAssociationMessage
	 * 
	 * @param group
	 *            number of the association group
	 * @return List of nodes in the group
	 */
	public List<Integer> getGroupMembers(int group) {
		if(configAssociations.get(group) == null)
			return null;
		return configAssociations.get(group).getNodes();
	}

	/**
	 * ZWave association group received event.
	 * Send from the association members to the binding
	 * Note that multiple events can be required to build up the full list.
	 * 
	 * @author Chris Jackson
	 * @since 1.4.0
	 */
	public class ZWaveAssociationEvent extends ZWaveEvent {

		private int group;
		private List<Integer> members = new ArrayList<Integer>();
		
		/**
		 * Constructor. Creates a new instance of the ZWaveAssociationEvent
		 * class.
		 * @param nodeId the nodeId of the event. Must be set to the controller node.
		 */
		public ZWaveAssociationEvent(int nodeId, int group) {
			super(nodeId, 1);
			
			this.group = group;
		}

		public int getGroup() {
			return group;
		}

		public List<Integer> getMembers() {
			return members;
		}

		public int getMemberCnt() {
			return members.size();
		}

		public void addMember(int member) {
			members.add(member);
		}
	}
}
