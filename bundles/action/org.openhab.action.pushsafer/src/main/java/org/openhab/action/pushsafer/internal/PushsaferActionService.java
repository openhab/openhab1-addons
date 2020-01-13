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
package org.openhab.action.pushsafer.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class registers an OSGi service for the Pushsafer action. Pushsafer is
 * a web based service that allows pushing of messages to mobile devices.
 *
 * @author Chris Graham
 * @since 1.9.0
 */
public class PushsaferActionService implements ActionService, ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(PushsaferActionService.class);

    /**
     * Indicates whether this action is properly configured which means all
     * necessary configurations are set. This flag can be checked by the action
     * methods before executing code.
     */
    static boolean isProperlyConfigured = false;

    public PushsaferActionService() {
        // nothing to do
    }

    public void activate() {
        logger.debug("Pushsafer action service activated");
    }

    public void deactivate() {
        logger.debug("Pushsafer action service deactivated");
    }

    @Override
    public String getActionClassName() {
        return Pushsafer.class.getCanonicalName();
    }

    @Override
    public Class<?> getActionClass() {
        return Pushsafer.class;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        logger.debug("Configuration file is being parsed but ignored.");
        if (config != null) {
            logger.debug("Configuration data exists but ignored.");
        } else {
            // Messages can be sent by providing API Key and User key in the action binding, so no issue here.
            logger.debug("The configurations information was empty. No defaults for Pushsafer loaded.");
        }

        isProperlyConfigured = true;
    }

}
