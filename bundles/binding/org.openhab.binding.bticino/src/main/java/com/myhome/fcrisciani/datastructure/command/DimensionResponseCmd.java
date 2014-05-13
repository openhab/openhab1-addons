/**
 * This command represent an OpenWebNet dimension response
 * See OpenWebNet manual for further documentation
 */
package com.myhome.fcrisciani.datastructure.command;
/**
 * @author Flavio Crisciani
 *
 */
public class DimensionResponseCmd extends CommandOPEN {
	
	// ----- TYPES ----- //

	// ---- MEMBERS ---- //
	
	int dimension = -1;					// Dimension type (see documentation for possible values)
	String[] values = null;				// List of values in the message
	
	// ---- METHODS ---- //
	/**
	 * Create a new Dimension response instance
	 * @param commandString command in string format
	 * @param who who field
	 * @param where where field
	 * @param dimension dimension type 
	 * @param values list of values of the specified dimension
	 */
	public DimensionResponseCmd(String commandString, String who, String where, String dimension, String[] values) {
		super(commandString, 4, who, where);
		this.dimension = Integer.parseInt(dimension);
		this.values = values;
	}

	/**
	 * Get dimension type
	 * @return dimension type
	 */
	public int getDimension() {
		return dimension;
	}
	/**
	 * Get list of values
	 * @return
	 */
	public String[] getValues() {
		return values;
	}
	
}
