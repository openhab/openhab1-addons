/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
