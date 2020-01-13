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
package org.openhab.binding.hms.internal;

import java.util.Dictionary;

import org.openhab.binding.hms.HMSBindingProvider;
import org.openhab.binding.hms.internal.HMSGenericBindingProvider.HMSBindingConfig;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.io.transport.cul.CULLifecycleListenerListenerRegisterer;
import org.openhab.io.transport.cul.CULLifecycleManager;
import org.openhab.io.transport.cul.CULListener;
import org.openhab.io.transport.cul.CULMode;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class makes use of a CUL device in SLOW_RF mode and handles messages received via this channel.
 *
 * @author Thomas Urmann
 * @since 1.7.0
 */
public class HMSBinding extends AbstractBinding<HMSBindingProvider>implements ManagedService, CULListener {

    private static final Logger logger = LoggerFactory.getLogger(HMSBinding.class);

    private final CULLifecycleManager culHandlerLifecycle;

    public HMSBinding() {
        culHandlerLifecycle = new CULLifecycleManager(CULMode.SLOW_RF,
                new CULLifecycleListenerListenerRegisterer(this));
    }

    @Override
    public void activate() {
        culHandlerLifecycle.open();
    }

    @Override
    public void deactivate() {
        logger.debug("Deactivating HMS binding");
        culHandlerLifecycle.close();
    }

    @Override
    public void dataReceived(String data) {
        // It is possible that we see here messages of other protocols
        if (data.startsWith("H")) {
            handleHMSMessage(data);
        }
    }

    /**
     * Code <s1><s0><t1><t0><f0><t2><f2><f1>
     *
     * similar perl code is:
     * $v[0] = int(substr($val, 5, 1) . substr($val, 2, 2))/10;
     * $v[0] = -$v[0] if($status1 & 8);
     * $v[1] = int(substr($val, 6, 2) . substr($val, 4, 1))/10;
     * $v[2] = $batstr1;
     * $val = "T: $v[0]  H: $v[1]  Bat: $v[2]";
     */
    protected void handleHMSMessage(String data) {
        if (data.startsWith("H")) {
            String device = data.substring(1, 5);
            String woHeader = data.substring(5, data.length() - 1);

            String s1 = woHeader.substring(0, 1);

            String batteryStatus = "ok";
            boolean isEmpty = (s1.charAt(0) & 2) == 2;
            boolean isReplaced = (s1.charAt(0) & 4) == 4;
            if (isEmpty) {
                batteryStatus = "empty";
            } else if (isReplaced) {
                batteryStatus = "replaced";
            }

            double temperature = Integer.parseInt(woHeader.substring(5, 6) + woHeader.substring(2, 4)) / 10.0;
            boolean isNegative = (s1.charAt(0) & 8) == 8;
            if (isNegative) {
                temperature = -temperature;
            }
            double humidity = Integer.parseInt(woHeader.substring(6, 8) + woHeader.substring(4, 5)) / 10.0;

            logger.info("device: {}, T: {},\tH: {}, Bat.: {}", device, (!isNegative ? " " : "") + temperature, humidity,
                    batteryStatus);

            HMSBindingConfig temperatureConfig = findConfig(device, HMSBindingConfig.Datapoint.TEMPERATURE);
            if (temperatureConfig != null) {
                updateItem(temperatureConfig.item, temperature);
            }
            HMSBindingConfig humidityConfig = findConfig(device, HMSBindingConfig.Datapoint.HUMIDITY);
            if (humidityConfig != null) {
                updateItem(humidityConfig.item, humidity);
            }

        } else {
            logger.warn("Received unparseable message: " + data);
        }
    }

    private void updateItem(Item item, double value) {
        DecimalType type = new DecimalType(value);
        eventPublisher.postUpdate(item.getName(), type);
    }

    private HMSBindingConfig findConfig(String address, HMSBindingConfig.Datapoint datapoint) {
        for (HMSBindingProvider provider : this.providers) {
            HMSBindingConfig config = provider.getBindingConfigForAddressAndDatapoint(address, datapoint);
            if (config != null) {
                return config;
            }
        }
        return null;
    }

    @Override
    protected void internalReceiveCommand(String itemName, Command command) {
        // the code being executed when a command was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.
        logger.debug("internalReceiveCommand() is called!");
    }

    @Override
    protected void internalReceiveUpdate(String itemName, State newState) {
        // the code being executed when a state was sent on the openHAB
        // event bus goes here. This method is only called if one of the
        // BindingProviders provide a binding for the given 'itemName'.
        logger.debug("internalReceiveCommand() is called!");
    }

    protected void addBindingProvider(HMSBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(HMSBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        culHandlerLifecycle.config(config);
    }

    @Override
    public void error(Exception e) {
        logger.error("Received error from CUL instead fo data", e);
    }
}
