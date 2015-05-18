package org.openhab.binding.wr3223;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;

public enum WR3223CommandType {
	
	TEMPERATURE_OUTSIDE {
		{
			command = "temperature_outside";
			itemClass = NumberItem.class;
		}
	};
	
	/** Represents the WR3223 command as it will be used in *.items configuration */
	String command;
	Class<? extends Item> itemClass;

	public String getCommand() {
		return command;
	}

	public Class<? extends Item> getItemClass() {
		return itemClass;
	}	
	
	
	/**
	 * Find the right command for an item string.
	 * 
	 * @param bindingConfigString
	 * @return
	 */
	public static WR3223CommandType fromString(String bindingConfigString) {

		if ("".equals(bindingConfigString)) {
			return null;
		}
		for (WR3223CommandType c : WR3223CommandType.values()) {

			if (c.getCommand().equalsIgnoreCase(bindingConfigString)) {
				return c;
			}
		}
		throw new IllegalArgumentException("cannot find wr3223Command for '" + bindingConfigString + "'");
	}	
	
	

	public static boolean validateBinding(WR3223CommandType bindingConfig, Class<? extends Item> itemClass) {
		if(bindingConfig != null && itemClass != null){
			return bindingConfig.getItemClass().equals(itemClass);
		}
		return false;
	}	

}
