/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.internal.cmd;

import org.openhab.binding.dmx.DmxService;

/**
 * DMX Command Interface. To be used for all different DMX commands.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public interface DmxCommand {

	/**
	 * List of available DMX Commands.
	 */
	public static enum types {
		FADE, SFADE
	}

	/**
	 * Execute a DMX command.
	 * 
	 * @param service
	 *            on which to execute the command.
	 */
	public void execute(DmxService service);

}
