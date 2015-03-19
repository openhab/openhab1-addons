/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal;

import java.util.Dictionary;
import java.util.Map;

import org.openhab.binding.onewire.OneWireBindingProvider;
import org.openhab.binding.onewire.internal.connection.OneWireConnection;
import org.openhab.binding.onewire.internal.deviceproperties.AbstractOneWireDevicePropertyBindingConfig;
import org.openhab.binding.onewire.internal.deviceproperties.AbstractOneWireDevicePropertyWritableBindingConfig;
import org.openhab.binding.onewire.internal.listener.InterfaceOneWireDevicePropertyWantsUpdateListener;
import org.openhab.binding.onewire.internal.listener.OneWireDevicePropertyWantsUpdateEvent;
import org.openhab.binding.onewire.internal.scheduler.OneWireUpdateScheduler;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The 1-wire items / device properties are scheduled and refreshed via OneWireUpdateScheduler for this binding
 * 
 * @author Thomas.Eichstaedt-Engelen, Dennis Riegelbauer
 * @since 0.6.0
 */
public class OneWireBinding extends AbstractBinding<OneWireBindingProvider> implements ManagedService, InterfaceOneWireDevicePropertyWantsUpdateListener {

	private static final Logger logger = LoggerFactory.getLogger(OneWireBinding.class);

	/**
	 * Scheduler for items
	 */
	private OneWireUpdateScheduler ivOneWireReaderScheduler;

	public OneWireBinding() {
		super();
		ivOneWireReaderScheduler = new OneWireUpdateScheduler(this);
	}

	@Override
	public void activate() {
		super.activate();
		logger.debug("activate onewire-binding");
		ivOneWireReaderScheduler.start();
	}

