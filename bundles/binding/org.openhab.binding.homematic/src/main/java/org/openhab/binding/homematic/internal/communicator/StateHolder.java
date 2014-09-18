/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.ProviderItemIterator.ProviderItemIteratorCallback;
import org.openhab.binding.homematic.internal.communicator.client.BaseHomematicClient.HmValueItemIteratorCallback;
import org.openhab.binding.homematic.internal.communicator.client.HomematicClientException;
import org.openhab.binding.homematic.internal.config.binding.DatapointConfig;
import org.openhab.binding.homematic.internal.config.binding.HomematicBindingConfig;
import org.openhab.binding.homematic.internal.converter.state.Converter;
import org.openhab.binding.homematic.internal.model.HmRssiInfo;
import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.openhab.core.items.Item;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which (re-)loads and holds the cached values from a Homematic server.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */

public class StateHolder {
	private static final Logger logger = LoggerFactory.getLogger(StateHolder.class);

	private HomematicContext context;

	private ExecutorService reloadExecutorPool;

	private boolean datapointReloadInProgress = false;
	private Map<HomematicBindingConfig, Object> refreshCache = new HashMap<HomematicBindingConfig, Object>();
	private Map<HomematicBindingConfig, HmValueItem> datapoints = new HashMap<HomematicBindingConfig, HmValueItem>();
	private Map<HomematicBindingConfig, HmValueItem> variables = new HashMap<HomematicBindingConfig, HmValueItem>();

	public StateHolder(HomematicContext context) {
		this.context = context;
	}

	/**
	 * If a datapoint reload is currently running, this returns true.
	 */
	public boolean isDatapointReloadInProgress() {
		return datapointReloadInProgress;
	}

	/**
	 * A datapoint reload takes some seconds, this method is called when a event
	 * receives from the Homematic server during the reload.
	 */
	public void addToRefreshCache(HomematicBindingConfig bindingConfig, Object value) {
		refreshCache.put(bindingConfig, value);
	}

	/**
	 * Returns the cached HmValueItem.
	 */
	public HmValueItem getState(HomematicBindingConfig bindingConfig) {
		HmValueItem hmValueItem = datapoints.get(bindingConfig);
		if (hmValueItem == null) {
			hmValueItem = variables.get(bindingConfig);
		}
		return hmValueItem;
	}

	/**
	 * Loads all datapoints from the Homematic server, only executed at startup.
	 */
	public void loadDatapoints() throws HomematicClientException {
		logger.info("Loading Homematic datapoints");
		context.getHomematicClient().iterateAllDatapoints(new HmValueItemIteratorCallback() {

			@Override
			public void iterate(HomematicBindingConfig bindingConfig, HmValueItem hmValueItem) {
				datapoints.put(bindingConfig, hmValueItem);
			}
		});
		logger.info("Finished loading {} Homematic datapoints", datapoints.size());
	}

	/**
	 * Reloads all datapoints from the Homematic server and publishes only
	 * changed values to the openHAB bus.
	 */
	public void reloadDatapoints() {
		reloadExecutorPool.execute(new Runnable() {

			@Override
			public void run() {

				try {
					logger.debug("Reloading Homematic server datapoints");
					datapointReloadInProgress = true;
					context.getHomematicClient().iterateAllDatapoints(new HmValueItemIteratorCallback() {
						@Override
						public void iterate(HomematicBindingConfig bindingConfig, HmValueItem hmValueItem) {
							if (!datapoints.containsKey(bindingConfig)) {
								logger.info("Adding new {}", bindingConfig);
								datapoints.put(bindingConfig, hmValueItem);
							} else {
								Object cachedValue = refreshCache.get(bindingConfig);
								if (cachedValue != null) {
									logger.debug("Value changed while refreshing from '{}' to '{}' for binding {}",
											hmValueItem.getValue(), cachedValue, bindingConfig);
									hmValueItem.setValue(cachedValue);
								}

								if (hasChanged(bindingConfig, datapoints.get(bindingConfig), hmValueItem)) {
									datapoints.put(bindingConfig, hmValueItem);
									publish(bindingConfig, hmValueItem);
								}
							}
						}
					});
					logger.debug("Finished reloading {} Homematic server datapoints", datapoints.size());
				} catch (HomematicClientException ex) {
					logger.error(ex.getMessage(), ex);
				} finally {
					datapointReloadInProgress = false;
					refreshCache.clear();
				}

			}
		});
	}

