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
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.ProviderItemIterator.ProviderItemIteratorCallback;
import org.openhab.binding.homematic.internal.config.binding.ActionConfig;
import org.openhab.binding.homematic.internal.config.binding.HomematicBindingConfig;
import org.openhab.binding.homematic.internal.config.binding.ProgramConfig;
import org.openhab.binding.homematic.internal.converter.state.Converter;
import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that sends OFF to items bound to a Homematic PRESS_* datapoint. This is
 * necessary because Homematic only sends ON for PRESS_* datapoints, so we have
 * to send OFF after a short delay. It's not possible to send the OFF command
 * immediately after the ON command, because i saw that sometimes the OFF
 * command receives before the ON command on the openHAB bus.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class ItemDisabler extends TimerTask {
	private static final Logger logger = LoggerFactory.getLogger(ItemDisabler.class);
	private static final long TIMER_DELAY = 1000;
	private static final long MIN_AGE = 2000;

	private HomematicContext context = HomematicContext.getInstance();

	private Map<HomematicBindingConfig, Long> itemsToDisable = new HashMap<HomematicBindingConfig, Long>();
	private Timer timer;

	/**
	 * Starts the ItemDisabler timer.
	 */
	public void start() {
		logger.debug("Starting {}", ItemDisabler.class.getSimpleName());
		timer = new Timer();
		timer.scheduleAtFixedRate(this, TIMER_DELAY, TIMER_DELAY);
	}

	/**
	 * Stops the ItemDisabler timer.
	 */
	public void stop() {
		logger.debug("Stopping {}", ItemDisabler.class.getSimpleName());
		this.cancel();
		timer.cancel();
	}

	/**
	 * Adds a binding to send a OFF command after a short delay.
	 */
	public void add(HomematicBindingConfig bindingConfig) {
		itemsToDisable.put(bindingConfig, System.currentTimeMillis());
	}

	/**
	 * Sends the OFF commands to the openHAB bus.
	 */
	@Override
	public void run() {
		Iterator<Map.Entry<HomematicBindingConfig, Long>> iterator = itemsToDisable.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<HomematicBindingConfig, Long> entry = iterator.next();
			long diff = System.currentTimeMillis() - entry.getValue();
			if (diff > MIN_AGE) {
				new ProviderItemIterator().iterate(entry.getKey(), new ProviderItemIteratorCallback() {

					@Override
					public void next(HomematicBindingConfig providerBindingConfig, Item item, Converter<?> converter) {
						HmValueItem hmValueItem = context.getStateHolder().getState(providerBindingConfig);
						if (providerBindingConfig instanceof ProgramConfig
								|| providerBindingConfig instanceof ActionConfig) {
							context.getEventPublisher().postUpdate(item.getName(), OnOffType.OFF);
						} else {
							hmValueItem.setValue(converter.convertToBinding(OnOffType.OFF, hmValueItem));
							State state = converter.convertFromBinding(hmValueItem);
							context.getEventPublisher().postUpdate(item.getName(), state);
						}
						logger.debug("Disabled Item {} with binding {}", item.getName(), providerBindingConfig);
					}
				});

				iterator.remove();
			}
		}
	}
}
