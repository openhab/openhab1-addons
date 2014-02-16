/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import org.openhab.binding.zwave.internal.protocol.event.ZWaveNetworkEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveTransactionCompletedEvent;
import org.openhab.binding.zwave.internal.protocol.initialization.ZWaveNodeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Network monitoring functions for the ZWave Binding This is an attempt to
 * implement a monitor for dead nodes, and repair them. Also, to implement a
 * daily network heal process where neighbours are updated associations read,
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
	private static long HEAL_TIMEOUT_PERIOD = 120000;
	private static long HEAL_DELAY_PERIOD = 4000;
	private static int HEAL_MAX_RETRIES = 5;

	private long networkHealDeadCheckPeriod = 60000;
	private long networkHealDeadCheckNext = 0;
	private int networkHealNightlyHour = -1;
	private long networkHealNextTime = 0;
	private long networkHealNightlyTime = 0;

	Map<Integer, HealNode> healNodes = null;

	enum HealState {
		WAITING, PING, SETSUCROUTE, UPDATENEIGHBORS, GETASSOCIATIONS, UPDATEROUTES, UPDATEROUTESNEXT, GETNEIGHBORS, PINGEND, SAVE, DONE
	};

	HealState networkHealState = HealState.WAITING;

	public ZWaveNetworkMonitor(ZWaveController controller) {
		zController = controller;

		// Initialise the time for the first heal
		networkHealNightlyTime = Long.MAX_VALUE;
		networkHealNextTime = networkHealNightlyTime;

		// Set an event callback so we get notification of network events
		zController.addEventListener(this);
	}

	/**
	 * Set the hour that a daily network heal is performed 
	 * @param time the hour of the heal (0-23)
	 */
	public void setHealTime(Integer time) {
		if(time == null)
			networkHealNightlyHour = -1;
		else
			networkHealNightlyHour = time;
		
		// Sanity check
		if(networkHealNightlyHour > 23)
			networkHealNightlyHour = -1;
		if(networkHealNightlyHour < 0)
			networkHealNightlyHour = -1;

		// Calculate the next heal time
		networkHealNightlyTime = calculateNextHeal();
		networkHealNextTime = networkHealNightlyTime;		
	}

	/**
	 * Perform an immediate heal on the specified node
	 * @param nodeId Node to perform the heal on
	 * @return true if the heal is scheduled
	 */
	public boolean healNode(int nodeId) {
		if(healNodes == null)
			healNodes = new HashMap<Integer, HealNode>();

		ZWaveNode node = zController.getNode(nodeId);
		if (node == null)
			return false;

		HealNode heal = new HealNode();
		heal.node = node;
		heal.nodeId = nodeId;
		heal.retriesCnt = 0;
		heal.routeList = null;
		heal.state = HealState.WAITING;
		heal.lastChange = Calendar.getInstance().getTime();

		healNodes.put(nodeId, heal);

		return true;
	}

	public String getNodeState(int nodeId) {
		String status = HealState.WAITING.toString();

		if(healNodes != null) {
			for (Map.Entry<Integer, HealNode> entry : healNodes.entrySet()) {
				HealNode node = entry.getValue();
				if (node.nodeId == nodeId) {
					if(node.state != HealState.WAITING) {
						if(node.retriesCnt != 0)
							status = node.state + "(" + node.retriesCnt + ") @ " + node.lastChange.toString();
						else
							status = node.state + " @ " + node.lastChange.toString();
					}
					break;
				}
			}
		}

		return status;
	}

	/**
	 * Start a full network heal manually.
	 * 
	 * @return true if the heal is started otherwise false
	 */
	public boolean rescheduleHeal() {
		if(healNodes == null)
			healNodes = new HashMap<Integer, HealNode>();

		// Build a list of devices that we need to heal
		// The list is built multiple times since it seems that in order to
		// fully optimise the network, this is required
		for (int cnt = 1; cnt <= 232; cnt++) {
			ZWaveNode node = zController.getNode(cnt);
			if (node == null)
				continue;

			HealNode heal = new HealNode();
			heal.node = node;
			heal.nodeId = cnt;
			heal.retriesCnt = 0;
			heal.routeList = null;
			heal.state = HealState.WAITING;
			heal.lastChange = Calendar.getInstance().getTime();


			// Ignore devices that haven't initialised yet
//			if(node.)

			// Find out if this is a listening device
			if (node.isListening())
				heal.listening = true;
			else
				heal.listening = false;

			healNodes.put(cnt, heal);
		}

		if (healNodes.size() == 0)
			return false;

		// Start the first heal
		for (Map.Entry<Integer, HealNode> entry : healNodes.entrySet()) {
			HealNode node = entry.getValue();
			if (node.listening == true) {
				nextHealStage(node);
				break;
			}
		}

		return true;
	}

	/**
	 * The execute method is called periodically from the binding. It is the
	 * main entry point for the network monitor class. This periodically checks
	 * for DEAD nodes, and if it finds any it will perform a heal It will also
	 * (optionally) perform a network heal at a specified time
	 */
	public void execute() {
		// Check for dead nodes
		/*
		 * if (networkHealDeadCheckNext < System.currentTimeMillis()) { for (int
		 * nodeId = 1; nodeId <= 232; nodeId++) { ZWaveNode node =
		 * zController.getNode(nodeId); if (node == null) continue;
		 * 
		 * if (node.isDead()) {
		 * logger.debug("NODE {}: DEAD node - requesting network heal.",
		 * node.getNodeId());
		 * 
		 * zController.requestAssignSucReturnRoute(node.getNodeId()); }
		 * 
		 * // Reset the node stage // This will also set the state back to DONE
		 * if the node // completed initialisation // TODO: We need to handle
		 * nodes that went to DEAD during // initialisation
		 * node.resetResendCount(); }
		 * 
		 * // Calculate the time for the next 'death' check
		 * networkHealDeadCheckNext = System.currentTimeMillis() +
		 * networkHealDeadCheckPeriod; }
		 */

		// deviceStressTest(5, 1);

		// Check if it's time to do another 'nightly' heal
		if (networkHealNightlyTime < System.currentTimeMillis()) {
			rescheduleHeal();
			networkHealNightlyTime = calculateNextHeal();
		}

		// Check to if there's been a timeout
		if (networkHealNextTime > System.currentTimeMillis())
			return;

		if (healNodes != null) {
			// If there's a heal in process, see if we need to run the next node
			// First check to see if there's a heal in progress - this is a
			// timeout
			for (Map.Entry<Integer, HealNode> entry : healNodes.entrySet()) {
				HealNode node = entry.getValue();
				if (node.state != HealState.WAITING && node.state != HealState.DONE && node.listening == true) {
					nextHealStage(node);
					return;
				}
			}

			// No nodes are running - run the next node
			for (Map.Entry<Integer, HealNode> entry : healNodes.entrySet()) {
				HealNode node = entry.getValue();
				// Don't automatically run 'listening' nodes
				// This should be triggered by a WAKEUP
				if (node.state != HealState.DONE && node.listening == true) {
					nextHealStage(node);
					return;
				}
			}
		}
	}

	/** Perform the next step in the heal process
	 * 
	 * @param healing The node on which to perform the heal
	 */
	private void nextHealStage(HealNode healing) {
		logger.debug("NODE {}: HEAL CALL - {}", healing.nodeId, healing.state);
		healing.lastChange = Calendar.getInstance().getTime();

		networkHealNextTime = System.currentTimeMillis() + HEAL_TIMEOUT_PERIOD;
		switch (healing.state) {
		case WAITING:
			logger.debug("NODE {}: ************** NETWORK HEAL - STARTING", healing.nodeId);
			// Disable the "Dead node" check while we're doing a heal
			// This might not be necessary, but it prevents any further network
			// congestion
			networkHealDeadCheckNext = Long.MAX_VALUE;

		case PING:
			if (healing.nodeId != zController.getOwnNodeId()) {
				healing.state = HealState.PING;
				ZWaveNoOperationCommandClass zwaveCommandClass = (ZWaveNoOperationCommandClass) healing.node
						.getCommandClass(CommandClass.NO_OPERATION);
				if (zwaveCommandClass == null)
					break;
				zController.sendData(zwaveCommandClass.getNoOperationMessage());
				healing.stateNext = HealState.UPDATENEIGHBORS;
				break;
			}
			healing.state = HealState.UPDATENEIGHBORS;

			// Skip over SUC Route - it seems to fail!
			/*
			 * case SETSUCROUTE: // Only set the route if this is not the
			 * controller if (healing.nodeId != zController.getOwnNodeId()) { //
			 * Update the route to the controller
			 * logger.debug("NODE {}: Heal is setting SUC route.",
			 * healing.nodeId); healing.event =
			 * ZWaveNetworkEvent.Type.AssignSucReturnRoute; healing.event =
			 * ZWaveNetworkEvent.Type.NodeRoutingInfo; healing.stateNext =
			 * HealState.UPDATENEIGHBORS;
			 * zController.requestNodeRoutingInfo(healing.nodeId); //
			 * zController.requestAssignSucReturnRoute(healing.nodeId); break; }
			 */
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
			// Save the XML file. This serialises the data we've just updated
			// (neighbors etc)
			ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
			nodeSerializer.SerializeNode(healing.node);

			healing.state = HealState.DONE;

			networkHealNextTime = System.currentTimeMillis() + HEAL_DELAY_PERIOD;
			break;
		default:
			break;
		}
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
	 * Capture events that might be useful during the heal process
	 * We need to know when a network event happens - these are specific
	 * events that are used in the heal process (routing etc).
	 * We need to know if a device wakes up so we can heal it (if needed)
	 * We need to know when a PING transaction completes.
	 */
	@Override
	public void ZWaveIncomingEvent(ZWaveEvent event) {
		if (healNodes == null)
			return;

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
				node.retriesCnt = 0;
				node.state = node.stateNext;
				break;
			case Failure:
				logger.debug("NODE {}: Network heal received FAILURE event", node.nodeId);
				if (node.retriesCnt >= HEAL_MAX_RETRIES) {
					logger.debug("NODE {}: Network heal has exceeded maximum retries", node.nodeId);
					node.state = node.stateNext;
					node.retriesCnt = 0;
				} else {
					logger.debug("NODE {}: Network heal will retry last request", node.nodeId);

					// Increment retries
					node.retriesCnt++;
				}
				break;
			}

			// If retry count is 0 and we have a list of routes, then this must
			// have
			// been a successful route set - remove this node
			if (node.retriesCnt == 0 && node.routeList != null && node.routeList.size() > 0)
				node.routeList.remove(0);

			// Continue....
			nextHealStage(node);
		} else if (event instanceof ZWaveTransactionCompletedEvent) {
			SerialMessage serialMessage = ((ZWaveTransactionCompletedEvent) event).getCompletedMessage();

			if (serialMessage.getMessageClass() != SerialMessageClass.SendData && serialMessage.getMessageType() != SerialMessageType.Request)
				return;

			byte[] payload = serialMessage.getMessagePayload();

			HealNode node = healNodes.get(payload[0] & 0xFF);
			if (node == null)
				return;

			// See if this node is waiting for a PING
			if ((node.state == HealState.PING || node.state == HealState.PINGEND) && payload.length >= 3 && (payload[2] & 0xFF) == ZWaveCommandClass.CommandClass.NO_OPERATION.getKey()) {
				node.state = node.stateNext;
				node.retriesCnt = 0;
				nextHealStage(node);
				return;
			}
		} else if (event instanceof ZWaveWakeUpCommandClass.ZWaveWakeUpEvent) {
			HealNode node = healNodes.get(event.getNodeId());
			if (node == null)
				return;

			logger.debug("NODE {}: HEAL WAKEUP EVENT", node.nodeId);
			nextHealStage(node);
		}
	}

	class HealNode {
		public HealState state;
		public HealState stateNext;
		public int nodeId;
		public boolean listening;
		public int retriesCnt = 0;
		public Date lastChange;
		public ArrayList<Integer> routeList;
		public ZWaveNetworkEvent.Type event;
		ZWaveNode node;
	}
}
