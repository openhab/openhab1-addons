/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveWakeUpCommandClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a message which is used in serial API 
 * interface to communicate with usb Z-Wave stick/
 * @author Victor Belov
 * @author Brian Crosby
 * @since 1.3.0
 */
public class SerialMessage {

	private static final Logger logger = LoggerFactory.getLogger(SerialMessage.class);
	private final static AtomicLong sequence = new AtomicLong();

	private long sequenceNumber;
	private byte[] messagePayload;
	private int messageLength = 0;
	private SerialMessageType messageType;
	private SerialMessageClass messageClass;
	private SerialMessagePriority priority;
	private SerialMessageClass expectedReply;

	private int messageNode = 255;
	
	private int transmitOptions = 0;
	private int callbackId = 0;
	
	private boolean transActionCanceled = false;

	/**
	 * Indicates whether the serial message is valid.
	 */
	public boolean isValid = false;
	
	/**
	 * Indicates the number of retry attempts left
	 */
	public int attempts = 3;

	/**
	 * Constructor. Creates a new instance of the SerialMessage class.
	 */
	public SerialMessage() {
		logger.trace("Creating empty message");
		messagePayload = new byte[] {};
	}
	
	/**
	 * Constructor. Creates a new instance of the SerialMessage class using the 
	 * specified message class and message type. An expected reply can be given
	 * to indicate that a transaction is complete. The priority indicates the
	 * priority to send the message with. Higher priority messages are taken from
	 * the send queue earlier than lower priority messages.
	 * @param messageClass the message class to use
	 * @param messageType the message type to use
	 * @param expectedReply the expected Reply for this messaage
	 * @param priority the message priority
	 */
	public SerialMessage(SerialMessageClass messageClass, SerialMessageType messageType, SerialMessageClass expectedReply, SerialMessagePriority priority) {
		this(255, messageClass, messageType, expectedReply, priority);
	}
	
	/**
	 * Constructor. Creates a new instance of the SerialMessage class using the 
	 * specified message class and message type. An expected reply can be given
	 * to indicate that a transaction is complete. The priority indicates the
	 * priority to send the message with. Higher priority messages are taken from
	 * the send queue earlier than lower priority messages.
	 * @param nodeId the node the message is destined for
	 * @param messageClass the message class to use
	 * @param messageType the message type to use
	 * @param expectedReply the expected Reply for this messaage
	 * @param priority the message priority
	 */
	public SerialMessage(int nodeId, SerialMessageClass messageClass, SerialMessageType messageType, SerialMessageClass expectedReply, SerialMessagePriority priority) {
		logger.debug(String.format("Creating empty message of class = %s (0x%02X), type = %s (0x%02X)", 
				new Object[] { messageClass, messageClass.key, messageType, messageType.ordinal()}));
		this.sequenceNumber = sequence.getAndIncrement();
		this.messageClass = messageClass;
		this.messageType = messageType;
		this.messagePayload = new byte[] {};
		this.messageNode = nodeId;
		this.expectedReply = expectedReply;
		this.priority = priority;
	}

	/**
	 * Constructor. Creates a new instance of the SerialMessage class from a
	 * specified buffer.
	 * @param buffer the buffer to create the SerialMessage from.
	 */
	public SerialMessage(byte[] buffer) {
		this(255, buffer);
	}
	
	/**
	 * Constructor. Creates a new instance of the SerialMessage class from a
	 * specified buffer, and subsequently sets the node ID.
	 * @param nodeId the node the message is destined for
	 * @param buffer the buffer to create the SerialMessage from.
	 */
	public SerialMessage(int nodeId, byte[] buffer) {
		logger.debug("Creating new SerialMessage from buffer = " + SerialMessage.bb2hex(buffer));
		messageLength = buffer.length - 2; // buffer[1];
		byte messageCheckSumm = calculateChecksum(buffer);
		byte messageCheckSummReceived = buffer[messageLength+1];
		logger.debug(String.format("Message checksum calculated = 0x%02X, received = 0x%02X", messageCheckSumm, messageCheckSummReceived));
		if (messageCheckSumm == messageCheckSummReceived) {
			logger.trace("Checksum matched");
			isValid = true;
		} else {
			logger.trace("Checksum error");
			isValid = false;
			return;
		}
		this.messageType = buffer[2] == 0x00 ? SerialMessageType.Request : SerialMessageType.Response;;
		this.messageClass = SerialMessageClass.getMessageClass(buffer[3] & 0xFF);
		this.messagePayload = ArrayUtils.subarray(buffer, 4, messageLength + 1);
		this.messageNode = nodeId;
		logger.debug("Message Node ID = " + getMessageNode());
		logger.debug("Message payload = " + SerialMessage.bb2hex(messagePayload));
	}

