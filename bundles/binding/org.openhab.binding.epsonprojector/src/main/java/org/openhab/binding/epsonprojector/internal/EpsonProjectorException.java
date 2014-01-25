/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.epsonprojector.internal;

/**
 * Exception for Epson projector errors.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class EpsonProjectorException extends Exception {

	private static final long serialVersionUID = -8048415193494625295L;

	public EpsonProjectorException() {
		super();
	}

	public EpsonProjectorException(String message) {
		super(message);
	}

	public EpsonProjectorException(String message, Throwable cause) {
		super(message, cause);
	}

	public EpsonProjectorException(Throwable cause) {
		super(cause);
	}

}
