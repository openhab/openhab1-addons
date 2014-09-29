/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tcp.protocol.internal;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.tcp.Direction;
import org.openhab.binding.tcp.protocol.ProtocolBindingProvider;
import org.openhab.core.binding.BindingConfig;
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
 * 
 * The "standard" TCP and UDP network bindings follow the same configuration format:
 * 
 * tcp=">[ON:192.168.0.1:3000:'some text'], >[OFF:192.168.0.1:3000:'some other command']"
 * tcp="<[192.168.0.1:3000:'some text']" - for String Items
 * 
 * direction[openhab command:hostname:port number:protocol command]
 * 
 * For String Items, the Item will be updated with the incoming string
 * openhab commands can be repeated more than once for a given Item, e.g. receiving ON command could trigger to pieces
 * of data to be sent to for example to different host:port combinations,...
 * 
 * @author Karel Goderis
 * @since 1.1.0
 */
abstract class ProtocolGenericBindingProvider extends AbstractGenericBindingProvider implements ProtocolBindingProvider {

	static final Logger logger = 
			LoggerFactory.getLogger(ProtocolGenericBindingProvider.class);

	/** {@link Pattern} which matches a binding configuration part */
	private static final Pattern BASE_CONFIG_PATTERN = Pattern.compile("([<|>]\\[.*?\\])*");
	private static final Pattern ACTION_CONFIG_PATTERN = Pattern.compile("(<|>)\\[(.*?):(.*?):(.*?):\'?(.*?)\'?\\]");
	private static final Pattern STATUS_CONFIG_PATTERN = Pattern.compile("(<|>)\\[(.*):(.*):\'?(.*?)\'?\\]");

