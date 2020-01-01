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
package org.openhab.binding.maxcube.internal.message;

import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

/**
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public class ConfigurationTest {

    public final String rawData = "C:003508,0gA1CAEBFP9JRVEwMTA5MTI1KCg9CQcoAzAM/wBESFUIRSBFIEUgRSBFIEUgRSBFIEUgRSBFIERIVQhFIEUgRSBFIEUgRSBFIEUgRSBFIEUgREhUbETMVRRFIEUgRSBFIEUgRSBFIEUgRSBESFRsRMxVFEUgRSBFIEUgRSBFIEUgRSBFIERIUmxEzFUURSBFIEUgRSBFIEUgRSBFIEUgREhUbETMVRRFIEUgRSBFIEUgRSBFIEUgRSBESFRsRMxVFEUgRSBFIEUgRSBFIEUgRSBFIA==";

    private C_Message c_message = null;
    private Configuration configuration = null;

    @Before
    public void Before() {
        c_message = new C_Message(rawData);
        configuration = Configuration.create(c_message);
    }

    @Test
    public void createTest() {
        Assert.assertNotNull(configuration);
    }

    @Test
    public void getRfAddressTest() {
        String rfAddress = configuration.getRFAddress();

        Assert.assertEquals("003508", rfAddress);
    }

    @Test
    public void getDeviceTypeTest() {

        DeviceType deviceType = configuration.getDeviceType();

        Assert.assertEquals(DeviceType.HeatingThermostat, deviceType);
    }

    @Test
    public void getSerialNumberTest() {
        String serialNumber = configuration.getSerialNumber();

        Assert.assertEquals("IEQ0109125", serialNumber);
    }
}
