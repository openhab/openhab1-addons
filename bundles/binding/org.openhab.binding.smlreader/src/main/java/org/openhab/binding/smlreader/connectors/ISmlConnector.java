/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.connectors;

import org.openmuc.jsml.structures.SML_File;

/**
 * Specifies the generic method to retrieve SML values from a device
 * 
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
public interface ISmlConnector {

	/**
	* Establishes the connection against the device and reads native encoded SML informations.
	*
	* @return native encoded SML informations from a device.
	*/
	SML_File getMeterValues();
}