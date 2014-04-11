package org.openhab.binding.withings;

import org.openhab.core.binding.BindingConfig;

public class WithingsBindingConfig implements BindingConfig {
	
	public enum MeasureType {
		FAT_RATIO, WEIGHT;
		
		public static MeasureType parse(String measureType) {
			return MeasureType.valueOf(measureType.toUpperCase());
		}
	}
	
	
	public WithingsBindingConfig(MeasureType measureType) {
		this.measureType = measureType;
	}
	
	public MeasureType measureType;
	
	@Override
	public String toString() {
		return "WithingsBindingConfig [measureType=" + measureType + "]";
	}
	
	
}