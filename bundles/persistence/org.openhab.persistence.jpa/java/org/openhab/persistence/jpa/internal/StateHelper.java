package org.openhab.persistence.jpa.internal;

import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;

public class StateHelper {

	static public boolean isStringStateType(State state) {
		return state instanceof StringType;
	}
	
	static public boolean isOtherStateType(State state) {
		return !isStringStateType(state);
	}
	
	static public String toOther(State state) throws Exception {
		if(state instanceof DateTimeType) {
			return String.valueOf(((DateTimeType)state).getCalendar().getTime().getTime());
		}
		if(state instanceof DecimalType) {
			return String.valueOf(((DecimalType)state).doubleValue());
		}

		return state.toString();
	}
	
	static public String toString(State state) {
		return state.toString();
	}
}
