/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.internal;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.openhab.binding.lcn.common.LcnAddr;
import org.openhab.binding.lcn.common.LcnAddrMod;
import org.openhab.binding.lcn.connection.Connection;
import org.openhab.binding.lcn.connection.ConnectionManager;
import org.openhab.binding.lcn.connection.ModInfo;
import org.openhab.binding.lcn.input.Input;
import org.openhab.binding.lcn.mappingtarget.Target;
import org.openhab.binding.lcn.mappingtarget.TargetWithLcnAddr;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;

/**
 * Holds all data bound to an openHAB item.
 * Contains all mappings read from item-configuration.
 * 
 * @author Tobias Jüttner
 */
class LcnBindingConfig implements BindingConfig {
	
	/** Single mapping. */
	static class Mapping {
		
		/** OpenHAB Command associated with the mapping. */
		private final Command cmd;
		
		/** The target connection's unique identifier. */
		private final String connId;
		
		/** The mapping's target. */
		private final Target target;
		
		/** Flag to indicate this mapping has already received status data. */
		private boolean hasInitialData = false;

		/**
		 * Constructor for a mapping.
		 * 
		 * @param connId the target connection's unique identifier
		 * @param cmd the openHAB command this mapping is bound to
		 * @param lcnTarget the target's configuration
		 */
		Mapping(Command cmd, String connId, String lcnTarget) {
			this.cmd = cmd;
			this.connId = connId;
			this.target = Target.parse(lcnTarget);
		}
		
		/**
		 * Gets the openHAB command the mapping is bound to.
		 * Might be an empty dummy for "visualization-mappings".
		 * 
		 * @return the openHAB command
		 */
		Command getOpenHabCmd() {
			return this.cmd;
		}
		
		/**
		 * Gets the target's connection identifier.
		 * 
		 * @return the connection identifier
		 */
		String getConnId() {
			return this.connId;
		}
		
		/**
		 * Get the parsed target.
		 * 
		 * @return the target
		 */
		Target getTarget() {
			return this.target;
		}
		
		/**
		 * Gets the flag that indicates if this mapping has already received status data.
		 * 
		 * @return true if the mapping has already received status data
		 */
		boolean hasInitialData() {
			return this.hasInitialData;
		}
		
		/**
		 * Sets the flag that indicates if this mapping has already received status data.
		 * 
		 * @param hasInitialData the flag
		 */
		void setInitialData(boolean hasInitialData) {
			this.hasInitialData = hasInitialData;
		}
		
	}
	
	/** The actual underlying openHAB item. */
	private final Item item;
	
	/** Mappings for this item. */
	private final LinkedList<Mapping> mappings = new LinkedList<Mapping>();
	
	/**
	 * Constructor for a new LCN item for a given openHAB item.
	 * 
	 * @param item the source openHAB item
	 */
	LcnBindingConfig(Item item) {
		this.item = item;
	}
	
	/**
	 * Adds a new mapping.
	 * 
	 * @param mapping the mapping to add
	 */
	void add(Mapping mapping) {
		this.mappings.add(mapping);
	}
	
	/**
	 * Processes received input.
	 * 
	 * @param pckInput the input
	 * @param conn the source connection
	 * @param eventPublisher the {@link EventPublisher} to receive updates
	 */
	void processInput(Input pckInput, Connection conn, EventPublisher eventPublisher) {
		for (Mapping mapping : this.mappings) {
			if (pckInput.tryVisualization(mapping.getTarget(), conn, mapping.getOpenHabCmd(), this.item, eventPublisher)) {
				return;  // Finish after one mapping has accepted the data
			}
		}
	}
	
	/**
	 * Sends data bound to the given openHAB command.
	 * 
	 * @param conns the main {@link ConnectionManager}
	 * @param cmd the openHAB command to send
	 */
	void send(ConnectionManager conns, Command cmd) {
		for (Mapping mapping : this.mappings) {
			boolean ok = false;
			if (mapping.getOpenHabCmd() instanceof StringType) {
				if (mapping.getOpenHabCmd().toString().equals("%i") && cmd instanceof DecimalType) {
					ok = true;
				}
			}
			if (mapping.getOpenHabCmd() == cmd) {
				ok = true;
			}
			if (ok) {
				Connection conn = conns.get(mapping.getConnId());
				if (conn != null && conn.isReady()) {
					mapping.getTarget().send(conn, this.item, cmd);
				}
			}
		}
	}
	
	/**
	 * Returns a set of LCN module addresses related to this item.
	 * Can be used as a filter to optimize input-processing.
	 *  
	 * @return the LCN module addresses
	 */
	Set<LcnAddrMod> getRelatedModules() {
		HashSet<LcnAddrMod> ret = new HashSet<LcnAddrMod>();
		for (Mapping mapping : this.mappings) {
			if (mapping.getTarget() instanceof TargetWithLcnAddr) {
				LcnAddr addr = ((TargetWithLcnAddr)mapping.getTarget()).getAddr();
				if (!addr.isGroup()) {
					ret.add((LcnAddrMod)addr);
				}
			}
		}
		return ret;
	}
	
	/**
	 * Called periodically to keep the item active.
	 * 
	 * @param conn the {@link Connection} to update for
	 */
	void update(Connection conn) {
		// Allows all targets to register their status-requests
		for (Mapping mapping : this.mappings) {
			if (mapping.connId.equalsIgnoreCase(conn.getSets().getId())) {
				// If our mapping is "brand new", it might still need its initial LCN (status) data
				if (!mapping.hasInitialData()) {
					for (LcnAddrMod addr : this.getRelatedModules()) {
						ModInfo info = conn.getModInfo(addr);
						if (info != null) {  // Only reset already-existing ones
							info.resetNotCachedStatusRequests();
						}
					}
					mapping.setInitialData(true);
				}
				mapping.getTarget().register(conn);
			}
		}
	}
	
}
