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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.openhab.core.types.TypeParser;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.core.ModelRepositoryChangeListener;
import org.openhab.model.rule.rules.ChangedEventTrigger;
import org.openhab.model.rule.rules.CommandEventTrigger;
import org.openhab.model.rule.rules.EventTrigger;
import org.openhab.model.rule.rules.Rule;
import org.openhab.model.rule.rules.RuleModel;
import org.openhab.model.rule.rules.SystemOnShutdownTrigger;
import org.openhab.model.rule.rules.SystemOnStartupTrigger;
import org.openhab.model.rule.rules.UpdateEventTrigger;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleEngine implements ManagedService, EventHandler, ItemRegistryChangeListener, StateChangeListener, ModelRepositoryChangeListener {

		static private final Logger logger = LoggerFactory.getLogger(RuleEngine.class);
		
		private ItemRegistry itemRegistry;
		private ModelRepository modelRepository;
		private ScriptEngine scriptEngine;
		
		private Map<String, Set<Rule>> updateEventTriggeredRules = new HashMap<String, Set<Rule>>();
		private Map<String, Set<Rule>> changedEventTriggeredRules = new HashMap<String, Set<Rule>>();
		private Map<String, Set<Rule>> commandEventTriggeredRules = new HashMap<String, Set<Rule>>();
		private List<Rule> systemStartupTriggeredRules = new ArrayList<Rule>();
		private List<Rule> systemShutdownTriggeredRules = new ArrayList<Rule>();

		
		public void activate() {
			if(!isEnabled()) return;
			
			logger.info("Started rule engine");		

			Iterable<String> ruleModelNames = modelRepository.getAllModelNamesOfType("rules");
			for(String ruleModelName : ruleModelNames) {
				EObject model = modelRepository.getModel(ruleModelName);
				if(model instanceof RuleModel) {
					RuleModel ruleModel = (RuleModel) model;
					initializeRules(ruleModel);
				}
			}
			
			// now add all registered items to the session
			if(itemRegistry!=null) {
				for(Item item : itemRegistry.getItems()) {
					itemAdded(item);
				}
			}
			
		}
		
		public void deactivate() {
			// execute all scripts that were registered for system shutdown
			executeScripts(systemShutdownTriggeredRules);
			
			updateEventTriggeredRules.clear();
			commandEventTriggeredRules.clear();
			systemShutdownTriggeredRules.clear();
			systemStartupTriggeredRules.clear();
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
			internalItemRemoved(item.getName());
			if (item instanceof GenericItem) {
				GenericItem genericItem = (GenericItem) item;
				genericItem.removeStateChangeListener(this);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void stateChanged(Item item, State oldState, State newState) {

			// we also want to execute the rules for simple updates
			stateUpdated(item, newState);
			
			Set<Rule> rules = changedEventTriggeredRules.get(item.getName());
			if(rules==null) return;
			for(Rule rule : rules) {
				for(EventTrigger t : rule.getEventtrigger()) {
					if (t instanceof ChangedEventTrigger) {
						ChangedEventTrigger ct = (ChangedEventTrigger) t;
						if(ct.getItem().equals(item.getName())) {
							if(ct.getOldState()!=null) {
								State triggerOldState = TypeParser.parseState(item.getAcceptedDataTypes(), ct.getOldState());
								if(!oldState.equals(triggerOldState)) {
									continue;
								}								
							}
							if(ct.getNewState()!=null) {
								State triggerNewState = TypeParser.parseState(item.getAcceptedDataTypes(), ct.getNewState());
								if(!newState.equals(triggerNewState)) {
									continue;
								}								
							}
							executeScript(rule);
						}
					}
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		public void stateUpdated(Item item, State state) {
			Set<Rule> rules = updateEventTriggeredRules.get(item.getName());
			if(rules==null) return;
			for(Rule rule : rules) {
				for(EventTrigger t : rule.getEventtrigger()) {
					if (t instanceof UpdateEventTrigger) {
						UpdateEventTrigger ut = (UpdateEventTrigger) t;
						if(ut.getItem().equals(item.getName())) {
							if(ut.getState()!=null) {
								State triggerState = TypeParser.parseState(item.getAcceptedDataTypes(), ut.getState());
								if(!state.equals(triggerState)) {
									continue;
								}
							}
							executeScript(rule);
						}
					}
				}
			}
		}

		public void receiveCommand(String itemName, Command command) {
			Set<Rule> rules = commandEventTriggeredRules.get(itemName);
			if(rules==null) return;
			for(Rule rule : rules) {
				for(EventTrigger t : rule.getEventtrigger()) {
					if (t instanceof CommandEventTrigger) {
						CommandEventTrigger ct = (CommandEventTrigger) t;
						if(ct.getItem().equals(itemName) &&
								(ct.getCommand()==null || command.equals(ct.getCommand()))) {
							executeScript(rule);
						}
					}
				}
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

		private void internalItemRemoved(String itemName) {
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
					removeRules(updateEventTriggeredRules.values(), model);
					removeRules(changedEventTriggeredRules.values(), model);
					removeRules(commandEventTriggeredRules.values(), model);
					removeRules(Collections.singletonList(systemStartupTriggeredRules), model);
					removeRules(Collections.singletonList(systemShutdownTriggeredRules), model);
				}

				// add new and modified rules to the trigger sets
				if(model!=null && 
						(type == org.openhab.model.core.EventType.ADDED 
						|| type == org.openhab.model.core.EventType.MODIFIED)) {
					initializeRules(model);
				}

				// FIXME: This is only for the demo! Remove it and run this code always in initializeRules
				if(type == org.openhab.model.core.EventType.MODIFIED) {
					executeScripts(systemStartupTriggeredRules);
					systemStartupTriggeredRules.clear();
				}

			}
		}

		private void initializeRules(RuleModel ruleModel) {
			for(Rule rule : ruleModel.getRules()) {
				addRule(rule);
			}			
		}

		private synchronized void addRule(Rule rule) {
			for(EventTrigger t : rule.getEventtrigger()) {
				if(t instanceof SystemOnStartupTrigger) {
					systemStartupTriggeredRules.add(rule);
				}
				if(t instanceof SystemOnShutdownTrigger) {
					systemShutdownTriggeredRules.add(rule);
				}
				if(t instanceof CommandEventTrigger) {
					CommandEventTrigger ceTrigger = (CommandEventTrigger) t;
					Set<Rule> rules = commandEventTriggeredRules.get(ceTrigger.getItem());
					if(rules==null) {
						rules = new HashSet<Rule>();
						commandEventTriggeredRules.put(ceTrigger.getItem(), rules);
					}
					rules.add(rule);
				}
				if(t instanceof UpdateEventTrigger) {
					UpdateEventTrigger ueTrigger = (UpdateEventTrigger) t;
					Set<Rule> rules = updateEventTriggeredRules.get(ueTrigger.getItem());
					if(rules==null) {
						rules = new HashSet<Rule>();
						updateEventTriggeredRules.put(ueTrigger.getItem(), rules);
					}
					rules.add(rule);
				}
				if(t instanceof ChangedEventTrigger) {
					ChangedEventTrigger ceTrigger = (ChangedEventTrigger) t;
					Set<Rule> rules = changedEventTriggeredRules.get(ceTrigger.getItem());
					if(rules==null) {
						rules = new HashSet<Rule>();
						changedEventTriggeredRules.put(ceTrigger.getItem(), rules);
					}
					rules.add(rule);
				}
			}
		}

		protected synchronized void executeScript(Rule rule) {
			Script script = scriptEngine.newScriptFromXExpression(rule.getScript());
			try {
				logger.info("Executing rule '{}'", rule.getName());
				script.execute();
			} catch (ScriptExecutionException e) {
				logger.error("Error during the execution of rule {}", rule.getName(), e);
			}
		}

		protected synchronized void executeScripts(List<Rule> rules) {
			for(Rule rule : rules) {
				executeScript(rule);
			}
		}
		
		protected void removeRules(Collection<? extends Collection<Rule>> ruleSets, RuleModel model) {
			for(Collection<Rule> ruleSet : ruleSets) {
				// first remove all rules of the model, if not null (=non-existent)
				if(model!=null) {
					for(Rule rule : model.getRules()) {
						ruleSet.remove(rule);
					}
				}
				// now also remove all proxified rules from the set
				Set<Rule> clonedSet = new HashSet<Rule>(ruleSet);
				for(Rule rule : clonedSet) {
					if(rule.eIsProxy()) {
						ruleSet.remove(rule);
					}
				}
			}
		}
		
		private boolean isEnabled() {
			return !"true".equalsIgnoreCase(System.getProperty("noRules"));
		}
		
}
