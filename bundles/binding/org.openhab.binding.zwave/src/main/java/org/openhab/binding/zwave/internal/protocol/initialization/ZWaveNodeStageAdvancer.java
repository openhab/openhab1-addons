/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.initialization;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

import org.openhab.binding.zwave.internal.protocol.NodeStage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveEventListener;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveVersionCommandClass.LibraryType;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClassDynamicState;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClassInitialization;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveManufacturerSpecificCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveMultiInstanceCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveNoOperationCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveVersionCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveWakeUpCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveTransactionCompletedEvent;
import org.openhab.binding.zwave.internal.protocol.serialmessage.GetRoutingInfoMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.IdentifyNodeMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.RequestNodeInfoMessageClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZWaveNodeStageAdvancer class. Advances the node stage, thereby controlling
 * the initialization of a node.
 * 
 * The NodeStageAdvancer registers an event listener during the initialisation of a node.
 * This allows it to be notified when each transaction is complete, and we can process
 * this accordingly. The event listener is removed when we stop initialising to reduce
 * processor loading.
 * 
 * Command classes are responsible for building lists of messages needed to initialise themselves.
 * The command class also needs to keep track of responses so it knows if initialisation of this
 * stage is complete. Other than that, the command class does not have any input into the initialisation
 * phase, and the sequencing of events - this is all handled here in the node advancer class.
 * 
 * For each stage, the advancer builds a list of all messages that need to be sent to the node.
 * Since the initialisation phase is an intense period, with a lot of messages on the network, we try
 * and ensure that only 1 packet is outstanding to any node at once.
 * 
 * Each time we receive an ack for a message, the node advancer gets called, and we see if this is an ack
 * for a message that's part of the initialisation. If it is, the message gets removed from the list.
 * 
 * Each time we receive a command message, the node advancer gets called. This is called after the command
 * class has been updated, so at this stage we know if the stage can be completed.
 * 
 * Two checks are performed to allow a node stage to advance. Firstly, we make sure we've sent all the messages
 * required for this phase. Sending the messages however doesn't guarantee that we get a response, so we
 * then run through the stage again to make sure that the command class really is initialised. If the second
 * run queues no messages, then we can reliably assume this stage is completed. If we've missed anything, then
 * we continue until there are no messages to send.
 * 
 * @author Jan-Willem Spuij
 * @author Chris Jackson
 * @since 1.4.0
 */
public class ZWaveNodeStageAdvancer implements ZWaveEventListener {

	private static final ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
	private static final Logger logger = LoggerFactory.getLogger(ZWaveNodeStageAdvancer.class);

	private ZWaveNode node;
	private ZWaveController controller;
	private boolean initializationComplete = false;
	private boolean restoredFromConfigfile = false;

	private static final int MAX_BUFFFER_LEN = 32;
	private ArrayBlockingQueue<SerialMessage> msgQueue;
	private boolean freeToSend = true;
	private boolean stageAdvanced = true;

	private Date queryStageTimeStamp;
	private NodeStage currentStage;

	/**
	 * Constructor. Creates a new instance of the ZWaveNodeStageAdvancer class.
	 * 
	 * @param node
	 *            the node this advancer belongs to.
	 * @param controller
	 *            the controller to use
	 */
	public ZWaveNodeStageAdvancer(ZWaveNode node, ZWaveController controller) {
		this.node = node;
		this.controller = controller;

		currentStage = NodeStage.EMPTYNODE;

		// Initialise the message queue
		msgQueue = new ArrayBlockingQueue<SerialMessage>(MAX_BUFFFER_LEN, true);
	}

	/**
	 * Handles the removal of frames from the send queue.
	 * This gets called after we have an ACK for our packet, but before we get the response.
	 * The actual sending of frames, and the advancing is carried out
	 * in the advanceNodeStage method.
	 */
	public void handleNodeQueue(SerialMessage incomingMessage) {
		// If initialisation is complete, then just return.
		if (this.initializationComplete) {
			return;
		}

		logger.debug("NODE {}: Node advancer - checking intialisation queue.", this.node.getNodeId());

		// If this message is in the queue, then remove it
		if (this.msgQueue.contains(incomingMessage)) {
			this.msgQueue.remove(incomingMessage);
			logger.debug("NODE {}: Node advancer - message removed from queue. Queue size now {}.", this.node.getNodeId(), this.msgQueue.size());

			freeToSend = true;
		}
		
		advanceNodeStage(incomingMessage.getMessageClass());
	}
	
