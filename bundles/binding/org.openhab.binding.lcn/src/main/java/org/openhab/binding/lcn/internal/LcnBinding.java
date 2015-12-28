/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.internal;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.lcn.common.LcnBindingNotification;
import org.openhab.binding.lcn.connection.Connection;
import org.openhab.binding.lcn.connection.ConnectionManager;
import org.openhab.binding.lcn.connection.ConnectionSettings;
import org.openhab.binding.lcn.input.Input;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class of the LCN openHAB binding.
 * <p>
 * The binding executes everything on the refresh-thread inside {@link #execute()}.
 * This assures the required thread-safety.
 * Input data is received through {@link #connections}.
 * 
 * @author Tobias Jüttner
 *
 * @param <P> {@link LcnGenericBindingProvider}
 */
public class LcnBinding<P extends LcnGenericBindingProvider> extends AbstractBinding<P> implements ManagedService, LcnBindingActiveService.Callback, ConnectionManager.Callback {
	
	/** Logger for this class. */
	private static final Logger logger = LoggerFactory.getLogger(LcnBinding.class);
	
	/** Refresh interval for the service thread. */
	private static final int REFRESH_INTERVAL_MSEC = 1000;
	
	/** embedded active service to allow the binding to have some code executed in a given interval. */
	private final LcnBindingActiveService activeService = new LcnBindingActiveService(this);
	
	/**
	 * Connection settings read from configuration.
	 * Key is the connection identifier {@link ConnectionSettings#getId()} in upper-case.
	 */
	private HashMap<String, ConnectionSettings> connectionSettings = new HashMap<String, ConnectionSettings>();
	
	/** Holds all currently existing connections. */
	private ConnectionManager connections = new ConnectionManager(this);	
	
	/** Semaphore to wait for new notifications to process in refresh thread. */
	private Semaphore notificationsSem = new Semaphore(0);
	
	/**
	 * List of notifications to process in the refresh thread.
	 * List must be synchronized.
	 */
	private final LinkedList<LcnBindingNotification> notifications = new LinkedList<LcnBindingNotification>(); 
	
	/**
	 * Called whenever the configurations are updated.
	 * 
	 * @param config the updated configurations
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.info("Loading LCN configuration...");
		// Reset to default values
		this.connectionSettings.clear();
		// Load connection settings
		int counter = 0;
		ConnectionSettings sets;
		while ((sets = ConnectionSettings.tryParse(config, counter)) != null) {
			this.connectionSettings.put(sets.getId().toUpperCase(), sets);
			++counter;
		}
		logger.info("LCN configuration loaded.");
		// Finished
		if (this.providers.size() > 0) {
			this.activeService.setProperlyConfigured(true);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		final String itemName2 = itemName;
		final Command command2 = command;
		this.runOnRefreshThreadAsync(new LcnBindingNotification() {
			public void execute() {
				for (LcnGenericBindingProvider provider : LcnBinding.this.providers) {
					LcnBindingConfig itemConfig = provider.getLcnItemConfig(itemName2);
					if (itemConfig != null) {
						itemConfig.send(LcnBinding.this.connections, command2);
					}
				}
			}
		});
	}
	
	/**
	 * Tells the connections the binding is activated.
	 * {@inheritDoc}
	 */
	@Override
	public void activate() {
		this.connections.activate();
	}

	/**
	 * Tells the connections the binding is deactivated.
	 * {@inheritDoc}
	 */
	@Override
	public void deactivate() {
		this.connections.deactivate();
	}
	
	/** {@inheritDoc} */
	@Override
	public void runOnRefreshThreadAsync(LcnBindingNotification n) {
		synchronized (this.notifications) {
			this.notifications.add(n);
			this.notificationsSem.release();
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void updateItems(Connection conn) {
		for (LcnGenericBindingProvider provider : this.providers) {
			for (String itemName : provider.getItemNames()) {
				LcnBindingConfig itemConfig = provider.getLcnItemConfig(itemName);
				itemConfig.update(conn);
			}
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void onInputReceived(String input, Connection conn) {
		logger.debug(String.format("Channel \"%s\" received input: %s", conn.getSets().getId(), input));
		for (Input pchkInput : Input.parse(input)) {
			pchkInput.process(conn);
			for (LcnGenericBindingProvider provider : this.providers) {
				for (String itemName : provider.getItemNamesForPchkInput(pchkInput)) {
					LcnBindingConfig itemConfig = provider.getLcnItemConfig(itemName);  // Might return null. "getItemNames" can hold (cached) old data 
					if (itemConfig != null) {
						itemConfig.processInput(pchkInput, conn, this.eventPublisher);
					}
				}
			}
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public Map<String, ConnectionSettings> getAllSets() {
		return this.connectionSettings;
	}
	
	/** Processes notifications. */
	@Override
	public void execute() {
		// Update connections
		this.connections.update();
		this.connections.flush();
		// Process notifications
		try {
			long startTime = System.nanoTime();
			int timeoutMSec = REFRESH_INTERVAL_MSEC;
			while (timeoutMSec >= 0 && this.notificationsSem.tryAcquire(timeoutMSec, TimeUnit.MILLISECONDS)) {
				LcnBindingNotification notification;
				synchronized (this.notifications) {
					notification = this.notifications.pollFirst();
				}
				notification.execute();
				this.connections.flush();
				// Next
				timeoutMSec = REFRESH_INTERVAL_MSEC - (int)((System.nanoTime() - startTime) / 1000000L);
			}
		} catch (InterruptedException ex) { }
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean bindingsExists() {
		return this.bindingsExist();
	}
	
	/** {@inheritDoc} */
	@Override
	public void addBindingProvider(BindingProvider provider) {
		super.addBindingProvider(provider);
		this.activeService.start();
	}

	/** {@inheritDoc} */
	@Override
	public void removeBindingProvider(BindingProvider provider) {
		super.removeBindingProvider(provider);
		// If there are no binding providers left, we can stop the service
		if (this.providers.size() == 0) {
			this.activeService.stop();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		super.bindingChanged(provider, itemName);
		if (this.bindingsExist()) {
			this.activeService.start();
		}
		else {
			this.activeService.stop();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void allBindingsChanged(BindingProvider provider) {
		super.allBindingsChanged(provider);
		if (bindingsExist()) {
			this.activeService.start();
		}
		else {
			this.activeService.stop();
		}
	}
	
}
