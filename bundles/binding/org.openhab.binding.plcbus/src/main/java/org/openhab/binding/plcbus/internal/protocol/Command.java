/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

import java.util.*;

/**
 * Base Class for a PLC Bus Command
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public abstract class Command {

	private byte data1;
	private byte data2;

	/**
	 * Returns the Id of the command
	 * 
	 * @return Id of command
	 */
	public abstract byte getId();

	/**
	 * Returns the first DataByte of Command
	 * 
	 * @return First DataByte of Command
	 */
	protected byte getData1() {
		return data1;
	}

	/**
	 * Sets the first DataByte
	 */
	protected void setData1(byte data) {
		this.data1 = data;
	}

	/**
	 * Returns the second DataByte of Command
	 * 
	 * @return Second DataByte of Command
	 */
	protected byte getData2() {
		return data2;
	}

	/**
	 * Sets the second DataByte
	 */
	protected void setData2(byte data) {
		this.data2 = data;
	}

	/**
	 * Return the DataBytes of the command
	 * 
	 * @return DataBytes
	 */
	public List<Byte> getDataBytes() {
		List<Byte> result = new ArrayList<Byte>();

		result.add(data1);
		result.add(data2);

		return result;
	}

	/**
	 * Parse the bytes
	 * 
	 * @param data
	 *            Data to parses
	 */
	public void parse(byte[] data) {
		data1 = data[0];
		data2 = data[1];
	}

}
