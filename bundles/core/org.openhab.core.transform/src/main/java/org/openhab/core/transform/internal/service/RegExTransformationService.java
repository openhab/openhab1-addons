/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.transform.internal.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * The implementation of {@link TransformationService} which transforms the input by Regular Expressions.
 * </p>
 * <p>
 * <b>Note:</b> the given Regular Expression must contain exactly one group!
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public class RegExTransformationService implements TransformationService {

	static final Logger logger = LoggerFactory.getLogger(RegExTransformationService.class);

	/**
	 * @{inheritDoc
	 */
	public String transform(String regExpression, String source) throws TransformationException {
		
		if (regExpression == null || source == null) {
			throw new TransformationException("the given parameters 'regex' and 'source' must not be null");
		}

		logger.debug("about to transform '{}' by the function '{}'", source, regExpression);

		Matcher matcher = Pattern.compile("^" + regExpression + "$", Pattern.DOTALL).matcher(source.trim());
		if (!matcher.matches()) {
			logger.debug("the given regex '^{}$' doesn't match the given content '{}' -> couldn't compute transformation", regExpression, source);
			return null;
		}
		matcher.reset();

		String result = "";
		while (matcher.find()) {
			
			if (matcher.groupCount() == 0) {
				logger.info("the given regular expression '^{}$' doesn't contain a group. No content will be extracted and returned!", regExpression);
				continue;
			}
			
			result = matcher.group(1);
			
			if (matcher.groupCount() > 1) {
				logger.debug("the given regular expression '^{}$' contains more than one group. Only the first group will be returned!", regExpression);
			}
		}

		return result;
	}

}
