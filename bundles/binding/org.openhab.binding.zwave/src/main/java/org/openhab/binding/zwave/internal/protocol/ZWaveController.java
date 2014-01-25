/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Basic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Generic;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Specific;
import org.openhab.binding.zwave.internal.protocol.NodeStage;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveWakeUpCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveInitializationCompletedEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveTransactionCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZWave controller class. Implements communication with the Z-Wave
 * controller stick using serial messages.
 * @author Victor Belov
 * @author Brian Crosby
 * @author Chris Jackson
 * @since 1.3.0
 */
public class ZWaveController {
	
	private static final Logger logger = LoggerFactory.getLogger(ZWaveController.class);
	
	private static final int QUERY_STAGE_TIMEOUT = 120000;
	private static final int ZWAVE_RESPONSE_TIMEOUT = 5000; // 5000 ms ZWAVE_RESPONSE TIMEOUT
	private static final int ZWAVE_RECEIVE_TIMEOUT = 1000; // 1000 ms ZWAVE_RECEIVE_TIMEOUT
	private static final int NODE_BYTES = 29; // 29 bytes = 232 bits, one for each supported node by Z-Wave;
	private static final int INITIAL_QUEUE_SIZE = 128; 
	private static final long WATCHDOG_TIMER_PERIOD = 10000; // 10 seconds watchdog timer

	private static final int TRANSMIT_OPTION_ACK = 0x01;
	private static final int TRANSMIT_OPTION_AUTO_ROUTE = 0x04;
	private static final int TRANSMIT_OPTION_EXPLORE = 0x20;
	
	private final Map<Integer, ZWaveNode> zwaveNodes = new HashMap<Integer, ZWaveNode>();
	private final ArrayList<ZWaveEventListener> zwaveEventListeners = new ArrayList<ZWaveEventListener>();
	private final PriorityBlockingQueue<SerialMessage> sendQueue = new PriorityBlockingQueue<SerialMessage>(INITIAL_QUEUE_SIZE, new SerialMessage.SerialMessageComparator(this));
	private ZWaveSendThread sendThread;
	private ZWaveReceiveThread receiveThread;
	
	private final Semaphore transactionCompleted = new Semaphore(1);
	private volatile SerialMessage lastSentMessage = null;
	private SerialPort serialPort;
	private Timer watchdog;
	
	private String zWaveVersion = "Unknown";
	private String serialAPIVersion = "Unknown";
	private int homeId = 0;
	private int ownNodeId = 0;
	private int manufactureId = 0;
	private int deviceType = 0; 
	private int deviceId = 0;
	private int ZWaveLibraryType = 0;
	private int sentDataPointer = 1;
	
	private int SOFCount = 0;
	private int CANCount = 0;
	private int NAKCount = 0;
	private int ACKCount = 0;
	private int OOFCount = 0;
	private AtomicInteger timeOutCount = new AtomicInteger(0);
	
	private boolean isConnected;

	// Constructors
	
	/**
	 * Constructor. Creates a new instance of the Z-Wave controller class.
	 * @param serialPortName the serial port name to use for 
	 * communication with the Z-Wave controller stick.
	 * @throws SerialInterfaceException when a connection error occurs.
	 */
	public ZWaveController(final String serialPortName) throws SerialInterfaceException {
			logger.info("Starting Z-Wave controller");
			connect(serialPortName);
			this.watchdog = new Timer(true);
			this.watchdog.schedule(
					new WatchDogTimerTask(serialPortName), 
					WATCHDOG_TIMER_PERIOD, WATCHDOG_TIMER_PERIOD);
	}

	// Incoming message handlers
	
	/**
	 * Handles incoming Serial Messages. Serial messages can either be messages
	 * that are a response to our own requests, or the stick asking us information.
	 * @param incomingMessage the incoming message to process.
	 */
	private void handleIncomingMessage(SerialMessage incomingMessage) {
		
		logger.debug("Incoming message to process");
		logger.debug(incomingMessage.toString());
		
		switch (incomingMessage.getMessageType()) {
			case Request:
				handleIncomingRequestMessage(incomingMessage);
				break;
			case Response:
				handleIncomingResponseMessage(incomingMessage);
				break;
			default:
				logger.warn("Unsupported incomingMessageType: 0x%02X", incomingMessage.getMessageType());
		}
	}

	/**
	 * Handles an incoming request message.
	 * An incoming request message is a message initiated by a node or the controller.
	 * @param incomingMessage the incoming message to process.
	 */
	private void handleIncomingRequestMessage(SerialMessage incomingMessage) {
		logger.debug("Message type = REQUEST");
		switch (incomingMessage.getMessageClass()) {
			case ApplicationCommandHandler:
				handleApplicationCommandRequest(incomingMessage);
				break;
			case SendData:
				handleSendDataRequest(incomingMessage);
				break;
			case ApplicationUpdate:
				handleApplicationUpdateRequest(incomingMessage);
				break;
			case RemoveFailedNodeID:
				handleRemoveFailedNodeRequest(incomingMessage);
				break;
			case RequestNodeNeighborUpdate:
				handleNodeNeighborUpdateRequest(incomingMessage);
				break;

			default:
			logger.warn(String.format("TODO: Implement processing of Request Message = %s (0x%02X)",
					incomingMessage.getMessageClass().getLabel(),
					incomingMessage.getMessageClass().getKey()));
			break;	
		}
	}
	
