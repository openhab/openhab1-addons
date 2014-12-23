package org.openhab.binding.jeelinkec3k.internal;

public class EC3KData {
	public EC3KData(String encoded) {
		/*
		 * OK 14E7 251 251 0 313 314 15 18
		 */

		String[] parts = encoded.split(" ");
		this.id = parts[1];
		this.secondsTotal = Long.parseLong(parts[2]);
		this.secondsOn = Long.parseLong(parts[3]);
		this.consumptionTotal = Integer.parseInt(parts[4]);
		this.currentWatt = Double.parseDouble(parts[5]) / 10;
		this.maxWatt = Double.parseDouble(parts[6]) / 10;
		this.resets = Integer.parseInt(parts[7]);
	}

	private double currentWatt;
	private double maxWatt;
	private long consumptionTotal;
	private long secondsOn;
	private long secondsTotal;
	private String id;
	private int resets;

	public double getCurrentWatt() {
		return currentWatt;
	}

	public void setCurrentWatt(double currentWatt) {
		this.currentWatt = currentWatt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getMaxWatt() {
		return maxWatt;
	}

	public void setMaxWatt(double maxWatt) {
		this.maxWatt = maxWatt;
	}

	public double getConsumptionTotal() {
		return consumptionTotal;
	}

	public long getSecondsOn() {
		return secondsOn;
	}

	public long getSecondsTotal() {
		return secondsTotal;
	}

	public int getResets() {
		return resets;
	}

}
