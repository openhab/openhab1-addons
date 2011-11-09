package org.openhab.model.rule.internal.engine;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.TypeParser;
import org.openhab.model.rule.rules.ChangedEventTrigger;
import org.openhab.model.rule.rules.CommandEventTrigger;
import org.openhab.model.rule.rules.EventTrigger;
import org.openhab.model.rule.rules.Rule;
import org.openhab.model.rule.rules.RuleModel;
import org.openhab.model.rule.rules.SystemOnShutdownTrigger;
import org.openhab.model.rule.rules.SystemOnStartupTrigger;
import org.openhab.model.rule.rules.UpdateEventTrigger;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * This is a helper class which deals with everything about rule triggers.
 * It keeps lists of which rule must be executed for which trigger and takes
 * over the evaluation of states and trigger conditions for the rule engine.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
public class RuleTriggerManager {

	public enum TriggerTypes {
		UPDATE, CHANGE, COMMAND, STARTUP, SHUTDOWN
	}
	
	// lookup maps for different triggering conditions
	private Map<String, Set<Rule>> updateEventTriggeredRules = Maps.newHashMap();
	private Map<String, Set<Rule>> changedEventTriggeredRules = Maps.newHashMap();
	private Map<String, Set<Rule>> commandEventTriggeredRules = Maps.newHashMap();
	private List<Rule> systemStartupTriggeredRules = Lists.newArrayList();
	private List<Rule> systemShutdownTriggeredRules = Lists.newArrayList();

	/**
	 * Returns all rules which have a trigger of a given type
	 * 
	 * @param type the trigger type of the rules to return
	 * @return rules with triggers of the given type
	 */
	public Iterable<Rule> getRules(TriggerTypes type) {
		Iterable<Rule> result;
		switch(type) {
			case STARTUP:  result = systemStartupTriggeredRules; break;
			case SHUTDOWN: result = systemShutdownTriggeredRules; break;
			case UPDATE:   result = Iterables.concat(updateEventTriggeredRules.values()); break;
			case CHANGE:   result = Iterables.concat(changedEventTriggeredRules.values()); break;
			case COMMAND:  result = Iterables.concat(commandEventTriggeredRules.values()); break;
			default:       result = Sets.newHashSet();
		}
		return Iterables.unmodifiableIterable(result);
	}

	/**
	 * Returns all rules for which the trigger condition is true for the given type, item and state.
	 * 
	 * @param triggerType
	 * @param item
	 * @param state
	 * @return all rules for which the trigger condition is true
	 */
	public Iterable<Rule> getRules(TriggerTypes triggerType, Item item, State state) {
		return internalGetRules(triggerType, item, state, null);
	}

	/**
	 * Returns all rules for which the trigger condition is true for the given type, item and states.
	 * 
	 * @param triggerType
	 * @param item
	 * @param oldState
	 * @param newState
	 * @return all rules for which the trigger condition is true
	 */
	public Iterable<Rule> getRules(TriggerTypes triggerType, Item item, State oldState, State newState) {
		return internalGetRules(triggerType, item, newState, oldState);
	}

	/**
	 * Returns all rules for which the trigger condition is true for the given type, itemName and command.
	 * 
	 * @param triggerType
	 * @param itemName
	 * @param command
	 * @return all rules for which the trigger condition is true
	 */
	public Iterable<Rule> getRules(TriggerTypes triggerType, String itemName, Command command) {
		Item dummyItem = new GenericItem(itemName) {
			public List<Class<? extends State>> getAcceptedDataTypes() {
				return Lists.newArrayList();
			}
			
			public List<Class<? extends Command>> getAcceptedCommandTypes() {
				return Lists.newArrayList();
			}
		};
		return internalGetRules(triggerType, dummyItem, command, null);
	}

	private Iterable<Rule> getAllRules(TriggerTypes type, String itemName) {
		switch(type) {
			case STARTUP:  return systemStartupTriggeredRules;
			case SHUTDOWN: return systemShutdownTriggeredRules;
			case UPDATE:   return updateEventTriggeredRules.get(itemName);
			case CHANGE:   return changedEventTriggeredRules.get(itemName);
			case COMMAND:  return commandEventTriggeredRules.get(itemName);
			default:       return Sets.newHashSet();
		}
	}

	private Iterable<Rule> internalGetRules(TriggerTypes triggerType, Item item, Type type1, Type type2) {
		List<Rule> result = Lists.newArrayList();
		Iterable<Rule> rules = getAllRules(triggerType, item.getName());
		if(rules==null) {
			rules = Lists.newArrayList();
		}
		switch(triggerType) {
		case STARTUP:  return systemStartupTriggeredRules;
		case SHUTDOWN: return systemShutdownTriggeredRules;
		case UPDATE:   
			if(type1 instanceof State) {
				State state = (State) type1;
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
								result.add(rule);
							}
						}
					}
				}
			}
			break;
		case CHANGE:
			if(type1 instanceof State && type2 instanceof State) {
				State newState = (State) type1;
				State oldState = (State) type2;
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
								result.add(rule);
							}
						}
					}
				}
			}
			break;
		case COMMAND:  
			if(type1 instanceof Command) {
				Command command = (Command) type1;
				for(Rule rule : rules) {
					for(EventTrigger t : rule.getEventtrigger()) {
						if (t instanceof CommandEventTrigger) {
							CommandEventTrigger ct = (CommandEventTrigger) t;
							if(ct.getItem().equals(item.getName()) &&
									(ct.getCommand()==null || command.equals(ct.getCommand()))) {
								result.add(rule);
							}
						}
					}
				}
			}
			break;
		}
		return result;
	}

	/**
	 * Removes all rules with a given trigger type from the mapping tables.
	 * 
	 * @param type the trigger type 
	 */
	public void clear(TriggerTypes type) {
		switch(type) {
			case STARTUP:  systemStartupTriggeredRules.clear();
			case SHUTDOWN: systemShutdownTriggeredRules.clear();
			case UPDATE:   updateEventTriggeredRules.clear();
			case CHANGE:   changedEventTriggeredRules.clear();
			case COMMAND:  commandEventTriggeredRules.clear();
		}
	}

	/**
	 * Removes all rules from all mapping tables.
	 */
	public void clearAll() {
		updateEventTriggeredRules.clear();
		commandEventTriggeredRules.clear();
		changedEventTriggeredRules.clear();
		systemShutdownTriggeredRules.clear();
		systemStartupTriggeredRules.clear();		
	}
	
	/**
	 * Adds a given rule to the mapping tables
	 * 
	 * @param rule the rule to add
	 */
	public synchronized void addRule(Rule rule) {
		for(EventTrigger t : rule.getEventtrigger()) {
			// add the rule to the lookup map for the trigger kind
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
	
	/**
	 * Removes all rules of a given model (file) from the mapping tables.
	 * 
	 * @param ruleModel the rule model
	 */
	public void removeRuleModel(RuleModel ruleModel) {
		removeRules(updateEventTriggeredRules.values(), ruleModel);
		removeRules(changedEventTriggeredRules.values(), ruleModel);
		removeRules(commandEventTriggeredRules.values(), ruleModel);
		removeRules(Collections.singletonList(systemStartupTriggeredRules), ruleModel);
		removeRules(Collections.singletonList(systemShutdownTriggeredRules), ruleModel);		
	}

	private void removeRules(Collection<? extends Collection<Rule>> ruleSets, RuleModel model) {
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
	
}
