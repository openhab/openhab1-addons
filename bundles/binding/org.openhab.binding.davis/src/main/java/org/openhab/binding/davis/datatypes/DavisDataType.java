/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.davis.datatypes;

import org.openhab.core.types.State;

/**
 * Interface to convert binary data into openHAB states
 * 
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public interface DavisDataType {

	/**
	 * Generate a openhab State object based on response data.
	 * 
	 * @param data
	 * @param valueType
	 * @return converted State object
	 */
	State convertToState(byte[] data, DavisValueType valueType);
	
}
