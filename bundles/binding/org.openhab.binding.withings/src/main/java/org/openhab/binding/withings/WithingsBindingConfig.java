/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.withings;

import org.openhab.binding.withings.internal.model.MeasureType;
import org.openhab.core.binding.BindingConfig;

/**
 * {@link WithingsBindingConfig} defines a binding configuration for an item.
 * The binding supports to define the {@link MeasureType}.
 * 
 * @author Dennis Nobel
 * @since 1.5.0
 */
public class WithingsBindingConfig implements BindingConfig {

	public String accountId;
	public MeasureType measureType;

	public WithingsBindingConfig(String accountId, MeasureType measureType) {
		this.accountId = accountId;
		this.measureType = measureType;
	}

	@Override
	public String toString() {
		return "WithingsBindingConfig [accountId=" + accountId + ", measureType=" + measureType + "]";
	}

}