package org.openhab.core.console.internal.commands;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.openhab.core.console.internal.ConsoleActivator;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.ItemRegistry;

public class ConsoleCommandProvider implements CommandProvider {

	public Object _openhab(CommandInterpreter interpreter) {
		String arg = interpreter.nextArgument();
		
		if(arg.equals("items")) {
			ItemRegistry registry = (ItemRegistry) ConsoleActivator.itemRegistryTracker.getService();
			if(registry!=null) {
				for(GenericItem item : registry.getItems()) {
					interpreter.println(item.getClass().getSimpleName() + ": " + item.getName());
				}
			} else {
				interpreter.println("Sorry, no item registry service available!");
			}
		}
		return null;
	}
	
	public String getHelp() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("---openHAB commands---\n\t");
		buffer.append("openhab command <item> <command> - sends a command for an item\n\t");
		buffer.append("openhab update <item> <state> - sends a status update for an item\n\t");
		buffer.append("openhab refresh <item> - sends a refresh request for an item\n\t");
		buffer.append("openhab items - lists names and types of all registered items\n");
		return buffer.toString();
	}

}
