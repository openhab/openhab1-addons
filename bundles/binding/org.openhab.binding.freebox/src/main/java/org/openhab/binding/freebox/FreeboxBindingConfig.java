package org.openhab.binding.freebox;

import org.openhab.binding.freebox.internal.CommandType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * This is a class that stores Freebox binding configuration elements :
 * 	 - the type of command to operate on the freebox
 *   - an interface to related openHAB item
 * @author clinique
 * @since 1.5.0
 *
 */

public class FreeboxBindingConfig implements BindingConfig {
	public CommandType commandType;
	public Item item;
	
	public FreeboxBindingConfig(CommandType commandType, Item item) {
		this.commandType = commandType;
		this.item = item;
	}
}
