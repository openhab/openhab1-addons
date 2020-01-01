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
package org.openhab.binding.homematic.internal.config.binding;

import org.openhab.binding.homematic.internal.config.BindingAction;
import org.openhab.core.binding.BindingConfig;

/**
 * Baseclass for all Homematic bindings.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class HomematicBindingConfig implements BindingConfig {
    protected BindingAction action;

    /**
     * Returns the action of the binding.
     */
    public BindingAction getAction() {
        return action;
    }
}
