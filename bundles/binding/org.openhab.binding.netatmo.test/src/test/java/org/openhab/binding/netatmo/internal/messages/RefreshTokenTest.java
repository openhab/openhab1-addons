/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.messages;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.openhab.binding.netatmo.internal.messages.RefreshTokenRequestStub.CLIENT_ID;
import static org.openhab.binding.netatmo.internal.messages.RefreshTokenRequestStub.CLIENT_SECRET;
import static org.openhab.binding.netatmo.internal.messages.RefreshTokenRequestStub.REFRESH_TOKEN;
import static org.openhab.binding.netatmo.internal.messages.RefreshTokenRequestStub.createRequest;

import org.junit.Test;

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

		assertEquals(
				"grant_type=refresh_token&refresh_token=" + REFRESH_TOKEN
						+ "&client_id=" + CLIENT_ID + "&client_secret="
						+ CLIENT_SECRET, request.getContent());
		assertEquals(
				"000000000000000000000000|11111111111111111111111111111111",
				response.getAccessToken());
	}
}
