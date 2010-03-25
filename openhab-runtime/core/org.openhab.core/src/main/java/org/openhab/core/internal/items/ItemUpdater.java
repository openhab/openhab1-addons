package org.openhab.core.internal.items;

import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemUpdater extends AbstractEventSubscriber {

	private static final Logger logger = LoggerFactory.getLogger(ItemUpdater.class);
	
	ItemRegistry itemRegistry;
	
	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}

	@Override
	public void receiveUpdate(String itemName, State newStatus) {
		if(itemRegistry!=null) {
			try {
				GenericItem item = (GenericItem) itemRegistry.getItem(itemName);
				item.setState(newStatus);
			} catch (ItemNotFoundException e) {
				logger.debug("Received update for non-existing item", e);
			}
		}
	}

}
