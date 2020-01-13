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
package org.openhab.binding.myq.internal;

import java.util.List;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.types.State;

/**
 * This represents the configuration of a openHAB item that is binded to garage
 * door opener. It contains the following information:
 *
 * <ul>
 * <li>The Device index of the device returned from the MyQ AP (0 would be the
 * first device)</li>
 * <li>The type is the item type</li>
 * <ul>
 *
 * @author Scott Hanson
 * @since 1.8.0
 */
public class MyqBindingConfig implements BindingConfig {

    int deviceIndex;
    List<Class<? extends State>> acceptedDataTypes;

    String attribute;
}
