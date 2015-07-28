/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.rule.valueconverter;

import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.xbase.conversion.XbaseValueConverterService;
import org.eclipse.xtext.conversion.impl.AbstractNullSafeConverter;

import java.util.regex.Pattern;

@SuppressWarnings("restriction")
public class RuleConverters extends XbaseValueConverterService {

	private static final Pattern ID_PATTERN = Pattern.compile("\\p{Alpha}\\w*");

	@ValueConverter(rule = "ValidCommand")
	public IValueConverter<String> ValidCommand() {
		return new AbstractNullSafeConverter<String>() {
			@Override
			protected String internalToValue(String string, INode node) {
				if((string.startsWith("'") && string.endsWith("'"))||(string.startsWith("\"") && string.endsWith("\""))) {
					return STRING().toValue(string, node);
				}
				return ID().toValue(string, node);
			}

			@Override
			protected String internalToString(String value) {
				if(ID_PATTERN.matcher(value).matches()) {
					return ID().toString(value);
				} else {
					return STRING().toString(value);
				}
			}
		};
	}

	@ValueConverter(rule = "ValidState")
	public IValueConverter<String> ValidState() {
		return new AbstractNullSafeConverter<String>() {
			@Override
			protected String internalToValue(String string, INode node) {
				if((string.startsWith("'") && string.endsWith("'"))||(string.startsWith("\"") && string.endsWith("\""))) {
					return STRING().toValue(string, node);
				}
				return ID().toValue(string, node);
			}

			@Override
			protected String internalToString(String value) {
				if(ID_PATTERN.matcher(value).matches()) {
					return ID().toString(value);
				} else {
					return STRING().toString(value);
				}
			}
		};
	}
}
