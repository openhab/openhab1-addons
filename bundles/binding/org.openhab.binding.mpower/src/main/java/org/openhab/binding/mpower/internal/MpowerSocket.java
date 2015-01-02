package org.openhab.binding.mpower.internal;

/**
 * Ubiquiti mPower strip binding
 * 
 * @author magcode
 */
public class MpowerSocket {

	private String voltageItemName;
	private String powerItemName;
	private String switchItemName;
	public String getVoltageItemName() {
		return voltageItemName;
	}
	public void setVoltageItemName(String voltageItemName) {
		this.voltageItemName = voltageItemName;
	}
	public String getSwitchItemName() {
		return switchItemName;
	}
	public void setSwitchItemName(String switchItemName) {
		this.switchItemName = switchItemName;
	}
	public String getPowerItemName() {
		return powerItemName;
	}
	public void setPowerItemName(String powerItemName) {
		this.powerItemName = powerItemName;
	}
}
