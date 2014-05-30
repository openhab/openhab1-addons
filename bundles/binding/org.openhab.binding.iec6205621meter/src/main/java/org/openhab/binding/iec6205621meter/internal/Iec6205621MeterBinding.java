/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.iec6205621meter.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.iec6205621meter.Iec6205621MeterBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openmuc.j62056.DataSet;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * iec 62056-21 meter binding implementation
 * 
 * @author Peter Kreutzer
 * @author Günter Speckhofer
 * @since 1.5.0
 */
public class Iec6205621MeterBinding extends
		AbstractActiveBinding<Iec6205621MeterBindingProvider> implements
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(Iec6205621MeterBinding.class);

	// regEx to validate a meter config
	// <code>'^(.*?)\\.(serialPort|baudRateChangeDelay|echoHandling)$'</code>
	private final Pattern METER_CONFIG_PATTERN = Pattern
			.compile("^(.*?)\\.(serialPort|baudRateChangeDelay|echoHandling)$");

	private static final long DEFAULT_REFRESH_INTERVAL = 60000;

	/**
	 * the refresh interval which is used to poll values from the IEC 62056-21 Meter
	 * server (optional, defaults to 10 minutes)
	 */
	private long refreshInterval = DEFAULT_REFRESH_INTERVAL;

	// configured meter devices - keyed by meter device name
	private final Map<String, Meter> meterDeviceConfigurtions = new HashMap<String, Meter>();

	public Iec6205621MeterBinding() {
	}

	public void activate() {

	}

	public void deactivate() {
		meterDeviceConfigurtions.clear();
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
		return "iec6205621meter Refresh Service";
	}

	private final Meter createIec6205621MeterConfig(String name,
			MeterConfig config) {

		Meter reader = null;
		reader = new Meter(name, config);
		return reader;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...

		for (Entry<String, Meter> entry : meterDeviceConfigurtions.entrySet()) {
			Meter reader = entry.getValue();

			Map<String, DataSet> dataSets = reader.read();

			for (Iec6205621MeterBindingProvider provider : providers) {

				for (String itemName : provider.getItemNames()) {
					String obis = provider.getObis(itemName);
					if (obis != null && dataSets.containsKey(obis)) {
						DataSet dataSet = dataSets.get(obis);
						Class<? extends Item> itemType = provider
								.getItemType(itemName);
						if (itemType.isAssignableFrom(NumberItem.class)) {
							eventPublisher.postUpdate(itemName,
									new DecimalType(dataSet.getValue()));
						}
						if (itemType.isAssignableFrom(StringItem.class)) {
							String value = dataSet.getValue();
							eventPublisher.postUpdate(itemName, new StringType(
									value));
						}
					}
				}
			}

		}
	}

	/**
	 * @{inheritDoc
	 */
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {

		if (config == null || config.isEmpty()) {
			logger.warn("Empty or null configuration. Ignoring.");
			return;
		}

		Set<String> names = getNames(config);

		for (String name : names) {

			String value = (String) config.get(name + ".serialPort");
			String serialPort = value != null ? value
					: MeterConfig.DEFAULT_SERIAL_PORT;

			value = (String) config.get(name + ".baudRateChangeDelay");
			int baudRateChangeDelay = value != null ? Integer.valueOf(value)
					: MeterConfig.DEFAULT_BAUD_RATE_CHANGE_DELAY;

			value = (String) config.get(name + ".echoHandling");
			boolean echoHandling = value != null ? Boolean.valueOf(value)
					: MeterConfig.DEFAULT_ECHO_HANDLING;

			Meter meterConfig = createIec6205621MeterConfig(name,
					new MeterConfig(serialPort, baudRateChangeDelay,
							echoHandling));

			if (meterDeviceConfigurtions.put(meterConfig.getName(), meterConfig) != null) {
				logger.info("Recreated reader {} with  {}!", meterConfig.getName(),
						meterConfig.getConfig());
			} else {
				logger.info("Created reader {} with  {}!", meterConfig.getName(),
						meterConfig.getConfig());
			}
		}

		if (config != null) {
			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			if (StringUtils.isNotBlank((String) config.get("refresh"))) {
				refreshInterval = Long
						.parseLong((String) config.get("refresh"));
			}
			setProperlyConfigured(true);
		}
	}

	/**
	 * Analyze configuration to get meter names
	 * 
	 * @return set of String of meter names
	 */
	private Set<String> getNames(Dictionary<String, ?> config) {
		Set<String> set = new HashSet<String>();

		Enumeration<String> keys = config.keys();
		while (keys.hasMoreElements()) {

			String key = (String) keys.nextElement();

			// the config-key enumeration contains additional keys that we
			// don't want to process here ...
			if ("service.pid".equals(key) || "refresh".equals(key)) {
				continue;
			}

			Matcher meterMatcher = METER_CONFIG_PATTERN.matcher(key);

			if (!meterMatcher.matches()) {
				logger.debug("given config key '"
						+ key
						+ "' does not follow the expected pattern '<meterName>.<serialPort|baudRateChangeDelay|echoHandling>'");
				continue;
			}

			meterMatcher.reset();
			meterMatcher.find();

			set.add(meterMatcher.group(1));
		}
		return set;
	}

}
