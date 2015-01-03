/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.frontiersiliconradio.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.frontiersiliconradio.internal.FrontierSiliconRadio;
import org.openhab.binding.frontiersiliconradio.FrontierSiliconRadioBindingProvider;

import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Rainer Ostendorf
 * @since 1.7.0
 */
public class FrontierSiliconRadioBinding extends AbstractActiveBinding<FrontierSiliconRadioBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(FrontierSiliconRadioBinding.class);

	
	/** 
	 * the refresh interval which is used to poll values from the FrontierSiliconRadio
	 * server (optional, defaults to 1000ms)
	 */
	private long refreshInterval = 1000;
	
	/** RegEx to validate a config <code>'^(.*?)\\.(host|port|pin)$'</code> */
	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(host|port|pin|refreshInterval)$");
	
	/**  default tcp port (http). Make this configurable cause maybe people do NAT/portforwarding. */
	private final static int DEFAULT_PORT = 80;
	
	/** the default radio pin. HAMA radio was delivered with '1234' **/
	private final static String DEFAULT_PIN = "1234";
	
	/** Map table to store all available radios configured by the user */
	protected Map<String, FrontierSiliconRadioBindingConfig> deviceConfigCache = null;
	
	
	public FrontierSiliconRadioBinding() {
	}
		
	
	public void activate() {
	}
	
	public void deactivate() {
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
		return "FrontierSiliconRadio Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		
		for (FrontierSiliconRadioBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames() ) {
										
				// find the matching device config for this item				
				String deviceId = provider.getDeviceID(itemName);
				if( deviceId == null ) {
					logger.error("could not find deviceId of item: "+ itemName );
					continue;
				}
				else
				{
					FrontierSiliconRadioBindingConfig deviceConf = deviceConfigCache.get(deviceId);
				
					if( deviceConf != null )	{
						
						// get the assigned radio
						FrontierSiliconRadio radio = deviceConf.getRadio();
						
						// get the assigned property of this item
						String property = provider.getProperty(itemName);							
						
						// depending on the selected property, poll the property from the radio and update the item
						switch( property )
						{
							case "POWER":
								Boolean powerState = radio.getPower();
								logger.debug("powerState is "+ powerState.toString());
								eventPublisher.postUpdate(itemName, powerState?OnOffType.ON:OnOffType.OFF);	
								break;
							case "MODE":
								Integer mode = radio.getMode();
								eventPublisher.postUpdate(itemName, new DecimalType(mode) );
								break;
							case "VOLUME":
								int volume = radio.getVolume();
								logger.debug("volume is "+ volume );
								if( provider.getItemType(itemName) == DimmerItem.class )
									eventPublisher.postUpdate(itemName, new PercentType( radio.convertVolumeToPercent(volume) ) );
								else
									eventPublisher.postUpdate(itemName, new DecimalType( radio.convertVolumeToPercent(volume) ) );
								break;
							case "PLAYINFONAME":
								String playInfoName = radio.getPlayInfoName();
								eventPublisher.postUpdate(itemName, new StringType(playInfoName) );
								break;
							case "PLAYINFOTEXT":
								String playInfoText = radio.getPlayInfoText();
								eventPublisher.postUpdate(itemName, new StringType(playInfoText) );
								break;
							case "PRESET":
								// preset is write-only, ignore
								break;
							case "MUTE":
								Boolean muteState = radio.getMuted();
								logger.debug("mute state is "+ muteState.toString());
								eventPublisher.postUpdate(itemName, muteState?OnOffType.ON:OnOffType.OFF);	
								break;
							default:
								logger.error("unknown property: '"+ property + "'" );
						}
		
					} else {
						logger.error("deviceConf is null, no config found for deviceId: '" + deviceId +"'. Check binding config.");								
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
		logger.debug("internalReceiveCommand() is called!");
		
		for( FrontierSiliconRadioBindingProvider provider : providers )	{
			for (String providerItemName : provider.getItemNames() ) {
			
				if( providerItemName.equals( itemName ) )	{
					
					// find the matching device config for this item
					String deviceId = provider.getDeviceID(itemName);
					if( deviceId == null ) {
						logger.error("could not find deviceId of item: "+ itemName );
						continue;
					}
	
					// try to get the config of the radio from our config cache
					FrontierSiliconRadioBindingConfig deviceConf = deviceConfigCache.get(deviceId);
					
					if( deviceConf != null )
					{
						// get the assigned radio
						FrontierSiliconRadio radio = deviceConf.getRadio();
							
						// get the assigned property for this item
						String property = provider.getProperty(itemName);
							
						// according the the assigned property, send the command to the assigned radio.
						switch( property ) {
						
							case "POWER":
								if (command.equals(OnOffType.ON) || command.equals(OpenClosedType.CLOSED)) {
									radio.setPower(true);
								} else {
									radio.setPower(false);
								}
								break;
							case "VOLUME":
								if (command instanceof IncreaseDecreaseType) {
									if (command.equals(IncreaseDecreaseType.INCREASE) )
										radio.increaseVolume();
									else
										radio.decreaseVolume();
								}
								else if (command instanceof UpDownType) {
									if (command.equals(UpDownType.UP) )
										radio.increaseVolume();
									else
										radio.decreaseVolume();
								}
								else {
									Integer volumeCommand = ((DecimalType) command).intValue();
									Integer absoluteVolume = radio.convertPercentToVolume(volumeCommand);
									radio.setVolume( absoluteVolume );
								}
								break;
							case "MODE":
								Integer mode = ((DecimalType) command).intValue() ;
								radio.setMode(mode);
								break;
							case "PRESET":
								Integer preset = ((DecimalType) command).intValue() ;
								radio.setPreset(preset);
								break;
							case "MUTE":
								if (command.equals(OnOffType.ON) || command.equals(OpenClosedType.CLOSED)) {
									radio.setMuted(true);
								} else {
									radio.setMuted(false);
								}
								break;
							default:
								logger.error("command on unkown property: '"+property+"'. Maybe trying to set read-only property?");
								break;
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
	protected void internalReceiveUpdate(String itemName, State newState) {
		
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		
		if (config != null) {
			
			logger.debug("Configuration updated, config {}", config != null ? true : false);
			
			Enumeration<String> keys = config.keys();
			
			if ( deviceConfigCache == null ) {
				deviceConfigCache = new HashMap<String, FrontierSiliconRadioBindingConfig>();
			}
			
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);

				if (!matcher.matches()) {
					logger.debug("given config key '" + key
						+ "' does not follow the expected pattern '<id>.<host|port|pin>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String deviceId = matcher.group(1);

				FrontierSiliconRadioBindingConfig deviceConfig = deviceConfigCache.get(deviceId);

				if (deviceConfig == null) {
					deviceConfig = new FrontierSiliconRadioBindingConfig(deviceId);
					deviceConfigCache.put(deviceId, deviceConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("host".equals(configKey)) {
					logger.debug("Hostname for " + deviceId + " is " + value);
					deviceConfig.host = value;
				} else if ("port".equals(configKey)) {
					logger.debug("Portnumber for " + deviceId + " is " + value);
					deviceConfig.port = Integer.valueOf(value);
				} else if ("pin".equals(configKey)) {
					logger.debug("PIN for " + deviceId + " is " + value);
					deviceConfig.pin = value;
				} else if ("refreshInterval".equals(configKey)) {
					logger.debug("refreshInterval is " + value);
					refreshInterval = Long.parseLong(value);
				}
				else {
					throw new ConfigurationException(configKey, "the given config key '" + configKey + "' is unknown");
				}
			}
			
			// open connection to radio
			for (String device : deviceConfigCache.keySet()) {
				FrontierSiliconRadio radio = deviceConfigCache.get(device).getRadio();
				if (radio != null) {
					radio.login();
				}
			}
		}

		setProperlyConfigured(true);
	}
	
	/**
	 * Holds the binding configuration, consisting of:
	 * - deviceID (e.g. "sleepingroom")
	 * - host (e.g. "192.167.2.23" )
	 * - portnumber (defaults to 80)
	 * - Access PIN (e.g. 1234) 
	 * 
	 * @author Rainer Ostendorf
	 *
	 */
	class FrontierSiliconRadioBindingConfig  {
	
		String deviceId; // the endpoint identifier, e.g. "RadioKitchen"
		String host;     // hostname or ip
		int port = DEFAULT_PORT; // TCP portnumber
		String pin = DEFAULT_PIN; 
		
		FrontierSiliconRadio radio = null;

		public FrontierSiliconRadioBindingConfig(String deviceId) {
			this.deviceId = deviceId;
		}

		public String getHost(){
			return host;
		}
		
		public int getPort(){
			return port;
		}
		
		@Override
		public String toString() {
			return "Device [id=" + deviceId + ", host=" + host + ", port=" + port + ",  pin: "+ pin + "]";
		}

		FrontierSiliconRadio getRadio() {
			if (radio == null) {
				logger.debug("creating new connection to " + host + ":" + port );
				radio = new FrontierSiliconRadio(host, port, pin);
			}
			return radio;
		}
	}
}
