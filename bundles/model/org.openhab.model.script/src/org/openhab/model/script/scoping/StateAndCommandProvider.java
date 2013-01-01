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
