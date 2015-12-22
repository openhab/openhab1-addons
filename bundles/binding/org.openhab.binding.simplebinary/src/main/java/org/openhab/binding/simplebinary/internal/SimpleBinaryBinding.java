/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.simplebinary.SimpleBinaryBindingProvider;
import org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.SimpleBinaryBindingConfig;
import org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.SimpleBinaryInfoBindingConfig;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like querying a Website/Device.
 * 
 * @author Vita Tucek
 * @since 1.8.0
 */
public class SimpleBinaryBinding extends AbstractActiveBinding<SimpleBinaryBindingProvider> {

	private static final Logger logger = LoggerFactory.getLogger(SimpleBinaryBinding.class);

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate() method and must not
	 * be accessed anymore once the deactivate() method was called or before activate() was called.
	 */
	@SuppressWarnings("unused")
	private BundleContext bundleContext;

	/**
	 * the refresh interval which is used to poll values from the SimpleBinary server (optional, defaults to 10000ms)
	 */
	private long refreshInterval = 10000;

	// devices
	private Map<String, SimpleBinaryUART> devices = new HashMap<String, SimpleBinaryUART>();
	// data item configs
	private Map<String, SimpleBinaryBindingConfig> items = new HashMap<String, SimpleBinaryBindingConfig>();
	// info item configs
	private Map<String, SimpleBinaryInfoBindingConfig> infoItems = new HashMap<String, SimpleBinaryInfoBindingConfig>();

	public SimpleBinaryBinding() {
	}

	/**
	 * Called by the SCR to activate the component with its configuration read from CAS
	 * 
	 * @param bundleContext
	 *            BundleContext of the Bundle that defines this component
	 * @param configuration
	 *            Configuration properties for this component obtained from the ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;

		logger.debug("activate() method is called!");
		logger.debug(bundleContext.toString());

		// the configuration is guaranteed not to be null, because the component definition has the
		// configuration-policy set to require. If set to 'optional' then the configuration may be null

		// to override the default refresh interval one has to add a
		// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		devices.clear();
		// items.clear();

		Pattern rgxKey = Pattern.compile("^port\\d*$");
		Pattern rgxValue = Pattern.compile("^(\\S+:\\d+)(;((onscan)|(onchange)))?(;((forceRTS)|(forceRTSInv)))?$");

		for (Map.Entry<String, Object> item : configuration.entrySet()) {
			// logger.debug("key:" + item.getKey() + "/value:" + item.getValue());

			// port
			if (rgxKey.matcher(item.getKey()).matches()) {
				String portString = (String) item.getValue();
				if (StringUtils.isNotBlank(portString)) {
					logger.debug("port: " + portString);

					Matcher matcher = rgxValue.matcher(portString);

					if (!matcher.matches()) {
						logger.error("{}: Wrong port configuration: {}", item.getKey(), portString);
						logger.info("Port configuration example: port=COM1:9600 or port=/dev/ttyS1:9600;onchange;forceRTS");
						setProperlyConfigured(false);
						return;
					}

					SimpleBinaryPoolControl simpleBinaryPoolControl = SimpleBinaryPoolControl.ONCHANGE;
					int speed = 9600;
					boolean forceRTS = false;
					boolean RTSInversion = false;

					// check group 3
					if (matcher.group(3) != null) {

						if (matcher.group(3).equals("onscan")) {
							simpleBinaryPoolControl = SimpleBinaryPoolControl.ONSCAN;
						}
					}
					// check group 7
					if (matcher.group(7) != null) {
						forceRTS = true;

						if (matcher.group(9) != null) {
							RTSInversion = true;
						}
					}

					if (matcher.group(1).contains(":")) {
						String[] s = matcher.group(1).split(":");

						portString = s[0];
						speed = Integer.valueOf(s[1]);
					} else
						portString = matcher.group(1);

					logger.info("SimpleBinary port={} speed={} mode={} forcerts={} rtsInversion={}", portString, speed, simpleBinaryPoolControl, forceRTS, RTSInversion);

					devices.put(item.getKey(), new SimpleBinaryUART(item.getKey(), portString, speed, simpleBinaryPoolControl, forceRTS, RTSInversion));
				} else {
					logger.error("Blank port configuration");
					setProperlyConfigured(false);
					return;
				}
			}
		}

		logger.debug("setProperlyConfigured ");

		setProperlyConfigured(true);

		for (Map.Entry<String, SimpleBinaryUART> item : devices.entrySet()) {
			item.getValue().setBindingData(eventPublisher, items, infoItems);
			item.getValue().open();
		}
	}

	/**
	 * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
	 * 
	 * @param configuration
	 *            Updated configuration properties
	 */
	public void modified(final Map<String, Object> configuration) {
		// update the internal configuration accordingly

		logger.debug("modified() method is called!");
	}

	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or mandatory references
	 * are no longer satisfied or the component has simply been stopped.
	 * 
	 * @param reason
	 *            Reason code for the deactivation:<br>
	 *            <ul>
	 *            <li>0 – Unspecified
	 *            <li>1 – The component was disabled
	 *            <li>2 – A reference became unsatisfied
	 *            <li>3 – A configuration was changed
	 *            <li>4 – A configuration was deleted
	 *            <li>5 – The component was disposed
	 *            <li>6 – The bundle was stopped
	 *            </ul>
	 */
	public void deactivate(final int reason) {
		this.bundleContext = null;
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again

		logger.debug("deactivate() method is called!");

		for (Map.Entry<String, SimpleBinaryUART> item : devices.entrySet()) {
			item.getValue().unsetBindingData();
			item.getValue().close();
		}

		devices.clear();
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
		return "SimpleBinary communication service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");

