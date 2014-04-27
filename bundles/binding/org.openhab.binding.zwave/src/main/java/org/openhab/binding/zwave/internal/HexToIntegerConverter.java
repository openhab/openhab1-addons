/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Implements a Hex value to integer converter and vice versa.
 * 
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class HexToIntegerConverter implements Converter {

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(Integer.class) || type.equals(int.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		int number = (Integer) source;
		writer.setValue("0x" + Integer.toHexString(number));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		String value = reader.getValue();
		if(value.startsWith("0x"))
			return Integer.decode(value);
		else
			return Integer.parseInt(value, 16);
	}

}