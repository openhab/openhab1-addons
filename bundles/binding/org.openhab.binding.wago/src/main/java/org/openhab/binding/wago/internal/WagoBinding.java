/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wago.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.wago.WagoBindingProvider;
import org.openhab.binding.wago.internal.WagoGenericBindingProvider.WagoBindingConfig;

import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
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
 * @author Kaltofen
 * @since 1.7.0
 */
public class WagoBinding extends AbstractActiveBinding<WagoBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(WagoBinding.class);

	private static final Pattern EXTRACT_CONFIG_WAGO_PATTERN = Pattern
			.compile("^(.*?)\\.(ip|modbus|ftp|username|password)$");

	/**
	 * the refresh interval which is used to poll values from the wago server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	private static Map<String, FBCoupler> couplers = 
		Collections.synchronizedMap(new HashMap<String, FBCoupler>());

	private FBCoupler getCoupler(String couplerName) {
		return Collections.synchronizedMap(couplers).get(couplerName);
	}

	public WagoBinding() {
	}

	public void activate() {
	}

	public void deactivate() {
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
		return "Wago Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		for (FBCoupler coupler : Collections.synchronizedMap(couplers).values()) {
			coupler.update(this);
		}
	}

	public void updateItem(String itemName, String couplerName, int module,
			boolean states[]) {
		for (WagoBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				WagoBindingConfig conf = provider.getConfig(itemName);
				if (conf.couplerName.equals(couplerName)
						&& conf.module == module) {
					State currentState = conf.getItemState();
					State newState = conf
							.translateBoolean2State(states[conf.channel]);
					if (!newState.equals(currentState)) {
						eventPublisher.postUpdate(itemName, newState);
					}
				}
			}
		}
	}

	public void updateItemPWM(String itemName, String couplerName, int module, int values[]) {
		for (WagoBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				WagoBindingConfig conf = provider.getConfig(itemName);
				if (conf.couplerName.equals(couplerName)
						&& conf.module == module) {
					State currentState = conf.getItemState();
					State newState;
					if (conf.getItem() instanceof DimmerItem) {
						newState = new PercentType(
								(int) ((float) values[conf.channel] / 1023 * 100));
					} else if (conf.getItem() instanceof SwitchItem) {
						if (values[conf.channel] == 0) {
							newState = OnOffType.OFF;
						} else {
							newState = OnOffType.ON;
						}
					} else {
						logger.debug("Unsupported Itemtype");
						return;
					}
					if (!newState.equals(currentState)) {
						eventPublisher.postUpdate(itemName, newState);
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
		for (WagoBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				WagoBindingConfig conf = provider.getConfig(itemName);
				FBCoupler coupler = getCoupler(conf.couplerName);
				coupler.executeCommand(command, conf);
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {
			Enumeration keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				if ("service.pid".equals(key)) {
					continue;
				}

				Matcher matcher = EXTRACT_CONFIG_WAGO_PATTERN.matcher(key);
				if (!matcher.matches()) {
					if ("refresh".equals(key)) {
						refreshInterval = Integer.valueOf((String) config
								.get(key));
					} else {
						logger.debug("unexpected configuration given. \"" + key
								+ "\" does not follow the expected pattern.");
					}
					continue;
				}

				matcher.reset();
				matcher.find();

				String couplerName = matcher.group(1);
				FBCoupler coupler = Collections.synchronizedMap(couplers).get(
						couplerName);
				if (coupler == null) {
					coupler = new FBCoupler(couplerName);
					Collections.synchronizedMap(couplers).put(couplerName,
							coupler);
				}

				String attrName = matcher.group(2);
				String value = (String) config.get(key);
				if (attrName.equals("ip"))
					coupler.setIp(value);
				else if (attrName.equals("modbus"))
					coupler.setModbus(Integer.parseInt(value));
				else if (attrName.equals("ftp"))
					coupler.setFTP(Integer.parseInt(value));
				else if (attrName.equals("username"))
					coupler.setUsername(value);
				else if (attrName.equals("password"))
					coupler.setPassword(value);
			}

			for (FBCoupler coupler : Collections.synchronizedMap(couplers)
					.values()) {
				coupler.setup();
			}

			setProperlyConfigured(true);
		}
	}

	public Collection<String> getItemNames() {
		Collection<String> items = null;
		for (BindingProvider provider : providers) {
			if (items == null)
				items = provider.getItemNames();
			else
				items.addAll(provider.getItemNames());
		}
		return items;
	}
}
