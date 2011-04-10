/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.core.rules.internal;

import static org.openhab.core.events.EventConstants.TOPIC_PREFIX;
import static org.openhab.core.events.EventConstants.TOPIC_SEPERATOR;

import java.util.Calendar;
import java.util.Collection;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.ObjectFilter;
import org.drools.SystemEventListener;
import org.drools.SystemEventListenerFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.ItemRegistryChangeListener;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.rules.event.CommandEvent;
import org.openhab.core.rules.event.RuleEvent;
import org.openhab.core.rules.event.StateEvent;
import org.openhab.core.service.AbstractActiveService;
import org.openhab.core.types.Command;
import org.openhab.core.types.EventType;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleService extends AbstractActiveService implements ManagedService, EventHandler, ItemRegistryChangeListener, StateChangeListener {

	private static final String RULES_CHANGESET = "org/openhab/core/rules/changeset.xml";

	static private final Logger logger = LoggerFactory.getLogger(RuleService.class);
	
	private ItemRegistry itemRegistry = null;
	
	private long refreshInterval = 200;
		
	private StatefulKnowledgeSession ksession = null;
	
	private Map<String, FactHandle> factHandleMap = new HashMap<String, FactHandle>();
	
	public void activate() {
		
		SystemEventListenerFactory.setSystemEventListener(new RuleEventListener());

		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource(RULES_CHANGESET, getClass()), ResourceType.CHANGE_SET);

		if(kbuilder.hasErrors()) {
		    logger.error("There are errors in the rules: " + kbuilder.getErrors());
		    return;
		}

		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

		KnowledgeAgentConfiguration aconf = KnowledgeAgentFactory.newKnowledgeAgentConfiguration();
		aconf.setProperty("drools.agent.newInstance", "false");
		KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent("RuleAgent", kbase, aconf);		 
		kagent.applyChangeSet(ResourceFactory.newClassPathResource(RULES_CHANGESET, getClass()));
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		ksession = kbase.newStatefulKnowledgeSession();
				
		// activate notifications
		ResourceFactory.getResourceChangeNotifierService().start();
		ResourceFactory.getResourceChangeScannerService().start();
		
		// activate this for extensive logging
		// KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
		
		// set the scan interval to 20 secs
		ResourceChangeScannerConfiguration sconf = ResourceFactory.getResourceChangeScannerService().newResourceChangeScannerConfiguration();
		sconf.setProperty( "drools.resource.scanner.interval", "20" ); 
		ResourceFactory.getResourceChangeScannerService().configure(sconf);
		
		// now add all registered items to the session
		if(itemRegistry!=null) {
			for(Item item : itemRegistry.getItems()) {
				itemAdded(item);
			}
		}
		setInterrupted(false);
		start();
	}
	
	public void deactivate() {
		if(ksession!=null) {
			ksession.dispose();
			ksession = null;
		}
		factHandleMap.clear();
		setInterrupted(true);
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
	@Override
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
	@Override
	public void allItemsChanged(Collection<String> oldItemNames) {
		if(ksession!=null) {
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
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void itemAdded(Item item) {
		if(ksession!=null) {
			internalItemAdded(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void itemRemoved(Item item) {
		if(ksession!=null) {
			internalItemRemoved(item.getName());
			if (item instanceof GenericItem) {
				GenericItem genericItem = (GenericItem) item;
				genericItem.removeStateChangeListener(this);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stateChanged(Item item, State oldState, State newState) {
		if(ksession!=null && item!=null) {
			FactHandle factHandle = factHandleMap.get(item.getName());
			if(factHandle!=null) {
				ksession.update(factHandle, item);
				StateEvent event = new StateEvent(item, oldState, newState);
				ksession.insert(event);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stateUpdated(Item item, State state) {
		if(ksession!=null && item!=null) {
			FactHandle factHandle = factHandleMap.get(item.getName());
			if(factHandle!=null) {
				ksession.update(factHandle, item);
				StateEvent event = new StateEvent(item, state);
				ksession.insert(event);
			}
		}
	}

	public void receiveCommand(String itemName, Command command) {
		if(ksession!=null) {
			try {
				Item item = itemRegistry.getItem(itemName);
				CommandEvent event = new CommandEvent(item, command);
				ksession.insert(event);
			} catch (ItemNotFoundException e) {
			} catch (ItemNotUniqueException e) {}
		}
	}
	
	private void internalItemAdded(Item item) {
		if(item==null) {
			logger.debug("Item must not be null here!");
			return;
		}
		
		FactHandle handle = factHandleMap.get(item.getName());
		if(handle!=null) {
			// we already know this item
			try {
				ksession.update(handle, item);
			} catch(NullPointerException e) {
				// this can be thrown because of a bug in drools when closing down the system
			}
		} else {
			// it is a new item
			handle = ksession.insert(item);
			factHandleMap.put(item.getName(), handle);
			if (item instanceof GenericItem) {
				GenericItem genericItem = (GenericItem) item;
				genericItem.addStateChangeListener(this);
			}
		}
	}

	private void internalItemRemoved(String itemName) {
		FactHandle handle = factHandleMap.get(itemName);
		if(handle!=null) {
			factHandleMap.remove(itemName);
			ksession.retract(handle);
		}		
	}

	@Override
	protected void execute() {
		// run the rule evaluation
		final Calendar cal = GregorianCalendar.getInstance();
		ksession.fireAllRules();
		
		// now remove all events again from the session
		Collection<FactHandle> handles = ksession.getFactHandles(new ObjectFilter() {			
			public boolean accept(Object obj) {
				if (obj instanceof RuleEvent) {
					RuleEvent event = (RuleEvent) obj;
					return event.getTimestamp().before(cal);
				}
				return false;
			}
		});
		for(FactHandle handle : handles) {
			ksession.retract(handle);
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
	@Override
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

	static private final class RuleEventListener implements SystemEventListener {
		
		private final Logger logger = LoggerFactory.getLogger(SystemEventListener.class);
	
		@Override
		public void warning(String message, Object object) {
			logger.warn(message);
		}
	
		@Override
		public void warning(String message) {
			logger.warn(message);			
		}
	
		@Override
		public void info(String message, Object object) {
			logger.info(message);
		}
	
		@Override
		public void info(String message) {
			logger.info(message);
		}
	
		@Override
		public void exception(String message, Throwable e) {
			logger.error(message, e);
		}
	
		@Override
		public void exception(Throwable e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	
		@Override
		public void debug(String message, Object object) {
			logger.debug(message);
		}
	
		@Override
		public void debug(String message) {
			logger.debug(message);
		}
	}

}
