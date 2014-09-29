/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openpaths;

import org.openhab.binding.openpaths.internal.OpenPathsBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * A custom binding provided for OpenPaths integration, to allow presence
 * detection using the OpenPaths service.
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public interface OpenPathsBindingProvider extends BindingProvider {

	/**
	 * Returns the config specified for item {@code itemName}.
	 */
	public OpenPathsBindingConfig getItemConfig(String itemName);
}
