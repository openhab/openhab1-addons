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
package org.openhab.binding.primare.internal.protocol.spa20;

import org.openhab.binding.primare.internal.protocol.PrimareResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for creating Primare SP31.7/SP31/SPA20/SPA21 responses
 * from data received from the Primare device
 *
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public class PrimareSPA20ResponseFactory extends PrimareResponseFactory {

    private static final Logger logger = LoggerFactory.getLogger(PrimareSPA20ResponseFactory.class);

    @Override
    public PrimareSPA20Response getResponse(byte[] message) {
        return new PrimareSPA20Response(message);
    }

}
