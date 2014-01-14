package org.openhab.binding.fht;

import java.util.List;

import org.openhab.binding.fht.FHTBindingConfig.Datapoint;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Till Klocke
 * @since 1.4.0
 */
public interface FHTBindingProvider extends BindingProvider {

	public FHTBindingConfig getConfigByItemName(String itemName);

	public FHTBindingConfig getConfigByFullAddress(String fullAddress,
			Datapoint datapoint);

	public List<FHTBindingConfig> getAllFHT80bBindingConfigs();

}
