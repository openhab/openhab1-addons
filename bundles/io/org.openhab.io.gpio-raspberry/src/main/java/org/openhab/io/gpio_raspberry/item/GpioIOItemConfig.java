package org.openhab.io.gpio_raspberry.item;

import org.openhab.io.gpio_raspberry.internal.GpioItemConfig;

public class GpioIOItemConfig extends GpioItemConfig {
	public boolean in = false;
	public boolean activeLow = false;
	
	@Override
	public String toString() {
		return "GPIOExtIOBindingConfig [in=" + in + ", activeLow=" + activeLow
				+ ", itemType=" + itemType + ", id=" + id + ", refresh="
				+ refresh + ", port=" + port + ", lastRefresh=" + lastRefresh
				+ "]";
	}
	
	
}