	/**
	 * Handles incoming Application Command Request.
	 * @param incomingMessage the request message to process.
	 */
	private void handleApplicationCommandRequest(SerialMessage incomingMessage) {
		logger.trace("Handle Message Application Command Request");
		int nodeId = incomingMessage.getMessagePayloadByte(1);
		logger.debug("Application Command Request from Node " + nodeId);
		ZWaveNode node = getNode(nodeId);
		
		if (node == null) {
			logger.warn("Node {} not initialized yet, ignoring message.", nodeId);
			return;
		}
		
		node.resetResendCount();
		
		int commandClassCode = incomingMessage.getMessagePayloadByte(3);
		CommandClass commandClass = CommandClass.getCommandClass(commandClassCode);

		if (commandClass == null) {
			logger.error(String.format("Unsupported command class 0x%02x", commandClassCode));
			return;
		}

		logger.debug(String.format("Incoming command class %s (0x%02x)", commandClass.getLabel(), commandClass.getKey()));
		ZWaveCommandClass zwaveCommandClass =  node.getCommandClass(commandClass);
		
		// Apparently, this node supports a command class that we did not get (yet) during initialization.
		// Let's add it now then to support handling this message.
		if (zwaveCommandClass == null) {
			logger.debug(String.format("Command class %s (0x%02x) not found on node %d, trying to add it.", 
					commandClass.getLabel(), commandClass.getKey(), nodeId));
			
			zwaveCommandClass = ZWaveCommandClass.getInstance(commandClass.getKey(), node, this);
			
			if (zwaveCommandClass != null) {
				logger.debug(String.format("Adding command class %s (0x%02x)", commandClass.getLabel(), commandClass.getKey()));
				node.addCommandClass(zwaveCommandClass);
			}
		}
		
		// We got an unsupported command class, return.
		if (zwaveCommandClass == null) {
			logger.error(String.format("Unsupported command class %s (0x%02x)", commandClass.getLabel(), commandClassCode));
			return;
		}
		
		logger.trace("Found Command Class {}, passing to handleApplicationCommandRequest", zwaveCommandClass.getCommandClass().getLabel());
		zwaveCommandClass.handleApplicationCommandRequest(incomingMessage, 4, 1);

		if (incomingMessage.getMessageClass() == this.lastSentMessage.getExpectedReply() && nodeId == this.lastSentMessage.getMessageNode() && !incomingMessage.isTransActionCanceled()) {
				notifyEventListeners(new ZWaveTransactionCompletedEvent(this.lastSentMessage));
				transactionCompleted.release();
				logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
		}
	}
	
	/**
	 * Handles incoming Send Data Request. Send Data request are used
	 * to acknowledge or cancel failed messages.
	 * @param incomingMessage the request message to process.
	 */
	private void handleSendDataRequest(SerialMessage incomingMessage) {
		logger.trace("Handle Message Send Data Request");
		
		int callbackId = incomingMessage.getMessagePayloadByte(0);
		TransmissionState status = TransmissionState.getTransmissionState(incomingMessage.getMessagePayloadByte(1));
		SerialMessage originalMessage = this.lastSentMessage;
		
		if (status == null) {
			logger.warn("Transmission state not found, ignoring.");
			return;
		}

		logger.debug("CallBack ID = {}", callbackId);
		logger.debug(String.format("Status = %s (0x%02x)", status.getLabel(), status.getKey()));
		
		if (originalMessage == null || originalMessage.getCallbackId() != callbackId) {
			logger.warn("Already processed another send data request for this callback Id, ignoring.");
			return;
		}
		
		switch (status) {
			case COMPLETE_OK:
				ZWaveNode node = this.getNode(originalMessage.getMessageNode());
				
				node.resetResendCount();
				// in case we received a ping response and the node is alive, we proceed with the next node stage for this node.
				if (node != null && node.getNodeStage() == NodeStage.PING) {
					node.advanceNodeStage(NodeStage.DETAILS);
				}
				if (incomingMessage.getMessageClass() == originalMessage.getExpectedReply() && !incomingMessage.isTransActionCanceled()) {
					notifyEventListeners(new ZWaveTransactionCompletedEvent(this.lastSentMessage));
					transactionCompleted.release();
					logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
				}
				return;
			case COMPLETE_NO_ACK:
				timeOutCount.incrementAndGet();
			case COMPLETE_FAIL:
			case COMPLETE_NOT_IDLE:
			case COMPLETE_NOROUTE:
				try {
					handleFailedSendDataRequest(originalMessage);
				} finally {
					transactionCompleted.release();
					logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
				}
			default:
		}
	}

	/**
	 * Handles a failed SendData request. This can either be because of the stick actively reporting it
	 * or because of a time-out of the transaction in the send thread.
	 * @param originalMessage the original message that was sent
	 */
	private void handleFailedSendDataRequest(SerialMessage originalMessage) {
		ZWaveNode node = this.getNode(originalMessage.getMessageNode());
		
		if (node.getNodeStage() == NodeStage.DEAD)
			return;
		
		if (!node.isListening() && !node.isFrequentlyListening() && originalMessage.getPriority() != SerialMessagePriority.Low) {
			ZWaveWakeUpCommandClass wakeUpCommandClass = (ZWaveWakeUpCommandClass)node.getCommandClass(CommandClass.WAKE_UP);
			
			if (wakeUpCommandClass != null) {
				wakeUpCommandClass.setAwake(false);
				wakeUpCommandClass.putInWakeUpQueue(originalMessage); //it's a battery operated device, place in wake-up queue.
				return;
			}
		} else if (!node.isListening() && !node.isFrequentlyListening() && originalMessage.getPriority() == SerialMessagePriority.Low)
			return;
		
		node.incrementResendCount();
		
		logger.error("Got an error while sending data to node {}. Resending message.", node.getNodeId());
		this.sendData(originalMessage);
	}
	
	/**
	 * Handles incoming Application Update Request.
	 * @param incomingMessage the request message to process.
	 */
	private void handleApplicationUpdateRequest(SerialMessage incomingMessage) {
		logger.trace("Handle Message Application Update Request");
		int nodeId = incomingMessage.getMessagePayloadByte(1);
		
		logger.trace("Application Update Request from Node " + nodeId);
		UpdateState updateState = UpdateState.getUpdateState(incomingMessage.getMessagePayloadByte(0));
		
		switch (updateState) {
		case NODE_INFO_RECEIVED:
			logger.debug("Application update request, node information received.");			
			int length = incomingMessage.getMessagePayloadByte(2);
			ZWaveNode node = getNode(nodeId);
			
			node.resetResendCount();
			
			for (int i = 6; i < length + 3; i++) {
				int data = incomingMessage.getMessagePayloadByte(i);
				if(data == 0xef )  {
					// TODO: Implement control command classes
					break;
				}
				logger.debug(String.format("Adding command class 0x%02X to the list of supported command classes.", data));
				ZWaveCommandClass commandClass = ZWaveCommandClass.getInstance(data, node, this);
				if (commandClass != null)
					node.addCommandClass(commandClass);
			}
			
			// advance node stage.
			node.advanceNodeStage(NodeStage.MANSPEC01);
			
			if (incomingMessage.getMessageClass() == this.lastSentMessage.getExpectedReply() && !incomingMessage.isTransActionCanceled()) {
				notifyEventListeners(new ZWaveTransactionCompletedEvent(this.lastSentMessage));
				transactionCompleted.release();
				logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
			}

			// Treat the node information frame as a wakeup
			ZWaveWakeUpCommandClass wakeUp = (ZWaveWakeUpCommandClass)node.getCommandClass(ZWaveCommandClass.CommandClass.WAKE_UP);
			if(wakeUp != null) {
				wakeUp.setAwake(true);
			}
			break;
		case NODE_INFO_REQ_FAILED:
			logger.debug("Application update request, Node Info Request Failed, re-request node info.");
			
			SerialMessage requestInfoMessage = this.lastSentMessage;
			
			if (requestInfoMessage.getMessageClass() != SerialMessageClass.RequestNodeInfo) {
				logger.warn("Got application update request without node info request, ignoring.");
				return;
			}
				
			if (--requestInfoMessage.attempts >= 0) {
				logger.error("Got Node Info Request Failed while sending this serial message. Requeueing");
				this.enqueue(requestInfoMessage);
			} else
			{
				logger.warn("Node Info Request Failed 3x. Discarding message: {}", lastSentMessage.toString());
			}
			transactionCompleted.release();
			break;
		default:
			logger.warn(String.format("TODO: Implement Application Update Request Handling of %s (0x%02X).", updateState.getLabel(), updateState.getKey()));
		}
	}

