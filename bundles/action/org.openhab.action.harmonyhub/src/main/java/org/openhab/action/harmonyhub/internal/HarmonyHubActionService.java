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
package org.openhab.action.harmonyhub.internal;

import org.openhab.core.scriptengine.action.ActionService;
import org.openhab.io.harmonyhub.HarmonyHubGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class registers an OSGi service for the HarmonyHub action.
 *
 * @author Matt Tucker
 * @since 1.7.0
 */
public class HarmonyHubActionService implements ActionService {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(HarmonyHubActionService.class);

    private static HarmonyHubGateway harmonyHubGateway;

    public HarmonyHubActionService() {
    }

    public void activate() {
    }

    public void deactivate() {
    }

    @Override
    public String getActionClassName() {
        return HarmonyHub.class.getCanonicalName();
    }

    @Override
    public Class<?> getActionClass() {
        return HarmonyHub.class;
    }

    public static boolean isProperlyConfigured() {
        return harmonyHubGateway.isProperlyConfigured();
    }

    public synchronized void addHarmonyHubGateway(HarmonyHubGateway harmonyHubGateway) {
        HarmonyHubActionService.harmonyHubGateway = harmonyHubGateway;
    }

    public synchronized void removeHarmonyHubGateway(HarmonyHubGateway harmonyHubGateway) {
        if (HarmonyHubActionService.harmonyHubGateway == harmonyHubGateway) {
            HarmonyHubActionService.harmonyHubGateway = null;
        }
    }

    public static HarmonyHubGateway gateway() {
        return harmonyHubGateway;
    }
}
