/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.internal;

import java.util.Objects;

import org.openhab.binding.ekozefir.ahucommand.AhuCommand;
import org.openhab.binding.ekozefir.ahucommand.AhuCommandListener;
import org.openhab.binding.ekozefir.response.Response;
import org.openhab.binding.ekozefir.response.ResponseListener;

import com.google.common.eventbus.EventBus;

/**
 * Implementation of AhuCommandBus, ResponseBus.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 */
public class EkozefirBus implements AhuCommandBus, ResponseBus {

	private final EventBus bus;

	public EkozefirBus(EventBus bus) {
		Objects.requireNonNull(bus);
		this.bus = bus;
	}

	public EkozefirBus() {
		this.bus = new EventBus();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void post(Response event) {
		Objects.requireNonNull(event);
		bus.post(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void register(ResponseListener listener) {
		Objects.requireNonNull(listener);
		bus.register(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void post(AhuCommand event) {
		Objects.requireNonNull(event);
		bus.post(event);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void register(AhuCommandListener listener) {
		Objects.requireNonNull(listener);
		bus.register(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void unregister(ResponseListener listener) {
		Objects.requireNonNull(listener);
		bus.unregister(listener);
	}
}
