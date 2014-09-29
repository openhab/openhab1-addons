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
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
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
 * anel:udpReceivePort=7777
 * 
 * # UDP send port (optional, defaults to 75)
 * anel:udpSendPort=7775
 * 
 * # IP or network address (optional, defaults to 'NET-CONTROL')
 * anel:ipAddress=anel1
 * 
 * # User name (optional, defaults to 'user7')
 * anel:user=user7
 * 
 * # Password (optional, defaults to 'anel')
 * anel:password=anel
 * 
 * # Refresh interval in seconds (optional, defaults to '60', disable with '0')
 * #refreshInterval=60
 * </pre>
 * 
 * Example items:
 * 
 * <pre>
 * Switch f1 { anel="F1" }
 * Switch io1 { anel="IO1" }
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
		 * Get the command type that is associated with the given item name.
		 * 
		 * @param itemName
		 *            An item name.
		 * @return The command type that is associated with the given item name.
		 */
		AnelCommandType getCommandTypeForItemName(String itemName);

		/**
		 * The event publisher so that the connector that can send events.
		 * 
		 * @return The event publisher that was set for the binding.
		 */
		EventPublisher getEventPublisher();
	}

	private static final Logger logger = LoggerFactory.getLogger(AnelBinding.class);

	// default connection settings
	private final static String DEFAULT_HOST = "net-control";
	private final static String DEFAULT_USER = "user7";
	private final static String DEFAULT_PASSWORD = "anel";
	private final static int DEFAULT_UDP_RECEIVE_PORT = 77;
	private final static int DEFAULT_UDP_SEND_PORT = 75;

	/**
	 * The refresh interval which is used to poll values from the Anel server
	 * (optional, defaults to 300000ms).
	 */
	private long refreshInterval = 300000;

	/** Thread to handle messages from devices */
	private AnelConnectorThread connectorThread = null;

	private final IInternalAnelBinding bindingFacade = new IInternalAnelBinding() {

		@Override
		public Collection<String> getItemNamesForCommandType(AnelCommandType cmd) {
			return AnelBinding.this.getItemNamesForCommandType(cmd);
		}

		@Override
		public EventPublisher getEventPublisher() {
			return eventPublisher;
		}

		@Override
		public AnelCommandType getCommandTypeForItemName(String itemName) {
			return AnelBinding.this.getCommandTypeForItemName(itemName);
		}
	};

	public void activate() {
		// don't do anything because we are still waiting for the update-call to
		// get config
	}

	public void deactivate() {
		// deallocate resources here that are no longer needed and
		// should be reset when activating this binding again
		if (connectorThread != null) {
			connectorThread.setInterrupted();
		}
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
		 * if the last update is more than <refreshInterval> ago, poll the
		 * device for its state.
		 */
		if (connectorThread != null && refreshInterval > 0) {
			connectorThread.requestRefresh();
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
		final AnelCommandType cmd = getCommandTypeForItemName(itemName);
		if (cmd == null) {
			logger.debug("Invalid command for item name: '" + itemName + "'");
			return;
		}
		if (connectorThread == null) {
			logger.debug("Connection to Anel device not yet initialized, no device updates sent.");
		}

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
		String host = DEFAULT_HOST;
		String user = DEFAULT_USER;
		String password = DEFAULT_PASSWORD;
		int udpReceivePort = DEFAULT_UDP_RECEIVE_PORT;
		int udpSendPort = DEFAULT_UDP_SEND_PORT;

		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			final String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Integer.parseInt(refreshIntervalString);
			}
			final String receivePortString = (String) config.get("udpReceivePort");
			if (StringUtils.isNotBlank(receivePortString)) {
				udpReceivePort = Integer.parseInt(receivePortString);
			}
			final String sendPortString = (String) config.get("udpSendPort");
			if (StringUtils.isNotBlank(sendPortString)) {
				udpSendPort = Integer.parseInt(sendPortString);
			}
			final String userString = (String) config.get("user");
			if (StringUtils.isNotBlank(userString)) {
				user = userString;
			}
			final String passwordString = (String) config.get("password");
			if (StringUtils.isNotBlank(passwordString)) {
				password = passwordString;
			}
			final String hostString = (String) config.get("host");
			if (StringUtils.isNotBlank(hostString)) {
				host = hostString;
			}
			logger.debug("Configuration read: host='" + host + "', sendUdpPort=" + udpSendPort + ", receiveUdpPort="
					+ udpReceivePort + ", user='" + user + "', pwd='" + password + "', refresh="
					+ (refreshInterval / 1000) + "s.");

			// used in template but not in example swgon binding...
			setProperlyConfigured(true);
		}

		if (connectorThread != null) {

			logger.debug("Close previous message listener");

			connectorThread.setInterrupted();
			try {
				connectorThread.join();
			} catch (InterruptedException e) {
				logger.info("Previous message listener closing interrupted", e);
			}
		}

		connectorThread = new AnelConnectorThread(host, udpReceivePort, udpSendPort, user, password, bindingFacade);
		connectorThread.start();

		// start new thread that calls an initial update so the internal state
		// can be initialized
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
				}
				if (connectorThread != null && !connectorThread.isInterrupted())
					connectorThread.requestRefresh();
			}
		}).start();
	}

	private Collection<String> getItemNamesForCommandType(AnelCommandType cmd) {
		final Set<String> itemNames = new HashSet<String>();
		for (AnelBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				final AnelCommandType commandType = provider.getCommandType(itemName);
				if (commandType.equals(cmd)) {
					itemNames.add(itemName);
				}
			}
		}
		return itemNames;
	}

	private AnelCommandType getCommandTypeForItemName(String itemName) {
		if (itemName == null || itemName.isEmpty())
			return null;
		for (AnelBindingProvider provider : providers) {
			for (String providerItemName : provider.getItemNames()) {
				if (itemName.equals(providerItemName)) {
					return provider.getCommandType(itemName);
				}
			}
		}
		return null;
	}
}
