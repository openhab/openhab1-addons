/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.config;

import org.openhab.binding.astro.internal.common.AstroType;
import org.openhab.core.binding.BindingConfig;

/**
 * An extended BindingConfig including the AstroType.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class AstroBindingConfig implements BindingConfig {
	private AstroType type;

	/**
	 * Creates a BiningConfig for the specified AstroType.
	 */
	public AstroBindingConfig(AstroType type) {
		this.type = type;
	}

	/**
	 * Returns the AstroType.
	 */
	public AstroType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "AstroBindingConfigElement[type=" + type.toString() + "]";
	}
}
