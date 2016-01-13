/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.rule.internal.engine;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.TypeParser;
import org.openhab.model.rule.rules.ChangedEventTrigger;
import org.openhab.model.rule.rules.CommandEventTrigger;
import org.openhab.model.rule.rules.EventTrigger;
import org.openhab.model.rule.rules.ItemEventTrigger;
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
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
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
 * @author dero
 * @since 0.9.0
 *
 */
public class RuleTriggerManager {

	private static final Logger logger = LoggerFactory.getLogger(RuleTriggerManager.class);
	
	private Map<String, RuleModel> models = Maps.newHashMap();	
	
	private List<Rule> systemStartupTriggeredRules = Lists.newArrayList();
	private List<Rule> systemShutdownTriggeredRules = Lists.newArrayList();
	private Map<Class<?>, Map<Item, List<Map.Entry<?, Rule>>>> cache = Maps.newHashMap();
	
	// the scheduler used for timer events
	private Scheduler scheduler;
	private Map<Rule, List<JobKey>> ruleJobKeys = Maps.newHashMap();	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> Map<Item, List<Map.Entry<T, Rule>>> getCache(Class<T> clazz) {
		Map<Item, List<Map.Entry<?, Rule>>> result = cache.get(clazz);
		if (result == null)
			cache.put(clazz, result = Maps.newHashMap());
		return (Map) /* sic! */ result;
	}
	
