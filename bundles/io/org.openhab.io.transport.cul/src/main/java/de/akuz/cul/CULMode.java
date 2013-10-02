package de.akuz.cul;

public enum CULMode {

	// TODO more research on correct CUL receiption modes
	SLOW_RF("X21"), ASK_SIN("X10"), MAX("X10");

	private String command;

	private CULMode(String command) {
		this.command = command;
	}

	public String getCommand() {
		return command;
	}

}
