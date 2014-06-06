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
import org.openhab.binding.homematic.internal.communicator.client.CcuClientException;
import org.openhab.binding.homematic.internal.communicator.client.TclRegaScriptClient.TclIteratorCallback;
import org.openhab.binding.homematic.internal.config.binding.HomematicBindingConfig;
import org.openhab.binding.homematic.internal.converter.state.Converter;
import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.openhab.core.items.Item;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class which (re-)loads and holds the cached values from the CCU.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */

public class CcuStateHolder {
	private static final Logger logger = LoggerFactory.getLogger(CcuStateHolder.class);

	private HomematicContext context;

	private ExecutorService reloadExecutorPool;

	private boolean datapointReloadInProgress = false;
	private Map<HomematicBindingConfig, Object> refreshCache = new HashMap<HomematicBindingConfig, Object>();
	private Map<HomematicBindingConfig, HmValueItem> ccuDatapoints = new HashMap<HomematicBindingConfig, HmValueItem>();
	private Map<HomematicBindingConfig, HmValueItem> ccuVariables = new HashMap<HomematicBindingConfig, HmValueItem>();

	public CcuStateHolder(HomematicContext context) {
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
	 * receives from the CCU during the reload.
	 */
	public void addToRefreshCache(HomematicBindingConfig bindingConfig, Object value) {
		refreshCache.put(bindingConfig, value);
	}

	/**
	 * Returns the cached HmValueItem.
	 */
	public HmValueItem getState(HomematicBindingConfig bindingConfig) {
		HmValueItem hmValueItem = ccuDatapoints.get(bindingConfig);
		if (hmValueItem == null) {
			hmValueItem = ccuVariables.get(bindingConfig);
		}
		return hmValueItem;
	}

	/**
	 * Loads all datapoints from the CCU, only executed at startup.
	 */
	public void loadDatapoints() throws CcuClientException {
		logger.info("Loading CCU datapoints");

		context.getTclRegaScriptClient().iterateAllDatapoints(new TclIteratorCallback() {

			@Override
			public void iterate(HomematicBindingConfig bindingConfig, HmValueItem hmValueItem) {
				ccuDatapoints.put(bindingConfig, hmValueItem);
			}
		});
		logger.info("Finished loading {} CCU datapoints", ccuDatapoints.size());
	}

	/**
	 * Reloads all datapoints from the CCU and publishes only changed values to
	 * the openHAB bus.
	 */
	public void reloadDatapoints() {
		reloadExecutorPool.execute(new Runnable() {

			@Override
			public void run() {

				try {
					logger.debug("Reloading CCU datapoints");
					datapointReloadInProgress = true;
					context.getTclRegaScriptClient().iterateAllDatapoints(new TclIteratorCallback() {
						@Override
						public void iterate(HomematicBindingConfig bindingConfig, HmValueItem hmValueItem) {
							if (!ccuDatapoints.containsKey(bindingConfig)) {
								logger.info("Adding new {}", bindingConfig);
								ccuDatapoints.put(bindingConfig, hmValueItem);
							} else {
								Object cachedValue = refreshCache.get(bindingConfig);
								if (cachedValue != null) {
									logger.debug("Value changed while refreshing from '{}' to '{}' for binding {}",
											hmValueItem.getValue(), cachedValue, bindingConfig);
									hmValueItem.setValue(cachedValue);
								}

								if (hasChanged(bindingConfig, ccuDatapoints.get(bindingConfig), hmValueItem)) {
									ccuDatapoints.put(bindingConfig, hmValueItem);
									publish(bindingConfig, hmValueItem);
								}
							}
						}
					});
					logger.debug("Finished reloading {} CCU datapoints", ccuDatapoints.size());
				} catch (CcuClientException ex) {
					logger.error(ex.getMessage(), ex);
				} finally {
					datapointReloadInProgress = false;
					refreshCache.clear();
				}

			}
		});
	}

	/**
	 * Loads all variables from the CCU, only executed at startup.
	 */
	public void loadVariables() throws CcuClientException {
		logger.info("Loading CCU variables");

		context.getTclRegaScriptClient().iterateAllVariables(new TclIteratorCallback() {

			@Override
			public void iterate(HomematicBindingConfig bindingConfig, HmValueItem variable) {
				ccuVariables.put(bindingConfig, variable);
			}
		});
		logger.info("Finished loading {} CCU variables", ccuVariables.size());
	}

	/**
	 * Reloads all variables from the CCU and publishes only changed values to
	 * the openHAB bus.
	 */
	public void reloadVariables() {
		reloadExecutorPool.execute(new Runnable() {

			@Override
			public void run() {
				logger.debug("Reloading CCU variables");

				try {
					context.getTclRegaScriptClient().iterateAllVariables(new TclIteratorCallback() {

						@Override
						public void iterate(HomematicBindingConfig bindingConfig, HmValueItem variable) {
							if (hasChanged(bindingConfig, ccuVariables.get(bindingConfig), variable)) {
								ccuVariables.put(bindingConfig, variable);
								publish(bindingConfig, variable);
							}
						}
					});
					logger.debug("Finished reloading {} CCU variables", ccuVariables.size());
				} catch (CcuClientException ex) {
					logger.error(ex.getMessage(), ex);
				}
			}
		});
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
		ccuDatapoints.clear();
		ccuVariables.clear();
		reloadExecutorPool.shutdownNow();
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
