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
 * Report about unsupported command.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public class UnsupportedCommandException extends CommandException {

	private static final long serialVersionUID = -1983838442832469348L;

	public UnsupportedCommandException(FatekPLCItem fatekPLCItem,
			Command command) {
		super(String.format(
				"Command: %s of class: %s is not supported by %s for item %s",
				command, command.getClass().getName(), fatekPLCItem.getClass()
						.getSimpleName(), fatekPLCItem.getItemName()));
	}
}
