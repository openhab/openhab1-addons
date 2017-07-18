/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul;

import java.util.Dictionary;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.io.transport.cul.internal.CULConfig;
import org.openhab.io.transport.cul.internal.CULConfigFactory;
import org.openhab.io.transport.cul.internal.CULHandlerInternal;
import org.openhab.io.transport.cul.internal.CULManager;
import org.osgi.service.cm.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CULLifecycleManager {

    static final String KEY_DEVICE_NAME = "device";
    private static final Pattern DEVICE_PATTERN = Pattern.compile("(?<type>[^:]+):(?<address>.+)");
    private static final Logger logger = LoggerFactory.getLogger(CULLifecycleManager.class);

    private CULLifecycleListener listener;
    private CULManager culManager;

    private CULMode mode;
    private CULConfig config;
    private CULHandlerInternal<?> cul;

    public CULLifecycleManager(CULMode mode, CULLifecycleListener listener) {
        this.mode = mode;
        this.listener = listener;
        culManager = CULManager.getInstance();
    }

    // for testing only!
    CULLifecycleManager(CULMode mode, CULLifecycleListener listener, CULManager culManager, CULHandlerInternal<?> cul,
            CULConfig config) {
        this.mode = mode;
        this.listener = listener;
        this.culManager = culManager;
        this.cul = cul;
        this.config = config;
    }

    public void config(Dictionary<String, ?> allConfig) throws ConfigurationException {
        if (allConfig != null) {
            String deviceName = (String) allConfig.get(KEY_DEVICE_NAME);
            if (StringUtils.isEmpty(deviceName)) {
                throw new ConfigurationException(KEY_DEVICE_NAME, "The device name can't be empty");
            }

            Matcher deviceMatcher = DEVICE_PATTERN.matcher(deviceName);
            if (!deviceMatcher.matches()) {
                throw new ConfigurationException(KEY_DEVICE_NAME,
                        "Invalid device name '" + deviceName + "'. Must be 'DEVICETYPE:ADDRESS'");
            }

            String deviceType = deviceMatcher.group("type");
            CULConfigFactory configFactory = culManager.getConfigFactory(deviceType);
            if (configFactory == null) {
                throw new ConfigurationException(KEY_DEVICE_NAME, "Invalid device type '" + deviceType + "'");
            }

            String deviceAddress = deviceMatcher.group("address");
            config = configFactory.create(deviceType, deviceAddress, mode, allConfig);
            open();
        }
    }

    public void open() {
        if (config == null || (cul != null && config.equals(cul.getConfig()))) {
            logger.warn("CUL config is NULL, doing nothing");
            return;
        }

        close();
        try {
            cul = culManager.getOpenCULHandler(config);
        } catch (CULDeviceException e) {
            logger.warn("Can't open CUL", e);
        }

        try {
            listener.open(cul);
        } catch (CULCommunicationException e) {
            logger.warn("Can't start listener", e);
            cul = null;
        }
    }

    public void close() {
        if (cul != null) {
            listener.close(cul);
            culManager.close(cul);
            cul = null;
        }
    }

    public CULHandler getCul() {
        return cul;
    }

    public boolean isCulReady() {
        return cul != null;
    }
}
