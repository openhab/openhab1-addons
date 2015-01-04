package org.openhab.binding.lacrosse;

import org.openhab.core.binding.BindingProvider;

public interface LaCrosseBindingProvider extends BindingProvider {
	public String getItemName(String id);
	public String getType(String itemName);
}
