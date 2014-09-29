/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wol.internal;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Hex;
import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.model.item.binding.BindingConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Binding to send the magic Wake-on-LAN packet to the given MAC-address
 * <p>
 * Valid configurations looks like:
 * <ul>
 * <li>{ wol="192.168.1.0#00:1f:d0:93:f8:b7" }</li>
 * <li>{ wol="192.168.1.0#00-1f-d0-93-f8-b7" }</li>
 * </ul>
 * 
 * @author Thomas.Eichstaedt-Engelen
 */
public class WolBinding extends AbstractEventSubscriber implements BindingConfigReader {

	private static final Logger logger = 
		LoggerFactory.getLogger(WolBinding.class);

    public static final int PORT = 9;
    
	/** 
	 * stores information about the which items are associated to which port. 
	 * The map has this content structure: itemname -> WolBindingConfig
	 */ 
	private Map<String, WolBindingConfig> itemMap = new HashMap<String, WolBindingConfig>();
	
	/**
	 * stores information about the context of items. The map has this 
	 * content structure: context -> Set of itemNames
	 */ 
	private Map<String, Set<String>> contextMap = new HashMap<String, Set<String>>();

	
	public String getBindingType() {
		return "wol";
	}
	
	
	/**
	 * Sends the WoL packet when there is a {@link WolBindingConfig} for 
	 * <code>itemName</code> and <code>command</code> is of type 
	 * {@link OnOffType}.ON 
	 */
	@Override
	public void receiveCommand(String itemName, Command command) {		
		if (itemMap.keySet().contains(itemName)) {
			if (OnOffType.ON.equals(command)) {
				sendWolPacket(itemMap.get(itemName));
			}
		}
	}
	
	private void sendWolPacket(WolBindingConfig config) {
		
		if (config == null) {
			logger.error("given parameter 'config' must not be null");
			return;
		}
		
    	InetAddress address = config.address;
        byte[] macBytes = config.macBytes;

        try {
        	
            byte[] bytes = fillMagicBytes(macBytes);
            
            DatagramPacket packet = 
            	new DatagramPacket(bytes, bytes.length, address, PORT);
            
            DatagramSocket socket = new DatagramSocket();
            socket.send(packet);
            socket.close();
            
            logger.info("Wake-on-LAN packet sent [broadcastIp={}, macaddress={}]", address.getHostName(), String.valueOf(Hex.encodeHex(macBytes)));
        }
        catch (Exception e) {
            logger.error("Failed to send Wake-on-LAN packet [broadcastIp=" + address.getHostAddress() + ", macaddress=" + String.valueOf(Hex.encodeHex(macBytes)) + "]", e);
        }
        
    }
	
	private static byte[] fillMagicBytes(byte[] macBytes) {
		
        byte[] bytes = new byte[6 + 16 * macBytes.length];
        
        for (int i = 0; i < 6; i++) {
            bytes[i] = (byte) 0xff;
        }
        for (int i = 6; i < bytes.length; i += macBytes.length) {
            System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
        }
		
        return bytes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only SwitchItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * @{inheritDoc}
	 */
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		String target = bindingConfig;
		
		String[] configParts = target.split("#");
		if (configParts.length != 2) {
			throw new BindingConfigParseException("WoL configuration must contain two parts (ip and macaddress separated by a '#'");
		}
		
		WolBindingConfig wolBindingConfig = new WolBindingConfig();
		wolBindingConfig.address = getInetAdress(configParts[0]);
		wolBindingConfig.macBytes = getMacBytes(configParts[1]);
		
		itemMap.put(item.getName(), wolBindingConfig);
		
		Set<String> itemNames = contextMap.get(context);
		if(itemNames==null) {
			itemNames = new HashSet<String>();
			contextMap.put(context, itemNames);
		}
	}
	
	private static InetAddress getInetAdress(String ipStr) throws BindingConfigParseException {
		 try {
			return InetAddress.getByName(ipStr);
		} catch (UnknownHostException e) {
			throw new BindingConfigParseException("couldn't parse ipaddress [" + ipStr + "]");
		}		
	}

    private static byte[] getMacBytes(String macStr) throws BindingConfigParseException {
    	
        byte[] bytes = new byte[6];
        String[] hex = macStr.split("(\\:|\\-)");
        
        if (hex.length != 6) {
            throw new BindingConfigParseException("Invalid MAC address [macStr=" + macStr + "]");
        }
        
    	int hexIndex = 0;
    	
        try {
            for (hexIndex = 0; hexIndex < 6; hexIndex++) {
                bytes[hexIndex] = (byte) Integer.parseInt(hex[hexIndex], 16);
            }
        }
        catch (NumberFormatException e) {
            throw new BindingConfigParseException("Invalid hex digit in MAC address [digit=" + hex[hexIndex]);
        }
        
        return bytes;
    }

	public void removeConfigurations(String context) {
		Set<String> itemNames = contextMap.get(context);
		if(itemNames!=null) {
			for(String itemName : itemNames) {
				itemMap.remove(itemName);
			}
			contextMap.remove(context);
		}
	}
	

	/**
	 * Container which carries the necessary binding configuration
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 */
	static class WolBindingConfig {
        InetAddress address;
		byte[] macBytes;
	}
	
	
}
