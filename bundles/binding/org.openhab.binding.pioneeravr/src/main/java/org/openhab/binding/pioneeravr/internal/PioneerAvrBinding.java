/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pioneeravr.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.pioneeravr.PioneerAvrBindingProvider;
import org.openhab.binding.pioneeravr.internal.ipcontrolprotocol.IpControl;
import org.openhab.binding.pioneeravr.internal.ipcontrolprotocol.IpControlCommand;
import org.openhab.binding.pioneeravr.internal.ipcontrolprotocol.IpControlCommandRef;
import org.openhab.binding.pioneeravr.internal.ipcontrolprotocol.IpControlDisplayInformation;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Binding listening to openHAB event bus and send commands to pioneerav devices when certain
 * commands are received.
 * 
 * @author Rainer Ostendorf
 * @author based on the Onkyo binding by Pauli Anttila and others
 * @since 1.4.0
 */
public class PioneerAvrBinding extends AbstractBinding<PioneerAvrBindingProvider> implements ManagedService, BindingChangeListener, PioneerAvrEventListener {

	private static final Logger logger = LoggerFactory.getLogger(PioneerAvrBinding.class);

	protected static final String ADVANCED_COMMAND_KEY = "#";
	protected static final String WILDCARD_COMMAND_KEY = "*";

	/** RegEx to validate a config <code>'^(.*?)\\.(host|port|checkconn)$'</code> */
	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(host|port|checkconn)$");
	
	/** pioneerav receiver default tcp port */
	private final static int DEFAULT_PORT = IpControl.DEFAULT_IPCONTROL_PORT;

	/** Map table to store all available receivers configured by the user */
	protected Map<String, DeviceConfig> deviceConfigCache = null;
	
	public PioneerAvrBinding() {
	}

	public void activate() {
		logger.debug("Activate");
	}

	public void deactivate() {
		logger.debug("Deactivate");
		closeAllConnections();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		logger.debug("bindingChanged {}", itemName);
		initializeItem(itemName);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		if (itemName != null) {
			PioneerAvrBindingProvider provider = 
				findFirstMatchingBindingProvider(itemName, command.toString());

			if (provider == null) {
				logger.warn("Doesn't find matching binding provider [itemName={}, command={}]", itemName, command);
				return;
			}

			logger.debug(
				"Received command (item='{}', state='{}', class='{}')",
				new Object[] { itemName, command.toString(), command.getClass().toString() });

			String tmp = provider.getDeviceCommand(itemName, command.toString());

			if (tmp == null) {
				tmp = provider.getDeviceCommand(itemName, WILDCARD_COMMAND_KEY);	
			}
			
			String[] commandParts = tmp.split(":");
			String deviceId = commandParts[0];
			String deviceCmd = commandParts[1];
			
			DeviceConfig device = deviceConfigCache.get(deviceId);
			PioneerAvrConnection remoteController = device.getConnection();

			if (device != null && remoteController != null) {
		
				if (deviceCmd.startsWith(ADVANCED_COMMAND_KEY)) {
					
					// advanced command
					
					deviceCmd = deviceCmd.replace(ADVANCED_COMMAND_KEY, "");
			
					if (deviceCmd.contains("%")) {

						deviceCmd = convertOpenHabCommandToDeviceCommand( command, deviceCmd);
					}
					
				} else {
					// normal command
					IpControlCommand cmd = IpControlCommand.valueOf(deviceCmd);
					deviceCmd = cmd.getCommand();
					
					if (deviceCmd.contains("%")) {
						deviceCmd = convertOpenHabCommandToDeviceCommand( command, deviceCmd);
					} 
				}
				
				if (deviceCmd != null) {
					remoteController.send(deviceCmd);
				} else {
					logger.warn("Cannot convert value '{}' to IpControl format", command);
				}

			} else {
				logger.warn("Cannot find connection details for device id '{}'", deviceId);
			}
		}
	}
	
	/**
	 * Convert OpenHAB commmand to pioneer avr receiver command.
	 * 
	 * @param command the openhab command to send
	 * @param cmdTemplate the format string to used for converting the command
	 * 
	 * @return the receiver command string
	 */
	private String convertOpenHabCommandToDeviceCommand( Command command, String cmdTemplate ) {
		String deviceCmd = null;
		
		if (command instanceof OnOffType) {
			deviceCmd = String.format(cmdTemplate, command == OnOffType.ON ? 1: 0);	 
		
		} else if (command instanceof StringType) {
			deviceCmd = String.format(cmdTemplate, command);	
			
		} else if (command instanceof PercentType) {
			
			// when settings the volume as percent, we need to map it
			if( cmdTemplate.equals( IpControlCommand.VOLUME_SET.getCommand() ) ) {
				Integer percentValue = convertPercentToVolume(((DecimalType) command).intValue());
				deviceCmd = String.format(cmdTemplate, percentValue );
			}
			else {
				deviceCmd = String.format(cmdTemplate, ((DecimalType) command).intValue());
			}
			
		} else if (command instanceof DecimalType) {
			deviceCmd = String.format(cmdTemplate, ((DecimalType) command).intValue());	 
		}		
		

		return deviceCmd;
	}

