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

public class RussoundPowerCommand extends RussoundCommand {

	public RussoundPowerCommand(ZoneAddress address) {
		 super(address);

		mCommand = new byte[] { (byte) 0xf0, (byte) 0x00, (byte) 0x00,
				(byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x70,
				(byte) 0x05, (byte) 0x02, (byte) 0x02, (byte) 0x00,
				(byte) 0x00, (byte) 0xf1, (byte) 0x23, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0x01 };
		mCommand[17] = (byte) address.getZone();
		mCommand[1] = (byte) address.getController();
	}

	@Override
	public byte[] getCommand(Command command) {
		byte[] myBytes = super.getCommand(command);
		byte[] returnValue = myBytes.clone();
		// 15 is the power flag, 1 on, 0 off
		if (command instanceof OnOffType)
			if (OnOffType.ON.equals(command))
				returnValue[15] = (byte) 0x01;
			else if (OnOffType.OFF.equals(command))
				returnValue[15] = (byte) 0x00;
			else
				throw new IllegalArgumentException("Command must be OnOffType");
		else
			throw new IllegalArgumentException("Command must be OnOffType");
		return returnValue;
	}

}
