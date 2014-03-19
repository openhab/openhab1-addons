/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.rule.internal.engine;

import static org.openhab.core.events.EventConstants.TOPIC_PREFIX;
import static org.openhab.core.events.EventConstants.TOPIC_SEPERATOR;
import static org.openhab.model.rule.internal.engine.RuleTriggerManager.TriggerTypes.CHANGE;
import static org.openhab.model.rule.internal.engine.RuleTriggerManager.TriggerTypes.COMMAND;
import static org.openhab.model.rule.internal.engine.RuleTriggerManager.TriggerTypes.SHUTDOWN;
import static org.openhab.model.rule.internal.engine.RuleTriggerManager.TriggerTypes.STARTUP;
import static org.openhab.model.rule.internal.engine.RuleTriggerManager.TriggerTypes.UPDATE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.naming.QualifiedName;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.ItemRegistryChangeListener;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.scriptengine.Script;
import org.openhab.core.scriptengine.ScriptEngine;
import org.openhab.core.scriptengine.ScriptExecutionException;
import org.openhab.core.scriptengine.ScriptExecutionThread;
import org.openhab.core.types.Command;
import org.openhab.core.types.EventType;
import org.openhab.core.types.State;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.core.ModelRepositoryChangeListener;
import org.openhab.model.rule.rules.Rule;
import org.openhab.model.rule.rules.RuleModel;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;


