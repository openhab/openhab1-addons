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

import org.openhab.core.items.Item;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.simplify4u.jfatek.FatekException;
import org.simplify4u.jfatek.FatekPLC;
import org.simplify4u.jfatek.FatekWriteMixDataCmd;
import org.simplify4u.jfatek.io.FatekIOException;
import org.simplify4u.jfatek.registers.Reg;
import org.simplify4u.jfatek.registers.UnknownRegNameException;

/**
 * Rollershutter item implementation.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public class FatekRollershutterItem extends FatekPercentItem {

	private Reg reg2;
	private Reg reg3;

	public FatekRollershutterItem(Item item, List<String> confItems)
			throws BindingConfigParseException {

		super(item, confItems);

		if (confItems.size() != 3) {
			throw new BindingConfigParseException("Incorrect binding for rollershutter item");
		}

		try {
			reg1 = Reg.parse(confItems.get(0));
			reg2 = Reg.parse(confItems.get(1));
			reg3 = Reg.parse(confItems.get(2));
		} catch (UnknownRegNameException e) {
			throw new BindingConfigParseException(e.getMessage());
		}

		if (reg1.isDiscrete()) {
			throw new BindingConfigParseException("Please use data reg for rollershutter position");
		}
	}

	@Override
	public void command(FatekPLC fatekPLC, Command command) throws CommandException {

		boolean up;
		boolean down;

		if (StopMoveType.STOP.equals(command)) {
			up = false;
			down = false;
		} else if (UpDownType.UP.equals(command)) {
			up  = true;
			down = false;
		} else if (UpDownType.DOWN.equals(command)) {
			up  = false;
			down = true;
		} else {
			throw new UnsupportedCommandException(this, command);
		}

		try {
			FatekWriteMixDataCmd cmd = new FatekWriteMixDataCmd(fatekPLC);
			cmd.addReg(reg2, up);
			cmd.addReg(reg3, down);
			cmd.send();
		} catch (FatekIOException | FatekException e) {
			throw new CommandException(this, command, e);
		}
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(", regUp=").append(reg2);
		sb.append(", regDown=").append(reg3);

		return toString(sb);
	}
}
