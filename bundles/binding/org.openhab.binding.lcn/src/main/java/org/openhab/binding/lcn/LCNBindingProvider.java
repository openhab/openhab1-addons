/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.lcn.LCNBinding.Credentials;
import org.openhab.binding.lcn.logic.LCNParser;
import org.openhab.binding.lcn.logic.data.LCNInputModule;
import org.openhab.binding.lcn.logic.data.LCNItemBinding;
import org.openhab.binding.lcn.logic.data.LCNItemMap;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class handles item binding definitions.
 * Whenever explicit bindings change, this class will update them for the LCNBinding.
 * 
 * @author Patrik Pastuschek
 * @since 1.7.0
 *
 */
public class LCNBindingProvider extends AbstractGenericBindingProvider implements BindingProvider {

	/**The logger to handle output.*/
	private Logger logger = LoggerFactory.getLogger(LCNBindingProvider.class);
	
	/**Pattern used to match bindings in general.*/
	private static final Pattern SIMPLE_BINDING = Pattern.compile("(\\[.*?\\])*");
	/**Pattern used to match bindings which use an openHAB command.*/
	private static final Pattern BINDING_WITH_OPENHAB = Pattern.compile("\\[(.*?):(.*?):\'?(.*?)\'?\\]");
	/**Pattern used to match bindings which don't use an openHAB command.*/
	private static final Pattern PURE_BINDING = Pattern.compile("\\[(.*):\'?(.*?)\'?\\]");
	
	/**Gives different commands different IDs, this is being used to differentiate artificially created keys.*/
	private static int counter = 0;
	
	/**Was updated flag.*/
	public boolean wasUpdated = false;
	
	/**
	 * Special BindingConfiguration processing for the LCN binding.
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		
		wasUpdated = true;
		super.processBindingConfiguration(context, item, bindingConfig);
		
		if (null != bindingConfig) {
			
			LCNItemMap lCNItemMap = new LCNItemMap();
			Matcher matcher = SIMPLE_BINDING.matcher(bindingConfig);

			if (!matcher.matches()) {
				throw new BindingConfigParseException(bindingConfig + "' contains no valid binding!");
			}
			
			matcher.reset();

			while (matcher.find()) {
				
				String bindingConfigPart = matcher.group(1);
			
				if (StringUtils.isNotBlank(bindingConfigPart)) {
					
					processBinding(lCNItemMap, item, bindingConfigPart);					
					addBindingConfig(item, lCNItemMap);
					
				}
				
			}
			
		} else {
			
			logger.warn("BindingConfiguration for {} is NULL", context);
			
		}
		
	}
	
	/**
	 * Processes a single binding into a map conform structure.
	 * @param lCNItemMap
	 * @param item
	 * @param binding
	 * @throws BindingConfigParseException
	 */
	private void processBinding(LCNItemMap lCNItemMap, Item item, String binding) throws BindingConfigParseException {

		String openHabCmd = null;
		String id = null;
		String lcnCmd = null;

		if(binding != null){

			Matcher openHabMatcher = BINDING_WITH_OPENHAB.matcher(binding);
			Matcher pureMatcher = PURE_BINDING.matcher(binding);

			if(openHabMatcher.matches()) {
				
				openHabCmd = openHabMatcher.group(1);
				id = openHabMatcher.group(2);
				lcnCmd = openHabMatcher.group(3);	
				
			} else if (pureMatcher.matches()) {
				
				id = pureMatcher.group(1);
				lcnCmd = pureMatcher.group(2);
				
			} else {
				
				throw new BindingConfigParseException("Invalid binding configuration for " + binding + "!");
				
			}			
			
			String lcnShort = LCNBinding.parseLCNShort(lcnCmd, openHabCmd);
			
			if (null == lcnShort || lcnShort.equals(openHabCmd)) {
				
				lcnShort = lcnCmd;
				
			}
			
			LCNItemBinding lCNItemBinding = new LCNItemBinding(id, lcnCmd, item.getAcceptedDataTypes(), item, lcnShort, false);

			Command cmd = null;
			
			if(null == openHabCmd) {
				
				cmd = TypeParser.parseCommand(new NumberItem("" + counter).getAcceptedCommandTypes(), Integer.toString(counter));
				counter++;
				
			} else {
				
				cmd = TypeParser.parseCommand(item.getAcceptedCommandTypes(), openHabCmd);
				
			}

			lCNItemMap.put(cmd, lCNItemBinding);			
			
		}
	}
	
