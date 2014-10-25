/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEventListener;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveAssociationCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveNoOperationCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveWakeUpCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveInitializationCompletedEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNetworkEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNodeStatusEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveTransactionCompletedEvent;
import org.openhab.binding.zwave.internal.protocol.initialization.ZWaveNodeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Network monitoring functions for the ZWave Binding This is an attempt to
 * implement a monitor for dead nodes, and repair them. Also, to implement a
 * daily network heal process where neighbors are updated associations read,
 * and all routes between nodes reset to account for changes in the network.
 * 
 * Currently it's a simple timed function where the various commands are sent
 * and we add a delay between commands to allow the network functions to
 * complete. The delay is quite long since we don't really know what happens at
 * RF level and if there's any retries, we don't want to cause a queue.
 * 
 * Rational
 * ========
 * # Ping the node to see if it's awake
 * # Set the SUC return route (if there's an SUC)
 * # Update all the neighbors so that all nodes know who is around them
 * # Update the associations so that we know which nodes need to talk to others
 * # Update the routes between devices that have associations set
 * # Retrieve the neighbor list so that the binding knows who's out there
 * # Ping the node to see if it's awake
 * # Save the device files
 * 
 * Observations
 * ============ 
 * # Updating the neighbor nodes on the controller can take a long time (1 minute)
 * and it can fail. The failure might be a timeout (??) - there is no indication of reason.
 * 
 * @author Chris Jackson
 * @since 1.5.0
 */
public final class ZWaveNetworkMonitor implements ZWaveEventListener {

	private static Logger logger = LoggerFactory.getLogger(ZWaveNetworkMonitor.class);

	ZWaveController zController = null;

	// This sets a timeout. It's the time we'll wait for an event from the node
	// before continuing
	private long HEAL_TIMEOUT_PERIOD = 90000;
	private long HEAL_DELAY_PERIOD = 4000;
	private int HEAL_MAX_RETRIES = 5;
	private long PING_PERIOD = 90000;

	private int networkHealNightlyHour = -1;
	private long networkHealNextTime = Long.MAX_VALUE;
	private long networkHealNightlyTime = Long.MAX_VALUE;
	private long pingNodeTime = Long.MAX_VALUE;

	private boolean doSoftReset = false;
	private boolean initialised = false;

    private DateFormat df;

	Map<Integer, HealNode> healNodes = new HashMap<Integer, HealNode>();

	enum HealState {
		IDLE, WAITING, PING, SETSUCROUTE, UPDATENEIGHBORS, GETASSOCIATIONS, UPDATEROUTES, UPDATEROUTESNEXT, GETNEIGHBORS, PINGEND, SAVE, DONE, FAILED;

		public boolean isActive() {
			switch (this) {
			case IDLE:
			case DONE:
			case FAILED:
				return false;
			default:
				return true;
			}
		}
	};

	HealState networkHealState = HealState.WAITING;

