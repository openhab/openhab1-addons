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
 * Z-Wave base event class. The Z-Wave controller notifies listeners using
 * Z-Wave events. Inherited classes should be created to indicate the type
 * of event and a possible event value.
 * @author Victor Belov
 * @author Brian Crosby
 * @ since 1.3.0
 */
public abstract class ZWaveEvent {
	private final int nodeId;
	private final int endpoint;

	/**
	 * Constructor. Creates a new instance of the Z-Wave event class.
	 * @param nodeId the nodeId of the event
	 * @param endpoint the endpoint of the event.
	 */
	public ZWaveEvent(int nodeId, int endpoint) {
		this.nodeId = nodeId;
		this.endpoint = endpoint;
	}
	
	/**
	 * Gets the node ID of this event.
	 * @return
	 */
	public int getNodeId() {
		return nodeId;
	}
	
	/**
	 * Gets the endpoint of this event.
	 * @return
	 */
	public int getEndpoint() {
		return endpoint;
	}
}
