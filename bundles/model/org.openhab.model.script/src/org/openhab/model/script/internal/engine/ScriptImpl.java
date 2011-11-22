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

package org.openhab.model.script.internal.engine;


import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.interpreter.IEvaluationContext;
import org.eclipse.xtext.xbase.interpreter.IEvaluationResult;
import org.eclipse.xtext.xbase.interpreter.IExpressionInterpreter;
import org.eclipse.xtext.xbase.scoping.XbaseScopeProvider;
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
		Object thisElement = null;
	    IEvaluationContext evaluationContext = contextProvider.get();
	    evaluationContext.newValue(XbaseScopeProvider.THIS, thisElement);
	    IEvaluationResult result = interpreter.evaluate(xExpression, evaluationContext, CancelIndicator.NullImpl);
	    if (result.getException() != null) {
	        throw new ScriptExecutionException("Error executing script", result.getException());
	    } 
	    return result.getResult();
	}
}