	static int counter = 0;


	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException { 
		// All Item Types are accepted by ProtocolGenericBindingProvider
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			parseAndAddBindingConfig(item, bindingConfig);
		} else {
			logger.warn(getBindingType()+" bindingConfig is NULL (item=" + item
					+ ") -> processing bindingConfig aborted!");
		}
	}

	private void parseAndAddBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
		ProtocolBindingConfig newConfig = new ProtocolBindingConfig();
		Matcher matcher = BASE_CONFIG_PATTERN.matcher(bindingConfig);

		if (!matcher.matches()) {
			throw new BindingConfigParseException("bindingConfig '" + bindingConfig + "' doesn't contain a valid binding configuration");
		}
		matcher.reset();

		while (matcher.find()) {
			String bindingConfigPart = matcher.group(1);
			if (StringUtils.isNotBlank(bindingConfigPart)) {
				parseBindingConfig(newConfig,item, bindingConfigPart);
				addBindingConfig(item, newConfig);
			}
		}
	}

	/**
	 * Parses the configuration string and update the provided config
	 * 
	 * @param config - the Configuration that needs to be updated with the parsing results
	 * @param item - the Item that this configuration is intented for
	 * @param bindingConfig - the configuration string that will be parsed
	 * @throws BindingConfigParseException
	 */
	private void parseBindingConfig(ProtocolBindingConfig config,Item item,
			String bindingConfig) throws BindingConfigParseException {

		String direction = null;
		Direction directionType = null;
		String commandAsString = null;
		String host = null;
		String port = null;
		String protocolCommand = null;

		if(bindingConfig != null){

			Matcher actionMatcher = ACTION_CONFIG_PATTERN.matcher(bindingConfig);
			Matcher statusMatcher = STATUS_CONFIG_PATTERN.matcher(bindingConfig);


			if ((!actionMatcher.matches() && !statusMatcher.matches())) {
				throw new BindingConfigParseException(getBindingType()+
						" binding configuration must consist of four [config="+statusMatcher+"] or five parts [config="
						+ actionMatcher + "]");
			} else {
				if(actionMatcher.matches()) {
					direction = actionMatcher.group(1);
					commandAsString = actionMatcher.group(2);
					host = actionMatcher.group(3);
					port = actionMatcher.group(4);
					protocolCommand = actionMatcher.group(5);					
				} else if (statusMatcher.matches()) {
					direction = statusMatcher.group(1);
					commandAsString = null;
					host = statusMatcher.group(2);
					port = statusMatcher.group(3);
					protocolCommand = statusMatcher.group(4);
				}

				if (direction.equals(">")){
					directionType = Direction.OUT;
				} else if (direction.equals("<")){
					directionType = Direction.IN;
				}

				ProtocolBindingConfigElement newElement = new ProtocolBindingConfigElement(host,port,directionType,protocolCommand,item.getAcceptedDataTypes());

				Command command = null;
				if(commandAsString == null) {
					// for those configuration strings that are not really linked to a openHAB command we
					// create a dummy Command to be able to store the configuration information
					// I have choosen to do that with NumberItems
					NumberItem dummy = new NumberItem(Integer.toString(counter));
					command = createCommandFromString(dummy,Integer.toString(counter));
					counter++;
				} else { 
					command = createCommandFromString(item, commandAsString);
				}

				config.put(command, newElement);

			}
		}
		else
		{
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
	 * {@inheritDoc}
	 */
	public String getHost(String itemName, Command command) {
		ProtocolBindingConfig config = (ProtocolBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(command) != null ? config.get(command).getHost() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getPort(String itemName, Command command) {
		ProtocolBindingConfig config = (ProtocolBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(command) != null ? Integer.parseInt(config.get(command).getPort()) : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getPortAsString(String itemName, Command command) {
		ProtocolBindingConfig config = (ProtocolBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(command) != null ? config.get(command).getPort() : null;
	}


	/**
	 * {@inheritDoc}
	 */
	public Direction getDirection(String itemName, Command command) {
		ProtocolBindingConfig config = (ProtocolBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(command) != null ? config.get(command).getDirection() : null;
	}


	/**
	 * {@inheritDoc}
	 */
	public String getProtocolCommand(String itemName, Command command) {
		ProtocolBindingConfig config = (ProtocolBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(command) != null ? config.get(command).getNetworkCommand() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Command> getQualifiedCommands(String itemName,Command command){
		List<Command> commands = new ArrayList<Command>();
		ProtocolBindingConfig aConfig = (ProtocolBindingConfig) bindingConfigs.get(itemName);
		for(Command aCommand : aConfig.keySet()) {
			if(aCommand == command) {
				commands.add(aCommand);
			} else {
				if(aCommand instanceof DecimalType) {
					commands.add(aCommand);
				}				
			}
		}

		return commands;
	}

	/**
	 * This is an internal data structure to map commands to 
	 * {@link ProtocolBindingConfigElement }. There will be map like 
	 * <code>ON->ProtocolBindingConfigElement</code>
	 */
	static class ProtocolBindingConfig extends HashMap<Command, ProtocolBindingConfigElement> implements BindingConfig {

		private static final long serialVersionUID = 6363085986521089771L;

	}


	static class ProtocolBindingConfigElement implements BindingConfig {

		final private String host;
		final private String port;
		final private Direction direction;
		final private String networkCommand;
		final private List<Class<? extends State>> acceptedTypes;


		public ProtocolBindingConfigElement(String host, String port, Direction direction, String networkCommand,List<Class<? extends State>> acceptedTypes) {
			this.host = host;
			this.port = port;
			this.direction = direction;
			this.networkCommand = networkCommand;
			this.acceptedTypes = acceptedTypes;
		}

		@Override
		public String toString() {
			return "ProtocolBindingConfigElement [Direction=" + direction
					+ ", host=" + host + ", port=" + port
					+ ", cmd=" + networkCommand + "]";
		}


		/**
		 * @return the networkCommand
		 */
		public String getNetworkCommand() {
			return networkCommand;
		}

		/**
		 * @return the direction
		 */
		public Direction getDirection() {
			return direction;
		}

		/**
		 * @return the host
		 */
		public String getHost() {
			return host;
		}

		/**
		 * @return the port
		 */
		public String getPort() {
			return port;
		}

		/**
		 * @return the list of accepted DataTypes for the Item linked to this Binding Config Element
		 */
		public List<Class<? extends State>> getAcceptedTypes() {
			return acceptedTypes;
		}
	}

	@Override
	public InetSocketAddress getInetSocketAddress(String itemName, Command command) {

		ProtocolBindingConfig config = (ProtocolBindingConfig) bindingConfigs.get(itemName);
		ProtocolBindingConfigElement element = config.get(command);
		InetSocketAddress socketAddress = new InetSocketAddress(element.getHost(),Integer.parseInt(element.getPort()));

		return socketAddress;
	}

	@Override
	public List<InetSocketAddress> getInetSocketAddresses(String itemName){
		List<InetSocketAddress> theList = new ArrayList<InetSocketAddress>();
		ProtocolBindingConfig config = (ProtocolBindingConfig) bindingConfigs.get(itemName);
		if(config != null ){
			for(Command command : config.keySet()) {
				InetSocketAddress anAddress = null;
				try {
					anAddress = new InetSocketAddress(InetAddress.getByName(config.get(command).getHost()),Integer.parseInt(config.get(command).getPort()));
				} catch (UnknownHostException e) {
					logger.warn("Could not resolve the hostname {} for item {}",config.get(command).getHost(),itemName);
				}
				theList.add(anAddress);
			}
		}
		return theList;
	}

	public Collection<String> getItemNames(String host, int port) {

		List<String> items = new ArrayList<String>();

		for (String itemName : bindingConfigs.keySet()) {
			ProtocolBindingConfig aConfig = (ProtocolBindingConfig) bindingConfigs.get(itemName);
			for(Command aCommand : aConfig.keySet()) {
				ProtocolBindingConfigElement anElement = (ProtocolBindingConfigElement) aConfig.get(aCommand);				
				if(anElement.getHost().equals(host) && anElement.getPort()==Integer.toString(port)) {
					if(!items.contains(itemName)) {
						items.add(itemName);
					}
				}			
			}
		}
		return items; 

	}

	@Override
	public List<String> getItemNames(String protocolCommand) {
		List<String> itemNames = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			ProtocolBindingConfig aConfig = (ProtocolBindingConfig) bindingConfigs.get(itemName);
			for(Command aCommand : aConfig.keySet()) {
				ProtocolBindingConfigElement anElement = (ProtocolBindingConfigElement) aConfig.get(aCommand);
				if(anElement.networkCommand.equals(protocolCommand)) {
					itemNames.add(itemName);
				}
			}

		}
		return itemNames;               
	}

	public List<Command> getAllCommands(String itemName, String protocolCommand){
		List<Command> commands = new ArrayList<Command>();
		ProtocolBindingConfig aConfig = (ProtocolBindingConfig) bindingConfigs.get(itemName);
		if(aConfig!=null) {
			for(Command aCommand : aConfig.keySet()) {
				ProtocolBindingConfigElement anElement = (ProtocolBindingConfigElement) aConfig.get(aCommand);
				if(anElement.networkCommand.equals(protocolCommand)) {
					commands.add(aCommand);
				}
			} 
		}
		return commands;
	}

	public List<Command> getAllCommands(String itemName) {
		List<Command> commands = new ArrayList<Command>();
		ProtocolBindingConfig aConfig = (ProtocolBindingConfig) bindingConfigs.get(itemName);
		if(aConfig != null) {
			for(Command aCommand : aConfig.keySet()) {
				commands.add(aCommand);
			}    
		}
		return commands;
	}

	public List<Class<? extends State>> getAcceptedDataTypes(String itemName, Command command) {
		if(itemName != null) {
			ProtocolBindingConfig config = (ProtocolBindingConfig) bindingConfigs.get(itemName);
			if(config != null) {
				ProtocolBindingConfigElement element = config.get(command);
				if(element != null) {
					return element.getAcceptedTypes();
				}
			}
		}
		return null;
	}

}