	/**
	 * Find the first matching {@link PioneerAvrBindingProvider} according to
	 * <code>itemName</code>.
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	private PioneerAvrBindingProvider findFirstMatchingBindingProvider(String itemName, String command) {
		PioneerAvrBindingProvider firstMatchingProvider = null;

		for (PioneerAvrBindingProvider provider : this.providers) {
			String tmp = provider.getDeviceCommand(itemName, command.toString());
			if (tmp != null) {
				firstMatchingProvider = provider;
				break;
			}
		}

		if (firstMatchingProvider == null) {
			for (PioneerAvrBindingProvider provider : this.providers) {
				String tmp = provider.getDeviceCommand(itemName, WILDCARD_COMMAND_KEY);
				if (tmp != null) {
					firstMatchingProvider = provider;
					break;
				}
			}

		}
		
		return firstMatchingProvider;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {

		logger.debug("Configuration updated, config {}", config != null ? true
				: false);

		if (config != null) {
			Enumeration<String> keys = config.keys();
			
			if ( deviceConfigCache == null ) {
				deviceConfigCache = new HashMap<String, DeviceConfig>();
			}
			
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);

				if (!matcher.matches()) {
					logger.debug("given config key '" + key
						+ "' does not follow the expected pattern '<id>.<host|port|checkconn>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String deviceId = matcher.group(1);

				DeviceConfig deviceConfig = deviceConfigCache.get(deviceId);

				if (deviceConfig == null) {
					deviceConfig = new DeviceConfig(deviceId);
					deviceConfigCache.put(deviceId, deviceConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("host".equals(configKey)) {
					deviceConfig.host = value;
				} else if ("port".equals(configKey)) {
					deviceConfig.port = Integer.valueOf(value);
				} else if ("checkconn".equals(configKey)) {
					if( value.equals("0") )
						deviceConfig.connectionCheckActive = false;
					else
						deviceConfig.connectionCheckActive = true;	
				} else {
					throw new ConfigurationException(configKey, "the given configKey '" + configKey + "' is unknown");
				}
			}
			
			// open connection to all receivers
			for (String device : deviceConfigCache.keySet()) {
				PioneerAvrConnection connection = deviceConfigCache.get(device).getConnection();
				if (connection != null) {
					connection.openConnection();
					connection.addEventListener(this);
				}
			}
			
			for (PioneerAvrBindingProvider provider : this.providers) {
				for (String itemName : provider.getItemNames()) {
					initializeItem(itemName);
				}
			}

		}
	}

	private void closeAllConnections() {
		if (deviceConfigCache != null) {
			for (String device : deviceConfigCache.keySet()) {
				PioneerAvrConnection connection = deviceConfigCache.get(device).getConnection();
				if (connection != null) {
					connection.closeConnection();
					connection.removeEventListener(this);
				}
			}
			deviceConfigCache = null;
		}
	}

	/**
	 * Find receiver from device caache by ip address.
	 * 
	 * @param ip
	 * @return
	 */
	private DeviceConfig findDevice(String ip) {
		for (String device : deviceConfigCache.keySet()) {
			DeviceConfig deviceConfig = deviceConfigCache.get(device);
			if (deviceConfig != null) {
				if (deviceConfig.getHost().equals(ip)) {
					return deviceConfig;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public void statusUpdateReceived(EventObject event, String ip, String data) {
		// find correct device from device cache
		DeviceConfig deviceConfig = findDevice(ip);

		if (deviceConfig != null) {
			logger.debug("Received status update '{}' from device {}", data, deviceConfig.host);

			for (PioneerAvrBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					// Update all items which refer to command
					HashMap<String, String> values = provider.getDeviceCommands(itemName);

					for (String cmd : values.keySet()) {
						
						String[] commandParts = values.get(cmd).split(":");
						String deviceCmd = commandParts[1];
						boolean match = false;
						
						if( deviceCmd.startsWith(ADVANCED_COMMAND_KEY) )
						{
							// we currently have no info about the expected response string
							// of a user configured "advanced" command, so skip this item
							continue;
						}
						
						try {
							String commandResponse = IpControlCommand.valueOf(deviceCmd).getResponse();
							
							// when no respone is expected, the response string is empty
							if( !commandResponse.isEmpty() ) {
								// compare response from network with response in enum								
								if (data.startsWith(commandResponse)) {
									match = true;
								}	
							}
							
						} catch (Exception e) {
							logger.error("Unregonized command '" + deviceCmd + "'", e);
						}
						

						if (match) {
							Class<? extends Item> itemType = provider.getItemType(itemName);
							State v = convertDeviceValueToOpenHabState(itemType, data, IpControlCommand.valueOf(deviceCmd) );
							eventPublisher.postUpdate(itemName, v);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Convert receiver value to OpenHAB state.
	 * 
	 * @param itemType
	 * @param data
	 * 
	 * @return
	 */
	private State convertDeviceValueToOpenHabState(Class<? extends Item> itemType, String data, IpControlCommand cmdType) {
		State state = UnDefType.UNDEF;

		try {
			int index;
			
			// cut off the leading response identifier to get the payload string 			
			String payloadSubstring = data.substring( cmdType.getResponse().length() );
			
			// if the response consisted just of the response-identifier and had
			// no further value attached handle it as boolean of value 1
			// this is used e.g. when switch items are used for selecting the source.
			// the selected source will then be set to "ON"
			if( payloadSubstring.length() == 0)
				payloadSubstring = "1";
			
			// special case for display info query: convert to human readable string
			if( cmdType.getCommandRef() == IpControlCommandRef.DISPLAY_INFO_QUERY )
			{
				IpControlDisplayInformation displayInfo = new IpControlDisplayInformation( payloadSubstring );
				payloadSubstring = displayInfo.getInfoText();				
				logger.debug("DisplayInfo: converted value '{}' to string '{}'", data, payloadSubstring );
			}
					
			if (itemType == SwitchItem.class) {
				index = Integer.parseInt(payloadSubstring);
				state = (index == 0) ? OnOffType.ON : OnOffType.OFF; // according to Spec: 0=ON, 1=OFF!
			}	
			 
			else if (itemType == DimmerItem.class) {
				
				if( cmdType.getCommandRef().getCommand() == IpControlCommandRef.VOLUME_QUERY.getCommand() || 
					cmdType.getCommandRef().getCommand() == IpControlCommandRef.VOLUME_SET.getCommand())	{
					index = convertVolumeToPercent( Integer.parseInt(payloadSubstring) );
				}
				else {
					index = Integer.parseInt(payloadSubstring);
				}
				state = new PercentType(index);
				
			} else if (itemType == NumberItem.class) {
					index = Integer.parseInt(payloadSubstring);
					state = new DecimalType(index);
				
			} else if (itemType == RollershutterItem.class) {
				index = Integer.parseInt(payloadSubstring);
				state = new PercentType(index);
				
			} else if (itemType == StringItem.class) {
				state = new StringType(payloadSubstring);
			}
		} catch (Exception e) {
			logger.debug("Cannot convert value '{}' to data type {}", data, itemType);
		}
		
		return state;
	}
	
	/**
	 * map the receiver volume values to percent values
	 * 
	 * receiver volume 0 is -80db and 0%
	 * receiver volume 185 is +12dB and 100% (at least for zone 1)
	 * 
	 * @param volume the receiver volume value
	 * 
	 */
	private Integer convertVolumeToPercent( Integer volume )	{
		Integer percent = Math.round( (volume*100)/185 );
		logger.debug("converted volume '" +  volume.toString() + "' to '" + percent.toString() + "%'" );
		return percent;
	}
	
	/**
	 * map percent values to receiver volumes
	 * 
	 * receiver volume 0 is -80db and 0%
	 * receiver volume 185 is +12dB and 100% (at least for zone 1)
	 * 
	 * @param volume the receiver volume value
	 * 
	 */
	private Integer convertPercentToVolume( Integer percent )	{
		Integer volume = Math.round( (percent*185)/100 );
		logger.debug("converted " +  percent.toString() + "% to volume " + volume.toString() );
		return volume;
	}
	
	
	/**
	 * Initialize item value. Method send query to receiver if init query is configured to binding item configuration
	 * 
	 * @param itemType
	 * 
	 */
	private void initializeItem(String itemName) {
		for (PioneerAvrBindingProvider provider : providers) {
			String initCmd = provider.getItemInitCommand(itemName);
			if (initCmd != null) {
				logger.debug("Initialize item {}", itemName);

				String[] commandParts = initCmd.split(":");
				String deviceId = commandParts[0];
				String deviceCmd = commandParts[1];

				DeviceConfig device = deviceConfigCache.get(deviceId);
				PioneerAvrConnection remoteController = device.getConnection();

				if (device != null && remoteController != null) {
					if (deviceCmd.startsWith(ADVANCED_COMMAND_KEY)) {
						deviceCmd = deviceCmd.replace(ADVANCED_COMMAND_KEY, "");
					} else {
						IpControlCommand cmd = IpControlCommand.valueOf(deviceCmd);
						deviceCmd = cmd.getCommand();
					}

					remoteController.send(deviceCmd);
				} else {
					logger.warn(
							"Cannot find connection details for device id '{}'",
							deviceId);
				}
			}
		}
	}
	

	/**
	 * Internal data structure which carries the connection details of one
	 * device (there could be several)
	 */
	static class DeviceConfig {

		String host;
		int port = DEFAULT_PORT;

		PioneerAvrConnection connection = null;
		String deviceId;
		Boolean connectionCheckActive;

		public DeviceConfig(String deviceId) {
			this.deviceId = deviceId;
			connectionCheckActive = true; // by default, the conn check is active
		}

		public String getHost(){
			return host;
		}
		
		public int getPort(){
			return port;
		}
		
		
		
		@Override
		public String toString() {
			return "Device [id=" + deviceId + ", host=" + host + ", port=" + port + "]";
		}

		PioneerAvrConnection getConnection() {
			if (connection == null) {
				connection = new PioneerAvrConnection(host, port, connectionCheckActive);
			}
			return connection;
		}

	}

}
