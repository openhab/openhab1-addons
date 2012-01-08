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
package org.openhab.model.script.interpreter;

import java.math.BigDecimal;

import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.xbase.XFeatureCall;
import org.eclipse.xtext.xbase.interpreter.IEvaluationContext;
import org.eclipse.xtext.xbase.interpreter.impl.EvaluationException;
import org.eclipse.xtext.xbase.interpreter.impl.XbaseInterpreter;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.types.Type;
import org.openhab.model.script.internal.engine.ItemRegistryProvider;
import org.openhab.model.script.scoping.StateAndCommandProvider;
import org.openhab.model.script.script.DecimalLiteral;

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
		
	protected Object _evaluateDecimalLiteral(DecimalLiteral literal, IEvaluationContext context, CancelIndicator indicator) {
		BigDecimal result = new BigDecimal(literal.getValue());
		return result;
	}
	
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
	
	protected Item getItem(String itemName) {
		ItemRegistry itemRegistry = itemRegistryProvider.get();
		try {
			return itemRegistry.getItem(itemName);
		} catch (ItemNotFoundException e) {
			throw new EvaluationException(e);
		} catch (ItemNotUniqueException e) {
			throw new EvaluationException(e);
		}
	}
}
