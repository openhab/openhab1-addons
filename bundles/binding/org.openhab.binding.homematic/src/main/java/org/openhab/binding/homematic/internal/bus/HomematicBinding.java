/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.bus;

import java.util.Dictionary;
import java.util.TimerTask;

import org.openhab.binding.homematic.HomematicBindingProvider;
import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.HomematicCommunicator;
import org.openhab.binding.homematic.internal.config.binding.HomematicBindingConfig;
import org.openhab.binding.homematic.internal.util.BindingChangedDelayedExecutor;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Homematic binding implementation.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class HomematicBinding extends AbstractActiveBinding<HomematicBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(HomematicBinding.class);

	private HomematicContext context = HomematicContext.getInstance();
	private HomematicCommunicator communicator = new HomematicCommunicator();
	private BindingChangedDelayedExecutor delayedExecutor = new BindingChangedDelayedExecutor(communicator);

	/**
	 * Adding shudown hook to stop the Homematic server communicator.
	 */
	public HomematicBinding() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				communicator.stop();
			}
		});
	}

	/**
	 * Saves the eventPublisher in the Homematic context.
	 */
	@Override
	public void setEventPublisher(EventPublisher eventPublisher) {
		super.setEventPublisher(eventPublisher);
		context.setEventPublisher(eventPublisher);
	}

	/**
	 * Saves the providers in the Homematic context.
	 */
	@Override
	public void activate() {
		context.setProviders(providers);
	}

	/**
	 * Stops the communicator.
	 */
	@Override
	public void deactivate() {
		communicator.stop();
	}

	/**
	 * Updates the configuration for the binding and (re-)starts the
	 * communicator.
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			setProperlyConfigured(false);
			communicator.stop();

			context.getConfig().parse(config);
			logger.info(context.getConfig().toString());

			if (context.getConfig().isValid()) {
				communicator.start();
				setProperlyConfigured(true);

				for (HomematicBindingProvider hmProvider : providers) {
					for (String itemName : hmProvider.getItemNames()) {
						informCommunicator(hmProvider, itemName);
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void allBindingsChanged(BindingProvider provider) {
		super.allBindingsChanged(provider);
		if (isProperlyConfigured()) {
			if (provider instanceof HomematicBindingProvider) {
				HomematicBindingProvider hmProvider = (HomematicBindingProvider) provider;
				for (String itemName : hmProvider.getItemNames()) {
					informCommunicator(hmProvider, itemName);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		super.bindingChanged(provider, itemName);
		if (isProperlyConfigured()) {
			if (provider instanceof HomematicBindingProvider) {
				HomematicBindingProvider hmProvider = (HomematicBindingProvider) provider;
				informCommunicator(hmProvider, itemName);
			}
		}
	}

	/**
	 * Schedules a job with a short delay to populate changed items to openHAB
	 * after startup or an item reload.
	 * 
	 * @see BindingChangedDelayedExecutor
	 */
	private void informCommunicator(HomematicBindingProvider hmProvider, String itemName) {
		final Item item = hmProvider.getItem(itemName);
		final HomematicBindingConfig bindingConfig = hmProvider.getBindingFor(itemName);
		if (bindingConfig != null) {
			delayedExecutor.cancel();
			delayedExecutor.addBindingConfig(item, bindingConfig);
			delayedExecutor.schedule(new TimerTask() {

				@Override
				public void run() {
					delayedExecutor.publishChangedBindings();
				}

			}, 3000);
		}
	}

	/**
	 * Receives a command and send it to the Homematic communicator.
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		for (HomematicBindingProvider provider : providers) {
			Item item = provider.getItem(itemName);
			HomematicBindingConfig config = provider.getBindingFor(itemName);
			communicator.receiveCommand(item, command, config);
		}
	}

	/**
	 * Receives a state and send it to the Homematic communicator.
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		for (HomematicBindingProvider provider : providers) {
			Item item = provider.getItem(itemName);
			HomematicBindingConfig config = provider.getBindingFor(itemName);
			communicator.receiveUpdate(item, newState, config);
		}
	}

	/**
	 * Restarts the Homematic communicator if no messages arrive within a
	 * configured time.
	 */
	@Override
	protected void execute() {
		long timeSinceLastEvent = (System.currentTimeMillis() - communicator.getLastEventTime()) / 1000;
		if (timeSinceLastEvent > context.getConfig().getAliveInterval()) {
			logger.info("No event since {} seconds, refreshing Homematic server connections", timeSinceLastEvent);
			communicator.stop();
			communicator.start();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return context.getConfig().getAliveInterval() * 1000;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Homematic server keep alive thread";
	}

}
