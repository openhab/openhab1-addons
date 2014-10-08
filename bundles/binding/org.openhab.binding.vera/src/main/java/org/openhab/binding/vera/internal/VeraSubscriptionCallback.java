/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal;

import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.gena.GENASubscription;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link SubscriptionCallback} implementation used to 
 * subscribed to Vera events. 
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
@SuppressWarnings("rawtypes")
public class VeraSubscriptionCallback extends SubscriptionCallback {
	
	private static final Logger logger = LoggerFactory.getLogger(VeraSubscriptionCallback.class);

	/**
	 * The binding instance this callback is owned by.
	 */
	private VeraBinding binding;

	/**
	 * Creates a new subscription callback.
	 * 
	 * @param binding the owning binding
	 * @param service the service to subscribe to
	 * @param timeout the timeout used for subscriptions
	 */
	public VeraSubscriptionCallback(VeraBinding binding, Service service, int timeout) {
		super(service, timeout);
		this.binding = binding;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void failed(GENASubscription subscription, UpnpResponse responseStatus, Exception exception, String defaultMsg) {
		logger.warn("[" + binding.getBindingConfig().getItemName() + "] failed " + subscription, exception);
		binding.reschedule();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void established(GENASubscription subscription) {
		logger.trace("[{}] established {}", binding.getBindingConfig().getItemName(), subscription);
		binding.established();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void ended(GENASubscription subscription, CancelReason reason, UpnpResponse responseStatus) {
		logger.trace("[{}] ended {}, reason = {}, responseStatus = {}", binding.getBindingConfig().getItemName(), subscription, reason, responseStatus);
		binding.reschedule();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void eventReceived(GENASubscription subscription) {
		logger.trace("[{}] events received {}", binding.getBindingConfig().getItemName(), subscription);
		binding.updateValues(subscription.getCurrentValues());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void eventsMissed(GENASubscription subscription, int numberOfMissedEvents) {
		logger.warn("[{}] missed {} events for subscription = {}", binding.getBindingConfig().getItemName(), numberOfMissedEvents, subscription);
	}

}
