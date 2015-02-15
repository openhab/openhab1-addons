/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mpower.internal;

import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.mpower.MpowerBindingProvider;
import org.openhab.binding.mpower.internal.connector.MpowerSSHConnector;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.persistence.FilterCriteria.Ordering;
import org.openhab.core.persistence.extensions.PersistenceExtensions;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ubiquiti mPower strip binding
 * 
 * @author magcode
 */
public class MpowerBinding extends AbstractBinding<MpowerBindingProvider>
		implements ManagedService {
	private QueryablePersistenceService persistenceService;
	private ServiceReference persistenceServiceReference;
	private static final Logger logger = LoggerFactory
			.getLogger(MpowerBinding.class);

	private static final String CONFIG_USERNAME = "user";
	private static final String CONFIG_HOST = "host";
	private static final String CONFIG_PASSWORD = "password";
	private static final String CONFIG_REFRESH = "refresh";
	private Map<String, MpowerSSHConnector> connectors = new HashMap<String, MpowerSSHConnector>();

	public MpowerBinding() {
	}

	public void activate() {
		
		BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		try {
			ServiceReference<?>[] all2 = context.getServiceReferences("org.openhab.core.persistence.QueryablePersistenceService", null);
			persistenceServiceReference = all2[0];
			persistenceService = (QueryablePersistenceService) context.getService(persistenceServiceReference);
			

			FilterCriteria filter = new FilterCriteria();
			filter.setEndDate(timestamp.toDate());
			filter.setItemName(item.getName());
			filter.setPageSize(1);
			filter.setOrdering(Ordering.DESCENDING);
			Iterable<HistoricItem> result = qService.query(filter);
			if(result.iterator().hasNext()) {
				return result.iterator().next();
			} else {
				return null;
			}
			
			int i = all.length;
			i = all2.length;
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ServiceReference<?> serviceReference  = context.getServiceReference("org.openhab.core.persistence.extensions");
		ServiceReference<?> serviceReference2 = context.getServiceReference(ItemRegistry.class.getName());
		PersistenceExtensions service = (PersistenceExtensions) context.getService(serviceReference);
		this.service = service;

	}

	public void deactivate() {
		shutDown();
		BundleContext context = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		context.ungetService(persistenceServiceReference);
	}

	protected Map<String, MpowerSSHConnector> getConnectors() {
		return this.connectors;
	}

	/**
	 * @{inheritDoc
	 */
	// @Override
	protected long getRefreshInterval() {
		return 2000;
	}

	/**
	 * @{inheritDoc
	 */
	// @Override
	protected String getName() {
		return "Ubiquiti mPower Binding";
	}

	/**
	 * @{inheritDoc
	 */
	// @Override
	protected void execute() {
	}

	/**
	 * stop all connectors
	 */
	private void shutDown() {
		for (MpowerSSHConnector connector : connectors.values()) {
			connector.stop();
			connector = null;
		}
		connectors.clear();
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// this is called as soon as a switch item gets triggered
		if (itemName != null && command instanceof OnOffType) {
			for (MpowerBindingProvider provider : providers) {
				// search through all mpower's and itemnames
				MpowerBindingConfig bindingConf = provider
						.getConfigForItemName(itemName);
				int socket = bindingConf.findSocketForItemName(itemName);
				OnOffType type = (OnOffType) command;
				connectors.get(bindingConf.getmPowerInstance()).send(socket,
						type);
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// we don't care
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {

			// clean up first
			shutDown();

			Enumeration<String> keys = config.keys();
			//
			// put all configurations into a nice structure
			//
			HashMap<String, MpowerConfig> bindingConfigs = new HashMap<String, MpowerConfig>();

			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				String mpowerId = StringUtils.substringBefore(key, ".");
				String configOption = StringUtils.substringAfterLast(key, ".");
				if (!"service".equals(mpowerId)) {

					MpowerConfig aConfig = null;
					if (bindingConfigs.containsKey(mpowerId)) {
						aConfig = bindingConfigs.get(mpowerId);
					} else {
						aConfig = new MpowerConfig();
						aConfig.setId(mpowerId);
						bindingConfigs.put(mpowerId, aConfig);
					}

					if (CONFIG_USERNAME.equals(configOption)) {
						aConfig.setUser((String) config.get(key));
					}

					if (CONFIG_PASSWORD.equals(configOption)) {
						aConfig.setPassword((String) config.get(key));
					}

					if (CONFIG_HOST.equals(configOption)) {
						aConfig.setHost((String) config.get(key));
					}

					if (CONFIG_REFRESH.equals(configOption)) {
						Long refresh = Long.parseLong((String) config.get(key));
						aConfig.setRefreshInterval(refresh);
					}
				}
			}

			//
			// now start the connectors
			//
			for (Map.Entry<String, MpowerConfig> entry : bindingConfigs
					.entrySet()) {
				MpowerConfig aConfig = entry.getValue();
				if (aConfig.isValid()) {

					logger.debug("Creating and starting new connector ",
							aConfig.getId());

					MpowerSSHConnector connector = new MpowerSSHConnector(
							aConfig.getHost(), aConfig.getId(),
							aConfig.getUser(), aConfig.getPassword(), 3000L,
							this);
					connectors.put(aConfig.getId(), connector);
					connector.start();
				} else {
					logger.warn("Invalid mPower configuration");
				}
			}
		}
	}

	/**
	 * Called from websocket listener This method will update the OH items if
	 * 
	 * a) the refresh time has passed and the item has changed
	 * 
	 * b) or we run on 'real time mode' and the item has changed
	 * 
	 * @param state
	 *            new data received from mPower
	 */
	public void receivedData(MpowerSocketState socketState) {
		for (MpowerBindingProvider provider : providers) {
			MpowerBindingConfig bindingCfg = provider
					.getConfigForAddress(socketState.getAddress());

			int socketNumber = socketState.getSocket();
			MpowerSocketState cachedState = bindingCfg
					.getCacheForSocket(socketNumber);

			long refresh = connectors.get(bindingCfg.getmPowerInstance())
					.getRefreshInterval();
			boolean needsUpdate = bindingCfg.needsUpdate(socketNumber, refresh);
			// only proceed if the data has changed

			if (needsUpdate
					&& (cachedState == null || !cachedState.equals(socketState))) {

				// update consumption today
				String consumptionTodayItemName = bindingCfg
						.getEnergyTodayItemName(socketState.getSocket());
				if (StringUtils.isNotBlank(consumptionTodayItemName)) {
					State itemState = new DecimalType(socketState.getEnergy()
							- bindingCfg.getConsumptionAtMidnight(socketState
									.getSocket()));
					eventPublisher.postUpdate(consumptionTodayItemName,
							itemState);

				}
				// update voltage
				String volItemName = bindingCfg.getVoltageItemName(socketState
						.getSocket());
				if (StringUtils.isNotBlank(volItemName)) {
					State itemState = new DecimalType(socketState.getVoltage());
					eventPublisher.postUpdate(volItemName, itemState);
				}

				// update power
				String powerItemname = bindingCfg.getPowerItemName(socketState
						.getSocket());
				if (StringUtils.isNotBlank(powerItemname)) {
					State itemState = new DecimalType(socketState.getPower());
					eventPublisher.postUpdate(powerItemname, itemState);
				}

				// update energy
				String energyItemname = bindingCfg
						.getEnergyItemName(socketState.getSocket());
				if (StringUtils.isNotBlank(energyItemname)) {
					State itemState = new DecimalType(socketState.getEnergy());
					eventPublisher.postUpdate(energyItemname, itemState);
				}

				// update the cache
				bindingCfg.setCachedState(socketNumber, socketState);
			} else {
				logger.trace("suppressing update as socket state has not changed");
			}

			// switch changes we handle immediately
			boolean switchHasChanged = false;
			if (cachedState != null) {
				switchHasChanged = cachedState.isOn() != socketState.isOn();
			}

			if (cachedState == null || switchHasChanged) {
				// update switch
				String switchItemname = bindingCfg
						.getSwitchItemName(socketState.getSocket());
				if (StringUtils.isNotBlank(switchItemname)) {
					OnOffType state = socketState.isOn() ? OnOffType.ON
							: OnOffType.OFF;
					eventPublisher.postUpdate(switchItemname, state);
					// update the cache
					bindingCfg.setCachedState(socketNumber, socketState);
				}
			}
		}
	}
}