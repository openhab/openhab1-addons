package org.openhab.core.jsr223.internal.shared;

import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * StartupTrigger to allow a Rule to listen for startup event
 * 
 * @author Simon Merschjohann
 * @since 1.7.0
 */
public class StartupTrigger implements EventTrigger {
	public StartupTrigger() {

	}

	@Override
	public boolean evaluate(Item item, State oldState, State newState, Command command, TriggerType type) {
		return type == TriggerType.STARTUP;
	}

	@Override
	public String getItem() {
		return null;
	}
}
