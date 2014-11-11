package org.openhab.binding.enigma2.internal;

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
