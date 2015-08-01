package org.openhab.binding.sapp.internal;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;

public class SappBindingConfigSwitchItem extends SappBindingConfig {

	private SappAddress status;
	private SappAddress control;

	public SappBindingConfigSwitchItem(Item item, String bindingConfig) throws BindingConfigParseException {

		super(item.getName());
		
		String[] bindingConfigParts = bindingConfig.split("/");
		if (bindingConfigParts.length != 2) {
			throw new BindingConfigParseException(errorMessage(bindingConfig));
		}
		
		this.status = parseSappAddress(bindingConfigParts[0]);
		this.control = parseSappAddress(bindingConfigParts[1]);
		if (this.control.getAddressType() != SappAddressType.VIRTUAL) {
			throw new BindingConfigParseException(errorMessage(bindingConfigParts[1]));
		}
	}

	public SappAddress getStatus() {
		return status;
	}

	public SappAddress getControl() {
		return control;
	}

	@Override
	public String toString() {
		return String.format("[itemName:%s: status:%s - control: %s ]", getItemName(), this.status.toString(), this.control.toString());
	}
	
	private String errorMessage(String bindingConfig) {
		return String.format("Invalid Sapp binding configuration for SwitchItem '%s'", bindingConfig);
	}
	
	private SappAddress parseSappAddress(String bindingStringAddress) throws BindingConfigParseException {
		
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

		return new SappAddress(pnmasId, addressType, address, subAddress, onValue, offValue);
	}
}
