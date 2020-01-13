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
package org.openhab.binding.netatmo.internal.camera;

import static junit.framework.Assert.*;

import java.util.List;

import org.junit.Test;
import org.openhab.binding.netatmo.internal.camera.GetHomeDataResponse.Camera;
import org.openhab.binding.netatmo.internal.camera.GetHomeDataResponse.Event;
import org.openhab.binding.netatmo.internal.camera.GetHomeDataResponse.Face;
import org.openhab.binding.netatmo.internal.camera.GetHomeDataResponse.Home;
import org.openhab.binding.netatmo.internal.camera.GetHomeDataResponse.Person;
import org.openhab.binding.netatmo.internal.camera.GetHomeDataResponse.Place;
import org.openhab.binding.netatmo.internal.camera.GetHomeDataResponse.Snapshot;
import org.openhab.binding.netatmo.internal.camera.GetHomeDataResponse.User;

/**
 * @author Ing. Peter Weiss
 * @since 1.9.0
 */
public class GetHomeDataTest {

    protected static final String ACCESS_TOKEN = "000000000000000000000000|11111111111111111111111111111111";

    public static GetHomeDataRequestStub createRequest(final String resource) throws Exception {
        return new GetHomeDataRequestStub(resource);
    }

    @Test
    public void testError() throws Exception {
        final GetHomeDataRequestStub request = createRequest("/error-2.json");
        final GetHomeDataResponse response = request.execute();

        assertTrue(response.isError());
    }

    @Test
    public void testSuccess() throws Exception {
        final GetHomeDataRequestStub request = createRequest("/gethomedata.json");
        final GetHomeDataResponse response = request.execute();

        assertFalse(response.isError());
        assertEquals("access_token=" + ACCESS_TOKEN, request.getContent());
        assertEquals("ok", response.getStatus());

        List<Home> myHomes = response.getHomes();
        assertEquals(1, myHomes.size());
        assertEquals(myHomes.get(0).getName(), "Home Tom");

        Place myPlace = myHomes.get(0).getPlace();
        assertEquals(myPlace.getTimezone(), "Europe/Stockholm");

        List<Person> myPersons = myHomes.get(0).getPersons();
        assertEquals(4, myPersons.size());
        assertEquals("13e843a4-2bcb-4d52-855c-1c5570663f76", myPersons.get(2).getId());
        assertEquals("Tom", myPersons.get(3).getPseudo());

        Face myFace = myPersons.get(0).getFace();
        assertEquals("55e598250a6e6e6170cf7cfb", myFace.getId());

        List<Event> myEvents = myHomes.get(0).getEvents();
        assertEquals(1, myEvents.size());
        assertEquals("70:ee:50:13:8b:35", myEvents.get(0).getCamera_id());

        Snapshot mySnapshot = myEvents.get(0).getSnapshot();
        assertEquals("8bbbe58c7b4fba061e8c29c2b7a57e6d16956b06c6ebd992e01267e755f7df72", mySnapshot.getKey());

        List<Camera> myCameras = myHomes.get(0).getCameras();
        assertEquals(2, myCameras.size());
        assertEquals("disconnected", myCameras.get(1).getStatus());

        User myUsr = response.getUser();
        assertEquals("en-US", myUsr.getLang());

    }

}
