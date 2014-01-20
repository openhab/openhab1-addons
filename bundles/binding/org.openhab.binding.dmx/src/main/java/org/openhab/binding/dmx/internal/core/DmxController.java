/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.core;

import java.util.Dictionary;
import java.util.Timer;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.dmx.DmxConnection;
import org.openhab.binding.dmx.DmxService;
import org.openhab.binding.dmx.DmxStatusUpdateListener;
import org.openhab.binding.dmx.internal.action.FadeAction;
import org.openhab.binding.dmx.internal.action.MirrorAction;
import org.openhab.core.library.types.PercentType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default DMX Service implementation
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public class DmxController implements DmxService, ManagedService {

	private static Logger logger = LoggerFactory.getLogger(DmxController.class);

	private static int TRANSMIT_FREQUENCY_MS = 35;

	/** Thread in which the DMX transmitter is running **/
	private Timer transmitterTimer;

	private DmxTransmitter transmitter;

	private DmxConnection connection;

	private String connectionString = "localhost:9010";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start() throws Exception {

		logger.trace("Starting Dmx transmitter ...");
		transmitter = new DmxTransmitter(this);
		transmitterTimer = new Timer(true);
		transmitterTimer.schedule(transmitter, 0, TRANSMIT_FREQUENCY_MS);
		logger.trace("Dmx transmitter started.");

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() {

		if (transmitterTimer != null) {
			transmitterTimer.cancel();
		}
		transmitterTimer = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChannelValue(int channel, int value) {
		logger.trace("Setting channel {} to {}", channel, value);
		transmitter.getChannel(channel).setValue(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChannelValue(int channel) {

		int value = transmitter.getChannel(channel).getValue();
		logger.trace("Getting channel {} value: {}", channel, value);
		return value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disableChannel(int channel) {
		logger.trace("Disabling channel {}", channel);
		transmitter.getChannel(channel).switchOff();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void enableChannel(int channel) {
		logger.trace("Enabling channel {}", channel);
		transmitter.getChannel(channel).switchOn();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStatusListener(DmxStatusUpdateListener listener) {
		logger.trace("Registering listener for channel {}",
				listener.getChannel());
		transmitter.getUniverse().addStatusListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unregisterStatusListener(DmxStatusUpdateListener listener) {
		logger.trace("Unregistering listener for channel {}",
				listener.getChannel());
		transmitter.getUniverse().removeStatusListener(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fadeChannel(int channel, int fadeTime, int targetValue,
			int holdTime, boolean immediate) {

		FadeAction fade = new FadeAction(fadeTime, targetValue, holdTime);
		if (immediate) {
			logger.trace("Fading channel {} to {}", channel, targetValue);
			transmitter.getChannel(channel).setChannelAction(fade);
		} else {
			logger.trace("Adding channel fade on channel {} to {}", channel,
					targetValue);
			transmitter.getChannel(channel).addChannelAction(fade);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fadeChannels(int startChannel, int fadeTime,
			int[] targetValues, int holdTime, boolean immediate) {
		logger.trace("Fading channels {} to {}", startChannel, targetValues);
		int channelId = startChannel;
		for (int v : targetValues) {
			FadeAction fade = new FadeAction(fadeTime, v, holdTime);
			if (immediate) {
				transmitter.getChannel(channelId++).setChannelAction(fade);
			} else {
				transmitter.getChannel(channelId++).addChannelAction(fade);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void switchToNextAction(int channel) {
		logger.trace("Switching fade for channel {}", channel);
		transmitter.getChannel(channel).switchToNextAction();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void switchToNextAction(int startChannel, int numberOfChannels) {
		logger.trace("Switching fades for channel {} x{}", startChannel,
				numberOfChannels);
		for (int i = 0; i < numberOfChannels; i++) {
			transmitter.getChannel(startChannel + i).switchToNextAction();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void mirrorChannel(int sourceChannel, int mirrorChannel, int duration) {

		logger.trace("Mirroring channel {} onto {}", sourceChannel,
				mirrorChannel);
		transmitter.getChannel(mirrorChannel).setChannelAction(
				new MirrorAction(transmitter.getChannel(sourceChannel),
						duration));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void increaseChannel(int channelId, int increment) {
		logger.trace("Increasing channel {}", channelId);
		transmitter.getChannel(channelId).increaseChannel(increment);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decreaseChannel(int channelId, int decrement) {
		logger.trace("Decreasing channel {}", channelId);
		transmitter.getChannel(channelId).decreaseChannel(decrement);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void suspend(boolean suspend) {
		if (suspend) {
			transmitter.setSuspend(true);
			while (transmitter.isRunning()) {
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			transmitter.setSuspend(false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setChannelValue(int channelId, PercentType outputlevel) {
		transmitter.getChannel(channelId).setValue(outputlevel);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasChannelActions(int channelId) {
		return transmitter.getChannel(channelId).hasRunningActions();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DmxConnection getConnection() {
		if (connection == null) {
			logger.error("No DMX connection available. Please provide a bundle which implements the connection.");
			return null;
		}
		if (connection.isClosed()) {
			try {
				connection.open(connectionString);
			} catch (Throwable t) {
				logger.error("Error opening DMX connection.", t);
				return null;
			}
		}
		return connection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setConnection(DmxConnection conn) {
		if (connection != null) {
			connection.close();
		}
		connection = conn;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unsetConnection(DmxConnection conn) {
		if (conn != null) {
			conn.close();
		}
		connection = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {
			String configuredConnection = (String) config.get("connection");
			if (StringUtils.isNotBlank(configuredConnection)) {
				connectionString = configuredConnection;
				logger.debug("Setting connection from config: {}",
						connectionString);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void suspendChannel(int channel) {
		transmitter.getChannel(channel).suspend();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addChannelResume(int channel) {
		transmitter.getChannel(channel).addResumeAction();
	}

}