		if (devices != null && devices.size() > 0) {
			for (Map.Entry<String, SimpleBinaryUART> item : devices.entrySet()) {
				item.getValue().checkNewData();
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
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);

		SimpleBinaryBindingConfig config = items.get(itemName);

		if (config != null) {
			SimpleBinaryUART device = devices.get(config.device);

			if (device != null)
				try {
					device.sendData(itemName, command, config);
				} catch (Exception ex) {
					logger.error("internalReceiveCommand(): line:" + ex.getStackTrace()[0].getLineNumber() + "|method:" + ex.getStackTrace()[0].getMethodName());
				}
			else
				logger.warn("No device for item: {}", itemName);
		} else {
			logger.warn("No config for item: {}", itemName);
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}

	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		super.bindingChanged(provider, itemName);

		logger.debug("bindingChanged({},{}) is called!", provider, itemName);

		BindingConfig config = ((SimpleBinaryGenericBindingProvider) provider).getItemConfig(itemName);

		if (config instanceof SimpleBinaryBindingConfig) {
			if (items.get(itemName) != null)
				items.remove(itemName);

			if (config != null)
				items.put(itemName, (SimpleBinaryBindingConfig) config);

		} else if (config instanceof SimpleBinaryInfoBindingConfig) {
			if (infoItems.get(itemName) != null)
				infoItems.remove(itemName);

			if (config != null)
				infoItems.put(itemName, (SimpleBinaryInfoBindingConfig) config);
		}

		logger.debug("ItemsConfig: {}:{}", items, items.entrySet().size());
	}

	@Override
	public void allBindingsChanged(BindingProvider provider) {
		super.allBindingsChanged(provider);

		items.clear();
		infoItems.clear();

		for (Entry<String, BindingConfig> item : ((SimpleBinaryGenericBindingProvider) provider).configs().entrySet()) {
			if (item.getValue() instanceof SimpleBinaryBindingConfig) {
				items.put(item.getKey(), (SimpleBinaryBindingConfig) item.getValue());
			} else if (item.getValue() instanceof SimpleBinaryInfoBindingConfig) {
				infoItems.put(item.getKey(), (SimpleBinaryInfoBindingConfig) item.getValue());
			}
		}

		logger.debug("allBindingsChanged({}) is called!", provider);
	}
}
