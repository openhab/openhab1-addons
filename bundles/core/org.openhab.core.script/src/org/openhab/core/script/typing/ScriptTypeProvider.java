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

package org.openhab.core.script.typing;

import java.math.BigDecimal;

import org.eclipse.xtext.common.types.JvmTypeReference;
import org.eclipse.xtext.common.types.util.TypeReferences;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.typing.XbaseTypeProvider;
import org.openhab.core.script.script.DecimalLiteral;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * This type provider must translate script specific types that are not known to Xbase
 * to the corresponding jvm types.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@SuppressWarnings("restriction")
@Singleton
public class ScriptTypeProvider extends XbaseTypeProvider {

	@Inject
	private TypeReferences typeReferences;

	@Override
	protected JvmTypeReference type(XExpression expression, JvmTypeReference rawExpectation, boolean rawType) {
		if(expression instanceof DecimalLiteral) {
			return typeReferences.getTypeForName(BigDecimal.class, expression);
		} else {
			return super.type(expression, rawExpectation, rawType);
		}
	}
		
}
