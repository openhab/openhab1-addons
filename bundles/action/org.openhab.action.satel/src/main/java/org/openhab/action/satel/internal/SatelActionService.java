/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.satel.internal;

import java.util.Dictionary;

import org.openhab.binding.satel.SatelCommModule;
import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

/**
 * This class registers an OSGi service for the Satel action.
 *
 * @author Krzysztof Goworek
 * @since 1.9.0
 */
public class SatelActionService implements ActionService, ManagedService {

    static SatelCommModule satelCommModule = null;

    /**
     * Indicates whether this action is properly configured which means all
     * necessary configurations are set. This flag can be checked by the
     * action methods before executing code.
     */
    /* default */ static boolean isProperlyConfigured = true;

    public SatelActionService() {
    }

    public void activate() {
    }

    public void deactivate() {
    }

    @Override
    public String getActionClassName() {
        return Satel.class.getCanonicalName();
    }

    @Override
    public Class<?> getActionClass() {
        return Satel.class;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void updated(Dictionary<String, ?> config) throws ConfigurationException {
        if (config != null) {
            // action has no configuration, we just keep this method for the future
        }
    }

    /**
     * Sets a reference to Satel communication module service.
     *
     * @param module reference to set
     */
    public static void setSatelCommModule(SatelCommModule module) {
        satelCommModule = module;
    }

    /**
     * Removes reference to Satel communication module service/
     *
     * @param module reference to remove
     */
    public static void unsetSatelCommModule(SatelCommModule module) {
        if (satelCommModule == module) {
            satelCommModule = null;
        }
    }

}
