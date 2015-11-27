/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.satel.SatelBindingConfig;
import org.openhab.binding.satel.SatelBindingProvider;
import org.openhab.binding.satel.internal.event.ConnectionStatusEvent;
import org.openhab.binding.satel.internal.event.NewStatesEvent;
import org.openhab.binding.satel.internal.event.SatelEvent;
import org.openhab.binding.satel.internal.event.SatelEventListener;
import org.openhab.binding.satel.internal.protocol.Ethm1Module;
import org.openhab.binding.satel.internal.protocol.IntRSModule;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.openhab.binding.satel.internal.protocol.SatelModule;
import org.openhab.binding.satel.internal.protocol.command.IntegraStatusCommand;
import org.openhab.binding.satel.internal.protocol.command.NewStatesCommand;
import org.openhab.binding.satel.internal.types.IntegraType;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is main service class that helps exchanging data between openHAB and
 * Satel module in both directions. Implements regular openHAB binding service.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class SatelBinding extends AbstractActiveBinding<SatelBindingProvider> implements ManagedService,
		SatelEventListener {

	private static final Logger logger = LoggerFactory.getLogger(SatelBinding.class);

	private long refreshInterval = 10000;
	private String userCode;
	private SatelModule satelModule = null;
	private SatelMessage newStatesCommand = null;
	private boolean forceRefresh = false;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Satel Refresh Service";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute() {
		if (!this.satelModule.isInitialized()) {
			logger.debug("Module not initialized yet, skipping refresh");
			return;
		}

		// get list of states that have changed
		logger.trace("Sending 'get new states' command");
		if (this.newStatesCommand == null) {
			this.newStatesCommand = NewStatesCommand
					.buildMessage(this.satelModule.getIntegraType() == IntegraType.I256_PLUS);
		}
		this.satelModule.sendCommand(this.newStatesCommand);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.trace("Binding configuration updated");

		if (config == null) {
			return;
		}

		ConfigurationDictionary configuration = new ConfigurationDictionary(config);

		this.refreshInterval = configuration.getLong("refresh", 10000);
		this.userCode = configuration.getString("user_code");

		int timeout = configuration.getInt("timeout", 5000);
		String host = configuration.getString("host");
		if (StringUtils.isNotBlank(host)) {
			this.satelModule = new Ethm1Module(host, configuration.getInt("port", 7094), timeout,
					configuration.getString("encryption_key"));
		} else {
			this.satelModule = new IntRSModule(configuration.getString("port"), timeout);
		}

		this.satelModule.addEventListener(this);
		this.satelModule.open();
		setProperlyConfigured(true);
		logger.trace("Binding properly configured");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		if (!isProperlyConfigured()) {
			logger.warn("Binding not properly configured, exiting");
			return;
		}

		if (!this.satelModule.isInitialized()) {
			logger.debug("Module not initialized yet, ignoring command");
			return;
		}

		for (SatelBindingProvider provider : providers) {
			SatelBindingConfig itemConfig = provider.getItemConfig(itemName);
			if (itemConfig != null) {
				logger.trace("Sending internal command for item {}: {}", itemName, command);
				SatelMessage message = itemConfig.convertCommandToMessage(command, this.satelModule.getIntegraType(),
						this.userCode);
				if (message != null) {
					this.satelModule.sendCommand(message);
				}
				break;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void incomingEvent(SatelEvent event) {
		logger.trace("Handling incoming event: {}", event);

		// refresh all states that have changed
		if (event instanceof NewStatesEvent) {
			List<SatelMessage> commands = getRefreshCommands((NewStatesEvent) event);
			for (SatelMessage message : commands) {
				this.satelModule.sendCommand(message);
			}
		}

		// if just connected, force refreshing
		if (event instanceof ConnectionStatusEvent) {
			ConnectionStatusEvent statusEvent = (ConnectionStatusEvent) event;
			if (statusEvent.isConnected()) {
				switchForceRefresh(true);
			}
		}

		// update items
		for (SatelBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				SatelBindingConfig itemConfig = provider.getItemConfig(itemName);
				Item item = provider.getItem(itemName);
				State newState = itemConfig.convertEventToState(item, event);
				if (newState != null) {
					logger.debug("Updating item state: item = {}, state = {}, event = {}", itemName, newState, event);
					eventPublisher.postUpdate(itemName, newState);
					itemConfig.setItemInitialized();
				}
			}
		}
	}

	/**
	 * Deactivates the binding by closing connected module.
	 */
	@Override
	public void deactivate() {
		if (this.satelModule != null) {
			this.satelModule.close();
			this.satelModule = null;
		}
		this.newStatesCommand = null;
	}

	private List<SatelMessage> getRefreshCommands(NewStatesEvent nse) {
		logger.trace("Gathering refresh commands from all items");

		boolean forceRefresh = switchForceRefresh(false);
		List<SatelMessage> commands = new ArrayList<SatelMessage>();
		for (SatelBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				logger.trace("Getting refresh command from item: {}", itemName);

				SatelBindingConfig itemConfig = provider.getItemConfig(itemName);
				SatelMessage message = itemConfig.buildRefreshMessage(this.satelModule.getIntegraType());

				if (message == null || commands.contains(message)) {
					continue;
				}

				// either state has changed or this is status command (so likely RTC has changed)
				// also if item hasn't received any update yet or refresh is forced
				// get the latest value from the module
				if (forceRefresh || !itemConfig.isItemInitialized() || (nse != null && nse.isNew(message.getCommand()))
						|| message.getCommand() == IntegraStatusCommand.COMMAND_CODE) {
					commands.add(message);
				}
			}
		}

		return commands;
	}

	/**
	 * Changes <code>forceRefresh</code> flag atomically returning previous
	 * value.
	 * 
	 * @param forceRefresh
	 *            new value for the flag
	 * @return previous value of the flag
	 */
	private synchronized boolean switchForceRefresh(boolean forceRefresh) {
		boolean oldValue = this.forceRefresh;
		this.forceRefresh = forceRefresh;
		return oldValue;
	}
}
