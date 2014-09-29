/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.plugwise.PlugwiseBindingProvider;
import org.openhab.binding.plugwise.PlugwiseCommandType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.types.Command;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation class of the Plugwise Binding Provider
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class PlugwiseGenericBindingProvider extends AbstractGenericBindingProvider implements PlugwiseBindingProvider {
	
	static final Logger logger = 
		LoggerFactory.getLogger(PlugwiseGenericBindingProvider.class);
	
	static int counter = 0;

	/** {@link Pattern} which matches a binding configuration part */
	private static final Pattern ACTION_CONFIG_PATTERN = Pattern.compile("\\[(.*):(.*):(.*):(.*)\\]");
	private static final Pattern STATUS_CONFIG_PATTERN = Pattern.compile("\\[(.*):(.*):(.*)\\]");


	@Override
	public String getBindingType() {
		return "plugwise";
	}

	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof NumberItem || item instanceof StringItem || item instanceof DateTimeItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch-, String and NumberItems are allowed - please check your *.items configuration");
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			parseAndAddBindingConfig(item, bindingConfig);
		} else {
			logger.warn(getBindingType()+" bindingConfig is NULL (item=" + item
					+ ") -> processing bindingConfig aborted!");
		}
	}

	private void parseAndAddBindingConfig(Item item,
			String bindingConfigs) throws BindingConfigParseException {

		String bindingConfig = StringUtils.substringBefore(bindingConfigs, ",");
		String bindingConfigTail = StringUtils.substringAfter(bindingConfigs, ",");

		PlugwiseBindingConfig newConfig = new PlugwiseBindingConfig();
		parseBindingConfig(newConfig,item,bindingConfig);
		addBindingConfig(item, newConfig);              

		while (StringUtils.isNotBlank(bindingConfigTail)) {
			bindingConfig = StringUtils.substringBefore(bindingConfigTail, ",");
			bindingConfig = StringUtils.strip(bindingConfig);
			bindingConfigTail = StringUtils.substringAfter(bindingConfig, ",");
			parseBindingConfig(newConfig,item, bindingConfig);
			addBindingConfig(item, newConfig);      
		}

	}
	
	/**
	 * Parses the configuration string and update the provided config
	 * 
	 * @param config
	 * @param item
	 * @param bindingConfig
	 * @throws BindingConfigParseException
	 */
	private void parseBindingConfig(PlugwiseBindingConfig config,Item item,
			String bindingConfig) throws BindingConfigParseException {
		
		String commandAsString = null;
		String plugwiseID = null;
		String plugwiseCommand = null;
		int interval = 60;

		if(bindingConfig != null){

			Matcher actionMatcher = ACTION_CONFIG_PATTERN.matcher(bindingConfig);
			Matcher statusMatcher = STATUS_CONFIG_PATTERN.matcher(bindingConfig);

			if (!actionMatcher.matches() && !statusMatcher.matches()) {
				throw new BindingConfigParseException(
						"Plugwise binding configuration must consist of either three [config="
								+ statusMatcher + "] or four parts [config="+actionMatcher+"]");
			} else {	
				if(actionMatcher.matches()) {
					commandAsString = actionMatcher.group(1);
					plugwiseID = actionMatcher.group(2);
					plugwiseCommand = actionMatcher.group(3);
					interval = Integer.valueOf(actionMatcher.group(4));	
				} else if(statusMatcher.matches()){
					commandAsString = null;
					plugwiseID = statusMatcher.group(1);
					plugwiseCommand = statusMatcher.group(2);
					interval = Integer.valueOf(statusMatcher.group(3));
				}

				PlugwiseCommandType type = PlugwiseCommandType.getCommandType(plugwiseCommand);	

				if(PlugwiseCommandType.validateBinding(type, item)) {

					PlugwiseBindingConfigElement newElement = new PlugwiseBindingConfigElement(plugwiseID,type,interval);

					Command command = null;
					if(commandAsString == null) {
						// for those configuration strings that are not really linked to a openHAB command we
						// create a dummy Command to be able to store the configuration information
						// I have choosen to do that with NumberItems
						NumberItem dummy = new NumberItem(Integer.toString(counter));
						command = createCommandFromString(dummy,Integer.toString(counter));
						counter++;
						config.put(command, newElement);						
					} else { 
						command = createCommandFromString(item, commandAsString);
						config.put(command, newElement);
					}
				} else {
					String validItemType = PlugwiseCommandType.getValidItemTypes(plugwiseCommand);
					if (StringUtils.isEmpty(validItemType)) {
						throw new BindingConfigParseException("'" + bindingConfig
								+ "' is no valid binding type");					
					} else {
						throw new BindingConfigParseException("'" + bindingConfig
								+ "' is not bound to a valid item type. Valid item type(s): " + validItemType) ;
					}
				}
			}
		}
		else {
			return;
		}
	}
	
	/**
	 * Creates a {@link Command} out of the given <code>commandAsString</code>
	 * incorporating the {@link TypeParser}.
	 *  
	 * @param item
	 * @param commandAsString
	 * 
	 * @return an appropriate Command (see {@link TypeParser} for more 
	 * information
	 * 
	 * @throws BindingConfigParseException if the {@link TypeParser} couldn't
	 * create a command appropriately
	 * 
	 * @see {@link TypeParser}
	 */
	private Command createCommandFromString(Item item, String commandAsString) throws BindingConfigParseException {
		
		Command command = TypeParser.parseCommand(
				item.getAcceptedCommandTypes(), commandAsString);

		if (command == null) {
			throw new BindingConfigParseException("couldn't create Command from '" + commandAsString + "' ");
		}

		return command;
	}
	
	/**
	 * This is an internal data structure to map commands to 
	 * {@link ProtocolBindingConfigElement }. There will be map like 
	 * <code>ON->ProtocolBindingConfigElement</code>
	 */
	static class PlugwiseBindingConfig extends HashMap<Command, PlugwiseBindingConfigElement> implements BindingConfig {
		
		private static final long serialVersionUID = -7252828812548386063L;
	}
	
	public static class PlugwiseBindingConfigElement implements BindingConfig {

		final private String id;
		final private int interval;
		final private PlugwiseCommandType type;


		public String getId() {
			return id;
		}


		public int getInterval() {
			return interval;
		}
		
		public PlugwiseCommandType getCommandType() {
			return type;
		}

		public PlugwiseBindingConfigElement(String id, PlugwiseCommandType type,int interval) {
			this.id = id;
			this.type = type;
			this.interval = interval;
		}

		@Override
		public String toString() {
			return "PlugwiseBindingConfigElement [id=" +id
					+ ", type=" + type.toString()
					+ ", interval=" + interval +"]";
		}

	}

	@Override
	public Boolean autoUpdate(String itemName) {
		return false;
	}


	@Override
	public String getPlugwiseID(String itemName, Command someCommand) {
		if(itemName != null && someCommand != null) {
			PlugwiseBindingConfig aConfig = (PlugwiseBindingConfig) bindingConfigs.get(itemName);
			if(aConfig != null) {
				PlugwiseBindingConfigElement element = aConfig.get(someCommand);
				if(element!=null) {
					return element.getId();
				}
			}
		}
		return null;
	}
	
	@Override
	public PlugwiseCommandType getPlugwiseCommandType(String itemName, Command someCommand) {
		if(itemName != null && someCommand != null) {
			PlugwiseBindingConfig aConfig = (PlugwiseBindingConfig) bindingConfigs.get(itemName);
			if(aConfig != null) {
				PlugwiseBindingConfigElement element = aConfig.get(someCommand);
				if(element!=null) {
					return element.getCommandType();
				}
			}
		}
		return null;
	}

	@Override
	public List<String> getPlugwiseID(String itemName) {
		List<String> ids = new ArrayList<String>();
		for(String anItem : bindingConfigs.keySet()) {
			PlugwiseBindingConfig aConfig = (PlugwiseBindingConfig) bindingConfigs.get(anItem);
			for(Command aCommand : aConfig.keySet()) {
				PlugwiseBindingConfigElement element = aConfig.get(aCommand);
				if(element != null) {
					ids.add(element.getId());
				}
			}
		}
		return ids;
	}

	@Override
	public Set<String> getItemNames(String PlugwiseID, PlugwiseCommandType type) {
		
		Set<String> result = new HashSet<String>();
		
		Collection<String> items = getItemNames();

		Iterator<String> itemIterator = items.iterator();
		while(itemIterator.hasNext()) {
			String anItem = itemIterator.next();

			PlugwiseBindingConfig pbConfig = (PlugwiseBindingConfig) bindingConfigs.get(anItem);
			for(Command command : pbConfig.keySet()) {
				PlugwiseBindingConfigElement element = pbConfig.get(command);

				if(element.getCommandType() == type && element.getId().equals(PlugwiseID)) {
					if(!result.contains(anItem)) {
						result.add(anItem);
					}
				}

			}

		}
		return result;
	}

	@Override
	public List<PlugwiseBindingConfigElement> getIntervalList() {
	
		List<PlugwiseBindingConfigElement> result = new ArrayList<PlugwiseBindingConfigElement>();
		
		Collection<String> items = getItemNames();
		
		Iterator<String> itemIterator = items.iterator();
		while(itemIterator.hasNext()) {
			String anItem = itemIterator.next();
			
			PlugwiseBindingConfig pbConfig = (PlugwiseBindingConfig) bindingConfigs.get(anItem);
			for(Command command : pbConfig.keySet()) {
				boolean found = false;
				PlugwiseBindingConfigElement element = pbConfig.get(command);
				
				// check if we already have a reference to this {ID,command}
				Iterator<PlugwiseBindingConfigElement> elementIterator = result.iterator();
				while(elementIterator.hasNext()) {
					PlugwiseBindingConfigElement resultElement = elementIterator.next();
					if(resultElement.getId().equals(element.getId()) && resultElement.getCommandType().getJobClass().equals(element.getCommandType().getJobClass())) {
						// bingo - now check if the interval is smaller
						found = true;
						if(resultElement.getInterval() > element.getInterval()) {
							result.remove(resultElement);
							result.add(element);
							break;
						}
					}
				}
				
				if(!found) {
					result.add(element);
				}	
			}	
		}
		return result;	
	}

	@Override
	public List<Command> getCommandsByType(String itemName,Class<? extends Command> class1) {
		List<Command> commands = new ArrayList<Command>();
			PlugwiseBindingConfig aConfig = (PlugwiseBindingConfig) bindingConfigs.get(itemName);
			for(Command aCommand : aConfig.keySet()) {
				PlugwiseBindingConfigElement element = aConfig.get(aCommand);
				if(element.getCommandType().getTypeClass().equals(class1)) {
					commands.add(aCommand);
				}
		}
		return commands;
	}

	@Override
	public List<Command> getAllCommands(String itemName) {
		List<Command> commands = new ArrayList<Command>();
		PlugwiseBindingConfig aConfig = (PlugwiseBindingConfig) bindingConfigs.get(itemName);
		for(Command aCommand : aConfig.keySet()) {
			commands.add(aCommand);
		}
		return commands;
	}

}