	public ZWaveNetworkMonitor(ZWaveController controller) {
		df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));

		zController = controller;

		// Set an event callback so we get notification of network events
		zController.addEventListener(this);
	}

	/**
	 * Set the hour that a daily network heal is performed
	 * 
	 * @param time
	 *            the hour of the heal (0-23)
	 */
	public void setHealTime(Integer time) {
		if (time == null)
			networkHealNightlyHour = -1;
		else
			networkHealNightlyHour = time;

		// Sanity check
		if (networkHealNightlyHour > 23)
			networkHealNightlyHour = -1;
		if (networkHealNightlyHour < 0)
			networkHealNightlyHour = -1;

		if (initialised == false)
			return;

		// Calculate the next heal time
		networkHealNightlyTime = calculateNextHeal();
		networkHealNextTime = networkHealNightlyTime;
	}
	
	/**
	 * Configures the binding to perform a soft reset during the heal
	 * 
	 * @param doReset true to enable performing a soft reset on error or heal
	 */
	public void resetOnError(boolean doReset) {
		doSoftReset = doReset;
	}

	public String getNodeState(int nodeId) {
		String status = HealState.IDLE.toString();

		for (Map.Entry<Integer, HealNode> entry : healNodes.entrySet()) {
			HealNode node = entry.getValue();
			if (node.nodeId == nodeId) {
				switch (node.state) {
				case IDLE:
					break;
				case FAILED:
					status = "FAILED during " + node.failState + " @ " + df.format(node.lastChange);
					break;
				default:
					status = node.state + " @ " + df.format(node.lastChange);
					if (node.retryCnt > 1) {
						status += " (" + node.retryCnt + ")";
					}
					break;
				}
				break;
			}
		}

		return status;
	}

	/**
	 * Returns true if the node is currently in a healing state or scheduled for
	 * a heal.
	 * 
	 * @param nodeId
	 *            node to check
	 * @return true if healing is ongoing. false if waiting or failed.
	 */
	public boolean isNodeHealing(int nodeId) {
		for (Map.Entry<Integer, HealNode> entry : healNodes.entrySet()) {
			HealNode node = entry.getValue();
			if (node.nodeId == nodeId) {
				switch (node.state) {
				case IDLE:
				case WAITING:
				case FAILED:
				case DONE:
					return false;
				default:
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Perform an immediate heal on the specified node
	 * 
	 * @param nodeId
	 *            Node to perform the heal on
	 * @return true if the heal is scheduled
	 */
	public boolean healNode(int nodeId) {
		ZWaveNode node = zController.getNode(nodeId);
		if (node == null) {
			logger.error("NODE {}: Heal node - can't be found.", nodeId);
			return false;
		}

		HealNode heal = new HealNode();
		heal.node = node;
		heal.nodeId = nodeId;
		heal.retryCnt = 0;
		heal.routeList = null;
		heal.state = HealState.WAITING;
		heal.lastChange = Calendar.getInstance().getTime();

		// Find out if this is a listening device
		if (node.isListening())
			heal.listening = true;
		else
			heal.listening = false;
		healNodes.put(nodeId, heal);

		// Start the first heal next time around the loop
		networkHealNextTime = 0;

		return true;
	}

	/**
	 * Start a full network heal manually.
	 * 
	 * @return true if the heal is started otherwise false
	 */
	public boolean rescheduleHeal() {
		// Build a list of devices that we need to heal
		// The list is built multiple times since it seems that in order to
		// fully optimize the network, this is required
		for (ZWaveNode node : zController.getNodes()) {
			// Ignore devices that haven't initialized yet - unless they are
			// DEAD or FAILED.
			if (node.isInitializationComplete() == false && node.isDead() == false && node.isFailed() == false) {
				logger.debug("NODE {}: Initialisation NOT yet complete. Skipping heal.", node.getNodeId());
				continue;
			}

			healNode(node.getNodeId());
		}

		if (healNodes.size() == 0)
			return false;

		// If we want to do a soft reset on the controller, do it now....
		if(doSoftReset == true) {
			logger.debug("HEAL - Performing soft reset!");
			zController.requestSoftReset();
		}

		return true;
	}

	/**
	 * The execute method is called periodically from the binding. It is the
	 * main entry point for the network monitor class. It will (optionally)
	 * perform a network heal at a specified time.
	 */
	public void execute() {
		// Don't start the next node if there's a queue
		if (zController.getSendQueueLength() > 1) {
			logger.debug("Queue length is {} - deferring HEAL.", zController.getSendQueueLength());
			return;
		}

		if (pingNodeTime < System.currentTimeMillis()) {
			// Update the time and send a ping...
			pingNodeTime = System.currentTimeMillis() + PING_PERIOD;

			// Find the node that we haven't communicated with for the longest
			// time
			ZWaveNode oldestNode = null;
			for (ZWaveNode node : zController.getNodes()) {
				// Ignore the controller and nodes that aren't listening
				if (node.getNodeId() == zController.getOwnNodeId() || node.isListening() == false)
					continue;
				if (oldestNode == null) {
					oldestNode = node;
				} else if (node.getLastUpdated().getTime() < oldestNode.getLastUpdated().getTime()) {
					oldestNode = node;
				}
			}

			// We now have the oldest node that we've heard from - ping it!
			if (oldestNode != null) {
				logger.debug("NODE {}: Sending periodic PING.", oldestNode.getNodeId());

				// Reset the resend count - also resets the lastUpdate timer
				oldestNode.resetResendCount();

				ZWaveNoOperationCommandClass zwaveCommandClass = (ZWaveNoOperationCommandClass) oldestNode
						.getCommandClass(CommandClass.NO_OPERATION);
				if (zwaveCommandClass != null)
					zController.sendData(zwaveCommandClass.getNoOperationMessage());
			}

			// To reduce congestion, we don't do anything else during this
			// period
			return;
		}

		// Check if it's time to do another 'nightly' heal
		if (networkHealNightlyTime < System.currentTimeMillis()) {
			rescheduleHeal();
			networkHealNightlyTime = calculateNextHeal();
		}

		// Check to see if there's been a timeout during the heal process
		if (networkHealNextTime > System.currentTimeMillis()) {
			return;
		}

		// If there's a heal in process, see if we need to run the next node
		// First check to see if there's a heal in progress - this is a
		// timeout
		for (Map.Entry<Integer, HealNode> entry : healNodes.entrySet()) {
			HealNode node = entry.getValue();
			if (node.state != HealState.WAITING && node.state != HealState.FAILED && node.state != HealState.DONE
					&& node.listening == true) {
				nextHealStage(node);
				return;
			}
		}

		// No nodes are currently healing - run the next node
		for (Map.Entry<Integer, HealNode> entry : healNodes.entrySet()) {
			HealNode node = entry.getValue();
			// Don't automatically run 'listening' nodes
			// This should be triggered by a WAKEUP
			if (node.state == HealState.WAITING && node.listening == true) {
				nextHealStage(node);
				return;
			}
		}

		// There's nothing more to do
		networkHealNextTime = networkHealNightlyTime;
	}

	/**
	 * Perform the next step in the heal process. This function also handles the
	 * retries. Each time through the loop it increments the retry counter. If
	 * the max retries is exceeded then the failed state is saved and we return
	 * to allow other nodes to continue.
	 * 
	 * @param healing
	 *            The node on which to perform the heal
	 */
	private void nextHealStage(HealNode healing) {
		// Don't do anything if it's failed already
		if (healing.state == HealState.FAILED)
			return;

		healing.lastChange = Calendar.getInstance().getTime();

		// Set the ping time into the future.
		// This holds off the routine ping when there's a heal in progress
		// to avoid congestion and false timeouts.
		pingNodeTime = System.currentTimeMillis() + HEAL_TIMEOUT_PERIOD + 20000;

		// Handle retries
		healing.retryCnt++;
		if (healing.retryCnt >= HEAL_MAX_RETRIES) {
			logger.debug("NODE {}: Network heal has exceeded maximum retries", healing.nodeId);
			healing.failState = healing.state;
			healing.state = HealState.FAILED;
			networkHealNextTime = System.currentTimeMillis() + HEAL_DELAY_PERIOD;

			// Save the XML file. This serialises the data we've just updated
			// (neighbors etc)
			healing.node.setHealState(this.getNodeState(healing.node.getNodeId()));
			
			ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
			nodeSerializer.SerializeNode(healing.node);
			return;
		}

		// Set the timeout
		networkHealNextTime = System.currentTimeMillis() + HEAL_TIMEOUT_PERIOD;

		switch (healing.state) {
		case WAITING:
			logger.debug("NODE {}: NETWORK HEAL - STARTING", healing.nodeId);

			// Reset the resend count.
			// This also resets the time so that we cycle through all the nodes
			healing.node.resetResendCount();

		case PING:
			if (healing.nodeId != zController.getOwnNodeId()) {
				healing.state = HealState.PING;
				ZWaveNoOperationCommandClass zwaveCommandClass = (ZWaveNoOperationCommandClass) healing.node
						.getCommandClass(CommandClass.NO_OPERATION);
				if (zwaveCommandClass != null) {
					zController.sendData(zwaveCommandClass.getNoOperationMessage());
					healing.stateNext = HealState.SETSUCROUTE;
					break;
				}
			}
			healing.state = HealState.SETSUCROUTE;
		case SETSUCROUTE:
			// Only set the route if this is not the controller and there is an SUC in the network
			if (healing.nodeId != zController.getOwnNodeId() && zController.getSucId() != 0) {
				// Update the route to the controller
				logger.debug("NODE {}: Heal is setting SUC route.", healing.nodeId);
				healing.event = ZWaveNetworkEvent.Type.AssignSucReturnRoute;
				healing.stateNext = HealState.UPDATENEIGHBORS;
				zController.requestAssignSucReturnRoute(healing.nodeId);
				break;
			}
			healing.state = HealState.UPDATENEIGHBORS;
		case UPDATENEIGHBORS:
			logger.debug("NODE {}: Heal is updating node neighbors.", healing.nodeId);
			healing.event = ZWaveNetworkEvent.Type.NodeNeighborUpdate;
			healing.stateNext = HealState.GETASSOCIATIONS;
			zController.requestNodeNeighborUpdate(healing.nodeId);
			break;
		case GETASSOCIATIONS:
			// Check if this node supports associations
			ZWaveAssociationCommandClass associationCommandClass = (ZWaveAssociationCommandClass) healing.node
					.getCommandClass(CommandClass.ASSOCIATION);
			if (associationCommandClass != null) {
				logger.debug("NODE {}: Heal is requesting device associations.", healing.nodeId);
				healing.stateNext = HealState.UPDATEROUTES;
				healing.event = ZWaveNetworkEvent.Type.AssociationUpdate;
				associationCommandClass.getAllAssociations();
				break;
			}
		case UPDATEROUTES:
			// Get the list of routes for this node
			healing.routeList = healing.node.getRoutingList();
			if (healing.routeList != null && healing.routeList.size() != 0) {
				// Delete all the return routes for the node
				logger.debug("NODE {}: Heal is deleting routes.", healing.nodeId);
				healing.event = ZWaveNetworkEvent.Type.DeleteReturnRoute;
				healing.stateNext = HealState.UPDATEROUTESNEXT;
				zController.requestDeleteAllReturnRoutes(healing.nodeId);
				break;
			}
		case UPDATEROUTESNEXT:
			if (healing.routeList != null && healing.routeList.size() != 0) {
				// Loop through all the nodes and set the return route
				logger.debug("NODE {}: Adding return route to {}", healing.nodeId, healing.routeList.get(0));
				healing.stateNext = HealState.GETNEIGHBORS;
				healing.event = ZWaveNetworkEvent.Type.AssignReturnRoute;
				zController.requestAssignReturnRoute(healing.nodeId, healing.routeList.get(0));
				break;
			}
		case GETNEIGHBORS:
			healing.event = ZWaveNetworkEvent.Type.NodeRoutingInfo;
			healing.stateNext = HealState.PINGEND;

			logger.debug("NODE {}: Heal is requesting node neighbor info.", healing.nodeId);
			zController.requestNodeRoutingInfo(healing.nodeId);
			break;
		case PINGEND:
			if (healing.nodeId != zController.getOwnNodeId()) {
				ZWaveNoOperationCommandClass zwaveCommandClass = (ZWaveNoOperationCommandClass) healing.node
						.getCommandClass(CommandClass.NO_OPERATION);
				if (zwaveCommandClass == null)
					break;
				zController.sendData(zwaveCommandClass.getNoOperationMessage());
				healing.stateNext = HealState.SAVE;
				break;
			}
		case SAVE:
			logger.debug("NODE {}: Heal is complete - saving XML.", healing.nodeId);
			healing.state = HealState.DONE;

			networkHealNextTime = System.currentTimeMillis() + HEAL_DELAY_PERIOD;
			// Save the XML file. This serialises the data we've just updated
			// (neighbors etc)
			healing.node.setHealState(this.getNodeState(healing.node.getNodeId()));
			
			ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
			nodeSerializer.SerializeNode(healing.node);
			break;
		default:
			break;
		}
		healing.node.setHealState(this.getNodeState(healing.node.getNodeId()));
	}

	/**
	 * Calculates the time (milliseconds) to start the next network
	 * heal
	 * @return time in milliseconds for next heal.
	 */
	private long calculateNextHeal() {
		// Initialise the time for the first heal
		if(networkHealNightlyHour == -1)
			return Long.MAX_VALUE;

		Calendar next = Calendar.getInstance();
		next.set(Calendar.HOUR_OF_DAY, networkHealNightlyHour);
		next.set(Calendar.MINUTE, 0);
		next.set(Calendar.SECOND, 0);
		next.set(Calendar.MILLISECOND, 0);

		if (next.getTimeInMillis() < System.currentTimeMillis())
			return next.getTimeInMillis() + 86400000;
		return next.getTimeInMillis();
	}

	/**
	 * Capture events that might be useful during the heal process We need to
	 * know when a network event happens - these are specific events that are
	 * used in the heal process (routing etc). We need to know if a device wakes
	 * up so we can heal it (if needed) We need to know when a PING transaction
	 * completes.
	 */
	@Override
	public void ZWaveIncomingEvent(ZWaveEvent event) {
		// Handle network events
		if (event instanceof ZWaveNetworkEvent) {
			ZWaveNetworkEvent nwEvent = (ZWaveNetworkEvent) event;
			logger.debug("NODE {}: Network heal EVENT", nwEvent.getNodeId());

			// Get the heal class for this notification
			HealNode node = healNodes.get(nwEvent.getNodeId());
			if (node == null)
				return;

			// Is this the event we're waiting for
			if (nwEvent.getEvent() != node.event)
				return;

			switch (nwEvent.getState()) {
			case Success:
				node.retryCnt = 0;
				node.state = node.stateNext;
				break;
			case Failure:
				logger.debug("NODE {}: Network heal received FAILURE event", node.nodeId);
				break;
			}

			// If retry count is 0 and we have a list of routes, then this must have
			// been a successful route set - remove this node
			if (node.retryCnt == 0 && node.routeList != null && node.routeList.size() > 0)
				node.routeList.remove(0);

			// Continue....
			nextHealStage(node);
		} else if (event instanceof ZWaveTransactionCompletedEvent) {
			SerialMessage serialMessage = ((ZWaveTransactionCompletedEvent) event).getCompletedMessage();

			if (serialMessage.getMessageClass() != SerialMessageClass.SendData
					&& serialMessage.getMessageType() != SerialMessageType.Request)
				return;

			byte[] payload = serialMessage.getMessagePayload();
			if (payload.length < 3)
				return;

			HealNode node = healNodes.get(payload[0] & 0xFF);
			if (node == null)
				return;

			// See if this node is waiting for a PING
			if ((node.state == HealState.PING || node.state == HealState.PINGEND) && payload.length >= 3
					&& (payload[2] & 0xFF) == ZWaveCommandClass.CommandClass.NO_OPERATION.getKey()) {
				node.state = node.stateNext;
				node.retryCnt = 0;
				nextHealStage(node);
				return;
			}
		} else if (event instanceof ZWaveWakeUpCommandClass.ZWaveWakeUpEvent) {
			// We only care about the wake-up notification
			if (((ZWaveWakeUpCommandClass.ZWaveWakeUpEvent) event).getEvent() != ZWaveWakeUpCommandClass.WAKE_UP_NOTIFICATION)
				return;

			// A wakeup event is received. Find the node in the node list
			HealNode node = healNodes.get(event.getNodeId());
			if (node == null)
				return;

			// Check to see the state of this node
			// and only process if there's something to do
			if (!node.state.isActive())
				return;

			logger.debug("NODE {}: Heal WakeUp EVENT {}", node.nodeId, node.state);
			nextHealStage(node);
		} else if (event instanceof ZWaveNodeStatusEvent) {
			ZWaveNodeStatusEvent statusEvent = (ZWaveNodeStatusEvent) event;

			logger.debug("NODE {}: Node Status event - Node is {}", statusEvent.getNodeId(), statusEvent.getState());

			switch (statusEvent.getState()) {
			case Dead:
				ZWaveNode node = zController.getNode(statusEvent.getNodeId());
				if (node == null) {
					logger.error("NODE {}: Status event received, but node not found.", statusEvent.getNodeId());
					return;
				}
				zController.requestIsFailedNode(node.getNodeId());
				// The node is dead, but we may have already started a Heal
				// If so, don't start it again!
				if (!isNodeHealing(node.getNodeId())) {
					logger.debug("NODE {}: DEAD node - requesting network heal.", node.getNodeId());

					healNode(node.getNodeId());

					// Reset the node stage to PING.
					// This will also set the state back to DONE in
					// resetResendCount if the node
					// has already completed initialisation.
					// node.setNodeStage(NodeStage.PING);

					node.resetResendCount();
				} else {
					logger.debug("NODE {}: DEAD node - already healing.", node.getNodeId());
				}
				break;
			case Failed:
				ZWaveNode failedNode = zController.getNode(statusEvent.getNodeId());
				if (failedNode == null) {
					logger.error("NODE {}: Status event received, but node not found.", statusEvent.getNodeId());
					return;
				}

				// The node is dead, but we may have already started a Heal
				// If so, don't start it again!
				if (!isNodeHealing(failedNode.getNodeId())) {
					logger.debug("NODE {}: FAILED node - requesting network heal.", failedNode.getNodeId());

					healNode(failedNode.getNodeId());

					// Reset the node stage to PING.
					// This will also set the state back to DONE in
					// resetResendCount if the node
					// has already completed initialisation.
					// node.setNodeStage(NodeStage.PING);

					failedNode.resetResendCount();
				} else {
					logger.debug("NODE {}: FAILED node - already healing.", failedNode.getNodeId());
				}
				break;
			case Alive:
				break;
			}
		} else if (event instanceof ZWaveInitializationCompletedEvent) {
			logger.debug("Network initialised - starting network monitor.");

			// Remember that we've initialsed the binding.
			initialised = true;

			// Calculate the next heal time
			networkHealNightlyTime = calculateNextHeal();
			networkHealNextTime = networkHealNightlyTime;

			// Set the next PING time
			pingNodeTime = System.currentTimeMillis() + PING_PERIOD;
		}
	}

	class HealNode {
		public HealState state;
		public HealState stateNext;
		public int nodeId;
		public boolean listening;
		public HealState failState;
		public int retryCnt = 0;
		public Date lastChange;
		public ArrayList<Integer> routeList;
		public ZWaveNetworkEvent.Type event;
		ZWaveNode node;
	}
}
