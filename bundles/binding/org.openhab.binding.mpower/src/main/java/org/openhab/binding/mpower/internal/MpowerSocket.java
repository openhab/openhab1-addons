package org.openhab.binding.mpower.internal;

import java.util.Calendar;

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
	private String energyTodayItemName;
	private int dayOfYear;
	private long totalConsumptionAtMidnight;
	private MpowerSocketState valueCache;
	private long lastUpdated = 0;

	public MpowerSocket() {
	}

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

	public void updateTotalConsumptionAtMidnight(double consumption) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(lastUpdated);
		int currentDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		if (this.dayOfYear != currentDayOfYear) {
			dayOfYear = currentDayOfYear;
			totalConsumptionAtMidnight = (new Double(consumption)).longValue();
		}
	}

	public long getTotalConsumptionAtMidnight() {
		return totalConsumptionAtMidnight;
	}

	public String getEnergyItemName() {
		return energyItemName;
	}

	public void setEnergyItemName(String energyItemName) {
		this.energyItemName = energyItemName;
	}

	public String getEnergyTodayItemName() {
		return energyTodayItemName;
	}

	public void setEnergyTodayItemName(String energyTodayItemName) {
		this.energyTodayItemName = energyTodayItemName;
	}

}
