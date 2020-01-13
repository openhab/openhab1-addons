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
package org.openhab.action.homematic.internal;

import java.util.Dictionary;

import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

/**
 * This class registers an OSGi service for the Homematic action.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class HomematicActionService implements ActionService, ManagedService {

    public void activate() {
    }

    public void deactivate() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getActionClassName() {
        return Homematic.class.getCanonicalName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> getActionClass() {
        return Homematic.class;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
    }

}
