package org.openhab.binding.sapp.internal.configs;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.sapp.internal.model.SappAddressDimmer;
import org.openhab.binding.sapp.internal.model.SappAddressType;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.BindingConfigParseException;

// Number binding format
// <pnmasid status>:<status address type, only V>:<status address, 1-2500>:<status subaddress, */H/L/1-16>:<min value>:<max value>:<increment>
// example: { sapp="home:V:180:*:0:100:10" }

public class SappBindingConfigDimmerItem extends SappBindingConfig {

	private SappAddressDimmer status;

	public SappBindingConfigDimmerItem(Item item, String bindingConfig) throws BindingConfigParseException {

		super(item.getName());

		this.status = parseSappAddressStatus(bindingConfig);
	}

	public SappAddressDimmer getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return String.format("[itemName:%s: status:%s ]", getItemName(), this.status.toString());
	}

	private String errorMessage(String bindingConfig) {
		return String.format("Invalid Sapp binding configuration for DimmerItem '%s'", bindingConfig);
	}

	private SappAddressDimmer parseSappAddressStatus(String bindingStringAddress) throws BindingConfigParseException {

		String pnmasId;
		SappAddressType addressType;
		int address;
		String subAddress;
		int increment;

		String[] bindingAddress = bindingStringAddress.split(":");
		if (bindingAddress.length != 5) {
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
		
		// increment
		try {
			increment = Integer.parseInt(bindingAddress[4]);
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException(errorMessage(bindingStringAddress));
		}

		return new SappAddressDimmer(pnmasId, addressType, address, subAddress, 0, 100, increment);
	}
}
