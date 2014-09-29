/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.drools.internal;

import static org.openhab.core.events.EventConstants.TOPIC_PREFIX;
import static org.openhab.core.events.EventConstants.TOPIC_SEPERATOR;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
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
import org.openhab.core.drools.event.CommandEvent;
import org.openhab.core.drools.event.RuleEvent;
import org.openhab.core.drools.event.StateEvent;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.ItemRegistryChangeListener;
import org.openhab.core.items.StateChangeListener;
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

	private static final String RULES_CHANGESET = "org/openhab/core/drools/changeset.xml";

	static private final Logger logger = LoggerFactory.getLogger(RuleService.class);
	
	private ItemRegistry itemRegistry = null;
	
	private long refreshInterval = 200;
		
	private StatefulKnowledgeSession ksession = null;
	
	private Map<String, FactHandle> factHandleMap = new HashMap<String, FactHandle>();
	
	private List<RuleEvent> eventQueue = Collections.synchronizedList(new ArrayList<RuleEvent>());
	
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
		
		setProperlyConfigured(true);
	}
	
	public void deactivate() {
		if(ksession!=null) {
			ksession.dispose();
			ksession = null;
		}
		factHandleMap.clear();
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
	public void itemAdded(Item item) {
		if(ksession!=null) {
			internalItemAdded(item);
		}
	}

	/**
	 * {@inheritDoc}
	 */
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
		} catch (ItemNotFoundException e) {}
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
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected synchronized void execute() {
		// remove all previous events from the session
		Collection<FactHandle> handles = ksession.getFactHandles(new ObjectFilter() {			
			public boolean accept(Object obj) {
				if (obj instanceof RuleEvent) {
					return true;
				}
				return false;
			}
		});
		for(FactHandle handle : handles) {
			ksession.retract(handle);
		}

		ArrayList<RuleEvent> clonedQueue = new ArrayList<RuleEvent>(eventQueue);
		eventQueue.clear();
		
		// now add all recent events to the session
		for(RuleEvent event : clonedQueue) {
			Item item = event.getItem();
			if(ksession!=null && item!=null) {
				FactHandle factHandle = factHandleMap.get(item.getName());
				if(factHandle!=null) {
					ksession.update(factHandle, item);
				}
				ksession.insert(event);
			}
		}
		
		// run the rule evaluation
		ksession.fireAllRules();
			
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

	static private final class RuleEventListener implements SystemEventListener {
		
		private final Logger logger = LoggerFactory.getLogger(SystemEventListener.class);
	
		public void warning(String message, Object object) {
			logger.warn(message);
		}
	
		public void warning(String message) {
			logger.warn(message);			
		}
	
		public void info(String message, Object object) {
			logger.info(message);
		}
	
		public void info(String message) {
			logger.info(message);
		}
	
		public void exception(String message, Throwable e) {
			logger.error(message, e);
		}
	
		public void exception(Throwable e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	
		public void debug(String message, Object object) {
			logger.debug(message);
		}
	
		public void debug(String message) {
			logger.debug(message);
		}
	}

}
