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
package org.openhab.binding.netatmo.internal.authentication;

import static junit.framework.Assert.*;
import static org.openhab.binding.netatmo.internal.authentication.RefreshTokenRequestStub.*;

import org.junit.Test;
import org.openhab.binding.netatmo.internal.messages.NetatmoError;

/**
 * @author Andreas Brenk
 * @since 1.4.0
 */
public class RefreshTokenTest {

    @Test
    public void testError() throws Exception {
        final RefreshTokenRequestStub request = createRequest("/error-2.json");
        final RefreshTokenResponse response = request.execute();

        assertTrue(response.isError());

        final NetatmoError error = response.getError();

        assertNotNull(error);
        assertEquals(2, error.getCode());
        assertEquals("Invalid access token", error.getMessage());
    }

    @Test
    public void testSuccess() throws Exception {
        final RefreshTokenRequestStub request = createRequest("/token.json");
        final RefreshTokenResponse response = request.execute();

        assertFalse(response.isError());
        assertNull(response.getError());

        assertEquals("grant_type=refresh_token&refresh_token=" + REFRESH_TOKEN + "&client_id=" + CLIENT_ID
                + "&client_secret=" + CLIENT_SECRET + "&scope=read_station read_camera", request.getContent());
        assertEquals("000000000000000000000000|11111111111111111111111111111111", response.getAccessToken());
    }
}
