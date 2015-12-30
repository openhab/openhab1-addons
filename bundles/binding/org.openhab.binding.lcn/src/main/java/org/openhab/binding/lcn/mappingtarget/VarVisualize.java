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
import org.openhab.binding.lcn.common.LcnDefs;
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
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;

/**
 * Visualizes the current value of a variable.
 * 
 * @author Tobias Jüttner
 */
public class VarVisualize extends TargetWithLcnAddr {
	
	/** Pattern to parse variable value visualizations. */ 
	private static final Pattern PATTERN_VAR_VALUE =
		Pattern.compile("(?<varId>\\d+)(\\.(?<unit>.*))?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** Pattern to parse set-point value visualizations. */ 
	private static final Pattern PATTERN_SETPOINT_VALUE =
		Pattern.compile("(?<regId>\\d+)(\\.(?<unit>.*))?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** Pattern to parse threshold value visualizations. */ 
	private static final Pattern PATTERN_THRESHOLD_VALUE =
		Pattern.compile("(?<registerId>\\d+)\\.(?<thrsId>\\d+)(\\.(?<unit>.*))?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** Pattern to parse S0-input value visualizations. */ 
	private static final Pattern PATTERN_S0INPUT_VALUE =
		Pattern.compile("(?<s0Id>\\d+)(\\.(?<unit>.*))?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

	/** The target variable to visualize. */
	private final LcnDefs.Var var;
	
	/** The unit to use for printing the variable's value. */
	private final LcnDefs.VarUnit unit;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN module
	 * @param var the LCN variable to visualize
	 * @param unit the {@link LcnDefs.VarUnit}
	 */
	VarVisualize(LcnAddrMod addr, LcnDefs.Var var, LcnDefs.VarUnit unit) {
		super(addr);
		this.var = var;
		this.unit = unit;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link VarVisualize} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, false);
		if (header != null) {
			Matcher matcher;
			try {
				switch (header.getCmd().toUpperCase()) {
					case "VAR_VALUE":
						if ((matcher = PATTERN_VAR_VALUE.matcher(header.getRestInput())).matches()) {
							LcnDefs.Var var = LcnDefs.Var.varIdToVar(Integer.parseInt(matcher.group("varId")) - 1);
							LcnDefs.VarUnit unit = matcher.group("unit") != null ? LcnDefs.VarUnit.parse(matcher.group("unit")) : LcnDefs.VarUnit.NATIVE;
							return new VarVisualize((LcnAddrMod)header.getAddr(), var, unit);
						}
						break;
					case "SETPOINT_VALUE":
						if ((matcher = PATTERN_SETPOINT_VALUE.matcher(header.getRestInput())).matches()) {
							LcnDefs.Var var = LcnDefs.Var.setPointIdToVar(Integer.parseInt(matcher.group("regId")) - 1);
							LcnDefs.VarUnit unit = matcher.group("unit") != null ? LcnDefs.VarUnit.parse(matcher.group("unit")) : LcnDefs.VarUnit.NATIVE;
							return new VarVisualize((LcnAddrMod)header.getAddr(), var, unit);
						}
						break;
					case "THRESHOLD_VALUE":
						if ((matcher = PATTERN_THRESHOLD_VALUE.matcher(header.getRestInput())).matches()) {
							LcnDefs.Var var = LcnDefs.Var.thrsIdToVar(Integer.parseInt(matcher.group("registerId")) - 1,
								Integer.parseInt(matcher.group("thrsId")) - 1);
							LcnDefs.VarUnit unit = matcher.group("unit") != null ? LcnDefs.VarUnit.parse(matcher.group("unit")) : LcnDefs.VarUnit.NATIVE;
							return new VarVisualize((LcnAddrMod)header.getAddr(), var, unit);							
						}
						break;
					case "S0_VALUE":
						if ((matcher = PATTERN_S0INPUT_VALUE.matcher(header.getRestInput())).matches()) {
							LcnDefs.Var var = LcnDefs.Var.s0IdToVar(Integer.parseInt(matcher.group("s0Id")) - 1);
							LcnDefs.VarUnit unit = matcher.group("unit") != null ? LcnDefs.VarUnit.parse(matcher.group("unit")) : LcnDefs.VarUnit.NATIVE;
							return new VarVisualize((LcnAddrMod)header.getAddr(), var, unit);
						}
						break;
				}
			} catch (IllegalArgumentException ex) { }
		}
		return null; 
	}

	/** {@inheritDoc} */
	@Override
	public void send(Connection conn, Item item, Command cmd) { }
	
	/** {@inheritDoc} */
	@Override
	public void register(Connection conn) {
		long currTime = System.nanoTime();
		ModInfo info = conn.updateModuleData((LcnAddrMod)this.addr);
		if (!info.requestSwAge.isActive()) {
			info.requestSwAge.nextRequestIn(0, currTime);  // Firmware version is required
		}
		if (info.requestStatusVars.containsKey(this.var) && !info.requestStatusVars.get(this.var).isActive()) {
			info.requestStatusVars.get(this.var).nextRequestIn(0, currTime);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationHandleOutputStatus(ModStatusOutput pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationHandleRelaysStatus(ModStatusRelays pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationBinSensorsStatus(ModStatusBinSensors pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/**
	 * Visualization for {@link StringType} and {@link DecimalType}.
	 * {@inheritDoc}
	 */
	@Override
	public boolean visualizationVarStatus(ModStatusVar pchkInput, Command cmd, Item item, EventPublisher eventPublisher) {
		if (pchkInput.getLogicalSourceAddr().equals(this.addr) && pchkInput.getVar() == this.var) {
			if (item.getAcceptedDataTypes().contains(StringType.class)) {
				String value = pchkInput.getValue().toVarUnitString(this.unit, LcnDefs.Var.isLockableRegulatorSource(this.var), LcnDefs.Var.useLcnSpecialValues(this.var));
				eventPublisher.postUpdate(item.getName(), new StringType(value));
				return true;
			}
			else if (item.getAcceptedDataTypes().contains(DecimalType.class)) {
				eventPublisher.postUpdate(item.getName(), new DecimalType(pchkInput.getValue().toVarUnit(this.unit, LcnDefs.Var.isLockableRegulatorSource(this.var))));
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean visualizationLedsAndLogicOpsStatus(ModStatusLedsAndLogicOps pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationKeyLocksStatus(ModStatusKeyLocks pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
}
