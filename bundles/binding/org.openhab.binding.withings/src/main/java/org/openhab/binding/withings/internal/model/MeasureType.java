package org.openhab.binding.withings.internal.model;

public enum MeasureType {

	FAT_FREE_MASS(5), FAT_MASS_WEIGHT(8), FAT_RATIO(6), SIZE(4), WEIGHT(1);

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
