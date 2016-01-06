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
 * Status of a variable received from an LCN module.
 * 
 * @author Tobias Jüttner
 */
public class ModStatusVar extends Mod {
	
	/** Variable type as received from input (might be "unknown"). */
	private final LcnDefs.Var origVar;
	
	/** Real variable type. */
	private LcnDefs.Var var;
	
	/** Value of variable. */
	private final LcnDefs.VarValue value;
	
	/**
	 * Constructor.
	 * 
	 * @param physicalSourceAddr the source address
	 * @param origVar the source variable as reported by the LCN module
	 * @param value the variable's current value
	 */
	ModStatusVar(LcnAddrMod physicalSourceAddr, LcnDefs.Var origVar, LcnDefs.VarValue value) {
		super(physicalSourceAddr);
		this.origVar = origVar;
		this.value = value;
	}
	
	/**
	 * Gets the variable's real type.
	 * 
	 * @return the real type
	 */
	public LcnDefs.Var getVar() {
		return this.var;
	}
	
	/**
	 * Gets the variable's value.
	 * 
	 * @return the value
	 */
	public LcnDefs.VarValue getValue() {
		return this.value;
	}
	
	/**
	 * Tries to parse the given input received from LCN-PCHK.
	 * 
	 * @param input the input
	 * @return list of {@link ModStatusVar} (might be empty, but not null}
	 */
	static Collection<Input> tryParseInput(String input) {
		LinkedList<Input> ret = new LinkedList<Input>();
		try {
			Matcher matcher;
			if ((matcher = PckParser.PATTERN_STATUS_VAR.matcher(input)).matches()) {
				ret.add(new ModStatusVar(
					new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("modId"))),
					LcnDefs.Var.varIdToVar(Integer.parseInt(matcher.group("id")) - 1),
					LcnDefs.VarValue.fromNative(Integer.parseInt(matcher.group("value")))));
			}
			else if ((matcher = PckParser.PATTERN_STATUS_SETVAR.matcher(input)).matches()) {
				ret.add(new ModStatusVar(
					new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("modId"))),
					LcnDefs.Var.setPointIdToVar(Integer.parseInt(matcher.group("id")) - 1),
					LcnDefs.VarValue.fromNative(Integer.parseInt(matcher.group("value")))));
			}
			else if ((matcher = PckParser.PATTERN_STATUS_THRS.matcher(input)).matches()) {
				ret.add(new ModStatusVar(
					new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("modId"))),
					LcnDefs.Var.thrsIdToVar(Integer.parseInt(matcher.group("registerId")) - 1, Integer.parseInt(matcher.group("thrsId")) - 1),
					LcnDefs.VarValue.fromNative(Integer.parseInt(matcher.group("value")))));
			}
			else if ((matcher = PckParser.PATTERN_STATUS_S0INPUT.matcher(input)).matches()) {
				ret.add(new ModStatusVar(
					new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("modId"))),
					LcnDefs.Var.s0IdToVar(Integer.parseInt(matcher.group("id")) - 1),
					LcnDefs.VarValue.fromNative(Integer.parseInt(matcher.group("value")))));
			}			
			else if ((matcher = PckParser.PATTERN_VAR_GENERIC.matcher(input)).matches()) {
				ret.add(new ModStatusVar(
					new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("modId"))),
					LcnDefs.Var.UNKNOWN,
					LcnDefs.VarValue.fromNative(Integer.parseInt(matcher.group("value")))));
			}
			else if ((matcher = PckParser.PATTERN_THRS5.matcher(input)).matches()) {
				for (int thrsId = 0; thrsId < 5; ++thrsId) {
					ret.add(new ModStatusVar(
						new LcnAddrMod(Integer.parseInt(matcher.group("segId")), Integer.parseInt(matcher.group("modId"))),
						LcnDefs.Var.thrsIdToVar(0, thrsId), LcnDefs.VarValue.fromNative(Integer.parseInt(matcher.group(String.format("value%d", thrsId + 1))))));
				}
			}
		} catch (IllegalArgumentException ex) { }
		return ret;
	}
	
	/**
	 * Resolves "unknown" variable types and notifies the connection about the received variable status.
	 * {@inheritDoc}
	 */
	@Override
	public void process(Connection conn) {
		super.process(conn);  // Will replace source segment 0 with the local segment id
		ModInfo info = conn.getModInfo(this.logicalSourceAddr);
		if (info != null) {
			this.var = this.origVar == LcnDefs.Var.UNKNOWN ? info.getLastRequestedVarWithoutTypeInResponse() : this.origVar;
			if (this.var != LcnDefs.Var.UNKNOWN) {
				if (info.getLastRequestedVarWithoutTypeInResponse() == this.var) {
					info.setLastRequestedVarWithoutTypeInResponse(LcnDefs.Var.UNKNOWN);  // Reset
				}
				if (info.requestStatusVars.containsKey(this.var)) {
					info.requestStatusVars.get(this.var).onResponseReceived();
				}
			}
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean tryVisualization(Input.VisualizationVisitor handler, Connection conn, Command cmd, Item item, EventPublisher eventPublisher) {
		return handler.visualizationVarStatus(this, cmd, item, eventPublisher);
	}
	
}
