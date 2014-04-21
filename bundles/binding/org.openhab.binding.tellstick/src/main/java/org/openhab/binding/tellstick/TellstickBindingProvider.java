/**
 * Copyright (c) 2013-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick;

import java.util.EventListener;

import org.openhab.binding.tellstick.internal.device.SupportedMethodsException;
import org.openhab.binding.tellstick.internal.device.TellstickDevice;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface provides the methods for getting the openhab configuration
 * for the tellstick binding.
 * @author jarlebh
 * @since 1.5.0
 */
public interface TellstickBindingProvider extends BindingProvider {
	TellstickBindingConfig getTellstickBindingConfig(String itemName);
	
	TellstickBindingConfig getTellstickBindingConfig(int id, TellstickValueSelector valueSel);
	
	TellstickDevice getDevice(String itemName);
	
	void addListener(EventListener changeListener);
	
	void resetTellstickListener() throws SupportedMethodsException;

	void removeTellstickListener();
}
