/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mochadx10.internal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.mochadx10.MochadX10BindingProvider;
import org.openhab.binding.mochadx10.commands.MochadX10Address;
import org.openhab.binding.mochadx10.commands.MochadX10Command;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This binding is able to do the following tasks with the mochad X10 System:
 * 
 * <ul>
 * <li>X10 Appliance modules: switch on, switch off.</li>
 * <li>X10 Dimmer modules: switch on, switch off, dim</li>
 * <li>X10 Shutter modules: open, close, stop, move</li>
 * </ul>
 * 
 * @author Jack Sleuters
 * @since 1.7.0
 */
public class MochadX10Binding extends AbstractBinding<MochadX10BindingProvider> implements ManagedService {
	static final Logger logger = LoggerFactory.getLogger(MochadX10Binding.class);
	
	/**
	 * The ip address of the Mochad X10 host (default 127.0.0.1)
	 */
	private String hostIp = "127.0.0.1";
	
	/**
	 * The port number of the Mochad X10 host (default 1099)
	 */
	private int hostPort  = 1099;
	
	/**
	 * To implement the stop command for x10 modules like Rollershutters, it is required
	 * to know whether the last issued command was a 'dim' or a 'bright' command. If it 
	 * was a 'dim' command, the 'bright' command has to be issued to realize stop functionality.
	 * If it was a 'bright' command, the 'dim' command has to be issued.
	 * 
	 * This map keeps track of the last issued command per X10 address
	 */
	private Map<String, Command> lastIssuedCommand = new HashMap<String, Command>(); // required to determine the X10 command for STOP

	/**
	 * Not every X10 module keeps its state. Furthermore, some X10 commands like 'dim' or 'bright' change the
	 * brightness level in a relative way. Therefore, it is required to keep the absolute level of such module.
	 * According to discussions on the openhab forums, it is bad practice to use the item registry to retrieve
	 * the current level of a module. Therefore, it is stored internally in this binding.
	 * 
	 * 'currentLevel' maps an X10 address string on a level value
	 * 
	 * One could initialize this map with all possible X10 addresses and a default level value. However,
	 * in practice a lot less than 256 X10 modules will be connected. To keep memory usage as low as possible,
	 * the map is not initialized. When the current level of a module with an address that is not yet in the map
	 * is requested, a value of '-1' will be returned. 
	 */
	private Map<String, Integer> currentLevel = new HashMap<String, Integer>();
	
	/**
	 * The socket used to communicate with the Mochad X10 host
	 */
	private Socket client;

	/**
	 * Used to receive messages from the Mochad X10 host
	 */
	private BufferedReader in;

	/**
	 * Used to send commands to the Mochad X10 host
	 */
	private DataOutputStream out;

	/**
	 * Receiving of messages from the Mochad X10 host is asynchronous. To make sure we
	 * capture incoming message, a separate receive thread is used.
	 */
	private ReceiveThread receiveThread;

	/**
	 * Keeps track of the latest used X10 address. This is required because the following messages
	 * can be received from the Mochad X10 server. The first message specifies the full address 'D1'
	 * the second message only specifies the 'houseCode' part of the address. This means that 
	 * the unitCode of the previous X10 address should be used. 
	 * 
	 * 03/11 22:08:01 Rx PL HouseUnit: D1
	 * 03/11 22:08:40 Rx PL House: D Func: Bright(1)
	 * 
	 */
	private MochadX10Address previousX10Address;
	
	/**
	 * The regular expression to check whether the ip address of the host is correct
	 */
    private static final String IPADDRESS_PATTERN = 
		"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    
    /**
     * This thread asynchronously receives messages from the Mochad X10 Host. It 
     * parses the message and posts the appropriate event on the openHAB event bus.
     * 
     * @author Jack Sleuters
     * @since  1.7.0
     *
     */
	private class ReceiveThread extends Thread {
		private MochadX10Binding binding;
		
		/**
		 * The command parser parses messages received from the host and transforms them
		 * into MochadX10Commands.
		 */
		private MochadX10CommandParser commandParser = new MochadX10CommandParser(eventPublisher);
		
		/**
		 * Constructor
		 * 
		 * @param binding	This binding
		 */
		public ReceiveThread(MochadX10Binding binding) {
			this.binding = binding; 
		}

		/**
		 * Process a message received by the Mochad X10 host
		 * 
		 * @param msg  string containing the received message
		 */
		private void processIncomingMessage(String msg) {			
			logger.debug("Received message: " + msg);
    		MochadX10Command command = commandParser.parse(msg);
    		
    		if (command != null) {
        		logger.debug("Command: " + command.toString());
        		
    			String itemName = getItemNameForAddress(command.getAddress());
    			
    			if (itemName != null) {
        			command.postCommand(itemName, getCurrentLevel(command.getAddress().toString()));

    				logger.debug("Address " + command.getAddress() + " level set to " + command.getLevel());
    				
        			binding.previousX10Address = command.getAddress();
    				logger.debug("ReceiveThread: previous X10 address set to " + previousX10Address.toString());
    			}
    		}
        }

