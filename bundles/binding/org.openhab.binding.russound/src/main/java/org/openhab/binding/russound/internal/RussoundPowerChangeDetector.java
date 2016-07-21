/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.russound.internal;

import java.beans.PropertyChangeEvent;
import java.util.Collection;

import org.openhab.binding.russound.RussoundBindingConfig;
import org.openhab.binding.russound.RussoundBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RussoundPowerChangeDetector implements ZoneListener {

	private static final Logger logger = LoggerFactory
			.getLogger(RussoundVolumeChangeDetector.class);
	private Collection<RussoundBindingProvider> providers;
	private EventPublisher mPublisher;

	public RussoundPowerChangeDetector(
			Collection<RussoundBindingProvider> context,
			EventPublisher publisher) {
		providers = context;
		mPublisher = publisher;

	}

	public void onPropertyChange(PropertyChangeEvent event, ZoneAddress address) {
		if ("power".equals(event.getPropertyName())) {
			AudioZone.ZonePower power = (AudioZone.ZonePower) event
					.getNewValue();
			Collection<RussoundBindingProvider> test = providers;
			for (RussoundBindingProvider russoundBindingProvider : test) {
				Collection<String> itemNames = russoundBindingProvider
						.getItemNames();
				for (String itemName : itemNames) {
					if (itemName.contains("_power")) {
						RussoundBindingConfig binding = russoundBindingProvider
								.getBindingConfig(itemName);
						if (binding.getZoneAddress().equals(address)) {
							logger.debug("Setting: " + itemName
									+ ", power level: " + power);
							switch (power) {
							case Off:
								mPublisher.postUpdate(itemName, OnOffType.OFF);
								break;
							case On:
								mPublisher.postUpdate(itemName, OnOffType.ON);
								break;
							default:
								logger.debug("Unexpected Zone Power value: "
										+ power);
								break;

							}

						}
					}
				}
			}
		}
	}

}
