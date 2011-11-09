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

package org.openhab.model.rule.internal.engine;

import static org.openhab.core.events.EventConstants.TOPIC_PREFIX;
import static org.openhab.core.events.EventConstants.TOPIC_SEPERATOR;
import static org.openhab.model.rule.internal.engine.RuleTriggerManager.TriggerTypes.CHANGE;
import static org.openhab.model.rule.internal.engine.RuleTriggerManager.TriggerTypes.COMMAND;
import static org.openhab.model.rule.internal.engine.RuleTriggerManager.TriggerTypes.SHUTDOWN;
import static org.openhab.model.rule.internal.engine.RuleTriggerManager.TriggerTypes.STARTUP;
import static org.openhab.model.rule.internal.engine.RuleTriggerManager.TriggerTypes.UPDATE;

import java.util.Collection;
import java.util.Dictionary;

import org.eclipse.emf.ecore.EObject;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.ItemRegistryChangeListener;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.script.engine.Script;
import org.openhab.core.script.engine.ScriptEngine;
import org.openhab.core.script.engine.ScriptExecutionException;
import org.openhab.core.types.Command;
import org.openhab.core.types.EventType;
import org.openhab.core.types.State;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.core.ModelRepositoryChangeListener;
import org.openhab.model.rule.rules.Rule;
import org.openhab.model.rule.rules.RuleModel;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the core of the openHAB rule engine.
 * It listens to changes to the rules folder, evaluates the trigger conditions of the rules and
 * schedules them for execution dependent on their triggering conditions.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
public class RuleEngine implements ManagedService, EventHandler, ItemRegistryChangeListener, StateChangeListener, ModelRepositoryChangeListener {

		static private final Logger logger = LoggerFactory.getLogger(RuleEngine.class);
		
		private ItemRegistry itemRegistry;
		private ModelRepository modelRepository;
		private ScriptEngine scriptEngine;

		private RuleTriggerManager triggerManager;
				
		public void activate() {
			triggerManager = new RuleTriggerManager();

			if(!isEnabled()) return;
			logger.info("Started rule engine");		
			
			// read all rule files
			Iterable<String> ruleModelNames = modelRepository.getAllModelNamesOfType("rules");
			for(String ruleModelName : ruleModelNames) {
				EObject model = modelRepository.getModel(ruleModelName);
				if(model instanceof RuleModel) {
					RuleModel ruleModel = (RuleModel) model;
					initializeRules(ruleModel);
				}
			}
			
			// register us on all items which are already available in the registry
			if(itemRegistry!=null) {
				for(Item item : itemRegistry.getItems()) {
					itemAdded(item);
				}
			}
			
		}
		
		public void deactivate() {
			// execute all scripts that were registered for system shutdown
			executeScripts(triggerManager.getRules(SHUTDOWN));
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
		@SuppressWarnings("rawtypes")
		public void updated(Dictionary config) throws ConfigurationException {
			if (config != null) {
			}
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
			if (item instanceof GenericItem) {
				GenericItem genericItem = (GenericItem) item;
				genericItem.removeStateChangeListener(this);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void stateChanged(Item item, State oldState, State newState) {

			// we also must execute the rules for simple updates
			stateUpdated(item, newState);
			
			// and now the rules, which only want to see state changes
			Iterable<Rule> rules = triggerManager.getRules(CHANGE, item, newState, oldState);
			for(Rule rule : rules) {
				executeRule(rule);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void stateUpdated(Item item, State state) {
			Iterable<Rule> rules = triggerManager.getRules(UPDATE, item, state);
			for(Rule rule : rules) {
				executeRule(rule);
			}
		}

		public void receiveCommand(String itemName, Command command) {
			Iterable<Rule> rules = triggerManager.getRules(COMMAND, itemName, command);
			for(Rule rule : rules) {
				executeRule(rule);
			}
		}
		
		private void internalItemAdded(Item item) {
			if(item==null) {
				logger.debug("Item must not be null here!");
				return;
			}
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

		@Override
		public void modelChanged(String modelName,
				org.openhab.model.core.EventType type) {
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
					initializeRules(model);
				}
			}
		}

		private void initializeRules(RuleModel ruleModel) {
			for(Rule rule : ruleModel.getRules()) {
				triggerManager.addRule(rule);
			}

			// now execute all rules that are meant to trigger at startup
			Iterable<Rule> startupRules = triggerManager.getRules(STARTUP);
			executeScripts(startupRules);
			triggerManager.clear(STARTUP);
		}


		protected synchronized void executeRule(Rule rule) {
			Script script = scriptEngine.newScriptFromXExpression(rule.getScript());
			try {
				logger.info("Executing rule '{}'", rule.getName());
				script.execute();
			} catch (ScriptExecutionException e) {
				logger.error("Error during the execution of rule {}", rule.getName(), e);
			}
		}

		protected synchronized void executeScripts(Iterable<Rule> rules) {
			for(Rule rule : rules) {
				executeRule(rule);
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
