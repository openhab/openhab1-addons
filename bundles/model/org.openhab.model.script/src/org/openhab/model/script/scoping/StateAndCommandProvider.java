/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.script.scoping;

import java.util.HashSet;
import java.util.Set;

import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

import com.google.inject.Singleton;

/**
 * This is a class which provides all available states and commands (obviously only the enum-based ones with a fixed name).
 * A future version might gather the sets through an extension mechanism, for the moment it is simply statically coded.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@Singleton
public class StateAndCommandProvider {

	final static protected Set<Command> COMMANDS = new HashSet<Command>();
	final static protected Set<State> STATES = new HashSet<State>();
	final static protected Set<Type> TYPES = new HashSet<Type>();
	
	static {
		COMMANDS.add(OnOffType.ON);
		COMMANDS.add(OnOffType.OFF);
		COMMANDS.add(UpDownType.UP);
		COMMANDS.add(UpDownType.DOWN);
		COMMANDS.add(IncreaseDecreaseType.INCREASE);
		COMMANDS.add(IncreaseDecreaseType.DECREASE);
		COMMANDS.add(StopMoveType.STOP);
		COMMANDS.add(StopMoveType.MOVE);

		STATES.add(UnDefType.UNDEF);
		STATES.add(UnDefType.NULL);
		STATES.add(OnOffType.ON);
		STATES.add(OnOffType.OFF);
		STATES.add(UpDownType.UP);
		STATES.add(UpDownType.DOWN);
		STATES.add(OpenClosedType.OPEN);
		STATES.add(OpenClosedType.CLOSED);
		
		TYPES.addAll(COMMANDS);
		TYPES.addAll(STATES);
	}
	
	public Iterable<Type> getAllTypes() {
		return TYPES;
	}

	public Iterable<Command> getAllCommands() {
		return COMMANDS;
	}

	public Iterable<State> getAllStates() {
		return STATES;
	}
	
}
