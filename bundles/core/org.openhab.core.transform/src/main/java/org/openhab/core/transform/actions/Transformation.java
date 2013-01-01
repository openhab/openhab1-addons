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
