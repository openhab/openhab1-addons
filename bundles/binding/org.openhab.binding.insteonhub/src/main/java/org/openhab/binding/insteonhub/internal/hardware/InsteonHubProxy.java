/**
 * openHAB, the open Home Automation Bus. Copyright (C) 2010-2013, openHAB.org
 * <admin@openhab.org>
 * 
 * See the contributors.txt file in the distribution for a full listing of
 * individual contributors.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 * 
 * Additional permission under GNU GPL version 3 section 7
 * 
 * If you modify this Program, or any covered work, by linking or combining it
 * with Eclipse (or a modified version of that library), containing parts
 * covered by the terms of the Eclipse Public License (EPL), the licensors of
 * this Program grant you additional permission to convey the resulting work.
 */
package org.openhab.binding.insteonhub.internal.hardware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubProxyUpdate.UpdateType;
import org.openhab.binding.insteonhub.internal.util.InsteonHubBindingLogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Insteon hub proxy used to control an insteon hub using HTTP
 * 
 * @author Eric Thill
 */
public class InsteonHubProxy {

	private static final Logger logger = LoggerFactory
			.getLogger(InsteonHubProxy.class);

	public static final int DEFAULT_PORT = 25105;

	private static final int LEVEL_MAX = 255;
	private static final int LEVEL_MIN = 0;

	private static final String COMMAND_ON_RAMP = "11";
	private static final String COMMAND_ON_FAST = "12";
	private static final String COMMAND_OFF_RAMP = "13";
	private static final String COMMAND_OFF_FAST = "14";
	private static final String COMMAND_STATUS = "19";
	private static final String STATUS_PREFIX = "<X D=\"";

	private final String host;
	private final int port;
	private final String credentials;
	private final InsteonHubProxyListener listener;

	// The AtomicIntegers in this map work as a batching mechanism for device
	// adjustments. Sending a command is expensive, so when a user presses
	// "increase" 5 times in a row, it causes a noticable lag. This map is used
	// to batch increase/decrease commands together before they are executed.
	private final Map<String, AtomicInteger> analogAdjustments = new HashMap<String, AtomicInteger>();
	// Queue of update commands
	private final BlockingQueue<InsteonHubProxyUpdate> updateQueue = new LinkedBlockingQueue<InsteonHubProxyUpdate>();
	// Runnable that executes all of the update commands
	private volatile StateUpdater stateUpdater;

	public InsteonHubProxy(String host, String user, String pass,
			InsteonHubProxyListener listener) {
		this(host, DEFAULT_PORT, user, pass, listener);
	}

	public InsteonHubProxy(String host, int port, String user, String pass,
			InsteonHubProxyListener listener) {
		this.host = host;
		this.port = port;
		this.listener = listener;
		this.credentials = Base64.encodeBase64String((user + ":" + pass)
				.getBytes());
	}

	public String getConnectionString() {
		return host + ":" + port;
	}

	public synchronized void start() {
		stop();
		stateUpdater = new StateUpdater();
		new Thread(stateUpdater, "InsteonHubProxy StateUpdater").start();
	}

	public synchronized void stop() {
		if (stateUpdater != null) {
			// Set stateUpdater to null to signal it to stop
			stateUpdater = null;
			// Make update thread unblock
			updateQueue.add(new InsteonHubProxyUpdate());
		}
	}

	/**
	 * Set the device's digital value.
	 * 
	 * @param device
	 *            Device to set
	 * @param power
	 *            true for ON, false for OFF
	 * @param ramp
	 *            If the device should ramp the value up/down
	 */
	public void setPower(String device, boolean power, boolean ramp) {
		InsteonHubProxyUpdate update = new InsteonHubProxyUpdate();
		update.type = UpdateType.DIGITAL_SET;
		update.device = device;
		update.level = power ? LEVEL_MAX : LEVEL_MIN;
		updateQueue.add(update);
	}

	/**
	 * Set the device's analog value. Will ramp up/down.
	 * 
	 * @param device
	 *            Device to set
	 * @param level
	 *            Level to set
	 */
	public void setLevel(String device, int level) {
		InsteonHubProxyUpdate update = new InsteonHubProxyUpdate();
		update.type = UpdateType.ANALOG_SET;
		update.device = device;
		update.level = level;
		updateQueue.add(update);
	}

	/**
	 * Adjust an analog device's value by the given amount
	 * 
	 * @param device
	 *            Device to adjust
	 * @param levelAdjustment
	 *            Amount to adjust (+/- 0 to 255)
	 */
	public void adjustLevel(String device, int levelAdjustment) {
		int curAdjustment = getDeviceAdjustment(device).getAndAdd(
				levelAdjustment);
		if (curAdjustment == 0) {
			// Adjustment was not batched, create new adjustment entry in queue
			InsteonHubProxyUpdate update = new InsteonHubProxyUpdate();
			update.type = UpdateType.ANALOG_ADJUST;
			update.device = device;
			updateQueue.add(update);
		}
	}

