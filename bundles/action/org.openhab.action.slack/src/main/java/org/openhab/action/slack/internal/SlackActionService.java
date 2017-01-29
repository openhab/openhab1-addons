/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.slack.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

/**
 * This class registers an OSGi service for the Slack action.
 *
 * @author Rino van Wijngaarden
 *
 */
public class SlackActionService implements ActionService, ManagedService {

    /**
     * Indicates whether this action is properly configured which means all
     * necessary configurations are set. This flag can be checked by the
     * action methods before executing code.
     */
    /* default */ static boolean isProperlyConfigured = false;

    public SlackActionService() {
    }

    public void activate() {
    }

    public void deactivate() {
        // deallocate Resources here that are no longer needed and
        // should be reset when activating this binding again
    }

    @Override
    public String getActionClassName() {
        return Slack.class.getCanonicalName();
    }

    @Override
    public Class<?> getActionClass() {
        return Slack.class;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void updated(Dictionary config) throws ConfigurationException {
        if (config != null) {
            Slack.authToken = (String) config.get("authToken");

            // check mandatory settings
            if (StringUtils.isBlank(Slack.authToken)) {
                throw new ConfigurationException("slack",
                        "Parameter authToken is mandatory and must be configured. Please check your slack.cfg!");
            }
            isProperlyConfigured = true;
        }
    }

}
