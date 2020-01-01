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
package org.openhab.binding.fht;

import java.util.List;

import org.openhab.binding.fht.FHTBindingConfig.Datapoint;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Till Klocke
 * @since 1.4.0
 */
public interface FHTBindingProvider extends BindingProvider {

    public FHTBindingConfig getConfigByItemName(String itemName);

    public FHTBindingConfig getConfigByFullAddress(String fullAddress, Datapoint datapoint);

    public List<FHTBindingConfig> getAllFHT80bBindingConfigs();

}
