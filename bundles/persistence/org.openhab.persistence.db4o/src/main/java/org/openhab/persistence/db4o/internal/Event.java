package org.openhab.persistence.db4o.internal;


public class Event {

	private String state;
	private String itemName;
	private Long timeMillis;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public Long getTimeMillis() {
		return timeMillis;
	}
	
	public void setTimeMillis(Long timeMillis) {
		this.timeMillis = timeMillis;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(this.timeMillis).append(" ");
		sb.append(this.itemName).append(" ");
		sb.append(this.state);
		return sb.toString();		
	}
	
}
