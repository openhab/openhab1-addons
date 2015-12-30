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
import org.openhab.binding.sapp.internal.model.SappAddressRollershutterControl;
import org.openhab.binding.sapp.internal.model.SappAddressRollershutterStatus;
import org.openhab.binding.sapp.internal.model.SappAddressType;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;

// Switch binding format
//<pnmasid status>:<status address type, only V>:<status address, 1-2500>:<status subaddress, */H/L/1-16>:<up value>:<down value>/<pnmasid up command>:<status address type, only V>:<control address, 1-2500>:<control subaddress, */H/L/1-16>:<up value>/<pnmasid down command>:<status address type, only V>:<control address, 1-2500>:<control subaddress, */H/L/1-16>:<down value>/<pnmasid stop command>:<status address type, only V>:<control address, 1-2500>:<control subaddress, */H/L/1-16>:<stop value>
// example: { sapp="home:V:154:*:100:0/home:V:155:1:1/home:V:155:2:1/home:V:155:3:1" }

/**
 * This is a helper class holding RollershutterItem binding specific configuration details
 * 
 * @author Paolo Denti
 * @since 1.8.0
 * 
 */
public class SappBindingConfigRollershutterItem extends SappBindingConfig {

	private SappAddressRollershutterStatus status;
	private SappAddressRollershutterControl upControl;
	private SappAddressRollershutterControl downControl;
	private SappAddressRollershutterControl stopControl;

	/**
	 * Constructor
	 */
	public SappBindingConfigRollershutterItem(Item item, String bindingConfig) throws BindingConfigParseException {

		super(item.getName());

		String[] bindingConfigParts = bindingConfig.split("/");
		if (bindingConfigParts.length != 4) {
			throw new BindingConfigParseException(errorMessage(bindingConfig));
		}

		this.status = parseSappAddressStatus(bindingConfigParts[0]);
		this.upControl = parseSappAddressControl(bindingConfigParts[1]);
		this.downControl = parseSappAddressControl(bindingConfigParts[2]);
		this.stopControl = parseSappAddressControl(bindingConfigParts[3]);
	}

	/**
	 * status getter
	 */
	public SappAddressRollershutterStatus getStatus() {
		return status;
	}

	/**
	 * upControl getter
	 */
	public SappAddressRollershutterControl getUpControl() {
		return upControl;
	}

	/**
	 * downControl getter
	 */
	public SappAddressRollershutterControl getDownControl() {
		return downControl;
	}

	/**
	 * stopControl getter
	 */
	public SappAddressRollershutterControl getStopControl() {
		return stopControl;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("[itemName:%s: status:%s ]", getItemName(), this.status.toString());
	}

	private String errorMessage(String bindingConfig) {
		return String.format("Invalid Sapp binding configuration for RollershutterItem '%s'", bindingConfig);
	}

	private SappAddressRollershutterStatus parseSappAddressStatus(String bindingStringAddress) throws BindingConfigParseException {

		String pnmasId;
		SappAddressType addressType;
		int address;
		String subAddress;
		int openValue;
		int closedValue;

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

		// openValue
		try {
			openValue = Integer.parseInt(bindingAddress[4]);
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		// closedValue
		try {
			closedValue = Integer.parseInt(bindingAddress[5]);
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		return new SappAddressRollershutterStatus(pnmasId, addressType, address, subAddress, openValue, closedValue);
	}

	private SappAddressRollershutterControl parseSappAddressControl(String bindingStringAddress) throws BindingConfigParseException {

		String pnmasId;
		SappAddressType addressType;
		int address;
		String subAddress;
		int activateValue;

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

		// activateValue
		try {
			activateValue = Integer.parseInt(bindingAddress[4]);
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		return new SappAddressRollershutterControl(pnmasId, addressType, address, subAddress, activateValue);
	}
}
