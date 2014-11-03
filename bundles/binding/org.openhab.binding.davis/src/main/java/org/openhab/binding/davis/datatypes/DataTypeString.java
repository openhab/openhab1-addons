/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.davis.datatypes;

import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;

/**
 * Class to handle numeric values encoding rain based on rain clicks
 * 
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public class DataTypeString implements DavisDataType {

	/**
	 * {@inheritDoc}
	 */
	public State convertToState(byte[] data, DavisValueType valueType) {
		return new StringType(new String(data));
	}
	
}
