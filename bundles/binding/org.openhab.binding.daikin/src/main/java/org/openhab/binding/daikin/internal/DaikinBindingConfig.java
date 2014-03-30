package org.openhab.binding.daikin.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;

public class DaikinBindingConfig implements BindingConfig {
	private String itemName;
	private String id;
	private DaikinCommandType commandType;
	
	public DaikinBindingConfig(String itemName, String bindingConfig) throws BindingConfigParseException {
		String[] config = bindingConfig.split(":");
		if (config.length != 2)
			throw new BindingConfigParseException("Invalid Daikin binding configuration '" + bindingConfig + "' for item " + itemName + ". Expecting '<id>:<command>'.");
		
		this.itemName = itemName;
		this.id = StringUtils.trim(config[0]);
		this.commandType = DaikinCommandType.fromString(config[1]);
	}

	public String getItemName() {
		return itemName;
	}

	public String getId() {
		return id;
	}

	public DaikinCommandType getCommandType() {
		return commandType;
	}
}
