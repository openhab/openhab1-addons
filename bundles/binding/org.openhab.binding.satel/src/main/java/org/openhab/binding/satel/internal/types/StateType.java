/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.types;

/**
 * Base of all kinds of Integra state.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public interface StateType {
	
	/**
	 * Returns Satel command to get current state for this state type.
	 * @return command identifier
	 */
	byte getRefreshCommand();
	
	/**
	 * Returns object type for this kind of state.
	 * @return Integra object type
	 */
	ObjectType getObjectType();
}
