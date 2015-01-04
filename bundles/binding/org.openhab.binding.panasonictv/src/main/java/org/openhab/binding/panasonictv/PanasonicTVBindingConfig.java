package org.openhab.binding.panasonictv;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

public class PanasonicTVBindingConfig implements BindingConfig {
	String tv, command;
	Item item;

	public PanasonicTVBindingConfig(Item item, String tv, String command) {
		super();
		this.item = item;
		this.tv = tv;
		this.command = command;
	}

	public String getTv() {
		return tv;
	}

	public String getCommand() {
		return command;
	}
}