	/**
	 * Handles an incoming response message.
	 * An incoming response message is a response, based one of our own requests.
	 * @param incomingMessage the response message to process.
	 */
	private void handleIncomingResponseMessage(SerialMessage incomingMessage) {
		logger.debug("Message type = RESPONSE");
		switch (incomingMessage.getMessageClass()) {
			case GetVersion:
				handleGetVersionResponse(incomingMessage);
				if (incomingMessage.getMessageClass() == this.lastSentMessage.getExpectedReply() && !incomingMessage.isTransActionCanceled()) {
					notifyEventListeners(new ZWaveTransactionCompletedEvent(this.lastSentMessage));
					transactionCompleted.release();
					logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
				}
				break;
			case MemoryGetId:
				handleMemoryGetId(incomingMessage);
				if (incomingMessage.getMessageClass() == this.lastSentMessage.getExpectedReply() && !incomingMessage.isTransActionCanceled()) {
					notifyEventListeners(new ZWaveTransactionCompletedEvent(this.lastSentMessage));
					transactionCompleted.release();
					logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
				}
				break;
			case SerialApiGetInitData:
				handleSerialApiGetInitDataResponse(incomingMessage);
				if (incomingMessage.getMessageClass() == this.lastSentMessage.getExpectedReply() && !incomingMessage.isTransActionCanceled()) {
					notifyEventListeners(new ZWaveTransactionCompletedEvent(this.lastSentMessage));
					transactionCompleted.release();
					logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
				}
				break;
			case IdentifyNode:
				handleIdentifyNodeResponse(incomingMessage);
				if (incomingMessage.getMessageClass() == this.lastSentMessage.getExpectedReply() && !incomingMessage.isTransActionCanceled()) {
					notifyEventListeners(new ZWaveTransactionCompletedEvent(this.lastSentMessage));
					transactionCompleted.release();
					logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
				}
				break;
			case RequestNodeInfo:
				handleRequestNodeInfoResponse(incomingMessage);
				break;
			case SerialApiGetCapabilities:
				handleSerialAPIGetCapabilitiesResponse(incomingMessage);
				if (incomingMessage.getMessageClass() == this.lastSentMessage.getExpectedReply() && !incomingMessage.isTransActionCanceled()) {
					notifyEventListeners(new ZWaveTransactionCompletedEvent(this.lastSentMessage));
					transactionCompleted.release();
					logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
				}
				break;
			case SendData:
				handleSendDataResponse(incomingMessage);
				break;
			case RemoveFailedNodeID:
				handleRemoveFailedNodeResponse(incomingMessage);
				if (incomingMessage.getMessageClass() == this.lastSentMessage.getExpectedReply() && !incomingMessage.isTransActionCanceled()) {
					// TODO: We should add an event here to notify the client
					transactionCompleted.release();
					logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
				}
				break;
			case GetRoutingInfo:
				handleNodeRoutingInfoRequest(incomingMessage);
				break;
			default:
				logger.warn(String.format("TODO: Implement processing of Response Message = %s (0x%02X)",
						incomingMessage.getMessageClass().getLabel(),
						incomingMessage.getMessageClass().getKey()));
				break;				
		}
	}

	/**
	 * Handles the response of the getVersion request.
	 * @param incomingMessage the response message to process.
	 */
	private void handleGetVersionResponse(SerialMessage incomingMessage) {
		this.ZWaveLibraryType = incomingMessage.getMessagePayloadByte(12);
		this.zWaveVersion = new String(ArrayUtils.subarray(incomingMessage.getMessagePayload(), 0, 11));
		logger.debug(String.format("Got MessageGetVersion response. Version = %s, Library Type = 0x%02X", zWaveVersion, ZWaveLibraryType));
	}
	
	/**
	 * Handles the response of the SerialApiGetInitData request.
	 * @param incomingMessage the response message to process.
	 */
	private void handleSerialApiGetInitDataResponse(
			SerialMessage incomingMessage) {
		logger.debug(String.format("Got MessageSerialApiGetInitData response."));
		this.isConnected = true;
		int nodeBytes = incomingMessage.getMessagePayloadByte(2);
		
		if (nodeBytes != NODE_BYTES) {
			logger.error("Invalid number of node bytes = {}", nodeBytes);
			return;
		}

		int nodeId = 1;
		
		// loop bytes
		for (int i = 3;i < 3 + nodeBytes;i++) {
			int incomingByte = incomingMessage.getMessagePayloadByte(i);
			// loop bits in byte
			for (int j=0;j<8;j++) {
				int b1 = incomingByte & (int)Math.pow(2.0D, j);
				int b2 = (int)Math.pow(2.0D, j);
				if (b1 == b2) {
					logger.info(String.format("Found node id = %d", nodeId));
					// Place nodes in the local ZWave Controller 
					this.zwaveNodes.put(nodeId, new ZWaveNode(this.homeId, nodeId, this));
					this.getNode(nodeId).advanceNodeStage(NodeStage.PROTOINFO);
				}
				nodeId++;
			}
		}
		
		logger.info("------------Number of Nodes Found Registered to ZWave Controller------------");
		logger.info(String.format("# Nodes = %d", this.zwaveNodes.size()));
		logger.info("----------------------------------------------------------------------------");
	}

	/**
	 * Handles the response of the MemoryGetId request.
	 * The MemoryGetId function gets the home and node id from the controller memory.
	 * @param incomingMessage the response message to process.
	 */
	private void handleMemoryGetId(SerialMessage incomingMessage) {
		this.homeId = ((incomingMessage.getMessagePayloadByte(0)) << 24) | 
				((incomingMessage.getMessagePayloadByte(1)) << 16) | 
				((incomingMessage.getMessagePayloadByte(2)) << 8) | 
				(incomingMessage.getMessagePayloadByte(3));
		this.ownNodeId = incomingMessage.getMessagePayloadByte(4);
		logger.debug(String.format("Got MessageMemoryGetId response. Home id = 0x%08X, Controller Node id = %d", this.homeId, this.ownNodeId));
	}

