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
package org.openhab.action.openwebif.internal.impl.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Simple HostnameVerifier used in SSL connection that allows everything.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class AllowAllHostnameVerifier implements HostnameVerifier {

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        return "ALLOW_ALL";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean verify(final String arg0, final SSLSession arg1) {
        return true;
    }

}