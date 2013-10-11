package org.openhab.binding.fht.internal;

public class WriteRegisterCommand {

	public String register;
	public String value;

	public WriteRegisterCommand(String register, String value) {
		this.register = register;
		this.value = value;
	}

}