	/**
	 * Reloads all RSSI values from the Homematic server and publishes only
	 * changed values to the openHAB bus.
	 */
	public void reloadRssi() {
		reloadExecutorPool.execute(new Runnable() {

			@Override
			public void run() {
				try {
					logger.debug("Reloading Homematic server RSSI values");
					Map<String, HmRssiInfo> rssiList = context.getHomematicClient().getRssiInfo();
					for (String address : rssiList.keySet()) {
						HmRssiInfo rssiInfo = rssiList.get(address);
						updateRssiInfo(new DatapointConfig(address, "0", "RSSI_DEVICE"), rssiInfo.getDevice());
						updateRssiInfo(new DatapointConfig(address, "0", "RSSI_PEER"), rssiInfo.getPeer());
					}
					logger.debug("Finished reloading {} Homematic server RSSI values", rssiList.size());
				} catch (HomematicClientException ex) {
					logger.error(ex.getMessage(), ex);
				}
			}
		});
	}

	/**
	 * Upates the RSSI datapoint if available.
	 */
	private void updateRssiInfo(DatapointConfig bindingConfig, Integer rssiValue) {
		HmValueItem valueItem = datapoints.get(bindingConfig);
		if (valueItem != null) {
			if (!valueItem.getValue().equals(rssiValue)) {
				logger.debug("Value changed from '{}' to '{}' for binding {}",
						valueItem == null ? "null" : valueItem.getValue(), rssiValue, bindingConfig);
				valueItem.setValue(rssiValue);
				publish(bindingConfig, valueItem);
			}
		} else {
			logger.debug("Rssi info not found: {}", bindingConfig);
		}
	}

	/**
	 * Loads all variables from the Homematic server, only executed at startup.
	 */
	public void loadVariables() throws HomematicClientException {
		if (context.getHomematicClient().supportsVariables()) {
			logger.info("Loading Homematic Server variables");

			context.getHomematicClient().iterateAllVariables(new HmValueItemIteratorCallback() {

				@Override
				public void iterate(HomematicBindingConfig bindingConfig, HmValueItem variable) {
					variables.put(bindingConfig, variable);
				}
			});
			logger.info("Finished loading {} Homematic server variables", variables.size());
		}
	}

	/**
	 * Reloads all variables from the Homematic server and publishes only
	 * changed values to the openHAB bus.
	 */
	public void reloadVariables() {
		if (context.getHomematicClient().supportsVariables()) {

			reloadExecutorPool.execute(new Runnable() {

				@Override
				public void run() {
					logger.debug("Reloading Homematic server variables");

					try {
						context.getHomematicClient().iterateAllVariables(new HmValueItemIteratorCallback() {

							@Override
							public void iterate(HomematicBindingConfig bindingConfig, HmValueItem variable) {
								if (hasChanged(bindingConfig, variables.get(bindingConfig), variable)) {
									variables.put(bindingConfig, variable);
									publish(bindingConfig, variable);
								}
							}
						});
						logger.debug("Finished reloading {} Homematic server variables", variables.size());
					} catch (HomematicClientException ex) {
						logger.error(ex.getMessage(), ex);
					}
				}
			});
		}
	}

	/**
	 * Initializes the stateholder by creating a new executor pool.
	 */
	public void init() {
		reloadExecutorPool = Executors.newCachedThreadPool();
	}

	/**
	 * Destroys the cache.
	 */
	public void destroy() {
		datapointReloadInProgress = false;
		if (reloadExecutorPool != null) {
			reloadExecutorPool.shutdownNow();
			reloadExecutorPool = null;
		}
		datapoints.clear();
		variables.clear();
	}

	/**
	 * Returns true if the cached value is not up to date.
	 */
	protected boolean hasChanged(HomematicBindingConfig bindingConfig, HmValueItem parentHmValueItem,
			HmValueItem hmValueItem) {
		if (parentHmValueItem == null
				|| (parentHmValueItem != null && !parentHmValueItem.getValue().equals(hmValueItem.getValue()))) {
			logger.debug("Value changed from '{}' to '{}' for binding {}", parentHmValueItem == null ? "null"
					: parentHmValueItem.getValue(), hmValueItem.getValue(), bindingConfig);
			return true;
		}
		return false;
	}

	/**
	 * Iterate through all items and publishes the state.
	 */
	protected void publish(final HomematicBindingConfig bindingConfig, final HmValueItem hmValueItem) {
		new ProviderItemIterator().iterate(bindingConfig, new ProviderItemIteratorCallback() {

			@Override
			public void next(HomematicBindingConfig providerBindingConfig, Item item, Converter<?> converter) {
				State state = converter.convertFromBinding(hmValueItem);
				context.getEventPublisher().postUpdate(item.getName(), state);
			}
		});
	}
}
