/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.console;

import java.util.Collection;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.scriptengine.Script;
import org.openhab.core.scriptengine.ScriptEngine;
import org.openhab.core.scriptengine.ScriptExecutionException;
import org.openhab.core.scriptengine.ScriptParsingException;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.io.console.internal.ConsoleActivator;
import org.openhab.io.multimedia.actions.Audio;

import com.google.common.base.Joiner;

/**
 * This class provides generic methods for handling console input (i.e. pure strings).
 * 
 * @author Kai Kreuzer
 * @since 0.4.0
 *
 */
public class ConsoleInterpreter {

	/**
	 * This method simply takes a list of arguments, where the first one is treated
	 * as the console command (such as "update", "send" etc.). The following entries
	 * are then the arguments for this command.
	 * If the command is unknown, the complete usage is printed to the console.
	 * 
	 * @param args array which contains the console command and all its arguments
	 * @param console the console for printing messages for the user
	 */
	static public void handleRequest(String[] args, Console console) {
		String arg = args[0];
		args = (String[]) ArrayUtils.remove(args, 0);
		if(arg.equals("items")) {
			ConsoleInterpreter.handleItems(args, console);
		} else if(arg.equals("send")) {
			ConsoleInterpreter.handleSend(args, console);
		} else if(arg.equals("update")) {
			ConsoleInterpreter.handleUpdate(args, console);
		} else if(arg.equals("status")) {
			ConsoleInterpreter.handleStatus(args, console);
		} else if(arg.equals("say")) {
			ConsoleInterpreter.handleSay(args, console);
		} else if(arg.equals(">")) {
			ConsoleInterpreter.handleScript(args, console);
		} else {
			console.printUsage(getUsage());
		}		
	}
	
	/**
	 * This method handles an update command. 
	 * 
	 * @param args array which contains the arguments for the update command
	 * @param console the console for printing messages for the user
	 */
	static public void handleUpdate(String[] args, Console console) {
		ItemRegistry registry = (ItemRegistry) ConsoleActivator.itemRegistryTracker.getService();
		EventPublisher publisher = (EventPublisher) ConsoleActivator.eventPublisherTracker.getService();
		if(publisher!=null) {
			if(registry!=null) {
				if(args.length>0) {
					String itemName = args[0];
					try {
						Item item = registry.getItemByPattern(itemName);
						if(args.length>1) {
							String stateName = args[1];
							State state = TypeParser.parseState(item.getAcceptedDataTypes(), stateName);
							if(state!=null) {
								publisher.postUpdate(item.getName(), state);
								console.println("Update has been sent successfully.");
							} else {
								console.println("Error: State '" + stateName +
										"' is not valid for item '" + itemName + "'");
								console.print("Valid data types are: ( ");
								for(Class<? extends State> acceptedType : item.getAcceptedDataTypes()) {
									console.print(acceptedType.getSimpleName() + " ");
								}
								console.println(")");
							}
						} else {
							console.printUsage(ConsoleInterpreter.getUpdateUsage());
						}
					} catch (ItemNotFoundException e) {
						console.println("Error: Item '" + itemName + "' does not exist.");
					} catch (ItemNotUniqueException e) {
						console.print("Error: Multiple items match this pattern: ");
						for(Item item : e.getMatchingItems()) {
							console.print(item.getName() + " ");
						}
					}
				} else {
					console.printUsage(ConsoleInterpreter.getUpdateUsage());
				}
			} else {
				console.println("Sorry, no item registry service available!");
			}
		} else {
			console.println("Sorry, no event publisher service available!");
		}
	}

	/**
	 * This method handles a send command. 
	 * 
	 * @param args array which contains the arguments for the send command
	 * @param console the console for printing messages for the user
	 */
	static public void handleSend(String[] args, Console console) {
		ItemRegistry registry = (ItemRegistry) ConsoleActivator.itemRegistryTracker.getService();
		EventPublisher publisher = (EventPublisher) ConsoleActivator.eventPublisherTracker.getService();
		if(publisher!=null) {
			if(registry!=null) {
				if(args.length>0) {
					String itemName = args[0];
					try {
						Item item = registry.getItemByPattern(itemName);
						if(args.length>1) {
							String commandName = args[1];
							Command command = TypeParser.parseCommand(item.getAcceptedCommandTypes(), commandName);
							if(command!=null) {
								publisher.sendCommand(itemName, command);
								console.println("Command has been sent successfully.");
							} else {
								console.println("Error: Command '" + commandName +
										"' is not valid for item '" + itemName + "'");
								console.print("Valid command types are: ( ");
								for(Class<? extends Command> acceptedType : item.getAcceptedCommandTypes()) {
									console.print(acceptedType.getSimpleName() + " ");
								}
								console.println(")");
							}
						} else {
							console.printUsage(ConsoleInterpreter.getCommandUsage());
						}
					} catch (ItemNotFoundException e) {
						console.println("Error: Item '" + itemName + "' does not exist.");
					} catch (ItemNotUniqueException e) {
						console.print("Error: Multiple items match this pattern: ");
						for(Item item : e.getMatchingItems()) {
							console.print(item.getName() + " ");
						}
					}
				} else {
					console.printUsage(ConsoleInterpreter.getCommandUsage());
				}
			} else {
				console.println("Sorry, no item registry service available!");
			}
		} else {
			console.println("Sorry, no event publisher service available!");
		}
	}

