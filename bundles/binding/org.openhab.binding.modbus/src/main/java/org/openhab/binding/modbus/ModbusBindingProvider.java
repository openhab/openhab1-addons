/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus;

import org.openhab.binding.modbus.internal.ModbusGenericBindingProvider.ModbusBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can map openHAB items to Modbus
 * binding types.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public interface ModbusBindingProvider extends BindingProvider {

	/**
	 * Allowed slave device types "coil" corresponds to discrete output Coils
	 * (read/write) "discrete" corresponds to discrete input contacts (read
	 * only) "holding" corresponds to analog output holding registers
	 * (read/write) "input" corresponds to analog input registers (read only)
	 */
	static final public String TYPE_COIL = "coil";
	static final public String TYPE_DISCRETE = "discrete";
	static final public String TYPE_HOLDING = "holding";
	static final public String TYPE_INPUT = "input";

	static final String[] SLAVE_DATA_TYPES = { TYPE_COIL, TYPE_DISCRETE, TYPE_HOLDING, TYPE_INPUT };

	/**
	 * Value type, primary for "input" type
	 */
	static final public String VALUE_TYPE_BIT     = "bit";
	static final public String VALUE_TYPE_INT8    = "int8";
	static final public String VALUE_TYPE_UINT8   = "uint8";
	static final public String VALUE_TYPE_INT16   = "int16";
	static final public String VALUE_TYPE_UINT16  = "uint16";
	static final public String VALUE_TYPE_INT32   = "int32";
	static final public String VALUE_TYPE_UINT32  = "uint32";
	static final public String VALUE_TYPE_FLOAT32 = "float32";

	static final String[] VALUE_TYPES = { VALUE_TYPE_BIT, VALUE_TYPE_INT8, VALUE_TYPE_UINT8, VALUE_TYPE_INT16, VALUE_TYPE_UINT16, VALUE_TYPE_INT32, VALUE_TYPE_UINT32, VALUE_TYPE_FLOAT32 };

	/**
	 * Returns Modbus item configuration
	 * 
	 * @param itemName item name
	 * @return Modbus item configuration
	 */
	ModbusBindingConfig getConfig(String itemName);

}
