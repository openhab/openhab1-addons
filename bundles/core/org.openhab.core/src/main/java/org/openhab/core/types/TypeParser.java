/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
		if(s!=null) {
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
		}
		return null;
	}
}
