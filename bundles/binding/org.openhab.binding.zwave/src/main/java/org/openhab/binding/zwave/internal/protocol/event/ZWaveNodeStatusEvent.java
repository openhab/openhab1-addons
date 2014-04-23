/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
 package org.openhab.binding.zwave.internal.protocol.event;

/**
 * Node status event is used to signal if a node is alive or dead
 * @author Chris Jackson
 * @since 1.5.0
 */
public class ZWaveNodeStatusEvent extends ZWaveEvent {
	State state;

	/**
	 * Constructor. Creates a new instance of the ZWaveNetworkEvent
	 * class.
	 * @param nodeId the nodeId of the event.
	 */
	public ZWaveNodeStatusEvent(int nodeId, State state) {
		super(nodeId, 1);

		this.state = state;
	}

	public State getState() {
		return state;
	}

	public enum State {
		Dead, Alive
	}
}