	public void startInitialisation() {
		// Set an event callback so we get notification of events
		this.controller.addEventListener(this);

		advanceNodeStage(null);
	}

	/**
	 * Sends a message if there is one queued
	 * @return true if a message was sent. false otherwise.
	 */
	private boolean sendMessage() {
		if(msgQueue.isEmpty() == true) {
			return false;
		}
		
		// Check to see if we need to send a frame
		if(freeToSend == true) {
			SerialMessage msg = this.msgQueue.peek();
			if(msg != null) {
				freeToSend = false;

				if (msg.getMessageClass() == SerialMessageClass.SendData) {
					controller.sendData(msg);
				}
				else {
					controller.enqueue(msg);
				}

				logger.debug("NODE {}: Node advancer - queued packet. Queue length now {}", this.node.getNodeId(), msgQueue.size());
			}
		}
		
		return true;
	}

	/**
	 * Advances the initialization stage for this node.
	 * This method is called after a response is received. We don't necessarily
	 * know if the response is to the frame we requested though, so to be sure the initialisation
	 * gets all the information it needs, the command class itself gets queried.
	 * This method also handles the sending of frames. Since the intialisation phase is a busy one
	 * we try and only have one outstanding request. Again though, we can't be sure that a response
	 * is aligned with the node advancer request so it is possible that more than one packet can
	 * be released at once, but it will constrain things.
	 */
	public void advanceNodeStage(SerialMessageClass eventClass) {
		// If initialisation is complete, then just return.
		if (this.initializationComplete) {
			return;
		}

		logger.debug("NODE {}: Node advancer - {} - queue length {} - free to send {}.", this.node.getNodeId(), currentStage.getLabel(), msgQueue.size(), freeToSend);

		// If the queue is not empty, then we can't advance any further.
		if(sendMessage() == true) {
			// We're still sending messages, so we're not ready to proceed.
			return;
		}

		// We run through all stages until one queues a message.
		// Then we will wait for the response before continuing
		do {
			// Remember the time so we can handle retries and keep users informed
			queryStageTimeStamp = Calendar.getInstance().getTime();

			switch (currentStage) {
			case EMPTYNODE:
				logger.debug("NODE {}: Starting initialisation", this.node.getNodeId());
				break;

			case PROTOINFO:
				// If the incoming frame is the IdentifyNode, then we continue
				if(eventClass == SerialMessageClass.IdentifyNode) {
					break;
				}

				logger.debug("NODE {}: PROTOINFO - send IdentifyNode", this.node.getNodeId());
				addToQueue(new IdentifyNodeMessageClass().doRequest(this.node.getNodeId()));
				break;

			case PING:
				// If this is the controller, we're done
				if (this.node.getNodeId() == this.controller.getOwnNodeId()) {
					logger.debug("NODE {}: PING - Controller - terminating initialisation", this.node.getNodeId());
					this.currentStage = NodeStage.DONE;
					break;
				}

				// Completion of this stage is reception of a SendData frame.
				// The purpose of this stage is to ensure that the node is awake before
				// requesting further information.
				// It's not 100% guaranteed that this was our NoOp frame, but who cares!
				if(eventClass == SerialMessageClass.SendData) {
					break;
				}

				ZWaveNoOperationCommandClass zwaveCommandClass = (ZWaveNoOperationCommandClass) this.node
						.getCommandClass(CommandClass.NO_OPERATION);
				if (zwaveCommandClass == null) {
					break;
				}

				logger.debug("NODE {}: PING - send NoOperation", this.node.getNodeId());
				addToQueue(zwaveCommandClass.getNoOperationMessage());
				break;

			case WAKEUP:
				break;

			case DETAILS:
				// If restored from a config file, redo from the dynamic node stage.
				if (this.isRestoredFromConfigfile()) {
					logger.debug("NODE {}: Restored from file - skipping static initialisation", this.node.getNodeId());
					this.currentStage = NodeStage.SESSION_START;
					break;
				}

				// If the incoming frame is the IdentifyNode, then we continue
				if(eventClass == SerialMessageClass.RequestNodeInfo) {
					break;
				}

				logger.debug("NODE {}: DETAILS - send RequestNodeInfo", this.node.getNodeId());
				addToQueue(new RequestNodeInfoMessageClass().doRequest(this.node.getNodeId()));
				break;

			case MANUFACTURER:
				// If we already know the device information, then continue
				if (this.node.getManufacturer() != Integer.MAX_VALUE && this.node.getDeviceType() != Integer.MAX_VALUE
						&& this.node.getDeviceId() != Integer.MAX_VALUE) {
					break;
				}

				// try and get the manufacturerSpecific command class.
				ZWaveManufacturerSpecificCommandClass manufacturerSpecific = (ZWaveManufacturerSpecificCommandClass) this.node
						.getCommandClass(CommandClass.MANUFACTURER_SPECIFIC);

				if (manufacturerSpecific != null) {
					// If this node implements the Manufacturer Specific command
					// class, we use it to get manufacturer info.
					logger.debug("NODE {}: MANUFACTURER - send ManufacturerSpecific", this.node.getNodeId());
					addToQueue(manufacturerSpecific.getManufacturerSpecificMessage());
				}
				break;

			case VERSION:
				// Try and get the version command class.
				ZWaveVersionCommandClass version = (ZWaveVersionCommandClass) this.node
						.getCommandClass(CommandClass.VERSION);

				// Loop through all command classes, requesting their version using
				// the Version command class
				for (ZWaveCommandClass zwaveVersionClass : this.node.getCommandClasses()) {
					logger.debug("NODE {}: VERSION - checking {}, version is {}", this.node.getNodeId(), zwaveVersionClass.getCommandClass().getLabel(), zwaveVersionClass.getVersion());
					if (version != null && zwaveVersionClass.getMaxVersion() > 1 && zwaveVersionClass.getVersion() == 0) {
						logger.debug("NODE {}: VERSION - queued   {}", this.node.getNodeId(), zwaveVersionClass.getCommandClass().getLabel());
						addToQueue(version.checkVersion(zwaveVersionClass));
					}
					else {
						zwaveVersionClass.setVersion(1);
					}
				}
				logger.debug("NODE {}: VERSION - queued {} frames", this.node.getNodeId(), this.msgQueue.size());
				break;

			case APP_VERSION:
				ZWaveVersionCommandClass versionCommandClass = (ZWaveVersionCommandClass) node
						.getCommandClass(CommandClass.VERSION);

				if (versionCommandClass == null) {
					break;
				}

				// If we know the library type, then we've got the app version
				if(versionCommandClass.getLibraryType() != LibraryType.LIB_UNKNOWN) {
					break;
				}

				// Request the version report for this node
				logger.debug("NODE {}: APP_VERSION - send VersionMessage", this.node.getNodeId());
				addToQueue(versionCommandClass.getVersionMessage());
				break;

			case ENDPOINTS:
				// Try and get the multi instance / channel command class.
				ZWaveMultiInstanceCommandClass multiInstance = (ZWaveMultiInstanceCommandClass) this.node
						.getCommandClass(CommandClass.MULTI_INSTANCE);

				if (multiInstance != null) {
					logger.debug("NODE {}: ENDPOINTS - MultiInstance is supported", this.node.getNodeId());
					addToQueue(multiInstance.initEndpoints(stageAdvanced));
					logger.debug("NODE {}: ENDPOINTS - queued {} frames", this.node.getNodeId(), this.msgQueue.size());
				}
				break;

			case STATIC_VALUES:
				// Loop through all classes looking for static initialisation
				for (ZWaveCommandClass zwaveStaticClass : this.node.getCommandClasses()) {
					logger.debug("NODE {}: STATIC_VALUES - checking {}", this.node.getNodeId(), zwaveStaticClass
							.getCommandClass().getLabel());
					if (zwaveStaticClass instanceof ZWaveCommandClassInitialization) {
						logger.debug("NODE {}: STATIC_VALUES - found    {}", this.node.getNodeId(),
								zwaveStaticClass.getCommandClass().getLabel());
						ZWaveCommandClassInitialization zcci = (ZWaveCommandClassInitialization) zwaveStaticClass;
						int instances = zwaveStaticClass.getInstances();
						if (instances == 0) {
							addToQueue(zcci.initialize(stageAdvanced));
						}
						else {
							for (int i = 1; i <= instances; i++) {
								addToQueue(zcci.initialize(stageAdvanced), zwaveStaticClass, i);
							}
						}
					}
					else if (zwaveStaticClass instanceof ZWaveMultiInstanceCommandClass) {
						ZWaveMultiInstanceCommandClass multiInstanceCommandClass = (ZWaveMultiInstanceCommandClass) zwaveStaticClass;
						for (ZWaveEndpoint endpoint : multiInstanceCommandClass.getEndpoints()) {
							for (ZWaveCommandClass endpointCommandClass : endpoint.getCommandClasses()) {
								logger.debug("NODE {}: STATIC_VALUES - checking {} for endpoint {}",
										this.node.getNodeId(), endpointCommandClass.getCommandClass().getLabel(),
										endpoint.getEndpointId());
								if (endpointCommandClass instanceof ZWaveCommandClassInitialization) {
									logger.debug("NODE {}: STATIC_VALUES - found    {}",
											this.node.getNodeId(), endpointCommandClass.getCommandClass().getLabel());
									ZWaveCommandClassInitialization zcci2 = (ZWaveCommandClassInitialization) endpointCommandClass;
									addToQueue(zcci2.initialize(stageAdvanced), endpointCommandClass, endpoint.getEndpointId());
								}
							}
						}
					}
				}
				logger.debug("NODE {}: STATIC_VALUES - queued {} frames", this.node.getNodeId(), this.msgQueue.size());
				break;

			case DYNAMIC_VALUES:
				for (ZWaveCommandClass zwaveDynamicClass : this.node.getCommandClasses()) {
					logger.debug("NODE {}: DYNAMIC_VALUES - checking {}", this.node.getNodeId(), zwaveDynamicClass
							.getCommandClass().getLabel());
					if (zwaveDynamicClass instanceof ZWaveCommandClassDynamicState) {
						logger.debug("NODE {}: DYNAMIC_VALUES - found    {}", this.node.getNodeId(),
								zwaveDynamicClass.getCommandClass().getLabel());
						ZWaveCommandClassDynamicState zdds = (ZWaveCommandClassDynamicState) zwaveDynamicClass;
						int instances = zwaveDynamicClass.getInstances();
						if (instances == 0) {
							addToQueue(zdds.getDynamicValues(stageAdvanced));
						}
						else {
							for (int i = 1; i <= instances; i++) {
								addToQueue(zdds.getDynamicValues(stageAdvanced), zwaveDynamicClass, i);
							}
						}
					}
					else if (zwaveDynamicClass instanceof ZWaveMultiInstanceCommandClass) {
						ZWaveMultiInstanceCommandClass multiInstanceCommandClass = (ZWaveMultiInstanceCommandClass) zwaveDynamicClass;
						for (ZWaveEndpoint endpoint : multiInstanceCommandClass.getEndpoints()) {
							for (ZWaveCommandClass endpointCommandClass : endpoint.getCommandClasses()) {
								logger.debug("NODE {}: DYNAMIC_VALUES - checking {} for endpoint {}",
										this.node.getNodeId(), endpointCommandClass.getCommandClass().getLabel(),
										endpoint.getEndpointId());
								if (endpointCommandClass instanceof ZWaveCommandClassDynamicState) {
									logger.debug("NODE {}: DYNAMIC_VALUES - found    {}",
											this.node.getNodeId(), endpointCommandClass.getCommandClass().getLabel());
									ZWaveCommandClassDynamicState zdds2 = (ZWaveCommandClassDynamicState) endpointCommandClass;
									addToQueue(zdds2.getDynamicValues(stageAdvanced), endpointCommandClass,
											endpoint.getEndpointId());
								}
							}
						}
					}
				}
				logger.debug("NODE {}: DYNAMIC_VALUES - queued {} frames", this.node.getNodeId(), this.msgQueue.size());
				break;
				
			case NEIGHBORS:
				// If the incoming frame is the IdentifyNode, then we continue
				if(eventClass == SerialMessageClass.GetRoutingInfo) {
					break;
				}

				logger.debug("NODE {}: NEIGHBORS - send RoutingInfo", this.node.getNodeId());
				addToQueue(new GetRoutingInfoMessageClass().doRequest(this.node.getNodeId()));
				break;

			case DONE:
				initializationComplete = true;
				logger.debug("NODE {}: Node advancer - initialisation complete!", this.node.getNodeId());
			case DEAD:
			case FAILED:
				// Save the node information to file
				nodeSerializer.SerializeNode(this.node);
				
				// We remove the event listener to reduce loading now that we're done
				controller.removeEventListener(this);
				
				// Return from here as we're now done and we don't want to increment the stage!
				return;
				
			case SESSION_START:
				// This is a 'do nothing' state.
				// It's used as a marker within the NodeStage class to indicate where
				// to start initialisation if we restored from XML.
				break;

			default:
				logger.error("NODE {}: Unknown node state {} encountered.", this.node.getNodeId(), this.node
						.getNodeStage().getLabel());
				break;
			}

			// The stageAdvanced flag is used to tell command classes that this is the first iteration.
			// During the first iteration all messages are queued. After this, only outstanding requests
			// are returned. This continues until there are no requests required.
			stageAdvanced = false;

			// If there are messages queued, send one.
			// If there are none, then it means we're happy that we have all the data for this stage.
			if(sendMessage() == false) {
				// Move on to the next stage
				currentStage = currentStage.getNextStage();
				stageAdvanced = true;
				logger.debug("NODE {}: Node advancer - advancing to {}.", this.node.getNodeId(), currentStage.getLabel());
			}
		}
		while (msgQueue.isEmpty());
	}

