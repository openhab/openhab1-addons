/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.mochadx10.MochadX10BindingProvider;
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
	
	private String hostIp = "127.0.0.1";	// default mochad host ip address
	private int hostPort  = 1099;			// default mochad host port
	private Map<String, Command> lastIssuedCommand = new HashMap<String, Command>(); // required to determine the X10 command for STOP
	
    private static final String IPADDRESS_PATTERN = 
		"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	
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
		}
	}
	
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

	private boolean isValidIpAddress(String hostIp) {
		if ( hostIp.matches(IPADDRESS_PATTERN) ) {
			return true;
		}
		
		return false;
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
		int nrLinesToRead = 2;

		if ( command instanceof OnOffType ) {
			commandStr = OnOffType.ON.equals( command ) ? "on" : "off";
		}
		else if ( command instanceof UpDownType ) {
			commandStr = UpDownType.UP.equals( command ) ? "bright" : "dim";
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
				if ( ((PercentType) command).intValue() == 0 ) {
					// If percent value equals 0 the x10 "off" command is used instead of xdim
					commandStr = "off";
				}
				else {
					// 100% maps to value 63 so we need to do scaling
					long dim_value = Math.round(((PercentType) command).doubleValue() * 63.0/100);
					commandStr = "xdim " + dim_value;
				}
			}
			else if ( deviceConfig.getItemType() == RollershutterItem.class ) {
				Double invert_level = 100 - ((PercentType) command).doubleValue();
				long level = Math.round(invert_level * 25.0/100);
				commandStr = "extended_code_1 0 1 " + level;
			}
			nrLinesToRead = 1;
		}
		else if ( command instanceof IncreaseDecreaseType ) {
			// Increase decrease not yet supported
			commandStr = "none";
		}

		try {
			if ( !commandStr.equals( "none" ) ) {
				Socket client = new Socket( hostIp, hostPort );
				client.setSoTimeout(3000);
				
				BufferedReader br = new BufferedReader( new InputStreamReader( client.getInputStream() ) );				
				DataOutputStream out = new DataOutputStream( client.getOutputStream() );

				out.writeBytes(tm + " " + address + " " + commandStr + "\n");
				logger.debug(tm + " " + address + " " + commandStr);
				out.flush();

				for (int i = 0; i < nrLinesToRead; i++) {
					logger.debug( br.readLine() );
				}
				
				out.close();
				client.close();
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		lastIssuedCommand.put(address, command);
	}
}
