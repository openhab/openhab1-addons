/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.jeelinkec3k;

import java.util.Calendar;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

public class JeeLinkEC3KBindingConfig implements BindingConfig {

	private String address;
	private long timestamp;
	private Item currentWattItem;
	private Item maxWattItem;
	private Item totalConsumptionItem;
	private Item lastUpdatedItem;
	private Item consumptionTodayItem;
	private Item switchRealTimeItem;
	private Item priceToday;
	private boolean realTime;
	private int dayOfYear;
	private double totalConsumptionAtMidnight;
	private double totalConsumption;
	private double consumptionToday;

	public JeeLinkEC3KBindingConfig(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public Item getCurrentWattItem() {
		return currentWattItem;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long ts) {

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp);
		int currentDayOfYear = cal.get(Calendar.DAY_OF_YEAR);
		if (this.dayOfYear != currentDayOfYear) {
			dayOfYear = currentDayOfYear;
			totalConsumptionAtMidnight = totalConsumption;
		}
		consumptionToday = totalConsumption - totalConsumptionAtMidnight;
		timestamp = ts;
	}

	public void setCurrentWattItem(Item item) {
		this.currentWattItem = item;
	}

	public Item getMaxWattItem() {
		return maxWattItem;
	}

	public void setMaxWattItem(Item maxWattItem) {
		this.maxWattItem = maxWattItem;
	}

	public Item getTotalConsumptionItem() {
		return totalConsumptionItem;
	}

	public void setTotalConsumptionItem(Item totalConsumptionItem) {
		this.totalConsumptionItem = totalConsumptionItem;
	}

	public Item getLastUpdatedItem() {
		return lastUpdatedItem;
	}

	public void setLastUpdatedItem(Item lastUpdatedItem) {
		this.lastUpdatedItem = lastUpdatedItem;
	}

	public Item getConsumptionTodayItem() {
		return consumptionTodayItem;
	}

	public void setConsumptionTodayItem(Item consumptionTodayItem) {
		this.consumptionTodayItem = consumptionTodayItem;
	}

	public double getConsumptionToday() {
		return consumptionToday;
	}

	public double getTotalConsumption() {
		return totalConsumption;
	}

	public void setTotalConsumption(double totalConsumption) {
		this.totalConsumption = totalConsumption;
	}

	public Item getSwitchRealTimeItem() {
		return switchRealTimeItem;
	}

	public void setSwitchRealTimeItem(Item switchRealTimeItem) {
		this.switchRealTimeItem = switchRealTimeItem;
	}

	public boolean isRealTime() {
		return realTime;
	}

	public void setRealTime(boolean realTime) {
		this.realTime = realTime;
	}

	public Item getPriceToday() {
		return priceToday;
	}

	public void setPriceToday(Item priceToday) {
		this.priceToday = priceToday;
	}
}
