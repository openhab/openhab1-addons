package org.openhab.binding.freebox;

import org.openhab.binding.freebox.internal.CommandType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

public class FreeboxBindingConfig implements BindingConfig {
	public CommandType commandType;
	public Item item;
	
	public FreeboxBindingConfig(CommandType commandType, Item item) {
		this.commandType = commandType;
		this.item = item;
	}
}