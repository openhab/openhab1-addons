/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.velux.internal;

import static org.apache.commons.lang.StringUtils.isNotBlank;

import java.util.Dictionary;

import org.openhab.binding.velux.VeluxBindingConstants;
import org.openhab.binding.velux.VeluxBindingProvider;
import org.openhab.binding.velux.handler.VeluxBridgeHandlerOH1;
import org.openhab.binding.velux.internal.config.VeluxBridgeConfiguration;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <B>Class for Velux binding which polls something and sends events frequently.</B>
 * <P>
 *
 * This binding is able to do the following tasks with the Velux KLF200 gateway:
 * <ul>
 * <LI>Startup phase:
 * <ul>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#VeluxBinding constructor}</li>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#setEventPublisher setEventPublisher}</li>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#addBindingProvider addBindingProvider}</li>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#allBindingsChanged allBindingsChanged}</li>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#getName getName}</li>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#activate activate}</li>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#getRefreshInterval getRefreshInterval}</li>
 * </ul>
 * </li>
 * <LI>Continuous phase:
 * <UL>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#execute execute}</li>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#receiveCommand receiveCommand}</li>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#internalReceiveCommand internalReceiveCommand}</li>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#internalReceiveUpdate internalReceiveUpdate}</li>
 * </ul>
 * </li>
 * <LI>Reconfiguration phase:
 * <UL>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#updated updated}</li>
 * </ul>
 * </li>
 * <LI>Shutdown phase:
 * <UL>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#deactivate deactivate}</li>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#unsetEventPublisher unsetEventPublisher}</li>
 * <li>{@link org.openhab.binding.velux.internal.VeluxBinding#removeBindingProvider removeBindingProvider}</li>
 * </ul>
 * </ul>
 *
 * @author Guenther Schreiner
 * @since 1.13.0
 */
public class VeluxBinding extends AbstractActiveBinding<VeluxBindingProvider> implements ManagedService {

    private static final Logger logger = LoggerFactory.getLogger(VeluxBinding.class);

    /**
     * The refresh interval which is used to poll values from the Velux binding.
     */
    private long refreshInterval;

    /**
     * The configuration parameters for accessing the Velux bridge.
     */
    private VeluxBridgeConfiguration config = new VeluxBridgeConfiguration();

    /**
     * Velux binding provider will hold the related OpenHAB configuration.
     */
    private VeluxBindingProvider bindingProvider = null;

    /**
     * Velux bridge handler will provide the related interfacing information and methods.
     */
    private VeluxBridgeHandlerOH1 bridgeHandler = null;

    /***
     *** Startup methods
     ***/

    /**
     * Constructor
     *
     * initializes the interface towards the Velux bridge.
     */
    public VeluxBinding() {
        logger.trace("VeluxBinding(constructor) called.");
        this.bridgeHandler = new VeluxBridgeHandlerOH1();
        this.refreshInterval = this.config.refresh;
        logger.trace("VeluxBinding(constructor) done.");
    }

    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        logger.trace("setEventPublisher() called.");
        this.eventPublisher = eventPublisher;
    }

    protected void addBindingProvider(VeluxBindingProvider thisBindingProvider) {
        logger.trace("addBindingProvider() called.");
        bindingProvider = thisBindingProvider;
        super.addBindingProvider(thisBindingProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void allBindingsChanged(BindingProvider provider) {
        logger.trace("allBindingsChanged() called.");
        super.allBindingsChanged(provider);
    }

    /**
     * Activates the binding. Actually does nothing, because on activation
     * OpenHAB always calls updated to indicate that the config is updated.
     * Activation is done there.
     */
    @Override
    public void activate() {
        logger.trace("activate() called.");
        logger.trace("activate() active items are: {}.", bindingProvider.getInBindingItemNames());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getName() {
        logger.trace("getName() called.");
        return VeluxBindingConstants.BINDING_ID + " Refresh Service";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected long getRefreshInterval() {
        logger.info("{} refresh interval is {} seconds.", VeluxBindingConstants.BINDING_ID, refreshInterval);
        logger.trace("getRefreshInterval() returns {}.", refreshInterval);
        return refreshInterval;
    }

    /***
     *** Continuous methods
     ***/

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        logger.debug("execute() called.");
        if (!bindingsExist()) {
            logger.debug("There is no existing Velux binding configuration => refresh cycle aborted!");
            return;
        }

        for (VeluxBindingProvider provider : providers) {
            logger.trace("execute() working with VeluxBindingProvider {}.", provider);
            for (String itemName : provider.getInBindingItemNames()) {
                if (this.bindingProvider.getConfigForItemName(itemName).getBindingItemType().isReadable()) {
                    logger.trace("execute() refreshing item {}.", itemName);
                    this.bridgeHandler.handleCommandOnChannel(itemName, null,
                            this.bindingProvider.getConfigForItemName(itemName), provider, this.eventPublisher);
                } else {
                    logger.trace("execute() ignoring item {} as not-readable.", itemName);
                }
            }
        }
        logger.debug("execute() done.");

    }

    @Override
    public void receiveCommand(String itemName, Command command) {
        logger.trace("receiveCommand({},{}) called.", itemName, command.toString());
        if (this.eventPublisher == null) {
            logger.warn("receiveCommand() eventPublisher is NULL");
        }
        super.receiveCommand(itemName, command);
    }

    @Override
    public void internalReceiveCommand(String itemName, Command command) {
        logger.trace("internalReceiveCommand({},{}) called.", itemName, command.toString());
        if (this.eventPublisher == null) {
            logger.warn("internalReceiveCommand() eventPublisher is NULL");
        }
        if (bindingProvider.getConfigForItemName(itemName).getBindingItemType().isWritable()
                || bindingProvider.getConfigForItemName(itemName).getBindingItemType().isExecutable()) {
            logger.trace("internalReceiveCommand() is about to send update to item {}.", itemName);
            for (VeluxBindingProvider provider : providers) {
                logger.trace("internalReceiveCommand() working with VeluxBindingProvider {}.", provider);
                bridgeHandler.handleCommandOnChannel(itemName, command, bindingProvider.getConfigForItemName(itemName),
                        provider, this.eventPublisher);
            }
        } else {
            logger.trace("internalReceiveCommand() ignoring command to item {} as neither writable nor executable.",
                    itemName);
        }
        logger.trace("internalReceiveCommand() done.");
    }

    @Override
    public void internalReceiveUpdate(String itemName, State newState) {
        logger.trace("internalReceiveUpdate({},{}) called.", itemName, newState);
    }

    /***
     *** Reconfiguration methods
     ***/

    @Override
    public void updated(final Dictionary<String, ?> config) throws ConfigurationException {
        logger.trace("updated({}) called.", config);
        if (config != null) {

            final String ipAddressString = (String) config.get(VeluxBridgeConfiguration.BRIDGE_IPADDRESS);
            if (isNotBlank(ipAddressString)) {
                this.config.bridgeIPAddress = ipAddressString;
            }

            final String tcpPortString = (String) config.get(VeluxBridgeConfiguration.BRIDGE_TCPPORT);
            if (isNotBlank(tcpPortString)) {
                try {
                    this.config.bridgeTCPPort = Integer.parseInt(tcpPortString);
                } catch (NumberFormatException e) {
                    throw new ConfigurationException(VeluxBridgeConfiguration.BRIDGE_TCPPORT, e.getMessage());
                }
            }
            final String passwordString = (String) config.get(VeluxBridgeConfiguration.BRIDGE_PASSWORD);
            if (isNotBlank(passwordString)) {
                this.config.bridgePassword = passwordString;
            }
            final String timeoutMsecsString = (String) config.get(VeluxBridgeConfiguration.BRIDGE_TIMEOUT_MSECS);
            if (isNotBlank(timeoutMsecsString)) {
                try {
                    this.config.timeoutMsecs = Integer.parseInt(timeoutMsecsString);
                } catch (NumberFormatException e) {
                    throw new ConfigurationException(VeluxBridgeConfiguration.BRIDGE_TCPPORT, e.getMessage());
                }
            }
            final String retryNoString = (String) config.get(VeluxBridgeConfiguration.BRIDGE_RETRIES);
            if (isNotBlank(retryNoString)) {
                try {
                    this.config.retries = Integer.parseInt(retryNoString);
                } catch (NumberFormatException e) {
                    throw new ConfigurationException(VeluxBridgeConfiguration.BRIDGE_TCPPORT, e.getMessage());
                }
            }
        }

        setProperlyConfigured(true);

        logger.info("{}Config[{}={},{}={},{}={},{}={},{}={}]", VeluxBindingConstants.BINDING_ID,
                VeluxBridgeConfiguration.BRIDGE_IPADDRESS, this.config.bridgeIPAddress,
                VeluxBridgeConfiguration.BRIDGE_TCPPORT, this.config.bridgeTCPPort,
                VeluxBridgeConfiguration.BRIDGE_PASSWORD, this.config.bridgePassword.replaceAll(".", "*"),
                VeluxBridgeConfiguration.BRIDGE_TIMEOUT_MSECS, this.config.timeoutMsecs,
                VeluxBridgeConfiguration.BRIDGE_RETRIES, this.config.retries);

        // Now that we've read ALL the configuration, initialize the binding.
        execute();
    }

    /***
     *** Shutdown methods
     ***/

    /**
     * Deactivates the binding. Nothing to do ;-)
     */
    @Override
    public void deactivate() {
        logger.trace("deactivate() called.");
    }

    @Override
    public void unsetEventPublisher(EventPublisher eventPublisher) {
        logger.trace("unsetEventPublisher() called.");
        this.eventPublisher = null;
    }

    protected void removeBindingProvider(VeluxBindingProvider thisBindingProvider) {
        logger.trace("removeBindingProvider() called.");
        super.removeBindingProvider(thisBindingProvider);
        bindingProvider = null;
    }

}

/**
 * end-of-internal/VeluxBinding.java
 */
