package org.openhab.binding.knx.core.internal.bus;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListener;

public class KNX2EventBinding implements ProcessListener {

	private EventPublisher eventPublisher;

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	public void groupWrite(ProcessEvent e) {
		e.getDestination()
	}

	public void detached(DetachEvent e) {
	}
}
