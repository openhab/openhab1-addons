/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powerdoglocalapi.internal;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.binding.powerdoglocalapi.PowerDogLocalApiBindingProvider;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redstone.xmlrpc.XmlRpcFault;
import redstone.xmlrpc.XmlRpcProxy;
import redstone.xmlrpc.XmlRpcStruct;

/**
 * Queries eco-data PowerDog
 * 
 * @author wuellueb
 * @since 1.9.0
 */
public class PowerDogLocalApiBinding extends AbstractActiveBinding<PowerDogLocalApiBindingProvider> {

    private static final Logger logger = LoggerFactory.getLogger(PowerDogLocalApiBinding.class);

    /**
     * The BundleContext. This is only valid when the bundle is ACTIVE. It is
     * set in the activate() method and must not be accessed anymore once the
     * deactivate() method was called or before activate() was called.
     */
    private BundleContext bundleContext;

    /**
     * the refresh interval which is used as minimum sample time to poll values
     * from the PowerDogLocalApi for all servers (optional, defaults to
     * 300000ms)
     */
    private long refreshInterval = 300000;

    /**
     * RegEx to validate a config <code>'^(.*?)\\.(host|port)$'</code>
     */
    private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(.*?)$");

    /**
     * Mapping of items to lastUpdate
     */
    private Map<String, Long> lastUpdateMap = new HashMap<String, Long>();

    /**
     * Mapping from serverId to PowerDog configuration structure
     */
    private Map<String, PowerDogLocalApiServerConfig> serverList = new HashMap<String, PowerDogLocalApiServerConfig>();

    public PowerDogLocalApiBinding() {
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
     */
    public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
        this.bundleContext = bundleContext;

        logger.debug("activate() method is called!");

        // the configuration is guaranteed not to be null, because the component
        // definition has the configuration-policy set to require. If set to
        // 'optional' then the configuration may be null
        this.bundleContext = bundleContext;

        // to override the default refresh interval for all powerdogs one has to
        // add a parameter to openhab.cfg like
        // <bindingName>:refresh=<intervalInMs>
        String refreshIntervalString = (String) configuration.get("refresh");
        if (StringUtils.isNotBlank(refreshIntervalString)) {
            refreshInterval = Long.parseLong(refreshIntervalString);
        }

        parseConfiguration(configuration);

        setProperlyConfigured(true);
    }

    /**
     * Called by the SCR when the configuration of a binding has been changed
     * through the ConfigAdmin service.
     * 
     * @param configuration
     *            Updated configuration properties
     */
    public void modified(final Map<String, Object> configuration) {
        // update the internal configuration accordingly
        logger.debug("modified() method is called!");

        parseConfiguration(configuration);

        setProperlyConfigured(true);
    }

    /**
     * Called by the SCR to deactivate the component when either the
     * configuration is removed or mandatory references are no longer satisfied
     * or the component has simply been stopped.
     * 
     * @param reason
     *            Reason code for the deactivation:<br>
     *            <ul>
     *            <li>0 Unspecified
     *            <li>1 The component was disabled
     *            <li>2 A reference became unsatisfied
     *            <li>3 A configuration was changed
     *            <li>4 A configuration was deleted
     *            <li>5 The component was disposed
     *            <li>6 The bundle was stopped
     *            </ul>
     */
    public void deactivate(final int reason) {
        this.bundleContext = null;
    }

    /**
     * @{inheritDoc
     */
    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    /**
     * @{inheritDoc
     */
    @Override
    protected String getName() {
        return "PowerDogLocalApi Refresh Service";
    }

    /**
     * @{inheritDoc
     */
    @Override
    protected void execute() {
        logger.debug("execute() method is called!");

        // cycle over all available powerdogs
        for (PowerDogLocalApiBindingProvider provider : providers) {
            // cycle over all available in-bindings
            /*
             * TODO: check if reading of out-bindings is useful - currently it
             * seems to be applicable only for openhab init (but it works
             * without, so I give it a try) and in case several clients are
             * writing to the same PowerAPI, which is risky due to concurrency
             */
            for (String itemName : provider.getInBindingItemNames()) {
                // get item specific refresh interval
                int refreshInterval = provider.getRefreshInterval(itemName);

                // check if item needs update
                Long lastUpdateTimeStamp = lastUpdateMap.get(itemName);
                if (lastUpdateTimeStamp == null) {
                    lastUpdateTimeStamp = 0L;
                }
                long age = System.currentTimeMillis() - lastUpdateTimeStamp;
                boolean itemNeedsUpdate = (age >= refreshInterval);

                if (itemNeedsUpdate) {
                    logger.debug("Item '{}' is about to be refreshed now", itemName);

                    // Get the unit serverId from the binding, and relate that
                    // to the config
                    String unit = provider.getServerId(itemName);
                    PowerDogLocalApiServerConfig server = serverList.get(unit);

                    // get the server specific update time and check if it needs
                    // an update
                    boolean serverNeedsUpdate = false;
                    if (server == null) {
                        serverNeedsUpdate = false;
                        logger.error("Unknown PowerDog server referenced: {}", unit);
                        continue;
                    } else {
                        age = System.currentTimeMillis() - server.lastUpdate;
                        serverNeedsUpdate = (age >= server.refresh);
                    }

                    XmlRpcStruct response = loadPowerDogResponse(serverNeedsUpdate, server);

                    // update item state
                    if (response != null) {
                        String value = getVariable(response, provider.getValueId(itemName), provider.getName(itemName));
                        if (value != null) {
                            Class<? extends Item> itemType = provider.getItemType(itemName);
                            State state = createState(itemType, value);
                            eventPublisher.postUpdate(itemName, state);
                            lastUpdateMap.put(itemName, System.currentTimeMillis());
                        }
                    }
                }
            }
        }

        logger.debug("execute() method is finished!");
    }

