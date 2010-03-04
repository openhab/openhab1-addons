/* 
* openHAB, the open Home Automation Bus.
* Copyright 2010, openHAB.org
*
* See the contributors.txt file in the distribution for a
* full listing of individual contributors.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as
* published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package org.openhab.core.console.internal.commands;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.openhab.core.console.internal.ConsoleActivator;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemRegistry;

/**
 * This class provides access to openHAB functionality through the OSGi console
 * of Equinox. Unfortunately, there these command providers are not standardized
 * for OSGi, so we need different implementations for different OSGi containers.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public class ConsoleCommandProvider implements CommandProvider {

	/**
	 * Methods staring with "_" will be used as commands. We only define one command "openhab" to make
	 * sure we do not get into conflict with other existing commands. The different functionalities
	 * can then be used by the first argument.
	 * 
	 * @param interpreter the equinox command interpreter
	 * @return null, return parameter is not used
	 */
	public Object _openhab(CommandInterpreter interpreter) {
		String arg = interpreter.nextArgument();
		
		if(arg.equals("items")) {
			ItemRegistry registry = (ItemRegistry) ConsoleActivator.itemRegistryTracker.getService();
			if(registry!=null) {
				for(Item item : registry.getItems()) {
					interpreter.println(item.getClass().getSimpleName() + ": " + item.getName());
				}
			} else {
				interpreter.println("Sorry, no item registry service available!");
			}
		}
		return null;
	}
	
	/**
	 * Contributes the usage of our command to the console help output.
	 */
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
