/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal.configs;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.sapp.internal.model.SappAddressRange;
import org.openhab.binding.sapp.internal.model.SappAddressType;
import org.openhab.core.binding.BindingConfig;

/**
 * This is a helper class holding binding generic configuration details
 * 
 * @author Paolo Denti
 * @since 1.8.0
 * 
 */
public abstract class SappBindingConfig implements BindingConfig {

	/**
	 * valid address ranges for each address type
	 */
	protected static final Map<SappAddressType, SappAddressRange> validAddresses = new HashMap<SappAddressType, SappAddressRange>() {
		protected static final long serialVersionUID = 592091386684476669L;
		{
			put(SappAddressType.INPUT, new SappAddressRange(1, 255));
			put(SappAddressType.OUTPUT, new SappAddressRange(1, 255));
			put(SappAddressType.VIRTUAL, new SappAddressRange(1, 2500));
		}
	};
	protected static final String[] validSubAddresses;

	static {
		validSubAddresses = new String[22];
		for (int i = 0; i < 16; i++) {
			validSubAddresses[i] = String.valueOf(i + 1);
		}
		validSubAddresses[16] = SappBindingConfigUtils.WORD_MASK_U;
		validSubAddresses[17] = SappBindingConfigUtils.WORD_MASK_S;
		validSubAddresses[18] = SappBindingConfigUtils.LOW_MASK_U;
		validSubAddresses[19] = SappBindingConfigUtils.LOW_MASK_S;
		validSubAddresses[20] = SappBindingConfigUtils.HIGH_MASK_U;
		validSubAddresses[21] = SappBindingConfigUtils.HIGH_MASK_S;
	}

	private String itemName;

	/**
	 * Constructor
	 */
	public SappBindingConfig(String itemName) {

		this.itemName = itemName;
	}

	/**
	 * itemName getter
	 */
	public String getItemName() {
		return itemName;
	}
}