	// Helper to get or create the AtomicInteger for device adjustments
	private synchronized AtomicInteger getDeviceAdjustment(String device) {
		AtomicInteger adjustment = analogAdjustments.get(device);
		if (adjustment == null) {
			adjustment = new AtomicInteger();
			analogAdjustments.put(device, adjustment);
		}
		return adjustment;
	}

	/**
	 * Check if the current device is on. That is, it checks if the current
	 * device's dimmer value is greater than 0.
	 * 
	 * @param device
	 *            The device
	 * @return True if the device's dimmer value is greater than 0, False
	 *         otherwise
	 * @throws IOException
	 */
	public boolean isPower(String device) throws IOException {
		return getLevel(device) > 0;
	}

	/**
	 * Get current level of an Insteon Device controlled by the hub
	 * 
	 * @param device
	 *            The device
	 * @return The current level of the device
	 * @throws IOException
	 */
	public int getLevel(String device) throws IOException {
		StringBuilder url = new StringBuilder();
		url.append("/sx.xml?");
		url.append(device);
		url.append("=");
		url.append(COMMAND_STATUS);
		url.append("00");

		String response = httpGet(url.toString());

		int startIdx = response.indexOf(STATUS_PREFIX);
		if (startIdx == -1) {
			throw new IOException("Unexpected response: " + response);
		}

		try {
			// ignore up to xml node value
			String hex = response.substring(startIdx + STATUS_PREFIX.length());
			// ignore after xml node value
			hex = hex.substring(0, hex.indexOf('"'));
			// only pay attention to last 2 digits
			hex = hex.substring(hex.length() - 2);
			// parse the hex value
			int value = Hex.decodeHex(hex.toCharArray())[0];
			if (value < 0) {
				value += 256;
			}
			return value;
		} catch (Exception e) {
			throw new IOException("Unexpected response: " + response, e);
		}
	}

	private String setLevel(String device, int level, boolean ramp)
			throws IOException {
		if (level > LEVEL_MAX) {
			level = LEVEL_MAX;
		} else if (level < LEVEL_MIN) {
			level = LEVEL_MIN;
		}
		String levelHex = byteToHex((byte) level);
		String command;
		if ("00".equals(levelHex)) {
			command = ramp ? COMMAND_OFF_RAMP : COMMAND_OFF_FAST;
		} else if ("FF".equals(levelHex)) {
			command = ramp ? COMMAND_ON_RAMP : COMMAND_ON_FAST;
		} else {
			// "dim" values must ramp
			command = COMMAND_ON_RAMP;
		}

		StringBuilder url = new StringBuilder();
		url.append("/sx.xml?");
		url.append(device);
		url.append("=");
		url.append(command);
		url.append(levelHex);

		return httpGet(url.toString());
	}

	private static final String byteToHex(byte b) {
		return new String(Hex.encodeHex(new byte[] { b })).toUpperCase();
	}

	private String httpGet(String urlSuffix) throws IOException {
		URLConnection connection = null;
		InputStream is = null;
		try {
			connection = new URL("http://" + getConnectionString() + urlSuffix)
					.openConnection();
			connection.setRequestProperty("Authorization", "Basic "
					+ credentials);

			is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (Throwable t) {
			throw new IOException("Could not execute http get", t);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	private class StateUpdater implements Runnable {
		@Override
		public void run() {
			while (stateUpdater == this) {
				// Poll the next update from the queue
				InsteonHubProxyUpdate update = null;
				try {
					update = updateQueue.take();
				} catch(InterruptedException e) {
					// ignore
				}
				if(update == null) {
					continue;
				}
				if(logger.isDebugEnabled()) {
					logger.debug(getConnectionString() + " - Processing: " + update);
				}
				if (update.device != null) {
					try {
						if (update.type == UpdateType.ANALOG_SET) {
							// simple "set" analog value
							setLevel(update.device, update.level, true);
							// callback to listener
							if (listener != null) {
								listener.onAnalogUpdate(update.device,
										update.level);
							}
						} else if (update.type == UpdateType.DIGITAL_SET) {
							// simple "set" digital value
							setLevel(update.device, update.level, false);
							// callback to listener
							if (listener != null) {
								listener.onDigitalUpdate(update.device,
										update.level > 0);
							}
						} else if (update.type == UpdateType.ANALOG_ADJUST) {
							// batch "adjust" analog value
							// get the current level of the device
							int curLevel = getLevel(update.device);
							// lookup batched adjust amount from map
							int adj = analogAdjustments.get(update.device)
									.getAndSet(0);
							// set the analog value
							int newLevel = curLevel + adj;
							setLevel(update.device, newLevel);
							// callback to listener
							if (listener != null) {
								listener.onAnalogUpdate(update.device, newLevel);
							}
						}
					} catch (Throwable t) {
						InsteonHubBindingLogUtil.warnCommunicationFailure(
								logger, InsteonHubProxy.this, update.device, t);
					}
				}
			}
		}
	}

}
