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
import org.openhab.core.types.Command;

public abstract class RussoundCommand {
	private ZoneAddress mZoneAddress;
	protected byte[] mCommand;
	
	protected RussoundCommand(ZoneAddress address) {
		mZoneAddress = address;
	}

	private enum CommandType {
		power, volume, allpower, source;

	}

	public ZoneAddress getZoneAddress() {
		return mZoneAddress;
	}

	

	public byte[] getCommand(Command command) {
		return mCommand.clone();
	}

	public static RussoundCommand getCommandFromItemFileDescription(
			String itemFileDescription, ZoneAddress address) {
		CommandType type = CommandType.valueOf(itemFileDescription);
		if (type != null) {
			switch (type) {
			case power:
				return new RussoundPowerCommand(address);
			case volume:
				return new RussoundVolumeCommand(address);
			case source:
				// TODO - should implement changing source
				return new RussoundVolumeCommand(address);
			case allpower:
				return new AllZonePowerCommand(address);
			default:
				throw new IllegalArgumentException(
						"not a valid CommandType description");
			}
		} else
			throw new IllegalArgumentException(
					"not a valid CommandType description");
	}
}
