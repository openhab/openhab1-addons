/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fatekplc.items;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
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
 * Color item implementation.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 *
 */
public class FatekColorItem extends FatekPLCItem {

	private Reg reg2;
	private Reg reg3;
	private Reg reg4;
	private boolean isColorRGB;

	private final int step;

	public FatekColorItem(Item item, List<String> confItems)
			throws BindingConfigParseException {

		super(item, confItems);

		step = getParamsFromConfAsInt(confItems, "step", 5);

		if (confItems.size() < 3 || confItems.size() > 5) {
			throw new BindingConfigParseException(
					"Incorrect binding for color item - should be: plcName:R1:R2:R3[:R4][:RGB|HSB]");
		}

		try {
			reg1 = Reg.parse(confItems.get(0));
			reg2 = Reg.parse(confItems.get(1));
			reg3 = Reg.parse(confItems.get(2));

		} catch (UnknownRegNameException e) {
			throw new BindingConfigParseException(e.getMessage());
		}

		if (confItems.size() == 4) {
			try {
				reg4 = Reg.parse(confItems.get(3));
			} catch (UnknownRegNameException e) {
				// try color mode
				initColorMode(confItems.get(3));
			}
		}

		if (confItems.size() == 5) {
			try {
				reg4 = Reg.parse(confItems.get(3));
			} catch (UnknownRegNameException e) {
				throw new BindingConfigParseException(e.getMessage());
			}
			initColorMode(confItems.get(4));
		}

		if (reg1.isDiscrete() || reg2.isDiscrete() || reg3.isDiscrete()) {
			throw new BindingConfigParseException(
					"Please use data reg for color item");
		}
	}

	private void initColorMode(String colorMode)
			throws BindingConfigParseException {

		if ("RGB".equalsIgnoreCase(colorMode)) {
			isColorRGB = true;
		} else if ("HSB".equalsIgnoreCase(colorMode)) {
			isColorRGB = false;
		} else {
			throw new BindingConfigParseException("Invalid color mode value");
		}

	}

	@Override
	public Collection<? extends Reg> getRegs() {

		if (reg4 != null) {
			return Arrays.asList(reg1, reg2, reg3, reg4);
		} else {
			return Arrays.asList(reg1, reg2, reg3);
		}
	}

	@Override
	public State getState(Map<Reg, RegValue> response) {

		if (reg4 != null) {
			boolean isOn = response.get(reg4).boolValue();
			if (!isOn) {
				return HSBType.BLACK;
			}
		}

		return reg2HSB(response.get(reg1), response.get(reg2),
				response.get(reg3));
	}

	@Override
	public void command(FatekPLC fatekPLC, Command command)
			throws CommandException {

		try {

			final HSBType val;

			if (command instanceof OnOffType) {
				val = valueForOnOff(fatekPLC, command);
			} else if (command instanceof IncreaseDecreaseType) {
				val = valueForIncreaseDecrease(fatekPLC, command);
			} else if (command instanceof HSBType) {
				val = (HSBType) command;
			} else {
				throw new UnsupportedCommandException(this, command);
			}

			if (val != null) {
				int v1;
				int v2;
				int v3;
				if (isColorRGB) {
					Color c = val.toColor();
					v1 = c.getRed();
					v2 = c.getGreen();
					v3 = c.getBlue();
				} else {
					v1 = val.getHue().intValue();
					v2 = val.getSaturation().intValue();
					v3 = val.getBrightness().intValue();
				}

				FatekWriteMixDataCmd cmd = new FatekWriteMixDataCmd(fatekPLC);
				cmd.addReg(reg1, v1);
				cmd.addReg(reg2, v2);
				cmd.addReg(reg3, v3);
				cmd.send();
			}

		} catch (FatekIOException | FatekException e) {
			throw new CommandException(this, command, e);
		}
	}

	private HSBType reg2HSB(RegValue r1, RegValue r2, RegValue r3) {

		int v1 = r1.intValueUnsigned();
		int v2 = r2.intValueUnsigned();
		int v3 = r3.intValueUnsigned();

		if (isColorRGB) {
			v1 = Math.min(v1, 255);
			v2 = Math.min(v2, 255);
			v3 = Math.min(v3, 255);

			return new HSBType(new Color(v1, v2, v3));
		} else {
			v1 = Math.min(v1, 360);
			v2 = Math.min(v2, 100);
			v3 = Math.min(v3, 100);

			return new HSBType(new DecimalType(v1), new PercentType(v2),
					new PercentType(v3));
		}
	}

	private HSBType valueForOnOff(FatekPLC fatekPLC, Command command)
			throws CommandException {

		HSBType val = null;
		try {
			if (OnOffType.ON.equals(command)) {
				if (reg4 != null) {
					new FatekWriteMixDataCmd(fatekPLC).addReg(reg4, true).send();
				} else {
					val = HSBType.WHITE;
				}
			} else if (OnOffType.OFF.equals(command)) {
				if (reg4 != null) {
					new FatekWriteMixDataCmd(fatekPLC).addReg(reg4, false).send();
				} else {
					val = HSBType.BLACK;
				}
			}
		} catch (FatekIOException | FatekException e) {
			throw new CommandException(this, command, e);
		}
		return val;
	}

	private HSBType valueForIncreaseDecrease(FatekPLC fatekPLC, Command command)
			throws CommandException {

		HSBType val = null;
		try {

			// first read current state
			Map<Reg, RegValue> regVal = new FatekReadMixDataCmd(fatekPLC, reg1,
					reg2, reg3).send();
			HSBType currentVal = reg2HSB(regVal.get(reg1), regVal.get(reg1),
					regVal.get(reg3));

			int b = currentVal.getBrightness().intValue();
			if (IncreaseDecreaseType.INCREASE.equals(command)) {
				b = Math.min(b + step, 100);
			} else if (IncreaseDecreaseType.DECREASE.equals(command)) {
				b = Math.max(b - step, 0);
			} else {
				throw new CommandException(this, command,
						"Unknown IncreaseDecrease type");
			}

			if (b != currentVal.getBrightness().intValue()) {
				val = new HSBType(currentVal.getHue(),
						currentVal.getSaturation(), new PercentType(b));
			}

		} catch (FatekIOException | FatekException e) {
			throw new CommandException(this, command, e);
		}
		return val;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("[");
		sb.append("slave=").append(getSlaveName());
		sb.append(", name=").append(getItemName());
		sb.append(", step=").append(step);

		if (isColorRGB) {
			sb.append(", R=").append(reg1);
			sb.append(", G=").append(reg2);
			sb.append(", B=").append(reg3);
		} else {
			sb.append(", H=").append(reg1);
			sb.append(", S=").append(reg2);
			sb.append(", B=").append(reg3);
		}
		sb.append("]");

		return sb.toString();
	}
}
