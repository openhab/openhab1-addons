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

import org.openhab.core.types.Command;

/**
 * RuntimeException - throwing when given type of state from openhab is not supported.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class UnexpectedCommandException extends RuntimeException {

	private static final long serialVersionUID = -4276504190857635461L;

	public UnexpectedCommandException(Command expected) {
		super("Unexpected command: " + Objects.toString(expected));
	}
}
