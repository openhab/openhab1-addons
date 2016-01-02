/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sallegra.internal;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.sallegra.SallegraBindingProvider;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;

/**
 * Implement this class if you are going create an actively polling service like querying a Website/Device.
 * 
 * @author Benjamin Marty (Developed on behalf of Satelco.ch)
 * @since 1.8.0
 */
public class SallegraBinding extends AbstractActiveBinding<SallegraBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(SallegraBinding.class);

	private HashMap<String, SallegraNode> sallegraNodes = new HashMap<String, SallegraNode>();

	/**
	 * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate() method and must not
	 * be accessed anymore once the deactivate() method was called or before activate() was called.
	 */
	public BundleContext bundleContext;

	/**
	 * the refresh interval which is used to poll values from the Sallegra server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 1000;

	public SallegraBinding() {
	}

	/**
	 * {@inheritDoc}
	 */
	public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
		this.bundleContext = bundleContext;
		logger.debug("Binding started!");

		// read further config parameters here ...
		setProperlyConfigured(true);
	}

	/**
	 * {@inheritDoc}
	 */
	public void modified(final Map<String, Object> configuration) {
		// update the internal configuration accordingly
	}

	/**
	 * @{inheritDoc
	 */
	public void deactivate(final int reason) {
		this.bundleContext = null;
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
		return "Sallegra Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		for (SallegraBindingProvider provider : providers) {
			Collection<String> names = provider.getItemNames();
			for (String name : names) {
				logger.debug("Name {}", name);
				SallegraBindingConfig bindingConfig = provider.getBindingConfigFor(name);
				String deviceId = bindingConfig.getModuleName();

				/*
				 * check if a device with this id is already configured
				 */
				if (sallegraNodes.containsKey(deviceId)) {
					SallegraNode node = sallegraNodes.get(deviceId);

					/*
					 * yes, there is a device with this id, now update it
					 */
					String value = null;
					switch (bindingConfig.getCmdId()) {
					case RELAY:
						value = node.getRelay(bindingConfig.getCmdValue());
						break;
					case DIMMER:
						value = node.getDimmer(bindingConfig.getCmdValue());
						break;
					case INPUT:
						value = node.getInput(bindingConfig.getCmdValue());
						break;
					}
					// If a value is got returned update it in openhab
					if (value != null) {
						postUpdate(provider, bindingConfig, value);
					}
				} else {
					logger.error("Unknown deviceId \"{}\"", deviceId);
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
		logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);

		/*
		 * do we have a binding for this item at all?
		 */
		if (providesBindingFor(itemName)) {
			/*
			 * go through all the providers and look for a BindingConfig
			 */
			for (SallegraBindingProvider provider : providers) {
				SallegraBindingConfig bindingConfig = provider.getBindingConfigFor(itemName);

				if (bindingConfig != null) {
					/*
					 * Found one
					 */
					SallegraNode node = sallegraNodes.get(bindingConfig.getModuleName());
					if (node == null) {
						logger.error("Invalid deviceId {}", bindingConfig.getModuleName());
					}

					switch (bindingConfig.getCmdId()) {
					case RELAY:
						node.setRelay(command, bindingConfig.getCmdValue());
						break;
					case DIMMER:
						node.setDimmer(command, bindingConfig.getCmdValue());
						break;
					case INPUT:
						logger.error("You can't set anything on the Sallegra Input Module");
						break;
					default:
						logger.error("Unknown cmdId \"{}\"", bindingConfig.getCmdId());
					}
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
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {

			String refreshIntervalString = (String) config.get("refresh");

			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();

				// Escape of dot absolutely necessary
				String[] keyElements = key.split("\\.");

				String deviceId = keyElements[0];

				if (keyElements.length >= 2) {

					SallegraNode node = sallegraNodes.get(deviceId);
					if (node == null) {
						node = new SallegraNode();
						sallegraNodes.put(deviceId, node);
					}

					String option = keyElements[1];
					if (option.equals("password")) {
						node.setPassword((String) config.get(key));
					} else if (option.equals("hostname")) {
						node.setHostName((String) config.get(key));
					}
				}
			}

			setProperlyConfigured(checkProperlyConfigured());
		}
	}

	/**
	 * Used to update Status of Items
	 */
	private void postUpdate(SallegraBindingProvider provider, SallegraBindingConfig string, final String value) {

		switch (string.getCmdId()) {
		case RELAY:
			eventPublisher.postUpdate(string.getItem(), OnOffType.valueOf(value));
			break;
		case DIMMER:
			eventPublisher.postUpdate(string.getItem(), PercentType.valueOf(value));
			break;
		case INPUT:
			eventPublisher.postUpdate(string.getItem(), StringType.valueOf(value));
			break;
		}
	}

	private boolean checkProperlyConfigured() {
		for (SallegraNode node : this.sallegraNodes.values()) {
			if (!node.properlyConfigured()) {
				return false;
			}
		}
		return true;
	}

}
