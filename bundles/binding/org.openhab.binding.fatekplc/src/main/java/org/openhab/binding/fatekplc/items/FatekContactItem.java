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
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.simplify4u.jfatek.registers.Reg;
import org.simplify4u.jfatek.registers.RegValue;
import org.simplify4u.jfatek.registers.UnknownRegNameException;

/**
 * Contact item implementation.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public class FatekContactItem extends FatekPLCItem {

	private final boolean negate;

	FatekContactItem(Item item, List<String> confItems) throws BindingConfigParseException {
		super(item, confItems);

		if (confItems.size() != 1) {
			throw new BindingConfigParseException("Incorrect number of register for contact binding");
		}

		String regName = confItems.get(0);

		if (regName.startsWith("!") ) {
			regName = regName.substring(1);
			negate = true;
		} else {
			negate = false;
		}

		try {
			reg1 = Reg.parse(regName);
		} catch (UnknownRegNameException e) {
			throw new BindingConfigParseException(e.getMessage());
		}
	}

	@Override
	public State getState(Map<Reg, RegValue> response) {

		boolean value = response.get(reg1).boolValue();

		if (negate) {
			value = !value;
		}

		if (value) {
			return OpenClosedType.CLOSED;
		} else {
			return OpenClosedType.OPEN;
		}
	}

	@Override
	public String toString() {
		return toString(null);
	}
}
