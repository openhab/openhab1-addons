package org.openhab.binding.fht.internal;

public class FHTDesiredTemperatureCommand {

	private String address;
	private String command;

	public FHTDesiredTemperatureCommand(String address, String command) {
		this.address = address;
		this.command = command;
	}

	public String getAddress() {
		return address;
	}

	public String getCommand() {
		return command;
	}

}
