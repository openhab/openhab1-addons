package org.openhab.binding.enocean.internal.converter;


public class CurrentItem {
	protected String currentItemName;
	
	public CurrentItem(){
	}
	
	public void setCurrentItemName(String itemName) {
		this.currentItemName = itemName;
	}
	
	public String getCurrentItemName() {
		return this.currentItemName;
	}
	
}
