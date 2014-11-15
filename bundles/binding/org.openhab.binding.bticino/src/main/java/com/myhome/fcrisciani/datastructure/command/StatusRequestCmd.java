/**
 * This class represent a Status/Request command in the OpenWebNet
 * For further details check the OpenWebNet reference
 */
package com.myhome.fcrisciani.datastructure.command;

/**
 * @author Flavio Crisciani
 *
 */
public class StatusRequestCmd extends CommandOPEN {
	// ----- TYPES ----- //

	// ---- MEMBERS ---- //

	// ---- METHODS ---- //
	
	/**
	 * Create a new instance of a StatusRequestCommand
	 * @param commandString command as a string
	 * @param who who field 
	 * @param where where field
	 */
	public StatusRequestCmd(String commandString, String who, String where) {
		super(commandString, 1, who, where);
		;
	}
	/**
	 * Generate automatically a new Status Request Command passing basic argument
	 * @param who who field
	 * @param where where field
	 */
	public StatusRequestCmd(String who, String where){
		super("*#"+who+"*"+where+"##", 1, who, where);
		;
	}
	
}
