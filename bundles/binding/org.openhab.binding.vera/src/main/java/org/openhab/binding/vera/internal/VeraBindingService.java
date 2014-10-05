/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.vera.VeraBindingConfig;
import org.openhab.binding.vera.VeraBindingConfigProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main Vera binding service responsible for managing
 * all the {@link VeraBinding}s configured.
 * 
 * @author Matthew Bowman
 * @since 1.6.0
 */
public class VeraBindingService extends AbstractActiveBinding<VeraBindingConfigProvider> implements ManagedService {
    
    private static final Logger logger = LoggerFactory.getLogger(VeraBindingService.class);
    
    /**
     * Unit config pattern used in the <code>openhab.cfg</code> file.
     */
    private static final Pattern UNIT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(host|webPort|upnpPort|timeout)$");
    
    // unit config defaults
    private static final int  DEFAULT_WEB_PORT         = 3480;
    private static final int  DEFAULT_UPNP_PORT        = 49451;
    private static final int  DEFAULT_VERA_TIMEOUT     = 60;            // s
    private static final long DEFAULT_REFRESH_INTERVAL = 60 * 1000;     // ms
    
    /** 
     * the refresh interval which is used to poll values from the Vera
     * server (optional, defaults to 60000ms)
     */
    private long refreshInterval = DEFAULT_REFRESH_INTERVAL;

    /**
     * Lookup table of {@link VeraUnitConfig}s indexed by unit ids.
     */
    private Map<String, VeraUnitConfig> unitConfigs = new HashMap<String, VeraUnitConfig>();
    
