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


public class RequestZoneInfoCommand extends RussoundCommand {

	public RequestZoneInfoCommand(ZoneAddress address) {
		super(address);

		mCommand = new byte[] { (byte) 0xf0, (byte) 0x00, (byte) 0x00,
				(byte) 0x7f, (byte) 0x00, (byte) 0x00, (byte) 0x70,
				(byte) 0x01, (byte) 0x04, (byte) 0x02, (byte) 0x00,
				(byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x00 };
		mCommand[11] = (byte) address.getZone();
		mCommand[1] = (byte) address.getController();
	}

}