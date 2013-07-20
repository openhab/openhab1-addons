package org.openhab.core.scriptengine.action;

/**
 * This interface must be implemented by services that want to contribute script actions.
 * 
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public interface ActionService {

	/**
	 * returns the FQCN of the action class.
	 * 
	 * @return the FQCN of the action class
	 */
	String getActionClassName();
	
	/**
	 * Returns the action class itself
	 * 
	 * @return the action class
	 */
	Class<?> getActionClass();
	
}
