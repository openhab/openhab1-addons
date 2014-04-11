package org.openhab.binding.withings.internal.model;

public class Measure {

	public MeasureType type;
	public int unit;
	public int value;

	public float getActualValue() {
		return (float) (value * Math.pow(10, unit));
	}

}
