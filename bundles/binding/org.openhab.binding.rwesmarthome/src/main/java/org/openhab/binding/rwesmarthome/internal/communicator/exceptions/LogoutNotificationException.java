/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal.communicator.exceptions;

/**
 * Exception if user was logged out.
 * 
 * @author ollie-dev
 *
 */
public class LogoutNotificationException extends SHFunctionalException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3885036245387829505L;

	/**
	 * Constructor with message.
	 * 
	 * @param message
	 */
	public LogoutNotificationException(String message) {
		super(message);
	}
}