	/**
	 * Handles the response of the IdentifyNode request.
	 * @param incomingMessage the response message to process.
	 */
	private void handleIdentifyNodeResponse(SerialMessage incomingMessage) {
		logger.trace("Handle Message Get Node ProtocolInfo Response");
		
		int nodeId = lastSentMessage.getMessagePayloadByte(0);
		logger.debug("ProtocolInfo for Node = " + nodeId);
		
		ZWaveNode node = this.zwaveNodes.get(nodeId);
		
		boolean listening = (incomingMessage.getMessagePayloadByte(0) & 0x80)!=0 ? true : false;
		boolean routing = (incomingMessage.getMessagePayloadByte(0) & 0x40)!=0 ? true : false;
		int version = (incomingMessage.getMessagePayloadByte(0) & 0x07) + 1;
		boolean frequentlyListening = (incomingMessage.getMessagePayloadByte(1) & 0x60)!= 0 ? true : false;
		
		logger.debug("Listening = " + listening);
		logger.debug("Routing = " + routing);
		logger.debug("Version = " + version);
		logger.debug("fLIRS = " + frequentlyListening);
		
		node.setListening(listening);
		node.setRouting(routing);
		node.setVersion(version);
		node.setFrequentlyListening(frequentlyListening);
		
		Basic basic = Basic.getBasic(incomingMessage.getMessagePayloadByte(3));
		if (basic == null) {
			logger.error(String.format("Basic device class 0x%02x not found", incomingMessage.getMessagePayloadByte(3)));
			return;
		}
		logger.debug(String.format("Basic = %s 0x%02x", basic.getLabel(), basic.getKey()));

		Generic generic = Generic.getGeneric(incomingMessage.getMessagePayloadByte(4));
		if (generic == null) {
			logger.error(String.format("Generic device class 0x%02x not found", incomingMessage.getMessagePayloadByte(4)));
			return;
		}
		logger.debug(String.format("Generic = %s 0x%02x", generic.getLabel(), generic.getKey()));

		Specific specific = Specific.getSpecific(generic, incomingMessage.getMessagePayloadByte(5));
		if (specific == null) {
			logger.error(String.format("Specific device class 0x%02x not found", incomingMessage.getMessagePayloadByte(5)));
			return;
		}
		logger.debug(String.format("Specific = %s 0x%02x", specific.getLabel(), specific.getKey()));
		
		ZWaveDeviceClass deviceClass = node.getDeviceClass();
		deviceClass.setBasicDeviceClass(basic);
		deviceClass.setGenericDeviceClass(generic);
		deviceClass.setSpecificDeviceClass(specific);
		
		// if restored the node from configuration information
		// then we don't have to add these command classes anymore.
		if (!node.restoreFromConfig()) {
			// Add mandatory command classes as specified by it's generic device class.
			for (CommandClass commandClass : generic.getMandatoryCommandClasses()) {
				ZWaveCommandClass zwaveCommandClass = ZWaveCommandClass.getInstance(commandClass.getKey(), node, this);
				if (zwaveCommandClass != null)
					this.zwaveNodes.get(nodeId).addCommandClass(zwaveCommandClass);
			}
	
			// Add mandatory command classes as specified by it's specific device class.
			for (CommandClass commandClass : specific.getMandatoryCommandClasses()) {
				ZWaveCommandClass zwaveCommandClass = ZWaveCommandClass.getInstance(commandClass.getKey(), node, this);
				if (zwaveCommandClass != null)
					node.addCommandClass(zwaveCommandClass);
			}
		}
		
    	// advance node stage of the current node.
		node.advanceNodeStage(NodeStage.PING);
	}
	
	/**
	 * Handles the response of the SerialAPIGetCapabilities request.
	 * @param incomingMessage the response message to process.
	 */
	private void handleSerialAPIGetCapabilitiesResponse(SerialMessage incomingMessage) {
		logger.trace("Handle Message Serial API Get Capabilities");

		this.serialAPIVersion = String.format("%d.%d", incomingMessage.getMessagePayloadByte(0), incomingMessage.getMessagePayloadByte(1));
		this.manufactureId = ((incomingMessage.getMessagePayloadByte(2)) << 8) | (incomingMessage.getMessagePayloadByte(3));
		this.deviceType = ((incomingMessage.getMessagePayloadByte(4)) << 8) | (incomingMessage.getMessagePayloadByte(5));
		this.deviceId = (((incomingMessage.getMessagePayloadByte(6)) << 8) | (incomingMessage.getMessagePayloadByte(7)));
		
		logger.debug(String.format("API Version = %s", this.getSerialAPIVersion()));
		logger.debug(String.format("Manufacture ID = 0x%x", this.getManufactureId()));
		logger.debug(String.format("Device Type = 0x%x", this.getDeviceType()));
		logger.debug(String.format("Device ID = 0x%x", this.getDeviceId()));
		
		// Ready to get information on Serial API		
		this.enqueue(new SerialMessage(SerialMessageClass.SerialApiGetInitData, SerialMessageType.Request, SerialMessageClass.SerialApiGetInitData, SerialMessagePriority.High));
	}

	/**
	 * Handles the response of the SendData request.
	 * @param incomingMessage the response message to process.
	 */
	private void handleSendDataResponse(SerialMessage incomingMessage) {
		logger.trace("Handle Message Send Data Response");
		if(incomingMessage.getMessageBuffer()[2] != 0x00)
			logger.debug("Sent Data successfully placed on stack.");
		else
			logger.error("Sent Data was not placed on stack due to error.");
	}
	
	/**
	 * Handles the response of the Request node request.
	 * @param incomingMessage the response message to process.
	 */
	private void handleRequestNodeInfoResponse(SerialMessage incomingMessage) {
		logger.trace("Handle RequestNodeInfo Response");
		if(incomingMessage.getMessageBuffer()[2] != 0x00)
			logger.debug("Request node info successfully placed on stack.");
		else
			logger.error("Request node info not placed on stack due to error.");
	}

	/**
	 * Handles the response of the RemoveFailedNode request.
	 * @param incomingMessage the response message to process.
	 */
	private void handleRemoveFailedNodeResponse(SerialMessage incomingMessage) {
		logger.debug("Got RemoveFailedNode response.");
		if(incomingMessage.getMessagePayloadByte(0) == 0x00) {
			logger.debug("Remove failed node successfully placed on stack.");
		} else
			logger.error("Remove failed node not placed on stack due to error 0x{}.", Integer.toHexString(incomingMessage.getMessagePayloadByte(0)));
	}

