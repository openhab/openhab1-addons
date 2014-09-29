/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nikobus.internal.core;

import org.openhab.binding.nikobus.internal.NikobusBinding;

/**
 * Basic interface to be implemented by all devices which want to listen to
 * commands received on the Nikobus.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public interface NikobusCommandListener {

	/**
	 * Process a command received from the Nikobus. All commands are broadcasted
	 * to all receivers, so it is up to the receiver to only act on the
	 * appropriate commands.
	 * 
	 * @param command
	 *            as it was read from serial port without CR.
	 */
	public void processNikobusCommand(NikobusCommand command,
			NikobusBinding binding);

	/**
	 * Get the name of the item.
	 * 
	 * @return item name
	 */
	public String getName();
}
