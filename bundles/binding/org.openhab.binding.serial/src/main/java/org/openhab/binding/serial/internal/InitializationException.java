/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.serial.internal;

public class InitializationException extends Exception {

	private static final long serialVersionUID = -5106059856757667266L;

	public InitializationException(String msg) {
		super(msg);
	}

	public InitializationException(Throwable cause) {
		super(cause);
	}

	public InitializationException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
