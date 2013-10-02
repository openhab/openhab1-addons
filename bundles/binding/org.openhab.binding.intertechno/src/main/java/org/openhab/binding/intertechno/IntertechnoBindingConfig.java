package org.openhab.binding.intertechno;

import org.openhab.core.binding.BindingConfig;

public class IntertechnoBindingConfig implements BindingConfig {

	private String address;

	private String commandOff;
	private String commandOn;

	public IntertechnoBindingConfig(String address, String commandOn,
			String commandOff) {
		this.address = address;
		this.commandOn = commandOn;
		this.commandOff = commandOff;
	}

	public String getAddress() {
		return address;
	}

	public String getCommandValueON() {
		return commandOn;
	}

	public String getCommandValueOFF() {
		return commandOff;
	}

}