		/**
		 * Run method. Runs the actual receiving process.
		 */
		@Override
		public void run() {
			String msg = null;

			logger.debug("Starting Mochad X10 Receive thread");
			
			while (!interrupted()) {
				try {
					// Blocking read, reading messages coming from Mochad X10 host
					msg = in.readLine();
					if (msg != null) {
						processIncomingMessage(msg);
					}
					else {
						logger.error("Received a \"null\" message");
						reconnectToMochadX10Server();
					}
				}
				catch (IOException e) {
					logger.trace("Caught IOException " + e.getMessage());
					reconnectToMochadX10Server();
				}
			}
			logger.debug("Stopped Mochad X10 Receive thread");
		}
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
		if ( properties != null ) {
			String ip = (String) properties.get( "hostIp" );
			if ( StringUtils.isNotBlank( ip ) ) {
				if ( isValidIpAddress( ip ) ) {
					this.hostIp = ip;
				}
				else {
					throw new ConfigurationException(hostIp, "The specified hostIp address \"" + ip + "\" is not a valid ip address");
				}
			}
			
			String port = (String) properties.get("hostPort");
			if ( StringUtils.isNotBlank( port ) ) {
				if ( isValidPort( port ) ) {
					this.hostPort = Integer.parseInt(port);
				}
				else {
					throw new ConfigurationException(port, "The specified port \"" + port + "\" is not a valid port number.");
				}
			}
			
			initializeBinding();
		}
	}
	
	@Override
	public void deactivate() {
		// Close the connection with the Mochad X10 Server
		disconnectFromMochadX10Server();
		
		super.deactivate();
	}
	
	/**
	 * Initialize the binding. Connection to the Mochad X10 host is established and after that
	 * the receive thread is started.
	 */
	private void initializeBinding() {
		previousX10Address = new MochadX10Address("a1");
		
		connectToMochadX10Server();
		
		receiveThread = new ReceiveThread(this);
		receiveThread.start();
	}
	
	/**
	 * Connect to the Mochad X10 host
	 */
	private void connectToMochadX10Server() {
		try {
			client = new Socket( hostIp, hostPort );

			in = new BufferedReader( new InputStreamReader( client.getInputStream() ) );				
			out = new DataOutputStream( client.getOutputStream() );
			
			logger.debug("Connected to Mochad X10 server");
		} catch (UnknownHostException e) {
			logger.error("Unknown host: " + hostIp + ":" + hostPort );
		} catch (IOException e) {
			logger.error("IOException: " + e.getMessage() + " while trying to connect to Mochad X10 host: " + hostIp + ":" + hostPort );
		}	
	}
	
	/**
	 * Disconnect from the Mochad X10 host. This method is required to clean up the connection
	 * after the binding was disconnected from the host.
	 */
	private void disconnectFromMochadX10Server() {
		try {
			in.close();
			out.close();
			client.close();

			logger.debug("Disconnected from Mochad X10 server");
		} catch (IOException e) {
			logger.error("IOException: " + e.getMessage() + " while trying to disconnect from Mochad X10 host: " + hostIp + ":" + hostPort );
		}
	}

	/**
	 * Reconnect to the Mochad X10 host after the connection to it was lost.
	 */
	private void reconnectToMochadX10Server() {
		disconnectFromMochadX10Server();
		connectToMochadX10Server();
		logger.debug("Reconnected to Mochad X10 server");
	}
	
	/**
	 * Check if the specified port number is a valid.
	 * 
	 * @param port	The port number to check
	 * @return		true when valid, false otherwise
	 */
	private boolean isValidPort(String port) {
		try {
			int portNumber = Integer.parseInt(port);
			
			if ( ( portNumber >= 1024 ) && ( portNumber <= 65535) ) {
				return true;
			}
		}
		catch (NumberFormatException e) {
			return false;
		}
		
		return false;
	}

