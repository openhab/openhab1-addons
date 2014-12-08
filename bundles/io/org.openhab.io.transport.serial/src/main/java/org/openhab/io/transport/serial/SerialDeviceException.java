/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.io.transport.serial;


/**
 * Serial device exception
 * 
 * @author Johannes Engelke <info@johannes-engelke.de>
 * @since 1.7.0
*/

public class SerialDeviceException extends Exception {
	public SerialDeviceException() {
		super();
	}

	public SerialDeviceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SerialDeviceException(String arg0) {
		super(arg0);
	}

	public SerialDeviceException(Throwable arg0) {
		super(arg0);
	}
	private static final long serialVersionUID = 6534526514968232330L;
}
