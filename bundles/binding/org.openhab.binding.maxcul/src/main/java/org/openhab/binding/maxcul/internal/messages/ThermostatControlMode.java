package org.openhab.binding.maxcul.internal.messages;

/**
 * Define Thermostat control modes
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public enum ThermostatControlMode {
	AUTO(0x0),
	MANUAL(0x1),
	TEMPORARY(0x2),
	BOOST(0x3);

	private final int controlMode;

	ThermostatControlMode(int mode) {
		this.controlMode = mode;
	}

	ThermostatControlMode(byte mode) {
		this.controlMode = mode;
	}

	public byte toByte() {
		return (byte) controlMode;
	}
}