	/**
	 * Check if the specified ip address is a valid.
	 * 
	 * @param hostIp	The ip address to check to check
	 * @return			true when valid, false otherwise
	 */
	private boolean isValidIpAddress(String hostIp) {
		if ( hostIp.matches(IPADDRESS_PATTERN) ) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Given an X10 address (<houseCode><unitCode>) find the name of the 
	 * corresponding bounded item
	 * 
	 * @param address	The X10 address
	 * @return			The name of the corresponding item, null if no corresponding
	 * 					item could be found.
	 */
	private String getItemNameForAddress(MochadX10Address address) {
		for (MochadX10BindingProvider provider : this.providers) {
			Collection<String> itemNames = provider.getItemNames();
			for (String itemName: itemNames) {
				MochadX10BindingConfig bindingConfig = provider.getItemConfig(itemName);
				if (bindingConfig.getAddress().equals(address.toString())) {
					return bindingConfig.getItemName();
				}
			}
		}
		logger.warn("No item name found for address '" + address.toString() + "'");
		
		return null;
	}
	
	
	
	/**
	 * Lookup of the configuration of the named item.
	 * 
	 * @param itemName
	 * @return The configuration, null otherwise.
	 */
	private MochadX10BindingConfig getConfigForItemName(String itemName) {
		for (MochadX10BindingProvider provider : this.providers) {
			if (provider.getItemConfig(itemName) != null) {
				return provider.getItemConfig(itemName);
			}
		}
		logger.warn("No configuration found for item '" + itemName + "'");
		
		return null;
	}
	
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		MochadX10BindingConfig deviceConfig = getConfigForItemName(itemName);

		if (deviceConfig == null) {
			return;
		}
		String address    = deviceConfig.getAddress();
		String tm         = deviceConfig.getTransmitMethod();
		String commandStr = "none";
		Command previousCommand = lastIssuedCommand.get( address );
		int level = -1;
		
		if ( command instanceof OnOffType ) {
			commandStr = OnOffType.ON.equals( command ) ? "on" : "off";
			level      = OnOffType.ON.equals( command ) ? 100 : 0;
		}
		else if ( command instanceof UpDownType ) {
			commandStr = UpDownType.UP.equals( command ) ? "bright" : "dim";
			level      = UpDownType.UP.equals( command ) ? 100 : 0;
		}
		else if ( command instanceof StopMoveType ) {
			if ( StopMoveType.STOP.equals( command ) ) {
				commandStr = UpDownType.UP.equals( previousCommand ) ? "dim" : "bright";
			}
			else {
				// Move not supported yet
				commandStr = "none";
			}
		}
		else if ( command instanceof PercentType ) {
			if ( deviceConfig.getItemType() == DimmerItem.class ) {
				level = ((PercentType) command).intValue();
				if ( ((PercentType) command).intValue() == 0 ) {
					// If percent value equals 0 the x10 "off" command is used instead of the dim command
					commandStr = "off";
				}
				else {
					long dim_value = 0;
					if (deviceConfig.getDimMethod().equals("xdim")) {
						// 100% maps to value (XDIM_LEVELS - 1) so we need to do scaling
						dim_value = Math.round(((PercentType) command).doubleValue() * (MochadX10Command.XDIM_LEVELS - 1)/100);
						commandStr = "xdim " + dim_value;
					}
					else {
						// 100% maps to value (DIM_LEVELS - 1) so we need to do scaling
						Integer currentValue = currentLevel.get(address);
						if (currentValue == null) {
							currentValue = 0;
						}

	    				logger.debug("Address " + address + " current level " + currentValue);

						int newValue = ((PercentType)command).intValue();
						int relativeValue;
						
						if (newValue > currentValue) {
							relativeValue = (int) Math.round((newValue - currentValue) * ((MochadX10Command.DIM_LEVELS - 1)*1.0/100));
							commandStr = "bright " + relativeValue;
						}
						else if (currentValue > newValue) {
							relativeValue = (int) Math.round((currentValue - newValue) * ((MochadX10Command.DIM_LEVELS - 1)*1.0/100));
							commandStr = "dim " + relativeValue;
						}
						else {
							// If there is no change in state, do nothing
							commandStr = "none";
						}
					}
				}
			}
			else if ( deviceConfig.getItemType() == RollershutterItem.class ) {
				level = ((PercentType) command).intValue();
				Double invert_level = 100 - ((PercentType) command).doubleValue();
				long newlevel = Math.round(invert_level * 25.0/100);
				commandStr = "extended_code_1 0 1 " + newlevel;
			}
		}
		else if ( command instanceof IncreaseDecreaseType ) {
			// Increase decrease not yet supported
			commandStr = "none";
		}

		try {
			if ( !commandStr.equals( "none" ) ) {
				out.writeBytes(tm + " " + address + " " + commandStr + "\n");
				logger.debug(tm + " " + address + " " + commandStr);
				out.flush();
				
				previousX10Address.setAddress(address);
				logger.debug("Previous X10 address set to " + previousX10Address.toString());

				if (level != -1) {
					currentLevel.put(address, level);
    				logger.debug("Address " + address + " level set to " + level);

				}
			}
		} catch (IOException e) {
			reconnectToMochadX10Server();
			logger.error("IOException: " + e.getMessage() + " while trying to send a command to Mochad X10 host: " + hostIp + ":" + hostPort );

		}
		lastIssuedCommand.put(address, command);
	}
	
	/**
	 * Retrieve the current level [0..100] of the X10 module identified by 'address'
	 * 
	 * @param address  the X10 address
	 * @return		   if the level was stored at least once, the current level otherwise -1	
	 */
	private int getCurrentLevel(String address) {
		if (currentLevel.get(address) == null) {
			return -1;
		}
		return currentLevel.get(address);
	}

	/**
	 * Retrieve the output stream of the established socket connection
	 * 
	 * @return the output stream
	 */
	public DataOutputStream getOut() {
		return out;
	}

}
