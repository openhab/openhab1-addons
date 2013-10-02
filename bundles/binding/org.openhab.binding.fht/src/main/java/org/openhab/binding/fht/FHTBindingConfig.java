package org.openhab.binding.fht;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

public class FHTBindingConfig implements BindingConfig {

	public static enum Datapoint {
		MEASURED_TEMP, DESIRED_TEMP, BATTERY, WINDOW, VALVE;
	}

	private String housecode;
	private String address;

	private Datapoint datapoint;

	private Item item;

	public FHTBindingConfig(Item item, String housecode, String address,
			Datapoint datapoint) {
		this.housecode = housecode;
		this.address = address;
		this.datapoint = datapoint;
		this.item = item;
	}

	public String getHousecode() {
		return housecode;
	}

	public String getAddress() {
		return address;
	}

	public Datapoint getDatapoint() {
		return datapoint;
	}

	public String getFullAddress() {
		if (address != null) {
			return housecode + address;
		}
		return housecode;
	}

	public Item getItem() {
		return item;
	}

}
