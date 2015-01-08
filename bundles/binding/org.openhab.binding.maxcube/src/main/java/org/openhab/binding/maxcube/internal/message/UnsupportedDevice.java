/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.message;

import java.util.Calendar;

public class UnsupportedDevice extends Device {

	public UnsupportedDevice(Configuration c) {
		super(c);
	}

	@Override
	public DeviceType getType() {
		return DeviceType.Invalid;
	}

	@Override
	public String getName() {
		return "Unsupported device";
	}

	@Override
	public Calendar getLastUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

}
