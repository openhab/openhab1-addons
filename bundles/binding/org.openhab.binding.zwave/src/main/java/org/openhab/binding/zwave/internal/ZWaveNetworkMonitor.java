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

	private static long HEAL_CYCLE_PERIOD = 15000;

	private long networkHealDeadCheckPeriod = 60000;
	private long networkHealDeadCheckNext = 0;
	private int networkHealNightlyNode = 0;
	private int networkHealNightlyHour = 2;
	private long networkHealNightlyTime = 0;

	enum HealState {
		WAITING, UPDATENEIGHBORS, UPDATENEIGHBORSNEXT, UPDATEROUTES, UPDATEROUTESNEXT, GETNEIGHBORS, GETNEIGHBORSNEXT
	};

	HealState networkHealState = HealState.WAITING;

	public ZWaveNetworkMonitor(ZWaveController controller) {
		zController = controller;
	}

	/**
	 * The execute method is called periodically from the binding. It is the
	 * main entry point for the network monitor class.
	 */
	public void execute() {
		// Perform network healing
		// This periodically checks for DEAD nodes, and if it finds any it will
		// perform a heal
		// It will also (optionally) perform a network heal at a specified time
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

		boolean nodeDone = false;
		switch (networkHealState) {
		case WAITING:
			networkHealState = HealState.UPDATENEIGHBORS;
			break;
		case UPDATENEIGHBORS:
			networkHealNightlyNode = 0;
			networkHealState = HealState.UPDATENEIGHBORSNEXT;
		case UPDATENEIGHBORSNEXT:
			for (; networkHealNightlyNode <= 232; ++networkHealNightlyNode) {
				ZWaveNode node = zController.getNode(networkHealNightlyNode);
				if (node == null)
					continue;

				logger.debug("NODE {}: Heal is updating node neighbors.");
				zController.requestNodeNeighborUpdate(node.getNodeId());
				break;
			}

			// Add a gap between nodes
			networkHealNightlyTime = System.currentTimeMillis() + HEAL_CYCLE_PERIOD;

			// Check if this is complete for all nodes
			if (nodeDone == false) {
				networkHealState = HealState.UPDATEROUTES;
				break;
			}
			break;
		case UPDATEROUTES:
			networkHealNightlyNode = 0;
			networkHealState = HealState.UPDATEROUTESNEXT;
		case UPDATEROUTESNEXT:
			for (; networkHealNightlyNode <= 232; ++networkHealNightlyNode) {
				ZWaveNode node = zController.getNode(networkHealNightlyNode);
				if (node == null)
					continue;

				logger.debug("NODE {}: Heal is updating node routes.");
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
			for (; networkHealNightlyNode <= 232; ++networkHealNightlyNode) {
				ZWaveNode node = zController.getNode(networkHealNightlyNode);
				if (node == null)
					continue;

				logger.debug("NODE {}: Heal is requesting node neighbor info.");
				zController.requestNodeRoutingInfo(node.getNodeId());
				break;
			}

			// Check if this is complete for all nodes
			if (nodeDone == false) {
				networkHealState = HealState.WAITING;

				// Calculate the time of the next nightly heal
				Calendar next = Calendar.getInstance();
				next.set(Calendar.HOUR_OF_DAY, networkHealNightlyHour);
				next.set(Calendar.MINUTE, 0);
				next.set(Calendar.SECOND, 0);
				networkHealNightlyTime = next.getTimeInMillis() + 86400000;
				break;
			}
			break;
		}
	}
}
