package org.openhab.core.types;

import java.lang.reflect.Method;
import java.util.List;

public class TypeParser {

	public static State parseState(List<Class<? extends State>> types, String stateName) {
		for(Class<? extends Type> type : types) {
			try {									
				Method valueOf = type.getMethod("valueOf", String.class);
				State state = (State) valueOf.invoke(type, stateName);
				if(state!=null) return state;
			} catch (Exception e) {}
		}
		return null;
	}

	public static Command parseCommand(List<Class<? extends Command>> types, String commandName) {
		for(Class<? extends Command> type : types) {
			try {									
				Method valueOf = type.getMethod("valueOf", String.class);
				Command value = (Command) valueOf.invoke(type, commandName);
				if(value!=null) return value;
			} catch (Exception e) {}
		}
		return null;
	}
}
