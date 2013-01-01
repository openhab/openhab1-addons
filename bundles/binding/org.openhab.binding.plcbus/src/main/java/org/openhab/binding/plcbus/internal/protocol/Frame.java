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
 * Baseclass for Frames in PLCBus Protocol
 * 
 * @author Robin Lenz
 * @since 1.1.0
 * 
 * @param <TDataFrame> Type of the containing DataFrame
 */
public abstract class Frame<TDataFrame extends DataFrame> {
	
	final static byte START_BYTE = 0x02;

	protected TDataFrame data;

	public Frame() {

	}

	public Frame(TDataFrame data) {
		this.data = data;
	}

	public byte getStartByte() {
		return START_BYTE;
	}

	public abstract byte getEndByte();

	public int getLength() {
		if (data == null) {
			return 0;
		}

		return data.getBytes().size();

	}

	public TDataFrame getData() {
		return data;
	}

	public List<Byte> getBytes() {
		List<Byte> result = new ArrayList<Byte>();

		result.add(getStartByte());
		result.add(Convert.toByte(getLength()));

		if (data != null) {
			result.addAll(data.getBytes());
		}

		result.add(getEndByte());

		return result;
	}

	public Command getCommand() {
		return data.getCommand();
	}
	
}