/**
 * This class is the core of the openHAB rule engine.
 * It listens to changes to the rules folder, evaluates the trigger conditions of the rules and
 * schedules them for execution dependent on their triggering conditions.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@SuppressWarnings("restriction")
public class RuleEngine implements EventHandler, ItemRegistryChangeListener, StateChangeListener, ModelRepositoryChangeListener {

		static private final Logger logger = LoggerFactory.getLogger(RuleEngine.class);
		
		private ItemRegistry itemRegistry;
		private ModelRepository modelRepository;
		private ScriptEngine scriptEngine;

		private RuleTriggerManager triggerManager;
						
		public void activate() {
			triggerManager = new RuleTriggerManager();

			if(!isEnabled()) {
				logger.info("Rule engine is disabled.");
				return;
			}
			
			logger.debug("Started rule engine");		
			
			// read all rule files
			Iterable<String> ruleModelNames = modelRepository.getAllModelNamesOfType("rules");
			ArrayList<String> clonedList = Lists.newArrayList(ruleModelNames);
			for(String ruleModelName : clonedList) {
				EObject model = modelRepository.getModel(ruleModelName);
				if(model instanceof RuleModel) {
					RuleModel ruleModel = (RuleModel) model;
					triggerManager.addRuleModel(ruleModel);
				}
			}
			
			// register us on all items which are already available in the registry
			for(Item item : itemRegistry.getItems()) {
				internalItemAdded(item);
			}
			runStartupRules();
		}
		
		public void deactivate() {
			// execute all scripts that were registered for system shutdown
			executeRules(triggerManager.getRules(SHUTDOWN));
			triggerManager.clearAll();
			triggerManager = null;
		}
		
		public void setItemRegistry(ItemRegistry itemRegistry) {
			this.itemRegistry = itemRegistry;
			itemRegistry.addItemRegistryChangeListener(this);
		}
		
		public void unsetItemRegistry(ItemRegistry itemRegistry) {
			itemRegistry.removeItemRegistryChangeListener(this);
			this.itemRegistry = null;
		}
		
		public void setModelRepository(ModelRepository modelRepository) {
			this.modelRepository = modelRepository;
			modelRepository.addModelRepositoryChangeListener(this);
		}

		public void unsetModelRepository(ModelRepository modelRepository) {
			modelRepository.removeModelRepositoryChangeListener(this);
			this.modelRepository = null;
		}
		
		public void setScriptEngine(ScriptEngine scriptEngine) {
			this.scriptEngine = scriptEngine;
		}

		public void unsetScriptEngine(ScriptEngine scriptEngine) {
			this.scriptEngine = null;
		}

		/**
		 * {@inheritDoc}
		 */
		public void allItemsChanged(Collection<String> oldItemNames) {
			// add the current items again
			Collection<Item> items = itemRegistry.getItems();
			for(Item item : items) {
				internalItemAdded(item);
			}
			runStartupRules();
		}

		/**
		 * {@inheritDoc}
		 */
		public void itemAdded(Item item) {
			internalItemAdded(item);
			runStartupRules();
		}

		/**
		 * {@inheritDoc}
		 */
		public void itemRemoved(Item item) {
			if (item instanceof GenericItem) {
				GenericItem genericItem = (GenericItem) item;
				genericItem.removeStateChangeListener(this);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void stateChanged(Item item, State oldState, State newState) {			
			if(triggerManager!=null) {
				Iterable<Rule> rules = triggerManager.getRules(CHANGE, item, oldState, newState);
				RuleEvaluationContext context = new RuleEvaluationContext();
				context.newValue(QualifiedName.create(RuleContextHelper.VAR_PREVIOUS_STATE), oldState);
				executeRules(rules, context);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void stateUpdated(Item item, State state) {
			if(triggerManager!=null) {
				Iterable<Rule> rules = triggerManager.getRules(UPDATE, item, state);
				executeRules(rules);
			}
		}

		public void receiveCommand(String itemName, Command command) {
			if(triggerManager!=null && itemRegistry!=null) {
				try {
					Item item = itemRegistry.getItem(itemName);
					Iterable<Rule> rules = triggerManager.getRules(COMMAND, item, command);
					RuleEvaluationContext context = new RuleEvaluationContext();
					context.newValue(QualifiedName.create(RuleContextHelper.VAR_RECEIVED_COMMAND), command);
					executeRules(rules, context);
				} catch (ItemNotFoundException e) {
					// ignore commands for non-existent items
				}
			}
		}
		
		private void internalItemAdded(Item item) {
			if (item instanceof GenericItem) {
				GenericItem genericItem = (GenericItem) item;
				genericItem.addStateChangeListener(this);
			}
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

		public void modelChanged(String modelName, org.openhab.model.core.EventType type) {
			if (triggerManager != null) {
				if(isEnabled() && modelName.endsWith("rules")) {
					RuleModel model = (RuleModel) modelRepository.getModel(modelName);
	
					// remove the rules from the trigger sets
					if(type == org.openhab.model.core.EventType.REMOVED ||
							type == org.openhab.model.core.EventType.MODIFIED) {
						triggerManager.removeRuleModel(model);
					}
	
					// add new and modified rules to the trigger sets
					if(model!=null && 
							(type == org.openhab.model.core.EventType.ADDED 
							|| type == org.openhab.model.core.EventType.MODIFIED)) {
						triggerManager.addRuleModel(model);
						// now execute all rules that are meant to trigger at startup
						runStartupRules();
					}
				}
			}
		}

		private void runStartupRules() {
			if (triggerManager!=null) {
				Iterable<Rule> startupRules = triggerManager.getRules(STARTUP);
				List<Rule> executedRules = Lists.newArrayList();
				
				for(Rule rule : startupRules) {
					try {
						Script script = scriptEngine.newScriptFromXExpression(rule.getScript());						
						logger.debug("Executing startup rule '{}'", rule.getName());
						RuleEvaluationContext context = new RuleEvaluationContext();
						context.setGlobalContext(RuleContextHelper.getContext(rule));
						script.execute(context);
						executedRules.add(rule);
					} catch (ScriptExecutionException e) {
						if(e.getCause() instanceof ItemNotFoundException || e.getCause().getMessage().contains("cannot be resolved to an item or type")) {
							// we do not seem to have all required items in place yet
							// so we keep the rule in the list and try it again later
						} else {
							logger.error("Error during the execution of startup rule '{}': {}", new String[] { rule.getName(), e.getCause().getMessage() });
							executedRules.add(rule);
						}
					}
				}
				for(Rule rule : executedRules) {
					triggerManager.removeRule(STARTUP, rule);
				}
			}
		}

		protected synchronized void executeRule(Rule rule) {
			executeRule(rule, new RuleEvaluationContext());
		}
			
		protected synchronized void executeRule(Rule rule, RuleEvaluationContext context) {
			Script script = scriptEngine.newScriptFromXExpression(rule.getScript());
			
			logger.debug("Executing rule '{}'", rule.getName());
			
			context.setGlobalContext(RuleContextHelper.getContext(rule));
			
			ScriptExecutionThread thread = new ScriptExecutionThread(rule.getName(), script, context);
			thread.start();
		}

		protected synchronized void executeRules(Iterable<Rule> rules) {
			executeRules(rules, new RuleEvaluationContext());
		}
		
		protected synchronized void executeRules(Iterable<Rule> rules, RuleEvaluationContext context) {
			for(Rule rule : rules) {
				executeRule(rule, context);
			}
		}
				
		/**
		 * we need to be able to deactivate the rule execution, otherwise the openHAB designer
		 * would also execute the rules.
		 * 
		 * @return true, if rules should be executed, false otherwise
		 */
		private boolean isEnabled() {
			return !"true".equalsIgnoreCase(System.getProperty("noRules"));
		}
		
}
