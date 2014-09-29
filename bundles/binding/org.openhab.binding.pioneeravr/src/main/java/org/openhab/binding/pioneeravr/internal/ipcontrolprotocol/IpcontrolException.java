/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pioneeravr.internal.ipcontrolprotocol;

/**
 * Exception for eISCP errors.
 * 
 * @author Rainer Ostendorf
 * @author based on the Onkyo binding by Pauli Anttila and others
 * @since 1.4.0
 */
public class IpcontrolException extends Exception {

	private static final long serialVersionUID = -7970958467980752003L;

	public IpcontrolException() {
		super();
	}

	public IpcontrolException(String message) {
		super(message);
	}

	public IpcontrolException(String message, Throwable cause) {
		super(message, cause);
	}

	public IpcontrolException(Throwable cause) {
		super(cause);
	}

}
