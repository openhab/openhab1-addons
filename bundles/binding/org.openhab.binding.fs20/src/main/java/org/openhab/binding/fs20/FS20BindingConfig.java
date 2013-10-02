package org.openhab.binding.fs20;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

public class FS20BindingConfig implements BindingConfig {

	private String address;
	private Item item;
	private String extraOpts;

	public FS20BindingConfig(String address, Item item) {
		this.address = address;
		this.item = item;
	}

	public String getExtraOpts() {
		return extraOpts;
	}

	public void setExtraOpts(String extraOpts) {
		this.extraOpts = extraOpts;
	}

	public String getAddress() {
		return address;
	}

	public Item getItem() {
		return item;
	}

}
