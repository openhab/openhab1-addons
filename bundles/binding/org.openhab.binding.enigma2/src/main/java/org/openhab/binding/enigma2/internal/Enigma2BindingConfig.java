/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enigma2.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * Wrapper class for a Enigma2Binding Configuration
 * 
 * @author Sebastian Kutschbach
 * @since 1.6.0
 * 
 */
public class Enigma2BindingConfig implements BindingConfig {

	private Item item;
	private String deviceId;
	private Enigma2Command cmdId;
	private String cmdValue;

	public Enigma2BindingConfig(Item item, String deviceId,
			Enigma2Command cmdId, String cmdValue) {
		this.item = item;
		this.deviceId = deviceId;
		this.cmdId = cmdId;
		this.cmdValue = cmdValue;
	}

	public Item getItem() {
		return item;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public Enigma2Command getCmdId() {
		return cmdId;
	}

	public String getCmdValue() {
		return cmdValue;
	}
}