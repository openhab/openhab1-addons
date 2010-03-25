package org.openhab.core.items;

import java.util.List;

import org.openhab.core.types.Command;
import org.openhab.core.types.State;

public interface Item {

	public State getState();

	public String getName();

	public List<Class<? extends State>> getAcceptedDataTypes();
	
	public List<Class<? extends Command>> getAcceptedCommandTypes();


}