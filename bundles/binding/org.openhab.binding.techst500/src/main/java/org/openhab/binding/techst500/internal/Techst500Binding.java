/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.techst500.internal;

import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.techst500.Techst500BindingProvider;
import org.openhab.binding.techst500.internal.hardware.Techst500Proxy;
import org.openhab.binding.techst500.internal.hardware.Techst500State;
import org.openhab.binding.techst500.internal.Techst500Binding;
import org.openhab.binding.techst500.internal.Techst500BindingConfig;
import org.openhab.binding.techst500.internal.Techst500BindingConfig.BindingType;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service like querying a Website/Device.
 * 
 * @author snoerenberg
 * @since 1.0
 */
public class Techst500Binding extends AbstractActiveBinding<Techst500BindingProvider> implements ManagedService {

	public static final String CONFIG_KEY_HOST = "host";
	public static final String CONFIG_KEY_USERNAME = "username";
	public static final String CONFIG_KEY_PASSWORD = "password";

	public static final long DEFAULT_REFRESH_INTERVAL = 60000;
	public static final String DEFAULT_DEVICE_UID = "default";

	private static final String BINDING_NAME = "Techst500Binding";

	private static final Logger logger = LoggerFactory
			.getLogger(Techst500Binding.class);

	private long refreshInterval = DEFAULT_REFRESH_INTERVAL;

