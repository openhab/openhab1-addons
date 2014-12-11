package org.openhab.binding.withings;

import org.openhab.binding.withings.internal.model.MeasureType;
import org.openhab.core.binding.BindingConfig;

/**
 * {@link WithingsBindingConfig} defines a binding configuration for an item.
 * The binding supports to define the {@link MeasureType}.
 * 
 * @author Dennis Nobel
 * @since 1.5.0
 */
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