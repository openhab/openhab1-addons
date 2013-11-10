/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
 package org.openhab.binding.zwave.internal.protocol.event;

/**
 * ZWave configuration parameter received event.
 * Send from the Configuration Command Class to the binding
 * when a configuration value is received.
 * 
 * @author Chris Jackson
 * @since 1.4.0
 */
public class ZWaveConfigurationParameterEvent extends ZWaveEvent {

	private int parameter;
	private int value;
	private int size;
	
	/**
	 * Constructor. Creates a new instance of the ZWaveConfigurationParameterEvent
	 * class.
	 * @param nodeId the nodeId of the event. Must be set to the controller node.
	 */
	public ZWaveConfigurationParameterEvent(int nodeId, int parameter, int value, int size) {
		super(nodeId, 1);
		
		this.parameter = parameter;
		this.value = value;
		this.size = size;
	}

	public int getParameter() {
		return parameter;
	}
	
	public int getValue() {
		return value;
	}

	public int getSize() {
		return size;
	}
}
