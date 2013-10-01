package org.openhab.binding.mqttitude.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;

public class MqttitudeItemConfig implements BindingConfig {	
	private final String itemName;
	private final String broker;
	private final String topic;

	public MqttitudeItemConfig(String itemName, String bindingConfig) throws BindingConfigParseException {
		
		String[] config = bindingConfig.split(":");
		
		if (config.length != 2)
			throw new BindingConfigParseException("Invalid Mqttitude binding configuration '" + bindingConfig + "' for item " + itemName);
		
		this.itemName = itemName;
		this.broker = StringUtils.trim(config[0]);
		this.topic = StringUtils.trim(config[1]);
	}

	public String getItemName() {
		return itemName;
	}
	
	public String getBroker() {
		return broker;
	}
	
	public String getTopic() {
		return topic;
	}
}
