/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.persistence.internal;

import org.eclipse.emf.ecore.EObject;
import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.persistence.persistence.PersistenceConfiguration;
import org.openhab.model.persistence.persistence.PersistenceModel;
import org.openhab.model.persistence.persistence.Strategy;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of Quartz {@link Job}-Interface. It takes a PersistenceModel and a CronStrategy,
 * scans through the relevant configurations and persists the concerned items.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 */
public class PersistItemsJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(PersistItemsJob.class);
		
	public static final String JOB_DATA_PERSISTMODEL = "model";
	public static final String JOB_DATA_STRATEGYNAME = "strategy";
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String modelName = (String) context.getJobDetail().getJobDataMap().get(JOB_DATA_PERSISTMODEL);				
		String strategyName = (String) context.getJobDetail().getJobDataMap().get(JOB_DATA_STRATEGYNAME);
		
		PersistenceManager persistenceManager = PersistenceManager.getInstance();
		if(persistenceManager!=null) {
			ModelRepository modelRepository = persistenceManager.modelRepository;
			PersistenceService persistenceService = persistenceManager.persistenceServices.get(modelName);
			
			if(modelRepository!=null && persistenceService!=null) {
				EObject model = modelRepository.getModel(modelName + ".persist");
				if (model instanceof PersistenceModel) {
					PersistenceModel persistModel = (PersistenceModel) model;
					for(PersistenceConfiguration config : persistModel.getConfigs()) {
						if(hasStrategy(persistModel, config, strategyName)) {
							for(Item item : persistenceManager.getAllItems(config)) {
								long startTime = System.currentTimeMillis();
								persistenceService.store(item, config.getAlias());
								logger.trace("Storing item '{}' with persistence service '{}' took {}ms",
										new Object[] { item.getName(), modelName, System.currentTimeMillis() - startTime});
							}
						}
					}
				} else {
					logger.debug("Persistence file '{}' does not exist", modelName);
				}
			}
		} else {
			logger.warn("Persistence manager is not available!");
		}
	}

	private boolean hasStrategy(PersistenceModel persistModel, PersistenceConfiguration config, String strategyName) {
		// check if the strategy is directly defined on the config
		for(Strategy strategy : config.getStrategies()) {
			if(strategy.getName().equals(strategyName)) return true;
		}
		// if no strategies are given, check the default strategies to use
		if(config.getStrategies().isEmpty() && isDefault(persistModel, strategyName)) {
			return true;
		}
		return false;
	}

	private boolean isDefault(PersistenceModel persistModel, String strategyName) {
		for (Strategy strategy : persistModel.getDefaults()) {
			if(strategy.getName().equals(strategyName)) {
				return true;
			}
		}
		return false;
	}

}