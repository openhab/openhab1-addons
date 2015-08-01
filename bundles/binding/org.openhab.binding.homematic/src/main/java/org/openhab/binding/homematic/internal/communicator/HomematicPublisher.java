/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.client.HomematicClientException;
import org.openhab.binding.homematic.internal.communicator.client.interfaces.HomematicClient;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.core.binding.BindingConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Publishes events to a Homematic server.
 * 
 * @author Gerhard Riegler
 * @since 1.7.0
 */
public class HomematicPublisher {
	private static final Logger logger = LoggerFactory.getLogger(HomematicPublisher.class);

	private HomematicContext context = HomematicContext.getInstance();
	private Map<BindingConfig, Timer> delayedEvents = new HashMap<BindingConfig, Timer>();

	/**
	 * Sends or delays a event to a Homematic server.
	 */
	public void execute(final Event event) throws HomematicClientException {
		double delay = event.getDelay();
		if (delay > 0.0) {
			synchronized (this) {
				logger.debug("Delaying event for {} seconds: {}", delay, event.getHmValueItem());

				Timer timer = delayedEvents.get(event.getBindingConfig());
				if (timer != null) {
					timer.cancel();
				}

				timer = new Timer();
				delayedEvents.put(event.getBindingConfig(), timer);
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						try {
							delayedEvents.remove(event.getBindingConfig());
							sendToClient(event);
						} catch (Exception ex) {
							logger.error(ex.getMessage(), ex);
						}
					}
				}, (long) (delay * 1000));
			}
		} else {
			sendToClient(event);
		}

	}
	/**
	 * Sends the event to a Homematic server.
	 */
	private void sendToClient(Event event) throws HomematicClientException {
		logger.debug("Sending to Homematic server {}", event.getHmValueItem());
		HomematicClient client = context.getHomematicClient();
		if (event.isVariable()) {
			client.setVariable(event.getHmValueItem(), event.getNewValue());
		} else {
			client.setDatapointValue((HmDatapoint) event.getHmValueItem(), event.getHmValueItem().getName(),
					event.getNewValue());
		}

		event.getHmValueItem().setValue(event.getNewValue());
	}
}
