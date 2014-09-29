/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openpaths.internal;

import org.openhab.core.binding.BindingConfig;

/**
 * This represents the configuration of an openHAB item that is bound to an
 * OpenPaths user/account.
 * 
 * @author Ben Jones
 * @since 1.4.0
 */
public class OpenPathsBindingConfig implements BindingConfig {
	private final String name;
	
	public OpenPathsBindingConfig(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
