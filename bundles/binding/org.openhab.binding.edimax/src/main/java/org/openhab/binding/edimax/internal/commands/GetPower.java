/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.edimax.internal.commands;

import java.math.BigDecimal;
import java.util.List;

/**
 * Command to retrieve the power consumption in Watt.
 * 
 * @author Heinz
 */
public class GetPower extends AbstractCMDNowPowerCommand<BigDecimal> {

	@Override
	protected List<String> getPath() {
		List<String> list = super.getPath();
		list.add("Device.System.Power.NowPower");
		return list;
	}

}
