package org.openhab.binding.withings;

import org.openhab.binding.withings.internal.model.MeasureType;
import org.openhab.core.binding.BindingConfig;

public class WithingsBindingConfig implements BindingConfig {

	public MeasureType measureType;

	public WithingsBindingConfig(MeasureType measureType) {
		this.measureType = measureType;
	}

	@Override
	public String toString() {
		return "WithingsBindingConfig [measureType=" + measureType + "]";
	}

}