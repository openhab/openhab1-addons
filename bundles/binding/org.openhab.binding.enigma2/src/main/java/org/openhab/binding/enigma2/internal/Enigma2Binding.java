/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enigma2.internal;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.enigma2.Enigma2BindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Sebastian Kutschbach
 * @since 1.6.0
 */
public class Enigma2Binding extends
		AbstractActiveBinding<Enigma2BindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(Enigma2Binding.class);

	private HashMap<String, Enigma2Node> enigmaNodes = new HashMap<String, Enigma2Node>();

	/**
	 * the refresh interval which is used to poll values from the Enigma2 server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	public Enigma2Binding() {
	}

	public void activate() {
	}

	public void deactivate() {
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
		return "Enigma2 Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		logger.debug("execute() called");
		for (Enigma2BindingProvider provider : providers) {
			Collection<String> names = provider.getItemNames();
			for (String name : names) {
				logger.debug("Name {}", name);
				Enigma2BindingConfig bindingConfig = provider
						.getBindingConfigFor(name);
				String deviceId = bindingConfig.getDeviceId();

				/*
				 * check if a device with this id is already configured
				 */
				if (enigmaNodes.containsKey(deviceId)) {
					Enigma2Node node = enigmaNodes.get(deviceId);

					/*
					 * yes, there is a device with this id, now update it
					 */
					String value = null;
					switch (bindingConfig.getCmdId()) {
					case VOLUME:
						value = node.getVolume();
						break;
					case CHANNEL:
						value = node.getChannel();
						break;
					case POWERSTATE:
						value = node.getOnOff();
						break;
					case PAUSE:
						logger.debug("Querying the player state (Play/Pause) is currently not supported");
						break;
					case MUTE:
						value = node.getMuteUnmute();
						break;
					case DOWNMIX:
						value = node.getDownmix();
					default:
						break;
					}
					if (value != null) {
						postUpdate(provider, bindingConfig.getItem(), value);
					}
				} else {
					logger.error("Unknown deviceId \"{}\"", deviceId);
				}
			}
		}
	}

	private void postUpdate(Enigma2BindingProvider provider, Item item,
			final String value) {
		Class<? extends Item> itemType = provider.getItemType(item.getName());
		State state = createState(itemType, value);

		if (state != null) {
			if (item instanceof GenericItem) {
				((GenericItem) item).setState(state);
			}
			eventPublisher.postUpdate(item.getName(), state);
		}
	}

	private State createState(Class<? extends Item> itemType,
			String transformedResponse) {
		try {
			if (itemType.isAssignableFrom(NumberItem.class)) {
				return DecimalType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(SwitchItem.class)) {
				return OnOffType.valueOf(transformedResponse);
			} else if (itemType.isAssignableFrom(DimmerItem.class)) {
				return PercentType.valueOf(transformedResponse);
			} else {
				return StringType.valueOf(transformedResponse);
			}
		} catch (Exception e) {
			logger.debug("Couldn't create state of type '{}' for value '{}'",
					itemType, transformedResponse);
			return StringType.valueOf(transformedResponse);
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug(
				"internalReceiveCommand() called, itemName={}, command={}",
				itemName, command);
		/*
		 * do we have a binding for this item at all?
		 */
		if (providesBindingFor(itemName)) {
			/*
			 * go through all the providers and look for a BindingConfig
			 */
			for (Enigma2BindingProvider provider : providers) {
				Enigma2BindingConfig bindingConfig = provider
						.getBindingConfigFor(itemName);
				if (bindingConfig == null) {
					continue;
				}

				/*
				 * Found one
				 */
				Enigma2Node node = enigmaNodes.get(bindingConfig.getDeviceId());
				if (node == null) {
					logger.error("Invalid deviceId {}",
							bindingConfig.getDeviceId());
				}

				switch (bindingConfig.getCmdId()) {
				case VOLUME:
					node.setVolume(command);
					break;
				case CHANNEL:
					node.setChannel(command);
					break;
				case PAUSE:
					node.sendPlayPause(command);
					break;
				case MUTE:
					node.sendMuteUnmute(command);
					break;
				case REMOTECONTROL:
					node.sendRcCommand(command, bindingConfig.getCmdValue());
					break;
				case POWERSTATE:
					node.sendOnOff(command, Enigma2PowerState.STANDBY);
					break;
				case DOWNMIX:
					node.setDownmix(command);
					break;
				default:
					logger.error("Unknown cmdId \"{}\"",
							bindingConfig.getCmdId());
				}
			}
		} else {
			logger.trace("No provider found for this item");
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				String[] keyElements = key.split(":");
				String deviceId = keyElements[0];

				if (keyElements.length >= 2) {

					Enigma2Node node = enigmaNodes.get(deviceId);
					if (node == null) {
						node = new Enigma2Node();
						enigmaNodes.put(deviceId, node);
					}

					String option = keyElements[1];
					if (option.equals("hostname")) {
						node.setHostName((String) config.get(key));
					} else if (option.equals("username")) {
						node.setUserName((String) config.get(key));
					} else if (option.equals("password")) {
						node.setPassword((String) config.get(key));
					}

				}
			}

			setProperlyConfigured(checkProperlyConfigured());
		}
	}

	private boolean checkProperlyConfigured() {
		for (Enigma2Node node : this.enigmaNodes.values()) {
			if (!node.properlyConfigured()) {
				return false;
			}
		}
		return true;
	}
}
