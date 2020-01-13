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
package org.openhab.binding.openpaths;

import org.openhab.binding.openpaths.internal.OpenPathsBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * A custom binding provided for OpenPaths integration, to allow presence
 * detection using the OpenPaths service.
 *
 * @author Ben Jones
 * @since 1.4.0
 */
public interface OpenPathsBindingProvider extends BindingProvider {

    /**
     * Returns the config specified for item {@code itemName}.
     */
    public OpenPathsBindingConfig getItemConfig(String itemName);
}
