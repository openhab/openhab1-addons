package org.openhab.binding.digitalstrom.internal.listener;

import org.openhab.core.types.State;

public interface ItemListener {
	
	public void itemStateUpdated(String itemName, State state);

}
