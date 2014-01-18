/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.script.interpreter;

import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.util.PolymorphicDispatcher;
import org.eclipse.xtext.xbase.XAbstractFeatureCall;
import org.eclipse.xtext.xbase.XAssignment;
import org.eclipse.xtext.xbase.XFeatureCall;
import org.eclipse.xtext.xbase.XVariableDeclaration;
import org.eclipse.xtext.xbase.interpreter.IEvaluationContext;
import org.eclipse.xtext.xbase.interpreter.impl.XbaseInterpreter;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.Type;
import org.openhab.model.script.internal.engine.ItemRegistryProvider;
import org.openhab.model.script.lib.NumberExtensions;
import org.openhab.model.script.scoping.StateAndCommandProvider;

import com.google.inject.Inject;

/**
 * The script interpreter handles the openHAB specific script components, which are not known
 * to the standard Xbase interpreter.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@SuppressWarnings("restriction")
public class ScriptInterpreter extends XbaseInterpreter {

	@Inject
	ItemRegistryProvider itemRegistryProvider;
	
	@Inject
	StateAndCommandProvider stateAndCommandProvider;
		
	private PolymorphicDispatcher<Object> featureCallDispatcher = createFeatureCallDispatcher();

	protected Object _featureCallJvmIdentifyableElement(JvmIdentifiableElement identifiable, XFeatureCall featureCall, Object receiver,
			IEvaluationContext context, CancelIndicator indicator) {
		Object value = super._featureCallJvmIdentifyableElement(identifiable, featureCall, receiver, context, indicator);
		if(value==null && receiver==null) {
			for(Type type : stateAndCommandProvider.getAllTypes()) {
				if(type.toString().equals(featureCall.toString())) {
					return type;
				}
			}
			value = getItem(featureCall.toString());
		}
		return value;
	}
	
	protected Object internalFeatureCallDispatch(XAbstractFeatureCall featureCall, Object receiverObj,
			IEvaluationContext context, CancelIndicator indicator) {
		if(featureCall.getFeature().eIsProxy()) {
			throw new RuntimeException("The name '" + featureCall.toString() + "' cannot be resolved to an item or type.");
		}
		return featureCallDispatcher.invoke(featureCall.getFeature(), featureCall, receiverObj, context, indicator);
	}

	protected Object _assignValue(XVariableDeclaration variable, XAssignment assignment, Object value,
			IEvaluationContext context, CancelIndicator indicator) {
		context.assignValue(QualifiedName.create(variable.getName()), value);
		return value;
	}

	protected Item getItem(String itemName) {
		ItemRegistry itemRegistry = itemRegistryProvider.get();
		try {
			return itemRegistry.getItem(itemName);
		} catch (ItemNotFoundException e) {
			return null;
		}
	}
	
	@Override
	protected boolean eq(Object a, Object b) {
		if(a instanceof Type && b instanceof Number) { 
			return NumberExtensions.operator_equals((Type) a, (Number) b);
		} else if(a instanceof Number && b instanceof Type) {
			return NumberExtensions.operator_equals((Type) b, (Number) a);
		} else {
			return super.eq(a, b);
		}
	}
}
