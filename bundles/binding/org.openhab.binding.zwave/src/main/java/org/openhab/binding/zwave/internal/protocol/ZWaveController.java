/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Enumeration;

import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClassDynamicState;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveMultiInstanceCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveWakeUpCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveSwitchAllCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveInclusionEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNetworkEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNodeStatusEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveTransactionCompletedEvent;
import org.openhab.binding.zwave.internal.protocol.initialization.ZWaveNodeSerializer;
import org.openhab.binding.zwave.internal.protocol.serialmessage.AddNodeMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.AssignReturnRouteMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.AssignSucReturnRouteMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.ControllerSetDefaultMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.DeleteReturnRouteMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.EnableSucMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.GetControllerCapabilitiesMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.GetSucNodeIdMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.IdentifyNodeMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.IsFailedNodeMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.RemoveNodeMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.RequestNodeNeighborUpdateMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.RemoveFailedNodeMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.RequestNodeInfoMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.GetRoutingInfoMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.SendDataMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.SerialApiSetTimeoutsMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.SerialApiSoftResetMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.SetSucNodeMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.ZWaveCommandProcessor;
import org.openhab.binding.zwave.internal.protocol.serialmessage.GetVersionMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.MemoryGetIdMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.SerialApiGetCapabilitiesMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.SerialApiGetInitDataMessageClass;
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

	private static final int ZWAVE_RESPONSE_TIMEOUT = 5000;		// 5000 ms ZWAVE_RESPONSE TIMEOUT
	private static final int ZWAVE_RECEIVE_TIMEOUT = 1000;		// 1000 ms ZWAVE_RECEIVE_TIMEOUT
	private static final int INITIAL_TX_QUEUE_SIZE = 128; 
	private static final int INITIAL_RX_QUEUE_SIZE = 8; 
	private static final long WATCHDOG_TIMER_PERIOD = 10000;	// 10 seconds watchdog timer

	private static final int TRANSMIT_OPTION_ACK = 0x01;
	private static final int TRANSMIT_OPTION_AUTO_ROUTE = 0x04;
	private static final int TRANSMIT_OPTION_EXPLORE = 0x20;
	
	private final ConcurrentHashMap<Integer, ZWaveNode> zwaveNodes = new ConcurrentHashMap<Integer, ZWaveNode>();
	private final ArrayList<ZWaveEventListener> zwaveEventListeners = new ArrayList<ZWaveEventListener>();
	private final PriorityBlockingQueue<SerialMessage> sendQueue = new PriorityBlockingQueue<SerialMessage>(INITIAL_TX_QUEUE_SIZE, new SerialMessage.SerialMessageComparator(this));
	private final PriorityBlockingQueue<SerialMessage> recvQueue = new PriorityBlockingQueue<SerialMessage>(INITIAL_RX_QUEUE_SIZE, new SerialMessage.SerialMessageComparator(this));
	private ZWaveSendThread sendThread;
	private ZWaveReceiveThread receiveThread;
	private ZWaveInputThread inputThread;

	private final Semaphore sendAllowed = new Semaphore(1);
	private final Semaphore transactionCompleted = new Semaphore(1);
	private volatile SerialMessage lastSentMessage = null;
	private long lastMessageStartTime = 0;
	private long longestResponseTime = 0;
	private SerialPort serialPort;
	private int zWaveResponseTimeout = ZWAVE_RESPONSE_TIMEOUT;
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
	private boolean setSUC = false;
	private ZWaveDeviceType controllerType = ZWaveDeviceType.UNKNOWN;
	private int sucID = 0;
	private boolean softReset = false;
	private boolean masterController = false;

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
	public ZWaveController(final boolean masterController, final boolean isSUC, final String serialPortName, final Integer timeout, final boolean reset)
							throws SerialInterfaceException {
			logger.info("Starting Z-Wave controller");
			this.masterController = masterController;
			this.setSUC = isSUC;
			this.softReset = reset;

			if(timeout != null && timeout >= 1500 && timeout <= 10000) {
				zWaveResponseTimeout = timeout;
			}
			logger.info("Z-Wave timeout is set to {}ms. Soft reset is {}.", zWaveResponseTimeout, reset);
			connect(serialPortName);
			this.watchdog = new Timer(true);
			this.watchdog.schedule(
					new WatchDogTimerTask(serialPortName), 
					WATCHDOG_TIMER_PERIOD, WATCHDOG_TIMER_PERIOD);

			// We have a delay in running the initialisation sequence to allow any
			// frames queued in the controller to be received before sending the init
			// sequence. This avoids protocol errors (CAN errors).
			Timer initTimer = new Timer();
			initTimer.schedule(new InitializeDelayTask(), 3000);
	}

	private class InitializeDelayTask extends TimerTask {
		private final Logger logger = LoggerFactory.getLogger(WatchDogTimerTask.class);

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			logger.debug("Initialising network");
			initialize();
		}
	}


	// Incoming message handlers
	
	/**
	 * Handles incoming Serial Messages. Serial messages can either be messages
	 * that are a response to our own requests, or the stick asking us information.
	 * @param incomingMessage the incoming message to process.
	 */
	private void handleIncomingMessage(SerialMessage incomingMessage) {
		logger.debug(incomingMessage.toString());
		
		switch (incomingMessage.getMessageType()) {
			case Request:
				handleIncomingRequestMessage(incomingMessage);
				break;
			case Response:
				handleIncomingResponseMessage(incomingMessage);
				break;
			default:
				logger.warn("Unsupported incomingMessageType: {}", incomingMessage.getMessageType());
		}
	}

	/**
	 * Handles an incoming request message.
	 * An incoming request message is a message initiated by a node or the controller.
	 * @param incomingMessage the incoming message to process.
	 */
	private void handleIncomingRequestMessage(SerialMessage incomingMessage) {
		logger.trace("Incoming Message type = REQUEST");

		ZWaveCommandProcessor processor = ZWaveCommandProcessor.getMessageDispatcher(incomingMessage.getMessageClass());
		if(processor == null) {
			logger.warn(String.format("TODO: Implement processing of Request Message = %s (0x%02X)",
					incomingMessage.getMessageClass().getLabel(),
					incomingMessage.getMessageClass().getKey()));
			
			return;
		}

		boolean result = processor.handleRequest(this, lastSentMessage, incomingMessage);
		if(processor.isTransactionComplete()) {
			notifyEventListeners(new ZWaveTransactionCompletedEvent(this.lastSentMessage, result));
			transactionCompleted.release();
			logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
		}
	}

	/**
	 * Handles a failed SendData request. This can either be because of the stick actively reporting it
	 * or because of a time-out of the transaction in the send thread.
	 * @param originalMessage the original message that was sent
	 */
	private void handleFailedSendDataRequest(SerialMessage originalMessage) {
		new SendDataMessageClass().handleFailedSendDataRequest(this, originalMessage);
	}

	/**
	 * Handles an incoming response message.
	 * An incoming response message is a response, based one of our own requests.
	 * @param incomingMessage the response message to process.
	 */
	private void handleIncomingResponseMessage(SerialMessage incomingMessage) {
		logger.trace("Incoming Message type = RESPONSE");

		ZWaveCommandProcessor processor = ZWaveCommandProcessor.getMessageDispatcher(incomingMessage.getMessageClass());
		if(processor == null) {
			logger.warn(String.format("TODO: Implement processing of Response Message = %s (0x%02X)",
					incomingMessage.getMessageClass().getLabel(),
					incomingMessage.getMessageClass().getKey()));

			return;
		}

		boolean result = processor.handleResponse(this, lastSentMessage, incomingMessage);
		if(processor.isTransactionComplete()) {
			notifyEventListeners(new ZWaveTransactionCompletedEvent(this.lastSentMessage, result));
			transactionCompleted.release();
			logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
		}

		switch (incomingMessage.getMessageClass()) {
			case GetVersion:
				this.zWaveVersion = ((GetVersionMessageClass)processor).getVersion();
				this.ZWaveLibraryType = ((GetVersionMessageClass)processor).getLibraryType();
				break;
			case MemoryGetId:
				this.ownNodeId = ((MemoryGetIdMessageClass)processor).getNodeId();
				this.homeId = ((MemoryGetIdMessageClass)processor).getHomeId();
				break;
			case SerialApiGetInitData:
				this.isConnected = true;
				for(Integer nodeId : ((SerialApiGetInitDataMessageClass)processor).getNodes()) {
					addNode(nodeId);
				}
				break;
			case GetSucNodeId:
				// Remember the SUC ID
				this.sucID = ((GetSucNodeIdMessageClass)processor).getSucNodeId();
				
				// If we want to be the SUC, enable it here
				if(this.setSUC == true && this.sucID == 0) {
					// We want to be SUC
					this.enqueue(new EnableSucMessageClass().doRequest(EnableSucMessageClass.SUCType.SERVER));
					this.enqueue(new SetSucNodeMessageClass().doRequest(this.ownNodeId, SetSucNodeMessageClass.SUCType.SERVER));
				}
				else if(this.setSUC == false && this.sucID == this.ownNodeId) {
					// We don't want to be SUC, but we currently are!
					// Disable SERVER functionality, and set the node to 0
					this.enqueue(new EnableSucMessageClass().doRequest(EnableSucMessageClass.SUCType.NONE));
					this.enqueue(new SetSucNodeMessageClass().doRequest(this.ownNodeId, SetSucNodeMessageClass.SUCType.NONE));
				}
				this.enqueue(new GetControllerCapabilitiesMessageClass().doRequest());
				break;
			case SerialApiGetCapabilities:
				this.serialAPIVersion = ((SerialApiGetCapabilitiesMessageClass)processor).getSerialAPIVersion();
				this.manufactureId = ((SerialApiGetCapabilitiesMessageClass)processor).getManufactureId();
				this.deviceId = ((SerialApiGetCapabilitiesMessageClass)processor).getDeviceId();
				this.deviceType = ((SerialApiGetCapabilitiesMessageClass)processor).getDeviceType();

				this.enqueue(new SerialApiGetInitDataMessageClass().doRequest());
				break;
			case GetControllerCapabilities:
				this.controllerType = ((GetControllerCapabilitiesMessageClass)processor).getDeviceType();
				break;
			default:
				break;				
		}
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
			this.inputThread = new ZWaveInputThread();
			this.inputThread.start();

			// RXTX serial port library causes high CPU load
			// Start event listener, which will just sleep and slow down event loop
			serialPort.addEventListener(this.receiveThread);
			serialPort.notifyOnDataAvailable(true);

			logger.info("Serial port is initialized");
		} catch (NoSuchPortException e) {
			logger.error("Serial Error: Port {} does not exist", serialPortName);
			throw new SerialInterfaceException(String.format("Port %s does not exist", serialPortName), e);
		} catch (PortInUseException e) {
			logger.error("Serial Error: Port {} in use.", serialPortName);
			throw new SerialInterfaceException(String.format("Port %s in use.", serialPortName), e);
		} catch (UnsupportedCommOperationException e) {
			logger.error("Serial Error: Unsupported comm operation on Port {}.", serialPortName);
			throw new SerialInterfaceException(String.format("Unsupported comm operation on Port %s.", serialPortName), e);
		} catch (TooManyListenersException e) {
			logger.error("Serial Error: Too many listeners on Port {}.", serialPortName);
			e.printStackTrace();
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
		ArrayList<ZWaveEventListener> copy = new ArrayList<ZWaveEventListener>(this.zwaveEventListeners);
		for (Object listener : copy.toArray()) {
			if (!(listener instanceof ZWaveNode))
				continue;
			
			this.zwaveEventListeners.remove(listener);
		}

		this.zwaveNodes.clear();		
		this.sendQueue.clear();
		this.recvQueue.clear();

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
		if (inputThread != null) {
			inputThread.interrupt();
			try {
				inputThread.join();
			} catch (InterruptedException e) {
			}
			inputThread = null;
		}
		if(transactionCompleted.availablePermits() < 0) {
			transactionCompleted.release(transactionCompleted.availablePermits());
		}
		
		transactionCompleted.drainPermits();
		logger.trace("Transaction completed permit count -> {}", transactionCompleted.availablePermits());
		if (this.serialPort != null) {
			this.serialPort.close();
			this.serialPort = null;
		}
		logger.info("Disconnected from serial port");
	}

	/**
	 * Removes the node, and restarts the initialisation sequence
	 * @param nodeId
	 */
	public void reinitialiseNode(int nodeId) {
		this.zwaveNodes.remove(nodeId);
		addNode(nodeId);
	}

	/**
	 * Add a node to the controller
	 * @param nodeId the node number to add
	 */
	private void addNode(int nodeId) {
		new ZWaveInitNodeThread(this, nodeId).start();
	}
	
	/**
	 * Send All On message to all devices that support the Switch All command class
	 */
	public void allOn() {
        Enumeration<Integer> nodeIds = this.zwaveNodes.keys();
        while (nodeIds.hasMoreElements()) {
            Integer nodeId = nodeIds.nextElement();
            ZWaveNode node = this.getNode(nodeId);
            ZWaveSwitchAllCommandClass switchAllCommandClass = (ZWaveSwitchAllCommandClass)node.getCommandClass(ZWaveCommandClass.CommandClass.SWITCH_ALL);
            if (switchAllCommandClass != null) {
                logger.debug("NODE {}: Supports Switch All Command Class Sending AllOn Message", (Object)nodeId);
                switchAllCommandClass.setNode(node);
                switchAllCommandClass.setController(this);
                this.enqueue(switchAllCommandClass.allOnMessage());
                continue;
            }
        }
    }

	/**
	 * Send All Off message to all devices that support the Switch All command class
	 */
    public void allOff() {
        Enumeration<Integer> nodeIds = this.zwaveNodes.keys();
        while (nodeIds.hasMoreElements()) {
            Integer nodeId = nodeIds.nextElement();
            ZWaveNode node = this.getNode(nodeId);
            ZWaveSwitchAllCommandClass switchAllCommandClass = (ZWaveSwitchAllCommandClass)node.getCommandClass(ZWaveCommandClass.CommandClass.SWITCH_ALL);
            if (switchAllCommandClass != null) {
                logger.debug("NODE {}: Supports Switch All Command Class Sending AllOff Message", (Object)nodeId);
                switchAllCommandClass.setNode(node);
                switchAllCommandClass.setController(this);
                this.enqueue(switchAllCommandClass.allOffMessage());
                continue;
            }
        }
    }

	private class ZWaveInitNodeThread extends Thread {
		int nodeId;
		ZWaveController controller;
		
		ZWaveInitNodeThread(ZWaveController controller, int nodeId) {
			this.nodeId = nodeId;
			this.controller = controller;
		}

		@Override
		public void run() {
			logger.debug("NODE {}: Init node thread start", nodeId);	

			// Check if the node exists
			if(zwaveNodes.get(nodeId) != null) {
				logger.warn("NODE {}: Attempting to add node that already exists", nodeId);
				return;
			}
	
			ZWaveNode node = null;
			try {
				ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
				node = nodeSerializer.DeserializeNode(nodeId);
			}
			catch (Exception e) {
				logger.error("NODE {}: Restore from config: Error deserialising XML file. {}", nodeId, e.toString());
				node = null;
			}
			String name = null;
			String location = null;

			// Did the node deserialise ok?
			if (node != null) {
				// Remember the name and location - in case we decide the file was invalid
				name = node.getName();
				location = node.getLocation();

				// Sanity check the data from the file
				if (node.getManufacturer() == Integer.MAX_VALUE ||
						node.getHomeId() != controller.homeId ||
						node.getNodeId() != nodeId) {
					logger.warn("NODE {}: Restore from config: Error. Data invalid, ignoring config.", nodeId);
					node = null;
				}
				else {
					// The restore was ok, but we have some work to set up the links that aren't
					// made as the deserialiser doesn't call the constructor
					logger.debug("NODE {}: Restore from config: Ok.", nodeId);
					node.setRestoredFromConfigfile(controller);

					// Set the controller and node references for all command classes
					for (ZWaveCommandClass commandClass : node.getCommandClasses()) {
						commandClass.setController(controller);
						commandClass.setNode(node);

						// Handle event handlers
						if (commandClass instanceof ZWaveEventListener) {
							controller.addEventListener((ZWaveEventListener)commandClass);
						}

						// If this is the multi-instance class, add all command classes for the endpoints
						if (commandClass instanceof ZWaveMultiInstanceCommandClass) {
							for (ZWaveEndpoint endPoint : ((ZWaveMultiInstanceCommandClass) commandClass)
									.getEndpoints()) {
								for (ZWaveCommandClass endpointCommandClass : endPoint.getCommandClasses()) {
									endpointCommandClass.setController(controller);
									endpointCommandClass.setNode(node);
									endpointCommandClass.setEndpoint(endPoint);

									// Handle event handlers
									if (endpointCommandClass instanceof ZWaveEventListener) {
										controller.addEventListener((ZWaveEventListener)endpointCommandClass);
									}
								}
							}
						}	
					}							
				}
			}

			// Create a new node if it wasn't deserialised ok
			if(node == null) {
				node = new ZWaveNode(controller.homeId, nodeId, controller);

				// Try to maintain the name and location (user supplied data)
				// even if the XML file was considered corrupt and we reload data from the device.
				node.setName(name);
				node.setLocation(location);
			}

			if(nodeId == controller.ownNodeId) {
				// This is the controller node.
				// We already know the device type, id, manufacturer so set it here
				// It won't be set later as we probably won't request the manufacturer specific data
				node.setDeviceId(controller.getDeviceId());
				node.setDeviceType(controller.getDeviceType());
				node.setManufacturer(controller.getManufactureId());
			}

			// Place nodes in the local ZWave Controller
			controller.zwaveNodes.putIfAbsent(nodeId, node);
			node.initialiseNode();

			logger.debug("NODE {}: Init node thread finished", nodeId);	
		}
	}

	/**
	 * Enqueues a message for sending on the send queue.
	 * @param serialMessage the serial message to enqueue.
	 */
	public void enqueue(SerialMessage serialMessage) {
		// Sanity check!
		if(serialMessage == null) {
			return;
		}

		// First try and get the node
		// If we're sending to a node, then this obviously isn't to the controller, and we should
		// queue anything to a battery node (ie a node supporting the WAKEUP class)!
    	ZWaveNode node = this.getNode(serialMessage.getMessageNode());
    	if (node != null) {
	    	// Keep track of the number of packets sent to this device
	    	node.incrementSendCount();

	    	// If the device isn't listening, queue the message if it supports the wakeup class
	    	if (!node.isListening() && !node.isFrequentlyListening()) {
				ZWaveWakeUpCommandClass wakeUpCommandClass = (ZWaveWakeUpCommandClass)node.getCommandClass(CommandClass.WAKE_UP);
	
				// If it's a battery operated device, check if it's awake or place in wake-up queue.
				if (wakeUpCommandClass != null && !wakeUpCommandClass.processOutgoingWakeupMessage(serialMessage)) {
					return;
				}
			}
    	}

		// Add the message to the queue
		this.sendQueue.add(serialMessage);
		logger.debug("Enqueueing message. Queue length = {}", this.sendQueue.size());
	}

	/**
	 * Returns the size of the send queue.
	 */
	public int getSendQueueLength() {
		return this.sendQueue.size();
	}

	/**
	 * Notify our own event listeners of a Z-Wave event.
	 * @param event the event to send.
	 */
	public void notifyEventListeners(ZWaveEvent event) {
		logger.debug("Notifying event listeners: {}", event.getClass().getSimpleName());
		ArrayList<ZWaveEventListener> copy = new ArrayList<ZWaveEventListener>(this.zwaveEventListeners);
		for (ZWaveEventListener listener : copy) {
			listener.ZWaveIncomingEvent(event);
		}
		
		// We also need to handle the inclusion internally within the controller
		if(event instanceof ZWaveInclusionEvent) {
			ZWaveInclusionEvent incEvent = (ZWaveInclusionEvent)event;
			switch(incEvent.getEvent()) {
			case IncludeDone:
				logger.debug("NODE {}: Including node.", incEvent.getNodeId());
				// First make sure this isn't an existing node
				if(getNode(incEvent.getNodeId()) != null) {
					logger.debug("NODE {}: Newly included node already exists - not initialising.", incEvent.getNodeId());
					break;
				}
				
				// Initialise the new node
				addNode(incEvent.getNodeId());
				break;
			case ExcludeDone:
				logger.debug("NODE {}: Excluding node.", incEvent.getNodeId());
				// Remove the node from the controller
				if(getNode(incEvent.getNodeId()) == null) {
					logger.debug("NODE {}: Excluding node that doesn't exist.", incEvent.getNodeId());
					break;
				}
				this.zwaveNodes.remove(incEvent.getNodeId());

				// Remove the XML file
				ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
				nodeSerializer.DeleteNode(event.getNodeId());
				break;
			default:
				break;
			}
		} else if(event instanceof ZWaveNetworkEvent) {
			ZWaveNetworkEvent networkEvent = (ZWaveNetworkEvent)event;
			switch(networkEvent.getEvent()) {
				case DeleteNode:
					if(getNode(networkEvent.getNodeId()) == null) {
						logger.debug("NODE {}: Deleting a node that doesn't exist.", networkEvent.getNodeId());
						break;
					}
					this.zwaveNodes.remove(networkEvent.getNodeId());
					
					//Remove the XML file
					ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
					nodeSerializer.DeleteNode(event.getNodeId());
					break;
				default:
					break;
			}
		} else if (event instanceof ZWaveNodeStatusEvent) {
			ZWaveNodeStatusEvent statusEvent = (ZWaveNodeStatusEvent) event;
			logger.debug("NODE {}: Node Status event - Node is {}", statusEvent.getNodeId(), statusEvent.getState());

			// Get the node
			ZWaveNode node = getNode(event.getNodeId());
			if (node == null) {
				logger.error("NODE {}: Node is unknown!", statusEvent.getNodeId());
				return;
			}

			// Handle node state changes
			switch (statusEvent.getState()) {
			case DEAD:
				break;
			case FAILED:
				break;
			case ALIVE:
				break;
			}
		}
	}
	
	/**
	 * Initializes communication with the Z-Wave controller stick.
	 */
	public void initialize() {
		this.enqueue(new GetVersionMessageClass().doRequest());
		this.enqueue(new MemoryGetIdMessageClass().doRequest());
		this.enqueue(new SerialApiGetCapabilitiesMessageClass().doRequest());
		this.enqueue(new SerialApiSetTimeoutsMessageClass().doRequest(150, 15));
		this.enqueue(new GetSucNodeIdMessageClass().doRequest());
	}

	/**
	 * Send Identify Node message to the controller.
	 * @param nodeId the nodeId of the node to identify
	 */
	public void identifyNode(int nodeId) {
		this.enqueue(new IdentifyNodeMessageClass().doRequest(nodeId));
	}
	
	/**
	 * Send Request Node info message to the controller.
	 * @param nodeId the nodeId of the node to identify
	 */
	public void requestNodeInfo(int nodeId) {
		this.enqueue(new RequestNodeInfoMessageClass().doRequest(nodeId));
	}

	/**
	 * Polls a node for any dynamic information
	 * @param node
	 */
	public void pollNode(ZWaveNode node) {
		for (ZWaveCommandClass zwaveCommandClass : node.getCommandClasses()) {
			logger.trace("NODE {}: Inspecting command class {}", node.getNodeId(), zwaveCommandClass.getCommandClass().getLabel());
			if (zwaveCommandClass instanceof ZWaveCommandClassDynamicState) {
				logger.debug("NODE {}: Found dynamic state command class {}", node.getNodeId(), zwaveCommandClass.getCommandClass()
						.getLabel());
				ZWaveCommandClassDynamicState zdds = (ZWaveCommandClassDynamicState) zwaveCommandClass;
				int instances = zwaveCommandClass.getInstances();
				if (instances == 1) {
					Collection<SerialMessage> dynamicQueries = zdds.getDynamicValues(true);
					for (SerialMessage serialMessage : dynamicQueries) {
						sendData(serialMessage);
					}
				} else {
					for (int i = 1; i <= instances; i++) {
						Collection<SerialMessage> dynamicQueries = zdds.getDynamicValues(true);
						for (SerialMessage serialMessage : dynamicQueries) {
							sendData(node.encapsulate(serialMessage, zwaveCommandClass, i));
						}
					}
				}
			} else if (zwaveCommandClass instanceof ZWaveMultiInstanceCommandClass) {
				ZWaveMultiInstanceCommandClass multiInstanceCommandClass = (ZWaveMultiInstanceCommandClass) zwaveCommandClass;
				for (ZWaveEndpoint endpoint : multiInstanceCommandClass.getEndpoints()) {
					for (ZWaveCommandClass endpointCommandClass : endpoint.getCommandClasses()) {
						logger.trace("NODE {}: Inspecting command class {} for endpoint {}", node.getNodeId(), endpointCommandClass
								.getCommandClass().getLabel(), endpoint.getEndpointId());
						if (endpointCommandClass instanceof ZWaveCommandClassDynamicState) {
							logger.debug("NODE {}: Found dynamic state command class {}", node.getNodeId(), endpointCommandClass
									.getCommandClass().getLabel());
							ZWaveCommandClassDynamicState zdds2 = (ZWaveCommandClassDynamicState) endpointCommandClass;
							Collection<SerialMessage> dynamicQueries = zdds2.getDynamicValues(true);
							for (SerialMessage serialMessage : dynamicQueries) {
								sendData(node.encapsulate(serialMessage,
										endpointCommandClass, endpoint.getEndpointId()));
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Request the node routing information.
	 *
	 * @param nodeId The address of the node to update
	 */
	public void requestNodeRoutingInfo(int nodeId)
	{
		this.enqueue(new GetRoutingInfoMessageClass().doRequest(nodeId));
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
		this.enqueue(new RequestNodeNeighborUpdateMessageClass().doRequest(nodeId));
	}

	/**
	 * Puts the controller into inclusion mode to add new nodes
	 */
	public void requestAddNodesStart()
	{
		this.enqueue(new AddNodeMessageClass().doRequestStart(true));
	}

	/**
	 * Terminates the inclusion mode
	 */
	public void requestAddNodesStop()
	{
		this.enqueue(new AddNodeMessageClass().doRequestStop());
	}

	/**
	 * Puts the controller into exclusion mode to remove new nodes
	 */
	public void requestRemoveNodesStart()
	{
		this.enqueue(new RemoveNodeMessageClass().doRequestStart(true));
	}

	/**
	 * Terminates the exclusion mode
	 */
	public void requestRemoveNodesStop()
	{
		this.enqueue(new RemoveNodeMessageClass().doRequestStop());
	}

	/**
	 * Sends a request to perform a soft reset on the controller.
	 * This will just reset the controller - probably similar to a power cycle.
	 * It doesn't reinitialise the network, or change the network configuration.
	 * 
	 * NOTE: At least for some (most!) sticks, this doesn't return a response.
	 * Therefore, the number of retries is set to 1.
	 * NOTE: On some (most!) ZWave-Plus sticks, this can cause the stick to hang.
	 */
	public void requestSoftReset()
	{
		SerialMessage msg = new SerialApiSoftResetMessageClass().doRequest();
		msg.attempts = 1;
		this.enqueue(msg);
	}

	/**
	 * Sends a request to perform a hard reset on the controller.
	 * This will reset the controller to its default, resetting the network completely
	 */
	public void requestHardReset()
	{
		// Clear the queues
		// If we're resetting, there's no point in queuing messages!
		sendQueue.clear();
		recvQueue.clear();
		
		// Hard reset the stick - everything will be reset to factory default
		SerialMessage msg = new ControllerSetDefaultMessageClass().doRequest();
		msg.attempts = 1;
		this.enqueue(msg);
		
		// Clear all the nodes and we'll reinitialise
		this.zwaveNodes.clear();
		this.enqueue(new SerialApiGetInitDataMessageClass().doRequest());
	}

	/**
	* Request if the node is currently marked as failed by the controller.
	* @param nodeId The address of the node to check
	*/
 	public void requestIsFailedNode(int nodeId)
 	{
 		this.enqueue(new IsFailedNodeMessageClass().doRequest(nodeId));
 	}
 	
	/**
	 * Removes a failed node from the network.
	 * Note that this won't remove nodes that have not failed.
	 * @param nodeId The address of the node to remove
	 */
	public void requestRemoveFailedNode(int nodeId)
	{
		this.enqueue(new RemoveFailedNodeMessageClass().doRequest(nodeId));
	}

	/**
	 * Delete all return nodes from the specified node. This should be performed
	 * before updating the routes
	 * 
	 * @param nodeId
	 */
	public void requestDeleteAllReturnRoutes(int nodeId)
	{
		this.enqueue(new DeleteReturnRouteMessageClass().doRequest(nodeId));
	}

	/**
	 * Request the controller to set the return route between two nodes
	 * 
	 * @param nodeId
	 *            Source node
	 * @param destinationId
	 *            Destination node
	 */
	public void requestAssignReturnRoute(int nodeId, int destinationId)
	{
		this.enqueue(new AssignReturnRouteMessageClass().doRequest(nodeId, destinationId, getCallbackId()));
	}

	/**
	 * Request the controller to set the return route from a node to the controller
	 * 
	 * @param nodeId
	 *            Source node
	 */
	public void requestAssignSucReturnRoute(int nodeId)
	{
		this.enqueue(new AssignSucReturnRouteMessageClass().doRequest(nodeId, getCallbackId()));
	}

	/**
	 * Returns the next callback ID
	 * @return callback ID
	 */
	public int getCallbackId() {
		if (++sentDataPointer > 0xFF)
			sentDataPointer = 1;
		logger.debug("Callback ID = {}", sentDataPointer);
		
		return sentDataPointer;
	}
	
	/**
	 * Transmits the SerialMessage to a single Z-Wave Node.
	 * Sets the transmission options as well.
	 * @param serialMessage the Serial message to send.
	 */
	public void sendData(SerialMessage serialMessage)
	{
		if (serialMessage == null) {
    		logger.error("Null message for sendData");
			return;
		}
    	if (serialMessage.getMessageClass() != SerialMessageClass.SendData) {
    		logger.error(String.format("Invalid message class %s (0x%02X) for sendData", serialMessage.getMessageClass().getLabel(), serialMessage.getMessageClass().getKey()));
    		return;
    	}
    	if (serialMessage.getMessageType() != SerialMessageType.Request) {
    		logger.error("Only request messages can be sent");
    		return;
    	}

    	// We need to wait on the ACK from the controller before completing the transaction.
    	// This is required in case the Application Message is received from the SendData ACK
    	serialMessage.setAckRequired();

    	serialMessage.setTransmitOptions(TRANSMIT_OPTION_ACK | TRANSMIT_OPTION_AUTO_ROUTE | TRANSMIT_OPTION_EXPLORE);
    	serialMessage.setCallbackId(getCallbackId());
    	this.enqueue(serialMessage);
	}
	
	/**
	 * Add a listener for Z-Wave events to this controller.
	 * @param eventListener the event listener to add.
	 */
	public void addEventListener(ZWaveEventListener eventListener) {
		synchronized(this.zwaveEventListeners) {
			this.zwaveEventListeners.add(eventListener);
		}
	}

	/**
	 * Remove a listener for Z-Wave events to this controller.
	 * @param eventListener the event listener to remove.
	 */
	public void removeEventListener(ZWaveEventListener eventListener) {
		synchronized(this.zwaveEventListeners) {
			this.zwaveEventListeners.remove(eventListener);
		}
	}
	
    /**
     * Gets the API Version of the controller.
	 * @return the serialAPIVersion
	 */
	public String getSerialAPIVersion() {
		return serialAPIVersion;
	}
	
    /**
     * Gets the zWave Version of the controller.
	 * @return the zWaveVersion
	 */
	public String getZWaveVersion() {
		return zWaveVersion;
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
	 * Gets the device type of the controller.
	 * @return the device type
	 */
	public ZWaveDeviceType getControllerType() {
		return controllerType;
	}

	/**
	 * Gets the networks SUC controller ID.
	 * @return the device id of the SUC, or 0 if none exists
	 */
	public int getSucId() {
		return sucID;
	}

	/**
	 * Returns true if the binding is the master controller in the network.
	 * The master controller simply means that we get notifications.
	 * @return true if this is the master
	 */
	public boolean isMasterController() {
		return masterController;
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
	 * Gets the node list
	 * @return
	 */
	public Collection<ZWaveNode> getNodes() {
		return this.zwaveNodes.values();
	}

	/**
	 * Indicates a working connection to the
	 * Z-Wave controller stick and initialization complete
	 * @return isConnected;
	 */
	public boolean isConnected() {
		return isConnected; // && initializationComplete;
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
	 * @return the OOFCount
	 */
	public int getOOFCount() {
		return OOFCount;
	}
	
	/**
	 * Returns the number of Time-Outs while sending.
	 * @return the timeoutCount
	 */
	public int getTimeOutCount() {
		return timeOutCount.get();
	}
	
	// Nested classes and enumerations
	
	/**
	 * Input thread. This processes incoming messages - it decouples the receive thread,
	 * which responds to messages from the controller, and the actual processing of messages
	 * to ensure we respond to the controller in a timely manner
	 * @author Chris Jackson
	 */
	private class ZWaveInputThread extends Thread {
		/**
		 * Run method. Runs the actual receiving process.
		 */
		@Override
		public void run() {
			logger.debug("Starting Z-Wave thread: Input");

			SerialMessage recvMessage;
			while (!interrupted()) {
	    		try {
	    			if(recvQueue.size() == 0) {
	    				sendAllowed.release();
	    			}
					recvMessage = recvQueue.take();
					logger.debug("Receive queue TAKE: Length={}", recvQueue.size());
					logger.debug("Process Message = {}", SerialMessage.bb2hex(recvMessage.getMessageBuffer()));

		    		handleIncomingMessage(recvMessage);
		    		sendAllowed.tryAcquire();
				}
				catch (InterruptedException e) {
					break;
				}
	    		catch (Exception e) {
					logger.error("Exception during Z-Wave thread: Input.", e);
				}
			}

			logger.debug("Stopped Z-Wave thread: Input");
		}
	}
	
	/**
	 * Z-Wave controller Send Thread. Takes care of sending all messages.
	 * It uses a semaphore to synchronize communication with the receiving thread.
	 * @author Jan-Willem Spuij
	 * @author Chris Jackson
	 * @since 1.3.0
	 */
	private class ZWaveSendThread extends Thread {

		private final Logger logger = LoggerFactory.getLogger(ZWaveSendThread.class);

		/**
		 * Run method. Runs the actual sending process.
		 */
		@Override
		public void run() {
			logger.debug("Starting Z-Wave thread: Send");
			try {
				while (!interrupted()) {
					// To avoid sending lots of frames when we still have input frames to
					// process, we wait here until we've processed all receive frames
					if(!sendAllowed.tryAcquire(1, zWaveResponseTimeout, TimeUnit.MILLISECONDS)) {
						logger.warn("Receive queue TIMEOUT:", recvQueue.size());
						continue;
					}
					sendAllowed.release();

					// Take the next message from the send queue
					try {
						lastSentMessage = sendQueue.take();
						logger.debug("Took message from queue for sending. Queue length = {}", sendQueue.size());
					} catch (InterruptedException e1) {
						break;
					}

					// Check we got a message
					if (lastSentMessage == null) {
						continue;
					}

					// Get the node for this message
					ZWaveNode node = getNode(lastSentMessage.getMessageNode());

					// If it's a battery device, it needs to be awake, or we queue the frame until it is.
					if (node != null && !node.isListening() && !node.isFrequentlyListening()) {
						ZWaveWakeUpCommandClass wakeUpCommandClass = (ZWaveWakeUpCommandClass)node.getCommandClass(CommandClass.WAKE_UP);

						// If it's a battery operated device, check if it's awake or place in wake-up queue.
						if (wakeUpCommandClass != null && !wakeUpCommandClass.processOutgoingWakeupMessage(lastSentMessage)) {
							continue;
						}
					}
					
					// A transaction consists of (up to) 4 parts -:
					// 1) We send a REQUEST to the controller.
					// 2) The controller sends a RESPONSE almost immediately.
					//    This RESPONSE typically tells us that the message was,
					//    or wasn't, added to the sticks queue.
					// 3) The controller sends a REQUEST once it's received
					//    the response from the device.
					//    We need to be aware that there is no synchronization of
					//    messages between steps 2 and 3 so we can get other messages
					//    received at step 3 that are not related to our original
					//    request.
					// 4) We ultimately receive the requested message from the device
					//    if we're requesting such a message.
					//
					//    A transaction is generally completed at the completion of step 4.
					//    However, for some messages, there may not be a further REQUEST
					//    so the transaction is terminated at step 2. This is handled
					//    by the serial message class processor by setting
					//    transactionCompleted.
					//
					//    It seems that some of these steps may occur out of order. For
					//    example, the requested message at step 4 may be received before
					//    the REQUEST at step 3. This can (I guess) occur if the message to
					//    the device is received by the device, but the ACK back to the controller
					//    is lost. The device then sends the requested data, and then finally
					//    the ACK is received.
					//    We cover this by setting an 'AckPending' flag in the sent message.
					//    This needs to be cleared before the transacion is completed.
					
					// Clear the semaphore used to acknowledge the completed transaction.
					transactionCompleted.drainPermits();

					// Send the REQUEST message TO the controller
					byte[] buffer = lastSentMessage.getMessageBuffer();
					logger.debug("NODE {}: Sending REQUEST Message = {}", lastSentMessage.getMessageNode(), SerialMessage.bb2hex(buffer));
					lastMessageStartTime = System.currentTimeMillis();
					try {
						synchronized (serialPort.getOutputStream()) {
							serialPort.getOutputStream().write(buffer);
							serialPort.getOutputStream().flush();
							logger.trace("Message SENT");
						}
					}
					catch (IOException e) {
						logger.error("Got I/O exception {} during sending. exiting thread.", e.getLocalizedMessage());
						break;
					}

					// Now wait for the RESPONSE, or REQUEST message FROM the controller
					// This will terminate when the transactionCompleted flag gets set
					// So, this might complete on a RESPONSE if there's an error (or no further REQUEST expected)
					// or it might complete on a subsequent REQUEST.
					try {
						if (!transactionCompleted.tryAcquire(1, zWaveResponseTimeout, TimeUnit.MILLISECONDS)) {
							timeOutCount.incrementAndGet();
							// If this is a SendData message, then we need to abort
							// This should only be sent if we didn't get the initial ACK!!!
							// So we need to check the ACK flag and only abort if it's not set
							if (lastSentMessage.getMessageClass() == SerialMessageClass.SendData && lastSentMessage.isAckPending()) {
								buffer = new SerialMessage(SerialMessageClass.SendDataAbort, SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Immediate).getMessageBuffer();
								logger.debug("NODE {}: Sending ABORT Message = {}", lastSentMessage.getMessageNode(), SerialMessage.bb2hex(buffer));
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

							// Check if we've exceeded the number of retries.
							// Requeue if we're ok, otherwise discard the message
							if (--lastSentMessage.attempts >= 0) {
								logger.error("NODE {}: Timeout while sending message. Requeueing - {} attempts left!",
										lastSentMessage.getMessageNode(), lastSentMessage.attempts);
								if (lastSentMessage.getMessageClass() == SerialMessageClass.SendData) {
									handleFailedSendDataRequest(lastSentMessage);
								}
								else {
									enqueue(lastSentMessage);
								}
							} else {
								logger.warn("NODE {}: Too many retries. Discarding message: {}",
										lastSentMessage.getMessageNode(), lastSentMessage.toString());
							}
							continue;
						}
						long responseTime = System.currentTimeMillis() - lastMessageStartTime;
						if(responseTime > longestResponseTime) {
							longestResponseTime = responseTime;
						}
						logger.debug("NODE {}: Response processed after {}ms/{}ms.", lastSentMessage.getMessageNode(), responseTime, longestResponseTime);
						logger.trace("Acquired. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
					}
					catch (InterruptedException e) {
						break;
					}
				}
			}
			catch (Exception e) {
				logger.error("Exception during Z-Wave thread: Send", e);
			}
			logger.debug("Stopped Z-Wave thread: Send");
		}
	}

	/**
	 * Z-Wave controller Receive Thread. Takes care of receiving all messages.
	 * It uses a semaphore to synchronize communication with the sending thread.
	 * @author Jan-Willem Spuij
	 * @since 1.3.0
	 */	
	private class ZWaveReceiveThread extends Thread implements SerialPortEventListener {
		
		private static final int SOF = 0x01;
		private static final int ACK = 0x06;
		private static final int NAK = 0x15;
		private static final int CAN = 0x18;

		private final Logger logger = LoggerFactory.getLogger(ZWaveReceiveThread.class);

		@Override
		public void serialEvent(SerialPortEvent arg0) {
			try {
				logger.trace("RXTX library CPU load workaround, sleep forever");
				Thread.sleep(Long.MAX_VALUE);
			} catch (InterruptedException e) {
			}
		}
		
		/**
    	 * Sends 1 byte frame response.
    	 * @param response the response code to send.
    	 */
		private void sendResponse(int response) {
			try {
				synchronized (serialPort.getOutputStream()) {
					serialPort.getOutputStream().write(response);
					serialPort.getOutputStream().flush();
					logger.trace("Response SENT");
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}

		/**
    	 * Processes incoming message and notifies event handlers.
    	 * @param buffer the buffer to process.
		 * @throws InterruptedException 
    	 */
    	private void processIncomingMessage(byte[] buffer) throws InterruptedException {
    		SerialMessage recvMessage = new SerialMessage(buffer);
    		if (recvMessage.isValid) {
    			logger.trace("Message is valid, sending ACK");
    			sendResponse(ACK);
    		} else {
    			logger.error("Message is not valid, discarding");
    			sendResponse(NAK);
    			
    			// The semaphore is acquired when we start the receive.
    			// We need to release it now...
    			if(recvQueue.size() == 0) {
    				sendAllowed.release();
    			}
    			return;
    		}

    		recvQueue.add(recvMessage);
			logger.debug("Receive queue ADD: Length={}", recvQueue.size());
        }

		/**
		 * Run method. Runs the actual receiving process.
		 */
		@Override
		public void run() {
			logger.debug("Starting Z-Wave thread: Receive");
			try {
				// Send a NAK to resynchronise communications
				sendResponse(NAK);

				// If we want to do a soft reset on the serial interfaces, do it here.
				// It seems there's no response to this message, so sending it through
				// 'normal' channels will cause a timeout.
				if(softReset == true) {
					try {
						synchronized (serialPort.getOutputStream()) {
							SerialMessage resetMsg = new SerialApiSoftResetMessageClass().doRequest();
							byte[] buffer = resetMsg.getMessageBuffer();

							serialPort.getOutputStream().write(buffer);
							serialPort.getOutputStream().flush();
						}
					} catch (IOException e) {
						logger.error("Error sending soft reset on initialisation: {}", e.getMessage());
					}
				}

				while (!interrupted()) {
					int nextByte;

					try {
						nextByte = serialPort.getInputStream().read();

						if (nextByte == -1) {
							continue;
						}
					} catch (IOException e) {
						logger.error("Got I/O exception {} during receiving. exiting thread.", e.getLocalizedMessage());
						break;
					}

					switch (nextByte) {
						case SOF:
				    		// Use the sendAllowed semaphore to signal that the receive queue is not empty!
							sendAllowed.acquire();

							SOFCount++;
							int messageLength;

							try {
								messageLength = serialPort.getInputStream().read();
							} catch (IOException e) {
								logger.error("Got I/O exception {} during receiving. exiting thread.", e.getLocalizedMessage());

								sendAllowed.release();
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

							logger.debug("Receive Message = {}", SerialMessage.bb2hex(buffer));
							processIncomingMessage(buffer);
							break;
						case ACK:
							ACKCount++;
	    					logger.trace("Received ACK");
							break;
						case NAK:
							NAKCount++;
	    					logger.error("Protocol error (NAK), discarding");
	    					transactionCompleted.release();
	    					logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
							break;
						case CAN:
							CANCount++;
	    					logger.error("Protocol error (CAN), resending");
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								break;
							}
	    					enqueue(lastSentMessage);
	    					transactionCompleted.release();
	    					logger.trace("Released. Transaction completed permit count -> {}", transactionCompleted.availablePermits());
							break;
						default:
	    					OOFCount++;
							logger.warn(String.format("Protocol error (OOF). Got 0x%02X. Sending NAK.", nextByte));
	    					sendResponse(NAK);
	    					break;
					}
				}
			} catch (Exception e) {
				logger.error("Exception during Z-Wave thread: Receive", e);
			}
			logger.debug("Stopped Z-Wave thread: Receive");

			serialPort.removeEventListener();
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
					(sendThread != null && !sendThread.isAlive()) ||
					(inputThread != null && !inputThread.isAlive())
					)
			{
				logger.warn("Threads not alive, respawning");
				disconnect();
				try {
					connect(serialPortName);
				} catch (SerialInterfaceException e) {
					logger.error("Unable to restart Serial threads: {}", e.getLocalizedMessage());
				}
			}
		}
	}
}
