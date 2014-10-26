/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal;

/**
 * @author John Cocula
 */
public class EcobeeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EcobeeException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public EcobeeException(final Throwable cause) {
		super(cause);
	}
}
