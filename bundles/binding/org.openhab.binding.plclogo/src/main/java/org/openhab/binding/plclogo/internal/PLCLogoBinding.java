/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Moka7.*;

import org.openhab.binding.plclogo.PLCLogoBindingProvider;
import org.openhab.binding.plclogo.PLCLogoBindingConfig;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NumberType;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author g8kmh
 * @since 1.5.0
 */
public class PLCLogoBinding extends AbstractActiveBinding<PLCLogoBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(PLCLogoBinding.class);
	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(.*?)$");


	/** 
	 * the refresh interval which is used to poll values from the PlcLogo
	 * server (optional, defaults to 500ms)
	 */
	private long refreshInterval = 5000;
	
	private static Map<String,PLCLogoConfig> controllers = new HashMap<String, PLCLogoConfig>();

	public PLCLogoBinding(){
	}
		
	
	public void activate() {
	}
	
	public void deactivate() {
		for (PLCLogoBindingProvider provider : providers) {
			provider.removeBindingChangeListener(this);
		}

		providers.clear();
		Iterator<Entry<String, PLCLogoConfig>> entries = controllers.entrySet().iterator();
		while (entries.hasNext()){
			Entry<String, PLCLogoConfig> thisEntry =  entries.next();
			PLCLogoConfig logoConfig = (PLCLogoConfig) thisEntry.getValue();
			S7Client LogoS7Client = logoConfig.getS7Client();
			if (LogoS7Client != null){
				LogoS7Client.Disconnect();
			
		    	}
		}
		controllers.clear();
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
		return "PLCLogo Polling Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		int resultant;
		byte Buffer[] = new byte[1024];
		// the frequently executed code (polling) goes here ...
		// logger.debug("execute() method is called!");
		if (!bindingsExist()) {
			logger.debug("There is no existing plclogo binding configuration => refresh cycle aborted!");
			return;
		}		
		Iterator<Entry<String, PLCLogoConfig>> entries = controllers.entrySet().iterator();
		while (entries.hasNext()){
			Entry<String, PLCLogoConfig> thisEntry =  entries.next();
			String controllerName = (String) thisEntry.getKey();
			PLCLogoConfig logoConfig = (PLCLogoConfig) thisEntry.getValue();
			S7Client LogoS7Client = logoConfig.getS7Client();
			if (LogoS7Client == null)
				logger.debug("No S7client for "+ controllerName);
			else {
		    	int Result = LogoS7Client.ReadArea(S7.S7AreaDB, 1, 0, 1024, Buffer);
		    	if (Result != 0){
		    			logger.warn("Failed to read memory - may attempt reconnect");
		    				// try and reconnect
		    				LogoS7Client.Disconnect();
		    				LogoS7Client.Connect();
		    				if (LogoS7Client.Connected)
		    					logger.warn("Reconnect successful");
 			
		    			return;
		    	}
		    	// logger.debug("Got it ");
			// Now have the LOGO! memory (note: not suitable for S7) - more efficient than multiple reads (test shows <14mS to read all)
			// iterate through bindings to see what has changed - this approach assumes a small number (< 100)of bindings
		    // otherwise might see what has changed in memory and map to binding
			}
			for (PLCLogoBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()){
					PLCLogoBindingConfig logoBindingConfig = (PLCLogoBindingConfig) provider.getBindingConfig(itemName);
					if (logoBindingConfig.getcontrollerName().equals(controllerName)){
						// it is for our currently selected controller
						int memvalue = Buffer[logoBindingConfig.getRealMemloc()];
						if ((logoBindingConfig.getMemloc().contains("AI"))||(logoBindingConfig.getMemloc().contains("VW"))||(logoBindingConfig.getMemloc().contains("AM"))||(logoBindingConfig.getMemloc().contains("AQ"))){
							// It is a 16 bit read (note: not all are in the docs as supported - yet)
							int mem2value = (int) Buffer[logoBindingConfig.getRealMemloc()+1] &0xff;
							memvalue = memvalue*256+mem2value;
						}
						// logger.debug("Memory is " + memvalue + " at " + logoBindingConfig.getMemloc() );
						// if a bitwise operation then need to perform mask
						if ((logoBindingConfig.getItemType() instanceof SwitchItem)|| (logoBindingConfig.getItemType() instanceof ContactItem)) {
							// bitmask created from bit number
							int bitmask = (0x0001 << logoBindingConfig.getBit()); // this works for 8 bits only as int is signed
							resultant = memvalue & bitmask & 0xff;
						}
						else{
							resultant = memvalue;
						}
						if (resultant != (logoBindingConfig.getLastvalue())){
							if ((logoBindingConfig.getMemloc().contains("AI"))||(logoBindingConfig.getMemloc().contains("AM"))||(logoBindingConfig.getMemloc().contains("AQ"))){
								// check for change being larger than delta
								if (Math.abs(logoBindingConfig.getLastvalue()-resultant ) < logoBindingConfig.getAnalogDelta()){
//									logger.debug("Analog value not larger than delta");
									return;	
								}
							}
							logger.debug("Value changed at " + logoBindingConfig.getitemName() + " to " + resultant);
							Item itemType = provider.getItemType(itemName);
							State state = createState(itemType, resultant);
							eventPublisher.postUpdate(itemName, state);
							logoBindingConfig.setLastvalue(resultant);

						}
					}
				}
				
			}
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		// Note itemname is the item name not the controller name/instance!
		//
		logger.debug("internalReceiveCommand() is called!");
		for (PLCLogoBindingProvider provider : providers) {
			if (!provider.providesBindingFor(itemName))
				continue;
			
			PLCLogoBindingConfig bindingConfig = provider.getBindingConfig(itemName);
			if ((bindingConfig.getRealMemloc() >849 && bindingConfig.getRealMemloc() < 942 )){
				logger.warn("Invalid write requested at memory location "+ bindingConfig.getRealMemloc() + " check config");
				continue;	
			}
				
			if (!controllers.containsKey(bindingConfig.getcontrollerName()))
				continue;
			
			PLCLogoConfig controller = controllers.get(bindingConfig.getcontrollerName());
		/**************************
		 * Send command to the LOGO! controller memory
		 * 
		 */
			S7Client LogoS7Client = controller.getS7Client();
			if (bindingConfig.getItemType() instanceof NumberItem){
				byte[] valueToStore = new byte[2];
				if  (command instanceof DecimalType){
					if (bindingConfig.getMemloc().substring(0, 2).equalsIgnoreCase("VB")){
					// Number and a byte
					valueToStore[0] =	(byte) (Integer.parseInt(command.toString()) & 0xff);
					//Get value from command
					int Result = LogoS7Client.WriteArea(S7.S7AreaDB, 1, bindingConfig.getRealMemloc(), 1, valueToStore);
			    	if (Result != 0){
		    			logger.warn("Failed to write to memory - may attempt reconnect but write failed at  " + bindingConfig.getRealMemloc());
		    				// try and reconnect
		    				LogoS7Client.Disconnect();
		    				LogoS7Client.Connect();
		    				if (LogoS7Client.Connected)
		    					logger.warn("Reconnect successful");
	    			
		    			return;
		    	}
					} else if (bindingConfig.getMemloc().substring(0, 2).equalsIgnoreCase("VW")){
						// Number and a word
						valueToStore[1] =	(byte) (Integer.parseInt(command.toString()) & 0xff);
						valueToStore[0] =	(byte) ((Integer.parseInt(command.toString()) / 256) & 0xff);
						//Get value from command
						int Result = LogoS7Client.WriteArea(S7.S7AreaDB, 1, bindingConfig.getRealMemloc(), 2, valueToStore);
				    	if (Result != 0){
			    			logger.warn("Failed to write to memory - may attempt reconnect but write failed at  " + bindingConfig.getRealMemloc());
			    				// try and reconnect
			    				LogoS7Client.Disconnect();
			    				LogoS7Client.Connect();
			    				if (LogoS7Client.Connected)
			    					logger.warn("Reconnect successful");
		    			
			    			return;
			    	}
	
					}
				}
			} else if (((bindingConfig.getItemType() instanceof SwitchItem)|| (bindingConfig.getItemType() instanceof ContactItem))&& (command instanceof OnOffType)){
				byte[] outBuffer = new byte[1];
				byte bitmask = (byte)(0x01 << bindingConfig.getBit());
				int cmdaction = ((command == OnOffType.ON) ? 0x01 : 0x00 );
				int Result = LogoS7Client.ReadArea(S7.S7AreaDB, 1, bindingConfig.getRealMemloc(), 1, outBuffer);
		    	if (Result != 0){
	    			logger.warn("Failed to read memory - may attempt reconnect but read for a write failed at  " + bindingConfig.getRealMemloc());
	    				// try and reconnect
	    				LogoS7Client.Disconnect();
	    				LogoS7Client.Connect();
	    				if (LogoS7Client.Connected)
	    					logger.warn("Reconnect successful");
    			
	    			return;
	    	}
				if (command == OnOffType.ON){
					outBuffer[0] = (byte) (outBuffer[0]| bitmask);
					logger.debug("plclogo - Sending on");
				} else if (command == OnOffType.OFF){
					outBuffer[0] = (byte) (outBuffer[0]& ~bitmask);
					logger.debug("plclogo - Sending off");
				}
				Result = LogoS7Client.WriteArea(S7.S7AreaDB, 1, bindingConfig.getRealMemloc(), 1, outBuffer);
		    	if (Result != 0){
	    			logger.warn("Failed to write to memory - may attempt reconnect but write failed at  " + bindingConfig.getRealMemloc());
	    				// try and reconnect
	    				LogoS7Client.Disconnect();
	    				LogoS7Client.Connect();
	    				if (LogoS7Client.Connected)
	    					logger.warn("Reconnect successful");
    			
	    			return;
	    	}
			
			}
		
			
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
		logger.debug("internalReceiveUpdate() is called!");
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		InetAddress LogoIP;
		Boolean configured = false;
		if (config != null) {
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			Enumeration<String> keys = config.keys();

			if ( controllers == null ) {
				controllers = new HashMap<String, PLCLogoConfig>();
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
					continue;
				}

				matcher.reset();
				matcher.find();
				String controllerName = matcher.group(1);
				PLCLogoConfig deviceConfig = controllers.get(controllerName);

				if (deviceConfig == null) {
					deviceConfig = new PLCLogoConfig(controllerName);
					controllers.put(controllerName, deviceConfig);
				}
				if (matcher.group(2).equals("host")){
					// matcher.find();
					String IP = config.get(key).toString();
					deviceConfig.setIP(IP);
					configured=true;
				}
				if (matcher.group(2).equals("remoteTSAP")){
					// matcher.find();
					String remoteTSAP = config.get(key).toString();
					deviceConfig.setremoteTSAP(Integer.decode(remoteTSAP));
				}
				if (matcher.group(2).equals("localTSAP")){
					// matcher.find();
					String localTSAP = config.get(key).toString();
					deviceConfig.setlocalTSAP(Integer.decode(localTSAP));
				}
			} //while
		Iterator<Entry<String, PLCLogoConfig>> entries = controllers.entrySet().iterator();
		while (entries.hasNext()){
			Entry<String, PLCLogoConfig> thisEntry = entries.next();
			String controllerName = (String) thisEntry.getKey();
			PLCLogoConfig logoConfig = (PLCLogoConfig) thisEntry.getValue();
			S7Client LogoS7Client = logoConfig.getS7Client();
			if (LogoS7Client == null)
				{LogoS7Client = new Moka7.S7Client();}
			else
				LogoS7Client.Disconnect();
			LogoS7Client.SetConnectionParams(logoConfig.getlogoIP(), logoConfig.getlocalTSAP(), logoConfig.getremoteTSAP());
			int connectPDU = LogoS7Client.Connect();
			if ((connectPDU == 0) && LogoS7Client.Connected ){
				logger.debug("Connected to PLC LOGO! device "+ controllerName );
			}
			else {
				logger.debug("Could not connect to PLC LOGO! device "+ controllerName );
				throw new ConfigurationException("Could not connect to PLC device ",controllerName +" " + logoConfig.getlogoIP().toString() );
			}
			logoConfig.setS7Client(LogoS7Client);
		}
				
		}
		setProperlyConfigured(configured);
	}
	
	
	private State createState(Item itemType, Object value) {
		if (itemType instanceof StringType) {
			return new StringType((String) value);
		} else if (itemType instanceof NumberItem) {
			if (value instanceof Number) {
				return new DecimalType(value.toString());
			} else if (value instanceof String) {
				String stringValue = ((String) value).replaceAll("[^\\d|.]", "");
				return new DecimalType(stringValue);
			} else {
				return null;
			}
		} else if (itemType instanceof SwitchItem) {
			return  ((int)value > 0) ? OnOffType.ON : OnOffType.OFF;
		} else if (itemType instanceof ContactItem)
			return  ((int)value > 0) ? OpenClosedType.CLOSED: OpenClosedType.OPEN;
	
		else {
			return null;
		}
	}

	
	private class PLCLogoConfig {
		/**
		 * Class which represents a LOGO! online controller/PLC connection params
		 * and current instance - there may be multiple PLC's
		 *  
		 * @author g8kmh
		 * @since 1.5.0
		 */

		private final String instancename;
		private String logoIP;
		private int localTSAP = 0x0300;
		private int remoteTSAP = 0x0200;
		private S7Client LogoS7Client;
		
		public PLCLogoConfig (String instancename){
			this.instancename = instancename;
		}
		public void setIP(String logoIP){
			this.logoIP = logoIP;
		}
		public void setlocalTSAP(int localTSAP){
			this.localTSAP = localTSAP;
		}
		public void setremoteTSAP(int remoteTSAP){
			this.remoteTSAP = remoteTSAP;
		}
		public void setS7Client(S7Client LogoS7Client){
			this.LogoS7Client=LogoS7Client;
		}
		public String getlogoIP(){
			return logoIP;
		}
		public String getintancename(){
			return instancename;
		}
		public int getlocalTSAP(){
			return localTSAP;
		}
		public int getremoteTSAP(){
			return remoteTSAP;
		}
		public S7Client getS7Client(){
			return LogoS7Client;
		}
		}

	}

