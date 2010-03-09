package org.openhab.core.items;

import java.util.List;

import org.openhab.core.types.CommandType;
import org.openhab.core.types.DataType;

public interface Item {

	public DataType getState();

	public String getName();

	public List<Class<? extends DataType>> getAcceptedDataTypes();
	
	public List<Class<? extends CommandType>> getAcceptedCommandTypes();


}