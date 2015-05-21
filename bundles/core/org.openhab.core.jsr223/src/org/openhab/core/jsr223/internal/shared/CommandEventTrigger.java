package org.openhab.core.jsr223.internal.shared;

import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * CommandEventTrigger is used by a Script to listen for (specific) commands
 * 
 * @author Simon Merschjohann
 * @since 1.7.0
 */
public class CommandEventTrigger implements EventTrigger {
	private String itemName;
	private Command command;

	public CommandEventTrigger(String itemName, Command command) {
		this.itemName = itemName;
		this.command = command;
	}

	@Override
	public boolean evaluate(Item item, State oldState, State newState, Command command, TriggerType type) {
		return (type == TriggerType.COMMAND && this.itemName.equals(item.getName()) && command == this.command);
	}

	@Override
	public String getItem() {
		return this.itemName;
	}

}
