/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.russound;

import org.openhab.binding.russound.internal.ZoneAddress;
import org.openhab.binding.russound.internal.command.RussoundCommand;
import org.openhab.core.binding.BindingConfig;

/**
 * This is a helper class holding binding specific configuration details
 * 
 * @author Hamilton
 * @since 1.7.0
 */
public class RussoundBindingConfig implements BindingConfig {
	// put member fields here which holds the parsed values
	private RussoundCommand mCommand;
	private ZoneAddress mAddress;

	public RussoundBindingConfig(ZoneAddress address,
			RussoundCommand command) {
		mAddress = address;
		mCommand = command;
	}

	public RussoundCommand getCommand() {
		return mCommand;
	}
	public ZoneAddress getZoneAddress()
	{
		return mAddress;
	}
	public String toString()
	{
		return "Binding Config: " + mAddress + "Command: " + mCommand;
	}
}
