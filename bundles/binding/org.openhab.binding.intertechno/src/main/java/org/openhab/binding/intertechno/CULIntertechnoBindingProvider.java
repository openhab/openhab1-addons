package org.openhab.binding.intertechno;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Till Klocke
 * @since 1.4.0
 */
public interface CULIntertechnoBindingProvider extends BindingProvider {

	public IntertechnoBindingConfig getConfigForItemName(String itemName);
}
