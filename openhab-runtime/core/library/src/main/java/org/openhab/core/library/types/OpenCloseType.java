package org.openhab.core.library.types;

import org.openhab.core.types.CommandType;
import org.openhab.core.types.DataType;
import org.openhab.core.types.PrimitiveType;

public enum OpenCloseType implements PrimitiveType, DataType, CommandType {
	OPEN, CLOSE, AJAR;
	
	public String toString() {
		switch(this) {
			case OPEN:  return "OPEN";
			case CLOSE: return "CLOSE";
			case AJAR:  return "AJAR";
		}
		return "";
	}
}
