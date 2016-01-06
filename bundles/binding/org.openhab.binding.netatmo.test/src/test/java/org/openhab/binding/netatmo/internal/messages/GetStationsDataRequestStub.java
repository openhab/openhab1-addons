/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal.messages;

import java.io.IOException;
import java.net.URL;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * @author Rob Nielsen
 * @since 1.8.0
 */
public class GetStationsDataRequestStub extends  GetStationsDataRequest {

	protected static final String ACCESS_TOKEN = "test-access-token";

	public static GetStationsDataRequestStub createRequest(final String resource)
			throws Exception {
		return new GetStationsDataRequestStub(resource);
	}

	private final String response;

	private String content;

	private GetStationsDataRequestStub(final String response) throws Exception {
		super(ACCESS_TOKEN);

		final URL resource = getClass().getResource(response);

		if (resource == null) {
			throw new IOException("Resource '" + response + "' not found!");
		}

		this.response = Resources.toString(resource, Charsets.UTF_8);
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