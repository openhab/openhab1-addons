package org.openhab.io.gpio_raspberry.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

public class GpioItemConfig implements BindingConfig {
	public Class<? extends Item> itemType;
	public String id;
	public long refresh = -1;
	public Byte port;
	
	// runtime
	public long lastRefresh = 0;

	@Override
	public String toString() {
		return "GpioItemConfig [itemType=" + itemType + ", id=" + id
				+ ", refresh=" + refresh + ", port=" + port + ", lastRefresh="
				+ lastRefresh + "]";
	}
}
