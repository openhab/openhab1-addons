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

import org.openhab.binding.lcn.common.PckGenerator;
import org.openhab.binding.lcn.common.PckParser;
import org.openhab.binding.lcn.connection.Connection;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * LCN connection state (LCN-PK/PKU) received from LCN-PCHK.
 * 
 * @author Tobias Jüttner
 */
public class LcnConnState extends Input {
	
	/** The connection state to LCN-PK/PKU. */
	private final boolean isLcnConnected;
	
	/**
	 * Constructor.
	 * 
	 * @param isLcnConnected the connection state
	 */
	LcnConnState(boolean isLcnConnected) {
		this.isLcnConnected = isLcnConnected; 
	}
	
	/**
	 * Gets the connection state
	 * 
	 * @return true if the LCN bus is connected to LCN-PK/PKU
	 */
	boolean isLcnConnected() {
		return this.isLcnConnected;
	}
	
	/**
	 * Tries to parse the given input received from LCN-PCHK.
	 * 
	 * @param input the input
	 * @return list of {@link LcnConnState} (might be empty, but not null}
	 */
	static Collection<Input> tryParseInput(String input) {
		LinkedList<Input> ret = new LinkedList<Input>();
		if (input.equals(PckParser.LCNCONNSTATE_CONNECTED)) {
			ret.add(new LcnConnState(true));
		}
		else if (input.equals(PckParser.LCNCONNSTATE_DISCONNECTED)) {
			ret.add(new LcnConnState(false));
		}
		return ret;
	}
	
	/**
	 * Notifies the connection about the LCN connection state.
	 * {@inheritDoc}
	 */
	@Override
	public void process(Connection conn) {
		if (this.isLcnConnected) {
			conn.queue(PckGenerator.setOperationMode(conn.getSets().getDimMode(), conn.getSets().getStatusMode()));
		}
		conn.setLcnConnected(this.isLcnConnected);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean tryVisualization(Input.VisualizationVisitor handler, Connection conn, Command cmd, Item item, EventPublisher eventPublisher) {
		// Not used for visualization
		return false;
	}
	
}