    private XmlRpcStruct loadPowerDogResponse(boolean needsUpdate, PowerDogLocalApiServerConfig server) {
        // Get all current linear values from the powerdog in case of an update
        XmlRpcStruct response = null;
        if (needsUpdate == true) {
            try {
                logger.debug("PowerDogLocalApi querying PowerDog");

                // perform XML RPC call and store response
                PowerDog powerdog = (PowerDog) XmlRpcProxy.createProxy(server.url(), "", new Class[] { PowerDog.class },
                        false);
                response = powerdog.getAllCurrentLinearValues(server.password);
                server.cache = response;
                server.lastUpdate = System.currentTimeMillis();

                logger.debug("PowerDog.getAllCurrentLinearValues() result: {}", response.toString());
            } catch (Exception e) {
                logger.warn("PowerDogLocalApi querying PowerDog failed");
                logger.warn(e.getMessage());
            }
        } else {
            logger.debug("Using PowerDogLocalApi cache");
            response = server.cache;
        }
        return response;
    }

    /**
     * Parse PowerDog xmlrpc response to getAllCurrentLinearValues
     * 
     * @param response
     *            PowerDog Response
     * @param valueId
     *            Value ID of PowerDog Item
     * @param name
     *            Parameter name to be updated
     * @return
     */
    private String getVariable(XmlRpcStruct response, String valueId, String name) {
        try {
            XmlRpcStruct reply = response.getStruct("Reply");
            XmlRpcStruct item = reply.getStruct(valueId);
            String value = item.getString(name);
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @{inheritDoc
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        logger.debug("internalReceiveCommand({},{}) is called!", itemName, command);

        State newState = null;

        // cast Interfaces
        if (command instanceof OnOffType) {
            newState = (OnOffType) command;
        } else if (command instanceof OpenClosedType) {
            newState = (OpenClosedType) command;
        } else if (command instanceof PercentType) {
            newState = (PercentType) command;
        } else if (command instanceof DecimalType) {
            newState = (DecimalType) command;
        }

        if (newState != null) {
            eventPublisher.postUpdate(itemName, newState);
        }
    }

    /**
     * @{inheritDoc
     */
    @Override
    protected void internalReceiveUpdate(String itemName, State newState) {
        logger.debug("internalReceiveUpdate({},{}) is called!", itemName, newState);

        // cycle on all available powerdogs
        for (PowerDogLocalApiBindingProvider provider : providers) {
            if (!provider.providesBindingFor(itemName)) {
                continue;
            }

            // only in case of an outbinding, this need to be handled
            if (provider.getOutBindingItemNames().contains(itemName)) {
                // check if item may send update already now
                // time indicated in config is the minimum time between two
                // updates
                Long lastUpdateTimeStamp = lastUpdateMap.get(itemName);
                if (lastUpdateTimeStamp == null) {
                    lastUpdateTimeStamp = 0L;
                }
                long age = System.currentTimeMillis() - lastUpdateTimeStamp;
                boolean itemMayUpdate = (age >= provider.getRefreshInterval(itemName));

                if (itemMayUpdate) {
                    // Convert new State to PowerDog set Current_Value string
                    String value = "0";
                    if (newState instanceof OnOffType) {
                        if (newState == OnOffType.ON) {
                            value = "1";
                        }
                    } // C-like Not-Zero is True, Zero is false; Powerdog does
                      // not offer boolean for PowerAPI, so this might not be
                      // the best solution, but it is sufficient
                    else if (newState instanceof OpenClosedType) {
                        if (newState == OpenClosedType.OPEN) {
                            value = "1";
                        }
                    } // see comment above
                    else if (newState instanceof PercentType) {
                        value = newState.toString();
                    } else if (newState instanceof DecimalType) {
                        value = newState.toString();
                    }

                    // Get the unit serverId from the binding, and relate that
                    // to the config
                    String unit = provider.getServerId(itemName);
                    PowerDogLocalApiServerConfig server = serverList.get(unit);

                    try {
                        logger.debug("PowerDogLocalApi sending to PowerDog");

                        // Perform XML RPC call
                        PowerDog powerdog = (PowerDog) XmlRpcProxy.createProxy(server.url(), "",
                                new Class[] { PowerDog.class }, false);
                        XmlRpcStruct response = powerdog.setLinearSensorDevice(server.password,
                                provider.getValueId(itemName), value);

                        lastUpdateMap.put(itemName, System.currentTimeMillis());

                        logger.debug("PowerDog.setLinearSensorDevice() result: {}", response.toString());
                    } catch (Exception e) {
                        logger.warn("PowerDogLocalApi sending to PowerDog failed");
                        logger.warn(e.getMessage());
                    }
                }
            }
        }

    }

    /**
     * Returns a {@link State} which is inherited from the {@link Item}s
     * accepted DataTypes. The call is delegated to the {@link TypeParser}. If
     * <code>item</code> is <code>null</code> the {@link StringType} is used.
     * 
     * PowerDog supports in the PowerAPI the following types for In-Bindings,
     * which should be mapped to the following items: V, A, �C, W, l, m/s, km/h
     * --> Number, String % --> Number, Switch, Dimmer, Contact* (in case of
     * Switch, 100% will be mapped to ON; in case of Contact, 100% is mapped to
     * OPEN) String (from PowerDog API output) --> String
     * 
     * @param itemType
     * @param transformedResponse
     * 
     * @return a {@link State} which type is inherited by the {@link TypeParser}
     *         or a {@link StringType} if <code>item</code> is <code>null</code>
     */
    private State createState(Class<? extends Item> itemType, String transformedResponse) {
        try {
            // Assign according to output type or cast to output type directly
            if (itemType.isAssignableFrom(SwitchItem.class)) {
                int value = Math.round(Float.parseFloat(transformedResponse));
                if (value > 0) {
                    return OnOffType.ON;
                } else {
                    return OnOffType.OFF;
                }
            } else if (itemType.isAssignableFrom(DimmerItem.class)) {
                return new PercentType(Math.round(Float.parseFloat(transformedResponse)));
            } else if (itemType.isAssignableFrom(ContactItem.class)) {
                int value = Math.round(Float.parseFloat(transformedResponse));
                if (value > 0) {
                    return OpenClosedType.OPEN;
                } else {
                    return OpenClosedType.CLOSED;
                }
            } else if (itemType.isAssignableFrom(NumberItem.class)) {
                return DecimalType.valueOf(transformedResponse);
            } else {
                return StringType.valueOf(transformedResponse);
            }
        } catch (Exception e) {
            logger.debug("Couldn't create state of type '{}' for value '{}'", itemType, transformedResponse);
            return StringType.valueOf(transformedResponse);
        }
    }

    /**
     * Parse PowerDog Openhab configuration
     * 
     * @param config
     *            PowerDog configuration string
     */
    private void parseConfiguration(Map<String, Object> config) {
        logger.debug("PowerDogLocalApi:parseConfiguration() method is called!");
        if (config != null) {
            Set<String> keyset = config.keySet();

            // create server list of not yet available
            if (serverList == null) {
                serverList = new HashMap<String, PowerDogLocalApiServerConfig>();
            }

            // check keys of config set
            for (Iterator<String>keys = keyset.iterator(); keys.hasNext();) {
                String key = keys.next();

                // the config-key enumeration contains additional keys that we
                // don't want to process here ...
                if ("service.pid".equals(key)) {
                    continue;
                } else if ("event.topics".equals(key)) {
                    continue;
                } else if ("component.name".equals(key)) {
                    continue;
                } else if ("component.id".equals(key)) {
                    continue;
                } else if ("objectClass".equals(key)) {
                    continue;
                }

                // check if key matches powerdog-regex
                Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);
                if (!matcher.matches()) {
                    continue;
                }
                matcher.reset();
                matcher.find();

                // get serverId as first item
                String serverId = matcher.group(1);

                // create config item for this specific powerdog unit
                PowerDogLocalApiServerConfig deviceConfig = serverList.get(serverId);
                if (deviceConfig == null) {
                    deviceConfig = new PowerDogLocalApiServerConfig();
                    serverList.put(serverId, deviceConfig);
                }

                // extract values for host, port, password or refresh
                String configKey = matcher.group(2);
                String value = (String) config.get(key);

                if ("host".equals(configKey)) {
                    deviceConfig.host = value;
                    logger.debug("value: {}", value);
                } else if ("port".equals(configKey)) {
                    if (StringUtils.isNotBlank(value)) {
                        deviceConfig.port = (int) Long.parseLong(value);
                        logger.debug("value: {}", value);
                    }
                } else if ("password".equals(configKey)) {
                    deviceConfig.password = value;
                } else if ("refresh".equals(configKey)) {
                    if (StringUtils.isNotBlank(value)) {
                        // refresh cannot be lower than refresh interval
                        deviceConfig.refresh = (int) Math.max(Long.parseLong(value), refreshInterval);
                        logger.debug("value: {}", value);
                    }
                } else {
                    // cannot throw new ConfigurationException(configKey,
                    // "The given PowerDogLocalApi configKey '" + configKey +
                    // "' is unknown");
                    logger.warn("The given PowerDogLocalApi configKey '{}' is unknown", configKey);
                }
                logger.debug("New Server config: {}", deviceConfig.toString());
            }

            setProperlyConfigured(true);
            logger.debug("PowerDogLocalApi:parseConfiguration() method is terminated");
        }
    }

    static class PowerDogLocalApiServerConfig {
        public String host; // IP adress or DNS entry
        public int port; // port number
        public String password; // password
        public int refresh; // refresh rate in ms
        public Long lastUpdate; // saves last update time when xmlrpc was read
        public XmlRpcStruct cache;

        PowerDogLocalApiServerConfig() {
            lastUpdate = (long) 0;

            // set defaults
            refresh = 300000; // 5 min is default
            password = ""; // empty password will normally not be accepted by
                           // PowerDog, needs to be configured
            port = 20000; // port 20000 is default for PowerDog
            host = "powerdog"; // local DNS in router might resolve this one
        }

        @Override
        public String toString() {
            String displayPassword = "[not set]";
            if (StringUtils.isNotBlank(password)) {
                displayPassword = "[set]";
            }
            return "PowerDogLocalApiServerCache [host=" + host + ", password=" + displayPassword + ", lastUpdate="
                    + lastUpdate + ", cache=" + cache + "]";
        }

        public URL url() throws MalformedURLException {
            return new URL("http", host, port, "");
        }

    }

    /*-
     * PowerAPI Local Device API 0.b (15.02.2013)
     * 
     * PowerDog supports via the PowerAPI Local Device API live communication
     * with your PowerDog device. The API is accessible using XMLRPC.
     *
     * This interface defines the possible RPC communication with the PowerDog.
     * This interface is according to PowerDog's RPC interface as per the
     * publicly available document
     * http://api.power-dog.eu/documentation/DOCUMENATION/PowerAPI%20Local%20Device%20API%20Description_v0.b.pdf
     * 
     * Tested against PowerDog Firmware V1.84 (High Velocity) 2013-09-19
     * 
     * VariantMap getPowerDogInfo(String password); 
     * VariantMap getSensors(String password); 
     * VariantMap getCounters(String password); 
     * VariantMap getRegulations(String password); 
     * VariantMap getLinearDevices(String password); 
     * VariantMap getAllCurrentLinearValues(String password);
     * VariantMap getCurrentLinearValues(String password, String comma_seperated_list_of_keys); 
     * VariantMap setLinearSensorDevice(String password, String key, String current_value); 
     * VariantMap setLinearCounterDevice(String password, String key, String current_value, String countup_meter_reading);
     * 
     * Remark: PowerDog also supports a web API with non-live data using the web
     * service available at http://power-dog.eu - this interface is different
     * and neither used nor supported by the PowerDogLocalApiBinding.
     * 
     * @author Wuellueb
     *
     */
    static interface PowerDog {
        public XmlRpcStruct getPowerDogInfo(String password) throws XmlRpcFault;

        public XmlRpcStruct getSensors(String password) throws XmlRpcFault;

        public XmlRpcStruct getCounters(String password) throws XmlRpcFault;

        public XmlRpcStruct getRegulations(String password) throws XmlRpcFault;

        public XmlRpcStruct getLinearDevices(String password) throws XmlRpcFault;

        public XmlRpcStruct getAllCurrentLinearValues(String password) throws XmlRpcFault;

        public XmlRpcStruct getCurrentLinearValues(String password, String comma_seperated_list_of_keys)
                throws XmlRpcFault;

        public XmlRpcStruct setLinearSensorDevice(String password, String key, String current_value) throws XmlRpcFault;

        public XmlRpcStruct setLinearCounterDevice(String password, String key, String current_value,
                String countup_meter_reading) throws XmlRpcFault;
    }
}
