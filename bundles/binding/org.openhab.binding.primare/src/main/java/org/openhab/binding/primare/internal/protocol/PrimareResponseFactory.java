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
package org.openhab.binding.primare.internal.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for Primare responses to be extended by model-specific
 * implementation classes
 * This class is used for converting Primare device messages received as byte streams
 * to PrimareReponse instances
 *
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public abstract class PrimareResponseFactory {

    private static final Logger logger = LoggerFactory.getLogger(PrimareResponseFactory.class);

    public abstract PrimareResponse getResponse(byte[] message);

}
