/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.fatekplc.items;

import org.openhab.core.types.Command;

/**
 * Exception for handling problems with commands.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public class CommandException extends Exception {

	private static final long serialVersionUID = -3052967909079047774L;

	public CommandException(String message) {
		super(message);
	}

	public CommandException(FatekPLCItem item, Command command,
			Throwable cause) {
		super(String.format("Error command: %s for %s item %s", command, item
				.getClass().getSimpleName(), item.getItemName()), cause);
	}

	public CommandException(FatekPLCItem item, Command command, String message) {
		super(String.format("Error command: %s for %s item %s %s", command,
				item.getClass().getSimpleName(), item.getItemName(),
				message));
	}
}
