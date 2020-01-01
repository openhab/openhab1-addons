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
package org.openhab.action.xmpp.internal;

import org.openhab.core.scriptengine.action.ActionService;

/**
 * This class registers an OSGi service for the XMPP action.
 *
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public class XMPPActionService implements ActionService {

    public XMPPActionService() {
    }

    public void activate() {
    }

    public void deactivate() {
    }

    @Override
    public String getActionClassName() {
        return XMPP.class.getCanonicalName();
    }

    @Override
    public Class<?> getActionClass() {
        return XMPP.class;
    }

}