	/**
	 * Handles the request of the RemoveFailedNode.
	 * This is received from the controller after a RemoveFailedNode request is made.
	 * This is only received if the node is found and deleted.
	 * @param incomingMessage the response message to process.
	 */
	private void handleRemoveFailedNodeRequest(SerialMessage incomingMessage) {
		int nodeId = lastSentMessage.getMessagePayloadByte(0);

		logger.debug("Got RemoveFailedNode request (Node {}).", nodeId);
		if(incomingMessage.getMessagePayloadByte(0) != 0x00) {
			logger.error("Remove failed node failed with error 0x{}.", Integer.toHexString(incomingMessage.getMessagePayloadByte(0)));
		}
	}

	/**
	 * Handles the request of the NodeNeighborUpdate.
	 * This is received from the controller after a NodeNeighborUpdate request is made.
	 * @param incomingMessage the response message to process.
	 */
	private void handleNodeNeighborUpdateRequest(SerialMessage incomingMessage) {
		final int REQUEST_NEIGHBOR_UPDATE_STARTED = 0x21;
		final int REQUEST_NEIGHBOR_UPDATE_DONE    = 0x22;
		final int REQUEST_NEIGHBOR_UPDATE_FAILED  = 0x23;

		int nodeId = lastSentMessage.getMessagePayloadByte(0);

		logger.debug("Got NodeNeighborUpdate request (Node {}).", nodeId);
		switch(incomingMessage.getMessagePayloadByte(1)) {
		case REQUEST_NEIGHBOR_UPDATE_STARTED:
			logger.error("NodeNeighborUpdate STARTED");
			break;
		case REQUEST_NEIGHBOR_UPDATE_DONE:
			logger.error("NodeNeighborUpdate DONE");

			// We're done
			transactionCompleted.release();

			// TODO: Add an event?
			break;
		case REQUEST_NEIGHBOR_UPDATE_FAILED:
			logger.error("NodeNeighborUpdate FAILED");
			// We're done
			transactionCompleted.release();
			break;
		}
	}
	
	/**
	 * Handles the request of the GetRoutingInfo. This is received from the
	 * controller after a GetRoutingInfo response is received. The nodes are
	 * indicated in a 29 byte bitmap - each bit related to a node (29 bytes * 8
	 * bits = 232 nodes)
	 * 
	 * @param incomingMessage
	 *            the response message to process.
	 */
	private void handleNodeRoutingInfoRequest(SerialMessage incomingMessage) {
		int nodeId = lastSentMessage.getMessagePayloadByte(0);
		
		logger.debug("Got NodeRoutingInfo request (Node {}).", nodeId);

		// Get the node
		ZWaveNode node = getNode(nodeId);
		if(node == null) {
			logger.error("Routing information for unknown node {}", nodeId);
			transactionCompleted.release();
			return;
		}

		node.clearNeighbors();
		boolean hasNeighbors = false;
		for (int by = 0; by < NODE_BYTES; by++) {
			for (int bi = 0; bi < 8; bi++) {
				if ((incomingMessage.getMessagePayloadByte(by) & (0x01 << bi)) != 0) {
					logger.debug("Node {}", (by << 3) + bi + 1);
					hasNeighbors = true;

					// Add the node to the neighbor list
					node.addNeighbor((by << 3) + bi + 1);
				}
			}
		}

		if (!hasNeighbors) {
			logger.debug("No neighbors reported");
		}

		// We're done
		transactionCompleted.release();

		// TODO: Add an event?
	}

	
	// Controller methods

	/**
	 * Connects to the comm port and starts send and receive threads.
	 * @param serialPortName the port name to open
	 * @throws SerialInterfaceException when a connection error occurs.
	 */
	public void connect(final String serialPortName)
			throws SerialInterfaceException {
		logger.info("Connecting to serial port {}", serialPortName);
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(serialPortName);
			CommPort commPort = portIdentifier.open("org.openhab.binding.zwave",2000);
			this.serialPort = (SerialPort) commPort;
			this.serialPort.setSerialPortParams(115200,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
			this.serialPort.enableReceiveThreshold(1);
			this.serialPort.enableReceiveTimeout(ZWAVE_RECEIVE_TIMEOUT);
			this.receiveThread = new ZWaveReceiveThread();
			this.receiveThread.start();
			this.sendThread = new ZWaveSendThread();
			this.sendThread.start();

			logger.info("Serial port is initialized");
		} catch (NoSuchPortException e) {
			logger.error(String.format("Port %s does not exist", serialPortName));
			throw new SerialInterfaceException(String.format("Port %s does not exist", serialPortName), e);
		} catch (PortInUseException e) {
			logger.error(String.format("Port %s in use.", serialPortName));
			throw new SerialInterfaceException(String.format("Port %s in use.", serialPortName), e);
		} catch (UnsupportedCommOperationException e) {
			logger.error(String.format("Unsupported comm operation on Port %s.", serialPortName));
			throw new SerialInterfaceException(String.format("Unsupported comm operation on Port %s.", serialPortName), e);
		}
	}
	
	/**
	 * Closes the connection to the Z-Wave controller.
	 */
	public void close()	{
		if (watchdog != null) {
			watchdog.cancel();
			watchdog = null;
		}
		
		disconnect();
		
		// clear nodes collection and send queue
		for (Object listener : this.zwaveEventListeners.toArray()) {
			if (!(listener instanceof ZWaveNode))
				continue;
			
			this.zwaveEventListeners.remove(listener);
		}
		
		this.zwaveNodes.clear();
		this.sendQueue.clear();
		
		logger.info("Stopped Z-Wave controller");
	}

	/**
	 * Disconnects from the serial interface and stops
	 * send and receive threads.
	 */
	public void disconnect() {
		if (sendThread != null) {
			sendThread.interrupt();
			try {
				sendThread.join();
			} catch (InterruptedException e) {
			}
			sendThread = null;
		}
		if (receiveThread != null) {
			receiveThread.interrupt();
			try {
				receiveThread.join();
			} catch (InterruptedException e) {
			}
			receiveThread = null;
		}
		if(transactionCompleted.availablePermits() < 0)
			transactionCompleted.release(transactionCompleted.availablePermits());
		
		transactionCompleted.drainPermits();
		logger.trace("Transaction completed permit count -> {}", transactionCompleted.availablePermits());
		if (this.serialPort != null) {
			this.serialPort.close();
			this.serialPort = null;
		}
		logger.info("Disconnected from serial port");
	}
	
