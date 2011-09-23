package org.openhab.model.rule.internal;

import static org.openhab.core.events.EventConstants.TOPIC_PREFIX;
import static org.openhab.core.events.EventConstants.TOPIC_SEPERATOR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.ItemRegistryChangeListener;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.service.AbstractActiveService;
import org.openhab.core.types.Command;
import org.openhab.core.types.EventType;
import org.openhab.core.types.State;
import org.openhab.model.rule.internal.event.CommandEvent;
import org.openhab.model.rule.internal.event.RuleEvent;
import org.openhab.model.rule.internal.event.StateEvent;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleEngine extends AbstractActiveService implements ManagedService, EventHandler, ItemRegistryChangeListener, StateChangeListener {

		static private final Logger logger = LoggerFactory.getLogger(RuleEngine.class);
		
		private ItemRegistry itemRegistry = null;
		
		private long refreshInterval = 200;
					
		private List<RuleEvent> eventQueue = Collections.synchronizedList(new ArrayList<RuleEvent>());
		
		public void activate() {
			
			// now add all registered items to the session
			if(itemRegistry!=null) {
				for(Item item : itemRegistry.getItems()) {
					itemAdded(item);
				}
			}
			
			start();
		}
		
		public void deactivate() {
			shutdown();
		}
		
		public void setItemRegistry(ItemRegistry itemRegistry) {
			this.itemRegistry = itemRegistry;
			itemRegistry.addItemRegistryChangeListener(this);
		}
		
		public void unsetItemRegistry(ItemRegistry itemRegistry) {
			itemRegistry.removeItemRegistryChangeListener(this);
			this.itemRegistry = null;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@SuppressWarnings("rawtypes")
		public void updated(Dictionary config) throws ConfigurationException {
			if (config != null) {
				String evalIntervalString = (String) config.get("evalInterval");
				if (StringUtils.isNotBlank(evalIntervalString)) {
					refreshInterval = Long.parseLong(evalIntervalString);
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void allItemsChanged(Collection<String> oldItemNames) {
			// first remove all previous items from the session
			for(String oldItemName : oldItemNames) {
				internalItemRemoved(oldItemName);
			}
			
			// then add the current ones again
			Collection<Item> items = itemRegistry.getItems();
			for(Item item : items) {
				internalItemAdded(item);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void itemAdded(Item item) {
			internalItemAdded(item);
		}

		/**
		 * {@inheritDoc}
		 */
		public void itemRemoved(Item item) {
			internalItemRemoved(item.getName());
			if (item instanceof GenericItem) {
				GenericItem genericItem = (GenericItem) item;
				genericItem.removeStateChangeListener(this);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void stateChanged(Item item, State oldState, State newState) {
			eventQueue.add(new StateEvent(item, oldState, newState));
		}

		/**
		 * {@inheritDoc}
		 */
		public void stateUpdated(Item item, State state) {
			eventQueue.add(new StateEvent(item, state));
		}

		public void receiveCommand(String itemName, Command command) {
			try {
				Item item = itemRegistry.getItem(itemName);
				eventQueue.add(new CommandEvent(item, command));
			} catch (ItemNotFoundException e) {
			} catch (ItemNotUniqueException e) {}
		}
		
		private void internalItemAdded(Item item) {
			if(item==null) {
				logger.debug("Item must not be null here!");
				return;
			}			
		}

		private void internalItemRemoved(String itemName) {
		}
		
		/**
		 * @{inheritDoc}
		 */
		@Override
		public boolean isProperlyConfigured() {
			return true;
		}

		/**
		 * @{inheritDoc}
		 */
		@Override
		protected synchronized void execute() {
			ArrayList<RuleEvent> clonedQueue = new ArrayList<RuleEvent>(eventQueue);
			eventQueue.clear();
			
			// now add all recent events to the session
			for(RuleEvent event : clonedQueue) {
				Item item = event.getItem();
			}
			
		}

		@Override
		protected long getRefreshInterval() {
			return refreshInterval;
		}

		@Override
		protected String getName() {
			return "Rule Evaluation Service";
		}

		/**
		 * {@inheritDoc}
		 */
		public void handleEvent(Event event) {  
			String itemName = (String) event.getProperty("item");
			
			String topic = event.getTopic();
			String[] topicParts = topic.split(TOPIC_SEPERATOR);
			
			if(!(topicParts.length > 2) || !topicParts[0].equals(TOPIC_PREFIX)) {
				return; // we have received an event with an invalid topic
			}
			String operation = topicParts[1];
			
			if(operation.equals(EventType.COMMAND.toString())) {
				Command command = (Command) event.getProperty("command");
				if(command!=null) receiveCommand(itemName, command);
			}
		}

}
