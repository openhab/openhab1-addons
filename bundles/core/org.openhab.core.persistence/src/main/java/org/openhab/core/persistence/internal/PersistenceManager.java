package org.openhab.core.persistence.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.emf.ecore.EObject;
import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.ItemRegistryChangeListener;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.types.State;
import org.openhab.model.core.EventType;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.core.ModelRepositoryChangeListener;
import org.openhab.model.persistence.persistence.AllConfig;
import org.openhab.model.persistence.persistence.ChangeStrategy;
import org.openhab.model.persistence.persistence.GroupConfig;
import org.openhab.model.persistence.persistence.ItemConfig;
import org.openhab.model.persistence.persistence.PersistenceConfiguration;
import org.openhab.model.persistence.persistence.PersistenceModel;
import org.openhab.model.persistence.persistence.Strategy;
import org.openhab.model.persistence.persistence.UpdateStrategy;

public class PersistenceManager extends AbstractEventSubscriber implements ModelRepositoryChangeListener, ItemRegistryChangeListener, StateChangeListener {
	
	public PersistenceManager() {}
	
	private ModelRepository modelRepository;

	private ItemRegistry itemRegistry;

	private Map<String, PersistenceService> persistenceServices = new HashMap<String, PersistenceService>();
	
	protected Map<String, List<PersistenceConfiguration>> persistenceConfigurations = new HashMap<String, List<PersistenceConfiguration>>();

	public void setModelRepository(ModelRepository modelRepository) {
		this.modelRepository = modelRepository;
		modelRepository.addModelRepositoryChangeListener(this);
		for(String modelName : modelRepository.getAllModelNamesOfType("persist")) {
			startEventHandling(modelName);
		}
	}

	public void unsetModelRepository(ModelRepository modelRepository) {
		modelRepository.removeModelRepositoryChangeListener(this);
		for(String modelName : modelRepository.getAllModelNamesOfType("persist")) {
			stopEventHandling(modelName);
		}
		this.modelRepository = null;
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
		itemRegistry.addItemRegistryChangeListener(this);
		allItemsChanged(null);
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		itemRegistry.removeItemRegistryChangeListener(this);
		this.itemRegistry = null;
	}

	public void addPersistenceService(PersistenceService persistenceService) {
		persistenceServices.put(persistenceService.getName(), persistenceService);
	}

	public void removePersistenceService(PersistenceService persistenceService) {
		persistenceServices.remove(persistenceService.getName());
	}

	public void activate() {
	}
	
	public void deactivate() {
	}
	
	public void modelChanged(String modelName, EventType type) {
		if(modelName.endsWith(".persist")) {
			String serviceName = modelName.substring(0, modelName.length()-".persist".length());
			if(type==EventType.REMOVED || type==EventType.MODIFIED) {
				stopEventHandling(serviceName);
			}
	
			if(type==EventType.ADDED || type==EventType.MODIFIED) {
				startEventHandling(serviceName);
			}
		}
	}

	private void startEventHandling(String modelName) {
		PersistenceModel model = (PersistenceModel) modelRepository.getModel(modelName + ".persist");
		persistenceConfigurations.put(modelName, model.getConfigs());		
	}

	private void stopEventHandling(String modelName) {
		persistenceConfigurations.remove(modelName);
	}

	public void stateChanged(Item item, State oldState, State newState) {
		for(Entry<String, List<PersistenceConfiguration>> entry : persistenceConfigurations.entrySet()) {
			String serviceName = entry.getKey();
			if(persistenceServices.containsKey(serviceName)) {
				if(shouldTriggerOnChangeEvent(item, entry.getValue())) {
					persistenceServices.get(serviceName).store(item);
				}
			}
		}
	}

	public void stateUpdated(Item item, State state) {
		for(Entry<String, List<PersistenceConfiguration>> entry : persistenceConfigurations.entrySet()) {
			String serviceName = entry.getKey();
			if(persistenceServices.containsKey(serviceName)) {
				if(shouldTriggerOnUpdateEvent(item, entry.getValue())) {
					persistenceServices.get(serviceName).store(item);
				}
			}
		}
	}

	private boolean shouldTriggerOnUpdateEvent(Item item,
			List<PersistenceConfiguration> persistenceConfigurations) {
		for(PersistenceConfiguration config : persistenceConfigurations) {
			if(hasUpdateEventStrategy(config)) {
				if(appliesToItem(config, item)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean shouldTriggerOnChangeEvent(Item item,
			List<PersistenceConfiguration> persistenceConfigurations) {
		for(PersistenceConfiguration config : persistenceConfigurations) {
			if(hasChangeEventStrategy(config)) {
				if(appliesToItem(config, item)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean hasUpdateEventStrategy(PersistenceConfiguration config) {
		for(Strategy strategy : config.getStrategies()) {
			if(strategy instanceof UpdateStrategy) {
				return true;
			}
		}
		return false;
	}

	private boolean hasChangeEventStrategy(PersistenceConfiguration config) {
		for(Strategy strategy : config.getStrategies()) {
			if(strategy.getDefinition() instanceof ChangeStrategy) {
				return true;
			}
		}
		return false;
	}

	protected boolean appliesToItem(PersistenceConfiguration config, Item item) {
		for(EObject itemCfg : config.getItems()) {
			if (itemCfg instanceof AllConfig) {
				return true;
			}
			if (itemCfg instanceof ItemConfig) {
				ItemConfig singleItemConfig = (ItemConfig) itemCfg;
				if(item.getName().equals(singleItemConfig.getItem().getName())) {
					return true;
				}
			}
			if (itemCfg instanceof GroupConfig) {
				GroupConfig groupItemCfg = (GroupConfig) itemCfg;
				String groupName = groupItemCfg.getGroup().getName();
				try {
					Item gItem = itemRegistry.getItem(groupName);
					if (gItem instanceof GroupItem) {
						GroupItem groupItem = (GroupItem) gItem;
						if(ArrayUtils.contains(groupItem.getAllMembers(), item)) {
							return true;
						}
					}
				} catch (Exception e) {}
			}
		}
		return false;
	}

	public void allItemsChanged(Collection<String> oldItemNames) {
		for(Item item : itemRegistry.getItems()) {
			itemAdded(item);
		}
	}

	public void itemAdded(Item item) {
		if (item instanceof GenericItem) {
			GenericItem genericItem = (GenericItem) item;
			genericItem.addStateChangeListener(this);
		}
	}

	public void itemRemoved(Item item) {
		if (item instanceof GenericItem) {
			GenericItem genericItem = (GenericItem) item;
			genericItem.removeStateChangeListener(this);
		}
	}
}