	/**
	 * Enqueues a message for sending on the send thread.
	 * @param serialMessage the serial message to enqueue.
	 */
	public void enqueue(SerialMessage serialMessage) {
		this.sendQueue.add(serialMessage);
		logger.debug("Enqueueing message. Queue length = {}", this.sendQueue.size());
	}
		
	/**
	 * Notify our own event listeners of a Z-Wave event.
	 * @param event the event to send.
	 */
	public void notifyEventListeners(ZWaveEvent event) {
		logger.debug("Notifying event listeners");
		for (ZWaveEventListener listener : this.zwaveEventListeners) {
			logger.trace("Notifying {}", listener.toString());
			listener.ZWaveIncomingEvent(event);
		}
	}
	
	/**
	 * Initializes communication with the Z-Wave controller stick.
	 */
	public void initialize() {
		this.enqueue(new SerialMessage(SerialMessageClass.GetVersion, SerialMessageType.Request, SerialMessageClass.GetVersion, SerialMessagePriority.High));
		this.enqueue(new SerialMessage(SerialMessageClass.MemoryGetId, SerialMessageType.Request, SerialMessageClass.MemoryGetId, SerialMessagePriority.High));
		this.enqueue(new SerialMessage(SerialMessageClass.SerialApiGetCapabilities, SerialMessageType.Request, SerialMessageClass.SerialApiGetCapabilities, SerialMessagePriority.High));
	}
	
	/**
	 * Send Identify Node message to the controller.
	 * @param nodeId the nodeId of the node to identify
	 * @throws SerialInterfaceException when timing out or getting an invalid response.
	 */
	public void identifyNode(int nodeId) throws SerialInterfaceException {
		SerialMessage newMessage = new SerialMessage(nodeId, SerialMessageClass.IdentifyNode, SerialMessageType.Request, SerialMessageClass.IdentifyNode, SerialMessagePriority.High);
    	byte[] newPayload = { (byte) nodeId };
    	newMessage.setMessagePayload(newPayload);
    	this.enqueue(newMessage);
	}
	
	/**
	 * Send Request Node info message to the controller.
	 * @param nodeId the nodeId of the node to identify
	 * @throws SerialInterfaceException when timing out or getting an invalid response.
	 */
	public void requestNodeInfo(int nodeId) {
		SerialMessage newMessage = new SerialMessage(nodeId, SerialMessageClass.RequestNodeInfo, SerialMessageType.Request, SerialMessageClass.ApplicationUpdate, SerialMessagePriority.High);
    	byte[] newPayload = { (byte) nodeId };
    	newMessage.setMessagePayload(newPayload);
    	this.enqueue(newMessage);
	}
	
	/**
	 * Checks for dead or sleeping nodes during Node initialization.
	 * JwS: merged checkInitComplete and checkForDeadOrSleepingNodes to prevent possibly looping nodes multiple times.
	 */
	public void checkForDeadOrSleepingNodes(){
		int completeCount = 0;
		
		if (zwaveNodes.isEmpty())
			return;
		
		// There are still nodes waiting to get a ping.
		// So skip the dead node checking.
		for (SerialMessage serialMessage : sendQueue) {
			if (serialMessage.getPriority() == SerialMessagePriority.Low)
				return;
		}
		
		logger.trace("Checking for Dead or Sleeping Nodes.");
		for (Map.Entry<Integer, ZWaveNode> entry : zwaveNodes.entrySet()){
			if (entry.getValue().getNodeStage() == NodeStage.EMPTYNODE)
				continue;
			
			logger.debug(String.format("Node %d has been in Stage %s since %s", entry.getKey(), entry.getValue().getNodeStage().getLabel(), entry.getValue().getQueryStageTimeStamp().toString()));
			
			if(entry.getValue().getNodeStage() == NodeStage.DONE || (!entry.getValue().isListening() && !entry.getValue().isFrequentlyListening())) {
				completeCount++;
				continue;
			}
			
			logger.trace("Checking if {} miliseconds have passed in current stage.", QUERY_STAGE_TIMEOUT);
			
			if(Calendar.getInstance().getTimeInMillis() < (entry.getValue().getQueryStageTimeStamp().getTime() + QUERY_STAGE_TIMEOUT))
				continue;
			
			logger.warn(String.format("Node %d may be dead, setting stage to DEAD.", entry.getKey()));
			entry.getValue().setNodeStage(NodeStage.DEAD);

			completeCount++;
		}
		
		if(this.zwaveNodes.size() == completeCount){
			ZWaveEvent zEvent = new ZWaveInitializationCompletedEvent(this.ownNodeId);
			this.notifyEventListeners(zEvent);
		}
	}

	/**
	 * Request the node routing information.
	 *
	 * @param nodeId The address of the node to update
	 */
	public void requestNodeRoutingInfo(int nodeId)
	{
		logger.debug("Request routing info for node {}", nodeId);

		// Queue the request
		SerialMessage newMessage = new SerialMessage(SerialMessageClass.GetRoutingInfo, SerialMessageType.Request, SerialMessageClass.GetRoutingInfo, SerialMessagePriority.High);
		byte[] newPayload = { (byte) nodeId,
				(byte) 0,
				(byte) 0,
				(byte) 3
		};
    	newMessage.setMessagePayload(newPayload);
    	this.enqueue(newMessage);
	}

	/**
	 * Request the node neighbor list to be updated for the specified node.
	 * Once this is complete, the requestNodeRoutingInfo will be called
	 * automatically to update the data in the binding.
	 *
	 * @param nodeId The address of the node to update
	 */
	public void requestNodeNeighborUpdate(int nodeId)
	{
		logger.debug("Request neighbor update for node {}", nodeId);

		// Queue the request
		SerialMessage newMessage = new SerialMessage(SerialMessageClass.RequestNodeNeighborUpdate, SerialMessageType.Request, SerialMessageClass.RequestNodeNeighborUpdate, SerialMessagePriority.High);
		byte[] newPayload = { (byte) nodeId };
    	newMessage.setMessagePayload(newPayload);
    	this.enqueue(newMessage);
	}

	/**
	 * Removes a failed nodes from the network.
	 * Note that this won't remove nodes that have not failed.
	 * @param nodeId The address of the node to remove
	 */
	public void requestRemoveFailedNode(int nodeId)
	{
		logger.debug("Marking node {} as having failed", nodeId);

		// Queue the request
		SerialMessage newMessage = new SerialMessage(SerialMessageClass.RemoveFailedNodeID, SerialMessageType.Request, SerialMessageClass.RemoveFailedNodeID, SerialMessagePriority.High);
		byte[] newPayload = { (byte) nodeId };
    	newMessage.setMessagePayload(newPayload);
    	this.enqueue(newMessage);
	}

