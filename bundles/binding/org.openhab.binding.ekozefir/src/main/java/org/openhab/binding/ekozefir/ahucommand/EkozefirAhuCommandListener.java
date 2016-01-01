/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.ahucommand;

import java.util.Objects;

import org.openhab.binding.ekozefir.ahucommand.AhuCommand;
import org.openhab.binding.ekozefir.ahucommand.AhuCommandListener;
import org.openhab.binding.ekozefir.ahucommand.refreshparameters.RefreshParametersAhuCommand;
import org.openhab.binding.ekozefir.internal.ResponseBus;
import org.openhab.binding.ekozefir.protocol.EkozefirMessageService;

import com.google.common.eventbus.Subscribe;

/**
 * Implementation of AhuCommandListener.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class EkozefirAhuCommandListener implements AhuCommandListener {

	private final EkozefirMessageService driver;

	private final ResponseBus bus;

	public EkozefirAhuCommandListener(EkozefirMessageService driver, ResponseBus bus) {
		Objects.requireNonNull(driver);
		Objects.requireNonNull(bus);
		this.bus = bus;
		this.driver = driver;
	}

	/**
	 * Connect ahu driver.
	 */
	public void connect() {
		driver.connect();
	}

	/**
	 * Disconnect ahu driver.
	 */
	public void disconnect() {
		driver.disconnect();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Subscribe
	public void listen(AhuCommand event) {
		Objects.requireNonNull(event);
		driver.sendMessage(event);
		if (event instanceof RefreshParametersAhuCommand) {
			bus.post(driver.getResponse());
		}
	}

}
