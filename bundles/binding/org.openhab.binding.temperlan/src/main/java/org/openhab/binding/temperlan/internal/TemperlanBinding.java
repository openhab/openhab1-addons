/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.temperlan.internal;

import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.temperlan.TemperlanBindingProvider;
import org.openhab.binding.temperlan.internal.TemperlanBinding;
import org.openhab.binding.temperlan.internal.TemperlanBindingConfig;
import org.openhab.binding.temperlan.internal.TemperlanBindingConfig.BindingType;
import org.openhab.binding.temperlan.internal.hardware.TemperlanProxy;
import org.openhab.binding.temperlan.internal.hardware.TemperlanState;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
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
 * @author snoerenberg
 * @since 1.0
 */
public class TemperlanBinding extends AbstractActiveBinding<TemperlanBindingProvider> implements ManagedService {

	public static final String CONFIG_KEY_HOST = "host";
	public static final long DEFAULT_REFRESH_INTERVAL = 60000;
	public static final String DEFAULT_DEVICE_UID = "default";

	private static final String BINDING_NAME = "TemperlanBinding";

	private static final Logger logger = LoggerFactory
			.getLogger(TemperlanBinding.class);

	private long refreshInterval = DEFAULT_REFRESH_INTERVAL;

	// Map of proxies. key=deviceUid, value=proxy
	// Used to keep track of proxies
	private final Map<String, TemperlanProxy> proxies = new HashMap<String, TemperlanProxy>();

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "Temperlan Refresh Service";
	}

	@Override
	protected void execute() {
		try {
			// Iterate through all proxies
			for (Map.Entry<String, TemperlanProxy> entry : proxies
					.entrySet()) {
				String deviceUid = entry.getKey();
				TemperlanProxy receiverProxy = entry.getValue();
				sendUpdates(receiverProxy, deviceUid);
			}
		} catch (Throwable t) {
			logger.error("Error polling devices for " + getName(), t);
		}
	}

	private void sendUpdates(TemperlanProxy receiverProxy, String deviceUid) {
		// Get all item configurations belonging to this proxy
		Collection<TemperlanBindingConfig> configs = getDeviceConfigs(deviceUid);
		try {
			
				// Poll the state from the device
				TemperlanState state = receiverProxy.getState();
	
				// Create state updates
				State temperature1Update = new DecimalType(state.getTemperature1());
				State temperature2Update = new DecimalType(state.getTemperature2());
				State temperature3Update = new DecimalType(state.getTemperature3());
				State temperature4Update = new DecimalType(state.getTemperature4());
				State temperature5Update = new DecimalType(state.getTemperature5());
				State temperature6Update = new DecimalType(state.getTemperature6());
				State temperature7Update = new DecimalType(state.getTemperature7());
				State temperature8Update = new DecimalType(state.getTemperature8());
				State temperature9Update = new DecimalType(state.getTemperature9());
				State temperature10Update = new DecimalType(state.getTemperature10());
				State temperature11Update = new DecimalType(state.getTemperature11());
				State temperature12Update = new DecimalType(state.getTemperature12());

				// Send updates
				if(state.getTemperature1() > -200) sendUpdate(configs, BindingType.temperature1, temperature1Update);
				if(state.getTemperature2() > -200) sendUpdate(configs, BindingType.temperature2, temperature2Update);
				if(state.getTemperature3() > -200) sendUpdate(configs, BindingType.temperature3, temperature3Update);
				if(state.getTemperature4() > -200) sendUpdate(configs, BindingType.temperature4, temperature4Update);
				if(state.getTemperature5() > -200) sendUpdate(configs, BindingType.temperature5, temperature5Update);
				if(state.getTemperature6() > -200) sendUpdate(configs, BindingType.temperature6, temperature6Update);
				if(state.getTemperature7() > -200) sendUpdate(configs, BindingType.temperature7, temperature7Update);
				if(state.getTemperature8() > -200) sendUpdate(configs, BindingType.temperature8, temperature8Update);
				if(state.getTemperature9() > -200) sendUpdate(configs, BindingType.temperature9, temperature9Update);
				if(state.getTemperature10() > -200) sendUpdate(configs, BindingType.temperature10, temperature10Update);
				if(state.getTemperature11() > -200) sendUpdate(configs, BindingType.temperature11, temperature11Update);
				if(state.getTemperature12() > -200) sendUpdate(configs, BindingType.temperature12, temperature12Update);
			
		} catch (IOException e) {
			logger.warn("Temperlan: cannot communicate with " + receiverProxy.getHost());
		}
	}

	private Collection<TemperlanBindingConfig> getDeviceConfigs(
			String deviceUid) {
		Map<String, TemperlanBindingConfig> items = new HashMap<String, TemperlanBindingConfig>();
		for (TemperlanBindingProvider provider : this.providers) {
			provider.getDeviceConfigs(deviceUid, items);
		}
		return items.values();
	}

	private void sendUpdate(Collection<TemperlanBindingConfig> configs, BindingType type, State state) {
		for (TemperlanBindingConfig config : configs) {
			if (config.getBindingType() == type) {
				eventPublisher.postUpdate(config.getItemName(), state);
			}
		}
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		
		TemperlanBindingConfig config = getConfigForItemName(itemName);
		if (config == null) {
			logger.error("Received command for unknown item '" + itemName + "'");
			return;
		}
		
		TemperlanProxy proxy = proxies.get(config.getDeviceUid());
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
	}

	private TemperlanBindingConfig getConfigForItemName(String itemName) {
		for (TemperlanBindingProvider provider : this.providers) {
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
				// ( temperlan:<uid>.host=10.0.0.2 )
				// ( temperlan:<uid>.username=admin )
				// ( temperlan:<uid>.password=pass )
				
				Enumeration<String> keys = config.keys();
				while (keys.hasMoreElements()) {
					String key = keys.nextElement();
					if (key.endsWith(CONFIG_KEY_HOST)) {
						// parse host
						String host = (String) config.get(key);

						int separatorIdx = key.indexOf('.');
						// no uid => one device => use default UID
						String uid = separatorIdx == -1 ? DEFAULT_DEVICE_UID : key.substring(0, separatorIdx);
												
						// proxy is stateless. keep them in a map in the
						// binding.
						proxies.put(uid, new TemperlanProxy(host));
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
