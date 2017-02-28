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
import java.util.List;
import java.util.Map;

import org.openhab.core.items.Item;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.simplify4u.jfatek.registers.Reg;
import org.simplify4u.jfatek.registers.RegValue;

/**
 * Common implementation for Percent items.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public abstract class FatekPercentItem extends FatekPLCItem {

	protected BigDecimal factor;

	public FatekPercentItem(Item item, List<String> confItems) throws BindingConfigParseException {
		super(item, confItems);

		String sFactor = getParamsFromConf(confItems, "factor", null);
		if (sFactor != null) {
			factor = new BigDecimal(sFactor);
		}
	}

	@Override
	public State getState(Map<Reg, RegValue> response) {

		int v = response.get(reg1).intValueUnsigned();

		if (factor != null) {
			v = new BigDecimal(v).multiply(factor).intValue();
		}

		v = Math.min(v, 100);

		if (v == 0) {
			return PercentType.ZERO;
		}

		if (v == 100) {
			return PercentType.HUNDRED;
		}

		return new PercentType(v);
	}

}
