/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.edimax.internal;

/**
 * Problem occured while discovering devices.
 * 
 * @author Heinz
 *
 */
public class DiscoveryException extends Exception {

	private static final long serialVersionUID = -2990047766796813449L;

	public DiscoveryException(Throwable cause) {
		super(cause);
	}

}
