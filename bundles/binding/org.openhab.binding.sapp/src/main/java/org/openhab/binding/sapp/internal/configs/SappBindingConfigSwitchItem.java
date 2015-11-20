/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal.configs;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.sapp.internal.model.SappAddressOnOffControl;
import org.openhab.binding.sapp.internal.model.SappAddressOnOffStatus;
import org.openhab.binding.sapp.internal.model.SappAddressType;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;

// Switch binding format
// <pnmasid status>:<status address type, I/O/V>:<status address, 1-250/1-250/1-2500>:<status subaddress, */H/L/1-16>:<on value>/<pnmasid control>:<status address type, only V>:<control address, 1-2500>:<control subaddress, */H/L/1-16>:<on value>:<off value>
// <on value> can be omitted, default is 1
// <on value>:<off value> can be omitted, default is 1:0
// example: { sapp="home:V:60:1/home:V:192:1:1:0" }
//
// stopping poller switch, suspends and restarts the polling
//<pnmasid status>:<P>
//example: { sapp="P" }

/**
 * This is a helper class holding SwitchItem binding specific configuration details
 * 
 * @author Paolo Denti
 * @since 1.8.0
 * 
 */
public class SappBindingConfigSwitchItem extends SappBindingConfig {

	private SappAddressOnOffStatus status;
	private SappAddressOnOffControl control;
	private boolean pollerSuspender;

	/**
	 * Constructor
	 */
	public SappBindingConfigSwitchItem(Item item, String bindingConfig) throws BindingConfigParseException {

		super(item.getName());

		if ("P".equals(bindingConfig)) {
			pollerSuspender = true;

			this.status = null;
			this.control = null;
		} else {
			pollerSuspender = false;

			String[] bindingConfigParts = bindingConfig.split("/");
			if (bindingConfigParts.length != 2) {
				throw new BindingConfigParseException(errorMessage(bindingConfig));
			}

			this.status = parseSappAddressStatus(bindingConfigParts[0]);
			this.control = parseSappAddressControl(bindingConfigParts[1]);
		}
	}

	/**
	 * status getter
	 */
	public SappAddressOnOffStatus getStatus() {
		return status;
	}

	/**
	 * control getter
	 */
	public SappAddressOnOffControl getControl() {
		return control;
	}

	/**
	 * pollerSuspender getter
	 */
	public boolean isPollerSuspender() {
		return pollerSuspender;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		if (pollerSuspender) {
			return String.format("[itemName:%s: pollerSuspender ]", getItemName());
		} else {
			return String.format("[itemName:%s: status:%s - control: %s ]", getItemName(), this.status.toString(), this.control.toString());
		}
	}

	private String errorMessage(String bindingConfig) {
		return String.format("Invalid Sapp binding configuration for SwitchItem '%s'", bindingConfig);
	}

	private SappAddressOnOffStatus parseSappAddressStatus(String bindingStringAddress) throws BindingConfigParseException {

		String pnmasId;
		SappAddressType addressType;
		int address;
		String subAddress;
		int onValue = 1;

		String[] bindingAddress = bindingStringAddress.split(":");
		if (bindingAddress.length != 4 && bindingAddress.length != 5) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// pnmasId
		pnmasId = bindingAddress[0];
		if (pnmasId.length() == 0) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// addressType
		addressType = SappAddressType.fromString(bindingAddress[1].toUpperCase());
		if (!validAddresses.keySet().contains(addressType)) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// address
		try {
			address = Integer.parseInt(bindingAddress[2]);
			if (address < validAddresses.get(addressType).getLoRange() || address > validAddresses.get(addressType).getHiRange()) {
				throw new BindingConfigParseException(errorMessage(bindingStringAddress));
			}
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// subaddress
		subAddress = bindingAddress[3].toUpperCase();
		if (!ArrayUtils.contains(validSubAddresses, subAddress)) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// onvalue, offvalue
		if (bindingAddress.length == 5) {
			try {
				onValue = Integer.parseInt(bindingAddress[4]);
			} catch (NumberFormatException e) {
				throw new BindingConfigParseException(errorMessage(bindingStringAddress));
			}
		}

		return new SappAddressOnOffStatus(pnmasId, addressType, address, subAddress, onValue);
	}

	private SappAddressOnOffControl parseSappAddressControl(String bindingStringAddress) throws BindingConfigParseException {

		String pnmasId;
		SappAddressType addressType;
		int address;
		String subAddress;
		int onValue = 1;
		int offValue = 0;

		String[] bindingAddress = bindingStringAddress.split(":");
		if (bindingAddress.length != 4 && bindingAddress.length != 6) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// pnmasId
		pnmasId = bindingAddress[0];
		if (pnmasId.length() == 0) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// addressType
		addressType = SappAddressType.fromString(bindingAddress[1].toUpperCase());
		if (!validAddresses.keySet().contains(addressType)) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}
		if (addressType != SappAddressType.VIRTUAL) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// address
		try {
			address = Integer.parseInt(bindingAddress[2]);
			if (address < validAddresses.get(addressType).getLoRange() || address > validAddresses.get(addressType).getHiRange()) {
				throw new BindingConfigParseException(errorMessage(bindingStringAddress));
			}
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// subaddress
		subAddress = bindingAddress[3].toUpperCase();
		if (!ArrayUtils.contains(validSubAddresses, subAddress)) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// onvalue, offvalue
		if (bindingAddress.length == 6) {
			try {
				onValue = Integer.parseInt(bindingAddress[4]);
				offValue = Integer.parseInt(bindingAddress[5]);
			} catch (NumberFormatException e) {
				throw new BindingConfigParseException(errorMessage(bindingStringAddress));
			}
		}

		return new SappAddressOnOffControl(pnmasId, addressType, address, subAddress, onValue, offValue);
	}
}
