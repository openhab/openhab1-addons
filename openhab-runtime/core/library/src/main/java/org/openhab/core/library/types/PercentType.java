package org.openhab.core.library.types;

import org.openhab.core.types.CommandType;
import org.openhab.core.types.DataType;
import org.openhab.core.types.PrimitiveType;

public class PercentType implements PrimitiveType, DataType, CommandType {

	private int value; 
	
	public PercentType() {
		this.value = 0;
	}
	
	public PercentType(int value) {
		if(value<0 || value>100) {
			throw new IllegalArgumentException("Value must be between 0 and 100");
		}
		this.value = value;
	}
	
	public String toString() {
		return Integer.toString(value);
	}
}
