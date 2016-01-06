/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hms;

import org.openhab.binding.hms.internal.HMSGenericBindingProvider.HMSBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface extends BindingProvider and provides a new method for getting
 * a HMS binding configuration by address string and datapoint.
 * 
 * @author Thomas Urmann
 * @since 1.7.0
 */
public interface HMSBindingProvider extends BindingProvider {
	public HMSBindingConfig getBindingConfigForAddressAndDatapoint(
			String address, HMSBindingConfig.Datapoint datapoint);
}
