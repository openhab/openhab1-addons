/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fht.internal;

/**
 * Class to store commands to manipulate registers.
 * 
 * @author Till Klocke
 * @since 1.4.0
 * 
 */
public class WriteRegisterCommand {

	public String register;
	public String value;

	public WriteRegisterCommand(String register, String value) {
		this.register = register;
		this.value = value;
	}

}
