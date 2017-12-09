/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fs20.internal;

import java.util.Dictionary;

import org.openhab.binding.fs20.FS20BindingConfig;
import org.openhab.binding.fs20.FS20BindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.types.Command;
import org.openhab.io.transport.cul.CULCommunicationException;
import org.openhab.io.transport.cul.CULLifecycleListenerListenerRegisterer;
import org.openhab.io.transport.cul.CULLifecycleManager;
import org.openhab.io.transport.cul.CULListener;
import org.openhab.io.transport.cul.CULMode;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the communcation between openHAB and FS20 devices. Via
 * RF received updates are received directly, there is no polling.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class FS20Binding extends AbstractBinding<FS20BindingProvider>implements ManagedService, CULListener {

    private static final Logger logger = LoggerFactory.getLogger(FS20Binding.class);

    private final CULLifecycleManager culHandlerLifecycle;

    public FS20Binding() {
        culHandlerLifecycle = new CULLifecycleManager(CULMode.SLOW_RF,
                new CULLifecycleListenerListenerRegisterer(this));
    }

    @Override
    public void activate() {
        logger.debug("Activating FS20 binding");
        culHandlerLifecycle.open();
    }

    @Override
    public void deactivate() {
        logger.debug("Deactivating FS20 binding");
        culHandlerLifecycle.close();
    }

    /**
     * @{inheritDoc
     */
    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        FS20BindingConfig bindingConfig = null;
        for (FS20BindingProvider provider : super.providers) {
            bindingConfig = provider.getConfigForItemName(itemName);
            if (bindingConfig != null) {
                break;
            }
        }
        if (bindingConfig != null) {
            logger.debug("Received command " + command.toString() + " for item " + itemName);
            try {
                FS20Command fs20Command = FS20CommandHelper.convertHABCommandToFS20Command(command);
                culHandlerLifecycle.getCul().send("F" + bindingConfig.getAddress() + fs20Command.getHexValue());
            } catch (CULCommunicationException e) {
                logger.error("An exception occurred while sending a command", e);
            }
        }
    }

    protected void addBindingProvider(FS20BindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(FS20BindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        logger.debug("Received new config");
        culHandlerLifecycle.config(config);
    }

    @Override
    public void dataReceived(String data) {
        // It is possible that we see here messages of other protocols
        if (data.startsWith("F")) {
            logger.debug("Received FS20 message: " + data);
            handleReceivedMessage(data);
        }

    }

    private void handleReceivedMessage(String message) {
        String houseCode = (message.substring(1, 5));
        String address = (message.substring(5, 7));
        String command = message.substring(7, 9);
        String fullAddress = houseCode + address;
        FS20BindingConfig config = null;
        for (FS20BindingProvider provider : providers) {
            config = provider.getConfigForAddress(fullAddress);
            if (config != null) {
                break;
            }
        }
        if (config != null) {
            FS20Command fs20Command = FS20Command.getFromHexValue(command);
            logger.debug("Received command " + fs20Command.toString() + " for device " + config.getAddress());
            eventPublisher.postUpdate(config.getItem().getName(),
                    FS20CommandHelper.getStateFromFS20Command(fs20Command));
        } else {
            logger.debug("Received message for unknown device " + fullAddress);
        }
    }

    @Override
    public void error(Exception e) {
        logger.error("Error while communicating with CUL", e);
    }

}
