/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.russound.internal.command;

import org.openhab.binding.russound.internal.ZoneAddress;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;

public class AllZonePowerCommand extends RussoundCommand {

	public AllZonePowerCommand(ZoneAddress address) {
		super(address);
		
		mCommand = new byte[] { (byte) 0xf0, (byte) 0x7f, (byte) 0x00,
				(byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x70,
				(byte) 0x05, (byte) 0x02, (byte) 0x02, (byte) 0x00,
				(byte) 0x00, (byte) 0xf1, (byte) 0x22, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01 };
	}

	@Override
	public byte[] getCommand(Command command) {
		byte[] myBytes = super.getCommand(command);
		byte[] returnValue = myBytes.clone();
		// 15 is the power flag, 1 on, 0 off
		if (command instanceof OnOffType)
			if (OnOffType.ON.equals(command)) {
				// disallow this for now
				// returnValue[16] = (byte) 0x01;
			} else if (OnOffType.OFF.equals(command))
				returnValue[16] = (byte) 0x00;
			else
				throw new IllegalArgumentException("Command must be OnOffType");
		else
			throw new IllegalArgumentException("Command must be OnOffType");
		return returnValue;
	}
}
