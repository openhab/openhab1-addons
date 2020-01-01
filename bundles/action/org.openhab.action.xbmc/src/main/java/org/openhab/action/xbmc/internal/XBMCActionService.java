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
package org.openhab.action.xbmc.internal;

import org.openhab.core.scriptengine.action.ActionService;

/**
 * This class registers an OSGi service for the XBMC action.
 *
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public class XBMCActionService implements ActionService {

    /**
     * Indicates whether this action is properly configured which means all
     * necessary configurations are set. This flag can be checked by the
     * action methods before executing code.
     */
    /* default */ static boolean isProperlyConfigured = false;

    public XBMCActionService() {
    }

    public void activate() {
    }

    public void deactivate() {
        // deallocate Resources here that are no longer needed and
        // should be reset when activating this binding again
    }

    @Override
    public String getActionClassName() {
        return XBMC.class.getCanonicalName();
    }

    @Override
    public Class<?> getActionClass() {
        return XBMC.class;
    }

}
