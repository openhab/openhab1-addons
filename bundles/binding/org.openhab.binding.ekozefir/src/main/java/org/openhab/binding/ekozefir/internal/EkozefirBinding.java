/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.internal;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.ekozefir.AhuCommandCreators;
import org.openhab.binding.ekozefir.EkozefirBindingProvider;
import org.openhab.binding.ekozefir.ResponseListenerCreators;
import org.openhab.binding.ekozefir.ahucommand.AhuCommand;
import org.openhab.binding.ekozefir.ahucommand.refreshparameters.RefreshParametersAhuCommand;
import org.openhab.binding.ekozefir.response.ResponseListener;
import org.openhab.binding.ekozefir.response.ResponseListenerCreator;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * Binding for Ekozefir air handling unit(ahu).
 * 
 * @author Michal Marasz
 * @since 1.6.0
 */
public class EkozefirBinding extends AbstractActiveBinding<EkozefirBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(EkozefirBinding.class);
	private long refreshInterval = 600;
	private final Map<String, ResponseListener> usedResponseListener = Maps.newHashMap();
	private final EkozefirTrunk trunk = new EkozefirTrunk();
	private final Map<String, Character> serviced = Maps.newHashMap();
	private AhuCommandCreators availableAhuCommandCreators;
	private ResponseListenerCreators availableResponseListenerCreators;
	private EventPublisher publisher;

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		super.bindingChanged(provider, itemName);
		EkozefirBindingConfig config = ((EkozefirBindingProvider) provider).getConfig(itemName);
		if (config == null) {
			removeConfig(itemName);
		} else {
			addConfig(config, itemName);
		}
	}

	private void removeConfig(String itemName) {
		Character ahuIdentifier = serviced.remove(itemName);
		trunk.unregister(ahuIdentifier, usedResponseListener.remove(itemName));
		logger.debug("Item name removed: {}", itemName);
	}

	private void addConfig(EkozefirBindingConfig config, String itemName) {
		Character driverIdentifier = config.getAhuIdentifier();
		String type = config.getType();
		Optional<ResponseListenerCreator> creator = availableResponseListenerCreators.getOfType(type);
		if (creator.isPresent()) {
			ResponseListener listener = creator.get().create(itemName, publisher);
			trunk.register(driverIdentifier, listener);
			usedResponseListener.put(itemName, listener);
			serviced.put(itemName, config.getAhuIdentifier());
		}
	}

	public void activate() {
		logger.debug("Ekozefir is active");
		trunk.connectAll();
	}

	public void deactivate() {
		logger.debug("Ekozefir is deactivate");
		trunk.disconnectAll();
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Ekozefir Refresh Service";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		logger.debug("Execute binding, serviced: {}", serviced);
		for (EkozefirBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				if (!serviced.containsKey(itemName)) {
					addConfig(provider.getConfig(itemName), itemName);
				}
			}
		}
		trunk.postforAllBus(new RefreshParametersAhuCommand());
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("internalReceiveCommand() is called!");
		Objects.requireNonNull(itemName);
		Objects.requireNonNull(command);
		for (EkozefirBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				postCommandToAhu(provider.getConfig(itemName), itemName, command);
			}
		}
	}

	private void postCommandToAhu(EkozefirBindingConfig config, String itemName, Command command) {
		Objects.requireNonNull(config);
		AhuCommand eventToSend = availableAhuCommandCreators.getOfType(config.getType()).get().create(command);
		trunk.post(config.getAhuIdentifier(), eventToSend);
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		logger.debug("internalReceiveCommand() is called!");
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		Objects.requireNonNull(config);
		for (String configKey : Collections.list(config.keys())) {
			parseConfig(clearAndCheckConfig(configKey, (String) config.get(configKey)));
		}
		setProperlyConfigured(true);
	}

	private Map.Entry<String, String> clearAndCheckConfig(String key, String value) throws ConfigurationException {
		if (StringUtils.isBlank(key)) {
			throw new ConfigurationException(key, "Blank key");
		}
		if (StringUtils.isBlank(value)) {
			throw new ConfigurationException(value, "Blank value");
		}
		return Maps.immutableEntry(StringUtils.trim(key), StringUtils.trim(value));
	}

	private void parseConfig(Map.Entry<String, String> config) throws ConfigurationException {
		String key = config.getKey();
		String value = config.getValue();
		Config parsedConfig = Config.parse(key);
		switch (parsedConfig) {
		case DRIVER:
			parseDriverConfig(value);
			break;
		case REFRESH:
			parseRefreshConfig(value);
			break;
		default:
			break;
		}
	}

	private void parseRefreshConfig(String value) {
		if (StringUtils.isNumeric(value)) {
			refreshInterval = Long.parseLong(value);
		}
	}

	private void parseDriverConfig(String driverName) {
		trunk.createAndConnectDriverBus(driverName);
	}

	public void unsetEventPublisher(EventPublisher publisher) {
		this.publisher = null;
	}

	public void setEventPublisher(EventPublisher publisher) {
		this.publisher = publisher;
	}

	public void setAhuCommandCreators(AhuCommandCreators config) {
		this.availableAhuCommandCreators = config;
	}

	public void unsetAhuCommandCreators(AhuCommandCreators config) {
		this.availableAhuCommandCreators = null;
	}

	public void setResponseListenerCreators(ResponseListenerCreators config) {
		this.availableResponseListenerCreators = config;
	}

	public void unsetResponseListenerCreators(ResponseListenerCreators config) {
		this.availableResponseListenerCreators = null;
	}

	private enum Config {
		DRIVER("driver"), REFRESH("refresh"), OTHER("other");
		private final String name;

		private Config(String name) {
			this.name = name;
		}

		public static Config parse(String key) {
			for (Config config : values()) {
				if (config.name.equals(key)) {
					return config;
				}
			}
			return OTHER;
		}
	}
}