    /**
     * Converts a byte array to a hexadecimal string representation    
     * @param bb the byte array to convert
     * @return string the string representation
     */
    static public String bb2hex(byte[] bb) {
		String result = "";
		for (int i=0; i<bb.length; i++) {
			result = result + String.format("%02X ", bb[i]);
		}
		return result;
	}
	
	/**
	 * Calculates a checksum for the specified buffer.
	 * @param buffer the buffer to calculate.
	 * @return the checksum value.
	 */
	private static byte calculateChecksum(byte[] buffer) {
		byte checkSum = (byte)0xFF;
		for (int i=1; i<buffer.length-1; i++) {
			checkSum = (byte) (checkSum ^ buffer[i]);
		}
		logger.trace(String.format("Calculated checksum = 0x%02X", checkSum));
		return checkSum;
	}

	/**
	 * Returns a string representation of this SerialMessage object.
	 * The string contains message class, message type and buffer contents.
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("Message: class = %s (0x%02X), type = %s (0x%02X), buffer = %s", 
				new Object[] { messageClass, messageClass.key, messageType, messageType.ordinal(),
				SerialMessage.bb2hex(this.getMessageBuffer()) });
	};
	
	/**
	 * Gets the SerialMessage as a byte array.
	 * @return the message
	 */
	public byte[] getMessageBuffer() {
		ByteArrayOutputStream resultByteBuffer = new ByteArrayOutputStream();
		byte[] result;
		resultByteBuffer.write((byte)0x01);
		int messageLength = messagePayload.length + 
				(this.messageClass == SerialMessageClass.SendData && 
				this.messageType == SerialMessageType.Request ? 5 : 3); // calculate and set length
		
		resultByteBuffer.write((byte) messageLength);
		resultByteBuffer.write((byte) messageType.ordinal());
		resultByteBuffer.write((byte) messageClass.getKey());
		
		try {
			resultByteBuffer.write(messagePayload);
		} catch (IOException e) {
			
		}

		// callback ID and transmit options for a Send Data message.
		if (this.messageClass == SerialMessageClass.SendData && this.messageType == SerialMessageType.Request) {
			resultByteBuffer.write(transmitOptions);
			resultByteBuffer.write(callbackId);
		}
		
		resultByteBuffer.write((byte) 0x00);
		result = resultByteBuffer.toByteArray();
		result[result.length - 1] = 0x01;
		result[result.length - 1] = calculateChecksum(result);
		logger.debug("Assembled message buffer = " + SerialMessage.bb2hex(result));
		return result;
	}
	
	/**
	 * Check whether an object is equal to this serial message.
	 * A serial message is considered equal when:
	 * - the object passed in is a serial message.
	 * - the message class is equal
	 * - the message type is equal
	 * - the expected reply is equal
	 * - the payload is equal
	 * @param obj the object to compare this message with.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (!obj.getClass().equals(this.getClass()))
			return false;
		
		SerialMessage other = (SerialMessage)obj;
		
		if (other.messageClass != this.messageClass)
			return false;
		
		if (other.messageType != this.messageType)
			return false;

		if (other.expectedReply != this.expectedReply)
			return false;

		return Arrays.equals(other.messagePayload, this.messagePayload);
	}
	
	/**
	 * Gets the message type (Request / Response).
	 * @return the message type
	 */
	public SerialMessageType getMessageType() {
		return messageType;
	}

	/**
	 * Gets the message class. This is the function it represents.
	 * @return
	 */
	public SerialMessageClass getMessageClass() {
		return messageClass;
	}

	/**
	 * Returns the Node Id for / from this message.
	 * @return the messageNode
	 */
	public int getMessageNode() {
		return messageNode;
	}

	/**
	 * Gets the message payload.
	 * @return the message payload
	 */
	public byte[] getMessagePayload() {
		return messagePayload;
	}
	
