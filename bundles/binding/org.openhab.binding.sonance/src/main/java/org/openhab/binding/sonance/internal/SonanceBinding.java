/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sonance.internal;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.sonance.SonanceBindingProvider;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Sonance Binding communicates with Sonance Amplifiers like the DSP 2-150,
 * DSP 8-130 and the DSP 2-750
 * 
 * @author Laurens Van Acker
 * @since 1.8.0
 */
public class SonanceBinding extends
		AbstractActiveBinding<SonanceBindingProvider> {
	private Map<String, Socket> socketCache = new HashMap<String, Socket>();
	private Map<String, DataOutputStream> outputStreamCache = new HashMap<String, DataOutputStream>();
	private Map<String, BufferedReader> bufferedReaderCache = new HashMap<String, BufferedReader>();
	private Map<String, ReentrantLock> volumeLocks = new HashMap<String, ReentrantLock>();
	private static Pattern volumePattern = Pattern
			.compile(".*Vol=(-?\\d{1,2}).*");

	private static final Logger logger = LoggerFactory
			.getLogger(SonanceBinding.class);

	/**
	 * the refresh interval which is used to poll values from the Sonance server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	/**
	 * Nothing happens in the constructor
	 */
	public SonanceBinding() {
	}

	/**
	 * Called by the SCR to activate the component with the refresh Interval. To
	 * override the default refresh interval one has to add a parameter to
	 * openhab.cfg like Sonance:refresh=<intervalInMs>.
	 * 
	 * @param bundleContext
	 *            BundleContext of the Bundle that defines this component
	 * @param configuration
	 *            Configuration properties for this component obtained from the
	 *            ConfigAdmin service
	 */
	public void activate(final BundleContext bundleContext,
			final Map<String, Object> configuration) {
		String refreshIntervalString = (String) configuration.get("refresh");
		if (StringUtils.isNotBlank(refreshIntervalString)) {
			refreshInterval = Long.parseLong(refreshIntervalString);
		}

		setProperlyConfigured(true);
	}

	/**
	 * Deallocate socket connection, output stream and buffered reader caches
	 * 
	 * @param reason
	 *            Reason code for the deactivation:<br>
	 *            <ul>
	 *            <li>0 – Unspecified
	 *            <li>1 – The component was disabled
	 *            <li>2 – A reference became unsatisfied
	 *            <li>3 – A configuration was changed
	 *            <li>4 – A configuration was deleted
	 *            <li>5 – The component was disposed
	 *            <li>6 – The bundle was stopped
	 *            </ul>
	 */
	public void deactivate(final int reason) {
		socketCache.clear();
		outputStreamCache.clear();
		bufferedReaderCache.clear();
	}

	/*
	 * @see org.openhab.core.binding.AbstractActiveBinding#getRefreshInterval()
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/*
	 * @see org.openhab.core.binding.AbstractActiveBinding#getName()
	 */
	@Override
	protected String getName() {
		return "Sonance Refresh Service";
	}

	/*
	 * @see org.openhab.core.binding.AbstractActiveBinding#execute()
	 */
	@Override
	protected void execute() {
		if (!bindingsExist()) {
			logger.debug("There is no existing Sonance binding configuration => refresh cycle aborted!");
			return;
		}

		logger.info("Refreshing all items");

		List<String> offlineEndPoints = new ArrayList<String>();

		for (SonanceBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				String group = provider.getGroup(itemName);
				String ip = provider.getIP(itemName);
				int port = provider.getPort(itemName);

				String key = ip + ":" + port;
				if (!offlineEndPoints.contains(key)) {
					try {
						if (!socketCache.containsKey(key)) {
							socketCache.put(key, new Socket(ip, port));
							outputStreamCache.put(key, new DataOutputStream(
									socketCache.get(key).getOutputStream()));
							bufferedReaderCache.put(key, new BufferedReader(
									new InputStreamReader(socketCache.get(key)
											.getInputStream())));
							logger.debug("New socket created ({}:{})", ip, port);
						}

						if (provider.isMute(itemName)) {
							sendMuteCommand(itemName, SonanceConsts.MUTE_QUERY
									+ group, outputStreamCache.get(key),
									bufferedReaderCache.get(key));
						} else if (provider.isVolume(itemName)) {
							sendVolumeCommand(itemName,
									SonanceConsts.VOLUME_QUERY + group,
									outputStreamCache.get(key),
									bufferedReaderCache.get(key));
						}// } else if (provider.isPower(itemName)) {
							// sendPowerCommand(itemName,
							// SonanceConsts.POWER_QUERY,
							// outputStreamCache.get(key),
							// bufferedReaderCache.get(key));
						// }
					} catch (UnknownHostException e) {
						logger.error(
								"UnknownHostException occured when connecting to amplifier {}:{}.",
								ip, port);
					} catch (IOException e) {
						logger.debug(
								"Amplifier ({},{}) is offline, status can't be updated at this moment.",
								ip, port);
						try {
							socketCache.get(key).close();
						} catch (Exception ex) {
						}
						socketCache.remove(key);
						outputStreamCache.remove(key);
						bufferedReaderCache.remove(key);
						offlineEndPoints.add(key); // Stop trying to fetch other
													// values from this end
													// point until next execute
													// cycle
					}
				}
			}
		}
	}

	/*
	 * @see
	 * org.openhab.core.binding.AbstractActiveBinding#bindingChanged(org.openhab
	 * .core.binding.BindingProvider, java.lang.String)
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		super.bindingChanged(provider, itemName);

		for (Map.Entry<String, Socket> entry : socketCache.entrySet())
			try {
				entry.getValue().close();
			} catch (IOException e) {
				logger.error("Can't close a socket when binding changed.");
			}

		// Cleanup all sockets
		socketCache.clear();
		outputStreamCache.clear();
		bufferedReaderCache.clear();
	}

	/*
	 * @see
	 * org.openhab.core.binding.AbstractBinding#internalReceiveCommand(java.
	 * lang.String, org.openhab.core.types.Command)
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.info("Command received ({}, {})", itemName, command);

		SonanceBindingProvider provider = findFirstMatchingBindingProvider(itemName);
		String group = provider.getGroup(itemName);
		String ip = provider.getIP(itemName);
		int port = provider.getPort(itemName);

		Socket s = null;
		try {
			s = new Socket(ip, port);
			DataOutputStream outToServer = new DataOutputStream(
					s.getOutputStream());
			BufferedReader i = new BufferedReader(new InputStreamReader(
					s.getInputStream()));

			if (provider.isMute(itemName)) {
				if (command.equals(OnOffType.OFF)) {
					sendMuteCommand(itemName, SonanceConsts.MUTE_ON + group,
							outToServer, i);
				} else if (command.equals(OnOffType.ON)) {
					sendMuteCommand(itemName, SonanceConsts.MUTE_OFF + group,
							outToServer, i);
				} else {
					logger.error(
							"I don't know what to do with the command \"{}\"",
							command);
				}
			} else if (provider.isPower(itemName)) {
				if (command.equals(OnOffType.OFF)) {
					sendPowerCommand(itemName, SonanceConsts.POWER_OFF,
							outToServer, i);
				} else if (command.equals(OnOffType.ON)) {
					sendPowerCommand(itemName, SonanceConsts.POWER_ON,
							outToServer, i);
				} else {
					logger.error(
							"I don't know what to do with the command \"{}\"",
							command);
				}
			} else if (provider.isVolume(itemName))
				if (command.equals(UpDownType.UP))
					sendVolumeCommand(itemName,
							SonanceConsts.VOLUME_UP + group, outToServer, i);
				else if (command.equals(UpDownType.DOWN))
					sendVolumeCommand(itemName, SonanceConsts.VOLUME_DOWN
							+ group, outToServer, i);
				else {
					try {
						setVolumeCommand(itemName, group,
								Integer.parseInt(command.toString()),
								outToServer, i, ip + ":" + port);
					} catch (NumberFormatException nfe) {
						logger.error(
								"I don't know what to do with the command \"{}\"",
								command);
					}

				}
			s.close();
		} catch (IOException e) {
			logger.debug("IO Exception when sending command. Exception: {}",
					e.getMessage());
		} finally {
			closeSilently(s);
		}
	}

	/**
	 * Closes a socket
	 * 
	 * @param s
	 *            socket to close
	 */
	private void closeSilently(Socket s) {
		try {
			if (s != null) {
				s.close();
			}
		} catch (IOException e) {
		}
	}

	/*
	 * @see
	 * org.openhab.core.binding.AbstractBinding#internalReceiveUpdate(java.lang
	 * .String, org.openhab.core.types.State)
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		logger.info("Update received ({},{})", itemName, newState);

		SonanceBindingProvider provider = findFirstMatchingBindingProvider(itemName);
		String group = provider.getGroup(itemName);
		String ip = provider.getIP(itemName);
		int port = provider.getPort(itemName);

		ip = null;
		group = null; // cleanup

		Socket s = null;
		try {
			s = new Socket(ip, port);
			DataOutputStream outToServer = new DataOutputStream(
					s.getOutputStream());
			BufferedReader i = new BufferedReader(new InputStreamReader(
					s.getInputStream()));

			if (provider.isMute(itemName))
				if (newState.equals(OnOffType.OFF))
					sendMuteCommand(itemName, SonanceConsts.MUTE_ON + group,
							outToServer, i);
				else if (newState.equals(OnOffType.ON))
					sendMuteCommand(itemName, SonanceConsts.MUTE_OFF + group,
							outToServer, i);
				else
					logger.error(
							"I don't know what to do with this new state \"{}\"",
							newState);
			if (provider.isPower(itemName))
				if (newState.equals(OnOffType.OFF))
					sendPowerCommand(itemName, SonanceConsts.POWER_OFF,
							outToServer, i);
				else if (newState.equals(OnOffType.ON))
					sendPowerCommand(itemName, SonanceConsts.POWER_ON,
							outToServer, i);
				else
					logger.error(
							"I don't know what to do with this new state \"{}\"",
							newState);
			else if (provider.isVolume(itemName))
				if (newState.equals(IncreaseDecreaseType.INCREASE))
					sendVolumeCommand(itemName,
							SonanceConsts.VOLUME_UP + group, outToServer, i);
				else if (newState.equals(IncreaseDecreaseType.DECREASE))
					sendVolumeCommand(itemName, SonanceConsts.VOLUME_DOWN
							+ group, outToServer, i);
				else
					logger.error(
							"I don't know what to do with this new state \"{}\"",
							newState);
			s.close();
		} catch (IOException e) {
			logger.error(
					"IO Exception when received internal command. Message: {}",
					e.getMessage());
		} finally {
			closeSilently(s);
		}
	}

	/**
	 * Send volume commands to groups (music zones)
	 * 
	 * @param itemName
	 *            item name to send update to
	 * @param command
	 *            Sonance IP code to execute
	 * @param outToServer
	 *            date output stream we can write to
	 * @param i
	 *            bufered reader where we can read from
	 * @throws IOException
	 *             throws an exception when we can't reach to amplifier
	 */
	private void sendVolumeCommand(String itemName, String command,
			DataOutputStream outToServer, BufferedReader i) throws IOException {
		char[] cbuf = new char[50]; // Response is always 50 characters

		logger.debug("Sending volume command {}", command);

		outToServer.write(hexStringToByteArray(command));
		i.read(cbuf, 0, 50);

		Matcher m = volumePattern.matcher(new String(cbuf));

		if (m.find()) {
			String volume = m.group(1);
			eventPublisher.postUpdate(itemName, new DecimalType(volume));
			logger.debug("Setting volume for item {} on {}", itemName, volume);
		} else {
			logger.error(
					"Error sending regular volume command {}, received this: {}",
					command, new String(cbuf));
		}
	}

	/**
	 * Enable or disable specific groups (music zones)
	 * 
	 * @param itemName
	 *            item name to send update to
	 * @param command
	 *            Sonance IP code to execute
	 * @param outToServer
	 *            date output stream we can write to
	 * @param i
	 *            bufered reader where we can read from
	 * @throws IOException
	 *             throws an exception when we can't reach to amplifier
	 */
	private void sendMuteCommand(String itemName, String command,
			DataOutputStream outToServer, BufferedReader i) throws IOException {
		char[] cbuf = new char[50]; // Response is always 50 characters

		logger.debug("Sending mute command {}", command);
		outToServer.write(hexStringToByteArray(command));
		i.read(cbuf, 0, 50);

		String result = new String(cbuf);

		logger.trace("Received this result: {}", result);

		if (result.contains("Mute=on") || result.contains("MuteOn")) {
			eventPublisher.postUpdate(itemName, OnOffType.OFF);
			logger.debug("Setting mute item {} on OFF", itemName);
		} else if (result.contains("Mute=off") || result.contains("MuteOff")) {
			eventPublisher.postUpdate(itemName, OnOffType.ON);
			logger.debug("Setting mute item {} on ON", itemName);
		} else
			logger.error("Error sending mute command {}, received this: {}",
					command, result);
	}

	/**
	 * Wake up or put amplifier to sleep
	 * 
	 * @param itemName
	 *            item name to send update to
	 * @param command
	 *            Sonance IP code to execute
	 * @param outToServer
	 *            date output stream we can write to
	 * @param i
	 *            bufered reader where we can read from
	 * @throws IOException
	 *             throws an exception when we can't reach to amplifier
	 */
	private void sendPowerCommand(String itemName, String command,
			DataOutputStream outToServer, BufferedReader i) throws IOException {
		char[] cbuf = new char[50]; // Response is always 50 characters

		logger.debug("Sending power command {}", command);
		outToServer.write(hexStringToByteArray(command));
		i.read(cbuf, 0, 50);

		String result = new String(cbuf);

		logger.trace("Received power response: {}", result);

		if (result.contains("Off")) {
			eventPublisher.postUpdate(itemName, OnOffType.OFF);
			logger.debug("Setting power item {} on OFF", itemName);
		} else if (result.contains("On")) {
			eventPublisher.postUpdate(itemName, OnOffType.ON);
			logger.debug("Setting power item {} on ON", itemName);
		} else {
			logger.debug("Error sending power command {}, received this: {}",
					command, result); // Is trigger when toggling to fast
			// Put back to old state
			if (command.equals(SonanceConsts.POWER_OFF)) {
				eventPublisher.postUpdate(itemName, OnOffType.ON);
			} else {
				eventPublisher.postUpdate(itemName, OnOffType.OFF);
			}
		}
	}

	/**
	 * Sets the group to the specified target volume. Amplifier doesn't support
	 * direct volume commands, so a loop is needed
	 * 
	 * @param itemName
	 *            item to publish result to
	 * @param group
	 *            target group
	 * @param targetVolume
	 *            target volume
	 * @param outToServer
	 *            data output stream where we can write to
	 * @param i
	 *            buffered reader where we can read from
	 * @param endpoint
	 *            ip:port
	 * @throws IOException
	 *             throws an IOException when we can't reach the amplifier
	 */
	private void setVolumeCommand(String itemName, String group,
			int targetVolume, DataOutputStream outToServer, BufferedReader i,
			String endpoint) throws IOException {
		char[] cbuf = new char[50]; // Response is always 50 characters

		// Now lock this part, so we don't end up with two functions going up
		// and now all the time
		String lockKey = endpoint + ":" + group;
		if (!volumeLocks.containsKey(lockKey))
			volumeLocks.put(lockKey, new ReentrantLock()); // We can keep this
															// once when we
															// finished with it,
															// speed before
															// memory usage

		try {
			volumeLocks.get(lockKey).tryLock(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			logger.debug("Lock waiting time (2s) expired for lockKey {}.",
					lockKey);
			return;
		}

		try {
			outToServer.write(hexStringToByteArray(SonanceConsts.VOLUME_QUERY
					+ group));
			i.read(cbuf, 0, 50);

			Matcher m = volumePattern.matcher(new String(cbuf));

			if (m.find()) {
				double currentVolume = Integer.parseInt(m.group(1));
				eventPublisher.postUpdate(itemName, new DecimalType(
						currentVolume));
				logger.debug("Updating {} with new volume {}", itemName,
						currentVolume);
				int step = 0; // We should be able to reach every volume in less
								// 29 steps
				while (currentVolume != targetVolume && step++ <= 28) {
					logger.debug(
							"Current volume: {}, target volume: {}, step: {}, lock: {})",
							currentVolume, targetVolume, step, lockKey);
					if (currentVolume + 3 <= targetVolume) {
						outToServer
								.write(hexStringToByteArray(SonanceConsts.VOLUME_UP_3
										+ group));
						logger.debug("Sending volume up 3 command {}",
								SonanceConsts.VOLUME_UP_3);
					}
					if (currentVolume - 3 >= targetVolume) {
						outToServer
								.write(hexStringToByteArray(SonanceConsts.VOLUME_DOWN_3
										+ group));
						logger.debug("Sending volume down 3 command {}",
								SonanceConsts.VOLUME_DOWN_3);
					} else if (currentVolume < targetVolume) {
						outToServer
								.write(hexStringToByteArray(SonanceConsts.VOLUME_UP
										+ group));
						logger.debug("Sending volume up command {}",
								SonanceConsts.VOLUME_UP);
					} else {
						outToServer
								.write(hexStringToByteArray(SonanceConsts.VOLUME_DOWN
										+ group));
						logger.debug("Sending volume down command {}",
								SonanceConsts.VOLUME_DOWN);
					}
					i.read(cbuf, 0, 50);
					m = volumePattern.matcher(new String(cbuf));
					if (m.find()) {
						currentVolume = Integer.parseInt(m.group(1));
						logger.info("Setting volume, current volume: {}",
								currentVolume);
						eventPublisher.postUpdate(itemName, new DecimalType(
								currentVolume));
					} else {
						logger.error(
								"Error sending volume command, received this: {}",
								new String(cbuf));
					}
				}
			} else {
				logger.error("Error sending volume command, received this: {}",
						new String(cbuf));
			}
		} finally {
			volumeLocks.get(lockKey).unlock();
		}
	}

	/**
	 * Get binding provider for that item
	 * 
	 * @param itemName
	 *            name of the item where we need to binding provder for
	 * @return SonanceBindingProvider
	 */
	protected SonanceBindingProvider findFirstMatchingBindingProvider(
			String itemName) {
		SonanceBindingProvider firstMatchingProvider = null;
		for (SonanceBindingProvider provider : providers) {
			if (provider.providesBindingFor(itemName)) {
				firstMatchingProvider = provider;
				break;
			}
		}
		return firstMatchingProvider;
	}

	/**
	 * Function to convert strings to hexadecimal bytes.
	 * 
	 * @param s
	 *            the string to convert to a hexadecimal byte array
	 * @return hexadecimal byte array
	 */
	private static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2)
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
					.digit(s.charAt(i + 1), 16));
		return data;
	}
}
