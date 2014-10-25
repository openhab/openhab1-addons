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
	 * Advances the initialization stage for this node. Every node follows a
	 * certain path through it's initialization phase. These stages are visited
	 * one by one to finally end up with a completely built node structure
	 * through querying the controller / node. TODO: Handle the rest of the node
	 * stages
	 */
	public void advanceNodeStage(SerialMessage incomingMessage) {
		// If initialisation is complete, then just return.
		if (this.initializationComplete) {
			return;
		}

		// If this message is in the queue, then remove it
		// When the queue is empty, we can move on to the next stage.
		if (this.msgQueue.contains(incomingMessage)) {
			logger.debug("NODE {}: Message in initialisation queue.", this.node.getNodeId());
			this.msgQueue.remove(incomingMessage);
		}

		// We run through all stages until one sends a message.
		// Then we will wait for the response before continuing
		while (msgQueue.isEmpty()) {
			// Move on to the next stage
			currentStage = currentStage.getNextStage();
			logger.debug("NODE {}: Advancing initialisation to {}.", this.node.getNodeId(), currentStage.getLabel());

			// Remember the time so we can handle retries
			this.node.setQueryStageTimeStamp(Calendar.getInstance().getTime());

			// A SerialMessage for queuing!
			SerialMessage msg;

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
				this.controller.sendData(msg);
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
				this.controller.enqueue(msg);
				break;

			case DETAILS:
				// try and get the manufacturerSpecific command class.
				ZWaveManufacturerSpecificCommandClass manufacturerSpecific = (ZWaveManufacturerSpecificCommandClass) this.node
						.getCommandClass(CommandClass.MANUFACTURER_SPECIFIC);

				if (manufacturerSpecific != null) {
					// If this node implements the Manufacturer Specific command
					// class, we use it to get manufacturer info.
					msg = manufacturerSpecific.getManufacturerSpecificMessage();
					this.msgQueue.add(msg);
					this.controller.sendData(msg);
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
						this.controller.sendData(msg);
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
					msg = multiInstance.initEndpoints();
					this.msgQueue.add(msg);
					this.controller.sendData(msg);
				}

				// WARNING: Currently, the multi_instance class will send
				// multiple
				// messages when the first request is received
				// This should be handled here so we can monitor its progress.
				break;

			case INSTANCES_ENDPOINTS:
				// This is in the command class at the moment
				// for (int i=1; i <= endpoints; i++) {
				// if (!endpointsAreTheSameDeviceClass || i == 1)
				// this.getController().sendData(this.getMultiChannelCapabilityGetMessage(endpoint));
				// }

				// this.node.setNodeStage(NodeStage.STATIC_VALUES);
				// Manually increment the stage and fall through
				this.currentStage = NodeStage.STATIC_VALUES;
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
							Collection<SerialMessage> initqueries = zcci.initialize();
							for (SerialMessage serialMessage : initqueries) {
								this.msgQueue.add(serialMessage);
								this.controller.sendData(serialMessage);
							}
						} else {
							for (int i = 1; i <= instances; i++) {
								Collection<SerialMessage> initqueries = zcci.initialize();
								for (SerialMessage serialMessage : initqueries) {
									msg = this.node.encapsulate(serialMessage, zwaveStaticClass, i);
									this.msgQueue.add(msg);
									this.controller.sendData(msg);
								}
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
									Collection<SerialMessage> initqueries = zcci2.initialize();
									for (SerialMessage serialMessage : initqueries) {
										msg = this.node.encapsulate(serialMessage, endpointCommandClass,
												endpoint.getEndpointId());
										this.msgQueue.add(msg);
										this.controller.sendData(msg);
									}
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
							Collection<SerialMessage> dynamicQueries = zdds.getDynamicValues();
							for (SerialMessage serialMessage : dynamicQueries) {
								this.msgQueue.add(serialMessage);
								this.controller.sendData(serialMessage);
							}
						} else {
							for (int i = 1; i <= instances; i++) {
								Collection<SerialMessage> dynamicQueries = zdds.getDynamicValues();
								for (SerialMessage serialMessage : dynamicQueries) {
									msg = this.node.encapsulate(serialMessage, zwaveDynamicClass, i);
									this.msgQueue.add(msg);
									this.controller.sendData(msg);
								}
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
									Collection<SerialMessage> dynamicQueries = zdds2.getDynamicValues();
									for (SerialMessage serialMessage : dynamicQueries) {
										msg = this.node.encapsulate(serialMessage, endpointCommandClass,
												endpoint.getEndpointId());
										this.msgQueue.add(msg);
										this.controller.sendData(msg);
									}
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

				logger.debug("NODE {}: Initialisation complete.", this.node.getNodeId());
				break;

			default:
				logger.error("NODE {}: Unknown node state {} encountered.", this.node.getNodeId(), this.node
						.getNodeStage().getLabel());
			}
		}
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
