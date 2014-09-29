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
 * ZWave Network Initialization Completed event.
 * Indicates that the ZWaveController has completed
 * the initialization phase and is ready to start
 * accepting commands and receiving events.
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class ZWaveInitializationCompletedEvent extends ZWaveEvent {

	/**
	 * Constructor. Creates a new instance of the ZWaveTransactionCompletedEvent
	 * class.
	 * @param nodeId the nodeId of the event. Must be set to the controller node.
	 */
	public ZWaveInitializationCompletedEvent(int nodeId) {
		super(nodeId, 1);
	}

}
