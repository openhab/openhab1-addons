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
import org.openhab.binding.zwave.internal.protocol.SerialInterfaceException;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClassDynamicState;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClassInitialization;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveManufacturerSpecificCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveMultiInstanceCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveNoOperationCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveVersionCommandClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.RequestNodeInfoMessageClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZWaveNodeStageAdvancer class. Advances the node stage, thereby controlling
 * the initialization of a node.
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
 * then run through the stage again to make sure that the command class really is initialised.
 * 
 * @author Jan-Willem Spuij
 * @author Chris Jackson
 * @since 1.4.0
 */
public class ZWaveNodeStageAdvancer {

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
	public void advanceNodeStage() {
		// If initialisation is complete, then just return.
		if (this.initializationComplete) {
			return;
		}

		logger.debug("NODE {}: Node advancer - state {}, queue length {}, free to send {}.", this.node.getNodeId(), currentStage.getLabel(), msgQueue.size(), freeToSend);

		// A SerialMessage for queuing!
		SerialMessage msg;

		// If the queue is not empty, then we can't advance any further.
		if(msgQueue.isEmpty() == false) {
			// Check to see if we need to send a frame
			if(freeToSend == true) {
				try {
					msg = this.msgQueue.take();
					if(msg != null) {
						freeToSend = false;
						controller.sendData(msg);

						logger.debug("NODE {}: Node advancer - queued packet.", this.node.getNodeId());
					}
				}
				catch(InterruptedException e) {
				}
			}

			return;
		}

		// We run through all stages until one queues a message.
		// Then we will wait for the response before continuing
		do {
			// Remember the time so we can handle retries and keep users informed
			queryStageTimeStamp = Calendar.getInstance().getTime();

			switch (currentStage) {
			case EMPTYNODE:
				// Start the node initialisation.
				// Check if the node has the manufacturer set.
				// If it's set, then we must have received the node identity
				// frame
				// so we can continue to the next stage.
				if (this.node.getManufacturer() != Integer.MAX_VALUE) {
					// Step to the next stage, and fall through...
					this.currentStage = NodeStage.PROTOINFO;
				} else {
					try {
						// this.node.setNodeStage(NodeStage.PROTOINFO);
						this.controller.identifyNode(this.node.getNodeId());
					} catch (SerialInterfaceException e) {
						logger.error("NODE {}: Got error {}, while identifying node", this.node.getNodeId(),
								e.getLocalizedMessage());
					}
				}
				break;

			case PROTOINFO:
				// If this is the controller, we're done
				if (this.node.getNodeId() == this.controller.getOwnNodeId()) {
					this.currentStage = NodeStage.DONE;
					break;
				}

				ZWaveNoOperationCommandClass zwaveCommandClass = (ZWaveNoOperationCommandClass) this.node
						.getCommandClass(CommandClass.NO_OPERATION);
				if (zwaveCommandClass == null) {
					break;
				}

				msg = zwaveCommandClass.getNoOperationMessage();
				this.msgQueue.add(msg);
				break;

			case PING:
			case WAKEUP:
				// If restored from a config file, redo from the dynamic node
				// stage.
				if (this.isRestoredFromConfigfile()) {
					this.currentStage = NodeStage.DYNAMIC;
					break;
				}

				msg = new RequestNodeInfoMessageClass().doRequest(this.node.getNodeId());
				this.msgQueue.add(msg);
				break;

			case DETAILS:
				// If we already know the manufacturer information, then
				// continue
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
					msg = manufacturerSpecific.getManufacturerSpecificMessage();
					this.msgQueue.add(msg);
					break;
				}
				break;

			case MANSPEC01:
				// Try and get the version command class.
				ZWaveVersionCommandClass version = (ZWaveVersionCommandClass) this.node
						.getCommandClass(CommandClass.VERSION);

				// Loop through all command classes, requesting their version
				// using
				// the Version command class
				for (ZWaveCommandClass zwaveVersionClass : this.node.getCommandClasses()) {
					if (version != null && zwaveVersionClass.getMaxVersion() > 1) {
						msg = version.checkVersion(zwaveVersionClass);
						this.msgQueue.add(msg);
					} else {
						zwaveVersionClass.setVersion(1);
					}
				}
				break;

			case VERSION:
				// this.node.setNodeStage(NodeStage.INSTANCES_ENDPOINTS);
				// try and get the multi instance / channel command class.
				ZWaveMultiInstanceCommandClass multiInstance = (ZWaveMultiInstanceCommandClass) this.node
						.getCommandClass(CommandClass.MULTI_INSTANCE);

				if (multiInstance != null) {
					addCollectionToQueue(multiInstance.initEndpoints(stageAdvanced));
				}
				break;

			case INSTANCES_ENDPOINTS:
				// This is in the command class at the moment
				// for (int i=1; i <= endpoints; i++) {
				// if (!endpointsAreTheSameDeviceClass || i == 1)
				// this.getController().sendData(this.getMultiChannelCapabilityGetMessage(endpoint));
				// }

				// this.node.setNodeStage(NodeStage.STATIC_VALUES);
				// Manually increment the stage and fall through
				break;

			case STATIC_VALUES:
				// Loop through all classes looking for static initialisation
				for (ZWaveCommandClass zwaveStaticClass : this.node.getCommandClasses()) {
					logger.trace("NODE {}: Inspecting command class {}", this.node.getNodeId(), zwaveStaticClass
							.getCommandClass().getLabel());
					if (zwaveStaticClass instanceof ZWaveCommandClassInitialization) {
						logger.debug("NODE {}: Found initializable command class {}", this.node.getNodeId(),
								zwaveStaticClass.getCommandClass().getLabel());
						ZWaveCommandClassInitialization zcci = (ZWaveCommandClassInitialization) zwaveStaticClass;
						int instances = zwaveStaticClass.getInstances();
						if (instances == 0) {
							addCollectionToQueue(zcci.initialize(stageAdvanced));
						} else {
							for (int i = 1; i <= instances; i++) {
								addCollectionToQueue(zcci.initialize(stageAdvanced), zwaveStaticClass, i);
							}
						}
					} else if (zwaveStaticClass instanceof ZWaveMultiInstanceCommandClass) {
						ZWaveMultiInstanceCommandClass multiInstanceCommandClass = (ZWaveMultiInstanceCommandClass) zwaveStaticClass;
						for (ZWaveEndpoint endpoint : multiInstanceCommandClass.getEndpoints()) {
							for (ZWaveCommandClass endpointCommandClass : endpoint.getCommandClasses()) {
								logger.trace("NODE {}: Inspecting command class {} for endpoint {}",
										this.node.getNodeId(), endpointCommandClass.getCommandClass().getLabel(),
										endpoint.getEndpointId());
								if (endpointCommandClass instanceof ZWaveCommandClassInitialization) {
									logger.debug("NODE {}: Found initializable command class {}",
											this.node.getNodeId(), endpointCommandClass.getCommandClass().getLabel());
									ZWaveCommandClassInitialization zcci2 = (ZWaveCommandClassInitialization) endpointCommandClass;
									addCollectionToQueue(zcci2.initialize(stageAdvanced), endpointCommandClass, endpoint.getEndpointId());
								}
							}
						}
					}
				}
				break;

			case DYNAMIC:
				for (ZWaveCommandClass zwaveDynamicClass : this.node.getCommandClasses()) {
					logger.trace("NODE {}: Inspecting command class {}", this.node.getNodeId(), zwaveDynamicClass
							.getCommandClass().getLabel());
					if (zwaveDynamicClass instanceof ZWaveCommandClassDynamicState) {
						logger.debug("NODE {}: Found dynamic state command class {}", this.node.getNodeId(),
								zwaveDynamicClass.getCommandClass().getLabel());
						ZWaveCommandClassDynamicState zdds = (ZWaveCommandClassDynamicState) zwaveDynamicClass;
						int instances = zwaveDynamicClass.getInstances();
						if (instances == 0) {
							addCollectionToQueue(zdds.getDynamicValues(stageAdvanced));
						} else {
							for (int i = 1; i <= instances; i++) {
								addCollectionToQueue(zdds.getDynamicValues(stageAdvanced), zwaveDynamicClass, i);
							}
						}
					} else if (zwaveDynamicClass instanceof ZWaveMultiInstanceCommandClass) {
						ZWaveMultiInstanceCommandClass multiInstanceCommandClass = (ZWaveMultiInstanceCommandClass) zwaveDynamicClass;
						for (ZWaveEndpoint endpoint : multiInstanceCommandClass.getEndpoints()) {
							for (ZWaveCommandClass endpointCommandClass : endpoint.getCommandClasses()) {
								logger.trace("NODE {}: Inspecting command class {} for endpoint {}",
										this.node.getNodeId(), endpointCommandClass.getCommandClass().getLabel(),
										endpoint.getEndpointId());
								if (endpointCommandClass instanceof ZWaveCommandClassDynamicState) {
									logger.debug("NODE {}: Found dynamic state command class {}",
											this.node.getNodeId(), endpointCommandClass.getCommandClass().getLabel());
									ZWaveCommandClassDynamicState zdds2 = (ZWaveCommandClassDynamicState) endpointCommandClass;
									addCollectionToQueue(zdds2.getDynamicValues(stageAdvanced), endpointCommandClass,
											endpoint.getEndpointId());
								}
							}
						}
					}
				}
				break;

			case DONE:
				initializationComplete = true;
			case DEAD:
				nodeSerializer.SerializeNode(this.node);

				logger.debug("NODE {}: Node advancer - initialisation complete.", this.node.getNodeId());
				break;

			default:
				logger.error("NODE {}: Unknown node state {} encountered.", this.node.getNodeId(), this.node
						.getNodeStage().getLabel());
			}
			
			// The stageAdvanced flag is used to tell command classes that this is the first iteration.
			// During the first iteration all messages are queued. After this, only outstanding requests
			// are returned. This continues until there are no requests required.
			stageAdvanced = false;
			
			if(msgQueue.isEmpty()) {
				// Move on to the next stage
				currentStage = currentStage.getNextStage();
				stageAdvanced = true;
				logger.debug("NODE {}: Node advancer - advancing to {}.", this.node.getNodeId(), currentStage.getLabel());
			}
		}
		while (msgQueue.isEmpty());
	}

	/**
	 * Move all the messages in a collection to the queue
	 * @param msgs the message collection
	 */
	private void addCollectionToQueue(Collection<SerialMessage> msgs) {
		for (SerialMessage serialMessage : msgs) {
			this.msgQueue.add(serialMessage);
		}
	}

	/**
	 * Move all the messages in a collection to the queue and encapsulates them
	 * @param msgs the message collection
	 * @param the command class to encapsulate
	 * @param endpointId the endpoint number
	 */
	private void addCollectionToQueue(Collection<SerialMessage> msgs, ZWaveCommandClass commandClass, int endpointId) {
		for (SerialMessage serialMessage : msgs) {
			this.msgQueue.add(this.node.encapsulate(serialMessage, commandClass, endpointId));
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
	 * Restores a node from an XML file using the @ ZWaveNodeSerializer} class.
	 * 
	 * @return true if succeeded, false otherwise.
	 */
	public boolean restoreFromConfig() {
		ZWaveNode restoredNode = nodeSerializer.DeserializeNode(this.node.getNodeId());

		if (restoredNode == null)
			return false;

		// Sanity check the data from the file
		if (restoredNode.getVersion() != this.node.getVersion() || restoredNode.getManufacturer() == Integer.MAX_VALUE
				|| restoredNode.isListening() != this.node.isListening()
				|| restoredNode.isFrequentlyListening() != this.node.isFrequentlyListening()
				|| restoredNode.isRouting() != this.node.isRouting()
				|| !restoredNode.getDeviceClass().equals(this.node.getDeviceClass())) {
			logger.warn("NODE {}: Config file differs from controller information, ignoring config.",
					this.node.getNodeId());
			return false;
		}

		this.node.setDeviceId(restoredNode.getDeviceId());
		this.node.setDeviceType(restoredNode.getDeviceType());
		this.node.setManufacturer(restoredNode.getManufacturer());

		this.node.setHealState(restoredNode.getHealState());

		for (ZWaveCommandClass commandClass : restoredNode.getCommandClasses()) {
			commandClass.setController(this.controller);
			commandClass.setNode(this.node);

			if (commandClass instanceof ZWaveMultiInstanceCommandClass) {
				for (ZWaveEndpoint endPoint : ((ZWaveMultiInstanceCommandClass) commandClass).getEndpoints()) {
					for (ZWaveCommandClass endpointCommandClass : endPoint.getCommandClasses()) {
						endpointCommandClass.setController(this.controller);
						endpointCommandClass.setNode(this.node);
						endpointCommandClass.setEndpoint(endPoint);
					}
				}
			}

			this.node.addCommandClass(commandClass);
		}

		logger.debug("NODE {}: Restored from config.", this.node.getNodeId());
		restoredFromConfigfile = true;
		return true;
	}
}
