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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.fatekplc.internal.FatekPLCActivator;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Number item implementation.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public class FatekNumberItem extends FatekPLCItem {

	private static final Pattern TRANSFORMATION_CONFIG_PATTERN = Pattern.compile("([A-Z]+)\\((.+)\\)");

	private static final Logger logger = LoggerFactory.getLogger(FatekNumberItem.class);

	private boolean signed = true;
	private boolean floatValue = false;
	private String transType = null;
	private String transFunc = null;

	private BigDecimal factor = null;

	public FatekNumberItem(Item item, List<String> confItems) throws BindingConfigParseException {

		super(item, confItems);

		String sFactor = getParamsFromConf(confItems, "factor", null);
		if (sFactor != null) {
			factor = new BigDecimal(sFactor);
		}

		// try to find transformation configuration
		parseForTransformation(confItems);

		if (confItems.size() != 1) {
			throw new BindingConfigParseException("Incorrect binding for number item");
		}

		String regName = confItems.get(0);

		if (regName.startsWith("+")) {
			signed = false;
			regName = regName.substring(1);
		}

		if (regName.endsWith("F")) {
			floatValue = true;
			regName = regName.substring(0, regName.length() - 1);
		}

		try {
			reg1 = Reg.parse(regName);
		} catch (UnknownRegNameException e) {
			throw new BindingConfigParseException(e.getMessage());
		}
	}

	/**
	 * Parse transformation config for item.
	 *
	 * @param confItems
	 *            config list
	 */
	private void parseForTransformation(List<String> confItems) {

		for (Iterator<String> iterator = confItems.iterator(); iterator.hasNext();) {

			String conf = iterator.next();

			Matcher matcher = TRANSFORMATION_CONFIG_PATTERN.matcher(conf);
			if (matcher.find()) {
				transType = matcher.group(1);
				transFunc = matcher.group(2);
				iterator.remove();
				return;
			}
		}
	}

	@Override
	public State getState(Map<Reg, RegValue> response) {


		BigDecimal val;

		if (floatValue) {
			val = new BigDecimal(response.get(reg1).floatValue());
		} else {
			long longVal;
			if (signed) {
				longVal = response.get(reg1).longValue();
			} else {
				longVal = response.get(reg1).longValueUnsigned();
			}
			val = new BigDecimal(longVal);
		}


		if (factor != null) {
			val = factor.multiply(val);
		}

		if (transType != null) {
			return getStateWithTransformation(val);
		} else {
			return new DecimalType(val);
		}
	}

	private State getStateWithTransformation(BigDecimal val) {

		TransformationService transformationService = TransformationHelper.getTransformationService(
				FatekPLCActivator.getContext(), transType);

		String strVal = String.valueOf(val);
		String transOut;
		if (transformationService != null) {
			try {
				transOut = transformationService.transform(transFunc, strVal);
				logger.debug("transOut={}", transOut);
			} catch (TransformationException e) {
				transOut = null;
				logger.warn("Transformation error: {}", e.getMessage());
			}
		} else {
			transOut = null;
			logger.warn("No transformation service for: {}", transType);
		}

		if (transOut != null && !"null".equals(transOut)) {
			strVal = transOut;
		}

		return new DecimalType(strVal);
	}

	@Override
	public void command(FatekPLC fatekPLC, Command command) throws CommandException {

		if (command instanceof DecimalType) {

			BigDecimal val = ((DecimalType) command).toBigDecimal();

			if (factor != null) {
				val = val.divide(factor);
			}

			try {
				FatekWriteMixDataCmd cmd = new FatekWriteMixDataCmd(fatekPLC);
				if (floatValue) {
					cmd.addReg(reg1, val.floatValue());
				} else {
					cmd.addReg(reg1, val.longValue());
				}
				cmd.send();
			} catch (FatekIOException | FatekException e) {
				throw new CommandException(this, command, e);
			}
		} else {
			throw new UnsupportedCommandException(this, command);
		}
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		if (!signed) {
			sb.append(", unsign(+)");
		}

		if (floatValue) {
			sb.append(", float");
		}

		if (factor != null) {
			sb.append(", factor=").append(factor);
		}

		if (transType != null) {
			sb.append(", trans=").append(transType).append("(").append(transFunc).append(")");
		}

		return toString(sb);
	}
}
