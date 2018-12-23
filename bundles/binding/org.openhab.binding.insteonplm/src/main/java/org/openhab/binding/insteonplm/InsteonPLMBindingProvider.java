/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm;

import org.openhab.core.binding.BindingProvider;

/**
 * Binding provider interface. Defines the methods to interact with the binding provider.
 *
 * @author Bernd Pfrommer
 * @since 1.5.0
 */
public interface InsteonPLMBindingProvider extends BindingProvider {
    /**
     * Returns the binding configuration for the item with
     * this name.
     *
     * @param itemName the name to get the binding configuration for.
     * @return the binding configuration.
     */
    public InsteonPLMBindingConfig getInsteonPLMBindingConfig(String itemName);
}
