/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.script.internal.engine;


import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.interpreter.IEvaluationContext;
import org.eclipse.xtext.xbase.interpreter.IEvaluationResult;
import org.eclipse.xtext.xbase.interpreter.IExpressionInterpreter;
import org.openhab.core.scriptengine.Script;
import org.openhab.core.scriptengine.ScriptExecutionException;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * This is the default implementation of a {@link Script}.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@SuppressWarnings("restriction")
public class ScriptImpl implements Script {

	@Inject protected IExpressionInterpreter interpreter;
	@Inject protected Provider<IEvaluationContext> contextProvider;

	private XExpression xExpression;

	@Inject
	public ScriptImpl() {}

	/* package-local */
	 void setXExpression(XExpression xExpression) {
		this.xExpression = xExpression;
	}

	/* package-local */
	XExpression getXExpression() {
		return xExpression;
	}
	
	public Object execute() throws ScriptExecutionException {
	    IEvaluationContext evaluationContext = contextProvider.get();
	    return execute(evaluationContext);
	}

	public Object execute(IEvaluationContext evaluationContext) throws ScriptExecutionException {
		if(xExpression!=null) {
		    try {
		    	IEvaluationResult result = interpreter.evaluate(xExpression, evaluationContext, CancelIndicator.NullImpl);
			    if(result==null) {
			    	// this can only happen on an InterpreterCancelledException, i.e. NEVER ;-)
			    	return null;
			    }
			    if (result.getException() != null) {
			        throw new ScriptExecutionException(result.getException().getMessage(), result.getException());
			    } 
			    return result.getResult();
		    } catch(Throwable e) {
		    	if(e instanceof ScriptExecutionException) {
		    		throw (ScriptExecutionException) e;
		    	} else {
		    		throw new ScriptExecutionException("An error occured during the script execution: " + e.getMessage(), e);
		    	}
		    }
		} else {
	        throw new ScriptExecutionException("Script does not contain any expression");
		}
	}
}