	/**
	 * Returns a List of InetSocketAddresses for a certain item.
	 * @param itemName The name of the item.
	 * @return A List of InetSocketAddresses, can be empty.
	 */
	public List<InetSocketAddress> getInetSocketAddresses(String itemName, Map<String, Credentials> credentials) {
		
		List<InetSocketAddress> result = new ArrayList<InetSocketAddress>();
		
		LCNItemMap config = (LCNItemMap) bindingConfigs.get(itemName);
		
		if(null != config) {
			
			for(Command command : config.keySet()) {
				
				InetSocketAddress address = null;
				
				try {
					address = new InetSocketAddress(InetAddress.getByName(credentials.get(config.get(command).getID()).ip), Integer.parseInt(credentials.get(config.get(command).getID()).port));
				} catch (UnknownHostException e) {
					logger.warn("Hostname {} could not be resolved", config.get(command).getID());
				}
				
				result.add(address);
				
			}
			
		}
		
		return result;
		
	}

	/**
	 * Returns an InetSocketAddress for a item and a certain openHAB Command.
	 * @param itemName The name of the item.
	 * @param command The openHAB Command.
	 * @return An InetSocketAddress.
	 */
	public InetSocketAddress getAddress(String itemName, Command command, Map<String, Credentials> credentials) {
		
		InetSocketAddress result = null;
		
		LCNItemMap config = (LCNItemMap) bindingConfigs.get(itemName);
		if (null != config) {
			LCNItemBinding element = config.get(command);
			if (null != element) {
				Credentials cred = credentials.get(element.getID());
				if (cred != null) {
					result = new InetSocketAddress(cred.ip, Integer.parseInt(cred.port));
				}
			}
		}
		
		return result;
		
	}
		
	/**
	 * Returns a list of openHAB Commands, which can be used with the given item.
	 * @param itemName The name of the item.
	 * @param command An openHAB Command.
	 * @return A list of openHAB Commands, which can be empty.
	 */
	public List<Command> getCommands(String itemName, Command command) {
		
		List<Command> result = new ArrayList<Command>();
		LCNItemMap config = (LCNItemMap) bindingConfigs.get(itemName);
		
		for (Command cmd : config.keySet()) {
			
			if (cmd.equals(command) || cmd instanceof DecimalType) { //TODO: used to be cmd == command
				
				result.add(cmd);
				
			} 
			
		}

		return result;
		
	}

	/**
	 * Returns a list of States that can be used with a certain item and command.
	 * @param itemName
	 * @param openHabCommand
	 * @return A List of States that can be used with the item and command.
	 */
	public List<Class<? extends State>> getAcceptedDataTypes(String itemName, Command openHabCommand) {
		
		List<Class<? extends State>> result = null;
		
		if(null != itemName) {
			
			LCNItemMap config = (LCNItemMap) bindingConfigs.get(itemName);
			
			if(null != config) {
				
				LCNItemBinding element = config.get(openHabCommand);
				
				if(null != element) {
					result = element.getStates();
				}
				
			}
			
		}
		
		return result;
		
	}
		
	/**
	 * Returns a list of openHAB Commands, which can be used with the given item.
	 * @param itemName The name of the item.
	 * @return A List of Commands, which can be empty.
	 */
	public List<Command> getOpenHabCommands(String itemName) {
		
		List<Command> result = new ArrayList<Command>();
		LCNItemMap config = (LCNItemMap) bindingConfigs.get(itemName);
		
		if(config != null) {
			
			for(Command cmd : config.keySet()) {
				
				result.add(cmd);
				
			}    
			
		}
		
		return result;
		
	}
	
