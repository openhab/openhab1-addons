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
package org.openhab.binding.velux.internal;

import java.util.Dictionary;
import java.util.Enumeration;

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
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
public class VeluxBinding extends AbstractActiveBinding<VeluxBindingProvider> implements ManagedService {
    private static Logger logger = LoggerFactory.getLogger(VeluxBinding.class);

    /**
     * The configuration parameters for accessing the Velux bridge.
     */
    private VeluxBridgeConfiguration config = null;

    /**
     * Velux binding provider will hold the related OpenHAB configuration.
     */
    private VeluxBindingProvider bindingProvider = null;

    /**
     * Velux bridge handler will provide the related interfacing information and methods.
     */
    private VeluxBridgeHandlerOH1 bridgeHandler = null;

    /**
     * Velux bridge handler will provide the related interfacing information and methods.
     */
    private static final int REFRESH_FIRST_TIME = 0;
    private int refreshCounter = REFRESH_FIRST_TIME;

    private boolean isModulo(int a, int b) {
        return ((a % b) == 0) ? true : false;
    }

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
        this.config = new VeluxBridgeConfiguration();
        this.bridgeHandler = new VeluxBridgeHandlerOH1(config);
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

    @Override
    public void allBindingsChanged(BindingProvider provider) {
        logger.trace("allBindingsChanged() called.");
        super.allBindingsChanged(provider);
    }

    /**
     * Activates the binding. Actually does nothing, because on activation
     * openHAB always calls updated to indicate that the configuration is updated.
     * Activation is done there.
     */
    @Override
    public void activate() {
        logger.trace("activate() called.");
        logger.info("Active items are: {}.", bindingProvider.getInBindingItemNames());
    }

    @Override
    protected String getName() {
        logger.trace("getName() called.");
        return VeluxBindingConstants.BINDING_ID + " Refresh Service";
    }

    @Override
    protected long getRefreshInterval() {
        logger.info("{} refresh interval set to {} milliseconds.", VeluxBindingConstants.BINDING_ID,
                this.config.refreshMSecs);
        logger.trace("getRefreshInterval() returns {}.", this.config.refreshMSecs);
        return this.config.refreshMSecs;
    }

    /***
     *** Continuous methods
     ***/

    @Override
    public void execute() {
        synchronized (this) {
            logger.debug("execute() called.");
            if (!bindingsExist()) {
                logger.debug("There is no existing Velux binding configuration => refresh cycle aborted!");
                return;
            }
            if (this.bindingProvider == null) {
                logger.debug("There is no existing Velux binding configuration => refresh cycle aborted.");
                return;
            }
            for (VeluxBindingProvider provider : providers) {
                logger.trace("execute(): working with VeluxBindingProvider {}.", provider);
                for (String itemName : provider.getInBindingItemNames()) {
                    VeluxBindingConfig itemConfig = this.bindingProvider.getConfigForItemName(itemName);
                    VeluxItemType itemType = itemConfig.getBindingItemType();
                    if (((this.refreshCounter == REFRESH_FIRST_TIME) && (itemType.isReadable()))
                            || (itemType.isToBeRefreshed())) {
                        if ((this.refreshCounter == REFRESH_FIRST_TIME)
                                || (isModulo(this.refreshCounter, itemType.getRefreshDivider()))) {
                            logger.trace("execute(): refreshing item {}.", itemName);
                            this.bridgeHandler.handleCommandOnChannel(itemName, null, itemConfig, provider,
                                    this.eventPublisher);
                        } else {
                            logger.trace("execute(): refresh cycle not yet come for item {}.", itemName);
                        }
                    } else {
                        logger.trace("execute(): ignoring item {} as not-refreshable.", itemName);
                    }
                }
            }
            this.refreshCounter++;
            logger.debug("execute() done.");
        }
    }

    @Override
    public void receiveCommand(String itemName, Command command) {
        logger.trace("receiveCommand({},{}) called.", itemName, command.toString());

        if (this.eventPublisher == null) {
            logger.warn("receiveCommand(): eventPublisher is NULL. Should NEVER occur.");
            return;
        }
        super.receiveCommand(itemName, command);
    }

