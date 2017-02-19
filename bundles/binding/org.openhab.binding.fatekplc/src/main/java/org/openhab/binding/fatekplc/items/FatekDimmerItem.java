/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fatekplc.items;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

import org.openhab.core.items.Item;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.simplify4u.jfatek.FatekException;
import org.simplify4u.jfatek.FatekPLC;
import org.simplify4u.jfatek.FatekReadMixDataCmd;
import org.simplify4u.jfatek.FatekWriteMixDataCmd;
import org.simplify4u.jfatek.io.FatekIOException;
import org.simplify4u.jfatek.registers.Reg;
import org.simplify4u.jfatek.registers.RegValue;
import org.simplify4u.jfatek.registers.UnknownRegNameException;

/**
 * Dimmer item implementation.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public class FatekDimmerItem extends FatekPercentItem {

	private int step;

	public FatekDimmerItem(Item item, List<String> confItems) throws BindingConfigParseException {

		super(item, confItems);

		step = getParamsFromConfAsInt(confItems, "step", 5);

		if (confItems.size() != 1) {
			throw new BindingConfigParseException("Incorrect binding for dimmer item");
		}

		try {
			reg1 = Reg.parse(confItems.get(0));
		} catch (UnknownRegNameException e) {
			throw new BindingConfigParseException(e.getMessage());
		}

		if (reg1.isDiscrete()) {
			throw new BindingConfigParseException("Please use data reg for dimmer item");
		}
	}

	@Override
	public void command(FatekPLC fatekPLC, Command command) throws CommandException {

		try {
			int val;
			if (OnOffType.ON.equals(command)) {
				val = 100;
			} else if (OnOffType.OFF.equals(command)) {
				val = 0;
			} else if (command instanceof IncreaseDecreaseType) {
				val = valueForIncreaseDecrease(fatekPLC, command);
			} else if (command instanceof PercentType) {
				val = ((PercentType) command).intValue();
			} else {
				throw new UnsupportedCommandException(this, command);
			}

			if (val >= 0) {
				if (factor != null) {
					val = new BigDecimal(val).divide(factor, RoundingMode.HALF_UP).intValue();
				}
				new FatekWriteMixDataCmd(fatekPLC).addReg(reg1, val).send();
			}

		} catch (FatekIOException | FatekException e) {
			throw new CommandException(this, command, e);
		}
	}

	private int valueForIncreaseDecrease(FatekPLC fatekPLC, Command command) throws FatekIOException, FatekException {

		// read current value from PLC
		Map<Reg, RegValue> currentPlcValues = new FatekReadMixDataCmd(fatekPLC, reg1).send();
		RegValue currRegValue = currentPlcValues.get(reg1);

		long plcVal = currRegValue.longValueUnsigned();

		if (factor != null) {
			plcVal =  new BigDecimal(plcVal).multiply(factor).longValue();
		}

		if (IncreaseDecreaseType.INCREASE.equals(command)) {
			plcVal += step;
		}

		if (IncreaseDecreaseType.DECREASE.equals(command)) {
			plcVal -= step;
		}

		plcVal = Math.max(plcVal, 0);
		plcVal = Math.min(plcVal, 100);

		RegValue val = RegValue.getForReg(reg1, plcVal);

		if (currRegValue.equals(val)) {
			return -1;
		}
		return (int) plcVal;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(", step=").append(step);
		return toString(sb);
	}
}
