/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

package org.openhab.core.types;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * This is a helper class that helps parsing a string into an openHAB type (state or command).
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public class TypeParser {

	/**
	 * <p>Determines a state from a string. Possible state types are passed as a parameter.
	 * Note that the order matters here; the first type that accepts the string as a valid
	 * value, will be used for the state.</p>
	 * <p>Example: The type list is OnOffType.class,StringType.class. The string "ON" is now
	 * accepted by the OnOffType and thus OnOffType.ON will be returned (and not a StringType
	 * with value "ON").</p>
	 *  
	 * @param types possible types of the state to consider 
	 * @param s the string to parse
	 * @return the corresponding State instance or <code>null</code>
	 */
	public static State parseState(List<Class<? extends State>> types, String s) {
		for(Class<? extends Type> type : types) {
			try {									
				Method valueOf = type.getMethod("valueOf", String.class);
				State state = (State) valueOf.invoke(type, s);
				if(state!=null) return state;
			} catch (NoSuchMethodException e) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}
		return null;
	}

	/**
	 * <p>Determines a command from a string. Possible command types are passed as a parameter.
	 * Note that the order matters here; the first type that accepts the string as a valid
	 * value, will be used for the command.</p>
	 * <p>Example: The type list is OnOffType.class,StringType.class. The string "ON" is now
	 * accepted by the OnOffType and thus OnOffType.ON will be returned (and not a StringType
	 * with value "ON").</p>
	 *  
	 * @param types possible types of the command to consider 
	 * @param s the string to parse
	 * @return the corresponding Command instance or <code>null</code>
	 */
	public static Command parseCommand(List<Class<? extends Command>> types, String s) {
		for(Class<? extends Command> type : types) {
			try {									
				Method valueOf = type.getMethod("valueOf", String.class);
				Command value = (Command) valueOf.invoke(type, s);
				if(value!=null) return value;
			} catch (NoSuchMethodException e) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}		
		}
		return null;
	}
}
