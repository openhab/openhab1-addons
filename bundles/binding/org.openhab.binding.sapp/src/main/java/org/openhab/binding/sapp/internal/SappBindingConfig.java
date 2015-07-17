/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.core.binding.BindingConfig;

/**
 * This is a helper class holding binding specific configuration details
 * 
 * @author Paolo Denti
 * @since 1.8.0
 * 
 */
public abstract class SappBindingConfig implements BindingConfig {
	protected static final Map<SappAddressType, SappAddressRange> validAddresses = new HashMap<SappAddressType, SappAddressRange>() {
		protected static final long serialVersionUID = 592091386684476669L;
		{
			put(SappAddressType.ALARM, new SappAddressRange(1, 250));
			put(SappAddressType.INPUT, new SappAddressRange(1, 255));
			put(SappAddressType.OUTPUT, new SappAddressRange(1, 255));
			put(SappAddressType.VIRTUAL, new SappAddressRange(1, 2500));
		}
	};
	protected static final String[] validSubAddresses;

	static {
		validSubAddresses = new String[19];
		for (int i = 0; i < 16; i++) {
			validSubAddresses[i] = String.valueOf(i);
		}
		validSubAddresses[16] = "L";
		validSubAddresses[17] = "H";
		validSubAddresses[18] = "*";
	}

	private String itemName;

	public SappBindingConfig(String itemName)  {

		this.itemName = itemName;
	}

	public String getItemName() {
		return itemName;
	}
}
