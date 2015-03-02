/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.devices;

import org.openhab.binding.smlreader.conversion.IUnitConverter;

/**
 * Specifies methods to read SML capable devices
 * 
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
public interface ISmlDevice {
	/**
	* Is called to initiate the read request.
	*/
	void readSmlValuesFromDevice();

	/**
	* Has to return the value of the specified obis code. 
	*/
	@SuppressWarnings("rawtypes")
	String getObisValue(String obis, Class<? extends IUnitConverter> unitConverter);
}



