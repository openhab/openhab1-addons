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
import org.openhab.binding.lcn.common.LcnDefs;
import org.openhab.binding.lcn.common.PckParser;
import org.openhab.binding.lcn.connection.Connection;
import org.openhab.binding.lcn.connection.ModInfo;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * Status of LEDs and logic-operations received from an LCN module.
 * 
 * @author Tobias Jüttner
 */
public class ModStatusLedsAndLogicOps extends Mod {
	
	/** 12x LED status. */
	private final LcnDefs.LedStatus[] statesLeds;
	
	/** 4x logic-operation status. */
	private final LcnDefs.LogicOpStatus[] statesLogicOps;
	
	/**
	 * Constructor.
	 * 
	 * @param physicalSourceAddr the source address
	 * @param statesLeds the 12 LED states
	 * @param statesLogicOps the 4 logic-operation states
	 */
	ModStatusLedsAndLogicOps(LcnAddrMod physicalSourceAddr, LcnDefs.LedStatus[] statesLeds, LcnDefs.LogicOpStatus[] statesLogicOps) {
		super(physicalSourceAddr);
		this.statesLeds = statesLeds;
		this.statesLogicOps = statesLogicOps;
	}
		
	/**
	 * Gets the status of a single LED.
	 * 
	 * @param ledId 0..11
	 * @return the LED's status
	 */
	public LcnDefs.LedStatus getLedState(int ledId) {
		return this.statesLeds[ledId];
	}
	
	/**
	 * Gets the status of a single logic operation.
	 * 
	 * @param logicOpId 0..3
	 * @return the logic-operation's status
	 */
	public LcnDefs.LogicOpStatus getLogicOpState(int logicOpId) {
		return this.statesLogicOps[logicOpId];
	}

	/**
	 * Tries to parse the given input received from LCN-PCHK.
	 * 
	 * @param input the input
	 * @return list of {@link ModStatusLedsAndLogicOps} (might be empty, but not null}
	 */
	static Collection<Input> tryParseInput(String input) {
		LinkedList<Input> ret = new LinkedList<Input>();
		Matcher matcher = PckParser.PATTERN_STATUS_LEDSANDLOGICOPS.matcher(input);
		if (matcher.matches()) {
			LcnDefs.LedStatus[] statesLeds = new LcnDefs.LedStatus[12];
			for (int i = 0; i < 12; ++i) {
				switch (matcher.group("ledStates").toUpperCase().charAt(i)) {
					case 'A': statesLeds[i] = LcnDefs.LedStatus.OFF; break;
					case 'E': statesLeds[i] = LcnDefs.LedStatus.ON; break;
					case 'B': statesLeds[i] = LcnDefs.LedStatus.BLINK; break;
					case 'F': statesLeds[i] = LcnDefs.LedStatus.FLICKER; break;
					default: throw new Error();
				}
			}
			LcnDefs.LogicOpStatus[] statesLogicOps = new LcnDefs.LogicOpStatus[4]; 
			for (int i = 0; i < 4; ++i) {
				switch (matcher.group("logicOpStates").toUpperCase().charAt(i)) {
					case 'N': statesLogicOps[i] = LcnDefs.LogicOpStatus.NOT; break;
					case 'T': statesLogicOps[i] = LcnDefs.LogicOpStatus.OR; break;
					case 'V': statesLogicOps[i] = LcnDefs.LogicOpStatus.AND; break;
					default: throw new Error();
				}
			}
			ret.add(new ModStatusLedsAndLogicOps(new LcnAddrMod(Integer.parseInt(matcher.group("segId")),
				Integer.parseInt(matcher.group("modId"))), statesLeds, statesLogicOps));
		}
		return ret;
	}
	
	/**
	 * Notifies the connection about the received LEDs and logic-operations status. 
	 * {@inheritDoc}
	 */
	@Override
	public void process(Connection conn) {
		super.process(conn);  // Will replace source segment 0 with the local segment id
		ModInfo info = conn.getModInfo(this.logicalSourceAddr);
		if (info != null) {
			info.requestStatusLedsAndLogicOps.onResponseReceived();
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean tryVisualization(Input.VisualizationVisitor handler, Connection conn, Command cmd, Item item, EventPublisher eventPublisher) {
		return handler.visualizationLedsAndLogicOpsStatus(this, cmd, item, eventPublisher);
	}	
	
}
