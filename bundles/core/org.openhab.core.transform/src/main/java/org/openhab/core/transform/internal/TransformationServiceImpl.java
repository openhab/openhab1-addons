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

package org.openhab.core.transform.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationProcessor;
import org.openhab.core.transform.TransformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Default implementation of the {@link TransformationService}. It takes a
 * transformation function like <code>REGEX(.*)</code>, parses it, searches for
 * a corresponding {@link TransformationProcessor} and delegates to transformation
 * to it.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public class TransformationServiceImpl implements TransformationService {

	static final Logger logger = LoggerFactory.getLogger(TransformationServiceImpl.class);
	
	/** RegEx to extract a parse a function String <code>'(.*)\((.*)\)'</code> */
	private static final Pattern EXTRACT_FUNCTION_PATTERN = Pattern.compile("(.*)\\((.*)\\)");
	
	
	/**
	 * @{inheritDoc}
	 */
	public String transform(String function, String source) throws TransformationException {
		
		if (function == null || source == null) {
			throw new TransformationException("the given parameters 'function' and 'source' must not be null");
		}
		
		Matcher matcher = EXTRACT_FUNCTION_PATTERN.matcher(function);
		
		if (!matcher.matches()) {
			throw new IllegalArgumentException("given transformation function '" + function + "' does not follow the expected pattern '<function>(<pattern>)'");
		}
		matcher.reset();
		
		while (matcher.find()) {
			
    		String type = matcher.group(1);
    		String pattern = matcher.group(2);
    		
    		TransformationProcessor processor = findProcessor(type);
    		if (processor != null) {
    			processor.transform(pattern, source);
    		}

    		// by definition only one occurrence of a transformation function 
    		// is valid, so we take the first one to operate on ...
    		break;
    	}
		
		return source;
		
	}

	/**
	 * Returns the {@link TransformationProcessor} according to the given 
	 * <code>type</code> or <code>null</code> if there is none matching.
	 * 
	 * @param type the type of the {@link TransformationProcessor} eg. REGEX, 
	 * XPATH or XSLT
	 * 
	 * @return the instance of the {@link TransformationProcessor} or <code>null</code>
	 * if there is no matching {@link TransformationProcessor}
	 */
	private TransformationProcessor findProcessor(String type) {
		return TransformationActivator.getProcessorMap().get(type.toUpperCase());
	}
	
	
}
