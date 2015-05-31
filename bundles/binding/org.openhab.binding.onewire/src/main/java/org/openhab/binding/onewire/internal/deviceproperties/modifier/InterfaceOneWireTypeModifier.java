/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.deviceproperties.modifier;

import org.openhab.core.types.Type;

/**
 * Interface to implement TypeModifier
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 * 
 */
public interface InterfaceOneWireTypeModifier {

	/**
	 * @return name of the modifier
	 */
	public String getModifierName();

	/**
	 * @param pvType
	 * @return modify read Value
	 */
	public Type modify4Read(Type pvType);

	/**
	 * @param pvType
	 * @return modify write Value 
	 */
	public Type modify4Write(Type pvType);

}
