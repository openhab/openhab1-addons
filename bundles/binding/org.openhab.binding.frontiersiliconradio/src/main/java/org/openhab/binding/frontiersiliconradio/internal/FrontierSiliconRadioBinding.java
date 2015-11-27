/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding for radios based on the Fontier Silicon Chipset. It is a simple refresh-based binding that updates item
 * providers. Moreover, a {@link #cachePeriod} can be used to cache the item states so that the event bus is not flooded
 * with events that do not change any state.
 * 
 * @author Rainer Ostendorf
 * @author paphko
 * @since 1.7.0
 */
public class FrontierSiliconRadioBinding extends AbstractActiveBinding<FrontierSiliconRadioBindingProvider> implements
		ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(FrontierSiliconRadioBinding.class);

	/** RegEx to validate a config <code>'^(.*?)\\.?(host|port|pin|refreshInterval|cachePeriod)$'</code> */
	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern
			.compile("^(.*?)\\.?(host|port|pin|refreshInterval|cachePeriod)$");

	/** default tcp port (http). Make this configurable cause maybe people do NAT/portforwarding. */
	private static final int DEFAULT_PORT = 80;

	/** the default radio pin. HAMA radio was delivered with '1234' **/
	private static final String DEFAULT_PIN = "1234";

	/**
	 * the refresh interval which is used to poll values from the FrontierSiliconRadio server (optional, defaults to
	 * 60000ms = 1min)
	 */
	private long refreshInterval = 60000;

	/** If caching is enabled, keep cache for this amount of minutes. */
	private long cachePeriod = 0;

	/** Remember last time the cache was purged. */
	private long lastCachePurge = 0;

	/** Map table to store all available radios configured by the user */
	private final Map<String, FrontierSiliconRadioBindingConfig> deviceConfigCache = new HashMap<String, FrontierSiliconRadioBindingConfig>();

	/** Cache all values so that we do not spam the event bus in case nothing changed. */
	private final Map<String, Map<String, Object>> deviceToStateMap = new HashMap<String, Map<String, Object>>();

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "FrontierSiliconRadio Service";
	}

	@Override
	protected void execute() {
		long now = System.currentTimeMillis();

		// clear cache after <cachePeriod> minutes
		if (cachePeriod > 0 && lastCachePurge + (cachePeriod * 60000) < now) {
			logger.debug("Clearing cache because it was older than " + cachePeriod + " minutes.");
			deviceToStateMap.clear();
			lastCachePurge = now;
		}

		for (FrontierSiliconRadioBindingProvider provider : providers) {
			updateProvider(provider);
		}
	}

	/**
	 * Update all items of the given item provider.
	 */
	private void updateProvider(FrontierSiliconRadioBindingProvider provider) {
		for (String itemName : provider.getItemNames()) {

			// find the matching device config for this item
			final String deviceId = provider.getDeviceID(itemName);
			if (deviceId == null) {
				logger.error("could not find deviceId of item: " + itemName);
				continue;
			} else {
				final FrontierSiliconRadioBindingConfig deviceConf = deviceConfigCache.get(deviceId);
				if (deviceConf != null) {

					// get the assigned radio and its state (on or off)
					final FrontierSiliconRadio radio = deviceConf.getRadio();
					final boolean powerState = radio.getPower();

					// get the assigned property of this item
					final String property = provider.getProperty(itemName);

					// if radio is OFF, set all values (except of power item) to uninitialized
					if (!powerState && !"POWER".equals(property)) {
						if (stateChanged(deviceId, property, null)) {
							eventPublisher.postUpdate(itemName, UnDefType.UNDEF);
						}
						continue; // continue with next item
					}

					// depending on the selected property, poll the property from the radio and update the item
					switch (property) {
					case "POWER":
						if (stateChanged(deviceId, property, powerState)) {
							logger.debug("powerState changed to " + powerState);
							eventPublisher.postUpdate(itemName, powerState ? OnOffType.ON : OnOffType.OFF);
						}
						break;
					case "MODE":
						final int mode = radio.getMode();
						if (stateChanged(deviceId, property, mode)) {
							logger.debug("powerState changed to " + mode);
							eventPublisher.postUpdate(itemName, new DecimalType(mode));
						}
						break;
					case "VOLUME":
						final int volume = radio.getVolume();
						if (stateChanged(deviceId, property, volume)) {
							final int percent = radio.convertVolumeToPercent(volume);
							logger.debug("volume changed to " + volume + " (" + percent + "%)");
							if (provider.getItemType(itemName) == DimmerItem.class) {
								eventPublisher.postUpdate(itemName, new PercentType(percent));
							} else {
								eventPublisher.postUpdate(itemName, new DecimalType(percent));
							}
						}
						break;
					case "PLAYINFONAME":
						final String playInfoName = radio.getPlayInfoName();
						if (stateChanged(deviceId, property, playInfoName)) {
							logger.debug("play info name changed to " + playInfoName);
							eventPublisher.postUpdate(itemName, new StringType(playInfoName));
						}
						break;
					case "PLAYINFOTEXT":
						final String playInfoText = radio.getPlayInfoText();
						if (stateChanged(deviceId, property, playInfoText)) {
							logger.debug("play info text changed to " + playInfoText);
							eventPublisher.postUpdate(itemName, new StringType(playInfoText));
						}
						break;
					case "PRESET":
						// preset is write-only, ignore
						break;
					case "MUTE":
						final boolean muteState = radio.getMuted();
						if (stateChanged(deviceId, property, muteState)) {
							logger.debug("mute state changed to " + muteState);
							eventPublisher.postUpdate(itemName, muteState ? OnOffType.ON : OnOffType.OFF);
						}
						break;
					default:
						logger.error("unknown property: '" + property + "'");
					}

				} else {
					logger.error("deviceConf is null, no config found for deviceId: '" + deviceId
							+ "'. Check binding config.");
				}
			}
		}
	}

	/**
	 * If {@link #cachePeriod} is set, this call checks whether the value changed. In the end, <code>newValue</code> is
	 * cached so that it can be compared in subsequent calls.
	 * 
	 * @param deviceId
	 *            The radio id.
	 * @param property
	 *            The property that is checked for update.
	 * @param newValue
	 *            The new value of the property.
	 * @return <code>true</code> if the value changed compared to the last call or {@link #cachePeriod} is not set;
	 *         <code>false</code> if the value did not changed compared to the last call and {@link #cachePeriod} is
	 *         set.
	 */
	private boolean stateChanged(String deviceId, String property, Object newValue) {
		if (cachePeriod <= 0)
			return true; // no caching
		final Map<String, Object> map = deviceToStateMap.get(deviceId);
		if (map == null) {
			final Map<String, Object> newMap = new HashMap<String, Object>();
			newMap.put(property, newValue);
			deviceToStateMap.put(deviceId, newMap);
			return true;
		}
		final Object oldValue = map.get(property);
		if (oldValue == null ? newValue != null : !oldValue.equals(newValue)) {
			map.put(property, newValue);
			return true;
		}
		return false;
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		for (final FrontierSiliconRadioBindingProvider provider : providers) {
			for (final String providerItemName : provider.getItemNames()) {
				if (providerItemName.equals(itemName)) {
					handleReceiveCommand(provider, itemName, command);
				}
			}
		}
	}

	/**
	 * Handle the command received for the given item provider and item. If the command switches the power state of the
	 * radio, all other items are updated afterwards independent of the next refresh call.
	 * 
	 * @param provider
	 * @param itemName
	 * @param command
	 */
	private void handleReceiveCommand(final FrontierSiliconRadioBindingProvider provider, final String itemName,
			final Command command) {
		// find the matching device config for this item
		final String deviceId = provider.getDeviceID(itemName);
		if (deviceId == null) {
			logger.error("could not find deviceId of item: " + itemName);
			return;
		}

		// try to get the config of the radio from our config cache
		final FrontierSiliconRadioBindingConfig deviceConf = deviceConfigCache.get(deviceId);

		if (deviceConf != null) {
			// get the assigned radio
			final FrontierSiliconRadio radio = deviceConf.getRadio();

			// get the assigned property for this item
			final String property = provider.getProperty(itemName);

			// according the the assigned property, send the command to the assigned radio.
			switch (property) {
			case "POWER":
				if (command.equals(OnOffType.ON) || command.equals(OpenClosedType.CLOSED)) {
					radio.setPower(true);
				} else {
					radio.setPower(false);
				}
				// now all items should be updated! (wait some seconds so that text items are up-to-date)
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							// let's hope 4 seconds are enough...
							Thread.sleep(4000);
						} catch (InterruptedException e) {
						}
						updateProvider(provider);
					}
				}).start();
				break;
			case "VOLUME":
				if (command instanceof IncreaseDecreaseType) {
					if (command.equals(IncreaseDecreaseType.INCREASE))
						radio.increaseVolume();
					else
						radio.decreaseVolume();
				} else if (command instanceof UpDownType) {
					if (command.equals(UpDownType.UP))
						radio.increaseVolume();
					else
						radio.decreaseVolume();
				} else if (command instanceof PercentType) {
					final Integer percentValue = ((DecimalType) command).intValue();
					final Integer absoluteValue = radio.convertPercentToVolume(percentValue);
					radio.setVolume(absoluteValue);
				} else if (command instanceof DecimalType) {
					final Integer intValue = ((DecimalType) command).intValue();
					radio.setVolume(intValue);
				}
				break;
			case "MODE":
				final Integer mode = ((DecimalType) command).intValue();
				radio.setMode(mode);
				break;
			case "PRESET":
				final Integer preset = ((DecimalType) command).intValue();
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
				logger.error("command on unkown property: '" + property + "'. Maybe trying to set read-only property?");
				break;
			}
		}
	}

	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		deviceConfigCache.clear();
		if (config != null) {
			logger.debug("Configuration updated with " + config.size() + " keys");

			final Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				final String key = (String) keys.nextElement();
				final Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);
				if (!matcher.matches()) {
					if (!"service.pid".equals(key)) {
						logger.debug("given config key '"
								+ key
								+ "' does not follow the expected pattern '<id>.<host|port|pin|refreshInterval|cachePeriod>'");
					}
					continue;
				} else {
					logger.debug("matching config item found: " + key + " = " + config.get(key));
				}
				// regex: "^(.*?)\\.?(host|port|pin|refreshInterval|cachePeriod)$"
				final String deviceId = matcher.group(1);
				final String configKey = matcher.group(2);
				final String value = (String) config.get(key);

				if (deviceId == null || deviceId.trim().isEmpty()) {
					// general config
					if ("cachePeriod".equalsIgnoreCase(configKey)) {
						logger.debug("Cache period is " + value);
						cachePeriod = Integer.parseInt(value.trim());
					} else if ("refreshInterval".equalsIgnoreCase(configKey)) {
						logger.debug("Refresh interval is " + value);
						refreshInterval = Long.parseLong(value.trim());
					} else {
						logger.error("the given config key '" + configKey + "' is unknown");
					}
				} else {
					// device-specific config
					FrontierSiliconRadioBindingConfig deviceConfig = deviceConfigCache.get(deviceId);

					if (deviceConfig == null) {
						deviceConfig = new FrontierSiliconRadioBindingConfig(deviceId);
						deviceConfigCache.put(deviceId, deviceConfig);
					}

					if ("host".equalsIgnoreCase(configKey)) {
						logger.debug("Host name for " + deviceId + " is " + value);
						deviceConfig.host = value;
					} else if ("port".equalsIgnoreCase(configKey)) {
						logger.debug("Port number for " + deviceId + " is " + value);
						deviceConfig.port = Integer.valueOf(value.trim());
					} else if ("pin".equalsIgnoreCase(configKey)) {
						logger.debug("PIN for " + deviceId + " is " + value);
						deviceConfig.pin = value;
					} else {
						logger.error("the given config key '" + configKey + "' is unknown");
					}
				}
			}

			// open connection to radio
			for (String device : deviceConfigCache.keySet()) {
				final FrontierSiliconRadio radio = deviceConfigCache.get(device).getRadio();
				if (radio != null) {
					radio.login();
				}
			}
		}
		setProperlyConfigured(!deviceConfigCache.isEmpty());
	}

	/**
	 * Holds the binding configuration, consisting of: - deviceID (e.g. "sleepingroom") - host (e.g. "192.167.2.23" ) -
	 * portnumber (defaults to 80) - Access PIN (e.g. 1234)
	 */
	private class FrontierSiliconRadioBindingConfig {

		private final String deviceId; // the end point identifier, e.g. "RadioKitchen"
		private String host; // host name or ip
		private int port = DEFAULT_PORT; // TCP port number
		private String pin = DEFAULT_PIN;

		private FrontierSiliconRadio radio = null;

		public FrontierSiliconRadioBindingConfig(String deviceId) {
			this.deviceId = deviceId;
		}

		@Override
		public String toString() {
			return "Device [id=" + deviceId + ", host=" + host + ", port=" + port + ",  pin: " + pin + "]";
		}

		public FrontierSiliconRadio getRadio() {
			if (radio == null) {
				logger.debug("creating new connection to " + host + ":" + port);
				radio = new FrontierSiliconRadio(host, port, pin);
			}
			return radio;
		}
	}
}
