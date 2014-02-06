/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal;

import java.util.Calendar;

import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveAssociationCommandClass;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;
import org.openhab.binding.zwave.internal.protocol.initialization.ZWaveNodeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Network monitoring functions for the ZWave Binding
 * 
 * @author Chris Jackson
 * @since 1.5.0
 */
public final class ZWaveNetworkMonitor {

	private static Logger logger = LoggerFactory.getLogger(ZWaveNetworkMonitor.class);

	ZWaveController zController = null;

	// This needs to be long enough to allow for any retries and other activities on the network
	private static long HEAL_CYCLE_PERIOD = 30000;

	private long networkHealDeadCheckPeriod = 60000;
	private long networkHealDeadCheckNext = 0;
	private int networkHealNightlyNode = 0;
	private int networkHealNightlyHour = 2;
	private long networkHealNightlyTime = 0;

	enum HealState {
		WAITING, UPDATENEIGHBORS, UPDATENEIGHBORSNEXT, GETASSOCIATIONS, GETASSOCIATIONSNEXT, UPDATEROUTES, UPDATEROUTESNEXT, GETNEIGHBORS, GETNEIGHBORSNEXT, COMPLETE
	};

	HealState networkHealState = HealState.WAITING;

	public ZWaveNetworkMonitor(ZWaveController controller) {
		zController = controller;

		// Initialise the time for the next heal
		networkHealNightlyTime = calculateNextHeal();
	}

	/**
	 * The execute method is called periodically from the binding. It is the
	 * main entry point for the network monitor class. This periodically checks
	 * for DEAD nodes, and if it finds any it will perform a heal It will also
	 * (optionally) perform a network heal at a specified time
	 */
	public void execute() {
		// Check for dead nodes
		if (networkHealDeadCheckNext < System.currentTimeMillis()) {
			for (int nodeId = 1; nodeId <= 232; nodeId++) {
				ZWaveNode node = zController.getNode(nodeId);
				if (node == null)
					continue;

				if (node.isDead()) {
					logger.debug("NODE {}: DEAD node - requesting network heal.", node.getNodeId());

					zController.requestUpdateNodeRoutes(node.getNodeId());
				}

				// Reset the node stage
				// This will also set the state back to DONE if the node
				// completed initialisation
				// TODO: We need to handle nodes that went to DEAD during
				// initialisation
				node.resetResendCount();
			}

			// Calculate the time for the next 'death' check
			networkHealDeadCheckNext = System.currentTimeMillis() + networkHealDeadCheckPeriod;
		}

		// Just ensure that we slow things down a bit so we don't swamp the
		// network.
		// All of the "healing", the updating of nodes etc is done by the
		// controller
		// So it's difficult to say what this does at network level (??).
		if (networkHealNightlyTime > System.currentTimeMillis())
			return;
		networkHealNightlyTime = System.currentTimeMillis() + HEAL_CYCLE_PERIOD;
		
		boolean nodeDone = false;
		switch (networkHealState) {
		case WAITING:
			// Disable the "Dead node" check while we're doing a heal
			// This might not be necessary, but it prevents any further network
			// congestion
			networkHealDeadCheckNext = Long.MAX_VALUE;

			networkHealState = HealState.UPDATENEIGHBORS;
			break;
		case UPDATENEIGHBORS:
			networkHealNightlyNode = 1;
			networkHealState = HealState.UPDATENEIGHBORSNEXT;
		case UPDATENEIGHBORSNEXT:
			while (networkHealNightlyNode <= 232) {
				ZWaveNode node = zController.getNode(networkHealNightlyNode);
				networkHealNightlyNode++;
				if (node == null)
					continue;

				nodeDone = true;
				logger.debug("NODE {}: Heal is updating node neighbors.", node.getNodeId());
				zController.requestNodeNeighborUpdate(node.getNodeId());
				break;
			}

			// Check if this is complete for all nodes
			if (nodeDone == false) {
				networkHealState = HealState.GETASSOCIATIONS;
				break;
			}
			break;
		case GETASSOCIATIONS:
			networkHealNightlyNode = 1;
			networkHealState = HealState.GETASSOCIATIONSNEXT;
		case GETASSOCIATIONSNEXT:
			while (networkHealNightlyNode <= 232) {
				ZWaveNode node = zController.getNode(networkHealNightlyNode);
				networkHealNightlyNode++;
				if (node == null)
					continue;


				ZWaveAssociationCommandClass associationCommandClass = (ZWaveAssociationCommandClass) node
						.getCommandClass(CommandClass.ASSOCIATION);
				if(associationCommandClass == null)
					continue;

				nodeDone = true;
				logger.debug("NODE {}: Heal is requesting device associations.", node.getNodeId());
				associationCommandClass.getAllAssociations();
				break;
			}

			// Check if this is complete for all nodes
			if (nodeDone == false) {
				networkHealState = HealState.UPDATEROUTES;
				break;
			}
			break;
		case UPDATEROUTES:
			networkHealNightlyNode = 1;
			networkHealState = HealState.UPDATEROUTESNEXT;
		case UPDATEROUTESNEXT:
			while (networkHealNightlyNode <= 232) {
				ZWaveNode node = zController.getNode(networkHealNightlyNode);
				networkHealNightlyNode++;
				if (node == null)
					continue;

				nodeDone = true;
				logger.debug("NODE {}: Heal is updating node routes.", node.getNodeId());
				zController.requestUpdateNodeRoutes(node.getNodeId());
				break;
			}

			// Check if this is complete for all nodes
			if (nodeDone == false) {
				networkHealState = HealState.GETNEIGHBORS;
				break;
			}
			break;
		case GETNEIGHBORS:
			networkHealNightlyNode = 0;
			networkHealState = HealState.GETNEIGHBORSNEXT;
		case GETNEIGHBORSNEXT:
			while (networkHealNightlyNode <= 232) {
				networkHealNightlyNode++;

				ZWaveNode node = zController.getNode(networkHealNightlyNode);
				if (node == null)
					continue;

				nodeDone = true;
				logger.debug("NODE {}: Heal is requesting node neighbor info.", node.getNodeId());
				zController.requestNodeRoutingInfo(node.getNodeId());
				break;
			}

			// Check if this is complete for all nodes
			if (nodeDone == false) {
				networkHealState = HealState.COMPLETE;
			}
			break;
		case COMPLETE:
			// Save the XML files. This serialises the current we've just
			// updated (neighbors etc)
			for (networkHealNightlyNode = 0; networkHealNightlyNode <= 232; ++networkHealNightlyNode) {
				ZWaveNode node = zController.getNode(networkHealNightlyNode);
				if (node == null)
					continue;

				// Write the node to disk
				ZWaveNodeSerializer nodeSerializer = new ZWaveNodeSerializer();
				nodeSerializer.SerializeNode(node);
			}

			networkHealState = HealState.WAITING;

			// Calculate the time of the next nightly heal
			networkHealNightlyTime = calculateNextHeal();

			// Calculate the time for the next 'death' check
			networkHealDeadCheckNext = System.currentTimeMillis() + networkHealDeadCheckPeriod;
		}
	}

	private long calculateNextHeal() {
		Calendar next = Calendar.getInstance();
		next.set(Calendar.HOUR_OF_DAY, networkHealNightlyHour);
		next.set(Calendar.MINUTE, 0);
		next.set(Calendar.SECOND, 0);

		return next.getTimeInMillis() + 86400000;
	}
}
