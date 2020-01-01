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
package org.openhab.binding.ebus.internal;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.ebus.EBusBindingProvider;
import org.openhab.binding.ebus.internal.connection.AbstractEBusWriteConnector;
import org.openhab.binding.ebus.internal.connection.EBusCommandProcessor;
import org.openhab.binding.ebus.internal.connection.EBusConnectorEventListener;
import org.openhab.binding.ebus.internal.connection.EBusTCPConnector;
import org.openhab.binding.ebus.internal.parser.EBusConfigurationProvider;
import org.openhab.binding.ebus.internal.parser.EBusTelegramCSVWriter;
import org.openhab.binding.ebus.internal.parser.EBusTelegramParser;
import org.openhab.binding.ebus.internal.utils.EBusUtils;
import org.openhab.binding.ebus.internal.utils.StateUtils;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * eBus binding implementation.
 *
 * @author Christian Sowada
 * @since 1.7.0
 */
public class EBusBinding extends AbstractBinding<EBusBindingProvider>
        implements ManagedService, EBusConnectorEventListener {

    private static final Logger logger = LoggerFactory.getLogger(EBusBinding.class);

    // Used to process the commands incl. polling
    private EBusCommandProcessor commandProcessor;

    // The connector to serial or ethernet
    private AbstractEBusWriteConnector connector;

    // The parser to converts received bytes to key/value maps
    private EBusTelegramParser parser;

    private EBusConfigurationProvider configurationProvider;

    private EBusTelegramCSVWriter debugWriter;

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.core.binding.AbstractBinding#internalReceiveCommand(java.lang.String,
     * org.openhab.core.types.Command)
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {

        for (EBusBindingProvider provider : providers) {
            byte[] data = commandProcessor.composeSendData(provider, itemName, command);

            connector.addToSendQueue(data);
        }
    }

    /**
     * Check and initialize the configuration provider if needed
     */
    private void checkConfigurationProvider() {
        if (configurationProvider == null) {
            configurationProvider = new EBusConfigurationProvider();
        }
    }

    /**
     * Check and initialize the command processor if needed
     */
    private void checkCommandProcessor() {
        if (commandProcessor == null) {

            checkConfigurationProvider();

            commandProcessor = new EBusCommandProcessor();
            commandProcessor.setConfigurationProvider(configurationProvider);
        }
    }

    private void stopConnector() {

        if (debugWriter != null) {
            try {
                debugWriter.close();
                debugWriter = null;
            } catch (IOException e) {
                logger.error("error!", e);
            }
        }

        // stop last thread if active
        if (connector != null && connector.isAlive()) {

            logger.info("Shutdown old eBus connector thread ...");
            connector.interrupt();

            try {
                // wait up to 20sec. for shutdown
                logger.debug("Waiting for connector thread shutdown ...");
                connector.join(20000);

                if (connector.isAlive()) {
                    logger.debug("Unable to stop eBUS connection thread!");
                } else {
                    logger.debug("Connector thread successfully shutdown ...");
                }

            } catch (InterruptedException e) {
                logger.error("Interrupted while shutdown old connector thread!", e);
            }
        }
    }

    protected void addBindingProvider(EBusBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(EBusBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
     */
    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {

        logger.info("Update eBus Binding configuration ...");

        if (properties == null || properties.isEmpty()) {
            throw new RuntimeException("No properties in openhab.cfg set!");
        }

        try {
            // stop last connector-thread if active
            stopConnector();

            // check to ensure that it is available
            checkConfigurationProvider();

            // clear current configuration
            configurationProvider.clear();

            // load parser from default url
            parser = new EBusTelegramParser(configurationProvider);

            URL configurationUrl = null;
            String parsers = (String) properties.get("parsers");

            if (StringUtils.isEmpty(parsers)) {
                // set to current stable configurations as default
                parsers = "common";
            }

            for (String elem : parsers.split(",")) {
                configurationUrl = null;

                // check for keyword custom to load custom configuration
                if (elem.trim().equals("custom")) {
                    String parserUrl = (String) properties.get("parserUrl");
                    if (parserUrl != null) {
                        logger.debug("Load custom eBus Parser with url {}", parserUrl);
                        configurationUrl = new URL(parserUrl);
                    }

                } else {
                    logger.debug("Load eBus Parser Configuration \"{}\" ...", elem.trim());
                    String filename = "src/main/resources/" + elem.trim() + "-configuration.json";

                    Bundle bundle = FrameworkUtil.getBundle(EBusBinding.class);
                    configurationUrl = bundle.getResource(filename);

                    if (configurationUrl == null) {
                        logger.error("Unable to load file {} ...", elem.trim() + "-configuration.json");
                    }
                }

                if (configurationUrl != null) {
                    configurationProvider.loadConfigurationFile(configurationUrl);
                }
            }

            // check minimal config
            if (properties.get("serialPort") != null && properties.get("hostname") != null) {
                throw new ConfigurationException("hostname", "Set property serialPort or hostname, not both!");
            }

            // use the serial connector
            if (StringUtils.isNotEmpty((String) properties.get("serialPort"))) {

                try {
                    // load class by reflection to keep gnu.io (serial) optional. Declarative Services causes an
                    // class not found exception, also if serial is not used!
                    // FIXME: Is there a better way to avoid that a class not found exception?
                    @SuppressWarnings("unchecked")
                    Class<AbstractEBusWriteConnector> _tempClass = (Class<AbstractEBusWriteConnector>) EBusBinding.class
                            .getClassLoader()
                            .loadClass("org.openhab.binding.ebus.internal.connection.EBusSerialConnector");

                    Constructor<AbstractEBusWriteConnector> constructor = _tempClass
                            .getDeclaredConstructor(String.class);
                    connector = constructor.newInstance((String) properties.get("serialPort"));

                } catch (ClassNotFoundException e) {
                    logger.error(e.toString(), e);
                } catch (NoSuchMethodException e) {
                    logger.error(e.toString(), e);
                } catch (SecurityException e) {
                    logger.error(e.toString(), e);
                } catch (InstantiationException e) {
                    logger.error(e.toString(), e);
                } catch (IllegalAccessException e) {
                    logger.error(e.toString(), e);
                } catch (IllegalArgumentException e) {
                    logger.error(e.toString(), e);
                } catch (InvocationTargetException e) {
                    logger.error(e.toString(), e);
                }

            } else if (StringUtils.isNotEmpty((String) properties.get("hostname"))) {

                // use the tcp-ip connector
                connector = new EBusTCPConnector((String) properties.get("hostname"),
                        Integer.parseInt((String) properties.get("port")));
            }

            // Set eBus sender id or default 0xFF
            if (StringUtils.isNotEmpty((String) properties.get("senderId"))) {
                connector.setSenderId(EBusUtils.toByte((String) properties.get("senderId")));
            }

            if (properties.get("record") != null) {
                String debugWriterMode = (String) properties.get("record");
                logger.info("Enable CSV writer for eBUS {}", debugWriterMode);

                debugWriter = new EBusTelegramCSVWriter();
                debugWriter.openInUserData("ebus-" + debugWriterMode + ".csv");

                parser.setDebugCSVWriter(debugWriter, debugWriterMode);
            }

            // add event listener
            connector.addEBusEventListener(this);

            // start thread
            connector.start();

            // set the new connector
            commandProcessor.setConnector(connector);
            commandProcessor.setConfigurationProvider(configurationProvider);

        } catch (MalformedURLException e) {
            logger.error(e.toString(), e);
        } catch (IOException e) {
            throw new ConfigurationException("general", e.toString(), e);
        }
    }

    @Override
    public void allBindingsChanged(BindingProvider provider) {
        super.allBindingsChanged(provider);

        checkCommandProcessor();

        commandProcessor.allBindingsChanged(provider);
    }

    @Override
    public void bindingChanged(BindingProvider provider, String itemName) {
        super.bindingChanged(provider, itemName);

        checkCommandProcessor();

        commandProcessor.bindingChanged(provider, itemName);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.core.binding.AbstractBinding#activate()
     */
    @Override
    public void activate() {
        super.activate();
        logger.debug("eBus binding has been started.");

        // check to ensure that it is available
        checkConfigurationProvider();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.core.binding.AbstractBinding#deactivate()
     */
    @Override
    public void deactivate() {
        super.deactivate();

        // stop last connector-thread if active
        stopConnector();

        if (commandProcessor != null) {
            commandProcessor.deactivate();
            commandProcessor = null;
        }

        if (debugWriter != null) {
            try {
                debugWriter.close();
            } catch (IOException e) {
                logger.error("io error", e);
            }
            debugWriter = null;
        }

        logger.debug("eBus binding has been stopped.");
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openhab.binding.ebus.connection.EBusConnectorEventListener#onTelegramReceived(org.openhab.binding.ebus.
     * EbusTelegram)
     */
    @Override
    public void onTelegramReceived(EBusTelegram telegram) {

        // parse the raw telegram to a key/value map
        final Map<String, Object> results = parser.parse(telegram);

        if (results == null) {
            logger.trace("No valid parser result for raw telegram!");
            return;
        }

        for (Entry<String, Object> entry : results.entrySet()) {

            State state = StateUtils.convertToState(entry.getValue());

            // process if the state is set
            if (state != null) {

                // loop over all items to update the state
                for (EBusBindingProvider provider : providers) {
                    for (String itemName : provider.getItemNames(entry.getKey())) {

                        Byte telegramSource = provider.getTelegramSource(itemName);
                        Byte telegramDestination = provider.getTelegramDestination(itemName);

                        // check if this item has a src or dst defined
                        if (telegramSource == null || telegram.getSource() == telegramSource) {
                            if (telegramDestination == null || telegram.getDestination() == telegramDestination) {
                                eventPublisher.postUpdate(itemName, state);
                            }
                        }
                    }
                } // for
            } // if
        }
    }

}
