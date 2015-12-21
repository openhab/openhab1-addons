/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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

import org.apache.commons.lang.StringUtils;
import org.binding.openhab.samsungac.communicator.AirConditioner;
import org.binding.openhab.samsungac.communicator.SsdpDiscovery;
import org.openhab.binding.samsungac.SamsungAcBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
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
		logger.debug("Started Samsung AC Binding");
	}

	public void deactivate() {
		logger.info("deactive");
		// close any open connections
		if(nameHostMapper!=null) {
    		for (AirConditioner connector : nameHostMapper.values()) {
    			connector.disconnect();
    		}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		if (itemName != null && command != null) {
			logger.debug("InternalReceiveCommand [" + itemName + ":"
					+ command + "]");
			String hostName = getAirConditionerInstance(itemName);
			AirConditioner host = nameHostMapper.get(hostName);
			if (host == null) {
				logger.debug("Host with hostname:" +hostName + " not found...");
				return;
			}
			CommandEnum property = getProperty(itemName);

			String cmd = getCmdStringFromEnumValue(command, property);

			if (cmd != null) {
				sendCommand(host, property, cmd);
			} else
				logger.warn("Not sending for itemName: '" + itemName
						+ "' because property not implemented: '" + property
						+ "'");
		}
	}

	private String getCmdStringFromEnumValue(Command command,
			CommandEnum property) {
		String cmd = null;
		switch (property) {
		case AC_FUN_POWER:
		case AC_ADD_SPI:
		case AC_ADD_AUTOCLEAN:
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
		case AC_FUN_ERROR:
		default:
			cmd = command.toString();
			break;
		}
		return cmd;
	}

	private void sendCommand(AirConditioner aircon, CommandEnum property,
			String value) {
		int i = 1;
		boolean commandSent = false;
		while (i < 5 && !commandSent) {
			try {
				logger.debug("[" + i + "/5] Sending command: " + value + " to property:"
						+ property + " with ip:" + aircon.getIpAddress());
				if (aircon.sendCommand(property, value) != null) {
					commandSent = true;
					logger.debug("Command["+ value +"] sent on try number " + i);
				}
			} catch (Exception e) {
				logger.warn("Could not send value: '" + value
						+ "' to property:'" + property + "', try " + i + "/5");
				e.printStackTrace();
			} finally {
				i++;
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

	private String getItemName(String acName, CommandEnum property) {
		for (BindingProvider provider : providers) {
			if (provider instanceof SamsungAcBindingProvider) {
				SamsungAcBindingProvider acProvider = (SamsungAcBindingProvider) provider;
				return acProvider.getItemName(acName, property);
			}
		}
		return null;
	}

	/**
	 * @{inheritDoc
	 */
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		Enumeration<String> keys = config.keys();

		String refreshIntervalString = (String) config.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
			logger.info("Refresh interval set to " + refreshIntervalString + " ms");
		} else {
			logger.info("No refresh interval configured, using default: " + refreshInterval + " ms");
		}
		
		Map<String, AirConditioner> hosts = new HashMap<String, AirConditioner>();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			logger.debug("Configuration key is: " + key);
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
			setProperlyConfigured(false);
			Map<String, Map<String, String>> discovered = SsdpDiscovery.discover();
			if (discovered != null && discovered.size() > 0) {
				for (Map<String, String> ac : discovered.values()) {
					if (ac.get("IP") != null && ac.get("MAC_ADDR") != null)
						logger.warn("We found air conditioner. Please put the following in your configuration file: " +
								"\r\n samsungac:<ACNAME>.host=" + ac.get("IP") +
								"\r\n samsungac:<ACNAME>.mac=" + ac.get("MAC_ADDR"));
				}
			} else {
				logger.warn("No Samsung Air Conditioner has been configured, and we could not find one either");
			}
		} else {
			setProperlyConfigured(true);
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
			String acName = entry.getKey();
			if (host.isConnected()) {
				getAndUpdateStatusForAirConditioner(acName, host);
			} else {
				reconnectToAirConditioner(entry.getKey(), host);
			}
		}
	}

	private void reconnectToAirConditioner(String key, AirConditioner host) {
		logger.info(
				"Broken connection found for '{}', attempting to reconnect...",
				key);
		try {
			host.login();
			logger.info("Connection to {} has succeeded", host.toString());
		} catch (Exception e) {
			if (e == null || e.toString() == null || e.getCause() == null) {
				logger.info("Returned null-exception...");
			} else 
				logger.debug(e.toString() + " : " + e.getCause().toString());
			logger.info(
					"Reconnect failed for '{}', will retry in {}s",
					key, refreshInterval / 1000);
		}
	}

	private void getAndUpdateStatusForAirConditioner(String acName, AirConditioner host) {
		Map<CommandEnum, String> status = new HashMap<CommandEnum, String>();
		try {
			logger.info("Getting status for ac: '" + acName + "'");
			status = host.getStatus();
		} catch (Exception e) {
			logger.info("Could not get status.. returning.., got exception: " + e.toString());
			return;
		}
		
		for (CommandEnum cmd : status.keySet()) {
			logger.debug("Trying to find item for: " + acName + " and cmd: " + cmd.toString());
			String item = getItemName(acName, cmd);
			String value = status.get(cmd);
			if (item != null && value != null) {
				updateItemWithValue(cmd, item, value);
			}
		}
	}

	private void updateItemWithValue(CommandEnum cmd, String item, String value) {
		switch (cmd) {
		case AC_FUN_TEMPNOW:
		case AC_FUN_TEMPSET:
			postUpdate(item, DecimalType.valueOf(value));
			break;
		case AC_FUN_POWER:
		case AC_ADD_SPI:
		case AC_ADD_AUTOCLEAN:
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
							.toString(WindLevelEnum
									.valueOf(value).value)));
			break;
		case AC_FUN_DIRECTION:
			postUpdate(item,
					DecimalType.valueOf(Integer
							.toString(DirectionEnum
									.valueOf(value).value)));
			break;
		case AC_FUN_ERROR:
		default:
			postUpdate(item, StringType.valueOf(value));
			break;
		}
	}

	private void postUpdate(String item, State state) {
		if (item != null && state != null) {
			logger.debug(item + " gets updated to: " + state);
			eventPublisher.postUpdate(item, state);
		} else {
			logger.debug("Could not update item: '" + item + "' with state: '" + state.toString() + "'");
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