	/**
	 * Gets a byte of the message payload at the specified index.
	 * The byte is returned as an integer between 0x00 (0) and 0xFF (255).
	 * @param index the index of the byte to return.
	 * @return an integer between 0x00 (0) and 0xFF (255).
	 */
	public int getMessagePayloadByte(int index) {
		return messagePayload[index] & 0xFF;
	}
	
	/**
	 * Sets the message payload.
	 * @param messagePayload
	 */
	public void setMessagePayload(byte[] messagePayload) {
		this.messagePayload = messagePayload;
	}

	/**
	 * Gets the transmit options for this SendData Request.
	 * @return the transmitOptions
	 */
	public int getTransmitOptions() {
		return transmitOptions;
	}

	/**
	 * Sets the transmit options for this SendData Request.
	 * @param transmitOptions the transmitOptions to set
	 */
	public void setTransmitOptions(int transmitOptions) {
		this.transmitOptions = transmitOptions;
	}

	/**
	 * Gets the callback ID for this SendData Request.
	 * @return the callbackId
	 */
	public int getCallbackId() {
		return callbackId;
	}

	/**
	 * Sets the callback ID for this SendData Request
	 * @param callbackId the callbackId to set
	 */
	public void setCallbackId(int callbackId) {
		this.callbackId = callbackId;
	}

	/**
	 * Gets the expected reply for this message.
	 * @return the expectedReply
	 */
	public SerialMessageClass getExpectedReply() {
		return expectedReply;
	}

	/**
	 * Returns the priority of this Serial message.
	 * @return the priority
	 */
	public SerialMessagePriority getPriority() {
		return priority;
	}

	/**
	 * Indicates that the transaction for the incoming message is canceled by a command class
	 * @return the transActionCanceled
	 */
	public boolean isTransActionCanceled() {
		return transActionCanceled;
	}

	/**
	 * Sets the transaction for the incoming message to canceled.
	 * @param transActionCanceled the transActionCanceled to set
	 */
	public void setTransActionCanceled(boolean transActionCanceled) {
		this.transActionCanceled = transActionCanceled;
	}

	/**
	 * Serial message type enumeration. Indicates whether the message
	 * is a request or a response.
	 * @author Jan-Willem Spuij
	 * @since 1.3.0
	 */
	public enum SerialMessageType
	{
		Request,																			// 0x00
		Response																			// 0x01
	}
	
	/**
	 * Serial message priority enumeration. Indicates the message priority.
	 * @author Jan-Willem Spuij
	 * @since 1.3.0
	 */
	public enum SerialMessagePriority
	{
		High,																				// 0x01
		Set,																				// 0x02
		Get,																				// 0x03
		Low 																				// 0x04
	}
	
