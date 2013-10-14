/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
