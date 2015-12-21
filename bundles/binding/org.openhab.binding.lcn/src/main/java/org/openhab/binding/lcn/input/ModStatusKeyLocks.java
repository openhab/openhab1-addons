/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.input;

import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Matcher;

import org.openhab.binding.lcn.common.LcnAddrMod;
import org.openhab.binding.lcn.common.PckParser;
import org.openhab.binding.lcn.connection.Connection;
import org.openhab.binding.lcn.connection.ModInfo;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * Status of locked keys received from an LCN module.
 * 
 * @author Tobias Jüttner
 */
public class ModStatusKeyLocks extends Mod {
	
	/** 4x8 key-lock states. */
	private final boolean[][] states;
	
	/**
	 * Constructor.
	 * 
	 * @param physicalSourceAddr the source address
	 * @param states the 4x8 key-lock states
	 */
	ModStatusKeyLocks(LcnAddrMod physicalSourceAddr, boolean[][] states) {
		super(physicalSourceAddr);
		this.states = states;
	}
	
	/**
	 * Gets the lock-state of a single key.
	 * 
	 * @param tableId 0(A)..3(D)
	 * @param keyId 0..7
	 * @return the key's lock-state
	 */
	public boolean getState(int tableId, int keyId) {
		return this.states[tableId][keyId];
	}
	
	/**
	 * Tries to parse the given input received from LCN-PCHK.
	 * 
	 * @param input the input
	 * @return list of {@link ModStatusKeyLocks} (might be empty, but not null}
	 */
	static Collection<Input> tryParseInput(String input) {
		LinkedList<Input> ret = new LinkedList<Input>();
		try {
			Matcher matcher = PckParser.PATTERN_STATUS_KEYLOCKS.matcher(input);
			if (matcher.matches()) {
				boolean[][] states = new boolean[4][];
				for (int i = 0; i < 4; ++i) {
					String s = matcher.group(String.format("table%d", i));
					states[i] = s != null ? PckParser.getBooleanValue(Integer.parseInt(s)) : new boolean[8];
				}
				ret.add(new ModStatusKeyLocks(
					new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("modId"))), states));
			}
		} catch (IllegalArgumentException ex) { }
		return ret;
	}
	
	/**
	 * Notifies the connection about the received key-locks status. 
	 * {@inheritDoc}
	 */
	@Override
	public void process(Connection conn) {
		super.process(conn);  // Will replace source segment 0 with the local segment id
		ModInfo info = conn.getModInfo(this.logicalSourceAddr);
		if (info != null) {
			info.requestStatusLockedKeys.onResponseReceived();
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean tryVisualization(Input.VisualizationVisitor handler, Connection conn, Command cmd, Item item, EventPublisher eventPublisher) {
		return handler.visualizationKeyLocksStatus(this, cmd, item, eventPublisher);
	}
	
}
