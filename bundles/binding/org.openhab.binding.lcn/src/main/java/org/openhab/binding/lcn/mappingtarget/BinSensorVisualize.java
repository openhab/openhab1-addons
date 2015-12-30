/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.mappingtarget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lcn.common.LcnAddrMod;
import org.openhab.binding.lcn.connection.Connection;
import org.openhab.binding.lcn.connection.ModInfo;
import org.openhab.binding.lcn.input.ModStatusBinSensors;
import org.openhab.binding.lcn.input.ModStatusKeyLocks;
import org.openhab.binding.lcn.input.ModStatusLedsAndLogicOps;
import org.openhab.binding.lcn.input.ModStatusOutput;
import org.openhab.binding.lcn.input.ModStatusRelays;
import org.openhab.binding.lcn.input.ModStatusVar;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;

/**
 * Visualizes a binary-sensor.
 * 
 * @author Tobias Jüttner
 */
public class BinSensorVisualize extends TargetWithLcnAddr {
	
	/** Pattern to parse binary-sensor visualizations. */ 
	private static final Pattern PATTERN_BINSENSOR_STATE =
		Pattern.compile("(?<binSensorId>[12345678])",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** Binary-sensor to visualize (0..7). */
	private final int binSensorId;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN module
	 * @param binSensorId 0..7
	 */
	BinSensorVisualize(LcnAddrMod addr, int binSensorId) {
		super(addr);
		this.binSensorId = binSensorId;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link BinSensorVisualize} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, false);
		if (header != null) {
			Matcher matcher;
			switch (header.getCmd().toUpperCase()) {
				case "BINARY_STATE":
					if ((matcher = PATTERN_BINSENSOR_STATE.matcher(header.getRestInput())).matches()) {
						return new BinSensorVisualize((LcnAddrMod)header.getAddr(), Integer.parseInt(matcher.group("binSensorId")) - 1);
					}
					break;
			}
		}
		return null;
	}
	
	/** {@inheritDoc} */
	@Override
	public void send(Connection conn, Item item, Command cmd) { }
	
	/** {@inheritDoc} */
	@Override
	public void register(Connection conn) {
		ModInfo info = conn.updateModuleData((LcnAddrMod)this.addr);
		if (!info.requestStatusBinSensors.isActive()) {
			info.requestStatusBinSensors.nextRequestIn(0, System.nanoTime());
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationHandleOutputStatus(ModStatusOutput pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationHandleRelaysStatus(ModStatusRelays pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/**
	 * Visualization for {@link OpenClosedType} and {@link OnOffType}.
	 * {@inheritDoc}
	 */
	@Override
	public boolean visualizationBinSensorsStatus(ModStatusBinSensors pchkInput, Command cmd, Item item, EventPublisher eventPublisher) {
		if (pchkInput.getLogicalSourceAddr().equals(this.addr)) {
			if (item.getAcceptedDataTypes().contains(OpenClosedType.class)) {
				eventPublisher.postUpdate(item.getName(), pchkInput.getState(this.binSensorId) ? OpenClosedType.CLOSED : OpenClosedType.OPEN);
				return true;
			}
			else if (item.getAcceptedDataTypes().contains(OnOffType.class)) {
				eventPublisher.postUpdate(item.getName(), pchkInput.getState(this.binSensorId) ? OnOffType.ON : OnOffType.OFF);
				return true;
			}
		}
		return false;		
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationVarStatus(ModStatusVar pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationLedsAndLogicOpsStatus(ModStatusLedsAndLogicOps pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationKeyLocksStatus(ModStatusKeyLocks pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
}
