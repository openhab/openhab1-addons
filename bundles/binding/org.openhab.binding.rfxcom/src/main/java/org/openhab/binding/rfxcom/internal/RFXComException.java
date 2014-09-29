/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal;

/**
 * Exception for RFXCOM errors.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class RFXComException extends Exception {

	private static final long serialVersionUID = 2975102966905930260L;

	public RFXComException() {
		super();
	}

	public RFXComException(String message) {
		super(message);
	}

	public RFXComException(String message, Throwable cause) {
		super(message, cause);
	}

	public RFXComException(Throwable cause) {
		super(cause);
	}

}
