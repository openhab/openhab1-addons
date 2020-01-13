/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.core.transform;

import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Kai Kreuzer - Initial contribution
 */
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
        return null;
    }

}
