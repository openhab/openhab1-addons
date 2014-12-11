/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.converter.state;

import org.openhab.binding.homematic.internal.model.HmValueItem;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;

/**
 * Converter interface for converting between openHab states and Homematic objects.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public interface Converter<T extends State> {
	
	/**
	 * Converts a openHAB type to a Homematic object.
	 */
	public Object convertToBinding(Type type, HmValueItem hmValueItem);

	/**
	 * Converts a Homematic object to a openHAB type.
	 */
	public T convertFromBinding(HmValueItem hmValueItem);

}
