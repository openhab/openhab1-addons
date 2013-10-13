/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol;

/**
 * Z-Wave event class. The Z-Wave controller notifies listeners using Z-Wave events.
 * 
 * @author Victor Belov
 * @author Brian Crosby
 * @since 1.3.0
 */
public class ZWaveEvent {
	
	private final int nodeId;
	private final int endpoint;

	private final ZWaveEventType eventType;
	private final Object eventValue;

	/**
	 * Constructor. Creates a new instance of the Z-Wave event class.
	 * @param eventType the type of the event
	 * @param nodeId the nodeId of the event
	 * @param endpoint the endpoint of the event.
	 * @param eventValue the value of the event.
	 */
	public ZWaveEvent(ZWaveEventType eventType, int nodeId, int endpoint, Object eventValue) {
		this.nodeId = nodeId;
		this.eventType = eventType;
		this.eventValue = eventValue;
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
	
	/**
	 * Gets the event type of this event.
	 * @return
	 */
	public ZWaveEventType getEventType() {
		return eventType;
	}

	/**
	 * Gets the event value of this event.
	 * @return
	 */
	public Object getEventValue() {
		return eventValue;
	}
	
	@Override
	public String toString() {
		return "ZWaveEvent [nodeId=" + nodeId + ", endpoint=" + endpoint
				+ ", eventType=" + eventType + ", eventValue=" + eventValue
				+ "]";
	}

	
	/**
	 * Event type enumeration. Indicates the type of event that occurred.
	 * @author Jan-Willem Spuij
	 * @since 1.3.0
	 */
	public enum ZWaveEventType	{
		BASIC_EVENT,
		SWITCH_EVENT,
		SENSOR_EVENT,
		DIMMER_EVENT,
		NETWORK_EVENT,
		BATTERY_EVENT,
		TRANSACTION_COMPLETED_EVENT,
	}
	
}
