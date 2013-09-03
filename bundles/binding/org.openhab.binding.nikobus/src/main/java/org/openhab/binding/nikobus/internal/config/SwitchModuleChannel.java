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

import org.openhab.binding.nikobus.internal.NikobusBinding;
import org.openhab.binding.nikobus.internal.core.NikobusCommand;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * Switch Channel. Represents a single switch channel in a Nikobus switch
 * module.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class SwitchModuleChannel extends AbstractNikobusItemConfig {

	private SwitchModuleChannelGroup channelGroup;

	private State state = OnOffType.OFF;

	/**
	 * Restricted default constructor.
	 */
	protected SwitchModuleChannel(String name, String address,
			SwitchModuleChannelGroup channelGroup) {
		super(name, address);
		this.channelGroup = channelGroup;
	}

	@Override
	public void processCommand(Command command, NikobusBinding binding) throws Exception {

		if (!(command instanceof OnOffType)) {
			return;
		}

		this.state = (OnOffType) command;
		channelGroup.publishStateToNikobus(this, binding);
	}

	/**
	 * Set the state of the nikobus channel. Changing the state will not trigger
	 * an update command for the parent switch module.
	 * 
	 * @param newState
	 *            ON or OFF.
	 */
	protected void setState(OnOffType newState) {
		this.state = newState;
	}

	/**
	 * @return current state of the channel. Can be ON or OFF.
	 */
	public State getState() {
		return state;
	}

	@Override
	public void processNikobusCommand(NikobusCommand command, NikobusBinding binding) {
		// noop, implemented by switch module channel group.
	}

}
