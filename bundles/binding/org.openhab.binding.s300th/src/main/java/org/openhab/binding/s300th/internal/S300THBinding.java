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
package org.openhab.binding.s300th.internal;

import java.util.Dictionary;

import org.openhab.binding.s300th.S300THBindingProvider;
import org.openhab.binding.s300th.internal.S300THGenericBindingProvider.S300THBindingConfig;
import org.openhab.binding.s300th.internal.S300THGenericBindingProvider.S300THBindingConfig.Datapoint;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.io.transport.cul.CULLifecycleListenerListenerRegisterer;
import org.openhab.io.transport.cul.CULLifecycleManager;
import org.openhab.io.transport.cul.CULListener;
import org.openhab.io.transport.cul.CULMode;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class S300THBinding extends AbstractBinding<S300THBindingProvider>implements ManagedService, CULListener {

    private static final Logger logger = LoggerFactory.getLogger(S300THBinding.class);

    private final static String KS_300_ADDRESS = "ks300";

    private final CULLifecycleManager culHandlerLifecycle;

    public S300THBinding() {
        culHandlerLifecycle = new CULLifecycleManager(CULMode.SLOW_RF,
                new CULLifecycleListenerListenerRegisterer(this));
    }

    @Override
    public void activate() {
        culHandlerLifecycle.open();
    }

    @Override
    public void deactivate() {
        culHandlerLifecycle.close();
    }

    protected void addBindingProvider(S300THBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(S300THBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        culHandlerLifecycle.config(config);
    }

    @Override
    public void dataReceived(String data) {
        if (data.startsWith("K")) {
            int firstByte = Integer.parseInt(data.substring(1, 2), 16);
            int typByte = Integer.parseInt(data.substring(2, 3), 16) & 7;
            int sfirstByte = firstByte & 7;

            if (sfirstByte == 7) {
                logger.debug("Received WS7000 message, but parsing for WS7000 is not implemented");
                // TODO parse different sensors from WS7000 (?)
            } else {
                if (data.length() > 8 && data.length() < 13) {
                    // S300TH default size = 9 characters
                    parseS300THData(data);
                } else if (data.length() > 14 && data.length() < 20) {
                    // KS300 default size = 15 characters.
                    // sometime we got values with more characters.
                    parseKS300Data(data);
                } else {
                    logger.warn("Received unparseable message: " + data);
                }
            }
        }
    }

    /**
     * Parse KS 300 data
     *
     * @param data
     */
    private void parseKS300Data(String data) {
        // TODO parse address and other bytes
        int rainValue = ParseUtils.parseKS300RainCounter(data);
        double windValue = ParseUtils.parseKS300Wind(data);
        double humidity = ParseUtils.parseKS300Humidity(data);
        double temperature = ParseUtils.parseTemperature(data);
        boolean isRaining = ParseUtils.isKS300Raining(data);

        logger.debug("Received data '" + data + "' from device with address ks300 : temperature: " + temperature
                + " humidity: " + humidity + " wind: " + windValue + " rain: " + rainValue + " isRain: " + isRaining);

        for (Datapoint datapoint : Datapoint.values()) {
            S300THBindingConfig config = findConfig(KS_300_ADDRESS, datapoint);
            if (config == null) {
                continue;
            }
            double value = 0.0;
            switch (datapoint) {
                case TEMPERATURE:
                    value = temperature;
                    break;
                case HUMIDITY:
                    value = humidity;
                    break;
                case WIND:
                    value = windValue;
                    break;
                case RAIN:
                    value = rainValue;
                    break;
                case IS_RAINING:
                    value = isRaining ? 1 : 0;
                    break;
            }
            updateItem(config.item, value);
        }
    }

    /**
     * Parse S300TH data
     *
     * @param data
     */
    private void parseS300THData(String data) {
        String address = ParseUtils.parseS300THAddress(data);
        double temperature = ParseUtils.parseTemperature(data);
        double humidity = ParseUtils.parseS300THHumidity(data);
        logger.debug("Received data '" + data + "' from device with address " + address + " : temperature: "
                + temperature + " humidity: " + humidity);

        S300THBindingConfig temperatureConfig = findConfig(address, Datapoint.TEMPERATURE);
        if (temperatureConfig != null) {
            updateItem(temperatureConfig.item, temperature);
        }
        S300THBindingConfig humidityConfig = findConfig(address, Datapoint.HUMIDITY);
        if (humidityConfig != null) {
            updateItem(humidityConfig.item, humidity);
        }
    }

    private void updateItem(Item item, double value) {
        DecimalType type = new DecimalType(value);
        eventPublisher.postUpdate(item.getName(), type);
    }

    private S300THBindingConfig findConfig(String address, Datapoint datapoint) {
        for (S300THBindingProvider provider : this.providers) {
            S300THBindingConfig config = provider.getBindingConfigForAddressAndDatapoint(address, datapoint);
            if (config != null) {
                return config;
            }
        }
        return null;
    }

    @Override
    public void error(Exception e) {
        logger.error("Received error from CUL instead of data", e);

    }

}
