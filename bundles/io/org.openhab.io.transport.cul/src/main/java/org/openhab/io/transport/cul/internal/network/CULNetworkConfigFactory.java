/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul.internal.network;

import java.util.Dictionary;

import org.openhab.io.transport.cul.CULMode;
import org.openhab.io.transport.cul.internal.CULConfig;
import org.openhab.io.transport.cul.internal.CULConfigFactory;
import org.osgi.service.cm.ConfigurationException;

/**
 * Configuration factory for network device handler implementation.
 *
 * @author Patrick Ruckstuhl
 * @since 1.9.0
 */
public class CULNetworkConfigFactory implements CULConfigFactory {

    @Override
    public CULConfig create(String deviceType, String deviceAddress, CULMode mode, Dictionary<String, ?> config)
            throws ConfigurationException {
        return new CULNetworkConfig(deviceType, deviceAddress, mode);
    }
}