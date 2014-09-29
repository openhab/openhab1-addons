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
 * This event signals a node being included or excluded into the network.
 * @author Chris Jackson
 * @since 1.5.0
 */
public class ZWaveInclusionEvent extends ZWaveEvent {
	Type type;
	
	/**
	 * Constructor. Creates a new instance of the ZWaveInclusionEvent
	 * class.
	 * @param nodeId the nodeId of the event.
	 */
	public ZWaveInclusionEvent(Type type, int nodeId) {
		super(nodeId, 1);
		
		this.type = type;
	}

	public ZWaveInclusionEvent(Type type) {
		super(255, 1);
		
		this.type = type;
	}
	
	public Type getEvent() {
		return type;
	}

	public enum Type {
		IncludeStart,
		IncludeSlaveFound,
		IncludeControllerFound,
		IncludeFail,
		IncludeDone,
		ExcludeStart,
		ExcludeSlaveFound,
		ExcludeControllerFound,
		ExcludeFail,
		ExcludeDone,
	}
}
