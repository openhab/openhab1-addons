/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.rule.internal.engine;

import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.xbase.interpreter.IEvaluationContext;
import org.eclipse.xtext.xbase.interpreter.impl.DefaultEvaluationContext;

@SuppressWarnings("restriction")
public class RuleEvaluationContext extends DefaultEvaluationContext {

	private IEvaluationContext globalContext = null;
	
	public RuleEvaluationContext() {
		super(new DefaultEvaluationContext());
	}
	
	public void setGlobalContext(IEvaluationContext context) {
		this.globalContext = context;
	}
	
	@Override
	public Object getValue(QualifiedName qualifiedName) {
		Object value = super.getValue(qualifiedName);
		if(value==null && this.globalContext!=null) {
			value = globalContext.getValue(qualifiedName);
		}
		return value;
	}
	
	@Override
	public void assignValue(QualifiedName qualifiedName, Object value) {
		try {
			super.assignValue(qualifiedName, value);
		} catch(IllegalStateException e) {
			if(globalContext!=null) {
				globalContext.assignValue(qualifiedName, value);
			} else {
				throw e;
			}
		}
	}

}
