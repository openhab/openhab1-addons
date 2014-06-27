package org.openhab.persistence.jpa.internal;

import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;

public class StateHelper {

	static public String toString(State state) throws Exception {
		if(state instanceof DateTimeType) {
			return String.valueOf(((DateTimeType)state).getCalendar().getTime().getTime());
		}
		if(state instanceof DecimalType) {
			return String.valueOf(((DecimalType)state).doubleValue());
		}

		return state.toString();
	}	
}
