/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.habmin.internal.resources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class LabelSplitHelper {
	private String label = null;
	private String format = null;
	private String translationService = null;
	private String translationRule = null;
	private String unit = null;

	private static final Pattern LABEL_PATTERN = Pattern.compile("(.*?)\\[(.*)\\]");
	private static final Pattern MAP_PATTERN = Pattern.compile("([a-zA-Z]*?)\\((.*?)\\):(.*)");

	final static String conversions = "bBhHsScCdoxXeEfgGaAtT%n";

	public LabelSplitHelper(String itemLabel) {
		Matcher labelMatcher = LABEL_PATTERN.matcher(itemLabel);

		if (labelMatcher.matches()) {
			label = labelMatcher.group(1).trim();
			format = labelMatcher.group(2).trim();
		} else
			label = itemLabel;

		if (format != null) {
			Matcher mapMatcher = MAP_PATTERN.matcher(format);
			if (mapMatcher.matches()) {
				translationService = mapMatcher.group(1).trim();
				translationRule = mapMatcher.group(2).trim();
				format = mapMatcher.group(3).trim();
			}
		}

		if (format != null && format.length() > 0) {
			format = format.trim();

			// Split the string according to the "Formatter" format
			// specification
			// Everything up to the last format string goes in the
			// format
			// Everything after goes in the units
			int state = 0;
			String s1 = "";
			String s2 = "";
			for (char ch : format.toCharArray()) {
				switch (state) {
				case 0:
					// Looking for start
					s2 += ch;
					if (ch == '%') {
						state = 1;
					}
					break;
				case 1:
					// Looking for end (conversion id)
					if (ch == '%') {
						// %% is not considered part of the format -
						// it's the "unit"
						state = 0;
						break;
					}
					// Concatenate here - this will remove the double %
					s2 += ch;
					if (conversions.indexOf(ch) != -1) {
						// This is a valid conversion ID
						s1 += s2;
						s2 = "";
						state = 0;

						// Is this a time format?
						if (ch == 't' || ch == 'T')
							state = 2;
					}
					break;
				case 2:
					// One more character for time conversion
					s1 += ch;
					state = 0;
					break;
				}
			}

			format = s1.trim();
			unit = s2.trim();
		}
	}

	public String getLabel() {
		return label;
	}

	public String getFormat() {
		return format;
	}

	public String getUnit() {
		return unit;
	}

	public String getTranslationService() {
		return translationService;
	}

	public String getTranslationRule() {
		return translationRule;
	}

	public LabelSplitHelper(String newLabel, String newFormat, String newUnit, String newService, String newRule) {
		label = newLabel;
		format = newFormat;
		unit = newUnit;
		translationService = newService;
		translationRule = newRule;
	}

	void setLabel(String newLabel) {
		label = newLabel;
	}

	void setFormat(String newFormat) {
		format = newFormat;
	}

	void setUnit(String newUnit) {
		unit = newUnit;
	}

	void setTranslationService(String newService) {
		translationService = newService;
	}

	void setTranslationRule(String newRule) {
		translationRule = newRule;
	}

	public String getLabelString() {
		String config = "";

		// Ensure everything is a string
		if (label == null)
			label = "";
		if (format == null)
			format = "";
		if (unit == null)
			unit = "";
		if (translationService == null)
			translationService = "";
		if (translationRule == null)
			translationRule = "";

		// Resolve double %% in unit
		String unitOut = "";
		for (int c = 0; c < unit.length(); c++) {
			unitOut += unit.charAt(c);
			if (unit.charAt(c) == '%')
				unitOut += '%';
		}
		unit = unitOut;

		// Concatenate it all together!
		config += label;
		if (!format.isEmpty() || !unit.isEmpty() || !translationService.isEmpty() || !translationRule.isEmpty()) {
			config += " [";
			if (!translationService.isEmpty() && !translationRule.isEmpty())
				config += translationService + "(" + translationRule + "):";
			if (!format.isEmpty())
				config += format;
			if (!unit.isEmpty())
				config += " " + unit;
			config += "]";
		}

		return config;
	}
}
