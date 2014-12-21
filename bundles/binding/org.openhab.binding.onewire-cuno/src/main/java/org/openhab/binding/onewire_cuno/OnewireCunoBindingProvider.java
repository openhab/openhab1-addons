/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire_cuno;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Robert Delbrück
 * @since 1.6.0
 */
public interface OnewireCunoBindingProvider extends BindingProvider {

	public OnewireCunoBindingConfig getConfigForItemName(String itemName);

	public OnewireCunoBindingConfig getConfigForAddress(String address);

}
