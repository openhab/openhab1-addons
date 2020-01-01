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
package org.openhab.binding.samsungac;

import org.openhab.binding.samsungac.internal.CommandEnum;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Samsung AC devices.
 *
 * @author Stein Tore Tøsse
 * @since 1.6.0
 */
public interface SamsungAcBindingProvider extends BindingProvider {
    String getAirConditionerInstance(String itemname);

    CommandEnum getProperty(String itemname);

    String getItemName(String acName, CommandEnum property);
}
