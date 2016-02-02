package org.openhab.io.transport.cul.internal.network;

import org.openhab.io.transport.cul.CULMode;
import org.openhab.io.transport.cul.internal.CULConfig;

/**
 * Configuration for network device handler implementation.
 *
 * @author Patrick Ruckstuhl
 * @since 1.9.0
 */
public class CULNetworkConfig extends CULConfig {

    public CULNetworkConfig(String deviceType, String deviceAddress, CULMode mode) {
        super(deviceType, deviceAddress, mode);
    }

}