	/**
	 * Returns whether there are still uninitialized items.
	 * @return True if there are still uninitialized items, false otherwise.
	 */
	public boolean hasUninitialized() {
		
		for (String itemName : bindingConfigs.keySet()) {
			
			LCNItemMap config = (LCNItemMap) bindingConfigs.get(itemName);
			
			for(Command cmd : config.keySet()) {
				
				if (!config.get(cmd).isInitialized()) {			
					return true;			
				}
				
			}
			
		}
		
		return false;
		
	}
	
	/**
	 * Returns whether there was a similar module updated.
	 * @param mod A LCNInputModule.
	 * @return True if there was a similar module updated, false otherwise.
	 */
	public boolean hasInitialized(LCNInputModule mod, int homeSegment) {
		
		for (String name : this.getItemNames()) {
			
			LCNItemMap conf = (LCNItemMap) bindingConfigs.get(name);
			
			for (Command cmd : conf.keySet()) {
				
				LCNInputModule temp = LCNParser.reverseParse(conf.get(cmd).getLcnshort(), conf.get(cmd).getID());
				
				if (mod.equals(temp, true, homeSegment) && conf.get(cmd).isInitialized()) {
					
					return true;
					
				}
				
			}
			
		}
		
		return false;	
		
	}
		
	/**
	 * Marks a certain LCNInputModule as initialized.
	 * @param mod The LCNInputModule.
	 */
	public void setInitialized(LCNInputModule mod, int homeSegment) {
		
		for (String itemName : this.getItemNamesByModule(mod, homeSegment)) {

			LCNItemMap conf = (LCNItemMap) bindingConfigs.get(itemName);
			
			for(Command cmd : conf.keySet()) {		
				conf.get(cmd).setInitialized(true);
			}
			
		}
		
	}
	
	/**
	 * Marks ALL LCNInputModules as uninitialized.
	 */
	public void setUninitialized() {
		
		for (String itemName : this.getItemNames()) {
			
			LCNItemMap cfg = (LCNItemMap) bindingConfigs.get(itemName);
			
			for (Command cmd : cfg.keySet()) {
				
				cfg.get(cmd).setInitialized(false);
				
			}
			
		}
		
	}
	
	/**
	 * Returns the actual openHAB item for a given itemName.
	 * @param itemName The name of the item as String.
	 * @return The actual openHAB item or null if no item with the given name was found.
	 */
	public Item getItem(String itemName) {
		
		LCNItemMap config = (LCNItemMap) bindingConfigs.get(itemName);
		if (null != config) {
			for (Command cmd : config.keySet()) {
				
				if (null != config.get(cmd) && null != config.get(cmd).getItem() && config.get(cmd).getItem().getName().equals(itemName)) {
					return config.get(cmd).getItem();
				}
				
			}
		}
		
		return null;
		
	}
	
	/**
	 * Returns the openHab command for an openHAB item, under a given openHAB command. 
	 * @param itemName The name of the openHAB item.
	 * @param command The openHAB command.
	 * @return String, the openHAB command.
	 */
	public String getOpenHabCmd(String itemName, Command command) {
		
		String result = null;
		LCNItemMap config = (LCNItemMap) bindingConfigs.get(itemName);
		if (null != config && null != config.get(command)) {
			result = config.get(command).getOpenHabCmd();
		}
		
		return result;
		
	}
	
	/**
	 * Returns the openHab cmd for an item, under a given LCN Command.
	 * @param itemName The name of the item.
	 * @param lcnShort The lcnShort Cmd.
	 * @return The openHab cmd bound to the lcnShort for that item.
	 */
	public Command getOpenHabCmd(String itemName, String lcnShort) {
		
		Command result = null;

		LCNItemMap config = (LCNItemMap) bindingConfigs.get(itemName);
		if (null != config) {
			for (Command cmd : config.keySet()) {
				
				if (null != config.get(cmd) && null != config.get(cmd).getItem() && config.get(cmd).getLcnshort().equals(lcnShort)) {
					result = cmd;
					break;
				}
				
			}
		}
		
		return result;
	}
	
