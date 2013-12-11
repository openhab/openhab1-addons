/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sonos.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.sonos.SonosBindingProvider;
import org.openhab.binding.sonos.SonosCommandType;
import org.openhab.binding.sonos.SonosIllegalCommandTypeException;
import org.openhab.binding.sonos.internal.Direction;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * sonos commands will be limited to the simple commands that take only up to one input parameter. unpnp actions
 * requiring input variables could potentially take their inputs from elsewhere in the binding, e.g. config parameters
 * or other
 *
 * sonos=">[ON:office:play], >[OFF:office:stop]" - switch items for ordinary sonos commands
 * 		using openhab command : player name : sonos command as format
 * 
 * sonos="<[office:getcurrenttrack]" - string and number items for UPNP service variable updates using
 * 		using player_name : somecommand, where somecommand takes a simple input/output value from/to the string
 * 
 * @author Karel Goderis
 * @author Pauli Anttila
 * @since 1.1.0
 */

public class SonosGenericBindingProvider extends AbstractGenericBindingProvider
		implements SonosBindingProvider {

	static final Logger logger = LoggerFactory
			.getLogger(SonosGenericBindingProvider.class);
	
	/** {@link Pattern} which matches a binding configuration part */
	private static final Pattern ACTION_CONFIG_PATTERN = Pattern.compile("(<|>|\\*)\\[(.*):(.*):(.*)\\]");
	private static final Pattern STATUS_CONFIG_PATTERN = Pattern.compile("(<|>|\\*)\\[(.*):(.*)\\]");
	
	static int counter = 0;
		
	public String getBindingType() {
		return "sonos";
	}

	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		// All Item Types are accepted by SonosGenericBindingProvider
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
			logger.warn("bindingConfig is NULL (item=" + item
					+ ") -> processing bindingConfig aborted!");
		}
	}
	
	private void parseAndAddBindingConfig(Item item,
			String bindingConfigs) throws BindingConfigParseException {

		String bindingConfig = StringUtils.substringBefore(bindingConfigs, ",");
		String bindingConfigTail = StringUtils.substringAfter(bindingConfigs,
				",");

		SonosBindingConfig newConfig = new SonosBindingConfig();
		parseBindingConfig(newConfig,item,bindingConfig);
		addBindingConfig(item, newConfig);              

		while (StringUtils.isNotBlank(bindingConfigTail)) {
			bindingConfig = StringUtils.substringBefore(bindingConfigTail, ",");
			bindingConfig = StringUtils.strip(bindingConfig);
			bindingConfigTail = StringUtils.substringAfter(bindingConfig,
					",");
			parseBindingConfig(newConfig,item, bindingConfig);
			addBindingConfig(item, newConfig);      
		}
	}
	
	private void parseBindingConfig(SonosBindingConfig config,Item item,
			String bindingConfig) throws BindingConfigParseException {
		

		String direction = null;
		String sonosID = null;
		String commandAsString = null;
		String sonosCommand = null;

		if(bindingConfig != null){

			Matcher actionMatcher = ACTION_CONFIG_PATTERN.matcher(bindingConfig);
			Matcher statusMatcher = STATUS_CONFIG_PATTERN.matcher(bindingConfig);

			
			if (!actionMatcher.matches() && !statusMatcher.matches()) {
				throw new BindingConfigParseException(
						"Sonos binding configuration must consist of either three [config="
								+ statusMatcher + "] or four parts [config="+actionMatcher+"]");
			} else {	
				if(actionMatcher.matches()) {
					direction = actionMatcher.group(1);
					commandAsString = actionMatcher.group(2);
					sonosID = actionMatcher.group(3);
					sonosCommand = actionMatcher.group(4);


				} else if(statusMatcher.matches()){
					direction = statusMatcher.group(1);
					commandAsString = null;
					sonosID = statusMatcher.group(2);
					sonosCommand = statusMatcher.group(3);
				}

				Direction directionType = Direction.BIDIRECTIONAL;

				if(direction.equals(">")){
					directionType = Direction.OUT;
				}else if (direction.equals("<")){
					directionType = Direction.IN;
				}else if (direction.equals("*")){
					directionType = Direction.BIDIRECTIONAL;
				}

				SonosCommandType type = null;
				try {
					type = SonosCommandType.getCommandType(sonosCommand,directionType);	
				} catch(SonosIllegalCommandTypeException e) {
					logger.error("Error parsing binding configuration : {}", e.getMessage());
					throw new BindingConfigParseException(
							"Sonos binding configuration error : " + sonosCommand.toString() +
									 " does not match the direction of type "+directionType.toString()+"]");
				}
				
				
				if(SonosCommandType.validateBinding(type, item)) {

					SonosBindingConfigElement newElement = new SonosBindingConfigElement(directionType,sonosID,type);

					Command command = null;
					if(commandAsString == null) {

						if(item instanceof NumberItem || item instanceof StringItem){
							command = createCommandFromString(item,Integer.toString(counter));
							counter++;
							config.put(command, newElement);
						} else {
							logger.warn("Only NumberItem or StringItem can have undefined command types");
						}								
					} else { 
						command = createCommandFromString(item, commandAsString);
						config.put(command, newElement);
					}
				} else {
					String validItemType = SonosCommandType.getValidItemTypes(sonosCommand);
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
		else
			return;

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
	 * {@link SonosBindingConfigElement }. There will be map like 
	 * <code>ON->SonosBindingConfigElement</code>
	 */
	static class SonosBindingConfig extends HashMap<Command, SonosBindingConfigElement> implements BindingConfig {

		private static final long serialVersionUID = 1943053272317438930L;

	}

	
	static class SonosBindingConfigElement implements BindingConfig {

		final private Direction direction;
		final private String id;
		final private SonosCommandType type;


		public SonosBindingConfigElement(Direction direction, String sonosID, SonosCommandType type) {
			this.direction = direction;
			this.id = sonosID;
			this.type = type;
		}

		@Override
		public String toString() {
			return "SonosBindingConfigElement [Direction=" + direction
					+ ", id=" + id 
					+ ", type=" + type.toString() + "]";
		}

		/**
		 * @return the id
		 */
		public String getSonosID() {
			return id;
		}


		/**
		 * @return the command
		 */
		public SonosCommandType getCommandType() {
			return type;
		}

		/**
		 * @return the direction
		 */
		public Direction getDirection() {
			return direction;
		}

	}


	public List<String> getSonosID(String itemName) {
		List<String> ids = new ArrayList<String>();
		SonosBindingConfig aConfig = (SonosBindingConfig) bindingConfigs.get(itemName);
		for(Command aCommand : aConfig.keySet()) {
			ids.add(aConfig.get(aCommand).getSonosID());
		}

		return ids;
	}

	public String getSonosID(String itemName, Command aCommand) {
		SonosBindingConfig aConfig = (SonosBindingConfig) bindingConfigs.get(itemName);
		return aConfig != null && aConfig.get(aCommand) != null ? aConfig.get(aCommand).getSonosID() : null;
	}

	public SonosCommandType getSonosCommandType(String itemName, Command aCommand, Direction direction) {
		SonosBindingConfig aBindingConfig = (SonosBindingConfig) bindingConfigs.get(itemName);
		if(aBindingConfig != null) {
			for(Command command : aBindingConfig.keySet()){
				SonosBindingConfigElement anElement = aBindingConfig.get(command);
				if(command == aCommand && anElement.getDirection().equals(direction)){
					return anElement.getCommandType();
				}
			}
		}
		return null;		
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Direction getDirection(String itemName, Command command) {
		SonosBindingConfig config = (SonosBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(command) != null ? config.get(command).getDirection() : null;
	}

	public List<String> getItemNames(String sonosID, SonosCommandType sonosCommandType) {
		List<String> result = new ArrayList<String>();
		Collection<String> items = getItemNames();
		for(String anItem : items) {
			SonosBindingConfig aBindingConfig = (SonosBindingConfig) bindingConfigs.get(anItem);
			for(Command command : aBindingConfig.keySet()){
				SonosBindingConfigElement anElement = aBindingConfig.get(command);
				if(anElement.getCommandType().equals(sonosCommandType) && anElement.getSonosID().equals(sonosID) && !result.contains(anItem)){
					result.add(anItem);
				}
			}
		}
		return result;
	
	}

	public List<Command> getCommands(String anItem,SonosCommandType sonosCommandType) {
		List<Command> commands = new ArrayList<Command>();
		SonosBindingConfig aConfig = (SonosBindingConfig) bindingConfigs.get(anItem);
		for(Command aCommand : aConfig.keySet()) {
			SonosBindingConfigElement anElement = aConfig.get(aCommand);
			if(anElement.getCommandType().equals(sonosCommandType)) {
				commands.add(aCommand);
			}
		}
		return commands;
	}

	public List<Command> getVariableCommands(String anItem) {
		List<Command> commands = new ArrayList<Command>();
		SonosBindingConfig aConfig = (SonosBindingConfig) bindingConfigs.get(anItem);
		for(Command aCommand : aConfig.keySet()) {
			if(aCommand instanceof StringType || aCommand instanceof DecimalType) {
				commands.add(aCommand);
			}
		}
		return commands;
	}

	
}
