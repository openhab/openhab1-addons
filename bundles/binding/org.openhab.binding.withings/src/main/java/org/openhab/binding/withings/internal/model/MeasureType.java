package org.openhab.binding.withings.internal.model;

/**
 * Java object for response of Withings API.
 * 
 * @see http://www.withings.com/de/api#bodymetrics
 * @author Dennis Nobel
 * @since 1.5.0
 */
public enum MeasureType {

	DIASTOLIC_BLOOD_PRESSURE(9), FAT_FREE_MASS(5), FAT_MASS_WEIGHT(8), FAT_RATIO(
			6), HEART_PULSE(11), HEIGHT(4), SYSTOLIC_BLOOD_PRESSURE(10), WEIGHT(
			1);

	public static MeasureType getForType(int type) {
		MeasureType[] measureTypes = values();
		for (MeasureType measureType : measureTypes) {
			if (measureType.type == type) {
				return measureType;
			}
		}
		return null;
	}

	public int type;

	private MeasureType(int type) {
		this.type = type;
	}
}
