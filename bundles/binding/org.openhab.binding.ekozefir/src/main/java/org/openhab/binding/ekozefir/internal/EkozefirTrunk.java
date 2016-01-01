/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.internal;

import java.util.Map;
import java.util.Objects;

import org.openhab.binding.ekozefir.ahucommand.AhuCommand;
import org.openhab.binding.ekozefir.ahucommand.EkozefirAhuCommandListener;
import org.openhab.binding.ekozefir.exception.AhuNameNotFoundException;
import org.openhab.binding.ekozefir.protocol.EkozefirMessageService;
import org.openhab.binding.ekozefir.protocol.EkozefirSerialConnector;
import org.openhab.binding.ekozefir.protocol.EkozefirSerialMessageService;
import org.openhab.binding.ekozefir.protocol.EkozefirSerialThreadedConnector;
import org.openhab.binding.ekozefir.response.ResponseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * Ekozefir bus.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class EkozefirTrunk {

	private static final Logger logger = LoggerFactory.getLogger(EkozefirTrunk.class);

	private final Map<Character, EkozefirBus> buses = Maps.newHashMap();
	private final Map<Character, EkozefirMessageService> services = Maps.newHashMap();

	/**
	 * Create connection service and find ahu name. Create bus to sending
	 * information from and to openhab.
	 * 
	 * @param driver serial driver
	 */
	public void createAndConnectDriverBus(String driver) {
		Objects.requireNonNull(driver);
		EkozefirMessageService service = new EkozefirSerialMessageService(new EkozefirSerialThreadedConnector(
				new EkozefirSerialConnector(driver)));
		service.connect();
		Character ahuName = service.getAhuName();
		services.put(ahuName, service);
		EkozefirBus bus = new EkozefirBus();
		bus.register(new EkozefirAhuCommandListener(service, bus));
		buses.put(ahuName, bus);
	}

	/**
	 * Disconnecting all drivers.
	 */
	public void disconnectAll() {
		for (EkozefirMessageService service : services.values()) {
			service.disconnect();
		}
	}

	/**
	 * Connecting all drivers.
	 */
	public void connectAll() {
		for (EkozefirMessageService service : services.values()) {
			service.connect();
		}
	}

	/**
	 * Register listener to given ahu name.
	 * 
	 * @param ahuName ahu name
	 * @param listener response listener
	 */
	public void register(Character ahuName, ResponseListener listener) {
		logger.debug("Try to register ahu name: {}, with listener: {}", ahuName, listener);
		Objects.requireNonNull(ahuName);
		Objects.requireNonNull(listener);
		if (!buses.containsKey(ahuName)) {
			throw new AhuNameNotFoundException(ahuName);
		}
		buses.get(ahuName).register(listener);
	}

	/**
	 * Unregister listener from given ahu name.
	 * 
	 * @param ahuName ahu name
	 * @param listener response listener
	 */
	public void unregister(Character ahuName, ResponseListener listener) {
		logger.debug("Try to unregister ahu name: {}, with listener: {}", ahuName, listener);
		Objects.requireNonNull(ahuName);
		Objects.requireNonNull(listener);
		if (!buses.containsKey(ahuName)) {
			throw new AhuNameNotFoundException(ahuName);
		}
		buses.get(ahuName).unregister(listener);
	}

	/**
	 * Post command to all ahus.
	 * 
	 * @param command command to send
	 */
	public void postforAllBus(AhuCommand command) {
		Objects.requireNonNull(command);
		for (EkozefirBus bus : buses.values()) {
			bus.post(command);
		}
	}

	/**
	 * Post command to specific ahu driver.
	 * 
	 * @param ahuName driver identifier
	 * @param command command to send
	 */
	public void post(Character ahuName, AhuCommand command) {
		Objects.requireNonNull(command);
		Objects.requireNonNull(ahuName);
		if (!buses.containsKey(ahuName)) {
			throw new AhuNameNotFoundException(ahuName);
		}
		buses.get(ahuName).post(command);
	}
}
