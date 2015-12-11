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
 * Segment information received from an LCN segment coupler.
 * 
 * @author Tobias Jüttner
 */
public class ModSk extends Mod {
	
	/** The logical segment id reported by the segment coupler. */
	private final int reportedSegId;
	
	/**
	 * Constructor.
	 * 
	 * @param physicalSourceAddr the source address
	 * @param reportedSegId the logical segment id reported by the segment coupler
	 */
	ModSk(LcnAddrMod physicalSourceAddr, int reportedSegId) {
		super(physicalSourceAddr);
		this.reportedSegId = reportedSegId;
	}

	/**
	 * Gets the logical segment id reported by the segment coupler.
	 * 
	 * @return the logical segment id
	 */
	public int getReportedSegId() {
		return this.reportedSegId;
	}
	
	/**
	 * Tries to parse the given input received from LCN-PCHK.
	 * 
	 * @param input the input
	 * @return list of {@link ModSk} (might be empty, but not null}
	 */
	static Collection<Input> tryParseInput(String input) {
		LinkedList<Input> ret = new LinkedList<Input>(); 
		Matcher matcher = PckParser.PATTERN_SK_RESPONSE.matcher(input);
		if (matcher.matches()) {
			ret.add(new ModSk(
				new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("modId"))),
				Integer.parseInt(matcher.group("id"))));
		}
		return ret;
	}
	
	/**
	 * Sets the local segment id in the connection.
	 * {@inheritDoc}
	 */
	@Override
	public void process(Connection conn) {
		if (this.physicalSourceAddr.getSegId() == 0) {  // Must come from the local segment coupler
			conn.setLocalSegId(this.reportedSegId);
		}
		super.process(conn);  // Will replace source segment 0 with the local segment id
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean tryVisualization(Input.VisualizationVisitor handler, Connection conn, Command cmd, Item item, EventPublisher eventPublisher) {
		// Not used for visualization
		return false;
	}
	
}
