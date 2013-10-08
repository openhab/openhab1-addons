/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.nikobus.internal.config;

import java.util.List;

import org.openhab.binding.nikobus.internal.NikobusBinding;
import org.openhab.binding.nikobus.internal.core.NikobusCommand;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * Module Channel. Represents a single channel in a Nikobus switch or dimmer
 * module.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class ModuleChannel extends AbstractNikobusItemConfig {

	private ModuleChannelGroup channelGroup;

	private State state = OnOffType.OFF;

	private List<Class<? extends Command>> supportedCommands;

	/**
	 * Restricted default constructor.
	 * 
	 * @param supportedCommands
	 */
	protected ModuleChannel(String name, String address,
			ModuleChannelGroup channelGroup,
			List<Class<? extends Command>> supportedCommands) {
		super(name, address);
		this.channelGroup = channelGroup;
		this.supportedCommands = supportedCommands;
	}

	@Override
	public void processCommand(Command command, NikobusBinding binding)
			throws Exception {

		if (command instanceof PercentType) {
			this.state = (PercentType) command;
		}
		if (command instanceof OnOffType) {
			this.state = (OnOffType) command;
		}

		channelGroup.publishStateToNikobus(this, binding);
	}

	/**
	 * Set the state of the nikobus channel. Changing the state will not trigger
	 * an update command for the parent module.
	 * 
	 * @param newState
	 *            ON/OFF or percent value
	 */
	protected void setState(State newState) {
		this.state = newState;
	}

	/**
	 * @return current state of the channel.
	 */
	public State getState() {
		return state;
	}

	@Override
	public void processNikobusCommand(NikobusCommand command,
			NikobusBinding binding) {
		// noop, implemented by switch module channel group.
	}

	/**
	 * @return true if percent type commands are supported.
	 */
	public boolean supportsPercentType() {
		return supportedCommands.contains(PercentType.class);
	}
}
