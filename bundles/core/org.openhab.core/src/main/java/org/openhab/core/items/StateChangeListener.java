package org.openhab.core.items;

import org.openhab.core.types.State;

public interface StateChangeListener {
	
	public void stateChanged(Item item, State oldState, State newState);

}
