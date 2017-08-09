/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.pushbullet.internal;

import java.util.Dictionary;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.openhab.action.pushbullet.internal.PushbulletConstants.ACCESS_TOKEN_KEY;
import static org.openhab.action.pushbullet.internal.PushbulletConstants.BOTS_KEY;
import static org.openhab.action.pushbullet.internal.PushbulletConstants.DEFAULT_BOTNAME;

/**
 * This class registers an OSGi service for the Pushbullet action.
 *
 * @author Hakan Tandogan
 * @since 1.11.0
 */
public class PushbulletActionService implements ActionService, ManagedService {

    private final Logger logger = LoggerFactory.getLogger(PushbulletActionService.class);

    /**
     * Indicates whether this action is properly configured which means all
     * necessary configurations are set. This flag can be checked by the action
     * methods before executing code.
     */
    static boolean isProperlyConfigured = false;

    public PushbulletActionService() {
        // nothing to do
    }

    public void activate() {
        logger.debug("PushbulletAPIConnector action service activated");
    }

    public void deactivate() {
        logger.debug("PushbulletAPIConnector action service deactivated");
    }

    @Override
    public String getActionClassName() {
        return PushbulletAPIConnector.class.getCanonicalName();
    }

    @Override
    public Class<?> getActionClass() {
        return PushbulletAPIConnector.class;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {
            String defaultAccessToken = Objects.toString(config.get(ACCESS_TOKEN_KEY), null);

            String botNames = Objects.toString(config.get(BOTS_KEY), null);
            if (StringUtils.isNotEmpty(botNames)) {
                String[] bots = botNames.split(",");
                for (String botName : bots) {
                    botName = botName.trim();

                    String accessTokenKey = String.format("%s.%s", botName, ACCESS_TOKEN_KEY);
                    String accessToken = Objects.toString(config.get(accessTokenKey), defaultAccessToken);

                    PushbulletAPIConnector.addBot(botName, accessToken);
                }
            }

            // Now, try to configure the default bot just in case we have older configuration around
            PushbulletAPIConnector.addBot(DEFAULT_BOTNAME, defaultAccessToken);

            PushbulletAPIConnector.logPushbulletBots();

            if (PushbulletAPIConnector.botCount() > 0) {
                isProperlyConfigured = true;
            }
        }

        logger.debug("Parsing configuration done, configuration is {}", isProperlyConfigured);
    }

}
