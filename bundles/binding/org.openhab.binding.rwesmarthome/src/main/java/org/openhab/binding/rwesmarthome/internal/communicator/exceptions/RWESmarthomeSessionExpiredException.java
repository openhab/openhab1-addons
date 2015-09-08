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
 * @author ollie-dev
 *
 */
public class RWESmarthomeSessionExpiredException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2799402410863391169L;

	/**
	 * 
	 */
	public RWESmarthomeSessionExpiredException() {
		super();
	}

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public RWESmarthomeSessionExpiredException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	/**
	 * @param detailMessage
	 */
	public RWESmarthomeSessionExpiredException(String detailMessage) {
		super(detailMessage);
	}

	/**
	 * @param throwable
	 */
	public RWESmarthomeSessionExpiredException(Throwable throwable) {
		super(throwable);
	}
}
