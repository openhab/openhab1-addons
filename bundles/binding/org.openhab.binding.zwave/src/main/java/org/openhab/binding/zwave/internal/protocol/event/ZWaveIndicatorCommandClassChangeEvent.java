/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.event;

import java.util.ArrayList;

import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;

/**
 * ZWave Command Class event. This event is fired when a command class
 * receives a value from the node. The event can be subclasses to add
 * additional information to the event. 
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class ZWaveIndicatorCommandClassChangeEvent extends ZWaveCommandClassValueEvent {
	private int currentIndicator;
	
	/**
	 * Constructor. Creates a new instance of the ZWaveCommandClassValueEvent class.
	 * @param nodeId the nodeId of the event
	 * @param endpoint the endpoint of the event.
	 * @param commandClass the command class that fired the ZWaveCommandClassValueEvent;
	 * @param the new indicator value for the event.
	 * @param the value currently held by indicator for the event.
	 */
	public ZWaveIndicatorCommandClassChangeEvent(int nodeId, int endpoint, CommandClass commandClass, int newValue, int oldValue) {
		super(nodeId, endpoint, commandClass, newValue);
		this.currentIndicator = oldValue;
	}

	/**
	 * Gets the value for the event.
	 * @return the value.
	 */
	public int getOldValue() {
		return currentIndicator;
	}
	
	public ArrayList<Integer> changes() {
		ArrayList<Integer> changesList = new ArrayList<Integer>();
		
		int newIndicator = ((Integer) this.getValue()).intValue();
		
		int changes = currentIndicator ^ newIndicator;
		
		for(int i = 0; i < 8; i++) {
			if ( (changes & 0x01) == 0x01 ) {
				changesList.add(i);
			}
			changes = changes >> 1;
		}
		return changesList;
	}
	
	public boolean isBitOn(int n) {
		byte newIndicator = ((Integer) this.getValue()).byteValue();
		if ( ((newIndicator >> (n-1)) & 0x01) == 1 ) {
			return true;
		}
		return false;
	}
}