	/**
	 * Move the messages to the queue
	 * @param msgs the message collection
	 */
	private void addToQueue(SerialMessage serialMessage) {
		if (!this.msgQueue.contains(serialMessage)) {
			this.msgQueue.add(serialMessage);
		}
	}

	/**
	 * Move all the messages in a collection to the queue
	 * @param msgs the message collection
	 */
	private void addToQueue(Collection<SerialMessage> msgs) {
		for (SerialMessage serialMessage : msgs) {
			addToQueue(serialMessage);
		}
	}

	/**
	 * Move all the messages in a collection to the queue and encapsulates them
	 * @param msgs the message collection
	 * @param the command class to encapsulate
	 * @param endpointId the endpoint number
	 */
	private void addToQueue(Collection<SerialMessage> msgs, ZWaveCommandClass commandClass, int endpointId) {
		for (SerialMessage serialMessage : msgs) {
			addToQueue(this.node.encapsulate(serialMessage, commandClass, endpointId));
		}
	}

	/**
	 * Gets the current node stage
	 * @return current node stage
	 */
	public NodeStage getCurrentStage() {
		return currentStage;
	}

	/**
	 * Sets the current node stage
	 */
	public void setCurrentStage(NodeStage newStage) {
		currentStage = newStage;
	}
	
