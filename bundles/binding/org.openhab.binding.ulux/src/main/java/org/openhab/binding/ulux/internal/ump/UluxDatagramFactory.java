/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump;

import java.net.InetAddress;

import org.openhab.binding.ulux.UluxBindingConfig;
import org.openhab.binding.ulux.internal.UluxConfiguration;

/**
 * A factory for {@link UluxDatagram}s.
 * 
 * @author Andreas Brenk
 * @since 1.8.0
 */
public class UluxDatagramFactory {

	private final UluxConfiguration configuration;

	/**
	 * Constructs a datagram factory for the given configuration.
	 */
	public UluxDatagramFactory(final UluxConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Creates an empty datagram.
	 * 
	 * @return never {@code null}
	 */
	public UluxMessageDatagram createMessageDatagram(short switchId, InetAddress sourceAddress) {
		return new UluxMessageDatagram(switchId, sourceAddress);
	}

	/**
	 * Creates an empty datagram.
	 * 
	 * @return never {@code null}
	 */
	public UluxMessageDatagram createMessageDatagram(UluxBindingConfig config) {
		final short switchId = config.getSwitchId();
		final InetAddress switchAddress = this.configuration.getSwitchAddress(switchId);

		return createMessageDatagram(switchId, switchAddress);
	}

}
