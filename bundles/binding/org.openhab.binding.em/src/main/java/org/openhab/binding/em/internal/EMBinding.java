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
package org.openhab.binding.em.internal;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.em.EMBindingProvider;
import org.openhab.binding.em.internal.EMBindingConfig.Datapoint;
import org.openhab.binding.em.internal.EMBindingConfig.EMType;
import org.openhab.core.binding.AbstractBinding;
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
public class EMBinding extends AbstractBinding<EMBindingProvider>implements ManagedService, CULListener {

    private static final Logger logger = LoggerFactory.getLogger(EMBinding.class);

    private final CULLifecycleManager culHandlerLifecycle;

    private Map<String, Integer> counterMap = new HashMap<String, Integer>();

    public EMBinding() {
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

    protected void addBindingProvider(EMBindingProvider bindingProvider) {
        super.addBindingProvider(bindingProvider);
    }

    protected void removeBindingProvider(EMBindingProvider bindingProvider) {
        super.removeBindingProvider(bindingProvider);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        culHandlerLifecycle.config(config);
    }

    @Override
    public void dataReceived(String data) {
        if (!StringUtils.isEmpty(data) && data.startsWith("E")) {
            parseDataLine(data);
        }
    }

    /**
     * Parse the received line of data and create updates for configured items
     *
     * @param data
     */
    private void parseDataLine(String data) {
        String address = ParsingUtils.parseAddress(data);
        if (!checkNewMessage(address, ParsingUtils.parseCounter(data))) {
            logger.warn("Received message from " + address + " more than once");
            return;
        }
        EMType type = ParsingUtils.parseType(data);
        EMBindingConfig emConfig = findConfig(type, address, Datapoint.CUMULATED_VALUE);
        if (emConfig != null) {
            updateItem(emConfig, ParsingUtils.parseCumulatedValue(data));
        }
        if (data.length() > 10) {
            emConfig = findConfig(type, address, Datapoint.LAST_VALUE);
            if (emConfig != null) {
                updateItem(emConfig, ParsingUtils.parseCurrentValue(data));
            }
            emConfig = findConfig(type, address, Datapoint.TOP_VALUE);
            if (emConfig != null) {
                updateItem(emConfig, ParsingUtils.parsePeakValue(data));
            }
        }
    }

    /**
     * Update an item given in the configuration with the given value multiplied
     * by the correction factor
     *
     * @param config
     * @param value
     */
    private void updateItem(EMBindingConfig config, int value) {
        DecimalType status = new DecimalType(value * config.getCorrectionFactor());
        eventPublisher.postUpdate(config.getItem().getName(), status);
    }

    /**
     * Check if we have received a new message to not consume repeated messages
     *
     * @param address
     * @param counter
     * @return
     */
    private boolean checkNewMessage(String address, int counter) {
        Integer lastCounter = counterMap.get(address);
        if (lastCounter == null) {
            lastCounter = -1;
        }
        if (counter > lastCounter) {
            return true;
        }
        return false;
    }

    private EMBindingConfig findConfig(EMType type, String address, Datapoint datapoint) {
        EMBindingConfig emConfig = null;
        for (EMBindingProvider provider : this.providers) {
            emConfig = provider.getConfigByTypeAndAddressAndDatapoint(type, address, datapoint);
            if (emConfig != null) {
                return emConfig;
            }
        }
        return null;
    }

    @Override
    public void error(Exception e) {
        logger.error("Exception instead of new data from CUL", e);
    }

}
