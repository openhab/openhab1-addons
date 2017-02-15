/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.ciscospark.internal;

import static org.apache.commons.lang.StringUtils.*;

import java.net.URI;
import java.util.Dictionary;
import java.util.Objects;

import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ciscospark.Person;
import com.ciscospark.Spark;
import com.ciscospark.SparkException;

/**
 * This class registers an OSGi service for the Cisco Spark action.
 *
 * @author Tom Deckers
 * @since 1.10.0
 */
public class CiscoSparkActionService implements ActionService, ManagedService {

    private final Logger logger = LoggerFactory.getLogger(CiscoSparkActionService.class);

    /**
     * Indicates whether this action is properly configured which means all
     * necessary configurations are set. This flag can be checked by the
     * action methods before executing code.
     */
    /* default */ static boolean isProperlyConfigured = false;

    /** the configured AccessToken (required!) */
    static String accessToken;

    /** default room (optional) */
    static String defaultRoomId;

    public CiscoSparkActionService() {
    }

    public void activate(ComponentContext componentContext) {
    }

    public void deactivate(ComponentContext componentContext) {
        // deallocate resources here that are no longer needed and
        // should be reset when activating this binding again
    }

    @Override
    public String getActionClassName() {
        return CiscoSpark.class.getCanonicalName();
    }

    @Override
    public Class<?> getActionClass() {
        return CiscoSpark.class;
    }

    /**
     * Creates and returns a Twitter4J Twitter client.
     *
     * @return a new instance of a Twitter4J Twitter client.
     */
    private Spark createSpark() {
        // Initialize the client
        Spark spark = Spark.builder().baseUrl(URI.create("https://api.ciscospark.com/v1")).accessToken(accessToken)
                .build();
        return spark;
    }

    private void start() {
        if (!isProperlyConfigured) {
            return;
        }

        try {
            logger.debug("Creating Cisco Spark client...");
            CiscoSpark.spark = createSpark();
            logger.debug("Retrieving user...");
            Person person = CiscoSpark.spark.people().path("/me").get();
            logger.info("Cisco Spark logged in as {}", person.getDisplayName());
        } catch (SparkException se) {
            logger.warn("Failed to initialized Cisco Spark", se);
        } catch (Exception e) {
            logger.warn("Failed to initialized Cisco Spark!", e);
        }
        logger.info("Cisco Spark has been successfully started");
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {

            String accessTokenString = Objects.toString(config.get("accessToken"));
            if (isNotBlank(accessTokenString)) {
                accessToken = accessTokenString;
            }

            if (isBlank(accessToken)) {
                throw new ConfigurationException("ciscospark",
                        "The parameter 'accessToken' is missing! Please refer to your config file.");
            }

            String defaultRoomIdString = Objects.toString(config.get("defaultRoomId"));
            if (isNotBlank(defaultRoomIdString)) {
                defaultRoomId = defaultRoomIdString;
            }

            isProperlyConfigured = true;
            start();
        }
    }

}
