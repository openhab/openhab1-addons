/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
