/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmowelcome.internal;

/**
 * @author Ing. Peter Weiss
 * @since 1.8.0
 */
public class NetatmoWelcomeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NetatmoWelcomeException(String message) {
		super(message);
	}
	
	public NetatmoWelcomeException(final Throwable cause) {
		super(cause);
	}

	public NetatmoWelcomeException(final String message, final Throwable cause) {
		super(message, cause);
	}
	
}
