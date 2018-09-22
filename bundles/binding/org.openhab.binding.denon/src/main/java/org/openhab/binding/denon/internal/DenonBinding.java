/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.denon.DenonBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding that communicates with one or multiple Denon receivers.
 *
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class DenonBinding extends AbstractActiveBinding<DenonBindingProvider> implements ManagedService {

    private static final String CONFIG_REFRESH = "refresh";

    private static final String CONFIG_HOST = "host";

    private static final String CONFIG_UPDATE_TYPE = "update";

    private static final String CONFIG_UPDATE_TYPE_HTTP = "http";

    private static final String CONFIG_UPDATE_TYPE_TELNET = "telnet";

    private static final String CONFIG_SERVICE_PID = "service.pid";

    private static final String SWITCH_INPUT = "SI";

    private static final Logger logger = LoggerFactory.getLogger(DenonBinding.class);

    private Map<String, DenonConnectionProperties> connections = new HashMap<String, DenonConnectionProperties>();

    private int refreshInterval = 5000;

    private ItemRegistry itemRegistry;

    @Override
    public void deactivate() {
        for (Entry<String, DenonConnectionProperties> entry : connections.entrySet()) {
            entry.getValue().getConnector().disconnect();
        }
    }

    @Override
    protected void execute() {
        for (Entry<String, DenonConnectionProperties> entry : connections.entrySet()) {
            DenonConnectionProperties connection = entry.getValue();
            if (connection.isHttp()) {
                entry.getValue().getConnector().updateState();
            }
        }
    }

    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    @Override
    protected String getName() {
        return "Denon Refresh Service";
    }

    public void setItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }

    public void unsetItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = null;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        logger.debug("Denon binding changed for item {}", itemName);
        if (provider instanceof DenonBindingProvider) {
            DenonBindingConfig config = ((DenonBindingProvider) provider).getConfig(itemName);
            if (config != null) {
                DenonConnector connector = getConnector(config);
                if (connector != null) {
                    connector.updateStateFromCache(config.getProperty());
                } else {
                    logger.debug("No connector found for item {}", itemName);
                }
            } else {
                logger.debug("No config found for item {}", itemName);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void allBindingsChanged(BindingProvider provider) {
        logger.debug("Denon all bindings changed");
        updateInitialState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        DenonBindingConfig config = getConfig(itemName);

        if (config != null) {
            DenonConnector connector = getConnector(config);

            if (connector != null) {
                connector.sendCommand(config, command);
            } else {
                logger.debug("Cannot send command, no connector found for item {}", itemName);
            }
        } else {
            logger.debug("Cannot send command, no config found for item {}", itemName);
        }
    }

    protected void addBindingProvider(DenonBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(DenonBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        logger.debug("Denon binding updated");

        if (config == null) {
            return;
        }

        Enumeration<String> keys = config.keys();

        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (CONFIG_SERVICE_PID.equals(key)) {
                continue;
            }

            String[] parts = key.split("\\.");
            String value = Objects.toString(config.get(key), null);

            if (parts.length == 1) {
                String option = parts[0];
                if (CONFIG_REFRESH.equals(option)) {
                    refreshInterval = Integer.valueOf(value);
                }
            } else {
                String instance = parts[0];

                DenonConnectionProperties connection = connections.get(instance);
                if (connection == null) {
                    connection = new DenonConnectionProperties();
                    connection.setInstance(instance);
                    connections.put(instance, connection);
                }

                String option = parts[1].trim();

                if (CONFIG_HOST.equals(option)) {
                    connection.setHost(value);
                } else if (CONFIG_UPDATE_TYPE.equals(option)) {
                    connection.setTelnet(value.equals(CONFIG_UPDATE_TYPE_TELNET));
                    connection.setHttp(value.equals(CONFIG_UPDATE_TYPE_HTTP));

                    if (!value.equals(CONFIG_UPDATE_TYPE_TELNET) && !value.equals(CONFIG_UPDATE_TYPE_HTTP)) {
                        logger.warn("Invalid connection type {} for instance {}, using default", value, instance);
                    }
                }
            }
        }

        boolean isActiveBinding = false;

        for (Entry<String, DenonConnectionProperties> entry : connections.entrySet()) {
            DenonConnectionProperties connection = entry.getValue();

            if (!StringUtils.isBlank(connection.getHost())) {
                logger.debug("Denon receiver configured at {}", connection.getHost());
                DenonConnector connector = new DenonConnector(connection, new DenonPropertyUpdatedCallback() {
                    @Override
                    public void updated(String instance, String property, State state) {
                        processPropertyUpdated(instance, property, state);
                    }
                });
                connection.setConnector(connector);
                connector.connect();

                if (connection.isHttp()) {
                    isActiveBinding = true;
                }
            } else {
                logger.debug("No host configured for receiver {}", connection.getInstance());
            }
        }

        setProperlyConfigured(isActiveBinding);
    }

    private void updateInitialState() {
        for (Entry<String, DenonConnectionProperties> entry : connections.entrySet()) {
            entry.getValue().getConnector().getInitialState();
        }
    }

    private void processPropertyUpdated(String instance, String property, State state) {
        updateIfChanged(instance, property, state);

        if (property.startsWith(SWITCH_INPUT)) {
            updateInputProperties(instance, property);
        }
    }

    /**
     * Update all the different input properties (=properties that start with SI).
     * This way, only the currently selected input has state 'ON'.
     */
    private void updateInputProperties(String instance, String property) {
        for (DenonBindingProvider provider : providers) {
            for (String itemName : provider.getItemNames()) {
                DenonBindingConfig cfg = provider.getConfig(itemName);
                if (cfg.getInstance().equals(instance)) {
                    if (cfg.getProperty().startsWith(SWITCH_INPUT) && !cfg.getProperty().equals(property)) {
                        updateIfChanged(cfg.getInstance(), cfg.getProperty(), OnOffType.OFF);
                    }
                }
            }
        }
    }

    /**
     * Only update the property if newState is different than its current state.
     */
    private void updateIfChanged(String instance, String property, State newState) {
        DenonBindingProvider firstProvider = getFirstMatchingProvider(instance, property);
        if (firstProvider != null) {
            DenonBindingConfig config = firstProvider.getConfig(instance, property);
            try {
                State oldState = itemRegistry.getItem(config.getItemName()).getState();
                if (!oldState.equals(newState)) {
                    eventPublisher.postUpdate(config.getItemName(), newState);
                }
            } catch (ItemNotFoundException e) {
                logger.debug("Cannot find item " + config.getItemName() + " in the registry", e);
            }
        }
    }

    private DenonBindingProvider getFirstMatchingProvider(String instance, String property) {
        for (DenonBindingProvider provider : providers) {
            DenonBindingConfig config = provider.getConfig(instance, property);
            if (config != null) {
                return provider;
            }
        }
        return null;
    }

    private DenonBindingConfig getConfig(String itemName) {
        for (DenonBindingProvider provider : providers) {
            DenonBindingConfig config = provider.getConfig(itemName);
            if (config != null) {
                return config;
            }
        }

        return null;
    }

    private DenonConnector getConnector(DenonBindingConfig config) {
        DenonConnectionProperties connection = connections.get(config.getInstance());
        if (connection != null) {
            return connection.getConnector();
        }

        return null;
    }
}
