/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.bticino.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.bticino.internal.BticinoGenericBindingProvider.BticinoBindingConfig;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements a binding of bticino devices to openHAB. The binding
 * configurations are provided by the {@link GenericItemProvider}.
 *
 * @author Tom De Vlaminck, Andrea Carabillo'
 * @serial 1.0
 * @since 1.7.0
 */
public class BticinoBinding extends AbstractBinding<BticinoBindingProvider> implements ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(BticinoBinding.class);

    /**
     * RegEx to validate a bticino gateway config
     * <code>'^(.*?)\\.(host|port)$'</code>
     */
    private static final Pattern EXTRACT_BTICINO_GATEWAY_CONFIG_PATTERN = Pattern
            .compile("^(.*?)\\.(host|port|passwd|rescan_secs)$");

    // indicates that the updated has been run once
    boolean m_binding_initialized = false;

    private EventPublisher eventPublisher = null;

    // (interfaceid, deviceconfig)
    private Map<String, BticinoConfig> m_bticino_devices_config = new HashMap<String, BticinoConfig>();
    // (interfaceid, device)
    private Map<String, BticinoDevice> m_bticino_devices = new HashMap<String, BticinoDevice>();

    static class BticinoConfig {
        String id;
        String host;
        // Default port is 20000 for a MH200
        int port = 20000;
        // Default OpenWebNet password
        String passwd = "12345";
        // Default rescan interval is 300 seconds
        int rescan_secs = 300;

        @Override
        public String toString() {
            return "Bticino [id=" + id + ", host=" + host + ", port=" + port + ", passwd=" + passwd + ", rescan secs="
                    + rescan_secs + "]";
        }
    }

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;

        for (BticinoDevice bticinodevice : m_bticino_devices.values()) {
            bticinodevice.setEventPublisher(eventPublisher);
        }
    }

    @Override
    public void unsetEventPublisher(EventPublisher eventPublisher) {
        this.eventPublisher = null;

        for (BticinoDevice bticinodevice : m_bticino_devices.values()) {
            bticinodevice.setEventPublisher(null);
        }
    }

    /**
     * For the given item, get the bticino binding configuration
     *
     * @param itemName
     * @return
     * @throws Exception
     */
    public BticinoBindingConfig getBticinoBindingConfigForItem(String itemName) {
        // Get the bticino binding config for the associated item
        BticinoBindingConfig l_item_binding = null;
        for (BticinoBindingProvider provider : providers) {
            if (provider.providesBindingFor(itemName)) {
                l_item_binding = provider.getConfig(itemName);
                break;
            }
        }
        // the item must have a config
        if (l_item_binding == null) {
            throw new RuntimeException("BindingConfig not found for item [" + itemName + "]");
        }
        return l_item_binding;
    }

    /**
     * Returns the BticinoBindingConfig(s) associated with the who / where
     * config
     *
     * @param who
     * @param where
     * @return
     */
    public List<BticinoBindingConfig> getItemForBticinoBindingConfig(String who, String where) {
        // Find all the bindings for the who-where combination (multiple
        // possible)
        List<BticinoBindingConfig> l_item_bindings = new LinkedList<BticinoBindingConfig>();

        // Get all the bticino providers
        for (BticinoBindingProvider provider : providers) {
            // Get all the items it provides binding for
            for (String l_item_name : provider.getItemNames()) {
                // Check if this config item provides binding for the given
                // who/where
                BticinoBindingConfig l_binding_config = provider.getConfig(l_item_name);

                if (l_binding_config.who.equals(who) && l_binding_config.where.equals(where)) {
                    // Add it to the list
                    l_item_bindings.add(l_binding_config);
                }
            }
        }
        return l_item_bindings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void internalReceiveCommand(String itemName, Command command) {
        super.internalReceiveCommand(itemName, command);
        // Get the bticino interface id from the itemconfig
        BticinoBindingConfig l_item_binding = getBticinoBindingConfigForItem(itemName);
        // Get the interface from the item config
        String l_interface_id = l_item_binding.gatewayID;
        // Get the bticino device from the map (if it exists)
        if (m_bticino_devices.containsKey(l_interface_id)) {
            BticinoDevice l_bticino_device = m_bticino_devices.get(l_interface_id);
            l_bticino_device.receiveCommand(itemName, command, l_item_binding);
        } else {
            // just to know that something is wrong
            logger.error("Item [" + itemName + "] uses Bticino interface with ID [" + l_interface_id
                    + "] but this gateway doesn't exist : check items.cfg and openhab.cfg!");
        }
    }

    protected void addBindingProvider(BticinoBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(BticinoBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * {@inheritDoc}
     */
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        if (!m_binding_initialized) {
            // remove all configs
            m_bticino_devices_config.clear();
            // remove all interfaces
            m_bticino_devices.clear();

            // We will read every configuration key, and encounter
            // hostname, port for the configured bticino gateways
            Enumeration<String> keys = properties.keys();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();

                // the config-key enumeration contains additional keys that we
                // don't want to process here ...

                if ("service.pid".equals(key)) {
                    continue;
                }

                Matcher matcher = EXTRACT_BTICINO_GATEWAY_CONFIG_PATTERN.matcher(key);

                if (!matcher.matches()) {
                    logger.debug("given bticino gateway-config-key '" + key
                            + "' does not follow the expected pattern '<gateway_name>.<host|port>'");
                    continue;
                }

                matcher.reset();
                matcher.find();

                // Get the interface id
                String l_gw_if_id = matcher.group(1);

                // Search the config, to update the values (row / row)
                BticinoConfig l_bticino_config = m_bticino_devices_config.get(l_gw_if_id);
                // Create a new config if it wasnt found now
                if (l_bticino_config == null) {
                    l_bticino_config = new BticinoConfig();
                    // set the id
                    l_bticino_config.id = l_gw_if_id;
                    // add (if_id, bticino config) entry
                    m_bticino_devices_config.put(l_gw_if_id, l_bticino_config);
                }

                String configKey = matcher.group(2);
                String value = Objects.toString(properties.get(key), null);

                // parameter host
                if ("host".equals(configKey)) {
                    l_bticino_config.host = value;
                }
                // parameter port
                else if ("port".equals(configKey)) {
                    l_bticino_config.port = Integer.valueOf(value);
                } else if ("passwd".equals(configKey)) {
                    l_bticino_config.passwd = value;
                } else if ("rescan_secs".equals(configKey)) {
                    l_bticino_config.rescan_secs = Integer.valueOf(value);
                } else {
                    throw new ConfigurationException(configKey,
                            "the given configKey '" + configKey + "' with value '" + value + "' is unknown");
                }
            }

            // Now for all the bticino gateways configured in the configuration,
            // connect to the physical gateways
            connectAllBticinoDevices();

            // Now start all the bticino gateways
            startAllBticinoDevices();

            // Indicate that this binding has been initialized
            m_binding_initialized = true;
        }
    }

    private void connectAllBticinoDevices() throws ConfigurationException {
        for (String l_gw_if_id : m_bticino_devices_config.keySet()) {
            BticinoConfig l_current_device_config = m_bticino_devices_config.get(l_gw_if_id);

            // Create a gw service object
            BticinoDevice l_bticino_device = new BticinoDevice(l_current_device_config.id, this);
            l_bticino_device.setEventPublisher(eventPublisher);
            l_bticino_device.setHost(l_current_device_config.host);
            l_bticino_device.setPort(l_current_device_config.port);
            l_bticino_device.setPasswd(l_current_device_config.passwd);
            l_bticino_device.setRescanInterval(l_current_device_config.rescan_secs);
            try {
                l_bticino_device.initialize();
            } catch (InitializationException e) {
                throw new ConfigurationException(l_gw_if_id, "Could not open create BTicino interface with ID ["
                        + l_gw_if_id + "], Exception : " + e.getMessage());
            } catch (Throwable e) {
                throw new ConfigurationException(l_gw_if_id, "Could not open create BTicino interface with ID ["
                        + l_gw_if_id + "], Exception : " + e.getMessage());
            }
            m_bticino_devices.put(l_gw_if_id, l_bticino_device);
        }
    }

    private void startAllBticinoDevices() throws ConfigurationException {
        for (String l_gw_if_id : m_bticino_devices.keySet()) {
            BticinoDevice l_bticino_device = m_bticino_devices.get(l_gw_if_id);
            l_bticino_device.startDevice();
        }
    }
}
