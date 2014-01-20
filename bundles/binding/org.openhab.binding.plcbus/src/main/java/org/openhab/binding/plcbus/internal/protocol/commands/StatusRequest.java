/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol.commands;

import org.openhab.binding.plcbus.internal.protocol.Command;

/**
 * StatusRequest Command in PLCBus Protocol
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class StatusRequest extends Command {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getId() {
		return 0x0F;
	}

	/**
	 * Returns the first Parameter
	 * 
	 * @return First Parameter
	 */
	public int getParameter1() {
		return getData1();
	}

	/**
	 * Returns the second Parameter
	 * 
	 * @return Seconds Parameter
	 */
	public int getParameter2() {
		return getData2();
	}
}