    /**
     * Lookup table of {@link VeraBinding}s index by item names.
     */
    private Map<String, VeraBinding> bindings = new HashMap<String, VeraBinding>();

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getName() {
        return "Vera Refresh Service";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute() {
        logger.trace("execute()");
        for (VeraBinding binding: bindings.values()) {
            binding.touch();
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        logger.trace("bindingChanged(provider = {}, item = {}", provider, itemName);
        super.bindingChanged(provider, itemName);
        if (!isProperlyConfigured()) {
        	//logger.warn("binding changed but not yet properly configured - ignoring!");
        	return;
        }
        if (provider.providesBindingFor(itemName)) {
            bindingAdded(provider, itemName);
        } else {
            bindingRemoved(provider, itemName);
        }
    }
    
    /**
     * Internal helper called whenever a binding is added.
     * @param provider the binding provider of the binding
     * @param itemName the item name of the binding added
     */
    private void bindingAdded(BindingProvider provider, String itemName) {
        logger.trace("bindingAdded(provider = {}, item = {}", provider, itemName);
        
        if (bindings.containsKey(itemName)) {
        	logger.warn("[{}] binding already exists!");
        	return;
        }
        
        if (provider instanceof VeraBindingConfigProvider) {
            VeraBindingConfigProvider bindingProvider = (VeraBindingConfigProvider) provider;
            VeraBindingConfig bindingConfig = bindingProvider.getBindingConfig(itemName);
            VeraUnitConfig unitConfig = unitConfigs.get(bindingConfig.getUnitId());
            if (unitConfig == null) {
                logger.warn("[{}] unknown unitId = '{}'", itemName, bindingConfig.getUnitId());
                return;
            }
            VeraBinding device = new VeraBinding(bindingConfig, unitConfig.unit, eventPublisher);
            device.bind();
            bindings.put(itemName, device);
            logger.debug("[{}] bound -> {}", itemName, device);
        }
    }
    
    /**
     * Internal helper called whenever a binding is removed.
     * @param provider the binding provider of the binding
     * @param itemName the item name of the binding removed
     */
    private void bindingRemoved(BindingProvider provider, String itemName) {
        logger.trace("bindingRemoved(provider = {}, item = {}", provider, itemName);
        if (bindings.containsKey(itemName)) {
            VeraBinding remove = bindings.remove(itemName);
            if (remove != null) {
                remove.unbind();
            }
            logger.debug("[{}] unbound -> {}", itemName, remove);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {

        if (config == null) {
            return;
        }
        
        if (logger.isTraceEnabled()) {
            logger.trace("updated(...)");
            Enumeration<String> keys = config.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                logger.trace(" {} = {}", key, config.get(key));
            }
        }
        
        disconnectAllBindings();
        disconnectAllUnits();
        
        Enumeration<?> keys = config.keys();
        while (keys.hasMoreElements()) {
            
            String key = (String) keys.nextElement();
            
            // the config-key enumeration contains additional keys that we
            // don't want to process here ...
            if ("service.pid".equals(key)) {
                continue;
            }

            // to override the default refresh interval one has to add a 
            // parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
            if ("refresh".equals(key)) {
                String refresh = (String) config.get(key);
                refreshInterval = StringUtils.isNotBlank(refresh) ? Long.parseLong(refresh) : DEFAULT_REFRESH_INTERVAL;
            }
            
            Matcher matcher = UNIT_CONFIG_PATTERN.matcher(key);
            if (!matcher.matches()) {
                logger.warn("invalid config '{}' does not match {}", key, UNIT_CONFIG_PATTERN);
                continue;
            }
            
            matcher.reset();
            matcher.find();
            
            String unitId = matcher.group(1);
            String configKey = matcher.group(2);
            String configValue = (String) config.get(key);

            VeraUnitConfig unitConfig = unitConfigs.get(unitId);
            if (unitConfig == null) {
                unitConfig = new VeraUnitConfig();
                unitConfig.unitId = unitId;
                unitConfigs.put(unitId, unitConfig);
            }
            
            if ("host".equals(configKey)) {
                unitConfig.host = configValue;
            }
            else if ("webPort".equals(configKey)) {
                unitConfig.webPort = Integer.valueOf(configValue);
            }
            else if ("upnpPort".equals(configKey)) {
                unitConfig.upnpPort = Integer.valueOf(configValue);
            }
            else if ("timeout".equals(configKey)) {
                unitConfig.timeout = Integer.valueOf(configValue);
            }
            
        }
        
        connectAllUnits();
        connectAllBindings();
        
        setProperlyConfigured(true);
    }
    
    /**
     * Connect all {@link VeraUnit}s.
     */
    private void connectAllUnits() {
        List<String> invalidUnitIds = new ArrayList<String>();
        for (String unitId: unitConfigs.keySet()) {
            VeraUnitConfig config = unitConfigs.get(unitId);
            // host is required
            if (StringUtils.isEmpty(config.host)) {
                invalidUnitIds.add(unitId);
                logger.warn("[{}] invalid config = {} (host is required)", unitId, config);
                continue;
            }
            logger.debug("[{}] valid config = {}", unitId, config);
            try {
                config.unit = new VeraUnit(config.host, config.webPort, config.upnpPort, 60);
            } catch (IOException e) {
                logger.warn(String.format("[%s] could not connect to unit, config = %s", unitId, config), e);
                invalidUnitIds.add(unitId);
            }
        }
        for (String invalidUnitId: invalidUnitIds) {
            unitConfigs.remove(invalidUnitId);
        }
    }
    
    private void connectAllBindings() {
    	for (VeraBindingConfigProvider provider: providers) {
    		for (String itemName: provider.getItemNames()) {
    			bindingAdded(provider, itemName);
    		}
    	}
    }
    
    /**
     * Disconnect all {@link VeraUnit}s.
     */
    private void disconnectAllUnits() {
        for (VeraUnitConfig unitConfig: unitConfigs.values()) {
            if (unitConfig.unit != null) {
                unitConfig.unit = null;
            }
        }
        unitConfigs.clear();
    }
    
    /**
     * Disconnect all {@link VeraBinding}s.
     */
    private void disconnectAllBindings() {
        for (VeraBinding binding: bindings.values()) {
            binding.unbind();
        }
        bindings.clear();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        logger.trace("internalReceiveCommand(itemName = {}, command = {})", itemName, command);
        VeraBinding item = bindings.get(itemName);
        item.receiveCommand(command);
    }
    
    /**
     * Represents a {@link VeraUnit} config read from the <code>openhab.cfg</code> file.
     *
     * @author Matthew Bowman
     * @since 1.6.0
     */
    private class VeraUnitConfig {

        /**
         * The unit id/
         */
        String unitId;
        
        /**
         * The unit host name.
         */
        String host;
        
        /**
         * The unit web port.
         */
        int webPort = DEFAULT_WEB_PORT;
        
        /**
         * The unit upnp port.
         */
        int upnpPort = DEFAULT_UPNP_PORT;
        
        /**
         * The unit timeout.
         */
        int timeout = DEFAULT_VERA_TIMEOUT;
        
        /**
         * The unit.
         */
        VeraUnit unit;

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return String.format("{ unit = %s, host = %s, webPort = %d, upnpPort = %d, timeout = %d }", unitId, host, webPort, upnpPort, timeout);
        }
        
    }
    
}
