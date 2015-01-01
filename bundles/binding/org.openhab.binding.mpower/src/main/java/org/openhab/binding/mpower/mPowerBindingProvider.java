/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mpower;

import org.openhab.binding.mpower.internal.mPowerBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * @author magcode
 * @since 1.0.0
 */
public interface mPowerBindingProvider extends BindingProvider {

	mPowerBindingConfig getConfigForItemName(String itemName);

	mPowerBindingConfig getConfigForAddress(String address);

}
