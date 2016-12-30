/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal;

import java.nio.charset.Charset;
import java.util.Dictionary;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.satel.SatelBindingConfig;
import org.openhab.binding.satel.SatelBindingProvider;
import org.openhab.binding.satel.SatelCommModule;
import org.openhab.binding.satel.command.IntegraStatusCommand;
import org.openhab.binding.satel.command.NewStatesCommand;
import org.openhab.binding.satel.command.SatelCommand;
import org.openhab.binding.satel.internal.event.ConnectionStatusEvent;
import org.openhab.binding.satel.internal.event.NewStatesEvent;
import org.openhab.binding.satel.internal.event.SatelEvent;
import org.openhab.binding.satel.internal.event.SatelEventListener;
import org.openhab.binding.satel.internal.protocol.Ethm1Module;
import org.openhab.binding.satel.internal.protocol.IntRSModule;
import org.openhab.binding.satel.internal.protocol.SatelModule;
import org.openhab.binding.satel.internal.types.IntegraType;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is main service class that helps exchanging data between openHAB and
 * Satel module in both directions. Implements regular openHAB binding service.
 *
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class SatelBinding extends AbstractActiveBinding<SatelBindingProvider>
        implements ManagedService, SatelEventListener, SatelCommModule {

    private static final Logger logger = LoggerFactory.getLogger(SatelBinding.class);

    private long refreshInterval = 10000;
    private String userCode;
    private SatelModule satelModule = null;
    private boolean forceRefresh = false;
    private String textEncoding;
    private String userCodeOverride = null;

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getName() {
        return "Satel Refresh Service";
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
    public void execute() {
        if (!this.satelModule.isInitialized()) {
            logger.debug("Module not initialized yet, skipping refresh");
            return;
        }

        // get list of states that have changed
        logger.trace("Sending 'get new states' command");
        this.satelModule.sendCommand(new NewStatesCommand(this.satelModule.getIntegraType() == IntegraType.I256_PLUS));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        logger.trace("Binding configuration updated");

        if (config == null) {
            return;
        }

        ConfigurationDictionary configuration = new ConfigurationDictionary(config);

        this.refreshInterval = configuration.getLong("refresh", 10000);
        this.userCode = configuration.getString("user_code");
        this.textEncoding = configuration.getString("encoding", "windows-1250");

        // validate charset
        try {
            Charset.forName(this.textEncoding);
        } catch (Exception e) {
            throw new ConfigurationException("encoding",
                    "Specified character set is either incorrect or not supported: " + this.textEncoding);
        }

        int timeout = configuration.getInt("timeout", 5000);
        String host = configuration.getString("host");
        if (StringUtils.isNotBlank(host)) {
            this.satelModule = new Ethm1Module(host, configuration.getInt("port", 7094), timeout,
                    configuration.getString("encryption_key"));
        } else {
            this.satelModule = new IntRSModule(configuration.getString("port"), timeout);
        }

        this.satelModule.addEventListener(this);
        this.satelModule.open();
        setProperlyConfigured(true);
        logger.trace("Binding properly configured");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        if (!isProperlyConfigured()) {
            logger.warn("Binding not properly configured, exiting");
            return;
        }

        if (!this.satelModule.isInitialized()) {
            logger.debug("Module not initialized yet, ignoring command");
            return;
        }

        for (SatelBindingProvider provider : providers) {
            SatelBindingConfig itemConfig = provider.getItemConfig(itemName);
            if (itemConfig != null) {
                logger.trace("Sending internal command for item {}: {}", itemName, command);
                SatelCommand satelCmd = itemConfig.convertCommand(command, this.satelModule.getIntegraType(),
                        getUserCode());
                if (satelCmd != null) {
                    this.satelModule.sendCommand(satelCmd);
                }
                break;
            }
        }
    }

    private String getUserCode() {
        if (StringUtils.isNotEmpty(this.userCodeOverride)) {
            return this.userCodeOverride;
        } else {
            return this.userCode;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void incomingEvent(SatelEvent event) {
        logger.trace("Handling incoming event: {}", event);

        // refresh all states that have changed
        if (event instanceof NewStatesEvent) {
            List<SatelCommand> commands = getRefreshCommands((NewStatesEvent) event);
            for (SatelCommand command : commands) {
                this.satelModule.sendCommand(command);
            }
        }

        // if just connected, force refreshing
        if (event instanceof ConnectionStatusEvent) {
            ConnectionStatusEvent statusEvent = (ConnectionStatusEvent) event;
            if (statusEvent.isConnected()) {
                switchForceRefresh(true);
            }
        }

        // update items
        for (SatelBindingProvider provider : providers) {
            for (String itemName : provider.getItemNames()) {
                SatelBindingConfig itemConfig = provider.getItemConfig(itemName);
                Item item = provider.getItem(itemName);
                State newState = itemConfig.convertEventToState(item, event);
                if (newState != null) {
                    logger.debug("Updating item state: item = {}, state = {}, event = {}", itemName, newState, event);
                    eventPublisher.postUpdate(itemName, newState);
                    itemConfig.setItemInitialized();
                }
            }
        }
    }

    /**
     * Deactivates the binding by closing connected module.
     */
    @Override
    public void deactivate() {
        if (this.satelModule != null) {
            this.satelModule.close();
            this.satelModule = null;
        }
    }

    private List<SatelCommand> getRefreshCommands(NewStatesEvent nse) {
        logger.trace("Gathering refresh commands from all items");

        boolean forceRefresh = switchForceRefresh(false);
        List<SatelCommand> commands = new LinkedList<SatelCommand>();
        for (SatelBindingProvider provider : providers) {
            for (String itemName : provider.getItemNames()) {
                logger.trace("Getting refresh command from item: {}", itemName);

                SatelBindingConfig itemConfig = provider.getItemConfig(itemName);
                SatelCommand command = itemConfig.buildRefreshCommand(this.satelModule.getIntegraType());

                if (command == null || commands.contains(command)) {
                    continue;
                }

                // either state has changed or this is status command (so likely RTC has changed)
                // also if item hasn't received any update yet or refresh is forced
                // get the latest value from the module
                byte commandCode = command.getRequest().getCommand();
                if (forceRefresh || !itemConfig.isItemInitialized() || (nse != null && nse.isNew(commandCode))
                        || commandCode == IntegraStatusCommand.COMMAND_CODE) {
                    commands.add(command);
                }
            }
        }

        return commands;
    }

    /**
     * Changes <code>forceRefresh</code> flag atomically returning previous
     * value.
     *
     * @param forceRefresh
     *            new value for the flag
     * @return previous value of the flag
     */
    private synchronized boolean switchForceRefresh(boolean forceRefresh) {
        boolean oldValue = this.forceRefresh;
        this.forceRefresh = forceRefresh;
        return oldValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        logger.debug("[SatelCommModule] isConnected");
        return this.satelModule != null && this.satelModule.isConnected();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sendCommand(SatelCommand command) {
        logger.debug("[SatelCommModule] sendCommand");
        if (this.satelModule == null) {
            return false;
        }
        if (!this.satelModule.sendCommand(command, true)) {
            return false;
        }

        boolean interrupted = false;
        while (!interrupted) {
            // wait for command state change
            try {
                synchronized (command) {
                    command.wait(this.satelModule.getTimeout());
                }
            } catch (InterruptedException e) {
                // ignore, we will leave the loop on next interruption state check
                interrupted = true;
            }
            // check current state
            switch (command.getState()) {
                case SUCCEEDED:
                    return true;
                case FAILED:
                    return false;
                default:
                    // wait for next change unless interrupted
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTextEncoding() {
        return textEncoding;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFirmwareVersion() {
        if (this.satelModule == null) {
            return null;
        }
        return this.satelModule.getIntegraVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUserCode(String userCode) {
        this.userCodeOverride = userCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetUserCode() {
        this.userCodeOverride = null;
    }

}
