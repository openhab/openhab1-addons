/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.panstamp;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Gideon le Grange
 * @since 1.8.0
 */
public interface PanStampBindingProvider extends BindingProvider {

	/**
	 * Return the configuration for the named item
	 * 
	 * @param itemName
	 *            The item name for which to lookup the configuration.
	 * @return The configuration
	 */
	public PanStampBindingConfig<?> getConfig(String itemName);

}