	/**
	 * Serial message class enumeration. Enumerates the different messages
	 * that can be exchanged with the controller.
	 * @author Jan-Willem Spuij
	 * @since 1.3.0
	 */
	public enum SerialMessageClass
	{
		SerialApiGetInitData(0x02,"SerialApiGetInitData"),									// Request initial information about devices in network
		SerialApiApplicationNodeInfo(0x03,"SerialApiApplicationNodeInfo"),					// Set controller node information
		ApplicationCommandHandler(0x04,"ApplicationCommandHandler"),						// Handle application command
		GetControllerCapabilities(0x05,"GetControllerCapabilities"),						// Request controller capabilities (primary role, SUC/SIS availability)
		SerialApiSetTimeouts(0x06,"SerialApiSetTimeouts"),									// Set Serial API timeouts
		SerialApiGetCapabilities(0x07,"SerialApiGetCapabilities"),							// Request Serial API capabilities
		SerialApiSoftReset(0x08,"SerialApiSoftReset"),										// Soft reset. Restarts Z-Wave chip
		SendNodeInfo(0x12,"SendNodeInfo"),													// Send Node Information Frame of the stick
		SendData(0x13,"SendData"),															// Send data.
		GetVersion(0x15,"GetVersion"),														// Request controller hardware version
		SendDataAbort(0x16,"SendDataAbort"),												// Abort Send data.
		RfPowerLevelSet(0x17,"RfPowerLevelSet"),											// Set RF Power level
		GetRandom(0x1c,"GetRandom"),														// ???
		MemoryGetId(0x20,"MemoryGetId"),													// ???
		MemoryGetByte(0x21,"MemoryGetByte"),												// Get a byte of memory.
		ReadMemory(0x23,"ReadMemory"),														// Read memory.
		SetLearnNodeState(0x40,"SetLearnNodeState"),    									// ???
		IdentifyNode(0x41,"IdentifyNode"),    												// Get protocol info (baud rate, listening, etc.) for a given node
		SetDefault(0x42,"SetDefault"),    													// Reset controller and node info to default (original) values
		NewController(0x43,"NewController"),												// ???
		ReplicationCommandComplete(0x44,"ReplicationCommandComplete"),						// Replication send data complete
		ReplicationSendData(0x45,"ReplicationSendData"),									// Replication send data
		AssignReturnRoute(0x46,"AssignReturnRoute"),										// Assign a return route from the specified node to the controller
		DeleteReturnRoute(0x47,"DeleteReturnRoute"),										// Delete all return routes from the specified node
		RequestNodeNeighborUpdate(0x48,"RequestNodeNeighborUpdate"),						// Ask the specified node to update its neighbors (then read them from the controller)
		ApplicationUpdate(0x49,"ApplicationUpdate"),										// Get a list of supported (and controller) command classes
		AddNodeToNetwork(0x4a,"AddNodeToNetwork"),											// Control the addnode (or addcontroller) process...start, stop, etc.
		RemoveNodeFromNetwork(0x4b,"RemoveNodeFromNetwork"),								// Control the removenode (or removecontroller) process...start, stop, etc.
		CreateNewPrimary(0x4c,"CreateNewPrimary"),											// Control the createnewprimary process...start, stop, etc.
		ControllerChange(0x4d,"ControllerChange"),    										// Control the transferprimary process...start, stop, etc.
		SetLearnMode(0x50,"SetLearnMode"),													// Put a controller into learn mode for replication/ receipt of configuration info
		AssignSucReturnRoute(0x51,"AssignSucReturnRoute"),									// Assign a return route to the SUC
		EnableSuc(0x52,"EnableSuc"),														// Make a controller a Static Update Controller
		RequestNetworkUpdate(0x53,"RequestNetworkUpdate"),									// Network update for a SUC(?)
		SetSucNodeID(0x54,"SetSucNodeID"),													// Identify a Static Update Controller node id
		DeleteSUCReturnRoute(0x55,"DeleteSUCReturnRoute"),									// Remove return routes to the SUC
		GetSucNodeId(0x56,"GetSucNodeId"),													// Try to retrieve a Static Update Controller node id (zero if no SUC present)
		RequestNodeNeighborUpdateOptions(0x5a,"RequestNodeNeighborUpdateOptions"),   		// Allow options for request node neighbor update
		RequestNodeInfo(0x60,"RequestNodeInfo"),											// Get info (supported command classes) for the specified node
		RemoveFailedNodeID(0x61,"RemoveFailedNodeID"),										// Mark a specified node id as failed
		IsFailedNodeID(0x62,"IsFailedNodeID"),												// Check to see if a specified node has failed
		ReplaceFailedNode(0x63,"ReplaceFailedNode"),										// Remove a failed node from the controller's list (?)
		GetRoutingInfo(0x80,"GetRoutingInfo"),												// Get a specified node's neighbor information from the controller
		SerialApiSlaveNodeInfo(0xA0,"SerialApiSlaveNodeInfo"),								// Set application virtual slave node information
		ApplicationSlaveCommandHandler(0xA1,"ApplicationSlaveCommandHandler"),				// Slave command handler
		SendSlaveNodeInfo(0xA2,"ApplicationSlaveCommandHandler"),							// Send a slave node information frame
		SendSlaveData(0xA3,"SendSlaveData"),												// Send data from slave
		SetSlaveLearnMode(0xA4,"SetSlaveLearnMode"),										// Enter slave learn mode
		GetVirtualNodes(0xA5,"GetVirtualNodes"),											// Return all virtual nodes
		IsVirtualNode(0xA6,"IsVirtualNode"),												// Virtual node test
		SetPromiscuousMode(0xD0,"SetPromiscuousMode"),										// Set controller into promiscuous mode to listen to all frames
		PromiscuousApplicationCommandHandler(0xD1,"PromiscuousApplicationCommandHandler");
		
