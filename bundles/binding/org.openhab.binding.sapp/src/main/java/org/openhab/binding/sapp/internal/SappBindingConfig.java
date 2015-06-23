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

import org.apache.commons.lang.ArrayUtils;
import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This is a helper class holding binding specific configuration details
 * 
 * @author Paolo Denti
 * @since 1.8.0
 * 
 */
public class SappBindingConfig implements BindingConfig {
	private static final Map<SappAddressType, SappAddressRange> validAddresses = new HashMap<SappAddressType, SappAddressRange>() {
		private static final long serialVersionUID = 592091386684476669L;
		{
			put(SappAddressType.ALARM, new SappAddressRange(1, 250));
			put(SappAddressType.INPUT, new SappAddressRange(1, 255));
			put(SappAddressType.OUTPUT, new SappAddressRange(1, 255));
			put(SappAddressType.VIRTUAL, new SappAddressRange(1, 2500));
		}
	};
	private static final String[] validSubAddresses;

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
	private String pnmasId;
	private SappAddressType addressType;
	private int address;
	private String subAddress;

	public SappBindingConfig(String itemName, String bindingConfig) throws BindingConfigParseException {

		this.itemName = itemName;

		// check syntax
		String[] bindingConfigParts = bindingConfig.split(":");
		if (bindingConfigParts.length != 4) {
			throw new BindingConfigParseException(String.format("Invalid Sapp binding configuration '%s'", bindingConfig));
		}

		// pnmasId
		this.pnmasId = bindingConfigParts[0];
		if (this.pnmasId.length() == 0) {
			throw new BindingConfigParseException(String.format("Invalid Sapp binding configuration '%s'", bindingConfig));
		}

		// addressType
		this.addressType = SappAddressType.fromString(bindingConfigParts[1].toUpperCase());
		if (!validAddresses.keySet().contains(this.addressType)) {
			throw new BindingConfigParseException(String.format("Invalid Sapp binding configuration '%s'", bindingConfig));
		}

		// address
		try {
			this.address = Integer.parseInt(bindingConfigParts[2]);
			if (this.address < validAddresses.get(this.addressType).getLoRange() || this.address > validAddresses.get(this.addressType).getHiRange()) {
				throw new BindingConfigParseException(String.format("Invalid Sapp binding configuration '%s'", bindingConfig));
			}
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException(String.format("Invalid Sapp binding configuration '%s'", bindingConfig));
		}

		this.subAddress = bindingConfigParts[3].toUpperCase();
		if (!ArrayUtils.contains(validSubAddresses, this.subAddress)) {
			throw new BindingConfigParseException(String.format("Invalid Sapp binding configuration '%s'", bindingConfig));
		}
	}

	public String getItemName() {
		return itemName;
	}

	public String getPnmasId() {
		return pnmasId;
	}

	public SappAddressType getAddressType() {
		return addressType;
	}

	public int getAddress() {
		return address;
	}

	public String getSubAddress() {
		return subAddress;
	}
	
	@Override
	public String toString() {
		return String.format("%s: [ %s:%s:%d:%s ]", this.itemName, this.pnmasId, this.addressType, this.address, this.subAddress);
	}
}
