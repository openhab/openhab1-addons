/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package org.openhab.binding.aleoncean.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.aleoncean.AleonceanBindingProvider;
import org.openhab.binding.aleoncean.internal.worker.Worker;
import org.openhab.binding.aleoncean.internal.worker.WorkerItemBindingChanged;
import org.openhab.binding.aleoncean.internal.worker.WorkerItemESP3Port;
import org.openhab.binding.aleoncean.internal.worker.WorkerItemReceivedCommand;
import org.openhab.binding.aleoncean.internal.worker.WorkerItemReceivedState;
import org.openhab.binding.aleoncean.internal.worker.WorkerItemSetBaseId;
import org.openhab.binding.aleoncean.internal.worker.WorkerItemSetEventPublisher;
import org.openhab.binding.aleoncean.internal.worker.WorkerReply;
import org.openhab.binding.aleoncean.internal.worker.WorkerReplyCode;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.aleon.aleoncean.packet.EnOceanId;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 * @since 1.6.0
 */
public class AleonceanBinding extends AbstractBinding<AleonceanBindingProvider> implements ManagedService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AleonceanBinding.class);

    private static final String CONFIG_PORT = "port";

    private static final String CONFIG_BASEID = "baseid";

    private final Worker worker;

    public AleonceanBinding() {
        this.worker = new Worker();
    }

    private void notifyWorkerAllBindingsChanged(final AleonceanGenericBindingProvider provider) {
        for (final String itemName : provider.getItemNames()) {
            final AleonceanBindingConfig config = provider.getBindingConfig(itemName);

            final WorkerReply reply = worker.addAndWaitForReply(new WorkerItemBindingChanged(itemName, config), 1,
                    TimeUnit.MINUTES);
            if (reply.getReplyCode() != WorkerReplyCode.OK && reply.getReplyCode() != WorkerReplyCode.NOT_RUNNING) {
                LOGGER.warn("Something went wrong ({}) on binding changed notification (item: {})",
                        reply.getReplyCode(), itemName);
            }
        }
    }

    @Override
    public void activate() {
        worker.start();
        setEventPublisher(eventPublisher);
        for (final BindingProvider p : providers) {
            if (p instanceof AleonceanGenericBindingProvider) {
                notifyWorkerAllBindingsChanged((AleonceanGenericBindingProvider) p);
            }
        }
    }

    @Override
    public void deactivate() {
        // deallocate resources here that are no longer needed and
        // should be reset when activating this binding again
        worker.stop();
    }

    @Override
    protected void internalReceiveCommand(final String itemName, final Command command) {
        // the code being executed when a command was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.
        LOGGER.debug("internalReceiveCommand() is called!");

        final WorkerReply reply = worker.addAndWaitForReply(new WorkerItemReceivedCommand(itemName, command), 1,
                TimeUnit.MINUTES);
        if (reply.getReplyCode() != WorkerReplyCode.OK) {
            LOGGER.warn("Something went wrong ({}) on internal received command.", reply.getReplyCode());
        }
    }

    @Override
    protected void internalReceiveUpdate(final String itemName, final State newState) {
        // the code being executed when a state was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.
        LOGGER.debug("internalReceiveCommand() is called!");

        final WorkerReply reply = worker.addAndWaitForReply(new WorkerItemReceivedState(itemName, newState), 1,
                TimeUnit.MINUTES);
        if (reply.getReplyCode() != WorkerReplyCode.OK) {
            LOGGER.warn("Something went wrong ({}) on internal received command.", reply.getReplyCode());
        }
    }

    @Override
    public void updated(final Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {
            final Enumeration<String> keys = config.keys();
            while (keys.hasMoreElements()) {
                final String key = keys.nextElement();
                final Object val = config.get(key);
                LOGGER.debug("Parse config: {}; {}", key, val);
            }

            WorkerReply reply;

            /*
             * Set the ESP3 port.
             */
            final String valuePort = (String) config.get(CONFIG_PORT);
            if (StringUtils.isBlank(valuePort)) {
                LOGGER.warn("You need to setup the port that should be used for the ESP3 interface.");
                return;
            }

            reply = worker.addAndWaitForReply(new WorkerItemESP3Port(valuePort), 1, TimeUnit.MINUTES);
            if (reply.getReplyCode() != WorkerReplyCode.OK) {
                LOGGER.warn("Something went wrong ({}) enqueue set ESP3 port.", reply.getReplyCode());
                return;
            }

            /*
             * Set the base ID if entry is available.
             */
            final String valueBaseId = (String) config.get(CONFIG_BASEID);
            if (StringUtils.isNotBlank(valueBaseId)) {
                try {
                    final EnOceanId baseId = new EnOceanId(valueBaseId);
                    reply = worker.addAndWaitForReply(new WorkerItemSetBaseId(baseId), 1, TimeUnit.MINUTES);
                    if (reply.getReplyCode() != WorkerReplyCode.OK) {
                        LOGGER.warn("Something went wrong ({}) set base id.", reply.getReplyCode());
                        return;
                    }
                } catch (final IllegalArgumentException ex) {
                    LOGGER.warn("Something went wrong parsing the base id.", ex);
                    return;
                }
            }

            LOGGER.debug("The configuration was successfully updated.");

            // Seems only to be used by AbstractActiveBinding and not by AbstractBinding.
            // setProperlyConfigured(true);
        } else {
            LOGGER.warn(
                    "You need to set at least the port for ESP3 hardware if you want to use the aleoncean binding.");
        }
    }

    public void addBindingProvider(final AleonceanBindingProvider provider) {
        LOGGER.debug("addBindingProvider({})", provider);
        super.addBindingProvider(provider);
        LOGGER.debug("addBindingProvider(...) done");
    }

    public void removeBindingProvider(final AleonceanBindingProvider provider) {
        LOGGER.debug("removeBindingProvider({})", provider);
        super.removeBindingProvider(provider);
        LOGGER.debug("removeBindingProvider(...) done");
    }

    @Override
    public void bindingChanged(final BindingProvider provider, final String itemName) {

        /*
         * The binding of an item was changed / removed / added.
         */
        LOGGER.debug("bindingChanged({},{})", provider, itemName);
        super.bindingChanged(provider, itemName);
        LOGGER.debug("bindingChanged(...) done");

        if (provider instanceof AleonceanGenericBindingProvider) {
            final AleonceanGenericBindingProvider prov = (AleonceanGenericBindingProvider) provider;
            final AleonceanBindingConfig config = prov.getBindingConfig(itemName);

            final WorkerReply reply = worker.addAndWaitForReply(new WorkerItemBindingChanged(itemName, config), 1,
                    TimeUnit.MINUTES);
            if (reply.getReplyCode() != WorkerReplyCode.OK && reply.getReplyCode() != WorkerReplyCode.NOT_RUNNING) {
                LOGGER.warn("Something went wrong ({}) on binding changed notification.", reply.getReplyCode());
            }
        } else {
            LOGGER.warn("Binding provider is not an instance of AleonceanGenericBindingProvider.");
        }
    }

    @Override
    public void allBindingsChanged(final BindingProvider provider) {
        LOGGER.debug("allBindingsChanged({})", provider);
        super.allBindingsChanged(provider);
        notifyWorkerAllBindingsChanged((AleonceanGenericBindingProvider) provider);
        LOGGER.debug("allBindingsChanged(...) done");
    }

    @Override
    public void setEventPublisher(final EventPublisher eventPublisher) {
        super.setEventPublisher(eventPublisher);

        final WorkerReply reply = worker.addAndWaitForReply(new WorkerItemSetEventPublisher(eventPublisher, true), 1,
                TimeUnit.MINUTES);
        if (reply.getReplyCode() != WorkerReplyCode.OK && reply.getReplyCode() != WorkerReplyCode.NOT_RUNNING) {
            LOGGER.warn("Something went wrong ({}) set event publisher.", reply.getReplyCode());
        }
    }

    @Override
    public void unsetEventPublisher(final EventPublisher eventPublisher) {
        super.unsetEventPublisher(eventPublisher);

        final WorkerReply reply = worker.addAndWaitForReply(new WorkerItemSetEventPublisher(eventPublisher, false), 1,
                TimeUnit.MINUTES);
        if (reply.getReplyCode() != WorkerReplyCode.OK && reply.getReplyCode() != WorkerReplyCode.NOT_RUNNING) {
            LOGGER.warn("Something went wrong ({}) unset event publisher.", reply.getReplyCode());
        }
    }

}
