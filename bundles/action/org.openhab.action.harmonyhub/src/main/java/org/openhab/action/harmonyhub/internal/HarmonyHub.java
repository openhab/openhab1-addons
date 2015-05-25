/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.harmonyhub.internal;

import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class contains the methods that are made available in scripts and rules for HarmonyHub.
 * 
 * @author Matt Tucker
 * @author Dan Cunningham
 * @since 1.7.0
 */
public class HarmonyHub {
    private static final Logger logger = LoggerFactory.getLogger(HarmonyHub.class);

    @ActionDoc(text="Send a button press event to the Harmony Hub",
            returns="<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean harmonyPressButton(@ParamDoc(name="deviceId") String deviceId, @ParamDoc(name="action") String action) {
        if (!HarmonyHubActionService.isProperlyConfigured()) {
            logger.debug("HarmonyHub action is not yet configured - execution aborted!");
            return false;
        }
        HarmonyHubActionService.gateway().pressButton(deviceId, action);

        return true;
    }
    
    @ActionDoc(text="Send a button press event to the Harmony Hub",
            returns="<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean harmonyPressButton(@ParamDoc(name="qualifier") String qualifier,
    		@ParamDoc(name="deviceId") String deviceId, @ParamDoc(name="action") String action) {
        if (!HarmonyHubActionService.isProperlyConfigured()) {
            logger.debug("HarmonyHub action is not yet configured - execution aborted!");
            return false;
        }
        HarmonyHubActionService.gateway().pressButton(qualifier, deviceId, action);

        return true;
    }

    @ActionDoc(text="Notify the Harmony Hub to start an activity",
            returns="<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean harmonyStartActivity(@ParamDoc(name="activityName") int activityId) {
        if (!HarmonyHubActionService.isProperlyConfigured()) {
            logger.debug("HarmonyHub action is not yet configured - execution aborted!");
            return false;
        }
        HarmonyHubActionService.gateway().startActivity(activityId);

        return true;
    }
    
    @ActionDoc(text="Notify the Harmony Hub to start an activity",
            returns="<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean harmonyStartActivity(@ParamDoc(name="qualifier") String qualifier,
    		@ParamDoc(name="activityName") int activityId) {
        if (!HarmonyHubActionService.isProperlyConfigured()) {
            logger.debug("HarmonyHub action is not yet configured - execution aborted!");
            return false;
        }
        HarmonyHubActionService.gateway().startActivity(qualifier, activityId);

        return true;
    }

    @ActionDoc(text="Notify the Harmony Hub to start an activity",
            returns="<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean harmonyStartActivity(@ParamDoc(name="activityName") String label) {
        if (!HarmonyHubActionService.isProperlyConfigured()) {
            logger.debug("HarmonyHub action is not yet configured - execution aborted!");
            return false;
        }
        HarmonyHubActionService.gateway().startActivity(label);

        return true;
    }
    
    @ActionDoc(text="Notify the Harmony Hub to start an activity",
            returns="<code>true</code>, if successful and <code>false</code> otherwise.")
    public static boolean harmonyStartActivity(@ParamDoc(name="qualifier") String qualifier,
    		@ParamDoc(name="activityName") String label) {
        if (!HarmonyHubActionService.isProperlyConfigured()) {
            logger.debug("HarmonyHub action is not yet configured - execution aborted!");
            return false;
        }
        HarmonyHubActionService.gateway().startActivity(qualifier,label);

        return true;
    }
}
