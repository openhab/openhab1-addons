/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.knx.config;

import org.openhab.core.types.Type;

import tuwien.auto.calimero.datapoint.Datapoint;

/**
 * This interface must be implemented by classes that provide a type mapping
 * between openHAB and KNX.
 * When a command or status update is sent to an item on the openHAB event bus,
 * it must be clear, in which format it must be sent to KNX and vice versa.
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public interface KNXTypeMapper {

	/**
	 * maps an openHAB command/state to a string value which correspond to its datapoint in KNX
	 *  
	 * @param type a command or state
	 * @param dpt the corresponding datapoint type
	 * @return datapoint value as a string
	 */
	public String toDPTValue(Type type, String dpt);

	/**
	 * maps a datapoint value to an openHAB command or state
	 * 
	 * @param datapoint the source datapoint 
	 * @param data the datapoint value as an ASDU byte array (see <code>{@link ProcessEvent}.getASDU()</code>)
	 * @return a command or state of openHAB
	 */
	public Type toType(Datapoint datapoint, byte[] data);

}
