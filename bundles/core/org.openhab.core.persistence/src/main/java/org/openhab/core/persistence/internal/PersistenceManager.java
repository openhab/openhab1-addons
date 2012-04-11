/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.core.persistence.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.emf.ecore.EObject;
import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
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
import org.openhab.model.persistence.persistence.CronStrategy;
import org.openhab.model.persistence.persistence.GroupConfig;
import org.openhab.model.persistence.persistence.ItemConfig;
import org.openhab.model.persistence.persistence.PersistenceConfiguration;
import org.openhab.model.persistence.persistence.PersistenceModel;
import org.openhab.model.persistence.persistence.Strategy;
import org.openhab.model.persistence.persistence.UpdateStrategy;
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
	protected Map<String, List<PersistenceConfiguration>> persistenceConfigurations = new HashMap<String, List<PersistenceConfiguration>>();

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
		createTimers(modelName);
	}

	private void stopEventHandling(String modelName) {
		persistenceConfigurations.remove(modelName);
		removeTimers(modelName);
	}

	public void stateChanged(Item item, State oldState, State newState) {
		for(Entry<String, List<PersistenceConfiguration>> entry : persistenceConfigurations.entrySet()) {
			String serviceName = entry.getKey();
			if(persistenceServices.containsKey(serviceName)) {				
				for(PersistenceConfiguration config : entry.getValue()) {
					if(hasChangeEventStrategy(config)) {
						if(appliesToItem(config, item)) {
							persistenceServices.get(serviceName).store(item, config.getAlias());
						}
					}
				}
			}
		}
	}

	public void stateUpdated(Item item, State state) {
		for(Entry<String, List<PersistenceConfiguration>> entry : persistenceConfigurations.entrySet()) {
			String serviceName = entry.getKey();
			if(persistenceServices.containsKey(serviceName)) {
				for(PersistenceConfiguration config : entry.getValue()) {
					if(hasUpdateEventStrategy(config)) {
						if(appliesToItem(config, item)) {
							persistenceServices.get(serviceName).store(item, config.getAlias());
						}
					}
				}
			}
		}
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
			if(strategy instanceof ChangeStrategy) {
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
						if(ArrayUtils.contains(groupItem.getAllMembers(), item)) {
							return true;
						}
					}
				} catch (Exception e) {}
			}
		}
		return false;
	}

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
					logger.warn("Item '{}' does not exist and thus will not be persisted.", singleItemConfig.getItem());
				}
			}
			if (itemCfg instanceof GroupConfig) {
				GroupConfig groupItemCfg = (GroupConfig) itemCfg;
				String groupName = groupItemCfg.getGroup();
				try {
					Item gItem = itemRegistry.getItem(groupName);
					if (gItem instanceof GroupItem) {
						GroupItem groupItem = (GroupItem) gItem;
						items.addAll(Arrays.asList(groupItem.getAllMembers()));
					}
				} catch (ItemNotFoundException e) {
					logger.warn("Item group '{}' does not exist and thus its members will not be persisted.", groupName);
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
					String cronExpression = cronStrategy.getDefinition();
					JobKey jobKey = new JobKey(strategy.getName(), modelName);
					try {
				        JobDetail job = newJob(PersistenceJob.class)
				        	.usingJobData(PersistenceJob.JOB_DATA_PERSISTMODEL, cronStrategy.eResource().getURI().trimFileExtension().path())
				        	.usingJobData(PersistenceJob.JOB_DATA_STRATEGYNAME, cronStrategy.getName())
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
