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
package org.openhab.binding.garadget.internal;

/**
 * Interface to implement when receiving the results of a {@link Connection.sendCommand} call.
 *
 * @author John Cocula
 * @since 1.9.0
 */
public interface HttpResponseHandler {

    /**
     * Upon completion of an HTTP request, provide the HTTP status code and the body of the HTTP response.
     * 
     * @param statusCode
     *            the HTTP status code (200=OK)
     * @param responseBody
     *            the response body from the HTTP response as a String
     */
    public void handleResponse(int statusCode, String responseBody);
}
