/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.mappingtarget;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lcn.common.LcnAddr;
import org.openhab.binding.lcn.common.LcnAddrMod;
import org.openhab.binding.lcn.common.LcnDefs;
import org.openhab.binding.lcn.common.LcnDefs.RelVarRef;
import org.openhab.binding.lcn.common.PckGenerator;
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
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;

/**
 * Changes the value of a variable relative to its current value.
 * 
 * @author Tobias Jüttner
 */
public class VarRel extends TargetWithLcnAddr {
	
	/** Pattern to parse variable commands. */
	private static final Pattern PATTERN_VAR_REL =
		Pattern.compile("(?<varId>\\d+)\\.(?<value>-?\\d+(,\\d+)?)(?<modifier>.+)?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** Pattern to parse set-point variable commands. */
	private static final Pattern PATTERN_SETPOINT_REL =
		Pattern.compile("(?<regId>[12])(\\.(?<ref>(CURRENT)|(PROG)))?\\.(?<value>-?\\d+(,\\d+)?)(?<modifier>.+)?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** Pattern to parse threshold commands. */
	private static final Pattern PATTERN_THRS_REL =
		Pattern.compile("(?<registerId>[1234])\\.(?<thrsId>[12345])(\\.(?<ref>(CURRENT)|(PROG)))?\\.(?<value>-?\\d+(,\\d+)?)(?<modifier>.+)?",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** The target LCN variable. */
	private final LcnDefs.Var var;
	
	/** Reference-point. */
	private final LcnDefs.RelVarRef targetLcnType;
	
	/** The relative value to add / subtract. */
	private final LcnDefs.VarValue value;
	
	/** Forces the old commands before 170206. Required if target is an LCN group. */
	private final boolean forceOld;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN address
	 * @param var the target LCN variable
	 * @param targetLcnType the reference-point
	 * @param value the value
	 * @param forceOld true to force old command before 170206 (only required if target is an LCN group)
	 */
	VarRel(LcnAddr addr, LcnDefs.Var var, LcnDefs.RelVarRef targetLcnType, LcnDefs.VarValue value, boolean forceOld) {
		super(addr);
		this.var = var;
		this.targetLcnType = targetLcnType;
		this.value = value;
		this.forceOld = forceOld;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link VarRel} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, true);
		if (header != null) {
			try {
				Matcher matcher;				
				switch (header.getCmd().toUpperCase()) {
					case "VAR_REL":
					case "VAR_ADD":  // Same as "VAR_REL"
					case "VAR_SUB":  // "VAR_REL" inverted				
						if ((matcher = PATTERN_VAR_REL.matcher(header.getRestInput())).matches()) {
							double value = NumberFormat.getInstance(Locale.GERMANY).parse(matcher.group("value")).doubleValue();
							if (header.getCmd().equalsIgnoreCase("VAR_SUB")) {
								value = -value;
							}
							LcnDefs.VarUnit unit = matcher.group("modifier") == null ? LcnDefs.VarUnit.NATIVE : LcnDefs.VarUnit.parse(matcher.group("modifier"));
							return new VarRel(header.getAddr(), LcnDefs.Var.varIdToVar(Integer.parseInt(matcher.group("varId")) - 1),
								LcnDefs.RelVarRef.CURRENT, LcnDefs.VarValue.fromVarUnit(value, unit, false), false);
						}
						break;
					case "SETPOINT_REL":
					case "SETPOINT_ADD":  // Same as "SETPOINT_REL"
					case "SETPOINT_SUB":  // "SETPOINT_REL" inverted
						if ((matcher = PATTERN_SETPOINT_REL.matcher(header.getRestInput())).matches()) {
							LcnDefs.RelVarRef targetLcnType = (matcher.group("ref") != null && matcher.group("ref").equalsIgnoreCase("PROG")) ?
								RelVarRef.PROG : RelVarRef.CURRENT;
							double value = NumberFormat.getInstance(Locale.GERMANY).parse(matcher.group("value")).doubleValue();
							if (header.getCmd().equalsIgnoreCase("SETPOINT_SUB")) {
								value = -value;
							}
							LcnDefs.VarUnit unit = matcher.group("modifier") == null ? LcnDefs.VarUnit.NATIVE : LcnDefs.VarUnit.parse(matcher.group("modifier"));
							return new VarRel(header.getAddr(), LcnDefs.Var.setPointIdToVar(Integer.parseInt(matcher.group("regId")) - 1),
								targetLcnType, LcnDefs.VarValue.fromVarUnit(value, unit, false), false);
						}
						break;
					case "THRESHOLD_REL":
					case "THRESHOLD_ADD":  // Same as "THRESHOLD_REL"
					case "THRESHOLD_SUB":  // "THRESHOLD_REL" inverted
					case "THRESHOLD_REL_OLD":
					case "THRESHOLD_ADD_OLD":  // Same as "THRESHOLD_REL_OLD"
					case "THRESHOLD_SUB_OLD":  // "THRESHOLD_REL_OLD" inverted
						if ((matcher = PATTERN_THRS_REL.matcher(header.getRestInput())).matches()) {
							LcnDefs.RelVarRef targetLcnType = (matcher.group("ref") != null && matcher.group("ref").equalsIgnoreCase("PROG")) ?
								RelVarRef.PROG : RelVarRef.CURRENT;
							double value = NumberFormat.getInstance(Locale.GERMANY).parse(matcher.group("value")).doubleValue();
							if (header.getCmd().toUpperCase().contains("SUB")) {
								value = -value;
							}
							LcnDefs.VarUnit unit = matcher.group("modifier") == null ? LcnDefs.VarUnit.NATIVE : LcnDefs.VarUnit.parse(matcher.group("modifier"));
							return new VarRel(header.getAddr(),
								LcnDefs.Var.thrsIdToVar(Integer.parseInt(matcher.group("registerId")) - 1, Integer.parseInt(matcher.group("thrsId")) - 1),
								targetLcnType, LcnDefs.VarValue.fromVarUnit(value, unit, false),
								header.getCmd().toUpperCase().endsWith("_OLD"));
						}
						break;
				}
			} catch (ParseException ex) {
			} catch (IllegalArgumentException ex) { }
		}
		return null; 
	}
	
	/** {@inheritDoc} */
	@Override
	public void send(Connection conn, Item item, Command cmd) {
		boolean ack = !item.getAcceptedDataTypes().contains(PercentType.class) && !this.addr.isGroup();  // No ack. for dimmers (produces many of them)
		try {
			boolean is2013 = !this.forceOld;
			ModInfo info = null;
			if (!this.addr.isGroup()) {
				info = conn.getModInfo((LcnAddrMod)this.addr);
				if (info != null) {
					is2013 = info.getSwAge() >= 0x170206;
				}
			}
			conn.queue(this.addr, ack, PckGenerator.varRel(this.var, this.targetLcnType, this.value.toNative(), is2013));
			// Force a status update
			if (info != null && LcnDefs.Var.shouldPollStatusAfterCommand(this.var, is2013) && info.requestStatusVars.containsKey(this.var))
				info.requestStatusVars.get(this.var).nextRequestIn(ModInfo.STATUS_REQUEST_DELAY_AFTER_COMMAND_MSEC, System.nanoTime());
		}
		catch (IllegalArgumentException ex) {
			logger.warn(String.format("Variable of type %s does not support relative commands.", this.var));
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void register(Connection conn) {
		if (!this.addr.isGroup()) {
			long currTime = System.nanoTime();
			ModInfo info = conn.updateModuleData((LcnAddrMod)this.addr);
			if (!info.requestSwAge.isActive()) {
				info.requestSwAge.nextRequestIn(0, currTime);  // Firmware version is required
			}
			if (info.requestStatusVars.containsKey(this.var) && !info.requestStatusVars.get(this.var).isActive()) {
				info.requestStatusVars.get(this.var).nextRequestIn(0, currTime);
			}
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