	/**
	 * This method handles an items command. 
	 * 
	 * @param args array which contains the arguments for the items command
	 * @param console the console for printing messages for the user
	 */
	static public void handleItems(String[] args, Console console) {
		ItemRegistry registry = (ItemRegistry) ConsoleActivator.itemRegistryTracker.getService();
		if(registry!=null) {
			String pattern = (args.length == 0) ? "*" : args[0];
			Collection<Item> items = registry.getItems(pattern);
			if(items.size()>0) {
				for(Item item : items) {
					console.println(item.toString());
				}
			} else {
				console.println("No items found for this pattern.");
			}
		} else {
			console.println("Sorry, no item registry service available!");
		}
	}
	
	/**
	 * This method handles a status command. 
	 * 
	 * @param args array which contains the arguments for the status command
	 * @param console the console for printing messages for the user
	 */
	static public void handleStatus(String[] args, Console console) {
		ItemRegistry registry = (ItemRegistry) ConsoleActivator.itemRegistryTracker.getService();
		if(registry!=null) {
			if(args.length>0) {
				String itemName = args[0];
				try {
					Item item = registry.getItemByPattern(itemName);
					console.println(item.getState().toString());
				} catch (ItemNotFoundException e) {
					console.println("Error: Item '" + itemName + "' does not exist.");
				} catch (ItemNotUniqueException e) {
					console.print("Error: Multiple items match this pattern: ");
					for(Item item : e.getMatchingItems()) {
						console.print(item.getName() + " ");
					}
				}
			} else {
				console.printUsage(ConsoleInterpreter.getStatusUsage());
			}
		} else {
			console.println("Sorry, no item registry service available!");
		}
	}

	/**
	 * This method handles a say command. 
	 * 
	 * @param args array which contains the arguments for the status command
	 * @param console the console for printing messages for the user
	 */
	static public void handleSay(String[] args, Console console) {
		StringBuilder msg = new StringBuilder();
		for(String word : args) {
			if(word.startsWith("%") && word.endsWith("%") && word.length()>2) {
				String itemName = word.substring(1, word.length()-1);
				ItemRegistry registry = (ItemRegistry) ConsoleActivator.itemRegistryTracker.getService();
				if(registry!=null) {
					try {
						Item item = registry.getItemByPattern(itemName);
						msg.append(item.getState().toString());
					} catch (ItemNotFoundException e) {
						console.println("Error: Item '" + itemName + "' does not exist.");
					} catch (ItemNotUniqueException e) {
						console.print("Error: Multiple items match this pattern: ");
						for(Item item : e.getMatchingItems()) {
							console.print(item.getName() + " ");
						}
					}
				} else {
					console.println("Sorry, no item registry service available!");
				}				
			} else {
				msg.append(word);
			}
			msg.append(" ");
		}
		try {
			Audio.say(msg.toString());
			console.println("Said: " + msg);
		} catch(NoClassDefFoundError e) {
			// The dependency to the Audio class is optional, so we have to handle the case that it is not there
			console.println("Could not perform command as no TTS service is available.");
		}
	}

	public static void handleScript(String[] args, Console console) {
		ScriptEngine scriptEngine = ConsoleActivator.scriptEngineTracker.getService();
		if(scriptEngine!=null) {
			String scriptString = Joiner.on(" ").join(args);
			Script script;
			try {
				script = scriptEngine.newScriptFromString(scriptString);
				Object result = script.execute();
				
				if(result!=null) {
					console.println(result.toString());
				} else {
					console.println("OK");
				}
			} catch (ScriptParsingException e) {
				console.println(e.getMessage());
			} catch (ScriptExecutionException e) {
				console.println(e.getMessage());
			}
		} else {
			console.println("Script engine is not available.");
		}
	}

	/** returns a CR-separated list of usage texts for all available commands */
	private static String getUsage() {
		StringBuilder sb = new StringBuilder();
		for(String usage : ConsoleInterpreter.getUsages()) {
			sb.append(usage + "\n");
		}
		return sb.toString();
	}

	/** returns an array of the usage texts for all available commands */
	static public String[] getUsages() {
		return new String[] {
				getUpdateUsage(),
				getCommandUsage(),
				getStatusUsage(),
				getItemsUsage(),
				getSayUsage(),
				getScriptUsage()
		};
	}
	
	static public String getUpdateUsage() {
		return "update <item> <state> - sends a status update for an item";
	}

	static public String getCommandUsage() {
		return "send <item> <command> - sends a command for an item";
	}

	static public String getStatusUsage() {
		return "status <item> - shows the current status of an item";
	}

	static public String getItemsUsage() {
		return "items [<pattern>] - lists names and types of all items matching the pattern";
	}

	public static String getSayUsage() {
		return "say <sentence to say> - Says a message through TTS on the host machine";
	}

	public static String getScriptUsage() {
		return "> <script to execute> - Executes a script";
	}

}
