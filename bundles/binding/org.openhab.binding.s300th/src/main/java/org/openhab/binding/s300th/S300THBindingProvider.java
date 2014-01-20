/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.s300th;

import org.openhab.binding.s300th.internal.S300THGenericBindingProvider.S300THBindingConfig;
import org.openhab.binding.s300th.internal.S300THGenericBindingProvider.S300THBindingConfig.Datapoint;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Till Klocke
 * @since 1.4.0
 */
public interface S300THBindingProvider extends BindingProvider {
	public S300THBindingConfig getBindingConfigForAddressAndDatapoint(String address, Datapoint datapoint);
}
