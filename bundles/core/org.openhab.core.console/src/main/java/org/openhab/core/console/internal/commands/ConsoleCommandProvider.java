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
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;

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
		
		if(arg==null) {
			interpreter.println(getHelp());
			return null;
		}
		
		if(arg.equals("items")) {
			handleItems(interpreter);
		} else if(arg.equals("send")) {
			handleSend(interpreter);
		} else if(arg.equals("update")) {
			handleUpdate(interpreter);
		} else if(arg.equals("status")) {
			handleStatus(interpreter);
		} else {
			interpreter.println(getHelp());
		}
		
		return null;
	}

	private void handleUpdate(CommandInterpreter interpreter) {
		ItemRegistry registry = (ItemRegistry) ConsoleActivator.itemRegistryTracker.getService();
		EventPublisher publisher = (EventPublisher) ConsoleActivator.eventPublisherTracker.getService();
		if(publisher!=null) {
			if(registry!=null) {
				String itemName = interpreter.nextArgument();
				if(itemName!=null) {
					try {
						Item item = registry.getItem(itemName);
						String stateName = interpreter.nextArgument();
						if(stateName!=null) {
							State state = TypeParser.parseState(item.getAcceptedDataTypes(), stateName);
							if(state!=null) {
								publisher.postUpdate(item.getName(), state);
								interpreter.println("Update has been sent successfully.");
							} else {
								interpreter.println("Error: State '" + stateName +
										"' is not valid for item '" + itemName + "'");
								interpreter.print("Valid data types are: ( ");
								for(Class<? extends State> acceptedType : item.getAcceptedDataTypes()) {
									interpreter.print(acceptedType.getSimpleName() + " ");
								}
								interpreter.println(")");
							}
						} else {
							interpreter.println("Usage: " + getUpdateUsage());
						}
					} catch (ItemNotFoundException e) {
						interpreter.println("Error: Item '" + itemName + "' does not exist.");
					} catch (ItemNotUniqueException e) {
						interpreter.print("Error: Multiple items match this pattern: ");
						for(Item item : e.getMatchingItems()) {
							interpreter.print(item.getName() + " ");
						}
					}
				} else {
					interpreter.println("Usage: " + getUpdateUsage());
				}
			} else {
				interpreter.println("Sorry, no item registry service available!");
			}
		} else {
			interpreter.println("Sorry, no event publisher service available!");
		}
	}

	private void handleSend(CommandInterpreter interpreter) {
		ItemRegistry registry = (ItemRegistry) ConsoleActivator.itemRegistryTracker.getService();
		EventPublisher publisher = (EventPublisher) ConsoleActivator.eventPublisherTracker.getService();
		if(publisher!=null) {
			if(registry!=null) {
				String itemName = interpreter.nextArgument();
				if(itemName!=null) {
					try {
						Item item = registry.getItem(itemName);
						String commandName = interpreter.nextArgument();
						if(commandName!=null) {
							Command command = TypeParser.parseCommand(item.getAcceptedCommandTypes(), commandName);
							if(command!=null) {
								publisher.sendCommand(itemName, command);
								interpreter.println("Command has been sent successfully.");
							} else {
								interpreter.println("Error: Command '" + commandName +
										"' is not valid for item '" + itemName + "'");
							}
						} else {
							interpreter.println("Usage: " + getCommandUsage());
						}
					} catch (ItemNotFoundException e) {
						interpreter.println("Error: Item '" + itemName + "' does not exist.");
					} catch (ItemNotUniqueException e) {
						interpreter.print("Error: Multiple items match this pattern: ");
						for(Item item : e.getMatchingItems()) {
							interpreter.print(item.getName() + " ");
						}
					}
				} else {
					interpreter.println("Usage: " + getCommandUsage());
				}
			} else {
				interpreter.println("Sorry, no item registry service available!");
			}
		} else {
			interpreter.println("Sorry, no event publisher service available!");
		}
	}

	private void handleItems(CommandInterpreter interpreter) {
		ItemRegistry registry = (ItemRegistry) ConsoleActivator.itemRegistryTracker.getService();
		if(registry!=null) {
			String pattern = interpreter.nextArgument();
			if(pattern==null || pattern.isEmpty()) pattern ="*";
			for(Item item : registry.getItems(pattern)) {
				interpreter.println(item.toString());
			}
		} else {
			interpreter.println("Sorry, no item registry service available!");
		}
	}
	
	private void handleStatus(CommandInterpreter interpreter) {
		ItemRegistry registry = (ItemRegistry) ConsoleActivator.itemRegistryTracker.getService();
		if(registry!=null) {
			String itemName = interpreter.nextArgument();
			try {
				Item item = registry.getItem(itemName);
				interpreter.println(item.getState().toString());
			} catch (ItemNotFoundException e) {
				interpreter.println("Error: Item '" + itemName + "' does not exist.");
			} catch (ItemNotUniqueException e) {
				interpreter.print("Error: Multiple items match this pattern: ");
				for(Item item : e.getMatchingItems()) {
					interpreter.print(item.getName() + " ");
				}
			}
		} else {
			interpreter.println("Sorry, no item registry service available!");
		}
	}

	/**
	 * Contributes the usage of our command to the console help output.
	 */
	public String getHelp() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("---openHAB commands---\n\t");
		buffer.append(getCommandUsage() + "\n\t");
		buffer.append(getUpdateUsage() + "\n\t");
		buffer.append(getStatusUsage() + "\n\t");
		buffer.append(getItemsUsage() + "\n");
		return buffer.toString();
	}

	private String getUpdateUsage() {
		return "openhab update <item> <state> - sends a status update for an item";
	}

	private String getCommandUsage() {
		return "openhab send <item> <command> - sends a command for an item";
	}

	private String getStatusUsage() {
		return "openhab status <item> - shows the current status of an item";
	}

	private String getItemsUsage() {
		return "openhab items [<pattern>] - lists names and types of all items matching the pattern";
	}
}
