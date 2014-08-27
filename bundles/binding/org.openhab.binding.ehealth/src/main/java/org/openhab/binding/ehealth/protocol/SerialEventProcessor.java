/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ehealth.protocol;

/**
 * The SerialEventProcessor has capabilities to process datagrams
 * of type String.
 * 
 * @author Thomas Eichstaedt-Engelen
 * @since 1.6.0
 */
public interface SerialEventProcessor {
	
	/**
	 * Is called whenever a new datagram has been read from the serial port
	 * 
	 * @param data the data being read from the serial port.
	 */
	void processSerialData(String data);

}
