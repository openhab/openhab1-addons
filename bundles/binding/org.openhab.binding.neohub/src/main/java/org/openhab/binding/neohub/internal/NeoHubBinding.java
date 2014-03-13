/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.neohub.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.openhab.binding.neohub.NeoHubBindingProvider;
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
 * Actively polls neohub for status of all devices.
 * 
 * @author Sebastian Prehn
 * @since 1.5.0
 */
public class NeoHubBinding extends AbstractActiveBinding<NeoHubBindingProvider>
		implements ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(NeoHubBinding.class);

	/**
	 * The refresh interval which is used to poll values from the neohub server
	 * (optional, defaults to 60000ms).
	 */
	private long refreshInterval = 60000;

	/**
	 * Hostname or IP address of neohub on tcp network.
	 */
	private String hostname;

	/**
	 * Port of neohub on tcp network. (optional, defaults to 4242).
	 */
	private int port = 4242;

	public NeoHubBinding() {
	}

	public void activate() {
		logger.debug("neohub activated");
	}

	public void deactivate() {
		logger.debug("neohub deactivated");
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
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
		return "neohub Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		logger.debug("execute() method is called!");

		final NeoHubProtocol protocol = new NeoHubProtocol(new NeoHubConnector(
				hostname, port));

		try {
			// send info request
			final InfoResponse response = protocol.info();
			for (NeoHubBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					String device = provider.getNeoStatDevice(itemName);

					State result = null;
					switch (provider.getNeoStatProperty(itemName)) {
					case CurrentTemperature:
						result = new DecimalType(
								response.getCurrentTemperature(device));
						break;

					case CurrentFloorTemperature:
						result = new DecimalType(
								response.getCurrentFloorTemperature(device));
						break;
					case CurrentSetTemperature:
						result = new DecimalType(
								response.getCurrentSetTemperature(device));
						break;
					case DeviceName:
						result = new StringType(response.getDeviceName(device));
						break;
					case Away:
						result = response.isAway(device) ? OnOffType.ON
								: OnOffType.OFF;
						break;
					case Standby:
						result = response.isStandby(device) ? OnOffType.ON
								: OnOffType.OFF;
						break;
					case Heating:
						result = response.isHeating(device) ? OnOffType.ON
								: OnOffType.OFF;
						break;
					}

					if (eventPublisher != null && result != null) {
						eventPublisher.postUpdate(itemName, result);
					}
				}
			}

		} catch (final JSONException e) { // runtime exception
			logger.error(
					"Failed to parse response or fetch expected result from it. Please check your configuration. Does device with index exist?",
					e);
			return;
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
		logger.debug(
				"internalReceiveCommand() is called! itemName {}, command {}",
				itemName, command);

		for (NeoHubBindingProvider provider : providers) {
			if (!provider.providesBindingFor(itemName)) {
				continue;
			}

			String device = provider.getNeoStatDevice(itemName);
			NeoStatProperty property = provider.getNeoStatProperty(itemName);
			final NeoHubProtocol protocol = new NeoHubProtocol(
					new NeoHubConnector(hostname, port));

			switch (property) {
			case Away:
				if (command instanceof OnOffType) {
					OnOffType onOffType = (OnOffType) command;
					protocol.setAway(OnOffType.ON == onOffType, device);
				}
				break;
			case Standby:
				if (command instanceof OnOffType) {
					OnOffType onOffType = (OnOffType) command;
					protocol.setStandby(OnOffType.ON == onOffType, device);
				}
			default:
				break;
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
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			// read further config parameters here ..
			String host = (String) config.get("hostname");
			if (StringUtils.isNotBlank(host)) {
				this.hostname = host;
			}

			String port = (String) config.get("port");
			if (StringUtils.isNotBlank(port)) {
				this.port = Integer.parseInt(port);
			}

			setProperlyConfigured(true);
		}
	}

}
