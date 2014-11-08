/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.anel.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.anel.AnelBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The actual Anel binding. Please see javadoc of
 * {@link AnelGenericBindingProvider} how to use it.
 * 
 * @author paphko
 * @since 1.6.0
 */
public class AnelBinding extends AbstractActiveBinding<AnelBindingProvider> implements ManagedService {

	/**
	 * Delay before first initial refresh call for initialization (required at
	 * any time after startup to make sure everything else is initialized).
	 */
	private static final int THREAD_INITIALIZATION_DELAY = 30000;

	/** Interruption timeout when disconnecting all threads. */
	private static final int THREAD_INTERRUPTION_TIMEOUT = 5000;

	/** Logger for this binding class. */
	private static final Logger logger = LoggerFactory.getLogger(AnelBinding.class);

	/**
	 * Internally used for communication with connector thread.
	 */
	static interface IInternalAnelBinding {
		/**
		 * Get all item names that are registered for the given command type.
		 * 
		 * @param cmd
		 *            A command type.
		 * @return All item names that are registered for the given command
		 *         type.
		 */
		Collection<String> getItemNamesForCommandType(AnelCommandType cmd);

		/**
		 * Connectors should use this to send updates to the event bus.
		 * 
		 * @param itemName
		 *            The item name (from
		 *            {@link #getItemNamesForCommandType(AnelCommandType)})
		 *            whose state was updated.
		 * @param newState
		 *            The new state to be sent to the event bus.
		 */
		void postUpdateToEventBus(String itemName, State newState);
	}

	/** The refresh interval which is used to poll values from the Anel server. */
	private long refreshInterval;

	/** Threads to communicate with Anel devices */
	private final Map<String, AnelConnectorThread> connectorThreads = new HashMap<String, AnelConnectorThread>();

	/** Binding facade used by the {@link #connectorThreads}. */
	private final IInternalAnelBinding bindingFacade = new IInternalAnelBinding() {

		@Override
		public Collection<String> getItemNamesForCommandType(AnelCommandType cmd) {
			return AnelBinding.this.getItemNamesForCommandType(cmd);
		}

		@Override
		public void postUpdateToEventBus(String itemName, State newState) {
			eventPublisher.postUpdate(itemName, newState);
		}
	};

	public void activate() {
		// don't do anything because we are still waiting for the update-call to
		// get config
	}

