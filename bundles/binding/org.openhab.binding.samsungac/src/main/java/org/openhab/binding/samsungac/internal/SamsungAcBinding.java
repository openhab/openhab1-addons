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

import org.binding.openhab.samsungac.communicator.AirConditioner;
import org.binding.openhab.samsungac.communicator.SsdpDiscovery;
import org.openhab.binding.samsungac.SamsungAcBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Binding listening OpenHAB bus and send commands to Samsung Air Conditioner
 * devices when command is received.
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

	private Map<String, AirConditioner> nameHostMapper = null;

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
		for (AirConditioner connector : nameHostMapper.values()) {
			connector.disconnect();
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		if (itemName != null && command != null) {
			logger.debug("InternalReceiveCommand +'" + itemName + "':'"
					+ command + "'");
			String hostName = getAirConditionerInstance(itemName);
			AirConditioner host = nameHostMapper.get(hostName);
			if (host == null) {
				return;
			}
			CommandEnum property = getProperty(itemName);

			String cmd = null;
			switch (property) {
			case AC_FUN_POWER:
				cmd = "ON".equals(command.toString()) ? "On" : "Off";
				break;
			case AC_FUN_WINDLEVEL:
				cmd = WindLevelEnum.getFromValue(command).toString();
				break;
			case AC_FUN_OPMODE:
				cmd = OperationModeEnum.getFromValue(command).toString();
				break;
			case AC_FUN_COMODE:
				cmd = ConvenientModeEnum.getFromValue(command).toString();
				break;
			case AC_FUN_DIRECTION:
				cmd = DirectionEnum.getFromValue(command).toString();
				break;
			case AC_FUN_TEMPSET:
				cmd = command.toString();
				break;
			default:
				cmd = command.toString();
				break;
			}

			if (cmd != null) {
				sendCommand(host, property, cmd);
			} else
				logger.debug("Not sending for itemName: '" + itemName
						+ "' because property not implemented: '" + property
						+ "'");
		}
	}

	private void sendCommand(AirConditioner aircon, CommandEnum property,
			String value) {
		try {
			logger.debug("Sending command: " + value + " to property:"
					+ property + " with ip:" + aircon.getIpAddress());
			aircon.sendCommand(property, value);
		} catch (Exception e) {
			logger.warn("Could not send value: '" + value + "' to property:'"
					+ property + "'");
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

	private CommandEnum getProperty(String itemName) {
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

	private String getItemName(CommandEnum property) {
		for (BindingProvider provider : providers) {
			if (provider instanceof SamsungAcBindingProvider) {
				SamsungAcBindingProvider acProvider = (SamsungAcBindingProvider) provider;
				return acProvider.getItemName(property);
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
		Enumeration<String> keys = config.keys();

		Map<String, AirConditioner> hosts = new HashMap<String, AirConditioner>();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			if ("service.pid".equals(key)) {
				continue;
			}

			String[] parts = key.split("\\.");
			String hostname = parts[0];

			AirConditioner host = hosts.get(hostname);
			if (host == null) {
				host = new AirConditioner();
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
		
		if (nameHostMapper == null || nameHostMapper.size() == 0) {
			Map<String, String> discovered = SsdpDiscovery.discover();
			if (discovered != null && discovered.size() > 0) {
				logger.warn("No Samsung Air Conditioner has been configured, but we found one with IP:'" +
						discovered.get("IP") + "' and MAC ADDRESS: '" + discovered.get("MAC_ADDR") + "'");
				AirConditioner host = new AirConditioner();
				host.setIpAddress(discovered.get("IP"));
				host.setMacAddress(discovered.get("MAC_ADDR"));
				nameHostMapper.put("DiscoveredAC", host);
			}
		}
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

		for (Map.Entry<String, AirConditioner> entry : nameHostMapper
				.entrySet()) {
			AirConditioner host = entry.getValue();
			if (host.isConnected()) {
				Map<CommandEnum, String> status = host.getStatus();

				for (CommandEnum cmd : CommandEnum.values()) {
					String item = getItemName(cmd);
					String value = status.get(cmd);
					if (item != null && value != null) {
						switch (cmd) {
						case AC_FUN_TEMPNOW:
						case AC_FUN_TEMPSET:
							postUpdate(item, DecimalType.valueOf(value));
							break;
						case AC_FUN_POWER:
							postUpdate(
									item,
									value.toUpperCase().equals("ON") ? OnOffType.ON
											: OnOffType.OFF);
							break;
						case AC_FUN_COMODE:
							postUpdate(item,
									DecimalType.valueOf(Integer
											.toString(ConvenientModeEnum
													.valueOf(value).value)));
							break;
						case AC_FUN_OPMODE:
							postUpdate(item,
									DecimalType.valueOf(Integer
											.toString(OperationModeEnum
													.valueOf(value).value)));
							break;
						case AC_FUN_WINDLEVEL:
							postUpdate(item,
									DecimalType.valueOf(Integer
											.toString(DirectionEnum
													.valueOf(value).value)));
							break;
						case AC_FUN_DIRECTION:
							postUpdate(item,
									DecimalType.valueOf(Integer
											.toString(DirectionEnum
													.valueOf(value).value)));
							break;
						default:
							logger.debug("Not implementation for updating: '"
									+ cmd + "'");
							break;
						}
					}
				}
			} else {
				// broken connection so attempt to reconnect
				logger.debug(
						"Broken connection found for '{}', attempting to reconnect...",
						entry.getKey());
				try {
					host.login();
				} catch (Exception e) {
					logger.debug(
							"Reconnect failed for '{}', will retry in {}s",
							entry.getKey(), refreshInterval / 1000);
					
					logger.debug("Trying to discover the AC");
					Map<String, String> discovered = SsdpDiscovery.discover();
					if (discovered != null && discovered.size() > 0) {
						logger.warn("Samsung Air Conditioner has been configured, and we found one with IP:'" +
								discovered.get("IP") + "' and MAC ADDRESS: '" + discovered.get("MAC_ADDR") + "'");
					}
				}
			}
		}
	}

	private void postUpdate(String item, State state) {
		if (item != null && state != null) {
			logger.debug(item + " gets updated to: " + state);
			eventPublisher.postUpdate(item, state);
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
