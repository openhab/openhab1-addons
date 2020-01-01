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
package org.openhab.binding.netatmo.internal.weather;

import static junit.framework.Assert.*;
import static org.openhab.binding.netatmo.internal.weather.GetStationsDataRequestStub.*;

import java.util.List;

import org.junit.Test;
import org.openhab.binding.netatmo.internal.weather.GetStationsDataResponse;
import org.openhab.binding.netatmo.internal.weather.GetStationsDataResponse.Device;
import org.openhab.binding.netatmo.internal.weather.GetStationsDataResponse.Module;
import org.openhab.binding.netatmo.internal.weather.GetStationsDataResponse.Place;

/**
 * @author Rob Nielsen
 * @author Ing. Peter Weiss
 * @since 1.8.0
 */
public class GetStationsDataTest {

    @Test
    public void testError() throws Exception {
        final GetStationsDataRequestStub request = createRequest("/error-2.json");
        final GetStationsDataResponse response = request.execute();

        assertTrue(response.isError());
    }

    @Test
    public void testSuccess() throws Exception {
        final GetStationsDataRequestStub request = createRequest("/getstationsdata.json");
        final GetStationsDataResponse response = request.execute();

        assertFalse(response.isError());
        assertEquals("access_token=" + ACCESS_TOKEN, request.getContent());
        assertEquals("ok", response.getStatus());

        final List<Device> devices = response.getDevices();
        assertEquals(2, devices.size());

        final Device device1 = devices.get(0);

        final List<Module> modules1 = device1.getModules();
        assertEquals(4, modules1.size());

        final Device device2 = devices.get(1);

        final List<Module> modules2 = device2.getModules();
        assertEquals(4, modules2.size());

        final Place place1 = device1.getPlace();
        assertEquals(30.478512648583, place1.getAltitude());

        final Place place2 = device2.getPlace();
        assertEquals(150.0, place2.getAltitude());
    }

}
