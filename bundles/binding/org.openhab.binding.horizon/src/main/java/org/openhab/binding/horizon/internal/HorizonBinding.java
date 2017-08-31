/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.horizon.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.ConfigurationException;

import org.openhab.binding.horizon.HorizonBindingProvider;
import org.openhab.binding.horizon.internal.control.HorizonBox;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.types.Command;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 *
 * @author Jurgen Kuijpers
 * @since 1.9.0
 */
public class HorizonBinding extends AbstractBinding<HorizonBindingProvider> {

    private static final Logger logger = LoggerFactory.getLogger(HorizonBinding.class);

    private final static int DEFAULT_HORIZON_PORT = 5900;
    private static final Pattern EXTRACT_HORIZON_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(host)$");

    protected Map<String, HorizonBox> deviceConfigCache = new HashMap<String, HorizonBox>();

    public HorizonBinding() {
    }

    protected void addBindingProvider(HorizonBindingProvider provider) {
        super.addBindingProvider(provider);
    }

    protected void removeBindingProvider(HorizonBindingProvider provider) {
        super.removeBindingProvider(provider);
    }

    /**
     * Called by the SCR to activate the component with its configuration read
     * from CAS
     *
     * @param bundleContext
     *            BundleContext of the Bundle that defines this component
     * @param configuration
     *            Configuration properties for this component obtained from the
     *            ConfigAdmin service
     * @throws ConfigurationException
     */
    public void activate(final BundleContext bundleContext, final Map<String, Object> configuration)
            throws ConfigurationException {
        if (configuration != null) {
            updateConfiguration(configuration);
        }
    }

    /**
     * Called by the SCR when the configuration of a binding has been changed
     * through the ConfigAdmin service.
     *
     * @param configuration
     *            Updated configuration properties
     * @throws ConfigurationException
     */
    public void modified(final Map<String, Object> configuration) throws ConfigurationException {
        if (configuration != null) {
            updateConfiguration(configuration);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);
        HorizonBindingProvider horizonBindingProvider = findFirstMatchingBindingProvider(itemName, command);
        String horizonKeyWithHorizonId = horizonBindingProvider.getHorizonCommand(itemName, command.toString());
        String[] horizonKeyWithHorizonIdSplit = horizonKeyWithHorizonId.split(":");
        String horizonId = horizonKeyWithHorizonIdSplit[0];
        String horizonKey = horizonKeyWithHorizonIdSplit[1];
        HorizonBox horizonBox = deviceConfigCache.get(horizonId);
        if (horizonBox != null) {
            try {
                horizonBox.sendKey(Integer.valueOf(horizonKey));
            } catch (Exception e) {
                logger.error("Error sending key to device with device id '{}'", horizonId, e);
            }
        } else {
            logger.warn("Cannot find connection details for device id '{}'", horizonId);
        }
    }

    private void updateConfiguration(final Map<String, Object> configuration) throws ConfigurationException {
        Set<String> keys = configuration.keySet();
        Iterator<String> keysIt = keys.iterator();
        while (keysIt.hasNext()) {
            String key = keysIt.next();

            if ("service.pid".equals(key)) {
                continue;
            }

            Matcher matcher = EXTRACT_HORIZON_CONFIG_PATTERN.matcher(key);
            if (!matcher.matches()) {
                logger.debug("given config key '" + key + "' does not follow the expected pattern '<id>.<host>'");
                continue;
            }

            matcher.reset();
            matcher.find();

            String deviceId = matcher.group(1);
            String host = null;
            String configKey = matcher.group(2);
            String value = (String) configuration.get(key);
            if ("host".equals(configKey)) {
                host = value;
            } else {
                throw new ConfigurationException("The given configKey '" + configKey + "' is unknown");
            }
            HorizonBox horizonBox = new HorizonBox(host, DEFAULT_HORIZON_PORT);
            deviceConfigCache.put(deviceId, horizonBox);
        }
    }

    /**
     * Find the first matching {@link HorizonBindingProvider} according to
     * <code>itemName</code> and <code>command</code>. If no direct match is
     * found, a second match is issued with wilcard-command '*'.
     *
     * @param itemName
     * @param command
     *
     * @return the matching binding provider or <code>null</code> if no binding
     *         provider could be found
     */
    private HorizonBindingProvider findFirstMatchingBindingProvider(String itemName, Command command) {
        HorizonBindingProvider firstMatchingProvider = null;
        for (HorizonBindingProvider provider : this.providers) {
            String commandLine = provider.getHorizonCommand(itemName, command.toString());
            if (commandLine != null) {
                firstMatchingProvider = provider;
                break;
            }
        }
        return firstMatchingProvider;
    }
}
