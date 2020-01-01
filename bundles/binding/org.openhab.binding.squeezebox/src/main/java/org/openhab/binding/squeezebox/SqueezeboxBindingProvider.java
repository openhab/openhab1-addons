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
package org.openhab.binding.squeezebox;

import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Squeezebox items.
 *
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 *
 * @author Markus Wolters
 * @author Ben Jones
 * @since 1.3.0
 */
public interface SqueezeboxBindingProvider extends BindingProvider {
    SqueezeboxBindingConfig getSqueezeboxBindingConfig(String itemName);
}
