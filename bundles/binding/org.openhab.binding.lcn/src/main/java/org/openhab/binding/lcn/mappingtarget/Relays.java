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

import org.openhab.binding.lcn.common.LcnAddr;
import org.openhab.binding.lcn.common.LcnAddrMod;
import org.openhab.binding.lcn.common.LcnDefs;
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
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;

/**
 * Controls one or multiple relays.
 * Can also visualize single relay states.
 * 
 * @author Tobias Jüttner
 */
public class Relays extends TargetWithLcnAddr {
	
	/** Pattern to parse relays commands. */ 
	private static final Pattern PATTERN_RELAYS =
		Pattern.compile("(?<states>[10T-]{8})",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** 8 target relay modifiers. */
	private final LcnDefs.RelayStateModifier[] states;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN address
	 * @param states the 8 relay modifiers
	 */
	Relays(LcnAddr addr, LcnDefs.RelayStateModifier[] states) {
		super(addr);
		this.states = states;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link Relays} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, true);
		if (header != null) {
			Matcher matcher;
			switch (header.getCmd().toUpperCase()) {
				case "RELAYS":
					if ((matcher = PATTERN_RELAYS.matcher(header.getRestInput())).matches()) {
						LcnDefs.RelayStateModifier[] states = new LcnDefs.RelayStateModifier[8];						
						for (int i = 0; i < 8; ++i) {
							switch (matcher.group("states").toUpperCase().charAt(i)) {
								case '1': states[i] = LcnDefs.RelayStateModifier.ON; break;
								case '0': states[i] = LcnDefs.RelayStateModifier.OFF; break;
								case 'T': states[i] = LcnDefs.RelayStateModifier.TOGGLE; break;
								case '-': states[i] = LcnDefs.RelayStateModifier.NOCHANGE; break;
								default: throw new Error();
							}
						}
						return new Relays(header.getAddr(), states);
					}
					break;
			}
		}
		return null; 
	}
	
	/** {@inheritDoc} */
	@Override
	public void send(Connection conn, Item item, Command cmd) {
		conn.queue(this.addr, !this.addr.isGroup(), PckGenerator.controlRelays(this.states));
	}
	
	/** {@inheritDoc} */
	@Override
	public void register(Connection conn) {
		if (!this.addr.isGroup()) {
			ModInfo info = conn.updateModuleData((LcnAddrMod)this.addr);
			if (!info.requestStatusRelays.isActive()) {
				info.requestStatusRelays.nextRequestIn(0, System.nanoTime());
			}
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationHandleOutputStatus(ModStatusOutput pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/**
	 * Visualization for {@link OnOffType}.
	 * {@inheritDoc}
	 */
	@Override
	public boolean visualizationHandleRelaysStatus(ModStatusRelays pchkInput, Command cmd, Item item, EventPublisher eventPublisher) {
		// We are actually not meant to visualize anything.
		// But (just in case) someone is really lazy in doing the item-definitions, we try to be helpful by implementing some ON/OFF logic.
		if (pchkInput.getLogicalSourceAddr().equals(this.addr) && item.getAcceptedDataTypes().contains(OnOffType.class)) {
			for (int i = 0; i < 8; ++i) {
				if (this.states[i] == LcnDefs.RelayStateModifier.ON || this.states[i] == LcnDefs.RelayStateModifier.OFF) {
					// Only update if the state we are bound to is equal to the reported one.
					if ((this.states[i] == LcnDefs.RelayStateModifier.ON) == pchkInput.getState(i)) {
						eventPublisher.postUpdate(item.getName(), cmd == OnOffType.ON ? OnOffType.ON : OnOffType.OFF);
						return true;
					}
					break;
				}
			}
		}
		return false;
	}
	
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
