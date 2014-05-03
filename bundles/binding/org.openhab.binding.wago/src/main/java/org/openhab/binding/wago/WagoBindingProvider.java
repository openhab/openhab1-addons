/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wago;

import org.openhab.binding.wago.internal.WagoGenericBindingProvider.WagoBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Kaltofen
 * @since 1.5.0
 */
public interface WagoBindingProvider extends BindingProvider {
	/*
	 * static final public String TYPE_COIL = "coil"; static final public String
	 * TYPE_PWM = "pwm";
	 * 
	 * static final String[] WAGO_DATA_TYPES = { TYPE_COIL, TYPE_PWM };
	 */

	WagoBindingConfig getConfig(String itemName);
}
