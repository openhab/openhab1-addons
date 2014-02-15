/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.rule.internal.engine;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.XVariableDeclaration;
import org.eclipse.xtext.xbase.interpreter.IEvaluationContext;
import org.openhab.core.scriptengine.ScriptEngine;
import org.openhab.core.scriptengine.ScriptExecutionException;
import org.openhab.model.rule.RulesStandaloneSetup;
import org.openhab.model.rule.internal.RuleModelActivator;
import org.openhab.model.rule.rules.Rule;
import org.openhab.model.rule.rules.RuleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provider;

/**
 * Helper class to deal with rule evaluation contexts.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@SuppressWarnings("restriction")
public class RuleContextHelper {

	private static final Logger logger = LoggerFactory.getLogger(RuleContextHelper.class);

	/** Variable name for the previous state of an item in a "changed state triggered" rule */
	public static final String VAR_PREVIOUS_STATE = "previousState";

	/** Variable name for the received command in a "command triggered" rule */
	public static final String VAR_RECEIVED_COMMAND = "receivedCommand";
	
	private static Provider<IEvaluationContext> contextProvider = RulesStandaloneSetup.getInjector().getProvider(IEvaluationContext.class);
	private static ScriptEngine scriptEngine = RuleModelActivator.scriptEngineTracker.getService();

	/**
	 * Retrieves the evaluation context (= set of variables) for a rule. The context is shared with all rules in the same model (= rule file).
	 * 
	 * @param rule the rule to get the context for
	 * @return the evaluation context
	 */
	public static synchronized IEvaluationContext getContext(Rule rule) {
	    RuleModel ruleModel = (RuleModel) rule.eContainer();

	    // check if a context already exists on the resource
	    for(Adapter adapter : ruleModel.eAdapters()) {
			if(adapter instanceof RuleContextAdapter) {
				return ((RuleContextAdapter) adapter).getContext();
			}
		}
		
		// no evaluation context found, so create a new one
	    IEvaluationContext evaluationContext = contextProvider.get();
	    for(XExpression expr : ruleModel.getVariables()) {
	    	if (expr instanceof XVariableDeclaration) {
				XVariableDeclaration var = (XVariableDeclaration) expr;
				try {
			    	Object initialValue = var.getRight()==null ? null : scriptEngine.newScriptFromXExpression(var.getRight()).execute();
					evaluationContext.newValue(QualifiedName.create(var.getName()), initialValue);
				} catch (ScriptExecutionException e) {
					logger.warn("Variable '{}' on rule file '{}' cannot be initialized with value '{}': {}", 
							new String[] { var.getName(), ruleModel.eResource().getURI().path(), var.getRight().toString(), e.getMessage() });
				}
			}
	    }
	    ruleModel.eAdapters().add(new RuleContextAdapter(evaluationContext));
		return evaluationContext;
	}

	/**
	 * Inner class that wraps an evaluation context into an EMF adapters
	 */
	private static class RuleContextAdapter extends EContentAdapter {
		
		private IEvaluationContext context;

		public RuleContextAdapter(IEvaluationContext context) {
			this.context = context;
		}

		public IEvaluationContext getContext() {
			return context;
		}

	}

}
