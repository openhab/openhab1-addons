/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.irtrans.internal;

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
import org.openhab.binding.irtrans.IRtransBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.binding.tcp.Direction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class IRtransGenericBindingProvider.
 *
 * @author Karel Goderis
 * @since 1.4.0
 * 
 * 
 * irtrans="[ON:192.168.0.1:3000:1:Samsung:Play], [OFF:192.168.0.1:3000:2:Samsung:Pause]"
 * 
 * [ON:192.168.0.1:3000:1:Samsung:Play] - send the Play command for the remote Samsung type
 * [ON:192.168.0.1:3000:1:Samsung:*] - accept any command from remote Samsung type
 * [ON:192.168.0.1:3000:*:Samsung:Play] - send command to all leds
 * [ON:192.168.0.1:3000:1:*:*] - accept all commands from any type of remote
 * [192.168.0.1:3000:1:*:*] - for String Items, take or update 'remote,command' strings that match the pattern
 */


public class IRtransGenericBindingProvider extends AbstractGenericBindingProvider implements IRtransBindingProvider {

	/** The Constant logger. */
	static final Logger logger = LoggerFactory
			.getLogger(IRtransGenericBindingProvider.class);

	/** The patterns {@link Pattern} which matches the binding configuration parts */	
	private static final Pattern ACTION_CONFIG_PATTERN = Pattern.compile("\\[(.*):(.*):(.*):(.*):(.*):(.*)\\]");
	private static final Pattern STATUS_CONFIG_PATTERN = Pattern.compile("\\[(.*):(.*):(.*):(.*):(.*)\\]");

