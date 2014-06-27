package org.openhab.binding.enocean.internal.converter;

import java.util.HashMap;
import java.util.Map;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.StopMoveType;

public class WaitForAckState {
	private Map<String, Boolean> ackReceived = new HashMap<String, Boolean>();
	private Map<String, Boolean> timerHasEnded = new HashMap<String, Boolean>();
	private Map<String, Integer> RTACounter = new HashMap<String, Integer>();
	private EventPublisher eventPublisher;
	
	public WaitForAckState() {
		this.eventPublisher = null;
	}
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		if(this.eventPublisher == null) {
			this.eventPublisher = eventPublisher;
		}
	}
	
	public boolean hasAckReceived(String itemKey) {
		if(!ackReceived.containsKey(itemKey)) {
			ackReceived.put(itemKey, false);
		}
		return ackReceived.get(itemKey);
	}
	
	public void setAckReceived(String itemKey, boolean value) {
		ackReceived.put(itemKey, value);
	}
	
	public void timerStart(String itemKey) {
		timerHasEnded.put(itemKey, false);
	}
	
	public void timerEnd(String itemKey) {
		timerHasEnded.put(itemKey, true);
	}
	
	public boolean timerState(String itemKey) {
		if(!timerHasEnded.containsKey(itemKey)) {
			timerHasEnded.put(itemKey, true);
		}
		return timerHasEnded.get(itemKey);
	}
	
	public void increaseRTACounter(String itemKey) {
		if(!RTACounter.containsKey(itemKey)) {
			RTACounter.put(itemKey, 0);
		}
		RTACounter.put(itemKey, RTACounter.get(itemKey)+1);
	}
	
	public void resetRTACounter(String itemKey) {
		RTACounter.put(itemKey, 0);
	}
	
	public int getRTACounter(String itemKey) {
		if(!RTACounter.containsKey(itemKey)) {
			RTACounter.put(itemKey, 0);
		}
		return RTACounter.get(itemKey);
	}
	
	public void updateUI(String itemName, boolean itemLost){
		if(itemLost == true) {
			eventPublisher.postCommand(itemName, StopMoveType.STOP);
		} else {
			eventPublisher.postCommand(itemName, StopMoveType.MOVE);
		}
	}
}
