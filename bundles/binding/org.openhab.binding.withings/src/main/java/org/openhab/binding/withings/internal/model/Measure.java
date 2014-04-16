package org.openhab.binding.withings.internal.model;

/**
 * Java object for response of Withings API.
 * 
 * @see http://www.withings.com/de/api#bodymetrics
 * @author Dennis Nobel
 * @since 1.5.0
 */
public class Measure {

	public MeasureType type;
	public int unit;
	public int value;

	public float getActualValue() {
		return (float) (value * Math.pow(10, unit));
	}

}
