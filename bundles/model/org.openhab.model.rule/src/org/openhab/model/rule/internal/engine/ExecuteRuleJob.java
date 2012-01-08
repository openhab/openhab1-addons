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

import org.eclipse.emf.ecore.EObject;
import org.openhab.core.scriptengine.Script;
import org.openhab.core.scriptengine.ScriptEngine;
import org.openhab.core.scriptengine.ScriptExecutionException;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.rule.internal.RuleModelActivator;
import org.openhab.model.rule.rules.Rule;
import org.openhab.model.rule.rules.RuleModel;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of Quartz {@link Job}-Interface. It takes a rule
 * and simply executes it.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 */
public class ExecuteRuleJob implements Job {

	private static final Logger logger = LoggerFactory.getLogger(ExecuteRuleJob.class);
		
	public static final String JOB_DATA_RULEMODEL = "model";
	public static final String JOB_DATA_RULENAME = "rule";
	
	public void execute(JobExecutionContext context) throws JobExecutionException {
		String modelName = (String) context.getJobDetail().getJobDataMap().get(JOB_DATA_RULEMODEL);				
		String ruleName = (String) context.getJobDetail().getJobDataMap().get(JOB_DATA_RULENAME);
		
		ModelRepository modelRepository = RuleModelActivator.modelRepositoryTracker.getService();
		ScriptEngine scriptEngine = RuleModelActivator.scriptEngineTracker.getService();
		
		if(modelRepository!=null && scriptEngine!=null) {
			EObject model = modelRepository.getModel(modelName);
			if (model instanceof RuleModel) {
				RuleModel ruleModel = (RuleModel) model;
				Rule rule = getRule(ruleModel, ruleName);
				if(rule!=null) {
					Script script = scriptEngine.newScriptFromXExpression(rule.getScript());
					logger.debug("Executing scheduled rule '{}'", rule.getName());
					try {
						script.execute();
					} catch (ScriptExecutionException e) {
						logger.error("Error during the execution of rule {}", rule.getName(), e.getCause());
					}
				} else {
					logger.debug("Scheduled rule '{}' does not exist", ruleName);
				}
			} else {
				logger.debug("Rule file '{}' does not exist", modelName);
			}
		}
	}

	private Rule getRule(RuleModel ruleModel, String ruleName) {
		for(Rule rule : ruleModel.getRules()) {
			if(rule.getName().equals(ruleName)) {
				return rule;
			}
		}
		return null;
	}
}