	// Map of proxies. key=deviceUid, value=proxy
	// Used to keep track of proxies
	private final Map<String, Techst500Proxy> proxies = new HashMap<String, Techst500Proxy>();

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "Techst500 Refresh Service";
	}

	@Override
	protected void execute() {
		try {
			// Iterate through all proxies
			for (Map.Entry<String, Techst500Proxy> entry : proxies
					.entrySet()) {
				String deviceUid = entry.getKey();
				Techst500Proxy receiverProxy = entry.getValue();
				sendUpdates(receiverProxy, deviceUid);
			}
		} catch (Throwable t) {
			logger.error("Error polling devices for " + getName(), t);
		}
	}

	private void sendUpdates(Techst500Proxy receiverProxy, String deviceUid) {
		// Get all item configurations belonging to this proxy
		Collection<Techst500BindingConfig> configs = getDeviceConfigs(deviceUid);
		try {
			
				// Poll the state from the device
				Techst500State state = receiverProxy.getState();
	
				// Create state updates
				State chTemperatureUpdate = new DecimalType(state.getChTemperature());
				State chTemperatureShouldUpdate = new DecimalType(state.getChTemperatureShould());
				State fanSpeedUpdate = new DecimalType(state.getFanSpeed());
				State fanOperatesUpdate = state.isFanOperating() ? OnOffType.ON : OnOffType.OFF;
				State feederOperatesUpdate = state.isFeederOperating() ? OnOffType.ON : OnOffType.OFF;
				State chPumpOperatesUpdate = state.isChPumpOperating() ? OnOffType.ON : OnOffType.OFF;
				State wwPumpOperatesUpdate = state.isWwPumpOperating() ? OnOffType.ON : OnOffType.OFF;
				State htwTemperatureUpdate = new DecimalType(state.getHtwTemperature());
				State currentTimeUpdate = new StringType(state.getCurrentTime());
				State currentDayUpdate = new StringType(state.getCurrentDay().toString());
				State wwTemperatureUpdate = new DecimalType(state.getWwTemperature());
				State outsideTemperatureUpdate = new DecimalType(state.getOutsideTemperature());
				State feederTemperatureUpdate = new DecimalType(state.getFeederTemperature());
				
				// Send updates
				
				// problem that sporadically the controller returns 0°C
				if(state.getChTemperature() != 0)
					sendUpdate(configs, BindingType.chTemperature, chTemperatureUpdate);
				
				sendUpdate(configs, BindingType.chTemperatureShould, chTemperatureShouldUpdate);
				sendUpdate(configs, BindingType.fanSpeed, fanSpeedUpdate);
				sendUpdate(configs, BindingType.fanOperates, fanOperatesUpdate);
				sendUpdate(configs, BindingType.feederOperates, feederOperatesUpdate);
				sendUpdate(configs, BindingType.chPumpOperates, chPumpOperatesUpdate);
				sendUpdate(configs, BindingType.wwPumpOperates, wwPumpOperatesUpdate);
				sendUpdate(configs, BindingType.htwTemperature, htwTemperatureUpdate);
				sendUpdate(configs, BindingType.currentTime, currentTimeUpdate);
				sendUpdate(configs, BindingType.currentDay, currentDayUpdate);
				sendUpdate(configs, BindingType.wwTemperature, wwTemperatureUpdate);
				
				// send update only if > -100°C
				if(state.getOutsideTemperature() > -100)
					sendUpdate(configs, BindingType.outsideTemperature, outsideTemperatureUpdate);
				
				sendUpdate(configs, BindingType.feederTemperature, feederTemperatureUpdate);
			
		} catch (IOException e) {
			logger.warn("Cannot communicate with " + receiverProxy.getHost());
		}
	}

	private Collection<Techst500BindingConfig> getDeviceConfigs(
			String deviceUid) {
		Map<String, Techst500BindingConfig> items = new HashMap<String, Techst500BindingConfig>();
		for (Techst500BindingProvider provider : this.providers) {
			provider.getDeviceConfigs(deviceUid, items);
		}
		return items.values();
	}

	private void sendUpdate(Collection<Techst500BindingConfig> configs, BindingType type, State state) {
		for (Techst500BindingConfig config : configs) {
			if (config.getBindingType() == type) {
				eventPublisher.postUpdate(config.getItemName(), state);
			}
		}
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		
		Techst500BindingConfig config = getConfigForItemName(itemName);
		if (config == null) {
			logger.error("Received command for unknown item '" + itemName + "'");
			return;
		}
		
		Techst500Proxy proxy = proxies.get(config.getDeviceUid());
		if (proxy == null) {
			logger.error("Received command for unknown device uid '"
					+ config.getDeviceUid() + "'");
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug(BINDING_NAME + " processing command '" + command
					+ "' of type '" + command.getClass().getSimpleName()
					+ "' for item '" + itemName + "'");
		}

		try {
			BindingType type = config.getBindingType();
			
			if(type == BindingType.chTemperatureShould)
			{
				if (command instanceof DecimalType)
				{
					float newTemperatureShould = ((DecimalType) command).floatValue();
					
					if (logger.isDebugEnabled()) {
						logger.debug(BINDING_NAME + " new temperatureShould: " + newTemperatureShould);
					}
					
					proxy.setTemperatureShould(newTemperatureShould);
					
					// send new value as update
					State newState = new DecimalType(newTemperatureShould);
					eventPublisher.postUpdate(itemName, newState);
				}
			}
		} catch (IOException e) {
			logger.warn("Cannot communicate with " + proxy.getHost()
					+ " (uid: " + config.getDeviceUid() + ")");
		} catch (Throwable t) {
			logger.error("Error processing command '" + command
					+ "' for item '" + itemName + "'", t);
		}
	}

	private Techst500BindingConfig getConfigForItemName(String itemName) {
		for (Techst500BindingProvider provider : this.providers) {
			if (provider.getItemConfig(itemName) != null) {
				return provider.getItemConfig(itemName);
			}
		}
		return null;
	}

	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// ignore
	}

	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		logger.debug(BINDING_NAME + " updated");
		try {
			// Process device configuration
			if (config != null) {
				String refreshIntervalString = (String) config.get("refresh");
				if (StringUtils.isNotBlank(refreshIntervalString)) {
					refreshInterval = Long.parseLong(refreshIntervalString);
				}
				// parse all configured receivers
				// ( techst500:<uid>.host=10.0.0.2 )
				// ( techst500:<uid>.username=admin )
				// ( techst500:<uid>.password=pass )
				
				Enumeration<String> keys = config.keys();
				while (keys.hasMoreElements()) {
					String key = keys.nextElement();
					if (key.endsWith(CONFIG_KEY_HOST)) {
						// parse host
						String host = (String) config.get(key);

						int separatorIdx = key.indexOf('.');
						// no uid => one device => use default UID
						String uid = separatorIdx == -1 ? DEFAULT_DEVICE_UID : key.substring(0, separatorIdx);
						
						String username = (String) config.get(uid + "." + CONFIG_KEY_USERNAME);
						String password = (String) config.get(uid + "." + CONFIG_KEY_PASSWORD);
						
						// proxy is stateless. keep them in a map in the
						// binding.
						proxies.put(uid, new Techst500Proxy(host, username, password));
					}
				}
				setProperlyConfigured(true);
			}
		} catch (Throwable t) {
			logger.error("Error configuring " + getName(), t);
		}
	}

	@Override
	public void activate() {
		logger.debug(BINDING_NAME + " activated");
	}

	@Override
	public void deactivate() {
		logger.debug(BINDING_NAME + " deactivated");
	}
}
