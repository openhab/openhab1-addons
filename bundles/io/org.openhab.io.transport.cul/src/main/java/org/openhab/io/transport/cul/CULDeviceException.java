/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul;

/**
 * An exception which represents error while opening/connecting to the culfw
 * based device.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class CULDeviceException extends Exception {

	public CULDeviceException() {
		super();
	}

	public CULDeviceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public CULDeviceException(String arg0) {
		super(arg0);
	}

	public CULDeviceException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 4834148919102194993L;

}