	@Override
	public void deactivate() {
		super.deactivate();
		logger.debug("deactivate onewire-binding");
		ivOneWireReaderScheduler.stop();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	public void updated(Dictionary<String, ?> pvConfig) throws ConfigurationException {

		logger.debug("updated onewire-binding");

		if (pvConfig != null) {
			OneWireConnection.updated(pvConfig);
		}

		for (OneWireBindingProvider lvProvider : providers) {
			scheduleAllBindings(lvProvider);
		}

	}

	@Override
	protected void internalReceiveCommand(String pvItemName, Command pvCommand) {

		AbstractOneWireDevicePropertyBindingConfig lvBindigConfig = getBindingConfig(pvItemName);

		if (lvBindigConfig instanceof AbstractOneWireDevicePropertyWritableBindingConfig) {
			logger.debug("received command " + pvCommand.toString() + " for item " + pvItemName);

			AbstractOneWireDevicePropertyWritableBindingConfig lvWritableBindingConfig = (AbstractOneWireDevicePropertyWritableBindingConfig) lvBindigConfig;

			String lvStringValue = lvWritableBindingConfig.convertTypeToString(pvCommand);

			OneWireConnection.writeToOneWire(lvWritableBindingConfig.getDevicePropertyPath(), lvStringValue);
		} else {
			logger.debug("received command " + pvCommand.toString() + " for item " + pvItemName + " which is not writable");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.core.binding.AbstractBinding#allBindingsChanged(org.openhab.core.binding.BindingProvider)
	 */
	public void allBindingsChanged(BindingProvider pvProvider) {
		scheduleAllBindings(pvProvider);
	}

	/**
	 * schedule All Bindings to get updated
	 * 
	 * @param pvProvider
	 */
	private void scheduleAllBindings(BindingProvider pvProvider) {
		if (OneWireConnection.isConnectionEstablished()) {
			logger.debug("scheduleAllBindings");

			if (pvProvider instanceof OneWireBindingProvider) {
				OneWireBindingProvider lvBindingProvider = (OneWireBindingProvider) pvProvider;
				ivOneWireReaderScheduler.clear();

				Map<String, BindingConfig> lvBindigConfigs = lvBindingProvider.getBindingConfigs();
				for (String lvItemName : lvBindigConfigs.keySet()) {
					logger.debug("Initializing read of item {}.", lvItemName);
					AbstractOneWireDevicePropertyBindingConfig lvBindingConfig = (AbstractOneWireDevicePropertyBindingConfig) lvBindigConfigs.get(lvItemName);
					
					if (lvBindingConfig != null) {
						int lvAutoRefreshTimeInSecs = lvBindingConfig.getAutoRefreshInSecs();
						if (lvAutoRefreshTimeInSecs > -1) {
							ivOneWireReaderScheduler.updateOnce(lvItemName);
						}
	
						if (lvAutoRefreshTimeInSecs > 0) {
							if (!ivOneWireReaderScheduler.scheduleUpdate(lvItemName, lvAutoRefreshTimeInSecs)) {
								logger.warn("Clouldn't add to OneWireUpdate scheduler", lvBindingConfig);
							}
						}
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.core.binding.AbstractBinding#bindingChanged(org.openhab.core.binding.BindingProvider,
	 * java.lang.String)
	 */
	public void bindingChanged(BindingProvider lvProvider, String lvItemName) {
		logger.debug("bindingChanged() for item {} msg received.", lvItemName);

		if (lvProvider instanceof OneWireBindingProvider) {
			OneWireBindingProvider lvBindingProvider = (OneWireBindingProvider) lvProvider;

			AbstractOneWireDevicePropertyBindingConfig lvBindingConfig = lvBindingProvider.getBindingConfig(lvItemName);

			if (lvBindingConfig != null) {
				logger.debug("Initializing read of item {}.", lvItemName);
				int lvAutoRefreshTimeInSecs = lvBindingConfig.getAutoRefreshInSecs();
				
				if (lvAutoRefreshTimeInSecs>-1) {
					ivOneWireReaderScheduler.updateOnce(lvItemName);
				}

				if (lvAutoRefreshTimeInSecs > 0) {
					if (!ivOneWireReaderScheduler.scheduleUpdate(lvItemName, lvAutoRefreshTimeInSecs)) {
						logger.warn("Clouldn't add to OneWireUpdate scheduler", lvBindingConfig);
					}
				} else {
					logger.debug("Didnt't add to OneWireUpdate scheduler, because refresh is <= 0: " + lvBindingConfig.toString());
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openhab.binding.onewire.internal.listener.InterfaceOneWireDevicePropertyWantsUpdateListener#
	 * devicePropertyWantsUpdate(org.openhab.binding.onewire.internal.listener.OneWireDevicePropertyWantsUpdateEvent)
	 */
	public void devicePropertyWantsUpdate(OneWireDevicePropertyWantsUpdateEvent pvWantsUpdateEvent) {
		String lvItemName = pvWantsUpdateEvent.getItemName();

		logger.debug("Item " + lvItemName + " wants update!");

		updateItemFromOneWire(lvItemName);
	}

	/**
	 * 
	 * @param pvItemName
	 * @return the corresponding AbstractOneWireDevicePropertyBindingConfig to the given <code>pvItemName</code>
	 */
	private AbstractOneWireDevicePropertyBindingConfig getBindingConfig(String pvItemName) {
		for (OneWireBindingProvider lvProvider : providers) {
			return lvProvider.getBindingConfig(pvItemName);
		}
		return null;
	}

	/**
	 * 
	 * @param pvItemName
	 * @return the corresponding Item to the given <code>pvItemName</code>
	 */
	private Item getItem(String pvItemName) {
		for (OneWireBindingProvider lvProvider : providers) {
			return lvProvider.getItem(pvItemName);
		}
		return null;
	}

	/**
	 * Update an item with value from 1-wire device property
	 * 
	 * @param pvItemName
	 */
	public void updateItemFromOneWire(String pvItemName) {
		if (OneWireConnection.getConnection() != null) {

			AbstractOneWireDevicePropertyBindingConfig pvBindingConfig = getBindingConfig(pvItemName);

			if (pvBindingConfig == null) {
				logger.error("no bindingConfig found for itemName=" + pvItemName + " cannot update! It will be removed from scheduler");
				ivOneWireReaderScheduler.removeItem(pvItemName);
				return;
			}

			String lvReadValue = OneWireConnection.readFromOneWire(pvBindingConfig.getDevicePropertyPath());

			Item lvItem = getItem(pvItemName);
			if (lvReadValue != null) {
				Type lvType = pvBindingConfig.convertReadValueToType(lvReadValue);
				if (lvItem != null) {
					synchronized (lvItem) {
						if (!lvItem.getState().equals(lvReadValue)) {
							State lvState = (State) lvType;
							eventPublisher.postUpdate(lvItem.getName(), lvState);
						}
					}
				} else {
					logger.error("There is no Item for ItemName=" + pvItemName);
				}
			} else {
				logger.error("Set Item for itemName=" + pvItemName + " to Undefined, because the readvalue is null");
				eventPublisher.postUpdate(lvItem.getName(), UnDefType.UNDEF);
			}
		}
	}
}
