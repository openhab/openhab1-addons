/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.internal.device;

/**
 * Exception for unsuported devices.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public class DeviceNotSupportedException extends Exception {

	private static final long serialVersionUID = 1L;

	public DeviceNotSupportedException(String message) {
		super(message);
	}
}
