/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol;

/**
 * Exceptions thrown from the serial interface.
 * @author Jan-Willem Spuij
 * @since 1.3.0
 */
public class SerialInterfaceException extends Exception {

	private static final long serialVersionUID = 8852643957484264124L;

	/**
	 * Constructor. Creates a new instance of SerialInterfaceException.
	 */
	public SerialInterfaceException() {
	}

	/**
	 * Constructor. Creates a new instance of SerialInterfaceException.
	 * @param message the detail message.
	 */
	public SerialInterfaceException(String message) {
		super(message);
	}

	/**
	 * Constructor. Creates a new instance of SerialInterfaceException.
	 * @param cause the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public SerialInterfaceException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructor. Creates a new instance of SerialInterfaceException.
	 * @param message the detail message.
	 * @param cause the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public SerialInterfaceException(String message, Throwable cause) {
		super(message, cause);
	}
}
