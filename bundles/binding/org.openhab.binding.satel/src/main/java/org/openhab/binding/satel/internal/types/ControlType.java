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
 * Interface for all types of control.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public interface ControlType {

	/**
	 * Returns Satel command to control state for this kind of object type.
	 * 
	 * @return command identifier
	 */
	byte getControlCommand();

	/**
	 * Returns object type for this kind of control.
	 * 
	 * @return Integra object type
	 */
	ObjectType getObjectType();
}
