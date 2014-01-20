/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.transform.actions;

import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.transform.internal.TransformationActivator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class holds static "action" methods that can be used from within rules to execute
 * transformations.
 * 
 * @author Kai Kreuzer
 * @since 0.8.0
 *
 */
public class Transformation {

	static private final Logger logger = LoggerFactory.getLogger(Transformation.class);
	
	/**
	 * Applies a transformation of a given type with some function to a value.
	 * 
	 * @param type the transformation type, e.g. REGEX or MAP
	 * @param function the function to call, this value depends on the transformation type
	 * @param value the value to apply the transformation to
	 * @return 
	 * 		the transformed value or the original one, if there was no service registered for the
	 * 		given type or a transformation exception occurred.
	 */
	public static String transform(String type, String function, String value) {
		String result;
		TransformationService service = TransformationHelper.getTransformationService(TransformationActivator.getContext(), type);
		if(service!=null) {
			try {
				result = service.transform(function, value);
			} catch (TransformationException e) {
				logger.error("Error executing the transformation '" + type + "': " + e.getMessage());
				result = value;
			}			
		} else {
			logger.warn("No transformation service '" + type + "' could be found.");
			result = value;
		}
		return result;
	}
	
}
