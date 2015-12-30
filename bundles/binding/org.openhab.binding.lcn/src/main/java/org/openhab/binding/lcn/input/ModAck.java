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
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * Acknowledge received from an LCN module.
 * 
 * @author Tobias Jüttner
 */
public class ModAck extends Mod {
	
	/** LCN internal code. -1 = "positive". */
	private final int code;
	
	/**
	 * Constructor.
	 * 
	 * @param physicalSourceAddr the source address
	 * @param code the LCN internal ack. code
	 */
	ModAck(LcnAddrMod physicalSourceAddr, int code) {
		super(physicalSourceAddr);
		this.code = code;
	}
	
	/**
	 * Gets the acknowledge code.
	 * 
	 * @return the LCN internal code (-1 = "positive")
	 */
	public int getCode() {
		return this.code;
	}
	
	/**
	 * Tries to parse the given input received from LCN-PCHK.
	 * 
	 * @param input the input
	 * @return list of {@link ModAck} (might be empty, but not null}
	 */
	static Collection<Input> tryParseInput(String input) {
		LinkedList<Input> ret = new LinkedList<Input>();
		Matcher matcher;
		if ((matcher = PckParser.PATTERN_ACK_POS.matcher(input)).matches()) {
			ret.add(new ModAck(
				new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("modId"))),
				-1));
		}
		else if ((matcher = PckParser.PATTERN_ACK_NEG.matcher(input)).matches()) {
			ret.add(new ModAck(
				new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("modId"))),
				Integer.parseInt(matcher.group("code"))));
		}
		return ret;
	}
	
	/**
	 * Notifies the connection about the acknowledge.
	 * {@inheritDoc}
	 */
	@Override
	public void process(Connection conn) {
		super.process(conn);  // Will replace source segment 0 with the local segment id
		conn.onAck(this.logicalSourceAddr, this.code);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean tryVisualization(Input.VisualizationVisitor handler, Connection conn, Command cmd, Item item, EventPublisher eventPublisher) {
		// Not used for visualization
		return false;
	}
	
}
