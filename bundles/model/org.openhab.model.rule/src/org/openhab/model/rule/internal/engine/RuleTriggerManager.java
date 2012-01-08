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
package org.openhab.model.rule.internal.engine;

import static org.openhab.model.rule.internal.engine.RuleTriggerManager.TriggerTypes.TIMER;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.util.EcoreUtil;
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
import org.openhab.model.rule.rules.TimerTrigger;
import org.openhab.model.rule.rules.UpdateEventTrigger;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger logger = LoggerFactory.getLogger(RuleTriggerManager.class);
	
	public enum TriggerTypes {
		UPDATE,		// fires whenever a status update is received for an item 
		CHANGE, 	// same as UPDATE, but only fires if the current item state is changed by the update 
		COMMAND, 	// fires whenever a command is received for an item
		STARTUP, 	// fires when the rule engine bundle starts and once as soon as all required items are available
		SHUTDOWN,	// fires when the rule engine bundle is stopped
		TIMER		// fires at a given time
	}
	
	// lookup maps for different triggering conditions
	private Map<String, Set<Rule>> updateEventTriggeredRules = Maps.newHashMap();
	private Map<String, Set<Rule>> changedEventTriggeredRules = Maps.newHashMap();
	private Map<String, Set<Rule>> commandEventTriggeredRules = Maps.newHashMap();
	private List<Rule> systemStartupTriggeredRules = Lists.newArrayList();
	private List<Rule> systemShutdownTriggeredRules = Lists.newArrayList();
	private List<Rule> timerEventTriggeredRules = Lists.newArrayList();

	// the scheduler used for timer events
	private Scheduler scheduler;
	
	public RuleTriggerManager() {
		 try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
            logger.error("initializing scheduler throws exception", e);
		}
	}
	
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
			case TIMER:    result = timerEventTriggeredRules; break;
			case UPDATE:   result = Iterables.concat(updateEventTriggeredRules.values()); break;
			case CHANGE:   result = Iterables.concat(changedEventTriggeredRules.values()); break;
			case COMMAND:  result = Iterables.concat(commandEventTriggeredRules.values()); break;
			default:       result = Sets.newHashSet();
		}
		return result;
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
			case STARTUP:  systemStartupTriggeredRules.clear(); break;
			case SHUTDOWN: systemShutdownTriggeredRules.clear(); break;
			case UPDATE:   updateEventTriggeredRules.clear(); break;
			case CHANGE:   changedEventTriggeredRules.clear(); break;
			case COMMAND:  commandEventTriggeredRules.clear(); break;
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
			} else if(t instanceof SystemOnShutdownTrigger) {
				systemShutdownTriggeredRules.add(rule);
			} else if(t instanceof CommandEventTrigger) {
				CommandEventTrigger ceTrigger = (CommandEventTrigger) t;
				Set<Rule> rules = commandEventTriggeredRules.get(ceTrigger.getItem());
				if(rules==null) {
					rules = new HashSet<Rule>();
					commandEventTriggeredRules.put(ceTrigger.getItem(), rules);
				}
				rules.add(rule);
			} else if(t instanceof UpdateEventTrigger) {
				UpdateEventTrigger ueTrigger = (UpdateEventTrigger) t;
				Set<Rule> rules = updateEventTriggeredRules.get(ueTrigger.getItem());
				if(rules==null) {
					rules = new HashSet<Rule>();
					updateEventTriggeredRules.put(ueTrigger.getItem(), rules);
				}
				rules.add(rule);
			} else if(t instanceof ChangedEventTrigger) {
				ChangedEventTrigger ceTrigger = (ChangedEventTrigger) t;
				Set<Rule> rules = changedEventTriggeredRules.get(ceTrigger.getItem());
				if(rules==null) {
					rules = new HashSet<Rule>();
					changedEventTriggeredRules.put(ceTrigger.getItem(), rules);
				}
				rules.add(rule);
			} else if(t instanceof TimerTrigger) {
				timerEventTriggeredRules.add(rule);
				try {
					createTimer(rule, (TimerTrigger) t);
				} catch (SchedulerException e) {
					logger.error("Cannot create timer for rule '{}': {}", rule.getName(), e.getMessage());
				}
			}
		}
	}
	
	/**
	 * Removes a given rule from the mapping tables of a certain trigger type
	 * 
	 * @param type the trigger type for which the rule should be removed
	 * @param rule the rule to add
	 */
	public void removeRule(TriggerTypes type, Rule rule) {
		switch(type) {
			case STARTUP:  systemStartupTriggeredRules.remove(rule); break;
			case SHUTDOWN: systemShutdownTriggeredRules.remove(rule); break;
			case UPDATE:   updateEventTriggeredRules.remove(rule); break;
			case CHANGE:   changedEventTriggeredRules.remove(rule); break;
			case COMMAND:  commandEventTriggeredRules.remove(rule); break;
			case TIMER:    timerEventTriggeredRules.remove(rule); 
							try {
								removeTimer(rule);
							} catch (SchedulerException e) {
								logger.error("Cannot remove timer for rule '{}'", rule.getName(), e);
							}
						   break;
		}
	}
	
	/**
	 * Adds all rules of a model to the mapping tables
	 * 
	 * @param model the rule model
	 */
	public void addRuleModel(RuleModel model) {
		for(Rule rule : model.getRules()) {
			addRule(rule);
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
		// remove the scheduled rules
		for(Rule rule : new ArrayList<Rule>(timerEventTriggeredRules)) {
			removeRule(TIMER, rule);
		}
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

	/**
	 * Creates and schedules a new quartz-job and trigger with model and rule name as jobData.
	 * 
	 * @param rule the rule to schedule
	 * @param trigger the defined trigger 
	 * 
	 * @throws SchedulerException if there is an internal Scheduler error.
	 */
	private void createTimer(Rule rule, TimerTrigger trigger) throws SchedulerException {
		String cronExpression = trigger.getCron();
		if(trigger.getTime()!=null) {
			if(trigger.getTime().equals("noon")) {
				cronExpression = "0 0 12 * * ?";
			} else if(trigger.getTime().equals("midnight")) {
				cronExpression = "0 0 0 * * ?";
			}
		}
		String jobIdentity = getJobIdentityString(rule);

		try {
	        JobDetail job = newJob(ExecuteRuleJob.class)
	        	.usingJobData(ExecuteRuleJob.JOB_DATA_RULEMODEL, rule.eResource().getURI().path())
	        	.usingJobData(ExecuteRuleJob.JOB_DATA_RULENAME, rule.getName())
	            .withIdentity(jobIdentity)
	            .build();
	
	        Trigger quartzTrigger = newTrigger()
		            .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
		            .build();

	        scheduler.scheduleJob(job, quartzTrigger);

			logger.debug("Scheduled rule {} with cron expression {}", new String[] { rule.getName(), cronExpression });
		} catch(RuntimeException e) {
			throw new SchedulerException(e.getMessage());
		}
	}

	/**
	 * Delete all {@link Job}s of the group <code>rule.getName()</code>
	 * 
	 * @throws SchedulerException if there is an internal Scheduler error.
	 */
	private void removeTimer(Rule rule) throws SchedulerException {
		JobKey jobKey = JobKey.jobKey(getJobIdentityString(rule));
		if(jobKey!=null) {
			boolean success = scheduler.deleteJob(jobKey);
			if(!success) {
				logger.warn("Failed to delete cron job '{}'", jobKey.getName());
			}
		}
	}
	
	private String getJobIdentityString(Rule rule) {
		String jobIdentity = EcoreUtil.getURI(rule).trimFragment().appendFragment(rule.getName()).toString();
		return jobIdentity;
	}
}
