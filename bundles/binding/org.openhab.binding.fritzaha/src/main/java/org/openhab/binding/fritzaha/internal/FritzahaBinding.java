/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzaha.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.fritzaha.FritzahaBindingProvider;
import org.openhab.binding.fritzaha.internal.hardware.FritzahaWebInterface;
import org.openhab.binding.fritzaha.internal.hardware.interfaces.FritzahaDevice;
import org.openhab.binding.fritzaha.internal.hardware.interfaces.FritzahaOutletMeter;
import org.openhab.binding.fritzaha.internal.hardware.interfaces.FritzahaSwitchedOutlet;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class facilitates the communication between AVM home automation devices
 * and the openHAB event bus
 * 
 * @author Christian Brauers
 * @since 1.3.0
 */
public class FritzahaBinding extends AbstractActiveBinding<FritzahaBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(FritzahaBinding.class);

	/**
	 * the refresh interval which is used to poll values from the fritzaha
	 * server (optional, defaults to 10000ms)
	 */
	private long refreshInterval = 10000;
		
	private static final Pattern DEVICES_PATTERN = Pattern.compile("^(.*?)\\.(host|port|protocol|username|password|synctimeout|asynctimeout)$");

	protected Map<String, Host> hostCache = new HashMap<String, Host>();

	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {
		super.setEventPublisher(eventPublisher);
		for (Host currentHostData : hostCache.values()) {
			currentHostData.eventPublisher = eventPublisher;
			FritzahaWebInterface currentHost = currentHostData.getConnection();
			currentHost.setEventPublisher(eventPublisher);
		}
	}

	@Override
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		super.unsetEventPublisher(eventPublisher);
		for (Host currentHostData : hostCache.values()) {
			currentHostData.eventPublisher = null;
			FritzahaWebInterface currentHost = currentHostData.getConnection();
			currentHost.unsetEventPublisher(eventPublisher);
		}
	}

	public FritzahaBinding() {
	}

	public void activate() {
	}

	public void deactivate() {
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "FritzAHA Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		logger.debug("execute() method is called!");
		for (FritzahaBindingProvider currentProvider : providers) {
			for (String currentItem : currentProvider.getItemNames()) {
				FritzahaDevice currentDevice = currentProvider.getDeviceConfig(currentItem);
				String currentHostId = currentDevice.getHost();
				if (!hostCache.containsKey(currentHostId))
					continue;
				FritzahaWebInterface currentHost = hostCache.get(currentHostId).getConnection();
				if (currentDevice instanceof FritzahaSwitchedOutlet) {
					FritzahaSwitchedOutlet currentSwitch = (FritzahaSwitchedOutlet) currentDevice;
					currentSwitch.updateSwitchState(currentItem, currentHost);
				} else if (currentDevice instanceof FritzahaOutletMeter) {
					FritzahaOutletMeter currentMeter = (FritzahaOutletMeter) currentDevice;
					currentMeter.updateMeterValue(currentItem, currentHost);
				}
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand() is called!");
		FritzahaBindingProvider commandProvider = null;
		FritzahaSwitchedOutlet switchDevice = null;
		for (FritzahaBindingProvider currentProvider : providers) {
			if (!currentProvider.getItemNames().contains(itemName))
				continue;
			FritzahaDevice device = currentProvider.getDeviceConfig(itemName);
			if (!(device instanceof FritzahaSwitchedOutlet))
				continue;
			switchDevice = (FritzahaSwitchedOutlet) device;
			commandProvider = currentProvider;
			break;
		}
		if (commandProvider == null || switchDevice == null) {
			logger.error("No provider found for item " + itemName);
			return;
		}
		if (command instanceof OnOffType) {
			String deviceHostID = switchDevice.getHost();
			Host host = hostCache.get(deviceHostID);
			if(host!=null) {
				FritzahaWebInterface deviceHost = host.getConnection();
				boolean valueToSet = (command == OnOffType.ON);
				switchDevice.setSwitchState(valueToSet, itemName, deviceHost);
			}
		} else {
			logger.debug("Unsupported command type for item " + itemName);
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// No action required on received updates
	}

	/**
	 * @{inheritDoc
	 */
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			// Based on SamsungTv parsing mechanism
			Enumeration<String> keys = config.keys();

			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
			
				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				// to override the default refresh interval one has to add a
				// parameter to openhab.cfg like
				// <bindingName>:refresh=<intervalInMs>
				if ("refresh".equals(key)) {
					refreshInterval = Long.parseLong((String) config.get("refresh"));
					continue;
				}

				Matcher matcher = DEVICES_PATTERN.matcher(key);

				if (!matcher.matches()) {
					logger.debug("given config key '" + key
							+ "' does not follow the expected pattern '<id>.<host|port|protocol|username|password>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String hostId = matcher.group(1);

				Host host = hostCache.get(hostId);

				if (host == null) {
					host = new Host(hostId);
					
					host.eventPublisher = eventPublisher;
					hostCache.put(hostId, host);
					logger.debug("Created new FritzAHA host " + hostId);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("host".equals(configKey)) {
					host.host = value;
				} else if ("port".equals(configKey)) {
					host.port = Integer.valueOf(value);
				} else if ("protocol".equals(configKey)) {
					host.protocol = value;
				} else if ("username".equals(configKey)) {
					host.username = value;
				} else if ("password".equals(configKey)) {
					host.password = value;
				} else if ("synctimeout".equals(configKey)) {
					host.synctimeout = Integer.parseInt((String) value);
				} else if ("asynctimeout".equals(configKey)) {
					host.asynctimeout = Integer.parseInt((String) value);
				} else {
					throw new ConfigurationException(configKey, "the given configKey '" + configKey + "' is unknown");
				}
			}
			setProperlyConfigured(true);
			logger.debug("FritzAHA Binding configured");
		}
	}

	/**
	 * Internal data structure which carries the connection details of one
	 * device (there could be several)
	 */
	static class Host {

		String host = "fritz.box";
		int port = -1;
		String protocol = "http";
		String username = "";
		String password = "";
		int synctimeout = 2000;
		int asynctimeout = 4000;

		FritzahaWebInterface connection;
		String hostId;

		EventPublisher eventPublisher;

		public Host(String hostId) {
			this.hostId = hostId;
		}

		@Override
		public String toString() {
			return "Host [id=" + hostId + ", host=" + host + ", port=" + port + ", username=" + username + "]";
		}

		/**
		 * Creates connection if necessary, uses previously created connection
		 * if not.
		 * 
		 * @return Connection
		 */
		FritzahaWebInterface getConnection() {
			if (connection == null) {
				logger.debug("New connection to " + host + " with timeouts ("+synctimeout+"|"+asynctimeout+").");
				connection = new FritzahaWebInterface(host, port, protocol, username, password, synctimeout, asynctimeout);
				connection.setEventPublisher(eventPublisher);
			}
			return connection;
		}

	}

}
