/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nibeheatpump.internal;

/**
 * Exception for Nibe heat pump errors.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class NibeHeatPumpException extends Exception {

	private static final long serialVersionUID = 8030315127747955747L;

	public NibeHeatPumpException() {
		super();
	}

	public NibeHeatPumpException(String message) {
		super(message);
	}

	public NibeHeatPumpException(String message, Throwable cause) {
		super(message, cause);
	}

	public NibeHeatPumpException(Throwable cause) {
		super(cause);
	}

}
