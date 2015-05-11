/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enigma2.internal;

/**
 * Representing the power state of the Enigma2 device
 * 
 * @author Sebastian Kutschbach
 * @since 1.6.0
 */
public enum Enigma2PowerState {
	/*
	 * Standby: http://dreambox/web/powerstate?newstate=0
	 * 
	 * Deepstandby: http://dreambox/web/powerstate?newstate=1
	 * 
	 * Reboot: http://dreambox/web/powerstate?newstate=2
	 * 
	 * Restart Enigma2: http://dreambox/web/powerstate?newstate=3
	 * 
	 * Wake-up: http://dreambox/web/powerstate?newstate=116
	 */

	STANDBY(0), DEEPSTANDBY(1), REBOOT(2), RESTART(3), WAKEUP(116);

	private int value;

	Enigma2PowerState(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