	/**
	 * Returns a list of item names for items that have not been initialized yet.
	 * Initialized means, that there has been a request about data to the bus.
	 * @return A list of Strings, which can be empty.
	 */
	public List<String> getUninitialized() {
		
		List<String> list = new ArrayList<String>();
		
		for (String itemName : bindingConfigs.keySet()) {
			
			LCNItemMap config = (LCNItemMap) bindingConfigs.get(itemName);
			
			for(Command cmd : config.keySet()) {
				
				if (!config.get(cmd).isInitialized() && !list.contains(config.get(cmd).getItem().getName())) {
					list.add(config.get(cmd).getItem().getName());
				}
				
			}
			
		}
		
		return list;
		
	}
	
	/**
	 * Returns whether a certain module (or its items) were already initialized.
	 * @param mod The LCNInputModule
	 * @return True if the module (or its items) were already initialized, false otherwise.
	 */
	private boolean wasInitialized(LCNInputModule mod) {
		
		boolean result = false;
		
		for (String itemName : bindingConfigs.keySet()) {
			
			LCNItemMap conf = (LCNItemMap) bindingConfigs.get(itemName);
			
			if (null != mod && null != getFirstModule(itemName) && mod.segment == getFirstModule(itemName).segment && mod.module == getFirstModule(itemName).module 
					&& conf != null && conf.get(itemName) != null && conf.get(itemName).isInitialized()) {
				
				result = true;
				break;
				
			}
			
		}
		
		return result;
		
	}
		
	/**
	 * Returns a list of LCNInputModules which haven't been initialized yet.
	 * Initialized means, that there has been a request about data to the bus.
	 * @param uninitialized A list of item names from which the Modules are to be extracted.
	 * @return A list of LCNInputModules, which can be empty. There are no duplicates (not even similar Modules) in the list.
	 */
	public List<LCNInputModule> getUninitializedModules(List<String> uninitialized) {
		
		List<LCNInputModule> list = new ArrayList<LCNInputModule>();
		
		for (String name : uninitialized) {
			
			LCNInputModule mod = this.getFirstModule(name);
			
			if (null != mod) {
				
				if (!list.isEmpty()) {
				
					boolean isContained = false;
					
					for (int i = 0; i < list.size(); i++) {

						if (null == list.get(i) || (mod.segment == list.get(i).segment && mod.module == list.get(i).module)) {
							isContained = true;
						} 
						
					}
					
					if (!isContained && !wasInitialized(mod)) {
						list.add(mod);
					}
				
				} else {
					
					list.add(mod);
					
				}
			
			}
			
		}
		
		return list;
	}
		
	/**
	 * Returns a list of item names that refer to the same LCNInputModule.
	 * @param mod The LCNInputModule to serach by.
	 * @return A list of Strings, which can be empty.
	 */
	public List<String> getItemNamesByModule(LCNInputModule mod, int homeSegment) {
		List<String> list = new ArrayList<String>();
		
		for (String itemName : bindingConfigs.keySet()) {
			
			LCNItemMap conf = (LCNItemMap) bindingConfigs.get(itemName);
			
			for(Command cmd : conf.keySet()) {
				
				LCNInputModule tempMod = LCNParser.reverseParse(conf.get(cmd).getLcnshort(), conf.get(cmd).getID());
			
				if (null != tempMod && tempMod.equals(mod, homeSegment)) {
					
					if (!list.contains(itemName)) {
						list.add(itemName);
					}
					
				}
				
			}

		}
		
		return list;
	}
		
