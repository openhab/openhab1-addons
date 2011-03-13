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

package org.openhab.binding.http.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This Aggregator keeps references of all relevant {@link TransformationService}s
 * It takes a transformation function like <code>REGEX(.*)</code>, parses it, 
 * searches for the corresponding {@link TransformationService} and delegates the
 * transformation to it.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public class TransformationServiceAggregator {

	static final Logger logger = LoggerFactory.getLogger(TransformationServiceAggregator.class);
	
	/** maps the component name to the processor instances */
	private Map<String, TransformationService> processorCache;

	/** RegEx to extract a parse a function String <code>'(.*?)\((.*)\)'</code> */
	private static final Pattern EXTRACT_FUNCTION_PATTERN = Pattern.compile("(.*?)\\((.*)\\)");
	
	
	public TransformationServiceAggregator() {
		processorCache = new HashMap<String, TransformationService>();
	}
	
	
	public void addTransformationService(TransformationService transformationService) {
		processorCache.put(transformationService.getName(), transformationService);
	}

	public void removeTransformationService(TransformationService transformationService) {
		processorCache.remove(transformationService.getName());
	}


	/**
	 * Transforms the input <code>source</code> by means of the given <code>function</code>
	 * and returns the transformed output. If the transformation couldn't be completed
	 * for any reason, <code>source</code> will be returned unchanged. The method to be
	 * used to transform the input is extracted from the <code>function</code>.
	 * 
	 * @param function the type of function to be used to transform the input 
	 * including the function itself
	 * @param source the input to be transformed
	 * 
	 * @return the transformed result or the unchanged <code>source</code> if the
	 * transformation couldn't be completed for any reason.
	 * 
	 * @throws TransformationException if any error occurs
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
    		
    		TransformationService service = findProcessor(type);
    		if (service != null) {
    			return service.transform(pattern, source);
    		}

    		// by definition only one occurrence of a transformation function 
    		// is valid, so we take the first one to operate on ...
    		break;
    	}
		
		return source;
		
	}

	/**
	 * Returns the {@link TransformationService} according to the given 
	 * <code>type</code> or <code>null</code> if there is none matching.
	 * 
	 * @param type the type of the {@link TransformationService} eg. REGEX, 
	 * XPATH or XSLT
	 * 
	 * @return the instance of the {@link TransformationService} or <code>null</code>
	 * if there is no matching {@link TransformationService}
	 */
	private TransformationService findProcessor(String type) {
		return processorCache.get(type.toUpperCase());
	}	
	
	
}
