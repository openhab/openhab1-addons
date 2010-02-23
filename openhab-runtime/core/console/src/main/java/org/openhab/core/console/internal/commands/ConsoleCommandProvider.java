package org.openhab.core.console.internal.commands;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

public class ConsoleCommandProvider implements CommandProvider {

	public Object _openhab(CommandInterpreter interpreter) {
		interpreter.println("OK");
		return null;
	}
	
	public String getHelp() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("---openHAB commands---\n\t");
		buffer.append("openhab command <item> <command> - sends a command for an item\n\t");
		buffer.append("openhab update <item> <state> - sends a status update for an item\n\t");
		buffer.append("openhab refresh <item> - sends a refresh request for an item\n");
		return buffer.toString();
	}

}
