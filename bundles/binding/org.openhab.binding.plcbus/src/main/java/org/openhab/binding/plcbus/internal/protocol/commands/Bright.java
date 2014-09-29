/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol.commands;

import org.openhab.binding.plcbus.internal.protocol.Command;
import org.openhab.binding.plcbus.internal.protocol.Convert;

/**
 * Bright Command in PLCBus Protocol
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class Bright extends Command {

	/**
	 * @{inheritDoc
	 */
	@Override
	public byte getId() {
		return 0x05;
	}

	/**
	 * Faderate in Seconds
	 * 
	 * @return Faderate in Seconds
	 */
	public int getSeconds() {
		return getData1();
	}

	/**
	 * Sets the Faderate
	 * 
	 * @param seconds
	 *            Faderate in Seconds
	 */
	public void setSeconds(int seconds) {
		setData1(Convert.toByte(seconds));
	}

}
