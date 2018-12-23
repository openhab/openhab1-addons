/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zibase.internal;

import java.util.Collection;
import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.zibase.ZibaseBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.zapi.Zibase;

/**
 * Main class for Zibase Binding..
 *
 * It launch a Zibase listener to translate zibase activity has event,
 * poll xml sensors file (http) and handle command to be sent to the zibase
 *
 * @author Julien Tiphaine
 * @since 1.7.0
 */
public class ZibaseBinding extends AbstractActiveBinding<ZibaseBindingProvider> implements ManagedService {

    /**
     * generic logger
     */
    private static final Logger logger = LoggerFactory.getLogger(ZibaseBinding.class);

    /**
     * zibase to which we are connected
     */
    private static Zibase zibase;

    /**
     * zibase binding provider
     */
    private static ZibaseGenericBindingProvider bindingProvider = new ZibaseGenericBindingProvider();

    /**
     * Associated Zibase listener instance
     */
    private static ZibaseListener zibaseListener = null;

    /**
     * the refresh interval which is used to poll values from the zibase
     * server (optional, defaults to 60000ms)
     */
    private static long refreshInterval = 60000;

    /**
     * ip adresse of zibase to connect to
     */
    private static String ip;

    /**
     * ip address sent to Zibase for registering
     */
    private static String listenerHost = "127.0.0.1";

    /**
     * ip address sent to Zibase for registering
     */
    private static int listenerPort = 9876;

    /**
     * Constructor
     */
    public ZibaseBinding() {

    }

    /**
     * get the associated binding provider
     *
     * @return
     */
    public static ZibaseGenericBindingProvider getBindingProvider() {
        return bindingProvider;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void activate() {

    }

    /**
     * start the binding : connect to the zibase.
     */
    private void launch() {
        // insure that lister is not yet running
        if (zibaseListener != null) {
            zibaseListener.shutdown();
        }

        // connect to zibase
        zibase = new Zibase(ip);
        logger.info("connected to zibase for command sending");

        // start listener thread for all events the zibase is sending
        logger.info("Starting zibase listener thread...");
        zibaseListener = new ZibaseListener();
        zibaseListener.setZibase(zibase);
        zibaseListener.setEventPubisher(eventPublisher);
        zibaseListener.setListenerHost(listenerHost);
        zibaseListener.setListenerPort(listenerPort);
        zibaseListener.start();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void deactivate() {
        // deallocate resources here that are no longer needed and
        // should be reset when activating this binding again
        logger.info("Shutting down zibase connection and/or thread...");
        zibaseListener.shutdown();
        zibase = null;
        logger.info("Zibase binding deactivated");
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
        return "zibase Refresh Service";
    }

    /**
     * @{inheritDoc}
     *               TODO : handle all states (variables, calendars...)
     *               TODO : catch java.net.SocketTimeoutException and deactivate / activate the binding to restart
     *               connection
     */
    @Override
    protected void execute() {
        Collection<ZibaseBindingConfig> configs = ZibaseGenericBindingProvider.itemNameMap.values();

        // Zibase may not yet be ready, or may be disconnected
        if (zibase == null) {
            return;
        }

        logger.debug("START Zibase items refresh (interval: {})", refreshInterval);

        for (ZibaseBindingConfig config : configs) {

            // Update receivers state
            if (config.getClass() == ZibaseBindingConfigReceiver.class
                    || config.getClass() == ZibaseBindingConfigVariable.class) {

                logger.debug("  trying to get zibase value for ID : {}", config.getId());

                State state = config.getOpenhabStateFromZibaseValue(zibase, null);

                if (state != null) {
                    logger.debug("  got value : {}", state);
                    eventPublisher.postUpdate(bindingProvider.getItemNamesById(config.getId()).firstElement(), state);
                } else {
                    logger.info("  got null value from zibase for ID: {}", config.getId());
                }
            }
        }

        logger.debug("END Zibase items refresh");
    }

    /**
     * @{inheritDoc}
     *               TODO : check how dim values work
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        // the code being executed when a command was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.
        logger.debug(
                "internalReceiveCommand() is called with ITEM = " + itemName + " / COMMAND = " + command.toString());
        ZibaseBindingConfig config = bindingProvider.getItemConfig(itemName);
        config.sendCommand(zibase, command, -1);
    }

    protected void addBindingProvider(ZibaseBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(ZibaseBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {
            // to override the default refresh interval one has to add a
            // parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
            logger.debug("Loading zibase configuration");

            String refreshIntervalString = (String) config.get("refresh");
            if (StringUtils.isNotBlank(refreshIntervalString)) {
                refreshInterval = Long.parseLong(refreshIntervalString);
            }

            String ip = (String) config.get("ip");
            if (StringUtils.isNotBlank(ip)) {
                ZibaseBinding.ip = ip;
            }

            String tmpListenerIp = (String) config.get("listenerHost");
            if (StringUtils.isNotBlank(tmpListenerIp)) {
                ZibaseBinding.listenerHost = tmpListenerIp;
            }

            String tmpListenerPort = (String) config.get("listenerPort");
            if (StringUtils.isNotBlank(tmpListenerPort)) {
                ZibaseBinding.listenerPort = Integer.parseInt(tmpListenerPort);
            }

            // read further config parameters here ...
            setProperlyConfigured(true);
            this.launch();
        }
    }
}
