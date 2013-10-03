package de.akuz.cul;

/**
 * This enum represents the different RF modes in which the CUL can work. Based
 * on this enum a culfw based device will be configured when openend for the
 * first time.
 * 
 * @author Till Klocke
 * 
 */
public enum CULMode {

	// TODO more research on correct CUL receiption modes
	/**
	 * Slow RF mode for FS20, FHT etc. Intertechno also works in this mode.
	 */
	SLOW_RF("X21"),
	/**
	 * Fast RF mode for Homematic. Intertechno should also work in this mode.
	 */
	ASK_SIN("X10"),
	/**
	 * Fast RF mode for the Moritz protocol of the Max! heating control system.
	 * Intertechno should also work in this mode.
	 */
	MAX("X10");

	private String command;

	private CULMode(String command) {
		this.command = command;
	}

	public String getCommand() {
		return command;
	}

}