	/**
	 * Sets the time stamp the node was last queried.
	 * @param queryStageTimeStamp the queryStageTimeStamp to set
	 */
	public Date getQueryStageTimeStamp() {
		return queryStageTimeStamp;
	}

	/**
	 * Returns whether the initialization process has completed.
	 * 
	 * @return true if initialization has completed. False otherwise.
	 */
	public boolean isInitializationComplete() {
		return initializationComplete;
	}

	/**
	 * Returns whether the node was restored from a config file.
	 * 
	 * @return the restoredFromConfigfile
	 */
	public boolean isRestoredFromConfigfile() {
		return restoredFromConfigfile;
	}

	/**
	 * Sets the flag to indicate that this node was restored from file
	 */
	public void setRestoredFromConfigfile() {
		restoredFromConfigfile = true;
	}

	@Override
	public void ZWaveIncomingEvent(ZWaveEvent event) {
		// If we've already completed initialisation, then we're done!
		if(this.initializationComplete == true) {
			return;
		}

		// Process transaction complete events
		if (event instanceof ZWaveTransactionCompletedEvent) {
			ZWaveTransactionCompletedEvent completeEvent = (ZWaveTransactionCompletedEvent)event;

			SerialMessage serialMessage = completeEvent.getCompletedMessage();
			byte[] payload = serialMessage.getMessagePayload();

			// Make sure this is addressed to us
			if (payload.length == 0 || this.node.getNodeId() != (payload[0] & 0xFF)) {
				// This is a corrupt frame, OR, it's not addressed to us
				return;
			}

			switch(serialMessage.getMessageClass()) {
				case SendData:
				case IdentifyNode:
				case RequestNodeInfo:
				case GetRoutingInfo:
					logger.debug("NODE {}: Initialisation transaction complete event ({}:{}) {}", this.node.getNodeId(),
							serialMessage.getMessageClass(), serialMessage.getMessageType(), completeEvent.getState());
					
					if(((ZWaveTransactionCompletedEvent) event).getState()) {
						handleNodeQueue(serialMessage);
					}
					break;
				default:
			}
		}
		// WAKEUP EVENT?.
		else if (event instanceof ZWaveWakeUpCommandClass.ZWaveWakeUpEvent) {
			// We only care about the wake-up notification
			if (((ZWaveWakeUpCommandClass.ZWaveWakeUpEvent) event).getEvent() != ZWaveWakeUpCommandClass.WAKE_UP_NOTIFICATION) {
				return;
			}

			// A wakeup event is received. Make sure it's for this node
			if(this.node.getNodeId() != event.getNodeId()) {
				return;
			}
			
			logger.debug("NODE {}: Wakeup during initialisation.", this.node.getNodeId());

			advanceNodeStage(null);
		}
	}
}
