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
 * RuntimeException - throwing when bytes of response from ahu is not
 * correct(different than 51).
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class ResponseBytesNumberException extends RuntimeException {

	private static final long serialVersionUID = -7232402546834495244L;

	public ResponseBytesNumberException(int messageByteNumber) {
		super("Wrong bytes number in response: " + Objects.toString(messageByteNumber));
	}
}
