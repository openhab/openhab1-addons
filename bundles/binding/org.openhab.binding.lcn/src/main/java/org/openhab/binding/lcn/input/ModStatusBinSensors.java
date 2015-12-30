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
 * Status of 8 binary sensors received from an LCN module.
 * 
 * @author Tobias Jüttner
 */
public class ModStatusBinSensors extends Mod {
	
	/** 8 binary states. */
	private final boolean[] states;
	
	/**
	 * Constructor.
	 * 
	 * @param physicalSourceAddr the source address
	 * @param states the 8 binary states
	 */
	ModStatusBinSensors(LcnAddrMod physicalSourceAddr, boolean[] states) {
		super(physicalSourceAddr);
		this.states = states;
	}
	
	/**
	 * Gets the state of a single binary-sensor.
	 * 
	 * @param binSensorId 0..7
	 * @return the binary-sensor's state
	 */
	public boolean getState(int binSensorId) {
		return this.states[binSensorId];
	}
	
	/**
	 * Tries to parse the given input received from LCN-PCHK.
	 * 
	 * @param input the input
	 * @return list of {@link ModStatusBinSensors} (might be empty, but not null}
	 */
	static Collection<Input> tryParseInput(String input) {
		LinkedList<Input> ret = new LinkedList<Input>();
		try {
			Matcher matcher = PckParser.PATTERN_STATUS_BINSENSORS.matcher(input);
			if (matcher.matches()) {
				ret.add(new ModStatusBinSensors(
					new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("modId"))),
					PckParser.getBooleanValue(Integer.parseInt(matcher.group("byteValue")))));
			}
		} catch (IllegalArgumentException ex) { }
		return ret;
	}
	
	/**
	 * Notifies the connection about the received binary-sensor status. 
	 * {@inheritDoc}
	 */
	@Override
	public void process(Connection conn) {
		super.process(conn);  // Will replace source segment 0 with the local segment id
		ModInfo info = conn.getModInfo(this.logicalSourceAddr);
		if (info != null) {
			info.requestStatusBinSensors.onResponseReceived();
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean tryVisualization(Input.VisualizationVisitor handler, Connection conn, Command cmd, Item item, EventPublisher eventPublisher) {
		return handler.visualizationBinSensorsStatus(this, cmd, item, eventPublisher);
	}
	
}
