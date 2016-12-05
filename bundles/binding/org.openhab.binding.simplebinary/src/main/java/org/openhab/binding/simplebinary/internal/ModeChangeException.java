/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

import org.openhab.binding.simplebinary.internal.SimpleBinaryByteBuffer.BufferMode;

/**
 * Exception class for SimpleBinaryByteBuffer. Raised when performed wrong operation in mode read or write
 * 
 * @author Vita Tucek
 * @since 1.8.0
 */
public class ModeChangeException extends Exception {
	private static final long serialVersionUID = -2494344117743188391L;

	public ModeChangeException() {
		super("Operation not supported in given mode");
	}

	public ModeChangeException(String msg) {
		super(msg);
	}

	public ModeChangeException(String operation, BufferMode mode) {
		super("Operation " + operation + " is not supported in mode " + mode.toString());
	}

}
