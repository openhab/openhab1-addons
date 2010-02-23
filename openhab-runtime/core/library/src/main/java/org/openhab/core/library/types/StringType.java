package org.openhab.core.library.types;

import org.openhab.core.types.CommandType;
import org.openhab.core.types.DataType;
import org.openhab.core.types.PrimitiveType;

public class StringType implements PrimitiveType, DataType, CommandType {

	private String value;

	public StringType() {
		this.value = "";
	};
	
	public StringType(String value) {
		// just to be sure that we stay with something human readable here;
		// if there's a usecase that requires more, the ComplexDataType might be a better option
		if(value.length() > 256) {
			throw new IllegalArgumentException("String must not exceed 256 characters!");
		}
		this.value = value;
	}
	
	public String toString() {
		return value;
	}
}
