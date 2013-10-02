package org.openhab.binding.intertechno.internal.parser;

import org.openhab.model.item.binding.BindingConfigParseException;

public class RawParser extends AbstractIntertechnoParser {

	private String commandOn;
	private String commandOff;

	@Override
	public String parseAddress(String... addressParts)
			throws BindingConfigParseException {
		String address = addressParts[0];
		commandOn = addressParts[1];
		commandOff = addressParts[2];
		return address;
	}

	@Override
	public String getCommandValueON() {
		return commandOn;
	}

	@Override
	public String getCOmmandValueOFF() {
		return commandOff;
	}

}
