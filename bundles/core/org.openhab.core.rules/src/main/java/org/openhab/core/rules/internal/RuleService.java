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

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
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
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.ItemRegistryChangeListener;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleService implements ManagedService, ItemRegistryChangeListener, StateChangeListener {

	private static final String RULES_CHANGESET = "org/openhab/core/rules/changeset.xml";

	static private final Logger logger = LoggerFactory.getLogger(RuleService.class);
	
	private ItemRegistry itemRegistry = null;
	
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
	}
	
	public void deactivate() {
		if(ksession!=null) {
			ksession.dispose();
			ksession = null;
		}
		factHandleMap.clear();
	}
	
	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
		itemRegistry.addItemRegistryChangeListener(this);
	}
	
	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		itemRegistry.removeItemRegistryChangeListener(this);
		this.itemRegistry = null;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {
	}

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
			ksession.fireAllRules();
		}
	}

	@Override
	public void itemAdded(Item item) {
		if(ksession!=null) {
			internalItemAdded(item);
			ksession.fireAllRules();
		}
	}

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

	@Override
	public void stateChanged(Item item, State oldState, State newState) {
		if(ksession!=null && item!=null) {
			FactHandle handle = factHandleMap.get(item.getName());
			if(handle!=null) {
				if (item instanceof GenericItem) {
					GenericItem genericItem = (GenericItem) item;
					genericItem.setUpdated(true);
					if(!oldState.equals(newState)) {
						genericItem.setChanged(true);
					}
					ksession.update(handle, item);
					ksession.fireAllRules();
					genericItem.setChanged(false);
					genericItem.setUpdated(false);
					ksession.update(handle, item);
				}
			}
		}
	}

	@Override
	public void stateUpdated(Item item, State state) {
		if(ksession!=null && item!=null) {
			FactHandle handle = factHandleMap.get(item.getName());
			if(handle!=null) {
				if (item instanceof GenericItem) {
					GenericItem genericItem = (GenericItem) item;
					genericItem.setUpdated(true);
					ksession.update(handle, item);
					ksession.fireAllRules();
					genericItem.setUpdated(false);
					ksession.update(handle, item);
				}
			}
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
