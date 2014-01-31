/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

/**
 * Factory for PLC Bus Commands
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class CommandFactory {
	
	private static CommandProvider provider = new CommandProvider();

	/**
	 * Create the Command for Commandbyte
	 * 
	 * @param Byte of Command
	 * @return created Command
	 */
	public static Command createBy(byte commandByte) {
		return provider.getCommandBy(commandByte);
	}

}
