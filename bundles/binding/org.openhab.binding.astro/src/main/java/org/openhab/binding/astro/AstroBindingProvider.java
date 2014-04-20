/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro;

import org.openhab.binding.astro.internal.common.AstroType;
import org.openhab.binding.astro.internal.config.AstroBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * The interface to implement to provide a binding for Astro.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public interface AstroBindingProvider extends BindingProvider {

	/**
	 * Returns the AstroBindingConfig for an item by name.
	 */
	public AstroBindingConfig getBindingFor(String itemName);

	/**
	 * Returns true, if this provider has a binding for the specified AstroType.
	 */
	public boolean providesBindingFor(AstroType astroType);

}
