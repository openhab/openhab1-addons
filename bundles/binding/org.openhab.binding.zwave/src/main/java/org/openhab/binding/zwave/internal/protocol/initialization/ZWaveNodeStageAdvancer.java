/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import org.openhab.binding.zwave.internal.config.ZWaveDbAssociationGroup;
import org.openhab.binding.zwave.internal.config.ZWaveDbCommandClass;
import org.openhab.binding.zwave.internal.config.ZWaveDbConfigurationParameter;
import org.openhab.binding.zwave.internal.config.ZWaveProductDatabase;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveDeviceClass.Specific;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveEventListener;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveVersionCommandClass.LibraryType;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveAssociationCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClassDynamicState;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClassInitialization;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveConfigurationCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveManufacturerSpecificCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveMultiInstanceCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveNoOperationCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveVersionCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveWakeUpCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveInitializationCompletedEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNodeInfoEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNodeStatusEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveTransactionCompletedEvent;
import org.openhab.binding.zwave.internal.protocol.serialmessage.GetRoutingInfoMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.IdentifyNodeMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.IsFailedNodeMessageClass;
import org.openhab.binding.zwave.internal.protocol.serialmessage.RequestNodeInfoMessageClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ZWaveNodeStageAdvancer class. Advances the node stage, thereby controlling
 * the initialization of a node.
 * 
 * Node initialisation is handled solely within the NodeStageAdvancer. It is not
 * based on time - it waits for the transactions to complete. Time cannot be
 * used since with larger networks, it may take a long time for the
 * initialisation. This is especially true if there are battery nodes since the
 * PING phase, used to detect if a node is active, will time-out for battery
 * devices. A timeout takes 5 seconds, and if there are retries active, this may
 * be extended to 15 seconds. For a network with 8 battery nodes, this could
 * mean a delay of 2 minutes!
 * 
 * We use the 'listening' flag to prioritise the initialisation of nodes. Rather
 * than kicking off all nodes at the same time and have battery nodes timing out
 * and delaying the initialisation of mains nodes, we try and initialise nodes
 * that are listening first. This is checked after the protocol information
 * is received, and non-listening nodes are held at a WAIT state until the
 * transmit queue drops below 2 frames when they are allowed to proceed to PING.
 * 
 * The NodeStageAdvancer registers an event listener during the initialisation
 * of a node. This allows it to be notified when each transaction is complete,
 * and we can process this accordingly. The event listener is removed when we
 * stop initialising to reduce processor loading.
 * 
 * Command classes are responsible for building lists of messages needed to
 * initialise themselves. The command class also needs to keep track of
 * responses so it knows if initialisation of this stage is complete. Other than
 * that, the command class does not have any input into the initialisation
 * phase, and the sequencing of events - this is all handled here in the node
 * advancer class.
 * 
 * For each stage, the advancer builds a list of all messages that need to be
 * sent to the node. Since the initialisation phase is an intense period, with a
 * lot of messages on the network, we try and ensure that only 1 packet is
 * outstanding to any node at once to avoid filling up the main transmit queue
 * which could impact on the performance of other nodes.
 * 
 * Each time we receive an ACK for a message, the node advancer gets called, and
 * we see if this is an ACK for a message that's part of the initialisation. If
 * it is, the message gets removed from the list.
 * 
 * Each time we receive a command message, the node advancer gets called. This
 * is called after the command class has been updated, so at this stage we know
 * if the stage can be completed.
 * 
 * Two checks are performed to allow a node stage to advance. Firstly, we make
 * sure we've sent all the messages required for this phase. Sending the
 * messages however doesn't guarantee that we get a response, so we then run
 * through the stage again to make sure that the command class really is
 * initialised. If the second run queues no messages, then we can reliably
 * assume this stage is completed. If we've missed anything, then we continue
 * until there are no messages to send.
 * 
 * If a node is DEAD (or FAILED) then we still try to initialise. No HEAL is
 * performed on initialising nodes, so we need to do enough here to find out
 * if the node comes back to life.
 * A 'is node failed' check is performed at the beginning of the init process.
 * This asks the controller if it thinks the node is dead - if it is, then we
 * treat the node as dead until it comes back to life.
 * A DEAD node will use a backoff to reduce the traffic. We start sending data
 * reasonably quickly, but if it fails, then we reduce the retry timer by a
 * factor of 2 until BACKOFF_TIMER_MAX is reached.
 * << NOTE THAT THE ABOVE BACKOFF ISN'T IMPLEMENTED YET >>
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
	private boolean restoredFromConfigfile = false;

	private static final int MAX_BUFFFER_LEN = 256;
	private ArrayBlockingQueue<SerialMessage> msgQueue;
	private boolean freeToSend = true;
	private boolean stageAdvanced = true;
	
	private static final int MAX_RETRIES = 5;
	private int retryCount = 0;

	private final int BACKOFF_TIMER_START = 5000;
	private final int BACKOFF_TIMER_MAX = 1800000;			// 30 minutes max backoff
	private int retryTimer;
	private TimerTask timerTask = null;
	private Timer timer = null;
	
	private int wakeupCount;

	private Date queryStageTimeStamp;
	private ZWaveNodeInitStage currentStage;

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

		// Initialise the message queue
		msgQueue = new ArrayBlockingQueue<SerialMessage>(MAX_BUFFFER_LEN, true);
	}

	/**
	 * Starts the initialisation from the beginning.
	 */
	public void startInitialisation() {
		// Reset the state variables
		currentStage = ZWaveNodeInitStage.EMPTYNODE;
		queryStageTimeStamp = Calendar.getInstance().getTime();
		retryTimer = BACKOFF_TIMER_START;

		// Create the timer and timer task
		timer = new Timer();

		wakeupCount = 0;

		// Set an event callback so we get notification of events
		controller.addEventListener(this);

		// Get things moving...
		advanceNodeStage(null);
	}

	/**
	 * Handles the removal of frames from the send queue. This gets called after
	 * we have an ACK for our packet, but before we get the response. The actual
	 * sending of frames, and the advancing is carried out in the
	 * advanceNodeStage method.
	 */
	public void handleNodeQueue(SerialMessage incomingMessage) {
		// If initialisation is complete, then just return.
		// This probably shouldn't be necessary since we remove the event
		// handler when we're done, but just to be sure...
		if (currentStage == ZWaveNodeInitStage.DONE) {
			return;
		}

		logger.debug("NODE {}: Node advancer - checking initialisation queue. Queue size {}.",
				node.getNodeId(), msgQueue.size());

		// If this message is in the queue, then remove it
		if (msgQueue.contains(incomingMessage)) {
			msgQueue.remove(incomingMessage);
			logger.debug("NODE {}: Node advancer - message removed from queue. Queue size {}.",
					node.getNodeId(), msgQueue.size());

			freeToSend = true;

			// We've sent a frame, let's process the stage...
			advanceNodeStage(incomingMessage.getMessageClass());
		}
	}

	/**
	 * Sends a message if there is one queued
	 * 
	 * @return true if a message was sent. false otherwise.
	 */
	private boolean sendMessage() {
		if (msgQueue.isEmpty() == true) {
			return false;
		}

		// Check to see if we need to send a frame
		if (freeToSend == true) {
			SerialMessage msg = msgQueue.peek();
			if (msg != null) {
				freeToSend = false;

				if (msg.getMessageClass() == SerialMessageClass.SendData) {
					controller.sendData(msg);
				}
				else {
					controller.enqueue(msg);
				}

				logger.debug("NODE {}: Node advancer - queued packet. Queue length is {}", node.getNodeId(),
						msgQueue.size());
			}
		}

		return true;
	}

	/**
	 * Advances the initialization stage for this node. This method is called
	 * after a response is received. We don't necessarily know if the response
	 * is to the frame we requested though, so to be sure the initialisation
	 * gets all the information it needs, the command class itself gets queried.
	 * This method also handles the sending of frames. Since the initialisation
	 * phase is a busy one we try and only have one outstanding request. Again
	 * though, we can't be sure that a response is aligned with the node
	 * advancer request so it is possible that more than one packet can be
	 * released at once, but it will constrain things.
	 */
	public void advanceNodeStage(SerialMessageClass eventClass) {
		// If initialisation is complete, then just return.
		// This probably shouldn't be necessary since we remove the event
		// handler when we're done, but just to be sure...
		if (currentStage == ZWaveNodeInitStage.DONE) {
			return;
		}

		logger.debug("NODE {}: Node advancer - {}: queue length({}), free to send({})", node.getNodeId(),
				currentStage.toString(), msgQueue.size(), freeToSend);

		// If this is a battery node, and we exceed the wakeup count without advancing
		// the stage, then reset.
		if(wakeupCount >= 3) {
			msgQueue.clear();
			wakeupCount = 0;
		}

		// Start the retry timer
		startIdleTimer();

		// If event class is null, then this call isn't the result of an
		// incoming frame.
		// It could be a wakeup, or the node is now alive. Get things moving
		// again.
		if (eventClass == null) {
			freeToSend = true;
		}

		// If the queue is not empty, then we can't advance any further.
		if (sendMessage() == true) {
			// We're still sending messages, so we're not ready to proceed.
			return;
		}

		// The stageAdvanced flag is used to tell command classes that this
		// is the first iteration.
		// During the first iteration all messages are queued. After this,
		// only outstanding requests are returned.
		// This continues until there are no requests required.
		stageAdvanced = false;

		ZWaveProductDatabase database;

		// We run through all stages until one queues a message.
		// Then we will wait for the response before continuing
		do {
			// Ensure we don't get stuck in an endless loop trying to initialise
			// something that is broken, or not responding to a particular request
			if(stageAdvanced == true) {
				retryCount = 0;
			} else {
				retryCount++;
				if(retryCount > MAX_RETRIES) {
					retryCount = 0;
					logger.error("NODE {}: Node advancer: Retries exceeded at {}", 
							node.getNodeId(), currentStage.toString());
					if(currentStage.isStageMandatory() == false) {
						// If the current stage is not mandatory, then we skip forward to the next
						// stage.
						logger.debug("NODE {}: Retry timout: Advancing", node.getNodeId());
						setCurrentStage(currentStage.getNextStage());
					}
					else {
						// For static stages, we MUST complete all steps otherwise we end
						// up with incomplete information about the device.
						// During the static stages, we use the back off timer to pace things
						// and retry until the stage is complete
						logger.debug("NODE {}: Retry timout: Can't advance", node.getNodeId());
						break;
					}
				}
			}

			logger.debug("NODE {}: Node advancer: loop - {} try {}: stageAdvanced({})", node.getNodeId(),
					currentStage.toString(), retryCount, stageAdvanced);

			switch (currentStage) {
			case EMPTYNODE:
				logger.debug("NODE {}: Node advancer: Initialisation starting", node.getNodeId());
				break;

			case PROTOINFO:
				// If the incoming frame is the IdentifyNode, then we continue
				if (eventClass == SerialMessageClass.IdentifyNode) {
					break;
				}

				logger.debug("NODE {}: Node advancer: PROTOINFO - send IdentifyNode", node.getNodeId());
				addToQueue(new IdentifyNodeMessageClass().doRequest(node.getNodeId()));
				break;

			case NEIGHBORS:
				// If the incoming frame is the IdentifyNode, then we continue
				if (eventClass == SerialMessageClass.GetRoutingInfo) {
					break;
				}

				logger.debug("NODE {}: Node advancer: NEIGHBORS - send RoutingInfo", node.getNodeId());
				addToQueue(new GetRoutingInfoMessageClass().doRequest(node.getNodeId()));
				break;

			case FAILED_CHECK:
				// It seems that PC_CONTROLLERs don't respond to a lot of requests, so let's
				// just assume their OK!
				// If this is a controller, we're done
				if (node.getDeviceClass().getSpecificDeviceClass() == Specific.PC_CONTROLLER) {
					logger.debug("NODE {}: Node advancer: FAILED_CHECK - Controller - terminating initialisation", node.getNodeId());
					currentStage = ZWaveNodeInitStage.DONE;
					break;
				}

				// If the incoming frame is the IdentifyNode, then we continue
				if (eventClass == SerialMessageClass.IsFailedNodeID) {
					break;
				}

				addToQueue(new IsFailedNodeMessageClass().doRequest(node.getNodeId()));
				break;

			case WAIT:
				logger.debug("NODE {}: Node advancer: WAIT - Listening={}, FrequentlyListening={}", node.getNodeId(),
						node.isListening(), node.isFrequentlyListening());
				// If the node is listening, or frequently listening, then we progress.
				if(node.isListening() == true || node.isFrequentlyListening() == true) {
					logger.debug("NODE {}: Node advancer: WAIT - Advancing", node.getNodeId());
					break;
				}

				// If the device supports the wakeup class, then see if we're awake
				ZWaveWakeUpCommandClass wakeUpCommandClass = (ZWaveWakeUpCommandClass) node
						.getCommandClass(CommandClass.WAKE_UP);
				if (wakeUpCommandClass != null && wakeUpCommandClass.isAwake() == true) {
					logger.debug("NODE {}: Node advancer: WAIT - Node is awake", node.getNodeId());
					break;
				}

				// If it's not listening, and not awake,
				// we'll wait a while before progressing with initialisation.
				logger.debug("NODE {}: Node advancer: WAIT - Still waiting!", node.getNodeId());
				return;

			case PING:
				// Completion of this stage is reception of a SendData frame.
				// The purpose of this stage is to ensure that the node is awake
				// before requesting further information.
				// It's not 100% guaranteed that this was our NoOp frame, but
				// who cares!
				if (eventClass == SerialMessageClass.SendData) {
					break;
				}

				ZWaveNoOperationCommandClass noOpCommandClass = (ZWaveNoOperationCommandClass) node
						.getCommandClass(CommandClass.NO_OPERATION);
				if (noOpCommandClass == null) {
					break;
				}

				logger.debug("NODE {}: Node advancer: PING - send NoOperation", node.getNodeId());
				SerialMessage msg = noOpCommandClass.getNoOperationMessage();
				if (msg != null) {
					// We only send out a single PING - no retries at controller
					// level! This is to try and reduce network congestion during
					// initialisation.
					// For battery devices, the PING will time-out. This takes 5
					// seconds and if there are retries, it will be 15 seconds! 
					// This will block the network for a considerable time if there
					// are a lot of battery devices (eg. 2 minutes for 8 battery devices!).
					msg.attempts = 1;
					addToQueue(msg);
				}
				break;

			case DETAILS:
				// If restored from a config file, redo from the dynamic node stage.
				if (isRestoredFromConfigfile()) {
					logger.debug("NODE {}: Node advancer: Restored from file - skipping static initialisation", node.getNodeId());
					currentStage = ZWaveNodeInitStage.SESSION_START;
					break;
				}

				// If the incoming frame is the IdentifyNode, then we continue
				if (node.getApplicationUpdateReceived() == true) {
					logger.debug("NODE {}: Node advancer: received RequestNodeInfo", node.getNodeId());
					break;
				}

				logger.debug("NODE {}: Node advancer: DETAILS - send RequestNodeInfo", node.getNodeId());
				addToQueue(new RequestNodeInfoMessageClass().doRequest(node.getNodeId()));
				break;

			case MANUFACTURER:
				// If we already know the device information, then continue
				if (node.getManufacturer() != Integer.MAX_VALUE && node.getDeviceType() != Integer.MAX_VALUE
						&& node.getDeviceId() != Integer.MAX_VALUE) {
					break;
				}

				// try and get the manufacturerSpecific command class.
				ZWaveManufacturerSpecificCommandClass manufacturerSpecific = (ZWaveManufacturerSpecificCommandClass) node
						.getCommandClass(CommandClass.MANUFACTURER_SPECIFIC);

				if (manufacturerSpecific != null) {
					// If this node implements the Manufacturer Specific command
					// class, we use it to get manufacturer info.
					logger.debug("NODE {}: Node advancer: MANUFACTURER - send ManufacturerSpecific", node.getNodeId());
					addToQueue(manufacturerSpecific.getManufacturerSpecificMessage());
				}
				break;

			case VERSION:
				// Try and get the version command class.
				ZWaveVersionCommandClass version = (ZWaveVersionCommandClass) node
						.getCommandClass(CommandClass.VERSION);

				// Loop through all command classes, requesting their version
				// using the Version command class
				for (ZWaveCommandClass zwaveVersionClass : node.getCommandClasses()) {
					logger.debug("NODE {}: Node advancer: VERSION - checking {}, version is {}", node.getNodeId(),
							zwaveVersionClass.getCommandClass().getLabel(), zwaveVersionClass.getVersion());
					if (version != null && zwaveVersionClass.getMaxVersion() > 1 && zwaveVersionClass.getVersion() == 0) {
						logger.debug("NODE {}: Node advancer: VERSION - queued   {}", node.getNodeId(), zwaveVersionClass
								.getCommandClass().getLabel());
						addToQueue(version.checkVersion(zwaveVersionClass));
					}
					else if (zwaveVersionClass.getVersion() == 0) {
						logger.debug("NODE {}: Node advancer: VERSION - VERSION default to 1", node.getNodeId());
						zwaveVersionClass.setVersion(1);
					}
				}
				logger.debug("NODE {}: Node advancer: VERSION - queued {} frames", node.getNodeId(), msgQueue.size());
				break;

			case APP_VERSION:
				ZWaveVersionCommandClass versionCommandClass = (ZWaveVersionCommandClass) node
						.getCommandClass(CommandClass.VERSION);

				if (versionCommandClass == null) {
					logger.debug("NODE {}: Node advancer: APP_VERSION - VERSION node supported", node.getNodeId());
					break;
				}

				// If we know the library type, then we've got the app version
				if (versionCommandClass.getLibraryType() != LibraryType.LIB_UNKNOWN) {
					break;
				}

				// Request the version report for this node
				logger.debug("NODE {}: Node advancer: APP_VERSION - send VersionMessage", node.getNodeId());
				addToQueue(versionCommandClass.getVersionMessage());
				break;

			case ENDPOINTS:
				// Try and get the multi instance / channel command class.
				ZWaveMultiInstanceCommandClass multiInstance = (ZWaveMultiInstanceCommandClass) node
						.getCommandClass(CommandClass.MULTI_INSTANCE);

				if (multiInstance != null) {
					logger.debug("NODE {}: Node advancer: ENDPOINTS - MultiInstance is supported", node.getNodeId());
					addToQueue(multiInstance.initEndpoints(stageAdvanced));
					logger.debug("NODE {}: Node advancer: ENDPOINTS - queued {} frames", node.getNodeId(), msgQueue.size());
				}
				else {
					logger.debug("NODE {}: Node advancer: ENDPOINTS - MultiInstance not supported.", node.getNodeId());
					// Set all classes to 1 instance.
					for (ZWaveCommandClass commandClass : node.getCommandClasses()) {
						commandClass.setInstances(1);
					}
				}
				break;

			case UPDATE_DATABASE:
				// This stage reads information from the database to allow us to modify the configuration
				logger.debug("NODE {}: Node advancer: UPDATE_DATABASE", node.getNodeId());

				// We now should know all the command classes, so run through the database and set any options
				database = new ZWaveProductDatabase();
				if(database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId(), node.getApplicationVersion()) == true) {
					List<ZWaveDbCommandClass> classList = database.getProductCommandClasses();

					if (classList != null) {
						// Loop through the command classes and update the records...
						for (ZWaveDbCommandClass dbClass : classList) {
							// If we want to remove the class, then remove it!
							if(dbClass.remove != null && dbClass.remove == true) {
								// TODO: This will only remove the root nodes and ignores endpoint
								// TODO: Do we need to search into multi_instance?
								node.removeCommandClass(CommandClass.getCommandClass(dbClass.Id));
								logger.debug("NODE {}: Node advancer: UPDATE_DATABASE - removing {}",
										node.getNodeId(), CommandClass.getCommandClass(dbClass.Id).getLabel());
								continue;
							}

							// Get the command class
							int endpoint =  dbClass.endpoint == null ? 0 : dbClass.endpoint;
							ZWaveCommandClass zwaveClass = node.resolveCommandClass(CommandClass.getCommandClass(dbClass.Id), endpoint);

							// If we found the command class, then set its options
							if(zwaveClass != null) {
								zwaveClass.setOptions(dbClass);
								continue;
							}

							// Command class isn't found! Do we want to add it?
							// TODO: Does this need to account for multiple endpoints!?!
							if(dbClass.add != null && dbClass.add == true) {
								ZWaveCommandClass commandClass = ZWaveCommandClass.getInstance(dbClass.Id, node, controller);
								if (commandClass != null) {
									logger.debug("NODE {}: Node advancer: UPDATE_DATABASE - adding {}",
											node.getNodeId(), CommandClass.getCommandClass(dbClass.Id).getLabel());
									node.addCommandClass(commandClass);
								}
							}
						}
					}
				}
				break;

			case STATIC_VALUES:
				// Loop through all classes looking for static initialisation
				for (ZWaveCommandClass zwaveStaticClass : node.getCommandClasses()) {
					logger.debug("NODE {}: Node advancer: STATIC_VALUES - checking {}", node.getNodeId(), zwaveStaticClass
							.getCommandClass().getLabel());
					if (zwaveStaticClass instanceof ZWaveCommandClassInitialization) {
						logger.debug("NODE {}: Node advancer: STATIC_VALUES - found    {}", node.getNodeId(), zwaveStaticClass
								.getCommandClass().getLabel());
						ZWaveCommandClassInitialization zcci = (ZWaveCommandClassInitialization) zwaveStaticClass;
						int instances = zwaveStaticClass.getInstances();
						logger.debug("NODE {}: Found {} instances of {}", node.getNodeId(), instances, zwaveStaticClass.getCommandClass());
						if (instances == 1) {
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
								logger.debug("NODE {}: Node advancer: STATIC_VALUES - checking {} for endpoint {}",
										node.getNodeId(), endpointCommandClass.getCommandClass().getLabel(),
										endpoint.getEndpointId());
								if (endpointCommandClass instanceof ZWaveCommandClassInitialization) {
									logger.debug("NODE {}: Node advancer: STATIC_VALUES - found    {}", node.getNodeId(),
											endpointCommandClass.getCommandClass().getLabel());
									ZWaveCommandClassInitialization zcci2 = (ZWaveCommandClassInitialization) endpointCommandClass;
									addToQueue(zcci2.initialize(stageAdvanced), endpointCommandClass,
											endpoint.getEndpointId());
								}
							}
						}
					}
				}
				logger.debug("NODE {}: Node advancer: STATIC_VALUES - queued {} frames", node.getNodeId(), msgQueue.size());
				break;

			case ASSOCIATIONS:
				// Do we support associations
				ZWaveAssociationCommandClass associationCommandClass = (ZWaveAssociationCommandClass) node.getCommandClass(CommandClass.ASSOCIATION);
				if(associationCommandClass == null) {
					break;
				}

				// For now, we have no-way of knowing if we've received an update to the association
				// so just do this once
				if(stageAdvanced == false) {
					break;
				}

				// Open the product database
				ZWaveProductDatabase associations = new ZWaveProductDatabase();
				if(associations.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId(), node.getApplicationVersion()) == true) {
					// We have this device in the database
					// Assume the database is correct since some devices report invalid number of groups!
					List<ZWaveDbAssociationGroup> groupList = associations.getProductAssociationGroups();

					// No groups known
					if (groupList == null) {
						logger.debug("NODE {}: Node advancer: ASSOCIATIONS - none in database", node.getNodeId());
						break;
					}

					// Request every group
					for (ZWaveDbAssociationGroup group : groupList) {
						logger.debug("NODE {}: Node advancer: ASSOCIATIONS request group {}", node.getNodeId(), group.Index);
						addToQueue(associationCommandClass.getAssociationMessage(group.Index));
					}
				}
				else {
					for(int group = 1; group <= associationCommandClass.getMaxGroups(); group++) {
						logger.debug("NODE {}: Node advancer: ASSOCIATIONS request group {}", node.getNodeId(), group);
						addToQueue(associationCommandClass.getAssociationMessage(group));
					}
				}
				break;

			case SET_WAKEUP:
				// This stage sets the wakeup class if we're the master controller
				// It sets the node to point to us, and the time is left along
				if(controller.isMasterController() == false) {
					break;
				}

				ZWaveWakeUpCommandClass wakeupCommandClass = (ZWaveWakeUpCommandClass) node.getCommandClass(CommandClass.WAKE_UP);

				if (wakeupCommandClass == null) {
					logger.debug("NODE {}: Node advancer: SET_WAKEUP - Wakeup command class not supported", node.getNodeId());
					break;
				}

				if (wakeupCommandClass.getTargetNodeId() == controller.getOwnNodeId()) {
					logger.debug("NODE {}: Node advancer: SET_WAKEUP - TargetNode is set to controller", node.getNodeId());
					break;
				}

				int value = 3600;
				if (wakeupCommandClass.getInterval() == 0) {
					logger.debug("NODE {}: Node advancer: SET_WAKEUP - Interval is currently 0. Set to 3600", node.getNodeId());
				}
				else {
					value = wakeupCommandClass.getInterval();
				}

				logger.debug("NODE {}: Node advancer: SET_WAKEUP - Set wakeup node to controller ({}), period {}", node.getNodeId(), controller.getOwnNodeId(), value);

				// Set the wake-up interval, and request an update
				addToQueue(wakeupCommandClass.setInterval(value));
				addToQueue(wakeupCommandClass.getIntervalMessage());
				break;

			case SET_ASSOCIATION:
				if(controller.isMasterController() == false) {
					break;
				}

				database = new ZWaveProductDatabase();
				if(database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId(), node.getApplicationVersion()) == false) {
					// No database entry for this device!
					logger.warn("NODE {}: Node advancer: SET_ASSOCIATION - Unknown device: {}:{}:{}", node.getNodeId(),
							Integer.toHexString(node.getManufacturer()), Integer.toHexString(node.getDeviceType()), Integer.toHexString(node.getDeviceId()));
					break;
				}

				List<ZWaveDbAssociationGroup> groups = database.getProductAssociationGroups();
				if(groups == null || groups.size() == 0) {
					logger.debug("NODE {}: Node advancer: SET_ASSOCIATION - No association groups", node.getNodeId());
					break;
				}

				// Get the group members
				ZWaveAssociationCommandClass associationCls = (ZWaveAssociationCommandClass) node
						.getCommandClass(CommandClass.ASSOCIATION);
				if(associationCls == null) {
					logger.debug("NODE {}: Node advancer: SET_ASSOCIATION - ASSOCIATION class not supported", node.getNodeId());
					break;
				}

				// Loop through all the groups in the database
				for (ZWaveDbAssociationGroup group : groups) {
					if(group.SetToController == true) {
						// Check if we're already a member
						if(associationCls.getGroupMembers(group.Index).contains(controller.getOwnNodeId())) {
							logger.debug("NODE {}: Node advancer: SET_ASSOCIATION - ASSOCIATION set for group {}", node.getNodeId(), group.Index);
						}
						else {
							logger.debug("NODE {}: Node advancer: SET_ASSOCIATION - Adding ASSOCIATION to group {}", node.getNodeId(), group.Index);
							// Set the association, and request the update so we confirm if it's set 
							addToQueue(associationCls.setAssociationMessage(group.Index, controller.getOwnNodeId()));
							addToQueue(associationCls.getAssociationMessage(group.Index));
						}
					}
				}
				break;

			case GET_CONFIGURATION:
				database = new ZWaveProductDatabase();
				if(database.FindProduct(node.getManufacturer(), node.getDeviceType(), node.getDeviceId(), node.getApplicationVersion()) == false) {
					// No database entry for this device!
					logger.warn("NODE {}: Node advancer: GET_CONFIGURATION - Unknown device: {}:{}:{}", node.getNodeId(),
							Integer.toHexString(node.getManufacturer()), Integer.toHexString(node.getDeviceType()), Integer.toHexString(node.getDeviceId()));
					break;
				}

				ZWaveConfigurationCommandClass configurationCommandClass = (ZWaveConfigurationCommandClass) node
						.getCommandClass(CommandClass.CONFIGURATION);

				// If there are no configuration entries for this node, then continue.
				List<ZWaveDbConfigurationParameter> configList = database.getProductConfigParameters();
				if(configList == null || configList.size() == 0) {
					break;
				}

				// If the node doesn't support configuration class, then we better let people know!
				if (configurationCommandClass == null) {
					logger.error("NODE {}: Node advancer: GET_CONFIGURATION - CONFIGURATION class not supported", node.getNodeId());
					break;
				}

				// Request all parameters for this node
				for (ZWaveDbConfigurationParameter parameter : configList) {
					// Some parameters don't return anything, so don't request them!
					if(parameter.WriteOnly != null && parameter.WriteOnly == true) {
						configurationCommandClass.setParameterWriteOnly(parameter.Index, true);
						continue;
					}

					// If this is the first time around the loop
					// or we don't have a value for this parameter
					// then request it!
					if(configurationCommandClass.getParameter(parameter.Index) == null) {
						addToQueue(configurationCommandClass.getConfigMessage(parameter.Index));
					}
				}
				break;
				
			case DYNAMIC_VALUES:
				for (ZWaveCommandClass zwaveDynamicClass : node.getCommandClasses()) {
					logger.debug("NODE {}: Node advancer: DYNAMIC_VALUES - checking {}", node.getNodeId(), zwaveDynamicClass
							.getCommandClass().getLabel());
					if (zwaveDynamicClass instanceof ZWaveCommandClassDynamicState) {
						logger.debug("NODE {}: Node advancer: DYNAMIC_VALUES - found    {}", node.getNodeId(), zwaveDynamicClass
								.getCommandClass().getLabel());
						ZWaveCommandClassDynamicState zdds = (ZWaveCommandClassDynamicState) zwaveDynamicClass;
						int instances = zwaveDynamicClass.getInstances();
						logger.debug("NODE {}: Found {} instances of {}", node.getNodeId(), instances, zwaveDynamicClass.getCommandClass());
						if (instances == 1) {
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
								logger.debug("NODE {}: Node advancer: DYNAMIC_VALUES - checking {} for endpoint {}",
										node.getNodeId(), endpointCommandClass.getCommandClass().getLabel(),
										endpoint.getEndpointId());
								if (endpointCommandClass instanceof ZWaveCommandClassDynamicState) {
									logger.debug("NODE {}: Node advancer: DYNAMIC_VALUES - found    {}", node.getNodeId(),
											endpointCommandClass.getCommandClass().getLabel());
									ZWaveCommandClassDynamicState zdds2 = (ZWaveCommandClassDynamicState) endpointCommandClass;
									addToQueue(zdds2.getDynamicValues(stageAdvanced), endpointCommandClass,
											endpoint.getEndpointId());
								}
							}
						}
					}
				}
				logger.debug("NODE {}: Node advancer: DYNAMIC_VALUES - queued {} frames", node.getNodeId(), msgQueue.size());
				break;

			case STATIC_END:
			case DONE:
				// Save the node information to file
				nodeSerializer.SerializeNode(node);

				if(currentStage != ZWaveNodeInitStage.DONE) {
					break;
				}
				logger.debug("NODE {}: Node advancer: Initialisation complete!", node.getNodeId());
				
				// Stop the retry timer
				resetIdleTimer();

				// We remove the event listener to reduce loading now that we're done
				controller.removeEventListener(this);

				// Notify everyone!
				ZWaveEvent zEvent = new ZWaveInitializationCompletedEvent(node.getNodeId());
				controller.notifyEventListeners(zEvent);

				// Return from here as we're now done and we don't want to
				// increment the stage!
				return;

			case SESSION_START:
				// This is a 'do nothing' state.
				// It's used as a marker within the NodeStage class to indicate
				// where to start initialisation if we restored from XML.
				break;

			default:
				logger.debug("NODE {}: Node advancer: Unknown node state {} encountered.",
						node.getNodeId(), currentStage.toString().toString());
				break;
			}

			// If there are messages queued, send one.
			// If there are none, then it means we're happy that we have all the
			// data for this stage.
			// If we have all the data, set stageAdvanced to true to tell the system
			// that we're starting again, then loop around again.
			if (currentStage != ZWaveNodeInitStage.DONE && sendMessage() == false) {
				// Move on to the next stage
				setCurrentStage(currentStage.getNextStage());
				stageAdvanced = true;

				// Reset the backoff timer
				retryTimer = BACKOFF_TIMER_START;

				logger.debug("NODE {}: Node advancer - advancing to {}", node.getNodeId(),
						currentStage.toString());
			}
		} while (msgQueue.isEmpty());
	}

	/**
	 * Move the messages to the queue
	 * 
	 * @param msgs
	 *            the message collection
	 */
	private void addToQueue(SerialMessage serialMessage) {
		if (serialMessage == null) {
			return;
		}
		if (!msgQueue.contains(serialMessage) && msgQueue.remainingCapacity() > 1) {
			msgQueue.add(serialMessage);
		}
	}

	/**
	 * Move all the messages in a collection to the queue
	 * 
	 * @param msgs
	 *            the message collection
	 */
	private void addToQueue(Collection<SerialMessage> msgs) {
		if (msgs == null) {
			return;
		}
		for (SerialMessage serialMessage : msgs) {
			addToQueue(serialMessage);
		}
	}

	/**
	 * Move all the messages in a collection to the queue and encapsulates them
	 * 
	 * @param msgs
	 *            the message collection
	 * @param the
	 *            command class to encapsulate
	 * @param endpointId
	 *            the endpoint number
	 */
	private void addToQueue(Collection<SerialMessage> msgs, ZWaveCommandClass commandClass, int endpointId) {
		if (msgs == null) {
			return;
		}
		for (SerialMessage serialMessage : msgs) {
			addToQueue(node.encapsulate(serialMessage, commandClass, endpointId));
		}
	}

	/**
	 * Gets the current node stage
	 * 
	 * @return current node stage
	 */
	public ZWaveNodeInitStage getCurrentStage() {
		return currentStage;
	}

	/**
	 * Sets the current node stage
	 */
	public void setCurrentStage(ZWaveNodeInitStage newStage) {
		currentStage = newStage;

		// Remember the time so we can handle retries and keep users
		// informed
		queryStageTimeStamp = Calendar.getInstance().getTime();
	}

	/**
	 * Sets the time stamp the node was last queried.
	 * 
	 * @param queryStageTimeStamp
	 *            the queryStageTimeStamp to set
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
		return (currentStage == ZWaveNodeInitStage.DONE);
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
		if (currentStage == ZWaveNodeInitStage.DONE) {
			return;
		}

		// Process transaction complete events
		if (event instanceof ZWaveTransactionCompletedEvent) {
			ZWaveTransactionCompletedEvent completeEvent = (ZWaveTransactionCompletedEvent) event;

			SerialMessage serialMessage = completeEvent.getCompletedMessage();
			byte[] payload = serialMessage.getMessagePayload();

			// Make sure this is addressed to us
			if (payload.length == 0 || node.getNodeId() != (payload[0] & 0xFF)) {
				// This is a corrupt frame, OR, it's not addressed to us
				// We use this as a trigger to kick things off again if they've stalled
				// by checking to see if the transmit queue is now empty.
				// This will allow battery devices stuck in WAIT state to get moving.
				if(controller.getSendQueueLength() < 2 && currentStage == ZWaveNodeInitStage.WAIT) {
					logger.debug("NODE {}: Node advancer - WAIT: The WAIT is over!", node.getNodeId());

					currentStage = currentStage.getNextStage();
					handleNodeQueue(null);
				}
				return;
			}

			switch (serialMessage.getMessageClass()) {
			case SendData:
			case IdentifyNode:
			case RequestNodeInfo:
			case GetRoutingInfo:
			case IsFailedNodeID:
				logger.debug("NODE {}: Node advancer - {}: Transaction complete ({}:{}) success({})",
						node.getNodeId(), currentStage.toString(), serialMessage.getMessageClass(),
						serialMessage.getMessageType(), completeEvent.getState());

				// If this frame was successfully sent, then handle the stage advancer
				if (((ZWaveTransactionCompletedEvent) event).getState() == true) {
					handleNodeQueue(serialMessage);
				}
				break;
			default:
				break;
			}
		} else if (event instanceof ZWaveWakeUpCommandClass.ZWaveWakeUpEvent) {
			// WAKEUP EVENT - only act if this is a wakeup notification
			if (((ZWaveWakeUpCommandClass.ZWaveWakeUpEvent) event).getEvent() != ZWaveWakeUpCommandClass.WAKE_UP_NOTIFICATION) {
				return;
			}

			// A wakeup event is received. Make sure it's for this node
			if (node.getNodeId() != event.getNodeId()) {
				return;
			}

			logger.debug("NODE {}: Wakeup during initialisation.", node.getNodeId());

			wakeupCount++;
			advanceNodeStage(null);
		} else if (event instanceof ZWaveNodeStatusEvent) {
			ZWaveNodeStatusEvent statusEvent = (ZWaveNodeStatusEvent) event;
			// A network status event is received. Make sure it's for this node.
			if (node.getNodeId() != event.getNodeId()) {
				return;
			}

			logger.debug("NODE {}: Node Status event during initialisation - Node is {}", statusEvent.getNodeId(),
					statusEvent.getState());

			switch (statusEvent.getState()) {
			case DEAD:
			case FAILED:
				break;
			case ALIVE:
				advanceNodeStage(null);
				break;
			}
			logger.trace("NODE {}: Node Status event during initialisation processed", statusEvent.getNodeId());
		} else if (event instanceof ZWaveNodeInfoEvent) {
			logger.debug("NODE {}: {} NIF event during initialisation stage {}", event.getNodeId(), node.getNodeId(), currentStage);
			if (node.getNodeId() != event.getNodeId()) {
				return;
			}
			
			if(currentStage == ZWaveNodeInitStage.PING) {
				logger.debug("NODE {}: NIF event during initialisation stage PING - advancing", event.getNodeId());
				setCurrentStage(currentStage.getNextStage());
			}
			logger.debug("NODE {}: NIF event during initialisation stage {}", event.getNodeId(), currentStage);
			advanceNodeStage(null);
/*		} else if (event instanceof ZWaveCommandClassValueEvent) {
			// This code is used to detect an event during the IDLE stage.
			// This is used to kick start the initialisation for battery nodes that do not support
			// the WAKE_UP class and don't send the ApplicationUpdateMessage when they are initialised.
			logger.debug("NODE {}: {} CC event during initialisation stage {}", event.getNodeId(), node.getNodeId(), currentStage);
			// A command class event is received. Make sure it's for this node.
			if (node.getNodeId() != event.getNodeId() || currentStage != ZWaveNodeInitStage.PING) {
				return;
			}
			logger.debug("NODE {}: CC event during initialisation stage PING - advancing", event.getNodeId());
			setCurrentStage(currentStage.getNextStage());
			logger.debug("NODE {}: CC event during initialisation stage PING - now {} - next", event.getNodeId(), currentStage);
			advanceNodeStage(null);*/
		}
	}
	
	// The following timer implements a re-triggerable timer. The timer is used to restart
	// initialisation if it stalls using a log backoff
	private class IdleTimerTask extends TimerTask {
		@Override
		public void run() {
			// Increase the backoff
			retryTimer *= 2;
			if(retryTimer > BACKOFF_TIMER_MAX) {
				retryTimer = BACKOFF_TIMER_MAX;
			}

			// Timer has triggered, so log it!
			logger.debug("NODE {}: Stage {}. Initialisation retry timer triggered. Increased to {}",
					node.getNodeId(), currentStage.toString(), retryTimer);

			// Kickstart comms - clear the queue and run the advancer
			msgQueue.clear();
			advanceNodeStage(null);
		}
	}

	private synchronized void resetIdleTimer() {
		// Stop any existing timer
		if(timerTask != null) {
			timerTask.cancel();
		}
		timerTask = null;
	}

	private synchronized void startIdleTimer() {
		// For nodes that aren't listening, don't start the timer
		// We handle battery nodes differently - by counting the number of wakeups.
		if(!node.isListening() && !node.isFrequentlyListening()) {
			return;
		}

		// Stop any existing timer
		resetIdleTimer();

		// Create the timer task
		timerTask = new IdleTimerTask();

		// Start the timer
		timer.schedule(timerTask, retryTimer);
		logger.debug("NODE {}: Initialisation retry timer started {}", node.getNodeId(), retryTimer);
	}
}
