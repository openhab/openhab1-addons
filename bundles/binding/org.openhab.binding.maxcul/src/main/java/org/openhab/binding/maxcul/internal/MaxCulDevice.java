package org.openhab.binding.maxcul.internal;

/**
 * Define device types
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public enum MaxCulDevice {
	CUBE(0), RADIATOR_THERMOSTAT(1), RADIATOR_THERMOSTAT_PLUS(2), WALL_THERMOSTAT(
			3), SHUTTER_CONTACT(4), PUSH_BUTTON(5), PAIR_MODE(0xfd), // not
																		// official
																		// MAX!
																		// leave
																		// others
																		// reserved
	LISTEN_MODE(0xfe), // not official MAX!
	UNKNOWN(0xff); // not official MAX!

	private final int devType;

	private MaxCulDevice(int idx) {
		devType = idx;
	}

	public int getDeviceTypeInt() {
		return devType;
	}

	public static MaxCulDevice getDeviceTypeFromInt(int idx) {
		for (int i = 0; i < MaxCulDevice.values().length; i++) {
			if (MaxCulDevice.values()[i].getDeviceTypeInt() == idx)
				return MaxCulDevice.values()[i];
		}
		return UNKNOWN;
	}
}
