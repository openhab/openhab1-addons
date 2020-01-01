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
package org.openhab.binding.nest.internal.messages;

/**
 * Base interface for all Nest API requests.
 *
 * @author John Cocula
 * @since 1.7.0
 */
public interface Request {

    /**
     * Send this request to the Nest API. Implementations specify a more concrete {@link Request} class.
     * 
     * @return a {@link Response} containing the requested data or an error
     */
    Response execute();
}
