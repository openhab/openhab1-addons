/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
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
