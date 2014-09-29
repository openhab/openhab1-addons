/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onkyo.internal.eiscp;

/**
 * Exception for eISCP errors.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class EiscpException extends Exception {

	private static final long serialVersionUID = -7970958467980752003L;

	public EiscpException() {
		super();
	}

	public EiscpException(String message) {
		super(message);
	}

	public EiscpException(String message, Throwable cause) {
		super(message, cause);
	}

	public EiscpException(Throwable cause) {
		super(cause);
	}

}