	/**
	 * Returns the LCNInputModule for a certain item.
	 * @param itemName The name of the item as String.
	 * @return an LCNInputModule or NULL is no matching module was found.
	 */
	public LCNInputModule getFirstModule(String itemName) {
		LCNInputModule mod = null;

		LCNItemMap map = (LCNItemMap) bindingConfigs.get(itemName);
		for(Command cmd : map.keySet()) {

			mod = LCNParser.reverseParse(map.get(cmd).getLcnshort(), map.get(cmd).getID());
			break;
			
		}
	
		return mod;
	}
	
	/**
	 * Returns the LCNItemBinding for a certain item name.
	 * @param itemName The name of the item as String.
	 * @param command The command of the desired binding.
	 * @return an LCNItemBinding or NULL is no matching binding was found.
	 */
	public LCNItemBinding getItemBinding(String itemName, Command command) {
		
		LCNItemBinding binding = null;
		
		LCNItemMap map = (LCNItemMap) bindingConfigs.get(itemName);
		
		if (null != map.get(command)) {
			
			binding = (map.get(command));
			
		}
		
		return binding;
		
	}
	
	/**
	 * Returns the LCNInputModules for a certain item and command.
	 * @param itemName The name of the item as String
	 * @param command The command.
	 * @return the LCNInputModule.
	 */
	public LCNInputModule getModule(String itemName, Command command) {
		
		LCNItemMap map = (LCNItemMap) bindingConfigs.get(itemName);
		for (Command cmd : map.keySet()) {
			
			if (cmd == command) {
				return LCNParser.reverseParse(map.get(cmd).getLcnshort(), map.get(cmd).getID());
			}
			
		}
		
		return null;
		
	}
	
	
	/**
	 * Returns all LCNInputModules for a certain item.
	 * @param itemName The name of the item as String
	 * @return a list of LCNInputModules.
	 */
	public List<LCNInputModule> getModules(String itemName) {
		
		List<LCNInputModule> result = new ArrayList<LCNInputModule>();
		
		LCNItemMap map = (LCNItemMap) bindingConfigs.get(itemName);
		for (Command cmd : map.keySet()) {
			
			result.add(LCNParser.reverseParse(map.get(cmd).getLcnshort(), map.get(cmd).getID()));
			
		}
		
		return result;
		
	}
	
	/**
	 * Returns the corresponding LCNItemMap to a given item name.
	 * @param itemName The name of the item as String.
	 * @return The corresponding LCNItemMap.
	 */
	public LCNItemMap getItemMap(String itemName) {
		return (LCNItemMap) bindingConfigs.get(itemName);
	}
	
	/**
	 * Returns the current state of an item.
	 * @param itemName The name of the item.
	 * @return The State of the item.
	 */
	public State getState(String itemName) {
		State result = null;
		
		LCNItemMap conf = (LCNItemMap) bindingConfigs.get(itemName);
		
		try {
			result = conf.get(itemName).getItem().getState();
		} catch (NullPointerException exc) {
			//do nothing
		}
		
		return result;
	}
	
	/**
	 * Checks whether this Provider provides the given module or not.
	 * @param mod The module in question.
	 * @return True if this provider provides the module, false otherwise.
	 */
	public boolean contains(LCNInputModule mod) {
		
		boolean result = false;
		
		for (String itemName : bindingConfigs.keySet()) {
			LCNItemMap map = (LCNItemMap) bindingConfigs.get(itemName);
			for (Command cmd : map.keySet()) {
				
				if (map.get(cmd).getID().equals(mod.id) && map.get(cmd).getLcnshort().equals(mod.LcnShort)) {
					result = true;
					break;
				}
				
			}
			
		}
		return result;
		
	}
	
	/**
	 * Removes an item form the bindingConfig.
	 * @param itemName The name of the item.
	 */
	public void removeItem(String itemName) {
		this.bindingConfigs.remove(itemName);		
	}
	
	/**
	 * {inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "lcn";
	}

	/**
	 * {inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//Everything is accepted!
	}

}
