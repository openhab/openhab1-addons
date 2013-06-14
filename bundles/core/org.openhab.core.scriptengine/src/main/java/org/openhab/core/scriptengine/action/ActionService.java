package org.openhab.core.scriptengine.action;

import java.lang.reflect.Method;


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
	
	/**
	 * returns the help text to be used by the designer for a certain method of the action.
	 * (TODO: insert exact format description here)
	 * 
	 * @param m the method to return the help text for
	 * @return the help text
	 */
	String getActionHelpText(Method m);
}
