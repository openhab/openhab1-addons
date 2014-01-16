/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

import java.util.HashMap;
import org.openhab.binding.plcbus.internal.protocol.commands.*;

/**
 * Provider for creating PLCBusCommmands by Commandbyte
 * 
 * @author Robin Lenz
 * @since 1.0.0
 */
public class CommandProvider {
	
	private HashMap<Byte, Class<? extends Command>> commands;

	/**
	 * Create a new CommandProvider
	 */
	public CommandProvider() {
		commands = new HashMap<Byte, Class<? extends Command>>();
		init();
	}

	/**
	 * Create the Command for commandByte
	 * 
	 * @param commandByte
	 *            of Command
	 * @return created Command
	 */
	public Command getCommandBy(byte commandByte) {
		if (!commands.containsKey(commandByte)) {
			return null;
		}

		Class<? extends Command> commandClass = commands.get(commandByte);

		return createCommandFrom(commandClass);
	}

	private void init() {
		add(UnitOn.class);
		add(UnitOff.class);
		add(Bright.class);
		add(Dim.class);
		add(FadeStop.class);
		add(StatusRequest.class);
		add(StatusOn.class);
		add(StatusOff.class);
	}

	private void add(Class<? extends Command> type) {
		Command command = createCommandFrom(type);

		if (command != null) {
			commands.put(command.getId(), type);
		}
	}

	private Command createCommandFrom(Class<? extends Command> type) {
		try {
			return (Command) type.getConstructor(null).newInstance(null);
		} catch (Exception e) {
			return null;
		}
	}
	
}
