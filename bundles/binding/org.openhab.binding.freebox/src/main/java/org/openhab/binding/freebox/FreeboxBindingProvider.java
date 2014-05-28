/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.freebox;

import org.openhab.core.binding.BindingProvider;

/**
 * @author clinique
 * @since 1.5.0
 */
public interface FreeboxBindingProvider extends BindingProvider {
	
    /**
     * @return the binding config to the given <code>itemName</code>
     */
	public FreeboxBindingConfig getConfig(String itemName);
}
