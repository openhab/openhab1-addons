/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.config;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.dmx.DmxBindingProvider;
import org.openhab.binding.dmx.DmxService;
import org.openhab.binding.dmx.DmxStatusUpdateListener;
import org.openhab.binding.dmx.internal.cmd.DmxCommand;
import org.openhab.binding.dmx.internal.cmd.DmxFadeCommand;
import org.openhab.binding.dmx.internal.cmd.DmxSuspendingFadeCommand;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DMX specific binding configuration for items. Used to link items to their DMX
 * config.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public abstract class DmxItem implements BindingConfig, DmxStatusUpdateListener {

	protected static final Logger logger = LoggerFactory
			.getLogger(DmxItem.class);

	private static final Pattern DMX_CHANNEL_PATTERN = Pattern
			.compile("[\\d,].*(:\\d{3,6}){0,1}");

	private static final Pattern DMX_CMD_PATTERN = Pattern
			.compile("[A-Z0-9]+\\[[A-Z0-9,:\\-/\\|]+\\]");

	private static final Pattern DMX_CONFIG_PATTERN = Pattern
			.compile("([ ]*[A-Z0-9]+\\[[A-Z0-9,:\\-/\\|]+\\][ ,]*)+");
	
	/** Minimum status update delay in ms */
	public static int MIN_UPDATE_DELAY = 100;

	/** DMX channel numbers (512 max) */
	protected int[] channels;

	/** Minimum number of ms between status updates */
	protected int updateDelay;

	/** Binding provider */
	private DmxBindingProvider bindingProvider;

	/** Item name */
	protected String name;

	/** Time when last status update was sent out */
	protected long lastUpdated;

	/** Map holding custom DMX commands which override the standard commands **/
	protected Map<String, DmxCommand> customCommands = new HashMap<String, DmxCommand>();

	/** Last known state */
	protected State currentState;

	/**
	 * Create new item based on the provided configuration string.
	 * 
	 * @param itemName
	 *            name for the item
	 * @param configString
	 *            item configuration string
	 * @param dmxBindingProvider
	 *            BindingProvider which created this item
	 * @throws BindingConfigParseException
	 *             when the configstring could not be parsed.
	 */
	public DmxItem(String itemName, String configString,
			DmxBindingProvider dmxBindingProvider)
			throws BindingConfigParseException {

		name = itemName;
		bindingProvider = dmxBindingProvider;

		Matcher configMatcher = DMX_CONFIG_PATTERN.matcher(configString.trim());
		if (!configMatcher.matches()) {
			throw new BindingConfigParseException(
					"DMX Configuration must match pattern: "
							+ configMatcher.pattern().toString());
		}
		
		try {

			Matcher cmdMatcher = DMX_CMD_PATTERN.matcher(configString.trim());
			while (cmdMatcher.find()) {

				String cmdString = cmdMatcher.group();
				String cmd = cmdString.substring(0, cmdString.indexOf('['));
				String cmdValue = cmdString.substring(cmdString.indexOf('[') + 1,
						cmdString.lastIndexOf(']'));

				if (cmd.equals("CHANNEL")) {
					parseChannelConfig(cmdValue);

				} else {

					String dmxCommandType = cmdValue.split("\\|")[0];
					if (dmxCommandType.equals(DmxCommand.types.FADE.toString())) {
						DmxCommand dmxCommand = new DmxFadeCommand(this,
								cmdValue.substring(cmdValue.indexOf("|") + 1));
						customCommands.put(cmd, dmxCommand);
					} else if (dmxCommandType.equals(DmxCommand.types.SFADE
							.toString())) {
						DmxCommand dmxCommand = new DmxSuspendingFadeCommand(
								this,
								cmdValue.substring(cmdValue.indexOf("|") + 1));
						customCommands.put(cmd, dmxCommand);
					} else {
						throw new BindingConfigParseException(
								"Unsupported DMX command: " + dmxCommandType);
					}
				}

			}

			if (channels == null) {
				throw new BindingConfigParseException("No valid channel config found in " + configString);
			}
			
		} catch (Exception e) {
			logger.error("Invalid DMX configuration for item {} : {}",
					itemName, e.getMessage());
			throw new BindingConfigParseException(e.getMessage());
		}
		
	}

	/**
	 * Extract channel id information from channel configuration string.
	 * 
	 * @param channelString
	 *            string to parse
	 * @throws BindingConfigParseException
	 *             if parsing failed.
	 */
	private void parseChannelConfig(String channelString)
			throws BindingConfigParseException {

		Matcher channelConfigMatcher = DMX_CHANNEL_PATTERN
				.matcher(channelString);

		if (!channelConfigMatcher.matches()) {
			throw new BindingConfigParseException(
					"DMX channel configuration : " + channelString
							+ " doesn't match " + DMX_CHANNEL_PATTERN);
		}

		String[] values = channelString.split(":");

		// parse channel number & footprint
		if (values[0].indexOf('/') == -1) {
			// no channel width specified
			String[] tmp = values[0].split(",");
			if (tmp.length == 1) {
				int footprint = getFootPrint();
				channels = new int[footprint];
				int start = parseChannelNumber(tmp[0]);
				for (int i = 0; i < footprint; i++) {
					channels[i] = start + i;
				}
			} else {
				channels = new int[tmp.length];
				for (int i = 0; i < tmp.length; i++) {
					channels[i] = parseChannelNumber(tmp[i]);
					if (channels[i] < 1 || channels[i] > 512) {

					}

				}
			}

		} else {
			// channel width specified
			String[] tmp = values[0].split("/");
			int startChannel = parseChannelNumber(tmp[0]);
			int channelWidth = Integer.parseInt(tmp[1]);
			channels = new int[channelWidth];
			for (int i = 0; i < channelWidth; i++) {
				channels[i] = startChannel + i;
			}
		}

		// parse update delay
		if (values.length == 2) {
			updateDelay = Integer.parseInt(values[1]);
			if (updateDelay < MIN_UPDATE_DELAY) {
				updateDelay = 0;
			}
		}

		logger.debug("Linked item {} to channels {}", name, channels);
	}

	private int parseChannelNumber(String input)
			throws BindingConfigParseException {
		try {
			int channel = Integer.parseInt(input);
			if (channel < 1 || channel > 512) {
				throw new BindingConfigParseException(
						"DMX channel configuration : " + input
								+ " is not a valid dmx channel (1-512)");
			}
			return channel;
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException(
					"DMX channel configuration : " + input
							+ " is not a valid dmx channel (1-512)");
		}
	}

	/**
	 * Try to execute the provided openHAB command.
	 * 
	 * @param service
	 *            DMXservice.
	 * @param command
	 *            openHAB command.
	 */
	public abstract void processCommand(DmxService service, Command command);

	/**
	 * Check if the current item wants to be notified of state changes.
	 * 
	 * @return true if status updates are needed.
	 */
	public abstract boolean isStatusListener();

	/**
	 * Check if an openHAB command has been overridden by a DMX command.
	 * 
	 * @param cmd
	 *            to check
	 * @return true if there is a DMX command available instead.
	 */
	protected final boolean isRedefinedByCustomCommand(Command cmd) {
		return customCommands.containsKey(cmd.toString());
	}

	/**
	 * Publish the new state to the event bus, if it was changed since the last
	 * known value.
	 * 
	 * @param state
	 *            new state.
	 */
	protected void publishState(State state) {

		lastUpdated = System.currentTimeMillis();

		if (bindingProvider == null) {
			return;
		}
		if (currentState != null && currentState.equals(state)) {
			return;
		}
		bindingProvider.postUpdate(name, state);
		currentState = state;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChannel() {
		return channels[0];
	}

	/**
	 * @return all DMX channels bound to this item
	 */
	public int[] getChannels() {
		return channels;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getFootPrint() {
		// default footprint
		return 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getUpdateDelay() {
		return updateDelay;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public abstract void processStatusUpdate(int[] channelValues);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getLastUpdateTime() {
		return lastUpdated;
	}

	/**
	 * Get the channel for the specified index.
	 * 
	 * @param index
	 * @return channel number
	 */
	public int getChannel(int index) {
		return channels[index];
	}

}