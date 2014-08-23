/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.comfoair.internal;

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.comfoair.ComfoAirBindingProvider;
import org.openhab.binding.comfoair.datatypes.ComfoAirDataType;
import org.openhab.binding.comfoair.handling.ComfoAirCommand;
import org.openhab.binding.comfoair.handling.ComfoAirCommandType;
import org.openhab.binding.comfoair.handling.ComfoAirConnector;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An active binding which requests a given URL frequently.
 * 
 * @author Holger Hees
 * @since 1.3.0
 */
public class ComfoAirBinding extends AbstractActiveBinding<ComfoAirBindingProvider> implements ManagedService {

	static final Logger logger = LoggerFactory.getLogger(ComfoAirBinding.class);
	
	/**
	 * The interval to find new refresh candidates (defaults to 60000
	 * milliseconds).
	 */
	private long refreshInterval = 60000L;
	private String port;

	private ComfoAirConnector connector;
	
	
	/**
	 * @{inheritDoc
	 */
	public void activate() {
	}

	/**
	 * @{inheritDoc
	 */
	public void deactivate() {
		for (ComfoAirBindingProvider provider : providers) {
			provider.removeBindingChangeListener(this);
		}

		providers.clear();

		if (connector != null) {
			connector.close();
		}
	}
	
	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "ComfoAir Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		Set<String> usedKeys = new HashSet<String>();

		for (ComfoAirBindingProvider provider : providers) {
			usedKeys.addAll(provider.getConfiguredKeys());
		}

		for (ComfoAirBindingProvider provider : providers) {
			String eventType = provider.getConfiguredKeyForItem(itemName);
			ComfoAirCommand changeCommand = 
				ComfoAirCommandType.getChangeCommand(eventType, (DecimalType) command);

			sendCommand(changeCommand);

			Collection<ComfoAirCommand> affectedReadCommands = 
				ComfoAirCommandType.getAffectedReadCommands(eventType, usedKeys);

			if (affectedReadCommands.size() > 0) {
				// refresh 3 seconds later all affected items
				Thread updateThread = new AffectedItemsUpdateThread(affectedReadCommands);
				updateThread.start();
			}
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void execute() {
		for (ComfoAirBindingProvider provider : providers) {
			Collection<ComfoAirCommand> commands = 
				ComfoAirCommandType.getReadCommandsByEventTypes(provider.getConfiguredKeys());

			for (ComfoAirCommand command : commands) {
				sendCommand(command);
			}
		}
	}

	/**
	 * send a command and send additional command which are affected by the
	 * first command
	 * 
	 * @param command
	 */
	private void sendCommand(ComfoAirCommand command) {
		
		int[] response = connector.sendCommand(command);

		if (response == null) {
			return;
		}

		List<ComfoAirCommandType> commandTypes = 
			ComfoAirCommandType.getCommandTypesByReplyCmd(command.getReplyCmd());

		for (ComfoAirCommandType commandType : commandTypes) {
			ComfoAirDataType dataType = commandType.getDataType();
			State value = dataType.convertToState(response, commandType);

			if (value == null) {
				logger.error("Unexpected value for DATA: " + ComfoAirConnector.dumpData(response));
			} else {
				for (ComfoAirBindingProvider provider : providers) {
					List<String> items = provider.getItemNamesForCommandKey(commandType.getKey());
					for (String item : items) {
						eventPublisher.postUpdate(item, value);
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {

		if (config != null) {
			String newPort = (String) config.get("port"); //$NON-NLS-1$
			if (StringUtils.isNotBlank(newPort) && !newPort.equals(port)) {
				// only do something if the newPort has changed
				port = newPort;

				String refreshIntervalString = (String) config.get("refresh");
				if (StringUtils.isNotBlank(refreshIntervalString)) {
					refreshInterval = Long.parseLong(refreshIntervalString);
				}

				if (connector != null) {
					connector.close();
				}

				connector = new ComfoAirConnector();
				try {
					connector.open(port);
				} catch (InitializationException e) {
					logger.error(e.getMessage());
				}

				setProperlyConfigured(true);
			}
		}
	}

	
	private class AffectedItemsUpdateThread extends Thread {

		private Collection<ComfoAirCommand> affectedReadCommands;

		public AffectedItemsUpdateThread(Collection<ComfoAirCommand> affectedReadCommands) {
			this.affectedReadCommands = affectedReadCommands;
		}

		public void run() {
			try {
				sleep(3000);
				for (ComfoAirCommand readCommand : this.affectedReadCommands) {
					sendCommand(readCommand);
				}
			} catch (InterruptedException e) {
				// nothing to do ...
			}
		}
	}
	
}
