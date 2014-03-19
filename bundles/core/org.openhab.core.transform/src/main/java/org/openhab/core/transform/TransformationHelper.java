/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
