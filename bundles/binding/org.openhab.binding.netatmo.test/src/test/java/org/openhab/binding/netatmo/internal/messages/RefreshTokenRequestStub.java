/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.messages;

import static com.google.common.base.Charsets.UTF_8;

import java.io.IOException;
import java.net.URL;

import com.google.common.io.Resources;

/**
 * @author Andreas Brenk
 * @since 1.4.0
 */
public class RefreshTokenRequestStub extends RefreshTokenRequest {

	protected static final String CLIENT_ID = "000000000000000000000000";

	protected static final String CLIENT_SECRET = "11111111111111111111111111111111111";

	protected static final String REFRESH_TOKEN = "000000000000000000000000|22222222222222222222222222222222";

	public static RefreshTokenRequestStub createRequest(final String resource)
			throws Exception {
		return new RefreshTokenRequestStub(resource);
	}

	private final String response;

	private String content;

	private RefreshTokenRequestStub(final String response) throws Exception {
		super(CLIENT_ID, CLIENT_SECRET, REFRESH_TOKEN);

		final URL resource = getClass().getResource(response);

		if (resource == null) {
			throw new IOException("Resource '" + response + "' not found!");
		}

		this.response = Resources.toString(resource, UTF_8);
	}

	public String getContent() {
		return this.content;
	}

	@Override
	protected String executeQuery(final String content) {
		this.content = content;

		return this.response;
	}

}
