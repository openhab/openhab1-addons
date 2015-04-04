/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal;

import static org.openhab.binding.ulux.internal.UluxBinding.LOG;
import static org.openhab.binding.ulux.internal.UluxBinding.PORT;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.osgi.service.cm.ConfigurationException;

/**
 * This class contains the u:Lux binding configuration from {@code openhab.cfg}.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxConfiguration {

	private static final String KEY_SERVICE_PID = "service.pid";

	private static final String KEY_SWITCH = "switch.";

	private static final String KEY_DESIGN = "designId";

	private static final String KEY_PROJECT = "projectId";

	private static final String KEY_BIND_ADDRESS = "bind_address";

	// TODO MicrophoneSecurityID

	private static final short DEFAULT_DESIGN_ID = 1;

	private static final short DEFAULT_PROJECT_ID = 1;

	private static final InetAddress DEFAULT_BIND_ADDRESS = null;

	/**
	 * Map from switch id to address.
	 */
	private final Map<Short, InetAddress> switchAdresses = new HashMap<Short, InetAddress>();

	/**
	 * Map from switch address to id.
	 */
	private final Map<InetAddress, Short> switchIds = new HashMap<InetAddress, Short>();

	private short designId = DEFAULT_DESIGN_ID;

	private short projectId = DEFAULT_PROJECT_ID;

	private InetAddress bindAddress = DEFAULT_BIND_ADDRESS;

	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config == null) {
			return;
		}

		final Enumeration<String> keys = config.keys();
		while (keys.hasMoreElements()) {
			final String key = keys.nextElement();
			final String value = (String) config.get(key);

			if (key.equals(KEY_SERVICE_PID)) {
				// ignore value
			} else if (key.startsWith(KEY_SWITCH)) {
				final String switchId = key.substring(KEY_SWITCH.length());

				try {
					InetAddress address = InetAddress.getByName(value);
					this.switchAdresses.put(Short.valueOf(switchId), address);
					this.switchIds.put(address, Short.valueOf(switchId));
				} catch (NumberFormatException e) {
					LOG.error("Illegal switch id: {}", switchId);
				} catch (UnknownHostException e) {
					LOG.error("Illegal switch address: {}", value);
				}
			} else if (key.equals(KEY_DESIGN)) {
				try {
					final String designId = (String) config.get(key);

					this.designId = Short.valueOf(designId);
				} catch (NumberFormatException e) {
					LOG.error("Illegal design id: {}", designId);
				}
			} else if (key.equals(KEY_PROJECT)) {
				try {
					final String projectId = (String) config.get(key);

					this.projectId = Short.valueOf(projectId);
				} catch (NumberFormatException e) {
					LOG.error("Illegal project id: {}", projectId);
				}
			} else if (key.equals(KEY_BIND_ADDRESS)) {
				try {
					final String bindAddress = (String) config.get(key);

					this.bindAddress = InetAddress.getByName(bindAddress);
				} catch (UnknownHostException e) {
					LOG.error("Illegal bind address: {}", bindAddress);
				}
			} else {
				LOG.warn("Ignoring unknown configuration: {} = {} ", key, value);
			}
		}
	}

	/**
	 * @throws NoSuchElementException
	 *             if switch is unknown
	 */
	public InetAddress getSwitchAddress(final short switchId) {
		if (!this.switchAdresses.containsKey(switchId)) {
			throw new NoSuchElementException();
		}

		return this.switchAdresses.get(switchId);
	}

	/**
	 * @throws NoSuchElementException
	 *             if switch is unknown
	 */
	public short getSwitchId(InetAddress switchAddress) {
		if (!this.switchIds.containsKey(switchAddress)) {
			throw new NoSuchElementException();
		}

		return this.switchIds.get(switchAddress);
	}

	public short getDesignId() {
		return this.designId;
	}

	public short getProjectId() {
		return this.projectId;
	}

	/**
	 * @return never <code>null</code>
	 */
	public InetSocketAddress getBindAddress() {
		if (this.bindAddress == null) {
			return new InetSocketAddress(PORT);
		} else {
			return new InetSocketAddress(this.bindAddress, PORT);
		}
	}
}
