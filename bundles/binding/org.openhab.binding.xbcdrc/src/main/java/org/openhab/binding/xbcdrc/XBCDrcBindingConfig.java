package org.openhab.binding.xbcdrc;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

public class XBCDrcBindingConfig implements BindingConfig{
	private String rcCode;
	private Item item;
	
	public XBCDrcBindingConfig(String rcCode, Item item) {
		setRcCode(rcCode);
		setItem(item);
	}

	public String getRcCode() {
		return rcCode;
	}

	public void setRcCode(String rcCode) {
		this.rcCode = rcCode;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
}