	public void deactivate() {
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
		disconnectAll();
		connectorThreads.clear();
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
	protected String getName() {
		return "Anel NET-PwrCtrl Service";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		/*
		 * poll the device for its state regularly, just in case UDP packets
		 * might be missed. Do that only when providers exist!
		 */
		if (refreshInterval > 0 && bindingsExist()) {
			refreshAll();
		}
	}

	/**
	 * Request a refresh on all threads.
	 */
	private void refreshAll() {
		for (AnelConnectorThread connectorThread : connectorThreads.values()) {
			connectorThread.requestRefresh();
		}
	}

	/**
	 * Disconnect all currently running listener threads.
	 */
	private void disconnectAll() {
		// first, notify all existing connector threads to interrupt
		for (String device : connectorThreads.keySet()) {
			logger.debug("Close message listener for device '" + device + "'");
			final AnelConnectorThread connectorThread = connectorThreads.get(device);
			connectorThread.setInterrupted();
		}
		// then wait for all of them to die
		for (String device : connectorThreads.keySet()) {
			final AnelConnectorThread connectorThread = connectorThreads.get(device);
			try {
				// wait for the thread to die with a timeout of 5 seconds
				connectorThread.join(THREAD_INTERRUPTION_TIMEOUT);
			} catch (InterruptedException e) {
				logger.info("Previous message listener closing interrupted for device '" + device + "'", e);
			}
		}
	}

	/**
	 * There is no need to react on status changes because we only listen to
	 * commands.
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
	}

	/**
	 * Valid commands are of type {@link OnOffType} for items bound to
	 * {@link AnelCommandType#F1} - {@link AnelCommandType#F8} or
	 * {@link AnelCommandType#IO1} - {@link AnelCommandType#IO8}.
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.trace("Received command (item='{}', command='{}')", itemName, command.toString());
		final Map<String, AnelCommandType> map = getCommandTypeForItemName(itemName);
		if (map == null || map.isEmpty()) {
			logger.debug("Invalid command for item name: '" + itemName + "'");
			return;
		}
		final String deviceId = map.keySet().iterator().next();
		final AnelConnectorThread connectorThread = connectorThreads.get(deviceId);
		if (connectorThread == null) {
			logger.debug("Could not find device '" + deviceId + "', missing configuration or not yet initialized.");
			return;
		}
		final AnelCommandType cmd = map.get(deviceId);

		// check for switchable command
		final boolean isSwitch = AnelCommandType.SWITCHES.contains(cmd);
		final boolean isIO = AnelCommandType.IOS.contains(cmd);
		if (isIO || isSwitch) {
			if (command instanceof OnOffType) {
				final boolean newStateBoolean = OnOffType.ON.equals(command);

				if (isSwitch) {

					final int switchNr = Integer.parseInt(cmd.name().substring(1));
					connectorThread.sendSwitch(switchNr, newStateBoolean);

				} else if (isIO) {

					final int ioNr = Integer.parseInt(cmd.name().substring(2));
					connectorThread.sendIO(ioNr, newStateBoolean);
				}
			} else {
				logger.warn("Invalid state for '" + cmd.name() + "' (expected: ON/OFF): " + command);
			}
		} else {
			logger.warn("Cannot switch '" + cmd.name() + "', supported switches: F1 - F8, IO1 - IO8");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		// disconnect all currently running threads
		disconnectAll();

		// clear map of previous threads because config changed
		connectorThreads.clear();
		refreshInterval = 0;

		// read new config
		try {
			refreshInterval = AnelConfigReader.readConfig(config, connectorThreads, bindingFacade);
			logger.debug("Anel configuration read with refresh interval " + refreshInterval + "ms and "
					+ connectorThreads.size() + " devices.");
		} catch (ConfigurationException e) {
			logger.error("Could not read configuration for Anel binding: " + e.getMessage());
			return;
		} catch (Exception e) {
			logger.error("Could not read configuration for Anel binding", e);
			return;
		}
		setProperlyConfigured(true);

		// successful! now start all device threads
		for (String device : connectorThreads.keySet()) {
			final AnelConnectorThread connectorThread = connectorThreads.get(device);
			logger.debug("Starting message listener for device: " + connectorThread);
			connectorThread.start();
		}

		// start new thread that calls an initial refresh so the internal state
		// can be initialized
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(THREAD_INITIALIZATION_DELAY);
				} catch (InterruptedException e) {
				}
				refreshAll();
			}
		}).start();
	}

	private Collection<String> getItemNamesForCommandType(AnelCommandType cmd) {
		if (cmd == null)
			return Collections.emptyList();
		final Set<String> itemNames = new HashSet<String>();
		for (final AnelBindingProvider provider : providers) {
			for (final String itemName : provider.getItemNames()) {
				final AnelCommandType commandType = provider.getCommandType(itemName);
				if (commandType.equals(cmd)) {
					itemNames.add(itemName);
				}
			}
		}
		return itemNames;
	}

	private Map<String, AnelCommandType> getCommandTypeForItemName(String itemName) {
		if (itemName == null || itemName.isEmpty())
			return null;
		for (final AnelBindingProvider provider : providers) {
			for (final String providerItemName : provider.getItemNames()) {
				if (itemName.equals(providerItemName)) {
					final AnelCommandType cmd = provider.getCommandType(itemName);
					final String deviceId = provider.getDeviceId(itemName);
					return Collections.singletonMap(deviceId, cmd);
				}
			}
		}
		return null;
	}
}
