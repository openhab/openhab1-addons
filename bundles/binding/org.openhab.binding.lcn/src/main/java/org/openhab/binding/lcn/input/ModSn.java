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
 * Serial number and firmware version received from an LCN module.
 * 
 * @author Tobias Jüttner
 */
public class ModSn extends Mod {
	
	/** Firmware date. */
	private final int swAge;
	
	/**
	 * Constructor.
	 * 
	 * @param physicalSourceAddr the source address
	 * @param swAge the firmware date
	 */
	ModSn(LcnAddrMod physicalSourceAddr, int swAge) {
		super(physicalSourceAddr);
		this.swAge = swAge;
	}
	
	/**
	 * Gets the firmware date.
	 * Used to check for available features.
	 * 
	 * @return the firmware date
	 */
	public int getSwAge() {
		return this.swAge;
	}
	
	/**
	 * Tries to parse the given input received from LCN-PCHK.
	 * 
	 * @param input the input
	 * @return list of {@link ModSn} (might be empty, but not null}
	 */
	static Collection<Input> tryParseInput(String input) {
		LinkedList<Input> ret = new LinkedList<Input>();
		Matcher matcher = PckParser.PATTERN_SN.matcher(input);
		if (matcher.matches()) {
			ret.add(new ModSn(
				new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("modId"))),
				Integer.parseInt(matcher.group("swAge"), 16)));
		}
		return ret;
	}
	
	/**
	 * Updates the module's data cache with the firmware date.
	 * {@inheritDoc}
	 */
	@Override
	public void process(Connection conn) {
		super.process(conn);  // Will replace source segment 0 with the local segment id
		ModInfo info = conn.getModInfo(this.logicalSourceAddr);
		if (info != null) {
			info.setSwAge(this.swAge);
			info.requestSwAge.onResponseReceived();
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean tryVisualization(Input.VisualizationVisitor handler, Connection conn, Command cmd, Item item, EventPublisher eventPublisher) {
		// Not used for visualization
		return false;
	}
	
}
