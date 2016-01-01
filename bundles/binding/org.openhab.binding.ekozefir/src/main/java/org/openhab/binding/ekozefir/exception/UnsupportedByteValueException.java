/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.exception;

import java.util.Objects;

/**
 * RuntimeException - throwing when given value of byte from ahu is not supported.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 */
public class UnsupportedByteValueException extends RuntimeException {

	private static final long serialVersionUID = -7483084418703656854L;

	public UnsupportedByteValueException(Class source, int value) {
		super("Byte value: " + Objects.toString(value) + " unsupported in " + Objects.toString(source));
	}

}
