package org.openhab.binding.intertechno.internal.parser;

import org.openhab.model.item.binding.BindingConfigParseException;

public class FLSParser extends AbstractIntertechnoParser {

	@Override
	public String parseAddress(String... addressParts)
			throws BindingConfigParseException {
		String group = addressParts[0];
		int subAddress = 0;
		try {
			subAddress = Integer.parseInt(addressParts[1]);
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException(
					"Sub address is not a number. Configured subaddress: "
							+ addressParts[1]);
		}
		return getGroupAddress(group) + getSubAddress(subAddress) + "00";
	}

	private String getGroupAddress(String group)
			throws BindingConfigParseException {
		StringBuffer addressBuffer = new StringBuffer(4);
		addressBuffer.append("FFFF");
		if ("I".equalsIgnoreCase(group)) {
			addressBuffer.setCharAt(0, '0');

		} else if ("II".equalsIgnoreCase(group)) {
			addressBuffer.setCharAt(1, '0');
		} else if ("III".equalsIgnoreCase(group)) {
			addressBuffer.setCharAt(2, '0');
		} else if ("IV".equalsIgnoreCase(group)) {
			addressBuffer.setCharAt(3, '0');
		} else {
			throw new BindingConfigParseException(
					"Unknown roman number given: " + group);
		}
		return addressBuffer.toString();
	}

	private String getSubAddress(int remoteId)
			throws BindingConfigParseException {
		if (remoteId < 1 || remoteId > 4) {
			throw new BindingConfigParseException(
					"Only remote addresses in the range 1 - 4 are supported");
		}
		StringBuffer buffer = new StringBuffer(4);
		buffer.append("FFFF");
		buffer.setCharAt(remoteId - 1, '0');
		return buffer.toString();
	}

	@Override
	public String getCommandValueON() {
		return "FF";
	}

	@Override
	public String getCOmmandValueOFF() {
		return "F0";
	}

}
