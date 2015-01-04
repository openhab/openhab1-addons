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
	private String energyItemName;
	private MpowerSocketState valueCache;
	private long lastUpdated = 0;

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

	public MpowerSocketState getValueCache() {
		return valueCache;
	}

	public void setValueCache(MpowerSocketState valueCache) {
		this.valueCache = valueCache;
	}

	public long getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(long lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getEnergyItemName() {
		return energyItemName;
	}

	public void setEnergyItemName(String energyItemName) {
		this.energyItemName = energyItemName;
	}
}
