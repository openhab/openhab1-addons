/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public FHTBindingConfig getConfigByFullAddress(String fullAddress,
			Datapoint datapoint);

	public List<FHTBindingConfig> getAllFHT80bBindingConfigs();

}
