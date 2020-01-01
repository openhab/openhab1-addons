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
package org.openhab.binding.openpaths.internal;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.model.Token;

/**
 * Simple extension class for the Scribe OAuth library
 *
 * Since we don't do authorisation requesting this doesn't need
 * to do anything - other than extend the default implementation
 *
 * @author Ben Jones
 * @since 1.4.0
 */
public class OpenPathsApi extends DefaultApi10a {
    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return null;
    }
}
