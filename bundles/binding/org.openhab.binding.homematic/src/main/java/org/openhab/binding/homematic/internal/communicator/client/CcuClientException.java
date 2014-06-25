/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.client;

/**
 * Exception if something happens in the communication to the CCU.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class CcuClientException extends Exception {
	private static final long serialVersionUID = 76348991234346L;

	public CcuClientException(String message) {
		super(message);
	}

	public CcuClientException(String message, Throwable cause) {
		super(message, cause);
	}
}
