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
package org.openhab.core.transform;

import java.util.Collection;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformationHelper {

	private static Logger logger = LoggerFactory.getLogger(TransformationHelper.class);

	/**
	 * Queries the OSGi service registry for a service that provides a transformation service of
	 * a given transformation type (e.g. REGEX, XSLT, etc.)
	 * 
	 * @param transformationType the desired transformation type
	 * @return a service instance or null, if none could be found
	 */
	static public TransformationService getTransformationService(BundleContext context, String transformationType) {
		if(context!=null) {
			String filter = "(openhab.transform=" + transformationType + ")";
			try {
				Collection<ServiceReference<TransformationService>> refs = context.getServiceReferences(TransformationService.class, filter);
				if(refs!=null && refs.size() > 0) {
					return (TransformationService) context.getService(refs.iterator().next());
				} else {
					logger.warn("Cannot get service reference for transformation service of type " + transformationType);
				}
			} catch (InvalidSyntaxException e) {
				logger.warn("Cannot get service reference for transformation service of type " + transformationType, e);
			}
		}
		return null;
	}

}
