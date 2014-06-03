/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.message;
import java.util.HashMap;

/**
 * Defines the data types that can be used in the fields of a message.
 * 
 * @author Daniel Pfrommer
 * @since 1.5.0
 */
public enum DataType {
	BYTE("byte", 1),
	INT("int", 4),
	FLOAT("float", 4),
	ADDRESS("address", 3),
	INVALID("INVALID", -1);
	
	private static HashMap<String, DataType> s_typeMap = new HashMap<String, DataType>();

	private int	m_size = -1;	// number of bytes consumed
	private String m_name = "";
	
	static {
		s_typeMap.put(BYTE.getName(), BYTE);
		s_typeMap.put(INT.getName(), INT);
		s_typeMap.put(FLOAT.getName(), FLOAT);
		s_typeMap.put(ADDRESS.getName(), ADDRESS);
	}
	/**
	 * Constructor
	 * @param name the name of the data type
	 * @param size the size (in bytes) of this data type 
	 */
	DataType(String name, int size) {
		m_size	= size;
		m_name = name;
	}
	/**
	 * @return the size (in bytes) of this data type
	 */
	public int		 getSize()		{ return m_size; }
	/**
	 * @return clear text string with the name
	 */
	public String   getName() 		{ return m_name; }

	/**
	 * Turns a string into the corresponding data type
	 * @param name the string to translate to a type
	 * @return the data type corresponding to the name string, or null if not found
	 */
	public static DataType s_getDataType(String name) {
		return s_typeMap.get(name);
	}
}
