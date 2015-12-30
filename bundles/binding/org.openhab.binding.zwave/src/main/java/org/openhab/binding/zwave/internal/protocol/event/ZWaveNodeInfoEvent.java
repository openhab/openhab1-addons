/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
 package org.openhab.binding.zwave.internal.protocol.event;

/**
 * This event signals a node information frame.
 * @author Chris Jackson
 * @since 1.8.0
 */
public class ZWaveNodeInfoEvent extends ZWaveEvent {
	
	/**
	 * Constructor. Creates a new instance of the ZWaveInclusionEvent
	 * class.
	 * @param nodeId the nodeId of the event.
	 */
	public ZWaveNodeInfoEvent(int nodeId) {
		super(nodeId);
	}
}
