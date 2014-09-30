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
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Anel binding. Example configuration:
 * 
 * <pre>
 * ########################## Anel NET-PwrCtrl Binding ###################################
 * #
 * # UDP receive port (optional, defaults to 77)
 * anel:anel1.udpReceivePort=7777
 * 
 * # UDP send port (optional, defaults to 75)
 * anel:anel1.udpSendPort=7775
 * 
 * # IP or network address (optional, defaults to 'net-control')
 * anel:anel1.host=anel1
 * 
 * # User name (optional, defaults to 'user7')
 * anel:anel1.user=user7
 * 
 * # Password (optional, defaults to 'anel')
 * anel:anel1.password=anel
 * 
 * # Global refresh interval in ms (optional, defaults to 300000=5min, disable with '0')
 * #refresh=60
 * </pre>
 * 
 * Example items:
 * 
 * <pre>
 * Switch f1 { anel="anel1:F1" }
 * Switch io1 { anel="anel1:IO1" }
 * </pre>
 * 
 * Example rule:
 * 
 * <pre>
 * rule "switch test on anel"
 * when Item io1 changed to OFF then
 * 	postUpdate(f1, if (f1.state == ON) OFF else ON)
 * end
 * </pre>
 * 
 * @author paphko
 * @since 1.6.0
 */
public class AnelBinding extends AbstractActiveBinding<AnelBindingProvider> implements ManagedService {

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
		 * The event publisher so that the connector that can send events.
		 * 
		 * @return The event publisher that was set for the binding.
		 */
		EventPublisher getEventPublisher();
	}

	private static final Logger logger = LoggerFactory.getLogger(AnelBinding.class);

	/**
	 * The refresh interval which is used to poll values from the Anel server
	 * (optional, defaults to 300000ms).
	 */
	private long refreshInterval = 300000;

	/** Threads to communicate with Anel devices */
	private final Map<String, AnelConnectorThread> connectorThreads = new HashMap<String, AnelConnectorThread>();

	private final IInternalAnelBinding bindingFacade = new IInternalAnelBinding() {

		@Override
		public Collection<String> getItemNamesForCommandType(AnelCommandType cmd) {
			return AnelBinding.this.getItemNamesForCommandType(cmd);
		}

		@Override
		public EventPublisher getEventPublisher() {
			return eventPublisher;
		}
	};

	public void activate() {
		// don't do anything because we are still waiting for the update-call to
		// get config
	}

	public void deactivate() {
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
		for (AnelConnectorThread connectorThread : connectorThreads.values()) {
			connectorThread.setInterrupted();
		}
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
		 * might be missed. Do that first when providers exist!
		 */
		if (refreshInterval > 0 && bindingsExist()) {
			for (AnelConnectorThread connectorThread : connectorThreads.values()) {
				connectorThread.requestRefresh();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		// logger.debug("internalReceiveCommand() is called! " + itemName +
		// " -> " + command);
		internalReceive(itemName, command);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		// logger.debug("internalReceiveUpdate() is called! " + itemName + " = "
		// + newState);
		internalReceive(itemName, newState);
	}

	private void internalReceive(String itemName, Type newState) {
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
			if (newState instanceof OnOffType) {
				final boolean newStateBoolean = OnOffType.ON.equals(newState);

				if (isSwitch) {

					final int switchNr = Integer.parseInt(cmd.name().substring(1));
					connectorThread.sendSwitch(switchNr, newStateBoolean);

				} else if (isIO) {

					final int ioNr = Integer.parseInt(cmd.name().substring(2));
					connectorThread.sendIO(ioNr, newStateBoolean);
				}
			} else {
				logger.debug("Invalid state for '" + cmd.name() + "' (expected: ON/OFF): " + newState);
			}
		} else {
			/*
			 * We receive this update even if *we* set the value, so better
			 * ignore all calls that are not changeable
			 */
			// logger.debug("Cannot switch '" + cmd.name() +
			// "', supported switches: F1 - F8, IO1 - IO8");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		// first, close all existing connector threads
		for (String device : connectorThreads.keySet()) {
			logger.debug("Close previous message listener for device '" + device + "'");
			final AnelConnectorThread connectorThread = connectorThreads.get(device);
			connectorThread.setInterrupted();
		}
		// then wait for all of them to die
		for (String device : connectorThreads.keySet()) {
			final AnelConnectorThread connectorThread = connectorThreads.get(device);
			try {
				connectorThread.join();
			} catch (InterruptedException e) {
				logger.info("Previous message listener closing interrupted for device '" + device + "'", e);
			}
		}

		// clear map of previous threads because config changed
		connectorThreads.clear();

		// read new config
		try {
			refreshInterval = AnelConfigReader.readConfig(config, connectorThreads, bindingFacade);
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
			logger.debug("Starting message listener for device '" + device + "'");
			final AnelConnectorThread connectorThread = connectorThreads.get(device);
			connectorThread.start();
		}

		// start new thread that calls an initial update so the internal state
		// can be initialized
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
				}
				for (AnelConnectorThread connectorThread : connectorThreads.values()) {
					connectorThread.requestRefresh();
				}
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