    static int counter = 0;
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "irtrans";
	}

	/**
	 * {@inheritDoc}
	 */
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {     
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

	/**
	 * Parses the and add binding config.
	 *
	 * @param item the item
	 * @param bindingConfigs the binding configs
	 * @throws BindingConfigParseException the binding config parse exception
	 */
	private void parseAndAddBindingConfig(Item item,
			String bindingConfigs) throws BindingConfigParseException {

		String bindingConfig = StringUtils.substringBefore(bindingConfigs, ",");
		String bindingConfigTail = StringUtils.substringAfter(bindingConfigs,
				",");

		IRtransBindingConfig newConfig = new IRtransBindingConfig();
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

	
	/**
	 * Parses the binding config.
	 *
	 * @param config the config
	 * @param item the item
	 * @param bindingConfig the binding config
	 * @throws BindingConfigParseException the binding config parse exception
	 */
	private void parseBindingConfig(IRtransBindingConfig config,Item item,
			String bindingConfig) throws BindingConfigParseException {
		
		String commandAsString = null;
		String host = null;
		String port = null;
		String led = null;
		String remote = null;
		String irCommand = null;
		Leds ledType = Leds.DEFAULT;	

		if(bindingConfig != null){

			Matcher actionMatcher = ACTION_CONFIG_PATTERN.matcher(bindingConfig);
            Matcher statusMatcher = STATUS_CONFIG_PATTERN.matcher(bindingConfig);
            
            
			if ((!actionMatcher.matches() && !statusMatcher.matches())) {
				throw new BindingConfigParseException(getBindingType()+
						" binding configuration must consist of five [config="+statusMatcher.pattern()+"] or six parts [config="
								+ actionMatcher.pattern() + "]");
			} else {
				if(actionMatcher.matches()) {
					 commandAsString = actionMatcher.group(1);
					 host = actionMatcher.group(2);
					 port = actionMatcher.group(3);
					 led = actionMatcher.group(4);
					 remote = actionMatcher.group(5);
					 irCommand = actionMatcher.group(6);
				} else if (statusMatcher.matches()) {
					 host = statusMatcher.group(1);
					 port = statusMatcher.group(2);
					 led = statusMatcher.group(3);
					 remote = statusMatcher.group(4);
					 irCommand = statusMatcher.group(5);
				}

				if(led.equals("*")){
					ledType = Leds.ALL;
				} else {
					ledType = Leds.valueOf(led);
				}

				IRtransBindingConfigElement newElement = new IRtransBindingConfigElement(host,port,ledType,remote,irCommand,item.getAcceptedDataTypes());

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
				
				config.put(command, newElement);
			
			}
		}
		else
			return;

	}

	/**
	 * Creates a {@link Command} out of the given <code>commandAsString</code>
	 * incorporating the {@link TypeParser}.
	 *
	 * @param item the item
	 * @param commandAsString the command as string
	 * @return an appropriate Command (see {@link TypeParser} for more
	 * information
	 * @throws BindingConfigParseException if the {@link TypeParser} couldn't
	 * create a command appropriately
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
	 * This is an internal data structure to map commands to.
	 *
	 * {@link IRtransBindingConfigElement }. There will be map like
	 * <code>ON->IRtransBindingConfigElement</code>
	 */
	static class IRtransBindingConfig extends HashMap<Command, IRtransBindingConfigElement> implements BindingConfig {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 7225634469734836948L;

	}


	/**
	 * The Class IRtransBindingConfigElement.
	 */
	static class IRtransBindingConfigElement implements BindingConfig {

		/** The host. */
		final private String host;
		
		/** The port. */
		final private String port;
		
		/** The led. */
		final private Leds led;
		
		/** The remote. */
		final private String remote;
		
		/** The ir command. */
		final private String irCommand;

		final private List<Class<? extends State>> acceptedTypes;


		/**
		 * Instantiates a new ir trans binding config element.
		 *
		 * @param host the host
		 * @param port the port
		 * @param led the led
		 * @param direction the direction
		 * @param remote the remote
		 * @param irCommand the ir command
		 */
		public IRtransBindingConfigElement(String host, String port, Leds led, String remote, String irCommand,List<Class<? extends State>> acceptedTypes) {
			this.host = host;
			this.port = port;
			this.led = led;
			this.remote = remote;
			this.irCommand = irCommand;
			this.acceptedTypes = acceptedTypes;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "IRtransBindingConfigElement ["
					+ ", host=" + host + ", port=" + port
					+ ", led=" + led 
					+ ", remote=" + remote 
					+ ", ir=" + irCommand + "]";
		}


		/**
		 * Gets the remote.
		 *
		 * @return the remote
		 */
		public String getRemote() {
			return remote;
		}

		/**
		 * Gets the ir command.
		 *
		 * @return the irCommand
		 */
		public String getIrCommand() {
			return irCommand;
		}


		/**
		 * Gets the host.
		 *
		 * @return the host
		 */
		public String getHost() {
			return host;
		}


		/**
		 * Gets the port.
		 *
		 * @return the port
		 */
		public String getPort() {
			return port;
		}


		/**
		 * Gets the led.
		 *
		 * @return the led
		 */
		public Leds getLed() {
			return led;
		}
		
		 /**
		  * @return the list of accepted DataTypes for the Item linked to this Binding Config Element
		  */
		public List<Class<? extends State>> getAcceptedTypes() {
			return acceptedTypes;
		}


	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getHost(String itemName, Command command) {
		IRtransBindingConfig config = (IRtransBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(command) != null ? config.get(command).getHost() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getPort(String itemName, Command command) {
		IRtransBindingConfig config = (IRtransBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(command) != null ? Integer.parseInt(config.get(command).getPort()) : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Leds getLed(String itemName, Command command) {
		IRtransBindingConfig config = (IRtransBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(command) != null ? config.get(command).getLed() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getRemote(String itemName, Command command) {
		IRtransBindingConfig config = (IRtransBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(command) != null ? config.get(command).getRemote() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getIrCommand(String itemName, Command command) {
		IRtransBindingConfig config = (IRtransBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(command) != null ? config.get(command).getIrCommand() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public InetSocketAddress getInetSocketAddress(String itemName, Command command) {
		InetSocketAddress socketAddress = null;
		try {
			socketAddress = new InetSocketAddress(InetAddress.getByName(getHost(itemName,command)),getPort(itemName,command));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return socketAddress;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<InetSocketAddress> getInetSocketAddresses(String itemName){
		List<InetSocketAddress> theList = new ArrayList<InetSocketAddress>();
		for(Command command : getAllCommands(itemName)) {
				InetSocketAddress anAddress = null;
				try {
					anAddress = new InetSocketAddress(InetAddress.getByName(getHost(itemName,command)),getPort(itemName,command));
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				theList.add(anAddress);
			}
		return theList;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<String> getItemNames(String remote, String irCommand) {
		List<String> itemNames = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			IRtransBindingConfig aConfig = (IRtransBindingConfig) bindingConfigs.get(itemName);
			for(Command aCommand : aConfig.keySet()) {
				IRtransBindingConfigElement anElement = (IRtransBindingConfigElement) aConfig.get(aCommand);
				if(anElement.remote.equals(remote) && anElement.irCommand.equals(irCommand)) {
					itemNames.add(itemName);
				}
			}
		}
		return itemNames;               
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<String> getItemNames(String host, int port) {

		List<String> items = new ArrayList<String>();

		for (String itemName : bindingConfigs.keySet()) {
			IRtransBindingConfig aConfig = (IRtransBindingConfig) bindingConfigs.get(itemName);
			for(Command aCommand : aConfig.keySet()) {
				IRtransBindingConfigElement anElement = (IRtransBindingConfigElement) aConfig.get(aCommand);				
				if(anElement.getHost().equals(host) && anElement.getPort()==Integer.toString(port)) {
					if(!items.contains(itemName)) {
						items.add(itemName);
					}
				}			
			}
		}
		return items; 
		
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Command> getAllCommands(String itemName){
		List<Command> commands = new ArrayList<Command>();
		IRtransBindingConfig aConfig = (IRtransBindingConfig) bindingConfigs.get(itemName);
		for(Command aCommand : aConfig.keySet()) {
			commands.add(aCommand);
		}

		return commands;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Command> getAllCommands(String itemName, String remote, String irCommand){
		List<Command> commands = new ArrayList<Command>();
		IRtransBindingConfig aConfig = (IRtransBindingConfig) bindingConfigs.get(itemName);
		for(Command aCommand : aConfig.keySet()) {
			IRtransBindingConfigElement anElement = (IRtransBindingConfigElement) aConfig.get(aCommand);
			if(anElement.remote.equals(remote) && anElement.irCommand.equals(irCommand)) {
				commands.add(aCommand);
			}
		}               
		return commands;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<Command> getQualifiedCommands(String itemName,Command command){
		List<Command> commands = new ArrayList<Command>();
		IRtransBindingConfig aConfig = (IRtransBindingConfig) bindingConfigs.get(itemName);
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
	 * {@inheritDoc}
	 */
	public List<Class<? extends State>> getAcceptedDataTypes(String itemName, Command command) {
		if(itemName != null) {
			IRtransBindingConfig config = (IRtransBindingConfig) bindingConfigs.get(itemName);
			if(config != null) {
				IRtransBindingConfigElement element = config.get(command);
				if(element != null) {
					return element.getAcceptedTypes();
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPortAsString(String itemName, Command command) {
		IRtransBindingConfig config = (IRtransBindingConfig) bindingConfigs.get(itemName);
		return config != null && config.get(command) != null ? config.get(command).getPort() : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Direction getDirection(String itemName, Command command) {
		// the IRtrans binding will only establish outbound connections to the remote IRtrans device
		return Direction.OUT;
	}
	
}