/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.panasonictv;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/*
 * An instance of this class is created for every declared item. It stores
 * the item itself, the related TV (based on the registered TVs in the openhab.cfg)
 * an the declared command 
 * 
 * @author André Heuer
 * @since 1.7.0
 */
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