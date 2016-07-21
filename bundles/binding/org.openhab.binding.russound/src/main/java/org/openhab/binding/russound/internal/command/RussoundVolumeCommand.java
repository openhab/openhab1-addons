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
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;

public class RussoundVolumeCommand extends RussoundCommand {

	public RussoundVolumeCommand(ZoneAddress address) {
		super(address);
		// 1 is controller
		// 17 is zone
		// 15 is volume level
		mCommand = new byte[] { (byte) 0xf0, (byte) 0x00, (byte) 0x00,
				(byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x20,
				(byte) 0x05, (byte) 0x02, (byte) 0x02, (byte) 0x00,
				(byte) 0x00, (byte) 0xf1, (byte) 0x21, (byte) 0x00,
				(byte) 0x12, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01 };
		mCommand[17] = (byte) address.getZone();
		mCommand[1] = (byte) address.getController();
		mCommand[2] = (byte) address.getZone();
	}

	@Override
	public byte[] getCommand(Command command) {
		byte[] returnValue = super.getCommand(command).clone();
		// 15 is the power flag, 1 on, 0 off
		if (command instanceof OnOffType) {
			if (OnOffType.ON.equals(command))
				returnValue[15] = (byte) 0x30;
			else if (OnOffType.OFF.equals(command))
				returnValue[15] = (byte) 0x00;
		} else if (command instanceof PercentType)
			returnValue[15] = (byte) (((PercentType) command).intValue() / 2);
		else
			throw new IllegalArgumentException("Command must be OnOffType or PercentType");
		return returnValue;
	}
}
