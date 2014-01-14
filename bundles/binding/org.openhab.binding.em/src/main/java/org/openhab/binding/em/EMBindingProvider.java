package org.openhab.binding.em;

import org.openhab.binding.em.internal.EMBindingConfig;
import org.openhab.binding.em.internal.EMBindingConfig.Datapoint;
import org.openhab.binding.em.internal.EMBindingConfig.EMType;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Till Klocke
 * @since 1.4.0
 */
public interface EMBindingProvider extends BindingProvider {

	public EMBindingConfig getConfigByTypeAndAddressAndDatapoint(EMType type,
			String address, Datapoint datapoint);

}
