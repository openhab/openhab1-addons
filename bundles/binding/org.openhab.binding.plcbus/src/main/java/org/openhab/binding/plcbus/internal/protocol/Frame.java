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
