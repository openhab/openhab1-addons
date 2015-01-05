package org.openhab.binding.hue.internal.hardware;

import org.joda.time.DateTime;

/**
 * keeps internal States quite as seen on Bridge
 * @author Gernot Eger
 *
 */
public class HueTapState {
	private int buttonEvent;
	private String lastupdated;
	
	public HueTapState(Integer buttonEvent, String lastupdated) {
		super();
		this.buttonEvent = buttonEvent;
		this.lastupdated = lastupdated;
	}

	public int getButtonEvent() {
		return buttonEvent;
	}

	/**
	 * retreive Lastupdated as String
	 * @return
	 */
	public String getLastupdated() {
		return lastupdated;
	}

	/**
	 * return datetime as yoda time
	 * @return
	 */
	public DateTime getLastUpdatedDateTime(){
		return DateTime.parse(lastupdated);	
	}
	
	@Override
	public boolean equals(Object o1) {
		if(o1==null) return false;
		if(!(o1 instanceof HueTapState)) return false;
		HueTapState other = (HueTapState)o1;
		if(other.buttonEvent!=buttonEvent) return false;
		if(!other.lastupdated.equals(lastupdated)) return false;
		
		return true;
	}
	
	
	
}
