/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.freebox;

import org.openhab.binding.freebox.internal.CommandType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * This is a class that stores Freebox binding configuration elements :
 * 	 - the type of command to operate on the freebox
 * 	 - an optional parameter for the type of command
 *   - an interface to related openHAB item
 * @author clinique
 * @since 1.5.0
 *
 */

public class FreeboxBindingConfig implements BindingConfig {
	public CommandType commandType;
	public String commandParam;
	public Item item;
	
	public FreeboxBindingConfig(CommandType commandType, String commandParam, Item item) {
		this.commandType = commandType;
		this.commandParam = commandParam;
		this.item = item;
	}
}
