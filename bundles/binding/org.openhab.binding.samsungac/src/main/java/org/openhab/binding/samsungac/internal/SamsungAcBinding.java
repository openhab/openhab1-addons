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
package org.openhab.binding.samsungac.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.samsungac.SamsungAcBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding listening OpenHAB bus and send commands to Samsung Air Conditioner
 * devices when command is received.
 *
 * @author Stein Tore Tøsse
 * @author John Cocula added optional port parameter
 * @since 1.6.0
 */
public class SamsungAcBinding extends AbstractActiveBinding<SamsungAcBindingProvider> implements ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(SamsungAcBinding.class);

    /**
     * the refresh interval which is used to check for lost connections
     * (optional, defaults to 60000ms)
     */
    private long refreshInterval = 60000;

    private Map<String, AirConditioner> nameHostMapper = null;

    protected Map<String, Map<String, String>> deviceConfigCache = new HashMap<String, Map<String, String>>();

    public SamsungAcBinding() {
    }

    @Override
    public void activate() {
        logger.debug("Started Samsung AC Binding");
    }

    @Override
    public void deactivate() {
        logger.debug("deactive");
        // close any open connections
        if (nameHostMapper != null) {
            for (AirConditioner connector : nameHostMapper.values()) {
                connector.disconnect();
            }
        }
    }

    /**
     * @{inheritDoc
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {

        if (itemName != null && command != null) {
            logger.debug("InternalReceiveCommand [{} : {}]", itemName, command);
            String hostName = getAirConditionerInstance(itemName);
            AirConditioner host = nameHostMapper.get(hostName);
            if (host == null) {
                logger.debug("Host with hostname: '{}' not found...", hostName);
                return;
            }
            CommandEnum property = getProperty(itemName);

            String cmd = getCmdStringFromEnumValue(command, property);

            if (cmd != null) {
                sendCommand(host, property, cmd, hostName);
            } else {
                logger.warn("Not sending for itemName: '{}' because property not implemented: '{}'", itemName,
                        property);
            }
        }
    }

    private String getCmdStringFromEnumValue(Command command, CommandEnum property) {
        String cmd = null;
        switch (property) {
            case AC_FUN_POWER:
            case AC_ADD_SPI:
            case AC_ADD_AUTOCLEAN:
                cmd = "ON".equals(command.toString()) ? "On" : "Off";
                break;
            case AC_FUN_WINDLEVEL:
                cmd = WindLevelEnum.getFromValue(command).toString();
                break;
            case AC_FUN_OPMODE:
                cmd = OperationModeEnum.getFromValue(command).toString();
                break;
            case AC_FUN_COMODE:
                cmd = ConvenientModeEnum.getFromValue(command).toString();
                break;
            case AC_FUN_DIRECTION:
                cmd = DirectionEnum.getFromValue(command).toString();
                break;
            case AC_FUN_TEMPSET:
                cmd = Integer.toString(new Double(command.toString()).intValue());
                break;
            case AC_FUN_ERROR:
            default:
                cmd = command.toString();
                break;
        }
        return cmd;
    }

    private void sendCommand(AirConditioner aircon, CommandEnum property, String value, String hostName) {
        int i = 1;
        boolean commandSent = false;
        while (i < 5 && !commandSent) {
            try {
                logger.debug("[{}/5] Sending command: {} to property:{} with ip:{}", i, value, property,
                        aircon.getIpAddress());
                Map<CommandEnum, String> status = aircon.sendCommand(property, value);
                if (status != null) {
                    commandSent = true;
                    logger.debug("Command[{}] sent on try number {}", value, i);
                    updateAllItemsFromStatusMap(status, hostName);
                }
            } catch (Exception e) {
                logger.warn("Could not send value: '{}' to property:'{}', try {}/5", value, property, i);
            } finally {
                i++;
            }
        }
    }

    private String getAirConditionerInstance(String itemName) {
        for (BindingProvider provider : providers) {
            if (provider instanceof SamsungAcBindingProvider) {
                SamsungAcBindingProvider acProvider = (SamsungAcBindingProvider) provider;
                if (acProvider.getItemNames().contains(itemName)) {
                    return acProvider.getAirConditionerInstance(itemName);
                }
            }
        }
        return null;
    }

    private CommandEnum getProperty(String itemName) {
        for (BindingProvider provider : providers) {
            if (provider instanceof SamsungAcBindingProvider) {
                SamsungAcBindingProvider acProvider = (SamsungAcBindingProvider) provider;
                if (acProvider.getItemNames().contains(itemName)) {
                    return acProvider.getProperty(itemName);
                }
            }
        }
        return null;
    }

    private String getItemName(String acName, CommandEnum property) {
        for (BindingProvider provider : providers) {
            if (provider instanceof SamsungAcBindingProvider) {
                SamsungAcBindingProvider acProvider = (SamsungAcBindingProvider) provider;
                return acProvider.getItemName(acName, property);
            }
        }
        return null;
    }

    protected void addBindingProvider(SamsungAcBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(SamsungAcBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * {@inheritDoc}
     */
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        Enumeration<String> keys = config.keys();

        String refreshIntervalString = (String) config.get("refresh");
        if (StringUtils.isNotBlank(refreshIntervalString)) {
            refreshInterval = Long.parseLong(refreshIntervalString);
            logger.info("Refresh interval set to {} ms", refreshIntervalString);
        } else {
            logger.info("No refresh interval configured, using default: {} ms", refreshInterval);
        }

        Map<String, AirConditioner> hosts = new HashMap<String, AirConditioner>();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            logger.debug("Configuration key is: {}", key);
            if ("service.pid".equals(key)) {
                continue;
            }

            String[] parts = key.split("\\.");
            String hostname = parts[0];

            AirConditioner host = hosts.get(hostname);
            if (host == null) {
                host = new AirConditioner();
            }

            String value = ((String) config.get(key)).trim();

            if ("host".equals(parts[1])) {
                host.setIpAddress(value);
            }
            if ("port".equals(parts[1])) {
                try {
                    host.setPort(Integer.parseInt(value));
                } catch (NumberFormatException nfe) {
                    throw new ConfigurationException("port", "Invalid port number specified '" + value + "'", nfe);
                }
            }
            if ("mac".equals(parts[1])) {
                host.setMacAddress(value);
            }
            if ("token".equals(parts[1])) {
                host.setToken(value);
            }
            if ("certificate".equals(parts[1])) {
                host.setCertificateFileName(value);
            }

            if ("password".equals(parts[1])) {
                host.setCertificatePassword(value);
            }
            hosts.put(hostname, host);
        }
        nameHostMapper = hosts;

        if (nameHostMapper == null || nameHostMapper.size() == 0) {
            setProperlyConfigured(false);
            Map<String, Map<String, String>> discovered = SsdpDiscovery.discover();
            if (discovered != null && discovered.size() > 0) {
                for (Map<String, String> ac : discovered.values()) {
                    if (ac.get("IP") != null && ac.get("MAC_ADDR") != null) {
                        logger.warn(
                                "We found air conditioner. Please put the following in your configuration file: "
                                        + "\r\n samsungac:<ACNAME>.host={}\r\n samsungac:<ACNAME>.mac={}",
                                ac.get("IP"), ac.get("MAC_ADDR"));
                    }
                }
            } else {
                logger.warn("No Samsung Air Conditioner has been configured, and we could not find one either");
            }
        } else {
            setProperlyConfigured(true);
        }
    }

    @Override
    protected void execute() {
        if (!bindingsExist()) {
            logger.debug("There is no existing Samsung AC binding configuration => refresh cycle aborted!");
            return;
        }

        if (nameHostMapper == null) {
            logger.debug("Name host mapper not yet set. Aborted refresh");
            return;
        }

        for (Map.Entry<String, AirConditioner> entry : nameHostMapper.entrySet()) {
            AirConditioner host = entry.getValue();
            String acName = entry.getKey();
            if (host.isConnected()) {
                getAndUpdateStatusForAirConditioner(acName, host);
            } else {
                reconnectToAirConditioner(entry.getKey(), host);
            }
        }
    }

    private void reconnectToAirConditioner(String key, AirConditioner host) {
        logger.debug("Broken connection found for '{}', attempting to reconnect...", key);
        try {
            host.login();
            logger.debug("Connection to {} has succeeded", host.toString());
        } catch (Exception e) {
            if (e == null || e.toString() == null || e.getCause() == null) {
                logger.debug("Returned null-exception...");
            } else {
                logger.debug("Caught exception: {} : {}", e.toString(), e.getCause().toString());
            }
            logger.debug("Reconnect failed for '{}', will retry in {}s", key, refreshInterval / 1000);
        }
    }

    private void getAndUpdateStatusForAirConditioner(String acName, AirConditioner host) {
        Map<CommandEnum, String> status = new HashMap<CommandEnum, String>();
        try {
            logger.debug("Getting status for ac: '{}'", acName);
            status = host.getStatus();
        } catch (Exception e) {
            logger.debug("Could not get status.. returning.., got exception: {}", e.toString());
            return;
        }

        updateAllItemsFromStatusMap(status, acName);
    }

    private void updateAllItemsFromStatusMap(Map<CommandEnum, String> status, String acName) {
        for (CommandEnum cmd : status.keySet()) {
            logger.debug("Trying to find item for: {} and cmd: {}", acName, cmd.toString());
            String item = getItemName(acName, cmd);
            String value = status.get(cmd);
            if (item != null && value != null) {
                updateItemWithValue(cmd, item, value);
            }
        }
    }

    private void updateItemWithValue(CommandEnum cmd, String item, String value) {
        try {
            switch (cmd) {
                case AC_FUN_TEMPNOW:
                case AC_FUN_TEMPSET:
                    postUpdate(item, DecimalType.valueOf(value));
                    break;
                case AC_FUN_POWER:
                case AC_ADD_SPI:
                case AC_ADD_AUTOCLEAN:
                    postUpdate(item, value.toUpperCase().equals("ON") ? OnOffType.ON : OnOffType.OFF);
                    break;
                case AC_FUN_COMODE:
                    postUpdate(item, DecimalType.valueOf(Integer.toString(ConvenientModeEnum.valueOf(value).value)));
                    break;
                case AC_FUN_OPMODE:
                    postUpdate(item, DecimalType.valueOf(Integer.toString(OperationModeEnum.valueOf(value).value)));
                    break;
                case AC_FUN_WINDLEVEL:
                    postUpdate(item, DecimalType.valueOf(Integer.toString(WindLevelEnum.valueOf(value).value)));
                    break;
                case AC_FUN_DIRECTION:
                    postUpdate(item, DecimalType.valueOf(Integer.toString(DirectionEnum.valueOf(value).value)));
                    break;
                case AC_FUN_ERROR:
                default:
                    postUpdate(item, StringType.valueOf(value));
                    break;
            }
        } catch (IllegalArgumentException iae) {
            logger.warn(
                    "Update of item [{}] failed, probably because the received value for the command is not implemented [{}.{}]",
                    item, cmd, value);
        }
    }

    private void postUpdate(String item, State state) {
        if (item != null && state != null) {
            logger.debug("{} gets updated to: {}", item, state);
            eventPublisher.postUpdate(item, state);
        } else {
            logger.debug("Could not update item: '{}' with state: '{}'", item, state.toString());
        }
    }

    @Override
    protected long getRefreshInterval() {
        return refreshInterval;
    }

    @Override
    protected String getName() {
        return "Samsung Air Conditioner service";
    }
}
