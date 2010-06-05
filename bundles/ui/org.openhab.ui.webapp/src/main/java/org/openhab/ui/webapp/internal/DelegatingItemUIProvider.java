package org.openhab.ui.webapp.internal;

import org.openhab.core.items.Item;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.items.ItemUIProvider;

/**
 * This class provides a simple way to ask different item providers by a 
 * single method call, i.e. the consumer does not need to iterate over all
 * registered providers as this is done inside this class.
 * 
 * @author Kai Kreuzer
 * @since 0.2.0
 *
 */
public class DelegatingItemUIProvider implements ItemUIProvider {
	
	private final WebAppService service;
	
	public DelegatingItemUIProvider(WebAppService webAppService) {
		this.service = webAppService;
	}

	@Override
	public String getIcon(String itemName) {		
		for(ItemUIProvider provider : service.getItemUIProviders()) {
			String currentIcon = provider.getIcon(itemName);
			if(currentIcon!=null) {
				return currentIcon;
			}
		}
		return null;
	}

	@Override
	public String getLabel(String itemName) {
		for(ItemUIProvider provider : service.getItemUIProviders()) {
			String currentLabel = provider.getLabel(itemName);
			if(currentLabel!=null) {
				return currentLabel;
			}
		}
		return null;
	}

	@Override
	public Widget getDefaultWidget(Class<? extends Item> itemType, String itemName) {
		for(ItemUIProvider provider : service.getItemUIProviders()) {
			Widget widget = provider.getDefaultWidget(itemType, itemName);
			if(widget!=null) {
				return widget;
			}
		}
		return null;
	}

}
