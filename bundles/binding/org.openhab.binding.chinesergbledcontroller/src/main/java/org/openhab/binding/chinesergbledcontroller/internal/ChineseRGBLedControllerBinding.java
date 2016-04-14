/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.chinesergbledcontroller.internal;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.chinesergbledcontroller.ChineseRGBLedControllerBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Konstantin Polihronov
 * @since 1.8.0
 */
public class ChineseRGBLedControllerBinding extends AbstractActiveBinding<ChineseRGBLedControllerBindingProvider> {

	private static final int socketPort = 5577;

	private static final Logger logger = 
			LoggerFactory.getLogger(ChineseRGBLedControllerBinding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
	 * method and must not be accessed anymore once the deactivate() method was called or before activate()
	 * was called.
	 */
	private BundleContext bundleContext;


	/** 
	 * the refresh interval which is used to poll values from the ChineseRGBLedController
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 360000;

	private static final Map<String,byte[]> ledControllerCommands;
		static
	    {
			ledControllerCommands = new HashMap<String, byte[]>();
			ledControllerCommands.put("PAUSE", new byte[]{(byte)0xcc,0x20,0x33});
			ledControllerCommands.put("RUN", new byte[]{(byte)0xcc,0x21,0x33});
			ledControllerCommands.put("ON", new byte[] {(byte)0xcc,0x23,0x33});
			ledControllerCommands.put("OFF", new byte[]{(byte)0xcc,0x24,0x33});

	    }
	
	/**
	 * 
	 * @throws IOException
	 */

	public ChineseRGBLedControllerBinding() throws IOException {
		//		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		//		sendTCPPacket("192.168.254.60","\\cc#3");
	}


	/**
	 * The function sends payload as byte value to the IPaddress on socket port
	 * @throws SocketException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private void sendTCPPacket(InetAddress ipAddress, byte [] payload, int socketPort ) throws SocketException, UnknownHostException, IOException {			
		SocketAddress address = new InetSocketAddress(ipAddress, socketPort);
		Socket clientSocket = new Socket();
		clientSocket.connect(address,20000);
		OutputStream streamOut = clientSocket.getOutputStream(); 
		DataOutputStream dataOutputStream = new DataOutputStream(streamOut);
		logger.debug("Sending payload {} to {} address on {} port !",payload, ipAddress, socketPort);
		dataOutputStream.write(payload,0,payload.length);
		clientSocket.close();
	}


	private boolean setRGBvalues(RGB RGB, InetAddress ipAddress) {
		byte red=(byte) RGB.getRed();
		byte green=(byte) RGB.getGreen();
		byte blue=(byte) RGB.getBlue();
		byte [] payload=new byte [] {
				0x56,
				red,green,blue,
				(byte) 0xAA
		};
		try {
			sendTCPPacket(ipAddress,payload,socketPort);
		} catch (IOException e) {
			logger.error("Unable to send TCP packet to {} on {}. Exception={}",ipAddress,socketPort,e.getMessage());
			return false;
		}
		logger.debug("Successfully set R={},G={},B={}",red&0xff,green&0xff,blue&0xff);
		return true;
	}

	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext BundleContext of the Bundle that defines this component
	 * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;

		// the configuration is guaranteed not to be null, because the component definition has the
		// configuration-policy set to require. If set to 'optional' then the configuration may be null


		// to override the default refresh interval one has to add a 
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		// read further config parameters here ...

		setProperlyConfigured(true);
	}

	/**
	 * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
	 * @param configuration Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		// update the internal configuration accordingly
	}

	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or
	 * mandatory references are no longer satisfied or the component has simply been stopped.
	 * @param reason Reason code for the deactivation:<br>
	 * <ul>
	 * <li> 0 – Unspecified
	 * <li> 1 – The component was disabled
	 * <li> 2 – A reference became unsatisfied
	 * <li> 3 – A configuration was changed
	 * <li> 4 – A configuration was deleted
	 * <li> 5 – The component was disposed
	 * <li> 6 – The bundle was stopped
	 * </ul>
	 */
	public void deactivate(final int reason) {
		this.bundleContext = null;
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
	}


	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "ChineseRGBLedController Refresh Service";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");

	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.	
		InetAddress ipAddress=null;
		ipAddress = getIPAddress(itemName);
		Item item = getItem(itemName);
		logger.debug("internalReceiveCommand(Item:{},Command:{}) is called!Item:{}", itemName, command,item);
		logger.debug("Instance of SwitchItem:{},Instance of ColorItem:{}",item instanceof SwitchItem,item instanceof ColorItem);
		if (item instanceof ColorItem) {
			colorItemHandler(command, ipAddress);			
		} else if (item instanceof SwitchItem){
			switchItemHandler(command, ipAddress);
		}
		else { 
			logger.error("Command not applicable for this item type");
		}
	}



	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}



	private void colorItemHandler(Command command, InetAddress ipAddress) {
		logger.debug("colorItemHandler(Command:{},InetAddress:{}) is called!", command,ipAddress);
		if (command.toString().equals("OFF")||command.toString().equals("ON")){
			switchItemHandler(command,ipAddress);
			return;
		}				
		String[] arrayHSB = command.toString().trim().split(",");
		float hue=Float.parseFloat(arrayHSB[0]);
		float saturation=Float.parseFloat(arrayHSB[1]);
		float brightness=Float.parseFloat(arrayHSB[2]);
		logger.debug("hue:{},sat:{},bri:{})",hue,saturation,brightness);
		RGB temp=new RGB();
		temp.HSLtoRGB(hue, saturation, brightness);
		logger.debug("RGB values after HSLtoRGB:"+temp);
		setRGBvalues(temp,ipAddress);
	}


	private void switchItemHandler(Command command, InetAddress ipAddress) {
		String commandStr = command.toString();
		try {
			byte[] payload = ledControllerCommands.get(commandStr);
			logger.debug("switchItemHandler(Command={},IP={}),Payload:{}",commandStr,ipAddress,payload);
			try {
				sendTCPPacket(ipAddress, payload, socketPort);
			} catch (IOException e) {
				logger.error("Cannot send TCP packet\nException:" + e.getMessage());
			}
		} catch (NullPointerException e) {
			logger.error("Command not found: {}.Exception:{}" + command, e.getMessage());
		}

	}


	private Item getItem(String itemName) {
		Item item=null;
		for(ChineseRGBLedControllerBindingProvider provider:providers) {
			if(provider.providesBindingFor(itemName)) {
				item = provider.getItem(itemName);
			}
		}
		if(item==null) { throw new NullPointerException("Item is pointing to null."); }
		return item;
	}

	private InetAddress getIPAddress(String itemName) {
		InetAddress ipAddress = null;
		for(ChineseRGBLedControllerBindingProvider provider:providers) {
			if(provider.providesBindingFor(itemName)) {
				ipAddress = provider.getIPAddress(itemName);
			}
		}
		if(ipAddress==null) { throw new NullPointerException("ipAddress is pointing to null."); }
		return ipAddress;
	}
}









/*
if(command.toString().equals("ON")){
try {
	byte []payload = new byte[] {(byte) 0xcc,0x23,0x33};
	sendTCPPacket(ipAddress, payload, socketPort);
} catch (IOException e) {
	logger.error("Cannot send TCP packet\nException:"+e.getMessage());
}
} else if(command.toString().equals("OFF")) {
try {
	byte []payload = new byte[] {(byte) 0xcc,0x24,0x33}; 
	sendTCPPacket(ipAddress, payload, socketPort);
} catch (IOException e) {
	logger.error("Cannot send TCP packet\nException:"+e.getMessage());
}
} else {
logger.error("Command not applicable for switch items: {}"+command);
} /**/