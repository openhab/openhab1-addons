/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.panasonictv;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

public class PanasonicTVBindingConfig implements BindingConfig {
	String tv, command;
	Item item;

	public PanasonicTVBindingConfig(Item item, String tv, String command) {
		super();
		this.item = item;
		this.tv = tv;
		this.command = command;
	}

	public String getTv() {
		return tv;
	}

	public String getCommand() {
		return command;
	}
}