/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator;

import java.util.TimerTask;

import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.ProviderItemIterator.ProviderItemIteratorCallback;
import org.openhab.binding.homematic.internal.communicator.client.BinRpcClient;
import org.openhab.binding.homematic.internal.communicator.client.CcuClient;
import org.openhab.binding.homematic.internal.communicator.client.HomegearClient;
import org.openhab.binding.homematic.internal.communicator.client.HomematicClientException;
import org.openhab.binding.homematic.internal.communicator.client.interfaces.HomematicClient;
import org.openhab.binding.homematic.internal.communicator.client.interfaces.RpcClient;
import org.openhab.binding.homematic.internal.communicator.server.BinRpcCallbackServer;
import org.openhab.binding.homematic.internal.config.BindingAction;
import org.openhab.binding.homematic.internal.config.binding.ActionConfig;
import org.openhab.binding.homematic.internal.config.binding.DatapointConfig;
import org.openhab.binding.homematic.internal.config.binding.HomematicBindingConfig;
import org.openhab.binding.homematic.internal.config.binding.ProgramConfig;
import org.openhab.binding.homematic.internal.config.binding.VariableConfig;
import org.openhab.binding.homematic.internal.converter.state.Converter;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmInterface;
import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.openhab.binding.homematic.internal.util.DelayedExecutor;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The heart of the Homematic binding, this class handles the complete
 * communication between a Homematic server and openHAB.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class HomematicCommunicator implements HomematicCallbackReceiver {
	private static final Logger logger = LoggerFactory.getLogger(HomematicCommunicator.class);

	private HomematicContext context = HomematicContext.getInstance();
	private DelayedExecutor delayedExecutor = new DelayedExecutor();

	private int newDevicesCounter;

	private HomematicCallbackServer homematicCallbackServer;
	private HomematicClient homematicClient;
	private ItemDisabler itemDisabler;

	private long lastEventTime = System.currentTimeMillis();

	/**
	 * Starts the communicator and initializes everything.
	 */
	public void start() {
		if (homematicCallbackServer == null) {
			logger.info("Starting Homematic communicator");
			try {
				homematicCallbackServer = new BinRpcCallbackServer(this);

				itemDisabler = new ItemDisabler();
				itemDisabler.start();
				newDevicesCounter = 0;

				RpcClient rpcClient = new BinRpcClient();
				context.setServerId(rpcClient.getServerId(HmInterface.RF));
				logger.info("Homematic {}", context.getServerId());

				homematicClient = context.getServerId().isHomegear() ? new HomegearClient(rpcClient) : new CcuClient(
						rpcClient);

				context.setHomematicClient(homematicClient);
				homematicClient.start();

				context.getStateHolder().init();
				context.getStateHolder().loadDatapoints();
				context.getStateHolder().loadVariables();

				homematicCallbackServer.start();
				homematicClient.registerCallback();

				scheduleFirstRefresh();
			} catch (Exception e) {
				logger.error("Could not start Homematic communicator: " + e.getMessage(), e);
				stop();
			}
		}
	}

	/**
	 * Schedule refresh after one minute in case there is a value change between
	 * initial load and server startup
	 */
	private void scheduleFirstRefresh() {
		logger.info("Scheduling one datapoint reload job in 60 seconds");
		delayedExecutor.schedule(new TimerTask() {

			@Override
			public void run() {
				logger.debug("Initial Homematic datapoints reload");
				context.getStateHolder().reloadDatapoints();
			}
		}, 60000);
	}

	/**
	 * Stops the communicator.
	 */
	public void stop() {
		if (homematicCallbackServer != null) {
			logger.info("Shutting down Homematic communicator");
			try {
				delayedExecutor.cancel();
				homematicCallbackServer.shutdown();
				if (homematicClient != null) {
					try {
						homematicClient.releaseCallback();
					} catch (HomematicClientException e) {
						// ignore
					}
					try {
						homematicClient.shutdown();
					} catch (HomematicClientException e) {
						// ignore
					}
				}
				if (itemDisabler != null) {
					itemDisabler.stop();
				}
				if (context.getStateHolder() != null) {
					context.getStateHolder().destroy();
				}
			} finally {
				homematicCallbackServer = null;
			}
		}
	}

	/**
	 * Receives a message from the Homematic server and publishes the state to
	 * openHAB.
	 */
	@Override
	public void event(String interfaceId, String addressWithChannel, String parameter, Object value) {
		boolean isVariable = "".equals(addressWithChannel);

		HomematicBindingConfig bindingConfig = null;
		if (isVariable) {
			bindingConfig = new VariableConfig(parameter);
		} else {
			bindingConfig = new DatapointConfig(HmInterface.parse(interfaceId), addressWithChannel, parameter);
		}

		String className = value == null ? "Unknown" : value.getClass().getSimpleName();
		logger.debug("Received new ({}) value '{}' for {}", className, value, bindingConfig);
		lastEventTime = System.currentTimeMillis();

		final Event event = new Event(bindingConfig, value);

		if (context.getStateHolder().isDatapointReloadInProgress() && !isVariable) {
			context.getStateHolder().addToRefreshCache(event.getBindingConfig(), event.getNewValue());
		}

		event.setHmValueItem(context.getStateHolder().getState(event.getBindingConfig()));
		if (event.getHmValueItem() != null) {
			event.getHmValueItem().setValue(event.getNewValue());

			new ProviderItemIterator().iterate(event.getBindingConfig(), new ProviderItemIteratorCallback() {

				@Override
				public void next(HomematicBindingConfig providerBindingConfig, Item item, Converter<?> converter) {
					State state = converter.convertFromBinding(event.getHmValueItem());
					context.getEventPublisher().postUpdate(item.getName(), state);
					if (state == OnOffType.ON) {
						executeBindingAction(providerBindingConfig);
						if (event.isPressValueItem()) {
							itemDisabler.add(providerBindingConfig);
						}
					}
				}
			});

		} else {
			logger.warn("Can't find {}, value is not published to openHAB!", event.getBindingConfig());
		}
	}

	/**
	 * Called on startup or when some binding has changed, for example if a item
	 * file is reloaded. Publishes the current States to openHAB.
	 */
	public void publishChangedItemToOpenhab(Item item, HomematicBindingConfig bindingConfig) {
		HmValueItem hmValueItem = context.getStateHolder().getState(bindingConfig);
		if (hmValueItem != null) {
			Converter<?> converter = context.getConverterFactory().createConverter(item, bindingConfig);
			if (converter != null) {
				State state = converter.convertFromBinding(hmValueItem);
				context.getEventPublisher().postUpdate(item.getName(), state);
			}
		} else if (bindingConfig instanceof ProgramConfig || bindingConfig instanceof ActionConfig) {
			context.getEventPublisher().postUpdate(item.getName(), OnOffType.OFF);
		} else {
			logger.warn("Can't find {}, value is not published to openHAB!", bindingConfig);
		}
	}

	/**
	 * Receives a update from openHAB and sends it to the Homematic server.
	 */
	public void receiveUpdate(Item item, State newState, HomematicBindingConfig bindingConfig) {
		logger.debug("Received update {} for item {}", newState, item.getName());
		Event event = new Event(item, newState, bindingConfig);
		receiveType(event);
	}

	/**
	 * Receives a command from openHAB and sends it to the Homematic server.
	 */
	public void receiveCommand(Item item, Command command, HomematicBindingConfig bindingConfig) {
		logger.debug("Received command {} for item {}", command, item.getName());
		Event event = new Event(item, command, bindingConfig);
		receiveType(event);
	}

	/**
	 * Receives updates/commands from openHAB and sends messages to the
	 * Homematic server.
	 */
	public void receiveType(Event event) {
		if (event.isProgram()) {
			if (event.isOnType()) {
				executeProgram(event);
			}
		} else if (event.isAction()) {
			if (event.isOnType()) {
				executeBindingAction(event.getBindingConfig());
				itemDisabler.add(event.getBindingConfig());
			}
		} else {
			event.setHmValueItem(context.getStateHolder().getState(event.getBindingConfig()));

			if (event.getHmValueItem() == null) {
				logger.warn("Can't find {}, value is not published to Homematic server!", event.getBindingConfig());
			} else {
				try {
					if (event.isStopLevelDatapoint()) {
						homematicClient.setDatapointValue((HmDatapoint) event.getHmValueItem(), "STOP", true);
					} else {
						Converter<?> converter = context.getConverterFactory().createConverter(event.getItem(),
								event.getBindingConfig());
						if (converter != null) {
							if (!event.isStopLevelDatapoint()) {
								event.setNewValue(converter.convertToBinding(event.getType(), event.getHmValueItem()));
							}
							publishToHomematicServer(event);
							publishToAllBindings(event);

							if (event.isOnType()) {
								executeBindingAction(event.getBindingConfig());
								if (event.isPressValueItem()) {
									itemDisabler.add(event.getBindingConfig());
								}
							}
						}
					}
				} catch (Exception ex) {
					logger.error(ex.getMessage(), ex);
					context.getStateHolder().reloadDatapoints();
					context.getStateHolder().reloadVariables();
				}
			}
		}
	}

	/**
	 * Sends the event to the Homematic server.
	 */
	private void publishToHomematicServer(Event event) throws HomematicClientException {
		if (event.isPressValueItem()) {
			logger.debug("PRESS_* items are not published to the Homematic server: {}", event.getBindingConfig());
		}

		else if (!event.getHmValueItem().isWriteable()) {
			logger.warn("Datapoint/Variable is not writeable, item is not published to the Homematic server: {}",
					event.getBindingConfig());
		}

		else if (event.isNewValueEqual()) {
			logger.debug("Value '{}' equals cached Homematic server value '{}' and forceUpdate is false, ignoring {}",
					event.getHmValueItem().getValue(), event.getNewValue(), event.getBindingConfig());
		}

		else {
			if (event.isVariable()) {
				homematicClient.setVariable(event.getHmValueItem(), event.getNewValue());
			} else {
				homematicClient.setDatapointValue((HmDatapoint) event.getHmValueItem(), event.getHmValueItem()
						.getName(), event.getNewValue());
			}
			event.getHmValueItem().setValue(event.getNewValue());
		}
	}

	/**
	 * Publishes the event to all items bound to the same Homematic item.
	 */
	private void publishToAllBindings(final Event event) {
		new ProviderItemIterator().iterate(event.getBindingConfig(), new ProviderItemIteratorCallback() {

			@Override
			public void next(HomematicBindingConfig providerBindingConfig, Item item, Converter<?> converter) {
				if (!item.getName().equals(event.getItem().getName())) {
					if (event.isCommand()) {
						context.getEventPublisher().postCommand(item.getName(), (Command) event.getType());
					} else {
						State state = converter.convertFromBinding(event.getHmValueItem());
						context.getEventPublisher().postUpdate(item.getName(), state);
					}
				}
			}
		});
	}

	/**
	 * Executes a program on the Homematic server.
	 */
	private void executeProgram(Event event) {
		try {
			homematicClient.executeProgram(((ProgramConfig) event.getBindingConfig()).getName());
		} catch (HomematicClientException ex) {
			logger.error(ex.getMessage(), ex);
		} finally {
			itemDisabler.add(event.getBindingConfig());
		}
	}

	/**
	 * If a binding action is configured, execute it.
	 */
	private void executeBindingAction(HomematicBindingConfig bindingConfig) {
		if (bindingConfig.getAction() != null) {
			if (bindingConfig.getAction() == BindingAction.RELOAD_VARIABLES) {
				context.getStateHolder().reloadVariables();
			} else if (bindingConfig.getAction() == BindingAction.RELOAD_DATAPOINTS) {
				context.getStateHolder().reloadDatapoints();
			} else if (bindingConfig.getAction() == BindingAction.RELOAD_RSSI) {
				context.getStateHolder().reloadRssi();
			} else {
				logger.warn("Unknown action {}", bindingConfig.getAction());
			}
		}
	}

	/**
	 * Called when the Homematic server detects a new device, datapoints are
	 * refreshed to have the new device in the cache.
	 */
	@Override
	public void newDevices(String interfaceId, Object[] deviceDescriptions) {
		if (newDevicesCounter < 3) {
			newDevicesCounter++;
		}

		// prevent from duplicate loading at startup
		if (newDevicesCounter > 2) {
			logger.info("New device(s) detected, refreshing datapoints");
			context.getStateHolder().reloadDatapoints();
		}
	}

	/**
	 * Returns the timestamp from the last Homematic server event.
	 */
	public long getLastEventTime() {
		return lastEventTime;
	}

}
