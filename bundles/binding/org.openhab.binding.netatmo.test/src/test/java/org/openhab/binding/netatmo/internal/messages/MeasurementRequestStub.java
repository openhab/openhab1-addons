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
public class MeasurementRequestStub extends MeasurementRequest {

	protected static final String ACCESS_TOKEN = "000000000000000000000000|11111111111111111111111111111111";

	protected static final String DEVICE_ID = "70:ee:50:00:02:20";

	protected static final String MODULE_ID = "02:00:00:00:02:a0";

	public static MeasurementRequestStub createRequest(final String resource)
			throws Exception {
		return new MeasurementRequestStub(resource);
	}

	private final String response;

	private String query;

	private MeasurementRequestStub(final String response) throws Exception {
		super(ACCESS_TOKEN, DEVICE_ID, MODULE_ID);

		final URL resource = getClass().getResource(response);

		if (resource == null) {
			throw new IOException("Resource '" + response + "' not found!");
		}

		this.response = Resources.toString(resource, UTF_8);

	}

	public String getQuery() {
		return this.query;
	}

	@Override
	protected String executeQuery(final String query) {
		this.query = query;

		return this.response;
	}

}
