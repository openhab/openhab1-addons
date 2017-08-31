/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fatekplc.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.fatekplc.FatekPLCBindingProvider;
import org.openhab.binding.fatekplc.items.FatekPLCItem;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.simplify4u.jfatek.io.FatekIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding implementation for Fatek PLC.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public class FatekPLCBinding extends AbstractActiveBinding<FatekPLCBindingProvider> implements ManagedService {

	/**
	 * Main logger.
	 */
	private static final Logger logger = LoggerFactory.getLogger(FatekPLCBinding.class);

	/**
	 * Globals config keys name
	 */
	private static final List<String> GLOBAL_CONFIG_NAMES = Arrays.asList("refresh", "service.pid");

	/**
	 * the refresh interval which is used to poll values from the Fatek PLC server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	/**
	 * Map of slave device.
	 */
	private Map<String, FatekPLCSlave> slaves = new HashMap<>();

	public FatekPLCBinding() {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Fatek PLC Refresh Service";
	}

	/**
	 * Called by the SCR to deactivate the component when either the configuration is removed or mandatory references
	 * are no longer satisfied or the component has simply been stopped.
	 *
	 * @param reason
	 *            Reason code for the deactivation:<br>
	 *            <ul>
	 *            <li>0 Unspecified
	 *            <li>1 The component was disabled
	 *            <li>2 A reference became unsatisfied
	 *            <li>3 A configuration was changed
	 *            <li>4 A configuration was deleted
	 *            <li>5 The component was disposed
	 *            <li>6 The bundle was stopped
	 *            </ul>
	 */
	public void deactivate(final int reason) {

		setProperlyConfigured(false);
		logger.debug("deactivate executed reason={}", reason);
	}

	@Override
	public void updated(Dictionary<String, ?> properties) throws ConfigurationException {

		logger.debug("update start");
		try {
			// stop and clear current slaves
			for (FatekPLCSlave slave : slaves.values()) {
				try {
					slave.disconnect();
				} catch (FatekIOException e) {
					logger.warn("diconnect", e);
				}
			}
			slaves.clear();

			// global configurations
			refreshInterval = getLongProperty(properties, "refresh", refreshInterval);

			// slaves configurations
			Enumeration<String> keys = properties.keys();
			while (keys.hasMoreElements()) {

				String key = keys.nextElement();
				// skip global configs item
				if (GLOBAL_CONFIG_NAMES.contains(key)) {
					continue;
				}

				String keyItems[] = key.split("\\.");
				if (keyItems.length != 2) {
					throw new ConfigurationException(key, "incorrect format, should be slave.property");
				}

				FatekPLCSlave slave = slaves.get(keyItems[0]);
				if (slave == null) {
					slave = new FatekPLCSlave(keyItems[0], eventPublisher);
					slaves.put(slave.getName(), slave);
				}

				slave.configure(keyItems[1], properties.get(key));
			}

		} catch (ConfigurationException e) {
			logger.error("", e);
			throw e;
		}

		setProperlyConfigured(true);
		logger.info("Configuration has been updated, refresh={}", refreshInterval);
	}

	/**
	 * Parse long from property with default.
	 *
	 * @param properties
	 *            properties map
	 * @param key
	 *            given key
	 * @param defaultVale
	 * @return long value for given key
	 */
	private long getLongProperty(Dictionary<String, ?> properties, String key, long defaultVale) {

		String value = (String) properties.get(key);
		long ret;
		try {
			ret = Long.parseLong(value);
		} catch (NumberFormatException e) {
			ret = defaultVale;
			logger.warn("Property {} parse exception {}", key, e.getMessage());
		}

		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void execute() {

		Map<String, List<FatekPLCItem>> toUpdate = new HashMap<>();

		// collect items by slave PLC
		for (FatekPLCBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {

				FatekPLCItem item = provider.geFatektItem(itemName);

				if (item.isToRefresh()) {

					List<FatekPLCItem> itemsList = toUpdate.get(item.getSlaveName());
					if (itemsList == null) {
						itemsList = new ArrayList<>();
						toUpdate.put(item.getSlaveName(), itemsList);
					}

					itemsList.add(item);
				}
			}
		}

		logger.debug("toUpdate={}", toUpdate);

		// process list to update
		for (String slaveName : toUpdate.keySet()) {

			FatekPLCSlave slave = slaves.get(slaveName);
			List<FatekPLCItem> items = toUpdate.get(slaveName);

			if (slave != null) {
				try {
					slave.updateItems(items);
				} catch (Exception e) {
					logger.error("update items error", e);
				}
			} else {
				logger.warn("Unknown slave: {} to process update: {}", slaveName, items);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);

		for (FatekPLCBindingProvider provider : providers) {
			FatekPLCItem item = provider.geFatektItem(itemName);
			if (item != null) {
				FatekPLCSlave slave = slaves.get(item.getSlaveName());
				if (slave != null) {
					try {
						slave.command(item, command);
					} catch (Exception e) {
						logger.error(String.format("internalReceiveCommand(%s, %s)", itemName, command), e);
					}
				}
			}
		}
	}

	protected void addBindingProvider(FatekPLCBindingProvider bindingProvider) {
		super.addBindingProvider(bindingProvider);
	}

	protected void removeBindingProvider(FatekPLCBindingProvider bindingProvider) {
		super.removeBindingProvider(bindingProvider);
	}
}
