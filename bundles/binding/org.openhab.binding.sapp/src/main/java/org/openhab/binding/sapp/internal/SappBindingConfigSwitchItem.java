package org.openhab.binding.sapp.internal;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;

public class SappBindingConfigSwitchItem extends SappBindingConfig {

	private String pnmasId;
	private SappAddressType addressType;
	private int address;
	private String subAddress;

	public SappBindingConfigSwitchItem(Item item, String bindingConfig) throws BindingConfigParseException {

		super(item.getName());

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
		return String.format("%s: [ %s:%s:%d:%s ]", getItemName(), this.pnmasId, this.addressType, this.address, this.subAddress);
	}
}
