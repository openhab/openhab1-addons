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

import java.io.IOException;
import java.net.URL;

import org.openhab.binding.netatmo.internal.weather.GetStationsDataRequest;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

/**
 * @author Rob Nielsen
 * @since 1.8.0
 */
public class GetStationsDataRequestStub extends GetStationsDataRequest {

    protected static final String ACCESS_TOKEN = "test-access-token";

    public static GetStationsDataRequestStub createRequest(final String resource) throws Exception {
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