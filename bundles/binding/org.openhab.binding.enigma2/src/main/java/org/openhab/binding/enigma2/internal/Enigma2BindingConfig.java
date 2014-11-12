package org.openhab.binding.enigma2.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * Wrapper class for a Enigma2Binding Configuration
 *  
 * @author Sebastian Kutschbach
 * @since 1.6.0
 *
 */
public class Enigma2BindingConfig implements BindingConfig {

	private Item item;
	private boolean inbound;
	private String deviceId;
	private Enigma2Command cmdId;
	private String cmdValue;

	public Enigma2BindingConfig(Item item, boolean inbound, String deviceId,
			Enigma2Command cmdId, String cmdValue) {
		this.item = item;
		this.inbound = inbound;
		this.deviceId = deviceId;
		this.cmdId = cmdId;
		this.cmdValue = cmdValue;
	}

	public Item getItem() {
		return item;
	}

	public boolean isInbound() {
		return inbound;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public Enigma2Command getCmdId() {
		return cmdId;
	}

	public String getCmdValue() {
		return cmdValue;
	}
}