	public RuleTriggerManager() {
		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
            logger.error("initializing scheduler throws exception", e);
		}
	}
	
	public void addRuleModel(String modelName, RuleModel ruleModel) {
		for (Rule rule: ruleModel.getRules()) {
			if (Iterables.any(rule.getEventtrigger(), Predicates.instanceOf(SystemOnStartupTrigger.class)))
				systemStartupTriggeredRules.add(rule);

			if (Iterables.any(rule.getEventtrigger(), Predicates.instanceOf(SystemOnShutdownTrigger.class)))
				systemShutdownTriggeredRules.add(rule);
			
			for (TimerTrigger trigger: Iterables.filter(rule.getEventtrigger(), TimerTrigger.class)) {
				try {
					createTimer(rule, (TimerTrigger) trigger);
				} catch (SchedulerException e) {
					logger.error("Cannot create timer for rule '{}': {}", rule.getName(), e.getMessage());
				}
			}
		}
		models.put(modelName, ruleModel);
		cache.clear();
	}
	
	/**
	 * Get trigger mapping for given item with triggers of type T.
	 * 
	 * If the item was not seen before, all matching triggers are bound to the item. 
	 * 
	 * @param item
	 * @param clazz
	 * @return
	 */
	private <T> Iterable<Map.Entry<T, Rule>> getTriggers(Item item, Class<T> clazz) {
		Map<Item, List<Map.Entry<T, Rule>>> cache = getCache(clazz);
		List<Map.Entry<T, Rule>> triggers = cache.get(item);
		if (triggers == null) {
			// first time we see this item, need to build a trigger cache
			cache.put(item, triggers = Lists.newArrayList());
			for (RuleModel ruleModel: models.values()) {
				for (final Rule rule: ruleModel.getRules()) {
					for (final ItemEventTrigger trigger: Iterables.filter(rule.getEventtrigger(), ItemEventTrigger.class)) {
						if (clazz.isAssignableFrom(trigger.getTrigger().getClass())) {
							final Pattern p = Pattern.compile(trigger.getItem());
							if (Iterables.any(!trigger.isIn() ? Collections.singleton(item.getName()) : item.getGroupNames(), 
									new Predicate<String>() { 
										public boolean apply(String s) {
											boolean matched = p.matcher(s).matches();
											logger.debug("Matching pattern '{}' of rule '{}' to name '{}' -> {}", trigger.getItem(), rule.getName(), s, matched ? "MATCHED" : "NOT MATCHED");
											return matched; 
										}
									})) {
								triggers.add(new AbstractMap.SimpleEntry<T, Rule>(clazz.cast(trigger.getTrigger()), rule));
								logger.info("Bound item '{}' to rule '{}'", item.getName(), rule.getName());
							}
						}
					}
				}
			}

		}
		return triggers;
	}

	public Iterable<Rule> getShutdownRules() {
		return systemShutdownTriggeredRules;
	}
		
 
	public void clearAll() {
		for (String model: Lists.newArrayList(models.keySet())) {
			removeRuleModel(model);
		}
	}

	private <T> Iterable<Rule> getRules(final Item item, Class<T> clazz, final Predicate<T> predicate) {
		return Iterables.filter(Iterables.transform(getTriggers(item, clazz),  
			new Function<Map.Entry<T, Rule>, Rule>() {
				public Rule apply(Map.Entry<T, Rule> t) {
					return predicate.apply(t.getKey()) ? t.getValue() : null;
				}
			}), Predicates.notNull());
	}
	
	public Iterable<Rule> getUpdateRules(final Item item, final State state) {
		return getRules(item, UpdateEventTrigger.class, 
			new Predicate<UpdateEventTrigger>() { 
				public boolean apply(UpdateEventTrigger ut) {
					return ut.getState() == null
						|| state.equals(TypeParser.parseState(item.getAcceptedDataTypes(), ut.getState()));
				}
		});
	}
	
	public Iterable<Rule> getChangeRules(final Item item, final State oldState, final State newState) {
		return getRules(item, ChangedEventTrigger.class, 
			new Predicate<ChangedEventTrigger>() { 
				public boolean apply(ChangedEventTrigger ct) {
						return (ct.getOldState() == null
								|| oldState.equals(TypeParser.parseState(item.getAcceptedDataTypes(), ct.getOldState())))
							&&
								(ct.getNewState() == null
								|| newState.equals(TypeParser.parseState(item.getAcceptedDataTypes(), ct.getNewState())));
				}
		});
	}
	
	public Iterable<Rule> getCommandRules(final Item item, final Command command) {
		return getRules(item, CommandEventTrigger.class, 
			new Predicate<CommandEventTrigger>() { 
				public boolean apply(CommandEventTrigger ct) {
					return ct.getCommand() == null
						|| command.equals(TypeParser.parseState(item.getAcceptedDataTypes(), ct.getCommand()));
				}
		});
	}
	
	public void removeRuleModel(String modelName) {
		RuleModel model = models.remove(modelName);
		if (model == null)
			return;
		
		for (Rule rule: model.getRules()) {
			systemStartupTriggeredRules.remove(rule);
			systemShutdownTriggeredRules.remove(rule);
			try {
				removeTimer(rule);
			} catch (SchedulerException e) {
				logger.error("Cannot remove timer for rule '{}'", rule.getName(), e);
			}
		}
		cache.clear();
	}

	public Iterable<Rule> getStartupRules() {
		return systemStartupTriggeredRules;
	}

	public void removeStartupRule(Rule rule) {
		systemStartupTriggeredRules.remove(rule);		
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
		if (trigger.getTime() != null) {
			if (trigger.getTime().equals("noon")) {
				cronExpression = "0 0 12 * * ?";
			} else if (trigger.getTime().equals("midnight")) {
				cronExpression = "0 0 0 * * ?";
			} else {
				logger.warn("Unrecognized time expression '{}' in rule '{}'", new String[] { trigger.getTime(), rule.getName() });
				return;
			}
		}
		
		String jobIdentity = getJobIdentityString(rule, trigger);

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
	        
	        List<JobKey> jobKeys = ruleJobKeys.get(rule);
	        if (jobKeys == null)
	        	ruleJobKeys.put(rule, jobKeys = Lists.newArrayList());
	        jobKeys.add(job.getKey());

			logger.debug("Scheduled rule {} with cron expression {}", new String[] { rule.getName(), cronExpression });
		} catch (RuntimeException e) {
			throw new SchedulerException(e.getMessage());
		}
	}

	/**
	 * Delete all {@link Job}s of the DEFAULT group whose name starts with
	 * <code>rule.getName()</code>.
	 * 
	 * @throws SchedulerException if there is an internal Scheduler error.
	 */
	private void removeTimer(Rule rule) throws SchedulerException {
		List<JobKey> jobKeys = ruleJobKeys.remove(rule);
		if (jobKeys == null)
			return;
 
		for (JobKey jobKey : jobKeys) {
			boolean success = scheduler.deleteJob(jobKey);
			if (!success) {
				logger.warn("Failed to delete cron job '{}'", jobKey.getName());
			} else {
				logger.debug("Removed scheduled cron job '{}'", jobKey.getName());
			}
		}
	}
	
	private String getJobIdentityString(Rule rule, TimerTrigger trigger) {
		String jobIdentity = EcoreUtil.getURI(rule).trimFragment().appendFragment(rule.getName()).toString();
		if (trigger != null) {
			if (trigger.getTime() != null) {
				jobIdentity += "#" + trigger.getTime();
			} else if (trigger.getCron() != null ) {
				jobIdentity += "#" + trigger.getCron();
			}
		}
		return jobIdentity;
	}
	
}
