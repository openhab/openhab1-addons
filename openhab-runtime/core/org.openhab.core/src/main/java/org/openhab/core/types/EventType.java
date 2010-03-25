package org.openhab.core.types;

public enum EventType {
	COMMAND, UPDATE;
	
	public String toString() {
		switch(this) {
			case COMMAND: return "command";
			case UPDATE:  return "update";
		}
		return "";
	}

}
