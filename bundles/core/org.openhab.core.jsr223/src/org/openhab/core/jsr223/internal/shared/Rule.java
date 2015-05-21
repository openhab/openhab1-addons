package org.openhab.core.jsr223.internal.shared;

import java.util.List;

/**
 * Rule-Interface: A script can implement multiple Rules
 * 
 * @author Simon Merschjohann
 * @since 1.7.0
 */
public interface Rule {
	public List<EventTrigger> getEventTrigger();

	public void execute(Event event);
}
