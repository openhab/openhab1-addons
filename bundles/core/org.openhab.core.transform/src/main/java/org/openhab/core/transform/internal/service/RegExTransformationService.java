/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
