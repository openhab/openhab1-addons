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
 * RuntimeException - throwing when cannot find ahu name.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class AhuNameNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4544398591112251308L;

	public AhuNameNotFoundException(Character centralName) {
		super("Central name " + Objects.toString(centralName) + " not found");
	}
}
