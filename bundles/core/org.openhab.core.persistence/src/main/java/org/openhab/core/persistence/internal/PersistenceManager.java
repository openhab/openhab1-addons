/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.persistence.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.text.DateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.ecore.EObject;
import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.ItemRegistryChangeListener;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.model.core.EventType;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.core.ModelRepositoryChangeListener;
import org.openhab.model.persistence.persistence.AllConfig;
import org.openhab.model.persistence.persistence.CronStrategy;
import org.openhab.model.persistence.persistence.GroupConfig;
import org.openhab.model.persistence.persistence.ItemConfig;
import org.openhab.model.persistence.persistence.PersistenceConfiguration;
import org.openhab.model.persistence.persistence.PersistenceModel;
import org.openhab.model.persistence.persistence.Strategy;
import org.openhab.model.persistence.scoping.GlobalStrategies;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the central part of the persistence management and delegation. It reads the persistence
 * models, schedules timers and manages the invocation of {@link PersistenceService}s upon events.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class PersistenceManager extends AbstractEventSubscriber implements ModelRepositoryChangeListener, ItemRegistryChangeListener, StateChangeListener {
	
	private static final Logger logger = LoggerFactory.getLogger(PersistenceManager.class);

	private static PersistenceManager instance;
	
	// the scheduler used for timer events
	private Scheduler scheduler;
	
	/*default */ ModelRepository modelRepository;

	private ItemRegistry itemRegistry;

	/*default */ Map<String, PersistenceService> persistenceServices = new HashMap<String, PersistenceService>();
	
	/** keeps a list of configurations for each persistence service */
	protected Map<String, List<PersistenceConfiguration>> persistenceConfigurations = new ConcurrentHashMap<String, List<PersistenceConfiguration>>();

	/** keeps a list of default strategies for each persistence service */
	protected Map<String, List<Strategy>> defaultStrategies = 
			Collections.synchronizedMap(new HashMap<String, List<Strategy>>());
	
	
	public PersistenceManager() {
		PersistenceManager.instance = this;
		 try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
          logger.error("initializing scheduler throws exception", e);
		}
	}
	
	static /* default */ PersistenceManager getInstance() {
		return instance;
	}
	
	
	public void activate() {
	}
	
	public void deactivate() {
	}
	
	
	public void setModelRepository(ModelRepository modelRepository) {
		this.modelRepository = modelRepository;
		modelRepository.addModelRepositoryChangeListener(this);
		for(String modelName : modelRepository.getAllModelNamesOfType("persist")) {
			String serviceName = modelName.substring(0, modelName.length()-".persist".length());
			stopEventHandling(serviceName);
			startEventHandling(serviceName);
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
		logger.debug("Initializing {} persistence service.", persistenceService.getName());
		persistenceServices.put(persistenceService.getName(), persistenceService);
		stopEventHandling(persistenceService.getName());
		startEventHandling(persistenceService.getName());
	}

	public void removePersistenceService(PersistenceService persistenceService) {
		stopEventHandling(persistenceService.getName());
		persistenceServices.remove(persistenceService.getName());
	}
	
	
	public void modelChanged(String modelName, EventType type) {
		if(modelName.endsWith(".persist")) {
			String serviceName = modelName.substring(0, modelName.length()-".persist".length());
			if(type==EventType.REMOVED || type==EventType.MODIFIED) {
				stopEventHandling(serviceName);
			}
	
			if(type==EventType.ADDED || type==EventType.MODIFIED) {
				if(itemRegistry!=null && persistenceServices.containsKey(serviceName)) {
					startEventHandling(serviceName);
				}
			}
		}
	}

	/**
	 * Registers a persistence model file with the persistence manager, so that it becomes active.
	 * 
	 * @param modelName the name of the persistence model without file extension
	 */
	private void startEventHandling(String modelName) {
		PersistenceModel model = (PersistenceModel) modelRepository.getModel(modelName + ".persist");
		if(model!=null) {
			persistenceConfigurations.put(modelName, model.getConfigs());
			defaultStrategies.put(modelName, model.getDefaults());
			for(PersistenceConfiguration config : model.getConfigs()) {
				if(hasStrategy(modelName, config, GlobalStrategies.RESTORE)) {
					for(Item item : getAllItems(config)) {
						initialize(item);
					}
				}
			}
			createTimers(modelName);
		}
	}

	/**
	 * Unregisters a persistence model file from the persistence manager, so that it is not further regarded.
	 * 
	 * @param modelName the name of the persistence model without file extension
	 */
	private void stopEventHandling(String modelName) {
		persistenceConfigurations.remove(modelName);
		defaultStrategies.remove(modelName);
		removeTimers(modelName);
	}

	public void stateChanged(Item item, State oldState, State newState) {
		handleStateEvent(item, true);
	}

	public void stateUpdated(Item item, State state) {
		handleStateEvent(item, false);
	}

	/**
	 * Calls all persistence services which use change or update policy for the given item
	 * 
	 * @param item the item to persist
	 * @param onlyChanges true, if it has the change strategy, false otherwise
	 */
	private void handleStateEvent(Item item, boolean onlyChanges) {
		synchronized (persistenceConfigurations) {
			for(Entry<String, List<PersistenceConfiguration>> entry : persistenceConfigurations.entrySet()) {
				String serviceName = entry.getKey();
				if(persistenceServices.containsKey(serviceName)) {				
					for(PersistenceConfiguration config : entry.getValue()) {
						if(hasStrategy(serviceName, config, onlyChanges ? GlobalStrategies.CHANGE : GlobalStrategies.UPDATE)) {
							if(appliesToItem(config, item)) {
								persistenceServices.get(serviceName).store(item, config.getAlias());
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Checks if a given persistence configuration entry has a certain strategy for the given service
	 * 
	 * @param serviceName the service to check the configuration for
	 * @param config the persistence configuration entry
	 * @param strategy the strategy to check for
	 * @return true, if it has the given strategy
	 */
	protected boolean hasStrategy(String serviceName, PersistenceConfiguration config, Strategy strategy) {
		if(defaultStrategies.get(serviceName).contains(strategy) && config.getStrategies().isEmpty()) {
			return true;
		} else {
			for(Strategy s : config.getStrategies()) {
				if(s.equals(strategy)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Checks if a given persistence configuration entry is relevant for an item
	 * 
	 * @param config the persistence configuration entry
	 * @param item to check if the configuration applies to
	 * @return true, if the configuration applies to the item
	 */
	protected boolean appliesToItem(PersistenceConfiguration config, Item item) {
		for(EObject itemCfg : config.getItems()) {
			if (itemCfg instanceof AllConfig) {
				return true;
			}
			if (itemCfg instanceof ItemConfig) {
				ItemConfig singleItemConfig = (ItemConfig) itemCfg;
				if(item.getName().equals(singleItemConfig.getItem())) {
					return true;
				}
			}
			if (itemCfg instanceof GroupConfig) {
				GroupConfig groupItemCfg = (GroupConfig) itemCfg;
				String groupName = groupItemCfg.getGroup();
				try {
					Item gItem = itemRegistry.getItem(groupName);
					if (gItem instanceof GroupItem) {
						GroupItem groupItem = (GroupItem) gItem;
						if(groupItem.getAllMembers().contains(item)) {
							return true;
						}
					}
				} catch (Exception e) {}
			}
		}
		return false;
	}

	/**
	 * Retrieves all items for which the persistence configuration applies to.
	 * 
	 * @param config the persistence configuration entry
	 * @return all items that this configuration applies to
	 */
	protected Iterable<Item> getAllItems(PersistenceConfiguration config) {
		// first check, if we should return them all
		for(EObject itemCfg : config.getItems()) {
			if (itemCfg instanceof AllConfig) {
				return itemRegistry.getItems();
			}
		}
		
		// otherwise, go through the detailed definitions
		Set<Item> items = new HashSet<Item>();
		for(EObject itemCfg : config.getItems()) {
			if (itemCfg instanceof ItemConfig) {
				ItemConfig singleItemConfig = (ItemConfig) itemCfg;
				try {
					Item item = itemRegistry.getItem(singleItemConfig.getItem());
					items.add(item);
				} catch (ItemNotFoundException e) {
					logger.debug("Item '{}' does not exist.", singleItemConfig.getItem());
				}
			}
			if (itemCfg instanceof GroupConfig) {
				GroupConfig groupItemCfg = (GroupConfig) itemCfg;
				String groupName = groupItemCfg.getGroup();
				try {
					Item gItem = itemRegistry.getItem(groupName);
					if (gItem instanceof GroupItem) {
						GroupItem groupItem = (GroupItem) gItem;
						items.addAll(groupItem.getAllMembers());
					}
				} catch (ItemNotFoundException e) {
					logger.debug("Item group '{}' does not exist.", groupName);
				}
			}
		}
		return items;
	}

	public void allItemsChanged(Collection<String> oldItemNames) {
		for(Item item : itemRegistry.getItems()) {
			itemAdded(item);
		}
	}

	public void itemAdded(Item item) {
		initialize(item);
		if (item instanceof GenericItem) {
			GenericItem genericItem = (GenericItem) item;
			genericItem.addStateChangeListener(this);
		}
	}

	/**
	 * Handles the "restoreOnStartup" strategy for the item.
	 * If the item state is still undefined when entering this method, all persistence configurations are checked,
	 * if they have the "restoreOnStartup" strategy configured for the item. If so, the item state will be set
	 * to its last persisted value.
	 * 
	 * @param item the item to restore the state for
	 */
	protected void initialize(Item item) {
		// get the last persisted state from the persistence service if no state is yet set
		if(item.getState().equals(UnDefType.NULL) && item instanceof GenericItem) {
			for(Entry<String, List<PersistenceConfiguration>> entry : persistenceConfigurations.entrySet()) {
				String serviceName = entry.getKey();
				for(PersistenceConfiguration config : entry.getValue()) {
					if(hasStrategy(serviceName, config, GlobalStrategies.RESTORE)) {
						if(appliesToItem(config, item)) {
							PersistenceService service = persistenceServices.get(serviceName);
							if(service instanceof QueryablePersistenceService) {
								QueryablePersistenceService queryService = (QueryablePersistenceService) service;
								FilterCriteria filter = new FilterCriteria().setItemName(item.getName()).setPageSize(1);
								Iterable<HistoricItem> result = queryService.query(filter);
								Iterator<HistoricItem> it = result.iterator();
								if(it.hasNext()) {
									HistoricItem historicItem = it.next();
									GenericItem genericItem = (GenericItem) item;
									genericItem.removeStateChangeListener(this);
									genericItem.setState(historicItem.getState());
									genericItem.addStateChangeListener(this);
									logger.debug("Restored item state from '{}' for item '{}' -> '{}'", 
											new Object[] { DateFormat.getDateTimeInstance().format(historicItem.getTimestamp()), 
											item.getName(), historicItem.getState().toString() } );
									return;
								}
							} else if(service!=null) {
								logger.warn("Failed to restore item states as persistence service '{}' can not be queried.", serviceName);
							}
						}
					}
				}
			}	
		}		
	}

	public void itemRemoved(Item item) {
		if (item instanceof GenericItem) {
			GenericItem genericItem = (GenericItem) item;
			genericItem.removeStateChangeListener(this);
		}
	}
	
	/**
	 * Creates and schedules a new quartz-job and trigger with model and rule name as jobData.
	 * 
	 * @param rule the rule to schedule
	 * @param trigger the defined trigger 
	 * 
	 * @throws SchedulerException if there is an internal Scheduler error.
	 */
	private void createTimers(String modelName) {
		PersistenceModel persistModel = (PersistenceModel) modelRepository.getModel(modelName + ".persist");
		if(persistModel!=null) {
			for(Strategy strategy : persistModel.getStrategies()) {
				if (strategy instanceof CronStrategy) {
					CronStrategy cronStrategy = (CronStrategy) strategy;
					String cronExpression = cronStrategy.getCronExpression();
					JobKey jobKey = new JobKey(strategy.getName(), modelName);
					try {
				        JobDetail job = newJob(PersistItemsJob.class)
				        	.usingJobData(PersistItemsJob.JOB_DATA_PERSISTMODEL, cronStrategy.eResource().getURI().trimFileExtension().path())
				        	.usingJobData(PersistItemsJob.JOB_DATA_STRATEGYNAME, cronStrategy.getName())
				            .withIdentity(jobKey)
				            .build();
				
				        Trigger quartzTrigger = newTrigger()
					            .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
					            .build();
			
				        scheduler.scheduleJob(job, quartzTrigger);
			
						logger.debug("Scheduled strategy {} with cron expression {}", new String[] { jobKey.toString(), cronExpression });
					} catch(SchedulerException e) {
						logger.error("Failed to schedule job for strategy {} with cron expression {}", new String[] { jobKey.toString(), cronExpression }, e);
					}
				}
			}
		}
	}

	/**
	 * Delete all {@link Job}s of the group <code>persistModelName</code>
	 * 
	 * @throws SchedulerException if there is an internal Scheduler error.
	 */
	private void removeTimers(String persistModelName) {
		try {
			Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(persistModelName));
			for(JobKey jobKey : jobKeys) {
				try {
					boolean success = scheduler.deleteJob(jobKey);
					if(success) {
						logger.debug("Removed scheduled cron job for strategy '{}'", jobKey.toString());						
					} else {
						logger.warn("Failed to delete cron jobs '{}'", jobKey.getName());
					}
				} catch (SchedulerException e) {
					logger.warn("Failed to delete cron jobs '{}'", jobKey.getName());
				}
			}
		} catch (SchedulerException e) {
			logger.warn("Failed to delete cron jobs of group '{}'", persistModelName);
		}
	}
		

}
