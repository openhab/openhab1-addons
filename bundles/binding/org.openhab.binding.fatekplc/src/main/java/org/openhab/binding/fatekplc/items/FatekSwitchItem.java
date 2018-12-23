/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fatekplc.items;

import java.util.List;
import java.util.Map;

import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.simplify4u.jfatek.FatekException;
import org.simplify4u.jfatek.FatekPLC;
import org.simplify4u.jfatek.FatekWriteMixDataCmd;
import org.simplify4u.jfatek.io.FatekIOException;
import org.simplify4u.jfatek.registers.Reg;
import org.simplify4u.jfatek.registers.RegValue;
import org.simplify4u.jfatek.registers.UnknownRegNameException;

/**
 * Switch item implementation.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public class FatekSwitchItem extends FatekPLCItem {

	private final boolean negate1;
	private boolean negate2;
	private Reg reg2;

	FatekSwitchItem(Item item, List<String> confItems) throws BindingConfigParseException {

		super(item, confItems);

		if (confItems.size() < 1 || confItems.size() > 2) {
			throw new BindingConfigParseException("Incorrect binding for switch item");
		}

		String regName = confItems.get(0);

		if (regName.startsWith("!") ) {
			regName = regName.substring(1);
			negate1 = true;
		} else {
			negate1 = false;
		}

		try {
			reg1 = Reg.parse(regName);
		} catch (UnknownRegNameException e) {
			throw new BindingConfigParseException(e.getMessage());
		}

		if (confItems.size() == 2) {
			regName = confItems.get(1);

			if (regName.startsWith("!") ) {
				regName = regName.substring(1);
				negate2 = true;
			} else {
				negate2 = false;
			}

			try {
				reg2 = Reg.parse(regName);
			} catch (UnknownRegNameException e) {
				throw new BindingConfigParseException(e.getMessage());
			}
		}
	}

	@Override
	public State getState(Map<Reg, RegValue> response) {

		boolean value = response.get(reg1).boolValue();

		if (negate1) {
			value = !value;
		}

		if (value) {
			return OnOffType.ON;
		} else {
			return OnOffType.OFF;
		}
	}

	@Override
	public void command(FatekPLC fatekPLC, Command command) throws CommandException {

		boolean value;

		if (OnOffType.ON.equals(command)) {
			value = true;
		} else if (OnOffType.OFF.equals(command)) {
			value = false;
		} else {
			throw new UnsupportedCommandException(this, command);
		}

		Reg reg;

		if (reg2 == null) {
			if (negate1) {
				value = !value;
			}
			reg = reg1;
		} else {
			if (negate2){
				value = !value;
			}
			reg = reg2;
		}

		try {
			new FatekWriteMixDataCmd(fatekPLC).addReg(reg, value).send();
		} catch (FatekIOException | FatekException e) {
			throw new CommandException(this, command, e);
		}
	}

	@Override
	public String toString() {

		StringBuilder sb = null;

		if (reg2 != null) {
			sb = new StringBuilder();
			sb.append(", reg2=").append(reg2);
		}

		return super.toString(sb);
	}
}
