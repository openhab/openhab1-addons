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
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveWakeUpCommandClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZWaveNodeStageAdvancer class. Advances the node stage, thereby controlling
 * the initialization of a node.
 * 
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class ZWaveNodeStageAdvancer {

	private static final ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
	private static final Logger logger = LoggerFactory.getLogger(ZWaveNodeStageAdvancer.class);

	private ZWaveNode node;
	private ZWaveController controller;
	private int queriesPending = -1;
	private boolean initializationComplete = false;
	private boolean restoredFromConfigfile = false;

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
	}

	/**
	 * Advances the initialization stage for this node. Every node follows a
	 * certain path through it's initialization phase. These stages are visited
	 * one by one to finally end up with a completely built node structure
	 * through querying the controller / node. TODO: Handle the rest of the node
	 * stages
	 */
	public void advanceNodeStage(NodeStage targetStage) {
		if (targetStage.getStage() <= this.node.getNodeStage().getStage() && targetStage != NodeStage.DONE) {
			logger.warn("Already in or beyond node stage, ignoring. current = {}, requested = {}", this.node
					.getNodeStage().getLabel(), targetStage.getLabel());
			return;
		}

		this.node.setQueryStageTimeStamp(Calendar.getInstance().getTime());
		switch (this.node.getNodeStage()) {
		case EMPTYNODE:
			try {
				this.node.setNodeStage(NodeStage.PROTOINFO);
				this.controller.identifyNode(this.node.getNodeId());
			} catch (SerialInterfaceException e) {
				logger.error("Got error: {}, while identifying node {}", e.getLocalizedMessage(), this.node.getNodeId());
			}
			break;
		case PROTOINFO:
			if (this.node.getNodeId() != this.controller.getOwnNodeId()) {
				ZWaveNoOperationCommandClass zwaveCommandClass = (ZWaveNoOperationCommandClass) this.node
						.getCommandClass(CommandClass.NO_OPERATION);
				if (zwaveCommandClass == null)
					break;

				this.node.setNodeStage(NodeStage.PING);
				this.controller.sendData(zwaveCommandClass.getNoOperationMessage());
			} else {
				this.node.setNodeStage(NodeStage.DONE); // nothing
														// more
														// to
														// do
														// for
														// this
														// node.
			}
			break;
		case PING:
		case WAKEUP:
			// if restored from a config file, redo from the dynamic
			// node stage.
			if (this.isRestoredFromConfigfile()) {
				this.node.setNodeStage(NodeStage.DYNAMIC);				
				advanceNodeStage(NodeStage.DONE);
				break;
			}
			this.node.setNodeStage(NodeStage.DETAILS);
			this.controller.requestNodeInfo(this.node.getNodeId());
			break;
		case DETAILS:
			// try and get the manufacturerSpecific command class.
			ZWaveManufacturerSpecificCommandClass manufacturerSpecific = (ZWaveManufacturerSpecificCommandClass) this.node
					.getCommandClass(CommandClass.MANUFACTURER_SPECIFIC);

			if (manufacturerSpecific != null) {
				// if this node implements the Manufacturer Specific command
				// class, we use it to get manufacturer info.
				this.node.setNodeStage(NodeStage.MANSPEC01);
				this.controller.sendData(manufacturerSpecific.getManufacturerSpecificMessage());
				break;
			}

			logger.warn("Node {} does not support MANUFACTURER_SPECIFIC, proceeding to version node stage.",
					this.node.getNodeId());
		case MANSPEC01:
			this.node.setNodeStage(NodeStage.VERSION); // nothing
														// more
														// to
														// do
														// for
														// this
														// node.
			// try and get the version command class.
			ZWaveVersionCommandClass version = (ZWaveVersionCommandClass) this.node
					.getCommandClass(CommandClass.VERSION);

			boolean checkVersionCalled = false;
			for (ZWaveCommandClass zwaveCommandClass : this.node.getCommandClasses()) {
				if (version != null && zwaveCommandClass.getMaxVersion() > 1) {
					version.checkVersion(zwaveCommandClass); // check version
																// for this
																// command
																// class.
					checkVersionCalled = true;
				} else
					zwaveCommandClass.setVersion(1);
			}

			if (checkVersionCalled) // wait for another call of advanceNodeStage
									// before continuing.
				break;
		case VERSION:
			this.node.setNodeStage(NodeStage.INSTANCES_ENDPOINTS); // nothing
																	// more
																	// to
																	// do
																	// for
																	// this
																	// node.
			// try and get the multi instance / channel command class.
			ZWaveMultiInstanceCommandClass multiInstance = (ZWaveMultiInstanceCommandClass) this.node
					.getCommandClass(CommandClass.MULTI_INSTANCE);

			if (multiInstance != null) {
				multiInstance.initEndpoints();
				break;
			}

			logger.trace("Node {} does not support MULTI_INSTANCE, proceeding to static node stage.",
					this.node.getNodeId());
		case INSTANCES_ENDPOINTS:
			this.node.setNodeStage(NodeStage.STATIC_VALUES);
		case STATIC_VALUES:
			if (queriesPending == -1) {
				queriesPending = 0;
				for (ZWaveCommandClass zwaveCommandClass : this.node.getCommandClasses()) {
					logger.trace("Inspecting command class {}", zwaveCommandClass.getCommandClass().getLabel());
					if (zwaveCommandClass instanceof ZWaveCommandClassInitialization) {
						logger.debug("Found initializable command class {}", zwaveCommandClass.getCommandClass()
								.getLabel());
						ZWaveCommandClassInitialization zcci = (ZWaveCommandClassInitialization) zwaveCommandClass;
						int instances = zwaveCommandClass.getInstances();
						if (instances == 0) {
							Collection<SerialMessage> initqueries = zcci.initialize();
							for (SerialMessage serialMessage : initqueries) {
								this.controller.sendData(serialMessage);
								queriesPending++;
							}
						} else {
							for (int i = 1; i <= instances; i++) {
								Collection<SerialMessage> initqueries = zcci.initialize();
								for (SerialMessage serialMessage : initqueries) {
									this.controller
											.sendData(this.node.encapsulate(serialMessage, zwaveCommandClass, i));
									queriesPending++;
								}
							}
						}
					} else if (zwaveCommandClass instanceof ZWaveMultiInstanceCommandClass) {
						ZWaveMultiInstanceCommandClass multiInstanceCommandClass = (ZWaveMultiInstanceCommandClass) zwaveCommandClass;
						for (ZWaveEndpoint endpoint : multiInstanceCommandClass.getEndpoints()) {
							for (ZWaveCommandClass endpointCommandClass : endpoint.getCommandClasses()) {
								logger.trace("Inspecting command class {} for endpoint {}", endpointCommandClass
										.getCommandClass().getLabel(), endpoint.getEndpointId());
								if (endpointCommandClass instanceof ZWaveCommandClassInitialization) {
									logger.debug("Found initializable command class {}", endpointCommandClass
											.getCommandClass().getLabel());
									ZWaveCommandClassInitialization zcci2 = (ZWaveCommandClassInitialization) endpointCommandClass;
									Collection<SerialMessage> initqueries = zcci2.initialize();
									for (SerialMessage serialMessage : initqueries) {
										this.controller.sendData(this.node.encapsulate(serialMessage,
												endpointCommandClass, endpoint.getEndpointId()));
										queriesPending++;
									}
								}
							}
						}
					}
				}
			}
			if (queriesPending-- > 0) // there is still something to be
										// initialized.
				break;

			logger.trace("Done getting static values, proceeding to dynamic node stage.", this.node.getNodeId());
			queriesPending = -1;
			this.node.setNodeStage(NodeStage.DYNAMIC);
		case DYNAMIC:
			if (queriesPending == -1) {
				queriesPending = 0;
				for (ZWaveCommandClass zwaveCommandClass : this.node.getCommandClasses()) {
					logger.trace("Inspecting command class {}", zwaveCommandClass.getCommandClass().getLabel());
					if (zwaveCommandClass instanceof ZWaveCommandClassDynamicState) {
						logger.debug("Found dynamic state command class {}", zwaveCommandClass.getCommandClass()
								.getLabel());
						ZWaveCommandClassDynamicState zdds = (ZWaveCommandClassDynamicState) zwaveCommandClass;
						int instances = zwaveCommandClass.getInstances();
						if (instances == 0) {
							Collection<SerialMessage> dynamicQueries = zdds.getDynamicValues();
							for (SerialMessage serialMessage : dynamicQueries) {
								this.controller.sendData(serialMessage);
								queriesPending++;
							}
						} else {
							for (int i = 1; i <= instances; i++) {
								Collection<SerialMessage> dynamicQueries = zdds.getDynamicValues();
								for (SerialMessage serialMessage : dynamicQueries) {
									this.controller
											.sendData(this.node.encapsulate(serialMessage, zwaveCommandClass, i));
									queriesPending++;
								}
							}
						}
					} else if (zwaveCommandClass instanceof ZWaveMultiInstanceCommandClass) {
						ZWaveMultiInstanceCommandClass multiInstanceCommandClass = (ZWaveMultiInstanceCommandClass) zwaveCommandClass;
						for (ZWaveEndpoint endpoint : multiInstanceCommandClass.getEndpoints()) {
							for (ZWaveCommandClass endpointCommandClass : endpoint.getCommandClasses()) {
								logger.trace("Inspecting command class {} for endpoint {}", endpointCommandClass
										.getCommandClass().getLabel(), endpoint.getEndpointId());
								if (endpointCommandClass instanceof ZWaveCommandClassDynamicState) {
									logger.debug("Found dynamic state command class {}", endpointCommandClass
											.getCommandClass().getLabel());
									ZWaveCommandClassDynamicState zdds2 = (ZWaveCommandClassDynamicState) endpointCommandClass;
									Collection<SerialMessage> dynamicQueries = zdds2.getDynamicValues();
									for (SerialMessage serialMessage : dynamicQueries) {
										this.controller.sendData(this.node.encapsulate(serialMessage,
												endpointCommandClass, endpoint.getEndpointId()));
										queriesPending++;
									}
								}
							}
						}
					}
				}
			}
			if (queriesPending-- > 0) // there is still something to be
										// initialized.
				break;
			logger.trace("Done getting dynamic values, proceeding to done node stage.", this.node.getNodeId());
			queriesPending = -1;

			this.node.setNodeStage(NodeStage.DONE); // nothing
													// more
													// to
													// do
													// for
													// this
													// node.

			nodeSerializer.SerializeNode(this.node);

			initializationComplete = true;

			if (this.node.isListening() || this.node.isFrequentlyListening())
				return;

			ZWaveWakeUpCommandClass wakeup = (ZWaveWakeUpCommandClass) this.node.getCommandClass(CommandClass.WAKE_UP);

			if (wakeup == null)
				return;

			logger.debug("Node {} is a battery operated device. Tell it to go to sleep.", this.node.getNodeId());
			this.controller.sendData(wakeup.getNoMoreInformationMessage());
			break;
		case DONE:
		case DEAD:
			break;
		default:
			logger.error("Unknown node state {} encountered on Node {}", this.node.getNodeStage().getLabel(),
					this.node.getNodeId());
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

		if (restoredNode.getVersion() != this.node.getVersion()
				|| restoredNode.isListening() != this.node.isListening()
				|| restoredNode.isFrequentlyListening() != this.node.isFrequentlyListening()
				|| restoredNode.isRouting() != this.node.isRouting()
				|| !restoredNode.getDeviceClass().equals(this.node.getDeviceClass())) {
			logger.warn("Config file differs from controler information for node {}, ignoring config.",
					this.node.getNodeId());
			return false;
		}
		
		this.node.setDeviceId(restoredNode.getDeviceId());
		this.node.setDeviceType(restoredNode.getDeviceType());
		this.node.setManufacturer(restoredNode.getManufacturer());

		for (ZWaveCommandClass commandClass : restoredNode.getCommandClasses()) {
			commandClass.setController(this.controller);
			commandClass.setNode(this.node);
			
			if (commandClass instanceof ZWaveMultiInstanceCommandClass) {
				for (ZWaveEndpoint endPoint : ((ZWaveMultiInstanceCommandClass)commandClass).getEndpoints()) {
					for (ZWaveCommandClass endpointCommandClass : endPoint.getCommandClasses()) {
						endpointCommandClass.setController(this.controller);
						endpointCommandClass.setNode(this.node);
						endpointCommandClass.setEndpoint(endPoint);
					}
				}
			}
			
			this.node.addCommandClass(commandClass);
		}

		logger.debug("Restored node {} from config.", this.node.getNodeId());
		restoredFromConfigfile = true;
		return true;
	}
}
