package org.openhab.binding.intertechno.internal.parser;

import org.openhab.model.item.binding.BindingConfigParseException;

public interface IntertechnoAddressParser {

	public String parseAddress(String... addressParts)
			throws BindingConfigParseException;

	public String getCommandValueON();

	public String getCOmmandValueOFF();

}
