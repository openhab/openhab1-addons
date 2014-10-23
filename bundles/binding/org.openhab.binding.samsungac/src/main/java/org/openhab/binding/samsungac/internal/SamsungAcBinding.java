/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.samsungac.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.binding.openhab.samsungac.communicator.SamsungAcHost;
import org.openhab.binding.samsungac.SamsungAcBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Binding listening OpenHAB bus and send commands to Samsung devices when
 * command is received.
 * 
 * @author Stein Tore Tøsse
 * @since 1.6.0
 */
public class SamsungAcBinding extends
		AbstractActiveBinding<SamsungAcBindingProvider> implements
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(SamsungAcBinding.class);

	/**
	 * the refresh interval which is used to check for lost connections
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	private Map<String, SamsungAcHost> nameHostMapper = null;

	protected Map<String, Map<String, String>> deviceConfigCache = new HashMap<String, Map<String, String>>();

	public SamsungAcBinding() {
	}

	public void activate() {
		logger.info("active");
		setProperlyConfigured(true);
	}

	public void deactivate() {
		logger.info("deactive");

		// close any open connections
		for (SamsungAcHost connector : nameHostMapper.values()) {
			if (connector.getAirConditioner() != null) {
				connector.getAirConditioner().disconnect();
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		if (itemName != null) {
			logger.debug("InternalReceiveCommand +'" + itemName + "':'"
					+ command + "'");
			String hostName = getAirConditionerInstance(itemName);
			logger.debug("hostName: " + hostName);
			String property = getProperty(itemName);
			logger.debug("property: " + property);
			
			SamsungAcHost host = nameHostMapper.get(hostName);
			
			Command commandEnum = Command.valueOf(property);
			
			switch (commandEnum) {
			case "AC_FUN_POWER":
				
				break;

			default:
				break;
			}
			
			if ("AC_FUN_POWER".equals(property)) {
				String cmd = "Off";
				if (command.toString() == "ON") cmd = "On";
				host.getAirConditioner().sendCommand(property, cmd);
			}
			
		}
	}
	
	private String getAirConditionerInstance(String itemName) {
		for (BindingProvider provider : providers) {
			if (provider instanceof SamsungAcBindingProvider) {
				SamsungAcBindingProvider acProvider = (SamsungAcBindingProvider) provider;
				if (acProvider.getItemNames().contains(itemName)) {
					return acProvider.getAirConditionerInstance(itemName);
				}
			}
		}
		return null;
	}
	
	private String getProperty(String itemName) {
		for (BindingProvider provider : providers) {
			if (provider instanceof SamsungAcBindingProvider) {
				SamsungAcBindingProvider acProvider = (SamsungAcBindingProvider) provider;
				if (acProvider.getItemNames().contains(itemName)) {
					return acProvider.getProperty(itemName);
				}
			}
		}
		return null;
	}
	
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		if (itemName != null) {
			logger.debug("InternalReceiveState +'" + itemName + "':'"
					+ newState + "'");
		}
	}

	/**
	 * @{inheritDoc
	 */
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		logger.info("Updated:" + config);

		Enumeration<String> keys = config.keys();

		Map<String, SamsungAcHost> hosts = new HashMap<String, SamsungAcHost>();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if ("service.pid".equals(key)) {
				continue;
			}

			String[] parts = key.split("\\.");
			String hostname = parts[0];

			SamsungAcHost host = hosts.get(hostname);
			if (host == null) {
				host = new SamsungAcHost();
			}

			String value = ((String) config.get(key)).trim();

			if ("host".equals(parts[1])) {
				host.setIpAddress(value);
			}
			if ("mac".equals(parts[1])) {
				host.setMacAddress(value);
			}
			if ("token".equals(parts[1])) {
				host.setToken(value);
			}
			hosts.put(hostname, host);
		}

		nameHostMapper = hosts;
		logger.debug("Size of nameHostMapper is: " + nameHostMapper.size());
	}

	@Override
	protected void execute() {
		if (!bindingsExist()) {
			logger.debug("There is no existing Samsung AC binding configuration => refresh cycle aborted!");
			return;
		}
		
		if (nameHostMapper == null) {
			logger.debug("Name host mapper not yet set. Aborted refresh");
			return;
		}
		
		for (Map.Entry<String, SamsungAcHost> entry : nameHostMapper.entrySet()) {
			SamsungAcHost host = entry.getValue();
			if (host.isConnected()) {
				Map<String, String> status =  host.getAirConditioner().getStatus();
				
				eventPublisher.postUpdate("ac_current_temp", StringType.valueOf(status.get("AC_FUN_TEMPNOW")));
				
			} else {
				// broken connection so attempt to reconnect
				logger.debug("Broken connection found for '{}', attempting to reconnect...", entry.getKey());
				try {
					host.connect();
					logger.debug("Connected");
				} catch (Exception e) {
					logger.debug("Reconnect failed for '{}', will retry in {}s", entry.getKey(), refreshInterval / 1000);
				}
			} 
		}
	}

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "Samsung Air Conditioner service";
	}
}
