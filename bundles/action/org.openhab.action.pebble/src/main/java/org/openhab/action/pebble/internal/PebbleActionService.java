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
package org.openhab.action.pebble.internal;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class registers an OSGi service for the Pebble action.
 *
 * @author Jeroen Idserda
 * @since 1.9.0
 */
public class PebbleActionService implements ActionService {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(PebbleActionService.class);

    @SuppressWarnings("unused")
    private BundleContext bundleContext;

    static boolean isProperlyConfigured = false;

    public PebbleActionService() {
    }

    public void activate(final BundleContext bundleContext, final Map<String, Object> configuration) {
        this.bundleContext = bundleContext;
        configureAction(configuration);
    }

    public void modified(final Map<String, Object> configuration) {
        configureAction(configuration);
    }

    public void deactivate(final int reason) {
        this.bundleContext = null;
    }

    @Override
    public String getActionClassName() {
        return Pebble.class.getCanonicalName();
    }

    @Override
    public Class<?> getActionClass() {
        return Pebble.class;
    }

    private void configureAction(final Map<String, Object> configuration) {
        for (Entry<String, Object> entry : configuration.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();

            String[] parts = key.split("\\.");
            if (parts.length == 2) {
                String name = parts[0].trim();
                if (!StringUtils.isBlank(name) && !name.equals("component") && !name.equals("service")) {
                    String property = parts[1].trim();

                    PebbleInstance instance = Pebble.getInstance(name);
                    if (instance == null) {
                        instance = new PebbleInstance(name);
                    }

                    if (property.equals("token")) {
                        instance.setToken(value);
                    }

                    Pebble.setInstance(instance);
                }
            }
        }

        isProperlyConfigured = true;
    }

}
