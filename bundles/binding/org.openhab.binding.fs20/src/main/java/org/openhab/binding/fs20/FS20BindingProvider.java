package org.openhab.binding.fs20;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Till Klocke
 * @since 1.4.0
 */
public interface FS20BindingProvider extends BindingProvider {

	public FS20BindingConfig getConfigForItemName(String itemName);

	public FS20BindingConfig getConfigForAddress(String address);

}
