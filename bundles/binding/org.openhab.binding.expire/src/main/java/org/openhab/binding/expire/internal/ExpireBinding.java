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
package org.openhab.binding.expire.internal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openhab.binding.expire.ExpireBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This binding monitors state changes and sets the state to "Undefined" (or any other configured expired state)
 * if not state change occurs within the configured time
 *
 * @author Michael Wyraz
 * @author John Cocula - minor refactoring
 * @since 1.9.0
 */
public class ExpireBinding extends AbstractActiveBinding<ExpireBindingProvider> {

    private static final Logger logger = LoggerFactory.getLogger(ExpireBinding.class);

    /**
     * The refresh interval is used to check if any of the bound items is expired.
     * One second should be fine for all use cases, so it's final and cannot be configured.
     */
    private static final long refreshInterval = 1000;

    /**
     * Mapping of item names to future timestamps (in milliseconds) to expire them.
     */
    private Map<String, Long> itemExpireMap = new ConcurrentHashMap<String, Long>();

    public ExpireBinding() {
    }

    protected void addBindingProvider(ExpireBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(ExpireBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * Called by the SCR to activate the component with its configuration read from CAS
     *
     * @param bundleContext BundleContext of the Bundle that defines this component
     * @param configuration Configuration properties for this component obtained from the ConfigAdmin service
     */
    public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
        setProperlyConfigured(true);
    }

    /**
     * Called by the SCR when the configuration of a binding has been changed through the ConfigAdmin service.
     *
     * @param configuration Updated configuration properties
     */
    public void modified(final Map<String, Object> configuration) {
        // update the internal configuration accordingly
    }

    /**
     * Called by the SCR to deactivate the component when either the configuration is removed or
     * mandatory references are no longer satisfied or the component has simply been stopped.
     *
     * @param reason Reason code for the deactivation:<br>
     *            <ul>
     *            <li>0 – Unspecified
     *            <li>1 – The component was disabled
     *            <li>2 – A reference became unsatisfied
     *            <li>3 – A configuration was changed
     *            <li>4 – A configuration was deleted
     *            <li>5 – The component was disposed
     *            <li>6 – The bundle was stopped
     *            </ul>
     */
    public void deactivate(final int reason) {
        // deallocate resources here that are no longer needed and
        // should be reset when activating this binding again
        itemExpireMap.clear();
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
    protected String getName() {
        return "Expire Refresh Service";
    }

    private boolean isReadyToExpire(String itemName) {
        Long nextExpireTs = itemExpireMap.get(itemName);
        return (nextExpireTs != null) && (nextExpireTs <= System.currentTimeMillis());
    }

    private void expire(String itemName, ExpireBindingProvider provider) {
        itemExpireMap.remove(itemName); // disable expire trigger until next update or command

        Command expireCommand = provider.getExpireCommand(itemName);
        State expireState = provider.getExpireState(itemName);

        if (expireCommand != null) {
            logger.debug("Item {} received no command or update for {} - posting command '{}'", itemName,
                    provider.getDurationString(itemName), expireCommand);
            eventPublisher.postCommand(itemName, expireCommand);
        } else if (expireState != null) {
            logger.debug("Item {} received no command or update for {} - posting state '{}'", itemName,
                    provider.getDurationString(itemName), expireState);
            eventPublisher.postUpdate(itemName, expireState);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute() {
        logger.trace("Executing...");

        // only bother to iterate if there is any possible work to do
        if (!itemExpireMap.isEmpty()) {

            for (ExpireBindingProvider provider : providers) {
                for (String itemName : provider.getItemNames()) {
                    if (isReadyToExpire(itemName)) {
                        expire(itemName, provider);
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void internalReceiveCommand(final String itemName, final Command newCommand) {
        logger.trace("Received command '{}' for item {}", newCommand, itemName);
        for (ExpireBindingProvider provider : providers) {
            if (provider.providesBindingFor(itemName)) {

                Command expireCommand = provider.getExpireCommand(itemName);
                State expireState = provider.getExpireState(itemName);

                if ((expireCommand != null && expireCommand.equals(newCommand))
                        || (expireState != null && expireState.equals(newCommand))) {
                    // New command is expired command or state -> no further action needed
                    itemExpireMap.remove(itemName); // remove expire trigger until next update or command
                    logger.debug("Item {} received command '{}'; stopping any future expiration.", itemName,
                            newCommand);
                } else {
                    // New command is not the expired command or state, so add the trigger to the map
                    long duration = provider.getDuration(itemName);
                    itemExpireMap.put(itemName, System.currentTimeMillis() + duration);
                    logger.debug("Item {} will expire (with '{}' {}) in {} ms", itemName,
                            expireCommand == null ? expireState : expireCommand,
                            expireCommand == null ? "state" : "command", duration);
                }
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void internalReceiveUpdate(final String itemName, final State newState) {
        logger.trace("Received update '{}' for item {}", newState, itemName);
        for (ExpireBindingProvider provider : providers) {
            if (provider.providesBindingFor(itemName)) {

                Command expireCommand = provider.getExpireCommand(itemName);
                State expireState = provider.getExpireState(itemName);

                if ((expireCommand != null && expireCommand.equals(newState))
                        || (expireState != null && expireState.equals(newState))) {
                    // New state is expired command or state -> no further action needed
                    itemExpireMap.remove(itemName); // remove expire trigger until next update or command
                    logger.debug("Item {} received update '{}'; stopping any future expiration.", itemName, newState);
                } else {
                    // New state is not the expired command or state, so add the trigger to the map
                    long duration = provider.getDuration(itemName);
                    itemExpireMap.put(itemName, System.currentTimeMillis() + duration);
                    logger.debug("Item {} will expire (with '{}' {}) in {} ms", itemName,
                            expireCommand == null ? expireState : expireCommand,
                            expireCommand == null ? "state" : "command", duration);
                }
                break;
            }
        }
    }
}