	/**
	 * Transmits the SerialMessage to a single Z-Wave Node.
	 * Sets the transmission options as well.
	 * @param serialMessage the Serial message to send.
	 */
	public void sendData(SerialMessage serialMessage)
	{
    	if (serialMessage.getMessageClass() != SerialMessageClass.SendData) {
    		logger.error(String.format("Invalid message class %s (0x%02X) for sendData", serialMessage.getMessageClass().getLabel(), serialMessage.getMessageClass().getKey()));
    		return;
    	}
    	if (serialMessage.getMessageType() != SerialMessageType.Request) {
    		logger.error("Only request messages can be sent");
    		return;
    	}
    	
    	ZWaveNode node = this.getNode(serialMessage.getMessageNode());
    			
    	if (node.getNodeStage() == NodeStage.DEAD) {
    		logger.debug("Node {} is dead, not sending message.", node.getNodeId());
			return;
    	}
		
    	if (!node.isListening() && !node.isFrequentlyListening() && serialMessage.getPriority() != SerialMessagePriority.Low) {
			ZWaveWakeUpCommandClass wakeUpCommandClass = (ZWaveWakeUpCommandClass)node.getCommandClass(CommandClass.WAKE_UP);
			
			if (wakeUpCommandClass != null && !wakeUpCommandClass.isAwake()) {
				wakeUpCommandClass.putInWakeUpQueue(serialMessage); //it's a battery operated device, place in wake-up queue.
				return;
			}
		}
    	
    	serialMessage.setTransmitOptions(TRANSMIT_OPTION_ACK | TRANSMIT_OPTION_AUTO_ROUTE | TRANSMIT_OPTION_EXPLORE);
    	if (++sentDataPointer > 0xFF)
    		sentDataPointer = 1;
    	serialMessage.setCallbackId(sentDataPointer);
    	logger.debug("Callback ID = {}", sentDataPointer);
    	this.enqueue(serialMessage);
	}
	
	/**
	 * Add a listener for Z-Wave events to this controller.
	 * @param eventListener the event listener to add.
	 */
	public void addEventListener(ZWaveEventListener eventListener) {
		this.zwaveEventListeners.add(eventListener);
	}

	/**
	 * Remove a listener for Z-Wave events to this controller.
	 * @param eventListener the event listener to remove.
	 */
	public void removeEventListener(ZWaveEventListener eventListener) {
		this.zwaveEventListeners.remove(eventListener);
	}
	
    /**
     * Gets the API Version of the controller.
	 * @return the serialAPIVersion
	 */
	public String getSerialAPIVersion() {
		return serialAPIVersion;
	}

	/**
	 * Gets the Manufacturer ID of the controller. 
	 * @return the manufactureId
	 */
	public int getManufactureId() {
		return manufactureId;
	}

	/**
	 * Gets the device type of the controller;
	 * @return the deviceType
	 */
	public int getDeviceType() {
		return deviceType;
	}

	/**
	 * Gets the device ID of the controller.
	 * @return the deviceId
	 */
	public int getDeviceId() {
		return deviceId;
	}
	
	/**
	 * Gets the node ID of the controller.
	 * @return the deviceId
	 */
	public int getOwnNodeId() {
		return ownNodeId;
	}

	/**
	 * Gets the node object using it's node ID as key.
	 * Returns null if the node is not found
	 * @param nodeId the Node ID of the node to get.
	 * @return node object
	 */
	public ZWaveNode getNode(int nodeId) {
		return this.zwaveNodes.get(nodeId);
	}
	
	/**
	 * Indicates a working connection to the
	 * Z-Wave controller stick.
	 * @return isConnected;
	 */
	public boolean isConnected() {
		return isConnected;
	}
	
	/**
	 * Gets the number of Start Of Frames received.
	 * @return the sOFCount
	 */
	public int getSOFCount() {
		return SOFCount;
	}

	/**
	 * Gets the number of Canceled Frames received.
	 * @return the cANCount
	 */
	public int getCANCount() {
		return CANCount;
	}

	/**
	 * Gets the number of Not Acknowledged Frames received.
	 * @return the nAKCount
	 */
	public int getNAKCount() {
		return NAKCount;
	}

	/**
	 * Gets the number of Acknowledged Frames received.
	 * @return the aCKCount
	 */
	public int getACKCount() {
		return ACKCount;
	}

	/**
	 * Returns the number of Out of Order frames received.
	 * @return the oOFCount
	 */
	public int getOOFCount() {
		return OOFCount;
	}
	
	/**
	 * Returns the number of Time-Outs while sending.
	 * @return the oOFCount
	 */
	public int getTimeOutCount() {
		return timeOutCount.get();
	}
	
	// Nested classes and enumerations
	
	/**
	 * Z-Wave controller Send Thread. Takes care of sending all messages.
	 * It uses a semaphore to synchronize communication with the receiving thread.
	 * @author Jan-Willem Spuij
	 * @since 1.3.0
	 */
	private class ZWaveSendThread extends Thread {
	
		private final Logger logger = LoggerFactory.getLogger(ZWaveSendThread.class);

