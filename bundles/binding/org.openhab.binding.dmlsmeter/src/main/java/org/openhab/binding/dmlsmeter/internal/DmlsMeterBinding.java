/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmlsmeter.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.dmlsmeter.DmlsMeterBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.*;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openmuc.j62056.DataSet;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Peter Kreutzer
 * @since 1.4.0
 */
public class DmlsMeterBinding extends
		AbstractActiveBinding<DmlsMeterBindingProvider> implements
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(DmlsMeterBinding.class);

    // configuration defaults for optional properties
	private static int DEFAULT_BAUD_RATE_CHANGE_DELAY = 0;
	private static boolean DEFAULT_ECHO_HANDLING= true;

	// regEx to validate a meter config <code>'^(.*?)\\.(serialPort|baudRateChangeDelay|echoHandling)$'</code>
	private final Pattern METER_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(serialPort|baudRateChangeDelay|echoHandling)$");

	/**
	 * the refresh interval which is used to poll values from the dmlsMeter
	 * server (optional, defaults to 1 Minute)
	 */
	private long refreshInterval = 60 * 1000; // in ms

	/** the serial port to use for connecting to the metering device */
	private static String serialPort;

	/**
	 * Delay of baud rate change in ms. Default is 0. USB to serial converters
	 * often require a delay of up to 250ms
	 */
	private static int baudRateChangeDelay = 0;

	/** Enable handling of echos caused by some optical tranceivers */
	private static boolean echoHandling = true;

    // configured meter devices - keyed by meter device name
	private Map<String, MeterDevice> meters = new HashMap<String, MeterDevice>();

	private DmlsMeterReader reader;

	public DmlsMeterBinding() {
	}

	public void activate() {

	}

	public void deactivate() {
		reader = null;
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
		return "dmlsMeter Refresh Service";
	}
	
	private final DmlsMeterReader getDmlsMeterReader(MeterDevice meter) {
		if (reader != null) {
			return reader;
		}
		
		if (System.getProperty("DmlsMeterSimulate") != null) {
			reader = new SimulateDmlsMeterReader();
		} else {
			reader = new DmlsMeterReaderImpl(meter.getSerialPort(), meter.getBaudRateChangeDelay(), meter.getEchoHandling());
		}
		return reader;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		
		for (Map.Entry<String, MeterDevice> entry : meters.entrySet()){
			MeterDevice meter = entry.getValue();
			
			Map<String, DataSet> dataSets = getDmlsMeterReader(meter).read();

			for (DmlsMeterBindingProvider provider : providers) {

				for (String itemName : provider.getItemNames()) {
					String obis = provider.getObis(itemName);
					if (obis != null && dataSets.containsKey(obis)) {
						DataSet dataSet = dataSets.get(obis);
						Class<? extends Item> itemType = provider.getItemType(itemName);
						if (itemType.isAssignableFrom(NumberItem.class)) {
							double value = Double.parseDouble(dataSet.getValue());
							eventPublisher.postUpdate(itemName, new DecimalType(value));
						}
						if (itemType.isAssignableFrom(StringItem.class)) {
							String value = dataSet.getValue();
							eventPublisher.postUpdate(itemName, new StringType(value));
						}
					}
				}
			}
			
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc
	 */
	@Override
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
		
        serialPort = null;
        baudRateChangeDelay = DEFAULT_BAUD_RATE_CHANGE_DELAY;
        echoHandling = DEFAULT_ECHO_HANDLING;
        
		meters.clear();
		
		if (config == null || config.isEmpty()) {
			logger.warn("Empty or null configuration. Ignoring.");            	
			return;
		}
		
		Enumeration<String> keys = config.keys();
        while (keys.hasMoreElements()) {
            
            String key = (String) keys.nextElement();
            
            // the config-key enumeration contains additional keys that we
            // don't want to process here ...
            if ("service.pid".equals(key) || "refresh".equals(key) ) {
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
			
            String meterName = meterMatcher.group(1);
			MeterDevice meterConfig = meters.get(meterName);
            
			if (meterConfig == null) {
				meterConfig = new MeterDevice("",baudRateChangeDelay,echoHandling);
				meters.put(meterName, meterConfig);
			}
			
			String configKey = meterMatcher.group(2);
			String value = (String) config.get(key);

			if ("serialPort".equals(configKey)) {
				meterConfig.setSerialPort(value);
			} else if ("baudRateChangeDelay".equals(configKey)) {
				meterConfig.setBaudRateChangeDelay(Integer.valueOf(value));
			} else if ("echoHandling".equals(configKey)) {
				meterConfig.setEchoHandling(Boolean.valueOf(value));
			} else {
				throw new ConfigurationException(configKey,
					"the given configKey '" + configKey + "' is unknown");
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

}
