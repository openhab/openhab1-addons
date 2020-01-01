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

import org.openhab.binding.homematic.internal.converter.state.Converter;

/**
 * Baseclass for all Homematic bindings which needs a converter.
 *
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public abstract class ValueBindingConfig extends HomematicBindingConfig {
    protected Converter<?> converter;
    protected boolean forceUpdate;
    protected double delay;

    /**
     * Returns a custom converter.
     */
    public Converter<?> getConverter() {
        return converter;
    }

    /**
     * Returns true for always sending the value to the Homematic server.
     */
    public boolean isForceUpdate() {
        return forceUpdate;
    }

    /**
     * Returns the delay in seconds.
     */
    public double getDelay() {
        return delay;
    }
}
