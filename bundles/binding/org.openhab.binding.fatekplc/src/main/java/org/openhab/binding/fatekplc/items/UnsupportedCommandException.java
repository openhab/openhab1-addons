/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
