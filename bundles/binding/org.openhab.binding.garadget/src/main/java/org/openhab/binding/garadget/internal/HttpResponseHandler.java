/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
