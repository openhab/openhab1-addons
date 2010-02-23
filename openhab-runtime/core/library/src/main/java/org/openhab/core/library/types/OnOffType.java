package org.openhab.core.library.types;

import org.openhab.core.types.CommandType;
import org.openhab.core.types.DataType;
import org.openhab.core.types.PrimitiveType;

public enum OnOffType implements PrimitiveType, DataType, CommandType {
	ON, OFF, DIMMED;
	
	public String toString() {
		switch(this) {
			case ON: return "ON";
			case OFF: return "OFF";
			case DIMMED: return "DIMMED";
		}
		return "";
	}
}
