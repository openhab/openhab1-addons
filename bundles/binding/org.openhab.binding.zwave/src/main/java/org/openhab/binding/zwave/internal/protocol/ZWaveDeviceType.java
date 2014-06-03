package org.openhab.binding.zwave.internal.protocol;

/**
 * enum defining the different ZWave node types
 * 
 * @author Chris Jackson
 * @since 1.5
 */
public enum ZWaveDeviceType {
	UNKNOWN("Unknown"), 
	SLAVE("Slave"), 
	PRIMARY("Primary Controller"), 
	SECONDARY("Secondary Controller"),
	SUC("Static Update Controller");

	private ZWaveDeviceType(final String text) {
		this.text = text;
	}

	private final String text;

	public String getLabel() {
		return text;
	}

	public static ZWaveDeviceType fromString(String text) {
		if (text != null) {
			for (ZWaveDeviceType c : ZWaveDeviceType.values()) {
				if (text.equalsIgnoreCase(c.name())) {
					return c;
				}
			}
		}
		return null;
	}
}
