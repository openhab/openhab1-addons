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
package org.openhab.action.mios.internal;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.xtext.xbase.lib.Pair;
import org.openhab.binding.mios.MiosActionProvider;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides static methods, for invocation of MiOS Action and Scene requests, for use in automation rules.
 *
 * @author Mark Clark
 * @since 1.7.0
 */
public class MiosAction {

    private static final Logger logger = LoggerFactory.getLogger(MiosAction.class);

    /**
     * Sends an Action invocation to a Device at a MiOS Unit, without parameters.
     */
    @ActionDoc(text = "Sends an Action invocation to a Device at a MiOS Unit, without parameters.")
    public static boolean sendMiosAction(
            @ParamDoc(name = "item", text = "The Item used to determine the MiOS Unit Address information for sending the Action call.") Object item,
            @ParamDoc(name = "action", text = "The Action string to be remotely invoked on the MiOS Unit.") String actionName) {

        return sendMiosActionInternal(item, actionName, null);
    }

    /**
     * Sends an Action invocation to a Device at a MiOS Unit, without parameters.
     */
    @ActionDoc(text = "Sends an Action invocation to a Device at a MiOS Unit, without parameters.")
    public static boolean sendMiosAction(
            @ParamDoc(name = "item", text = "The Item used to determine the MiOS Unit Address information for sending the Action call.") String itemName,
            @ParamDoc(name = "action", text = "The Action string to be remotely invoked on the MiOS Unit.") String actionName) {

        return sendMiosActionInternal(itemName, actionName, null);
    }

    /**
     * Sends an Action invocation to a Device at a MiOS Unit, with parameters.
     */
    @ActionDoc(text = "Sends an Action invocation to a Device at a MiOS Unit, with parameters.")
    public static boolean sendMiosAction(
            @ParamDoc(name = "item", text = "The Item used to determine the MiOS Unit Address information for sending the Action call.") Object item,
            @ParamDoc(name = "actionName", text = "The Action string to be remotely invoked on the MiOS Unit.") String actionName,
            @ParamDoc(name = "params", text = "The list of Action Parameters.") List<Pair> params) {

        return sendMiosActionInternal(item, actionName, params);
    }

    /**
     * Sends an Action invocation to a Device at a MiOS Unit, with parameters.
     */
    @ActionDoc(text = "Sends an Action invocation to a Device at a MiOS Unit, with parameters.")
    public static boolean sendMiosAction(
            @ParamDoc(name = "item", text = "The Item used to determine the MiOS Unit Address information for sending the Action call.") String itemName,
            @ParamDoc(name = "actionName", text = "The Action string to be remotely invoked on the MiOS Unit.") String actionName,
            @ParamDoc(name = "params", text = "The list of Action Parameters.") List<Pair> params) {

        return sendMiosActionInternal(itemName, actionName, params);
    }

    /**
     * Sends a Scene invocation to a MiOS Unit.
     */
    @ActionDoc(text = "Sends a Scene invocation to a MiOS Unit.")
    public static boolean sendMiosScene(
            @ParamDoc(name = "item", text = "The Item used to determine the MiOS Unit Address information for sending the Action call.") Object item) {

        return sendMiosSceneInternal(item);
    }

    /**
     * Sends a Scene invocation to a MiOS Unit.
     */
    @ActionDoc(text = "Sends a Scene invocation to a MiOS Unit.")
    public static boolean sendMiosScene(
            @ParamDoc(name = "item", text = "The Item used to determine the MiOS Unit Address information for sending the Action call.") String itemName) {

        return sendMiosSceneInternal(itemName);
    }

    private static MiosActionProvider getActionProviderInternal(String itemName) throws Exception {
        MiosActionService service = MiosActionService.getMiosActionService();
        if (service == null) {
            throw new Exception(
                    String.format("MiOS Service is not configured, Action for Item %1$s not queued.", itemName));
        }

        MiosActionProvider actionProvider = service.getMiosActionProvider();
        if (actionProvider == null) {
            throw new Exception(String
                    .format("MiOS Action Provider is not configured, Action for Item %1$s not queued.", itemName));
        }

        return actionProvider;
    }

    private static String getName(Object item) throws Exception {
        Method method = item.getClass().getMethod("getName");
        return (String) method.invoke(item);
    }

    private static boolean sendMiosActionInternal(Object item, String actionName, List<Pair> params) {
        try {
            // Both OH 1 and ESH have a "getName" method.
            return sendMiosActionInternal(getName(item), actionName, params);
        } catch (Exception e) {
            logger.error("An unexpected error occurred using sendMiosAction: {}", e);
            return false;
        }
    }

    private static boolean sendMiosActionInternal(String itemName, String actionName, List<Pair> params) {
        try {
            logger.debug("Attempting to invoke MiOS Action {} using Item {} and {} Parameters",
                    new Object[] { actionName, itemName, (params == null) ? 0 : params.size() });
            ArrayList<Entry<String, Object>> paramList;

            // Convert from XText to the form needed to invoke MiOS.
            if (params != null) {
                paramList = new ArrayList<Entry<String, Object>>(params.size());

                for (Pair<String, String> p : params) {
                    logger.trace("Type of parameter key={} value={}", p.getKey(), p.getValue());
                    paramList.add(new AbstractMap.SimpleImmutableEntry<String, Object>(p.getKey(), p.getValue()));
                }
            } else {
                paramList = null;
            }

            MiosActionProvider actionProvider = getActionProviderInternal(itemName);

            return actionProvider.invokeMiosAction(itemName, actionName, paramList);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }

    private static boolean sendMiosSceneInternal(Object item) {
        try {
            // Both OH 1 and ESH have a "getName" method.
            return sendMiosSceneInternal(getName(item));
        } catch (Exception e) {
            logger.error("An unexpected error occurred using sendMiosScene: {}", e);
            return false;
        }
    }

    private static boolean sendMiosSceneInternal(String itemName) {
        try {
            logger.debug("Attempting to invoke MiOS Scene using Item {}", itemName);

            MiosActionProvider actionProvider = getActionProviderInternal(itemName);

            return actionProvider.invokeMiosScene(itemName);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return false;
        }
    }
}