		/**
		 * Run method. Runs the actual sending process.
		 */
		@Override
		public void run() {
			logger.debug("Starting Z-Wave send thread");
			while (!interrupted()) {
				
				try {
					lastSentMessage = sendQueue.take();
					logger.debug("Took message from queue for sending. Queue length = {}", sendQueue.size());
				} catch (InterruptedException e1) {
					break;
				}
				
				if (lastSentMessage == null)
					continue;
				
				if (lastSentMessage.getMessageClass() == SerialMessageClass.SendData) {
					ZWaveNode node = getNode(lastSentMessage.getMessageNode());
					
					if (node != null && !node.isListening() && !node.isFrequentlyListening() && lastSentMessage.getPriority() != SerialMessagePriority.Low) {
						ZWaveWakeUpCommandClass wakeUpCommandClass = (ZWaveWakeUpCommandClass)node.getCommandClass(CommandClass.WAKE_UP);
						
						if (wakeUpCommandClass != null && !wakeUpCommandClass.isAwake()) {
							wakeUpCommandClass.putInWakeUpQueue(lastSentMessage); //it's a battery operated device that is sleeping, place in wake-up queue.
							continue;
						}
					}
				}
				
				transactionCompleted.drainPermits();
				
				byte[] buffer = lastSentMessage.getMessageBuffer();
				logger.debug("Sending Message = " + SerialMessage.bb2hex(buffer));
				try {
					synchronized (serialPort.getOutputStream()) {
						serialPort.getOutputStream().write(buffer);
						serialPort.getOutputStream().flush();
					}
				} catch (IOException e) {
					logger.error("Got I/O exception {} during sending. exiting thread.", e.getLocalizedMessage());
					break;
				}
				
				try {
					if (!transactionCompleted.tryAcquire(1, ZWAVE_RESPONSE_TIMEOUT, TimeUnit.MILLISECONDS)) {
						timeOutCount.incrementAndGet();
						if (lastSentMessage.getMessageClass() == SerialMessageClass.SendData) {
							
							buffer = new SerialMessage(SerialMessageClass.SendDataAbort, SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.High).getMessageBuffer();
							logger.debug("Sending Message = " + SerialMessage.bb2hex(buffer));
							try {
								synchronized (serialPort.getOutputStream()) {
									serialPort.getOutputStream().write(buffer);
									serialPort.getOutputStream().flush();
								}
							} catch (IOException e) {
								logger.error("Got I/O exception {} during sending. exiting thread.", e.getLocalizedMessage());
								break;
							}
						}
																			
						if (--lastSentMessage.attempts >= 0) {
							logger.error("Timeout while sending message to node {}. Requeueing", lastSentMessage.getMessageNode());
							if (lastSentMessage.getMessageClass() == SerialMessageClass.SendData)
								handleFailedSendDataRequest(lastSentMessage);
							else
								enqueue(lastSentMessage);
						} else
						{
							logger.warn("Discarding message: {}", lastSentMessage.toString());
						}
						continue;
					}
					logger.trace("Acquired. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
				} catch (InterruptedException e) {
					break;
				}
				
			}
			logger.debug("Stopped Z-Wave send thread");
		}
	}

	/**
	 * Z-Wave controller Receive Thread. Takes care of receiving all messages.
	 * It uses a semaphore to synchronize communication with the sending thread.
	 * @author Jan-Willem Spuij
	 * @since 1.3.0
	 */	
	private class ZWaveReceiveThread extends Thread {
		
		private static final int SOF = 0x01;
		private static final int ACK = 0x06;
		private static final int NAK = 0x15;
		private static final int CAN = 0x18;
		
		private final Logger logger = LoggerFactory.getLogger(ZWaveReceiveThread.class);

		/**
    	 * Sends 1 byte frame response.
    	 * @param response the response code to send.
    	 */
		private void sendResponse(int response) {
			try {
				synchronized (serialPort.getOutputStream()) {
					serialPort.getOutputStream().write(response);
					serialPort.getOutputStream().flush();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		
		/**
    	 * Processes incoming message and notifies event handlers.
    	 * @param buffer the buffer to process.
    	 */
    	private void processIncomingMessage(byte[] buffer) {
    		SerialMessage serialMessage = new SerialMessage(buffer);
    		if (serialMessage.isValid) {
    			logger.trace("Message is valid, sending ACK");
    			sendResponse(ACK);
    		} else {
    			logger.error("Message is not valid, discarding");
    			return;
    		}
    		
    		handleIncomingMessage(serialMessage);
        }
		
		/**
		 * Run method. Runs the actual receiving process.
		 */
		@Override
		public void run() {
			logger.debug("Starting Z-Wave receive thread");
			while (!interrupted()) {
				int nextByte;
				
				try {
					nextByte = serialPort.getInputStream().read();
					
					if (nextByte == -1)
						continue;
					
				} catch (IOException e) {
					logger.error("Got I/O exception {} during receiving. exiting thread.", e.getLocalizedMessage());
					break;
				}
				
				switch (nextByte) {
					case SOF:
						int messageLength;
						
						try {
							messageLength = serialPort.getInputStream().read();
							
						} catch (IOException e) {
							logger.error("Got I/O exception {} during receiving. exiting thread.", e.getLocalizedMessage());
							break;
						}
						
						byte[] buffer = new byte[messageLength + 2];
						buffer[0] = SOF;
						buffer[1] = (byte)messageLength;
						int total = 0;
						
						while (total < messageLength) {
							try {
								int read = serialPort.getInputStream().read(buffer, total + 2, messageLength - total); 
								total += (read > 0 ? read : 0);
							} catch (IOException e) {
								logger.error("Got I/O exception {} during receiving. exiting thread.", e.getLocalizedMessage());
								return;
							}
						}
						
						logger.trace("Reading message finished" );
						logger.debug("Message = " + SerialMessage.bb2hex(buffer));
						processIncomingMessage(buffer);
						SOFCount++;
						break;
					case ACK:
    					logger.trace("Received ACK");
						ACKCount++;
						break;
					case NAK:
    					logger.error("Message not acklowledged by controller (NAK), discarding");
    					transactionCompleted.release();
    					logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
						NAKCount++;
						break;
					case CAN:
    					logger.error("Message cancelled by controller (CAN), resending");
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							break;
						}
    					enqueue(lastSentMessage);
    					transactionCompleted.release();
    					logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
						CANCount++;
						break;
					default:
						logger.warn(String.format("Out of Frame flow. Got 0x%02X. Sending NAK.", nextByte));
    					sendResponse(NAK);
    					OOFCount++;
				}
			}
			logger.debug("Stopped Z-Wave receive thread");
		}
	}

	/**
	 * WatchDogTimerTask class. Acts as a watch dog and
	 * checks the serial threads to see whether they are
	 * still running.  
	 * @author Jan-Willem Spuij
	 * @since 1.3.0
	 */
	private class WatchDogTimerTask extends TimerTask {
		
		private final Logger logger = LoggerFactory.getLogger(WatchDogTimerTask.class);
		private final String serialPortName;
		
		/**
		 * Creates a new instance of the WatchDogTimerTask class.
		 * @param serialPortName the serial port name to reconnect to
		 * in case the serial threads have died.
		 */
		public WatchDogTimerTask(String serialPortName) {
			this.serialPortName = serialPortName;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			logger.trace("Watchdog: Checking Serial threads");
			if ((receiveThread != null && !receiveThread.isAlive()) ||
					(sendThread != null && !sendThread.isAlive()))
			{
				logger.warn("Threads not alive, respawning");
				disconnect();
				try {
					connect(serialPortName);
				} catch (SerialInterfaceException e) {
					logger.error("unable to restart Serial threads: {}", e.getLocalizedMessage());
				}
			}
		}
	}
}
