/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.connectsdk.internal.bridges;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openhab.binding.connectsdk.ConnectSDKBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.command.ServiceSubscription;
import com.connectsdk.service.command.URLServiceSubscription;


/**
 * @author Sebastian Prehn
 * @since 1.8.0
 */
public abstract class AbstractOpenhabConnectSDKPropertyBridge<T> implements OpenhabConnectSDKPropertyBridge {
	private static final Logger logger = LoggerFactory.getLogger(AbstractOpenhabConnectSDKPropertyBridge.class);

	private Map<String, ServiceSubscription<T>> subscriptions;

	private synchronized Map<String, ServiceSubscription<T>> getSubscriptions() {
		if (subscriptions == null) {
			subscriptions = new ConcurrentHashMap<String, ServiceSubscription<T>>();
		}
		return subscriptions;
	}
	
	@Override
	public void onDeviceReady(final ConnectableDevice device, final Collection<ConnectSDKBindingProvider> providers, final EventPublisher eventPublisher) {
		// NOP
	}
	
	@Override
	public void onDeviceRemoved(final ConnectableDevice device, final Collection<ConnectSDKBindingProvider> providers, final EventPublisher eventPublisher) {
		// NOP
	}

	@Override
	public final synchronized void refreshSubscription(final ConnectableDevice device,
			final Collection<ConnectSDKBindingProvider> providers, final EventPublisher eventPublisher) {
		removeAnySubscription(device); // ensure all old subscriptions are cleaned out
		Collection<String> matchingItemNames = findMatchingItemNames(device, providers);
		if (!matchingItemNames.isEmpty()) { // only listen if least one item is configured
			ServiceSubscription<T> listener = getSubscription(device, matchingItemNames, eventPublisher);
			if (listener != null) {
				logger.debug("Subscribed {}:{} listener on IP: {}", getItemClass(), getItemProperty(),
						device.getIpAddress());
				getSubscriptions().put(device.getIpAddress(), listener);
			}
		}
	}

	protected Collection<String> findMatchingItemNames(ConnectableDevice device,
			Collection<ConnectSDKBindingProvider> providers) {
		Collection<String> matchingItemNames = new ArrayList<String>();

		for (ConnectSDKBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				try {
					if (matchClassAndProperty(provider.getClassForItem(itemName), provider.getPropertyForItem(itemName))
							&& device.getIpAddress().equals(
									InetAddress.getByName(provider.getDeviceForItem(itemName)).getHostAddress())) {
						matchingItemNames.add(itemName);
					}
				} catch (UnknownHostException e) {
					logger.error("Failed to resolve {} to IP address. Skipping update on item {}.", device, itemName);
				}
			}

		}
		return matchingItemNames;
	}

	/**
	 * Creates a subscription instance for this device. This may return <code>null</code> if no subscription is possible
	 * or required.
	 * 
	 * @param device device to which state changes to subscribe to
	 * @param itemNames item's names that shall be update on device status change. Only items that match the device ip, item property and item class must be provided.
	 * @param eventPublisher 
	 * @return instance or <code>null</code> if no subscription is possible or required
	 */
	protected ServiceSubscription<T> getSubscription(final ConnectableDevice device,  final Collection<String> itemNames, final EventPublisher eventPublisher) {
		return null;
	}

	@Override
	public final synchronized void removeAnySubscription(final ConnectableDevice device) { // here
		if (subscriptions != null) { // only if subscriptions was initialized (lazy loading)
			ServiceSubscription<T> l = subscriptions.remove(device.getIpAddress());
			if (l != null) {
				l.unsubscribe();

				// not sure if this is required, trying to find performance leak
				if (l instanceof URLServiceSubscription) {
					((URLServiceSubscription<?>) l).removeListeners();
				}

				logger.debug("Unsubscribed {}:{} listener on IP: {}", getItemClass(), getItemProperty(),
						device.getIpAddress());
			}
		}
	}

	/**
	 * 
	 * @return the third binding configuration parameter in the items file
	 */
	protected abstract String getItemProperty();

	/**
	 * 
	 * @return the second binding configuration parameter in the items file
	 */
	protected abstract String getItemClass();

	protected boolean matchClassAndProperty(String clazz, String property) {
		return getItemClass().equals(clazz) && getItemProperty().equals(property);
	}

	protected <O> ResponseListener<O> createDefaultResponseListener() {
		return new ResponseListener<O>() {

			@Override
			public void onError(ServiceCommandError error) {
				logger.error("Error setting {}:{}: {}.", getItemClass(), getItemProperty(), error.getMessage());

			}

			@Override
			public void onSuccess(O object) {
				logger.debug("Successfully set {}:{}: {}.", getItemClass(), getItemProperty(), object);

			}
		};
	}

}