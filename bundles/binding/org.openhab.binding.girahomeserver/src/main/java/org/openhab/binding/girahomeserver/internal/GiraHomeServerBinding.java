/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.girahomeserver.internal;

import static org.apache.commons.lang.StringUtils.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.girahomeserver.GiraHomeServerBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 *
 * @author Jochen Mattes
 * @since 1.9.0
 */
public class GiraHomeServerBinding extends AbstractActiveBinding<GiraHomeServerBindingProvider>
        implements ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(GiraHomeServerBinding.class);

    private static final int DEFAULT_PORT = 7003;
    private static final long DEFAULT_REFRESH_INTERVAL = 1000L;

    /**
     * The BundleContext. This is only valid when the bundle is ACTIVE. It is set in the activate()
     * method and must not be accessed anymore once the deactivate() method was called or before activate()
     * was called.
     */
    private BundleContext bundleContext;

    /** ip address of the girahomeserver */
    protected static String ip;

    /** port of the girahomeserver */
    protected static int port = DEFAULT_PORT;

    /** password of the girahomeserver */
    protected static String password;

    /**
     * the refresh interval which is used to poll values from the GiraHomeServer
     * server (optional, defaults to 60000ms)
     */
    private long refreshInterval = DEFAULT_REFRESH_INTERVAL;

    /**
     * HasMap of items -> type that have changed since the last
     * communication with the girahomeserver
     */
    private static volatile HashMap<String, Type> updatedParams = new HashMap<String, Type>();

    public GiraHomeServerBinding() {
    }

    /**
     * Called by the SCR to activate the component with its configuration read from CAS
     *
     * @param bundleContext BundleContext of the Bundle that defines this component
     * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void updated(Dictionary configuration) throws ConfigurationException {
        logger.warn("Girahomeserver updated");

        boolean properlyConfigured = true;

        long refreshInterval = DEFAULT_REFRESH_INTERVAL;
        int port = DEFAULT_PORT;
        String ip = null;
        String password = null;
        try {
            String refreshIntervalString = (String) configuration.get("refresh"); //$NON-NLS-1$
            refreshInterval = isNotBlank(refreshIntervalString) ? Long.parseLong(refreshIntervalString)
                    : DEFAULT_REFRESH_INTERVAL;

            ip = (String) configuration.get("server"); //$NON-NLS-1$
            properlyConfigured = properlyConfigured && isNotBlank(ip);

            String portString = (String) configuration.get("port"); //$NON-NLS-1$
            port = isNumeric(portString) ? Integer.parseInt(portString) : DEFAULT_PORT;

            password = (String) configuration.get("password");
            properlyConfigured = properlyConfigured && isNotBlank(password);
        } catch (NumberFormatException ex) {
            properlyConfigured = false;
        }

        if (properlyConfigured) {
            this.refreshInterval = refreshInterval;
            GiraHomeServerBinding.ip = ip;
            GiraHomeServerBinding.port = port;
            GiraHomeServerBinding.password = password;
        }
        setProperlyConfigured(properlyConfigured);

    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected String getName() {
        return "GiraHomeServer Refresh Service";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void execute() {
        if (!bindingsExist()) {
            logger.warn("There is no existing Girahomeserver binding configuration => refresh cycle aborted!");
            return;
        }

        // establish the connection
        GiraHomeServerConnector connector = new GiraHomeServerConnector(ip, port, password);

        try {
            connector.connect();

            // read all available values
            readValuesFromGirahomeserver(connector);

            // write the updates
            flushValuesToGirahomeserver(connector);

        } catch (UnknownHostException e) {
            logger.warn("the given hostname '{}' of the Gira Home Server is unknown", ip);

        } catch (IOException e) {
            logger.warn("couldn't establish network connection [host '{}']", ip);

        } finally {
            if (connector != null) {
                connector.disconnect();
            }
        }

    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        writeToGiraHomeServerBuffered(itemName, command);
    }

    /**
     * Get the updated values through the connector and update the local states
     *
     * @param connector
     * @throws IOException
     */
    protected void readValuesFromGirahomeserver(GiraHomeServerConnector connector) throws IOException {

        // get the values from the girahomeserver
        HashMap<String, String> giraValues = connector.getValues();

        // ignore empty updates
        if (giraValues.isEmpty()) {
            return;
        }

        // update the openhab items through the first provider
        for (Map.Entry<String, String> entry : giraValues.entrySet()) {

            // send update
            for (GiraHomeServerBindingProvider provider : providers) {
                for (String itemName : provider.getItemNames(entry.getKey())) {
                    // TODO: implement Strings
                    Type type = new DecimalType(entry.getValue());
                    eventPublisher.postCommand(itemName, (Command) type);
                }
            }
        }
    }

    /**
     * Flush the accumulated changes to the Girahomeserver
     *
     * @param connector
     * @throws IOException
     */
    protected void flushValuesToGirahomeserver(GiraHomeServerConnector connector) throws IOException {

        // copy the updatedParams -- updatedParams is volatile
        HashMap<String, Type> updatedParamsCopy = updatedParams;
        updatedParams = new HashMap<String, Type>();

        // process the updated params
        for (Map.Entry<String, Type> entry : updatedParamsCopy.entrySet()) {
            String itemName = entry.getKey();
            Type value = entry.getValue();

            GiraHomeServerBindingProvider provider = findFirstMatchingBindingProvider(itemName, value);
            String communicationObject = provider.getOutboundCommunicationObject(itemName, value);
            if (communicationObject != null && !communicationObject.isEmpty()) {
                connector.setParam(communicationObject, value);
            }
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void internalReceiveUpdate(String itemName, State newState) {
        writeToGiraHomeServerBuffered(itemName, newState);
    }

    /**
     * Update the item state in the output buffer
     *
     * @param itemName
     * @param value
     */
    protected void writeToGiraHomeServerBuffered(String itemName, Type value) {
        GiraHomeServerBinding.updatedParams.put(itemName, value);
    }

    /**
     * Find the first matching binding provider
     *
     * @param itemName
     * @param value
     * @return
     */
    private GiraHomeServerBindingProvider findFirstMatchingBindingProvider(String itemName, Type value) {
        GiraHomeServerBindingProvider firstMatchingProvider = null;
        for (GiraHomeServerBindingProvider provider : this.providers) {
            String communicationObject = provider.getOutboundCommunicationObject(itemName, value);
            if (communicationObject != null) {
                firstMatchingProvider = provider;
                break;
            }
        }
        return firstMatchingProvider;
    }
}
