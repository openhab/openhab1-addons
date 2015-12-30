/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.mappingtarget;

import java.text.DecimalFormat;
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
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;

/**
 * Visualizes an output-port status.
 * 
 * @author Tobias Jüttner
 */
public class OutputVisualize extends TargetWithLcnAddr {
	
	/** Pattern to parse output-port visualizations. */ 
	private static final Pattern PATTERN_OUTPUT_STATUS =
		Pattern.compile("(?<outputId>[1234])",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** The target output-port to visualize (0..3). */
	private final int outputId;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN module
	 * @param outputId 0..3
	 */
	OutputVisualize(LcnAddrMod addr, int outputId) {
		super(addr);
		this.outputId = outputId;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link OutputVisualize} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, false);
		if (header != null) {
			Matcher matcher;
			switch (header.getCmd().toUpperCase()) {
				case "OUTPUT_STATUS":
					if ((matcher = PATTERN_OUTPUT_STATUS.matcher(header.getRestInput())).matches()) {
						return new OutputVisualize((LcnAddrMod)header.getAddr(), Integer.parseInt(matcher.group("outputId")) - 1);
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
		if (!info.requestStatusOutputs.get(this.outputId).isActive()) {
			info.requestStatusOutputs.get(this.outputId).nextRequestIn(0, System.nanoTime());
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationHandleOutputStatus(ModStatusOutput pchkInput, Command cmd, Item item, EventPublisher eventPublisher) {
		if (pchkInput.getLogicalSourceAddr().equals(this.addr) && pchkInput.getOutputId() == this.outputId) {
			if (item.getAcceptedDataTypes().contains(StringType.class)) {
				String value = new DecimalFormat("0.#").format(pchkInput.getPercent());
				eventPublisher.postUpdate(item.getName(), new StringType(value));
				return true;
			}
			else if (item.getAcceptedDataTypes().contains(PercentType.class)) {
				eventPublisher.postUpdate(item.getName(), new PercentType((int)Math.round(pchkInput.getPercent())));
				return true;
			}
			else if (item.getAcceptedDataTypes().contains(DecimalType.class)) {
				eventPublisher.postUpdate(item.getName(), new DecimalType(pchkInput.getPercent()));
				return true;
			}
			else if (item.getAcceptedDataTypes().contains(OnOffType.class)) {
				eventPublisher.postUpdate(item.getName(), pchkInput.getPercent() != 0 ? OnOffType.ON : OnOffType.OFF);
				return true;
			}
		}
		return false;
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationHandleRelaysStatus(ModStatusRelays pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationBinSensorsStatus(ModStatusBinSensors pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
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