		/**
		 * A mapping between the integer code and its corresponding ZWaveMessage
		 * value to facilitate lookup by code.
		 */
		private static Map<Integer, SerialMessageClass> codeToMessageClassMapping;

		private int key;
		private String label;

		private SerialMessageClass(int key, String label) {
			this.key = key;
			this.label = label;
		}

		private static void initMapping() {
			codeToMessageClassMapping = new HashMap<Integer, SerialMessageClass>();
			for (SerialMessageClass s : values()) {
				codeToMessageClassMapping.put(s.key, s);
			}
		}

		/**
		 * Lookup function based on the generic device class code.
		 * @param i the code to lookup
		 * @return enumeration value of the generic device class.
		 */
		public static SerialMessageClass getMessageClass(int i) {
			if (codeToMessageClassMapping == null) {
				initMapping();
			}
			return codeToMessageClassMapping.get(i);
		}
		
		/**
		 * Returns the enumeration key.
		 * @return the key
		 */
		public int getKey() {
			return key;
		}

		/**
		 * Returns the enumeration label.
		 * @return the label
		 */
		public String getLabel() {
			return label;
		}
	}

	/**
	 * Comparator Class. Compares two serial messages with each other based on
	 * node status (awake / sleep), priority and sequence number. 
	 * @author Jan-Willem Spuij
	 * @since 1.3.0
	 */
	public static class SerialMessageComparator implements Comparator<SerialMessage> {

		private final ZWaveController controller;
		
		/**
		 * Constructor. Creates a new instance of the SerialMessageComparator class.
		 * @param controller the {@link ZWaveController to use}
		 */
		public SerialMessageComparator(ZWaveController controller) {
			this.controller = controller;
		}

		/**
		 * Compares a serial message to another serial message.
		 * Used by the priority queue to order messages.
		 * @param arg0 the first serial message to compare the other to.
		 * @param arg1 the other serial message to compare the first one to.
		 */
		@Override
		public int compare(SerialMessage arg0, SerialMessage arg1) {

			boolean arg0Awake = false;
			boolean arg0Listening = true;
			boolean arg1Awake = false;
			boolean arg1Listening = true;
			
			if ((arg0.getMessageClass() == SerialMessageClass.RequestNodeInfo ||
					arg0.getMessageClass() == SerialMessageClass.SendData)) {
				ZWaveNode node = this.controller.getNode(arg0.getMessageNode());
				
				if (node != null && !node.isListening() && !node.isFrequentlyListening()) {
					arg0Listening = false;
					ZWaveWakeUpCommandClass wakeUpCommandClass = (ZWaveWakeUpCommandClass)node.getCommandClass(CommandClass.WAKE_UP);
					
					if (wakeUpCommandClass != null && wakeUpCommandClass.isAwake())
						arg0Awake = true;
				}
			}
			
			if ((arg1.getMessageClass() == SerialMessageClass.RequestNodeInfo ||
					arg1.getMessageClass() == SerialMessageClass.SendData)) {
				ZWaveNode node = this.controller.getNode(arg1.getMessageNode());
				
				if (node != null && !node.isListening() && !node.isFrequentlyListening()) {
					arg1Listening = false;
					ZWaveWakeUpCommandClass wakeUpCommandClass = (ZWaveWakeUpCommandClass)node.getCommandClass(CommandClass.WAKE_UP);
					
					if (wakeUpCommandClass != null && wakeUpCommandClass.isAwake())
						arg1Awake = true;
				}
			}
			
			// messages for awake nodes get priority over 
			// messages for sleeping (or listening) nodes.
			if (arg0Awake && !arg1Awake)
				return -1;
			else if (arg1Awake && !arg0Awake)
				return 1;
			
			// messages for listening nodes get priority over
			// non listening nodes.
			if (arg0Listening && !arg1Listening)
				return -1;
			else if (arg1Listening && !arg0Listening)
				return 1;
			
			int res = arg0.priority.compareTo(arg1.priority);
			
			if (res == 0 && arg0 != arg1)
			   res = (arg0.sequenceNumber < arg1.sequenceNumber ? -1 : 1);
			
			return res;
		}

	}
}
