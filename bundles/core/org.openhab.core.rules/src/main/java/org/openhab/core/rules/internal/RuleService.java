package org.openhab.core.rules.internal;

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentConfiguration;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceChangeScannerConfiguration;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
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
		
		// set up the session
//		ksession.setGlobal("Audio", new Audio());
		
		// activate notifications
		ResourceFactory.getResourceChangeNotifierService().start();
		ResourceFactory.getResourceChangeScannerService().start();
		
		// set the scan interval to 20 secs
		ResourceChangeScannerConfiguration sconf = ResourceFactory.getResourceChangeScannerService().newResourceChangeScannerConfiguration();
		sconf.setProperty( "drools.resource.scanner.interval", "20" ); 
		ResourceFactory.getResourceChangeScannerService().configure(sconf);
		
		// some debugging stuff
		KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
		
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
		if(ksession!=null) {
			FactHandle handle = factHandleMap.get(item.getName());
			if(handle!=null && item!=null) {
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

	private void internalItemAdded(Item item) {
		assert item!=null;
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
			factHandleMap.remove(handle);
			ksession.retract(handle);
		}		
	}

}
