package org.openhab.binding.s300th;

import org.openhab.binding.s300th.internal.S300THGenericBindingProvider.S300THBindingConfig;
import org.openhab.binding.s300th.internal.S300THGenericBindingProvider.S300THBindingConfig.Datapoint;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Till Klocke
 * @since 1.4.0
 */
public interface S300THBindingProvider extends BindingProvider {
	public S300THBindingConfig getBindingConfigForAddressAndDatapoint(String address, Datapoint datapoint);
}