    @Override
    public void internalReceiveCommand(String itemName, Command command) {
        logger.trace("internalReceiveCommand({},{}) called.", itemName, command.toString());
        if (this.eventPublisher == null) {
            logger.warn("internalReceiveCommand(): eventPublisher is NULL. Should NEVER occur.");
            return;
        }
        VeluxBindingConfig itemConfig = this.bindingProvider.getConfigForItemName(itemName);
        VeluxItemType itemType = itemConfig.getBindingItemType();
        if (itemType.isWritable() || itemType.isExecutable()) {
            logger.trace("internalReceiveCommand() is about to send update to item {}.", itemName);
            for (VeluxBindingProvider provider : providers) {
                logger.trace("internalReceiveCommand() working with VeluxBindingProvider {}.", provider);
                bridgeHandler.handleCommandOnChannel(itemName, command, itemConfig, provider, this.eventPublisher);
            }
        } else {
            logger.warn("internalReceiveCommand() ignoring command to item {} as neither writable nor executable.",
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
        if (config == null) {
            logger.info("updated() called with empty dictionary.");
            return;
        }
        logger.debug("updated() called with {} dictionary entries.", config.size());
        Enumeration<String> configKeys = config.keys();
        while (configKeys.hasMoreElements()) {
            String key = configKeys.nextElement();
            System.out.println(key + ": " + config.get(key));
            switch (key) {
                case VeluxBridgeConfiguration.BRIDGE_PROTOCOL:
                    final String protocol = (String) config.get(VeluxBridgeConfiguration.BRIDGE_PROTOCOL);
                    if (protocol.length() > 0) {
                        this.config.bridgeProtocol = protocol;
                        this.config.hasChanged = true;
                        logger.debug("updated(): adapted BRIDGE_PROTOCOL to {}.", this.config.bridgeProtocol);
                    }
                    break;
                case VeluxBridgeConfiguration.BRIDGE_IPADDRESS:
                    final String ipAddressString = (String) config.get(VeluxBridgeConfiguration.BRIDGE_IPADDRESS);
                    if (ipAddressString.length() > 0) {
                        this.config.bridgeIPAddress = ipAddressString;
                        this.config.hasChanged = true;
                        logger.debug("updated(): adapted BRIDGE_IPADDRESS to {}.", this.config.bridgeIPAddress);
                    }
                    break;
                case VeluxBridgeConfiguration.BRIDGE_TCPPORT:
                    final String tcpPortString = (String) config.get(VeluxBridgeConfiguration.BRIDGE_TCPPORT);
                    if (tcpPortString.length() > 0) {
                        try {
                            this.config.bridgeTCPPort = Integer.parseInt(tcpPortString);
                        } catch (NumberFormatException e) {
                            throw new ConfigurationException(VeluxBridgeConfiguration.BRIDGE_TCPPORT, e.getMessage());
                        }
                        this.config.hasChanged = true;
                        logger.debug("updated(): adapted BRIDGE_TCPPORT to {}.", this.config.bridgeTCPPort);
                    }
                    break;
                case VeluxBridgeConfiguration.BRIDGE_PASSWORD:
                    final String passwordString = (String) config.get(VeluxBridgeConfiguration.BRIDGE_PASSWORD);
                    if (passwordString.length() > 0) {
                        this.config.bridgePassword = passwordString;
                        this.config.hasChanged = true;
                        logger.debug("updated(): adapted BRIDGE_PASSWORD to {}.", this.config.bridgePassword);
                    }
                    break;
                case VeluxBridgeConfiguration.BRIDGE_TIMEOUT_MSECS:
                    final String timeoutMsecsString = (String) config
                            .get(VeluxBridgeConfiguration.BRIDGE_TIMEOUT_MSECS);
                    if (timeoutMsecsString.length() > 0) {
                        try {
                            this.config.timeoutMsecs = Integer.parseInt(timeoutMsecsString);
                        } catch (NumberFormatException e) {
                            throw new ConfigurationException(VeluxBridgeConfiguration.BRIDGE_TIMEOUT_MSECS,
                                    e.getMessage());
                        }
                        this.config.hasChanged = true;
                        logger.debug("updated(): adapted BRIDGE_TIMEOUT_MSECS to {}.", this.config.timeoutMsecs);
                    }
                    break;
                case VeluxBridgeConfiguration.BRIDGE_RETRIES:
                    final String retryNoString = (String) config.get(VeluxBridgeConfiguration.BRIDGE_RETRIES);
                    if (retryNoString.length() > 0) {
                        try {
                            this.config.retries = Integer.parseInt(retryNoString);
                        } catch (NumberFormatException e) {
                            throw new ConfigurationException(VeluxBridgeConfiguration.BRIDGE_RETRIES, e.getMessage());
                        }
                        this.config.hasChanged = true;
                        logger.debug("updated(): adapted BRIDGE_RETRIES to {}.", this.config.retries);
                    }
                    break;
                case VeluxBridgeConfiguration.BRIDGE_REFRESH_MSECS:
                    final String refreshMsecsString = (String) config
                            .get(VeluxBridgeConfiguration.BRIDGE_REFRESH_MSECS);
                    if (refreshMsecsString.length() > 0) {
                        try {
                            this.config.refreshMSecs = Integer.parseInt(refreshMsecsString);
                        } catch (NumberFormatException e) {
                            throw new ConfigurationException(VeluxBridgeConfiguration.BRIDGE_REFRESH_MSECS,
                                    e.getMessage());
                        }
                        this.config.hasChanged = true;
                        logger.debug("updated(): adapted BRIDGE_REFRESH_MSECS to {}.", this.config.refreshMSecs);
                    }
                    break;
                case VeluxBridgeConfiguration.BRIDGE_IS_BULK_RETRIEVAL_ENABLED:
                    try {
                        final String bulkRetrievalString = (String) config
                                .get(VeluxBridgeConfiguration.BRIDGE_IS_BULK_RETRIEVAL_ENABLED);
                        if (bulkRetrievalString.length() > 0) {
                            try {
                                this.config.isBulkRetrievalEnabled = Boolean.parseBoolean(bulkRetrievalString);
                            } catch (NumberFormatException e) {
                                throw new ConfigurationException(
                                        VeluxBridgeConfiguration.BRIDGE_IS_BULK_RETRIEVAL_ENABLED, e.getMessage());
                            }
                            this.config.hasChanged = true;
                            logger.debug("updated(): adapted BRIDGE_IS_BULK_RETRIEVAL_ENABLED to {}.",
                                    this.config.isBulkRetrievalEnabled);
                        }
                    } catch (ClassCastException ex) {
                        logger.warn("updated(): bad configuration setting for {}: {}.",
                                VeluxBridgeConfiguration.BRIDGE_IS_BULK_RETRIEVAL_ENABLED, ex.toString());
                    }
                    break;
                default:
                    logger.warn("updated(): unknown configuration entry {}:={}.", key, config.get(key));
            }
        }

        setProperlyConfigured(true);

        logger.info("{}Config[{}={},{}={},{}={},{}={},{}={},{}={},{}={},{}={}]", VeluxBindingConstants.BINDING_ID,
                VeluxBridgeConfiguration.BRIDGE_PROTOCOL, this.config.bridgeProtocol,
                VeluxBridgeConfiguration.BRIDGE_IPADDRESS, this.config.bridgeIPAddress,
                VeluxBridgeConfiguration.BRIDGE_TCPPORT, this.config.bridgeTCPPort,
                VeluxBridgeConfiguration.BRIDGE_PASSWORD, this.config.bridgePassword.replaceAll(".", "*"),
                VeluxBridgeConfiguration.BRIDGE_TIMEOUT_MSECS, this.config.timeoutMsecs,
                VeluxBridgeConfiguration.BRIDGE_RETRIES, this.config.retries,
                VeluxBridgeConfiguration.BRIDGE_REFRESH_MSECS, this.config.refreshMSecs,
                VeluxBridgeConfiguration.BRIDGE_IS_BULK_RETRIEVAL_ENABLED, this.config.isBulkRetrievalEnabled);

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
        if (this.bridgeHandler != null) {
            this.bridgeHandler.finalize();
        }
        logger.trace("deactivate() done.");
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
