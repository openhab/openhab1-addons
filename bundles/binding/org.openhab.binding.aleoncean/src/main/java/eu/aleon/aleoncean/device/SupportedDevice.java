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
package eu.aleon.aleoncean.device;

import java.util.NavigableMap;
import java.util.TreeMap;

import eu.aleon.aleoncean.device.local.LocalDeviceEEPA53808CMD02;
import eu.aleon.aleoncean.device.local.LocalDeviceEEPF60201;
import eu.aleon.aleoncean.device.local.LocalDeviceEEPF60202;
import eu.aleon.aleoncean.device.remote.RemoteDeviceEEPA50205;
import eu.aleon.aleoncean.device.remote.RemoteDeviceEEPA50401;
import eu.aleon.aleoncean.device.remote.RemoteDeviceEEPA50402;
import eu.aleon.aleoncean.device.remote.RemoteDeviceEEPA50802;
import eu.aleon.aleoncean.device.remote.RemoteDeviceEEPA51201;
import eu.aleon.aleoncean.device.remote.RemoteDeviceEEPA52001;
import eu.aleon.aleoncean.device.remote.RemoteDeviceEEPD20108;
import eu.aleon.aleoncean.device.remote.RemoteDeviceEEPD50001;
import eu.aleon.aleoncean.device.remote.RemoteDeviceEEPF60201;
import eu.aleon.aleoncean.device.remote.RemoteDeviceEEPF60202;
import eu.aleon.aleoncean.device.remote.RemoteDeviceEEPF61000;
import eu.aleon.aleoncean.device.remote.RemoteDeviceEEPF61001;

/**
 * The format of the identifier is defined as:
 * RD (remote device) or LD (local device) _ EEP (separator is a dash character).
 * So, for example LD_F6-02-01, RD_D2-01-08, ...
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public enum SupportedDevice {
    LD_A53808CMD2("LD_A5-38-08_CMD02", LocalDeviceEEPA53808CMD02.class),
    LD_F60201("LD_F6-02-01", LocalDeviceEEPF60201.class),
    LD_F60202("LD_F6-02-02", LocalDeviceEEPF60202.class),
    RD_A50205("RD_A5-02-05", RemoteDeviceEEPA50205.class),
    RD_A50401("RD_A5-04-01", RemoteDeviceEEPA50401.class),
    RD_A50402("RD_A5-04-02", RemoteDeviceEEPA50402.class),
    RD_A50802("RD_A5-08-02", RemoteDeviceEEPA50802.class),
    RD_A51201("RD_A5-12-01", RemoteDeviceEEPA51201.class),
    RD_A52001("RD_A5-20-01", RemoteDeviceEEPA52001.class),
    RD_D20108("RD_D2-01-08", RemoteDeviceEEPD20108.class),
    RD_D50001("RD_D5-00-01", RemoteDeviceEEPD50001.class),
    RD_F60201("RD_F6-02-01", RemoteDeviceEEPF60201.class),
    RD_F60202("RD_F6-02-02", RemoteDeviceEEPF60202.class),
    RD_F61000("RD_F6-10-00", RemoteDeviceEEPF61000.class),
    RD_F61001("RD_F6-10-01", RemoteDeviceEEPF61001.class);

    private final String ident;
    private final Class<? extends StandardDevice> clazz;

    private SupportedDevice(final String ident, final Class<? extends StandardDevice> clazz) {
        this.ident = ident;
        this.clazz = clazz;
    }

    private static final NavigableMap<String, Class<? extends StandardDevice>> IDENT_TO_CLASS_MAPPING;

    static {
        IDENT_TO_CLASS_MAPPING = new TreeMap<>();
        for (final SupportedDevice dev : SupportedDevice.values()) {
            IDENT_TO_CLASS_MAPPING.put(dev.ident, dev.clazz);
        }
    }

    public static Class<? extends StandardDevice> getClassForIdent(final String ident) {
        return IDENT_TO_CLASS_MAPPING.get(ident);
    }

    public static boolean containsIdent(final String ident) {
        return IDENT_TO_CLASS_MAPPING.containsKey(ident);
    }
}
